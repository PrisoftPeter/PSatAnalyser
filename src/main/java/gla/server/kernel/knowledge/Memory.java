package gla.server.kernel.knowledge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;

import gla.client.session.ClientServerBroker;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.KnowledgeLevel;
import gla.server.PSatAPI;
import gla.server.kernel.behaviour.InformationFlows;
import gla.server.kernel.knowledge.worlds.*;
import gla.server.kernel.util.AgentFactory;
import gla.server.kernel.util.ServerAgentFactory;
import gla.server.session.ServerConfigInstance;

public class Memory implements Serializable{	
	private static final long serialVersionUID = 1L;
	private Agent [] otheragents;
	private Agent self;
	private String subjectName;
	
	private Attribute tempAttribute;
	private String memoryStorePath;
	private String assertionsStorePath;
	

	public Memory(Agent self,String subjectName,ConfigInstance instance){
		this.self = self;
		this.subjectName = subjectName;
		otheragents = new Agent[0];
		setOtherAgents(instance);
		createMemoryStorePath(instance.sessionid);
		createAssertionsStorePath(instance.sessionid);
	}
		
	public void resetKnowledge(ServerConfigInstance instance){
		removeAllWorldsFromMemoryStore();
		removeAllWorldsFromAssertionsStore();
		
		createMemoryStorePath(instance.sessionid);
		createAssertionsStorePath(instance.sessionid);
		
		Agent subject =ServerAgentFactory.getAgent(subjectName, instance);
		if(subject != null){
			for(Attribute attribute: subject.getPersonalAttributes()){
				tempAttribute = attribute;
				
		        int r = 2;
		        int n = otheragents.length;
		        runAgentCombination(otheragents, n, r);
			}
		}
				
	}
	
	private boolean substituteEntropyLearning(World oldworld, World newworld, String processName){
		boolean removed = removeWorldFromMemoryStore(oldworld);
		String info = processName+" transaction memory update passed: "+oldworld.toLimitHtmlString()+"&#8594;"+newworld.toLimitHtmlString();

		if(removed){
			addToMemoryStore(newworld);
			if(InformationFlows.successfulSubs !=null){
				String temp[] = new String[InformationFlows.successfulSubs.length+1];
				for(int i =0;i<InformationFlows.successfulSubs.length;i++){
					temp[i] = InformationFlows.successfulSubs[i];
				}
				temp[InformationFlows.successfulSubs.length] = info;
				
				InformationFlows.successfulSubs = temp;
				InformationFlows.successfulSubsCount = InformationFlows.successfulSubsCount +1; 
			}
			InformationFlows.successfulSubsCountLearning = InformationFlows.successfulSubsCountLearning +1;
						
			return true;
		}
	
		return false;
	}
	
	public boolean substitute(World oldworld, World newworld, String processName, ConfigInstance instance){

		if(instance.learningMaxSubs){
			return substituteEntropyLearning(oldworld, newworld, processName);
		}
		
		boolean removed = removeWorldFromMemoryStore(oldworld);
		
		String info = processName+" transaction memory update passed: "+oldworld.toLimitHtmlString()+"&#8594;"+newworld.toLimitHtmlString();
				
		if(removed){
			addToMemoryStore(newworld);
			
			if(InformationFlows.successfulSubs !=null){
				String temp[] = new String[InformationFlows.successfulSubs.length+1];
				for(int i =0;i<InformationFlows.successfulSubs.length;i++){
					temp[i] = InformationFlows.successfulSubs[i];
				}
				temp[InformationFlows.successfulSubs.length] = info;
				
				InformationFlows.successfulSubs = temp;
				InformationFlows.successfulSubsCount = InformationFlows.successfulSubsCount +1; 
			}
			if(instance.log_knowledge_transformation){	
				String info0 = "<html><font color='#005ce6'>("+InformationFlows.successfulSubsCount+") "+InformationFlows.executedTransaction+" "+info+"</font></html>";
				ClientServerBroker.messageEvent("updateLogPage", info0+"₦"+false, null,null);
			}			
			return true;
		}
		if(instance.log_knowledge_transformation){
			boolean alreadySub = false;
			int subcount = 0;
			for(String s:InformationFlows.successfulSubs){
				subcount = subcount +1;
				String s1 = s.split(":")[1];
				String s2 = info.split(":")[1];
				if(s1.equals(s2)){
					alreadySub = true;
					break;
				}
				
			}
			
			String info1 = processName+" transaction memory update failed: "+oldworld.toLimitHtmlString()+"&#8594;"+newworld.toLimitHtmlString();
			if(alreadySub){
				info1 = info1 +" previously executed in ("+subcount+")";
			}
			else{
				info1 = info1 +" not found";
			}
			info1 = "<html>"+InformationFlows.executedTransaction+"-"+info1+"</html>";
			ClientServerBroker.messageEvent("updateLogPage", info1+"₦"+false, null,null);
		}		
		return false;
	}
	
	private void fillMemory(Agent [] pair){
		
		Agent agent1 = pair[0];
		Agent agent2 = pair[1];
		
//		if(PSatAPI.instance.collectiveStrategy != CollectiveStrategy.NONE){
//			addToAssertionsStore(new K0a(tempAttribute));
//			addToAssertionsStore(new K0(tempAttribute));
//		}
		
//		addToAssertionsStore(new K0a(tempAttribute));
//		addToAssertionsStore(new K0(tempAttribute));
		
		addToMemoryStore(new K1(self, tempAttribute));
		addToAssertionsStore(new K1a(self, tempAttribute));
		addToAssertionsStore(new K1(self, tempAttribute));
		
		if(!self.getAgentName().equals(agent1.getAgentName())){
			addToMemoryStore(new K23(self, agent1,tempAttribute));
			
			addToAssertionsStore(new K23a(self, agent1, tempAttribute));
			addToAssertionsStore(new K23(self, agent1, tempAttribute));	
			
			addToMemoryStore(new K31(self, agent1,tempAttribute));
			addToAssertionsStore(new K31a(self, agent1, tempAttribute));
			addToAssertionsStore(new K31(self, agent1, tempAttribute));

			addToAssertionsStore(new K21a(self, agent1, tempAttribute));
			addToAssertionsStore(new K21(self, agent1, tempAttribute));
		}
		
		if(!self.getAgentName().equals(agent2.getAgentName())){
			addToMemoryStore(new K24(self, agent2,tempAttribute));
			addToAssertionsStore(new K24a(self, agent2, tempAttribute));
			addToAssertionsStore(new K24(self, agent2, tempAttribute));
			
			addToMemoryStore(new K32(self, agent2,tempAttribute));
			addToAssertionsStore(new K32a(self, agent2, tempAttribute));
			addToAssertionsStore(new K32(self, agent2, tempAttribute));
			
			addToAssertionsStore(new K22a(self, agent2, tempAttribute));
			addToAssertionsStore(new K22(self, agent2, tempAttribute));
		}
		
		if(!self.getAgentName().equals(agent1.getAgentName())){
			if(!self.getAgentName().equals(agent2.getAgentName())){
				if(!agent1.getAgentName().equals(agent2.getAgentName())){
					addToMemoryStore(new K41(self, agent1, agent2,tempAttribute));
					addToAssertionsStore(new K41a(self, agent1, agent2, tempAttribute));
					addToAssertionsStore(new K41(self, agent1, agent2, tempAttribute));
					
					addToMemoryStore(new K42(self, agent1, agent2,tempAttribute));
					addToAssertionsStore(new K42a(self, agent1, agent2, tempAttribute));
					addToAssertionsStore(new K42(self, agent1, agent2, tempAttribute));	
				}				
			}
		}
	}
			
	private void setOtherAgents(ConfigInstance instance){

		if(instance.is_dynamic_memory_store){
			for(String agentName:instance.validAgents){
				Agent agent = AgentFactory.getAgent(agentName, instance);
				if(!agent.getAgentName().equals(self.getAgentName())){
					Agent [] tempoa = new Agent[otheragents.length+1];	
					for(int i=0;i<otheragents.length;i++){
						tempoa[i] = otheragents[i];
					}
					tempoa[otheragents.length] = agent;
					otheragents = tempoa;
				}	
			}
		}
		else{
			int noagents = instance.noOfKnownAgentsGenerator.nextInt((instance.maxNoOfknowAgents - instance.minNoOfknowAgents) + 1) + instance.minNoOfknowAgents;
			int k=0;
			
			for(Agent agent: instance.agents){
				if(k<noagents){
					if(!agent.getAgentName().equals(self.getAgentName())){
						Agent [] tempoa = new Agent[otheragents.length+1];	
						for(int i=0;i<otheragents.length;i++){
							tempoa[i] = otheragents[i];
						}
						tempoa[otheragents.length] = agent;
						otheragents = tempoa;
					}	
					k = k+1;
				}			
			}	
		}		
	}
	
	private void agentCombinationUtil(Agent arr[], int n, int r, int index, Agent data[], int i) {
		if (index == r) {
			fillMemory(data);
			return;
		}

		if (i >= n)
			return;

		data[index] = arr[i];
		agentCombinationUtil(arr, n, r, index + 1, data, i + 1);
		agentCombinationUtil(arr, n, r, index, data, i + 1);
	}
	
	
    private void runAgentCombination(Agent arr[], int n, int r){
        Agent data[]=new Agent[r];
        this.agentCombinationUtil(arr, n, r, 0, data, 0);
    }
    
	private void createMemoryStorePath(String sessionid){
		String folderName0 = PSatAPI.datastore_file_path+"/"+sessionid;
		String folderName1 = PSatAPI.datastore_file_path+"/"+sessionid+"/memory";
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/memory/"+self.getAgentName();
		String folderName3 = PSatAPI.datastore_file_path+"/"+sessionid+"/memory/"+self.getAgentName()+"/"+self.getAgentName()+"_"+subjectName;
		
		File folder0 = new File(folderName0);
		boolean exist0 = false;
		if(folder0.exists()){
			if(folder0.isDirectory()){
				exist0 = true;
			}				
		}
		if(!exist0){
			folder0.mkdir();
		}
		
		File folder1 = new File(folderName1);
		boolean exist1 = false;
		if(folder1.exists()){
			if(folder1.isDirectory()){
				exist1 = true;
			}				
		}
		if(!exist1){
			folder1.mkdir();
		}
		
		File folder2 = new File(folderName2);
		boolean exist2 = false;
		if(folder2.exists()){
			if(folder2.isDirectory()){
				exist2 = true;
			}				
		}
		if(!exist2){
			folder2.mkdir();
		}
		
		File folder3 = new File(folderName3);
		boolean exist3 = false;
		if(folder3.exists()){
			if(folder3.isDirectory()){
				exist3 = true;
			}				
		}
		if(!exist3){
			folder3.mkdir();
		}
		
		memoryStorePath = folderName3;
	}
	
	private boolean addToMemoryStore(World world){		
		try{
			String fileName = memoryStorePath+"/"+world.toString();

			File if_file = new File(fileName);
	        if(if_file.exists()){
	         if_file.delete();
	        }
	        if_file.createNewFile();
	        FileOutputStream fileOut = new FileOutputStream(fileName);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(world);
	        out.close();
	        fileOut.close();
	        
//	        if(Display.verboseViewMode){
//	        	Display.updateLogPage(world.toHtmlString(), false);
//	        }
	        
	        return true;
	      }
		catch(IOException i){
	          i.printStackTrace();
	          return false;
	    }
		
	}
	
	private void removeAllWorldsFromMemoryStore(){
		try {
			FileUtils.forceDelete(new File(memoryStorePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean removeWorldFromMemoryStore(World world){

		try {
			FileUtils.forceDelete(new File(memoryStorePath+"/"+world.toString()));
			//System.out.println(world+ "removed from memory store");
			return true;
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println(world+ "not found in memory store");
			return false;
		}
	}
	
	public boolean contains(String worldDesc){
		String fileName = memoryStorePath+"/"+worldDesc;

		File if_file = new File(fileName);
		if(if_file.exists()){
			return true;
		}
		return false;
	}
	
	private World[] addWorld(World world, World worlds[]){
		World [] temp = new World[worlds.length +1];
		
		for(int i=0;i<worlds.length;i++){
			temp[i] = worlds[i];
		}
		temp[worlds.length] = world;
		worlds = temp;
		
		return worlds;
	}
	
	public World [] getUncertainties(String principalName, String reference1, String reference2){

		String folderName2 = memoryStorePath;
		World[] beliefs = new World[0];

		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				for (final File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					
					if(w instanceof K1){
						if(w.getSelf().getAgentName().equals(principalName)){
							beliefs = addWorld(w, beliefs);
						}	
					}
					else if(w instanceof K1a){
						//belief	
												
					}
					else if(w instanceof K1b){
						//belief					
					}
					else if(w instanceof K21){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}			
					}
					else if(w instanceof K21a){
						//belief					
					}
					else if(w instanceof K21b){
						//belief					
					}
					else if(w instanceof K23){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K23a){
						//belief						
					}
					else if(w instanceof K23b){
						//belief					
					}
					else if(w instanceof K24){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent2().getAgentName().equals(reference1)||w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}				
					}
					else if(w instanceof K24a){
						//belief						
					}
					else if(w instanceof K24b){
						//belief						
					}
					else if(w instanceof K31){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K31a){
						//belief					
					}
					else if(w instanceof K31b){
						//belief					
					}
					else if(w instanceof K41){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1) && w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
							else if(w.getAgent2().getAgentName().equals(reference1) && w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K41b){
						//belief					
					}
					else if(w instanceof K41a){
						//belief						
					}
					else if(w instanceof K42){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1) && w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
							else if(w.getAgent2().getAgentName().equals(reference1) && w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K42a){
						//belief						
					}
					else if(w instanceof K42b){
						//belief						
					}
								
					in.close();
					fileIn.close();
			    }
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readWorld");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("World class not found");
		}
				
		return beliefs;
	
	
	}
		
	
	public World [] getBeliefs(String principalName, String reference1, String reference2){
		String folderName2 = memoryStorePath;
		World[] beliefs = new World[0];

		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				for (final File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					
					if(w instanceof K1){
						//uncertainty element	
					}
					else if(w instanceof K1a){
						if(w.getSelf().getAgentName().equals(principalName)){
							beliefs = addWorld(w, beliefs);
						}	
												
					}
					else if(w instanceof K1b){
						if(w.getSelf().getAgentName().equals(principalName)){
							beliefs = addWorld(w, beliefs);
						}						
					}
					else if(w instanceof K21){
						//uncertainty element			
					}
					else if(w instanceof K21a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K21b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K22){
						//uncertainty element			
					}
					else if(w instanceof K22a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent2().getAgentName().equals(reference1)||w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K22b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent2().getAgentName().equals(reference1)||w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K23){
						//uncertainty					
					}
					else if(w instanceof K23a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K23b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K24){
						//uncertainty					
					}
					else if(w instanceof K24a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent2().getAgentName().equals(reference1)||w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K24b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent2().getAgentName().equals(reference1)||w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K31){
						//uncertainty						
					}
					else if(w instanceof K31a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K31b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1)||w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K32){
						//uncertainty						
					}
					else if(w instanceof K32a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent2().getAgentName().equals(reference1)||w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K32b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent2().getAgentName().equals(reference1)||w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K41){
						//uncertainty					
					}
					else if(w instanceof K41b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1) && w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
							else if(w.getAgent2().getAgentName().equals(reference1) && w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}					
					}
					else if(w instanceof K41a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1) && w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
							else if(w.getAgent2().getAgentName().equals(reference1) && w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K42){
						//uncertainty						
					}
					else if(w instanceof K42a){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1) && w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
							else if(w.getAgent2().getAgentName().equals(reference1) && w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
					else if(w instanceof K42b){
						if(w.getSelf().getAgentName().equals(principalName)){
							if(w.getAgent1().getAgentName().equals(reference1) && w.getAgent2().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
							else if(w.getAgent2().getAgentName().equals(reference1) && w.getAgent1().getAgentName().equals(reference2)){
								beliefs = addWorld(w, beliefs);
							}
						}						
					}
								
					in.close();
					fileIn.close();
			    }
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readWorld");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("World class not found");
		}
				
		return beliefs;
	
	}
	
	public boolean containsType(World world){
		String folderName2 = memoryStorePath;
		boolean contained = false;
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				for (final File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					if(w instanceof K1 && world instanceof K1){
						contained = true;
						break;						
					}
					else if(w instanceof K1a && world instanceof K1a){
						contained = true;
						break;						
					}
					else if(w instanceof K1b && world instanceof K1b){
						contained = true;
						break;						
					}
					else if(w instanceof K21 && world instanceof K21){
						contained = true;
						break;						
					}
					else if(w instanceof K21a && world instanceof K21a){
						contained = true;
						break;						
					}
					else if(w instanceof K21b && world instanceof K21b){
						contained = true;
						break;						
					}
					else if(w instanceof K23 && world instanceof K23){
						contained = true;
						break;						
					}
					else if(w instanceof K23a && world instanceof K23a){
						contained = true;
						break;						
					}
					else if(w instanceof K23b && world instanceof K23b){
						contained = true;
						break;						
					}
					else if(w instanceof K24 && world instanceof K24){
						contained = true;
						break;						
					}
					else if(w instanceof K24a && world instanceof K24a){
						contained = true;
						break;						
					}
					else if(w instanceof K24b && world instanceof K24b){
						contained = true;
						break;						
					}
					else if(w instanceof K31 && world instanceof K31){
						contained = true;
						break;						
					}
					else if(w instanceof K31a && world instanceof K31a){
						contained = true;
						break;						
					}
					else if(w instanceof K31b && world instanceof K31b){
						contained = true;
						break;						
					}
					else if(w instanceof K41 && world instanceof K41){
						contained = true;
						break;						
					}
					else if(w instanceof K41b && world instanceof K41b){
						contained = true;
						break;						
					}
					else if(w instanceof K41a && world instanceof K41a){
						contained = true;
						break;						
					}
					else if(w instanceof K42 && world instanceof K42){
						contained = true;
						break;						
					}
					else if(w instanceof K42a && world instanceof K42a){
						contained = true;
						break;						
					}
					else if(w instanceof K42b && world instanceof K42b){
						contained = true;
						break;						
					}
								
					in.close();
					fileIn.close();
			    }
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readWorld");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("World class not found");
		}
				
		return contained;
	}
	
	
	private void createAssertionsStorePath(String sessionid){
		String folderName0 = PSatAPI.datastore_file_path+"/"+sessionid;
		String folderName1 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertions";
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertions/"+self.getAgentName();
		String folderName3 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertions/"+self.getAgentName()+"/"+self.getAgentName()+"_"+subjectName;
		
		File folder0 = new File(folderName0);
		boolean exist0 = false;
		if(folder0.exists()){
			if(folder0.isDirectory()){
				exist0 = true;
			}				
		}
		if(!exist0){
			folder0.mkdir();
		}
		
		File folder1 = new File(folderName1);
		boolean exist1 = false;
		if(folder1.exists()){
			if(folder1.isDirectory()){
				exist1 = true;
			}				
		}
		if(!exist1){
			folder1.mkdir();
		}
		
		File folder2 = new File(folderName2);
		boolean exist2 = false;
		if(folder2.exists()){
			if(folder2.isDirectory()){
				exist2 = true;
			}				
		}
		if(!exist2){
			folder2.mkdir();
		}
		
		File folder3 = new File(folderName3);
		boolean exist3 = false;
		if(folder3.exists()){
			if(folder3.isDirectory()){
				exist3 = true;
			}				
		}
		if(!exist3){
			folder3.mkdir();
		}
		
		assertionsStorePath = folderName3;
	}
	
	private boolean addToAssertionsStore(World world){		
		try{
			String fileName = assertionsStorePath+"/"+world.toString();

			File if_file = new File(fileName);
	        if(if_file.exists()){
	         if_file.delete();
	        }
	        if_file.createNewFile();
	        FileOutputStream fileOut = new FileOutputStream(fileName);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        
//	        if(world instanceof K0a){
//	        	out.writeObject((K0a)world);
//			}
//	        else if(world instanceof K0){
//	        	out.writeObject((K0)world);
//			}
//	        else 
	        if(world instanceof K1){
	        	out.writeObject((K1)world);
			}
			else if(world instanceof K1a){
				out.writeObject((K1a)world);						
			}
			else if(world instanceof K1b){
				out.writeObject((K1b)world);					
			}
			else if(world instanceof K21){
				out.writeObject((K21)world);					
			}
			else if(world instanceof K21a){
				out.writeObject((K21a)world);						
			}
			else if(world instanceof K21b){
				out.writeObject((K21b)world);						
			}
			else if(world instanceof K22){
				out.writeObject((K22)world);						
			}
			else if(world instanceof K22a){
				out.writeObject((K22a)world);						
			}
			else if(world instanceof K22b){
				out.writeObject((K22b)world);					
			}	
			else if(world instanceof K23){
				out.writeObject((K23)world);					
			}
			else if(world instanceof K23a){
				out.writeObject((K23a)world);					
			}
			else if(world instanceof K23b){
				out.writeObject((K23b)world);					
			}
			else if(world instanceof K24){
				out.writeObject((K24)world);						
			}
			else if(world instanceof K24a){
				out.writeObject((K24a)world);						
			}
			else if(world instanceof K24b){
				out.writeObject((K24b)world);					
			}
			else if(world instanceof K31){
				out.writeObject((K31)world);					
			}
			else if(world instanceof K31a){
				out.writeObject((K31a)world);
			}
			else if(world instanceof K31b){
				out.writeObject((K31b)world);					
			}
			else if(world instanceof K32){
				out.writeObject((K32)world);						
			}
			else if(world instanceof K32a){
				out.writeObject((K32a)world);
			}
			else if(world instanceof K32b){
				out.writeObject((K32b)world);						
			}
			else if(world instanceof K41){
				out.writeObject((K41)world);				
			}
			else if(world instanceof K41b){
				out.writeObject((K41b)world);					
			}
			else if(world instanceof K41a){
				out.writeObject((K41a)world);				
			}
			else if(world instanceof K42){
				out.writeObject((K42)world);					
			}
			else if(world instanceof K42a){
				out.writeObject((K42a)world);					
			}
			else if(world instanceof K42b){
				out.writeObject((K42b)world);					
			}	
			else{
				System.out.println("No world type found");
			}
	        
	        out.close();
	        fileOut.close();
	        
	        return true;
	      }
		catch(IOException i){
	          i.printStackTrace();
	          return false;
	    }
		
	}
	
	
	private void removeAllWorldsFromAssertionsStore(){
		try {
			FileUtils.forceDelete(new File(assertionsStorePath));
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
		
	
	public KnowledgeLevel getCurrentKnowledgeLevel(){
		double totalElements =0;
		double atomicElements =0;
		double compositeElements=0;
		
		KnowledgeLevel kl = null;
		
		String folderName2 = memoryStorePath;
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				for (final File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					
					boolean isComposite = ServerMemoryFactory.isCompositeWorld(w);
								
					totalElements = totalElements +1;
					if(isComposite){
						compositeElements = compositeElements+1;
					}
					else{
						atomicElements = atomicElements+1;
					}
					
					in.close();
					fileIn.close();
			    }
				
				kl = new KnowledgeLevel(self.getAgentName(), null, null);
//				double p_uncertainty = (compositeElements/totalElements)*100;
//				double p_belief = (atomicElements/totalElements)*100;
				double p_uncertainty = (compositeElements/totalElements);
				double p_belief = (atomicElements/totalElements);
				
				kl.setUncertaintyLevel(p_uncertainty);
				kl.setBeliefLevel(p_belief);				
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readWorld");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("World class not found");
		}
				
		return kl;
	}
//	private void removeWorldFromAssertionsStore(World world){
//
//		try {
//			FileUtils.forceDelete(new File(assertionsStorePath+world.toString()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
