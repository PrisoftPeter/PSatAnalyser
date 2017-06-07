package gla.prisoft.server.kernel.verification;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.ServerMemoryFactory;
import gla.prisoft.server.kernel.util.ServerAgentFactory;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.AssertionAspect;
import gla.prisoft.shared.AssertionInstance;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.KnowledgeBase;
import gla.prisoft.shared.kernel.knowledge.worlds.K1;
import gla.prisoft.shared.kernel.knowledge.worlds.World;
import gla.prisoft.shared.kernel.knowledge.worlds.*;

public class ServerAssertionsFactory implements Serializable{
	private static final long serialVersionUID = 1L;
			

	public ServerAssertionsFactory(String agentName, ServerConfigInstance sinstance){
		sinstance.agentName = agentName;
	}
	
	public static boolean init(ServerConfigInstance sinstance){
		boolean done =false;
		if(!(sinstance.agentName == null)){
			sinstance.agent = ServerAgentFactory.getAgent(sinstance.agentName, sinstance);
			done = true;
		}
		return done;
	}
	
//	public ServerAssertionsFactory(String agentName){		
//		this.agentName = agentName;
//		
//	}
//	
//	public void init(String sessionid){
//		agent = ServerAgentFactory.getAgent(agentName, sessionid);
//	}
	
	public static World[] retrieveAspectPicks(String agentName, ServerConfigInstance sinstance,ConfigInstance instance){
		
		World []aspectpicks = new World[0];
		String folderName2 = sinstance.assertionAspectsStorePath;
		
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				Agent self = ServerAgentFactory.getAgent(agentName, sinstance);
				Agent agent1 = new Agent("a1");
				Agent agent2 = new Agent("a2");
						
				Attribute h = new Attribute();
				h.setSubjectName(instance.sourceAgentName);
				h.setKey("f");
				Random rand = new Random();
				int val1 = rand.nextInt(10) + 1;
				h.setValue(""+val1);		
				self.addToPersonalAttributes(h);
				
				for (File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					
					if(instance.is_aspect_run){
						if(w instanceof K1){
							w = new K1(self, h);
						}
						else if(w instanceof K1a){
							w = new K1a(self, h);
						}
						else if(w instanceof K1b){
							w = new K1b(self, h);	
						}
						else if(w instanceof K21){
							w = new K21(self, agent1, h);
						}
						else if(w instanceof K21a){
							w = new K21a(self, agent1, h);	
						}
						else if(w instanceof K21b){
							w = new K21b(self, agent1, h);	
						}
						else if(w instanceof K22){
							w = new K22(self, agent2, h);	
						}
						else if(w instanceof K22a){
							w = new K22a(self, agent2, h);	
						}
						else if(w instanceof K22b){
							w = new K22b(self, agent2, h);	
						}	
						else if(w instanceof K23){
							w = new K23(self, agent1, h);	
						}
						else if(w instanceof K23a){
							w = new K23a(self, agent1, h);	
						}
						else if(w instanceof K23b){
							w = new K23b(self, agent1, h);	
						}
						else if(w instanceof K24){
							w = new K24(self, agent2, h);
						}
						else if(w instanceof K24a){
							w = new K24a(self, agent2, h);	
						}
						else if(w instanceof K24b){
							w = new K24b(self, agent2, h);	
						}
						else if(w instanceof K31){
							w = new K31(self, agent1, h);
						}
						else if(w instanceof K31a){
							w  = new K31a(self, agent1, h);
						}
						else if(w instanceof K31b){
							w = new K31b(self, agent1, h);		
						}
						else if(w instanceof K32){
							w = new K32(self, agent2, h);	
						}
						else if(w instanceof K32a){
							w  = new K32a(self, agent2, h);
						}
						else if(w instanceof K32b){
							w = new K32b(self, agent2, h);	
						}
						else if(w instanceof K41){
							w = new K41(self, agent1, agent2, h);
						}
						else if(w instanceof K41b){
							w =new K41b(self, agent1, agent2, h);
						}
						else if(w instanceof K41a){
							w = new K41a(self, agent1, agent2, h);	
						}
						else if(w instanceof K42){
							w = new K42(self, agent1, agent2, h);	
						}
						else if(w instanceof K42a){
							w = new K42a(self, agent1, agent2, h);
						}
						else if(w instanceof K42b){
							w = new K42b(self, agent1, agent2, h);	
						}	
						
						World [] temp = new World[aspectpicks.length+1];
						for(int i=0;i <aspectpicks.length;i++){
							temp[i] = aspectpicks[i];
						}
						temp[aspectpicks.length] = w;
						aspectpicks = temp;						
					}
					in.close();
					fileIn.close();
			    }
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readAspectPicks");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("World class not found");
		}
		return aspectpicks;
	}
	
	public Properties[] displayAssertionsStore(String agentName, String partialPath, ServerConfigInstance sinstance,ConfigInstance instance){

		Properties [] properties = new Properties[0];
		sinstance.a_counter = 1;
		
		String folderName2 = "";
		if(!instance.is_aspect_run){
			Agent a = ServerAgentFactory.getAgent(agentName, sinstance);
			if(!a.containedInMemoryStores(instance.sourceAgentName)){
				ServerMemoryFactory.newMemoryStore(a.getAgentName(), sinstance, instance);
			}
			folderName2 = PSatAPI.datastore_file_path+"/"+sinstance.sessionid+"/assertions/"+agentName+"/"+partialPath;
		}
		else{
			folderName2 = sinstance.assertionAspectsStorePath;
		}
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				Agent self = ServerAgentFactory.getAgent(agentName, sinstance);
				Agent agent1 = new Agent("a1");
				Agent agent2 = new Agent("a2");
				String aspectType ="";
						
				Attribute h = new Attribute();
				h.setSubjectName(instance.sourceAgentName);
				h.setKey("f");
				Random rand = new Random();
				int val1 = rand.nextInt(10) + 1;
				h.setValue(""+val1);		
				self.addToPersonalAttributes(h);
				
				for (File fileEntry : folder2.listFiles()) {
					
					Properties rowproperties = new Properties();
					
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					
					if(instance.is_aspect_run){
						if(w instanceof K1){
							w = new K1(self, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K1a){
							w = new K1a(self, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K1b){
							w = new K1b(self, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K21){
							w = new K21(self, agent1, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K21a){
							w = new K21a(self, agent1, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K21b){
							w = new K21b(self, agent1, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K22){
							w = new K22(self, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K22a){
							w = new K22a(self, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K22b){
							w = new K22b(self, agent2, h);	
							aspectType = w.htmlType;
						}	
						else if(w instanceof K23){
							w = new K23(self, agent1, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K23a){
							w = new K23a(self, agent1, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K23b){
							w = new K23b(self, agent1, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K24){
							w = new K24(self, agent2, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K24a){
							w = new K24a(self, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K24b){
							w = new K24b(self, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K31){
							w = new K31(self, agent1, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K31a){
							w  = new K31a(self, agent1, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K31b){
							w = new K31b(self, agent1, h);		
							aspectType = w.htmlType;
						}
						else if(w instanceof K32){
							w = new K32(self, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K32a){
							w  = new K32a(self, agent2, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K32b){
							w = new K32b(self, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K41){
							w = new K41(self, agent1, agent2, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K41b){
							w =new K41b(self, agent1, agent2, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K41a){
							w = new K41a(self, agent1, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K42){
							w = new K42(self, agent1, agent2, h);	
							aspectType = w.htmlType;
						}
						else if(w instanceof K42a){
							w = new K42a(self, agent1, agent2, h);
							aspectType = w.htmlType;
						}
						else if(w instanceof K42b){
							w = new K42b(self, agent1, agent2, h);	
							aspectType = w.htmlType;
						}	
					}
					
					boolean checked = false;
					double goal_v = -1;
					if(!instance.is_aspect_run){
						for(AssertionInstance assertion: sinstance.agent.getAssertionInstances()){
							if(assertion !=null && w.toHtmlString().equals(assertion.getAssertion())){
								checked = true;
								goal_v = assertion.getGoalv();
								break;
							}
						}
					}
					else{
						String atype = "<html>"+aspectType+"</html>";
						if(self.aspectExist(agentName, atype, instance.knowledgeBase)){
							String [] zoneAgents = null;
							zoneAgents = new String[sinstance.pathAgentNames.size()];
							int i=0;
							for(String an: sinstance.pathAgentNames){
								zoneAgents[i] = an;
								i = i+1;		        			
							}   
							goal_v = self.getAspectVGoal(agentName, atype, instance.knowledgeBase);
			        		AssertionAspect anew = new AssertionAspect(agentName, atype, zoneAgents,instance.knowledgeBase, goal_v);
			        		
			        		self.addAspect(anew);
			        		ServerAgentFactory.writeAgent(self, sinstance);
			        		
							checked = true;
						}						
					}
					if(instance.is_aspect_run){
						String meaning = "";
						String genericFormula = "";
						if(instance.knowledgeBase == null){
							instance.knowledgeBase = KnowledgeBase.SENDER;
						}
						if(instance.knowledgeBase.equals(KnowledgeBase.SUBJECT)){
							meaning = w.getGenericMeaning("[su]", "[s]", "[r]");
							genericFormula = w.getGenericFormula("su", "s", "r");
						}
						else if(instance.knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
							meaning = w.getGenericMeaning("[r]", "[s]", "[su]");
							genericFormula = w.getGenericFormula("r", "s", "su");
						}
						else if(instance.knowledgeBase.equals(KnowledgeBase.SENDER)){
							meaning = w.getGenericMeaning("[s]", "[su]", "[r]");
							genericFormula = w.getGenericFormula("s", "su", "r");
						}
						
						//update checked
						if(checked){
							meaning = "<html><font color='red'>"+meaning+"</font></html>";
							genericFormula = "<html><font color='red'>"+genericFormula+"</font></html>";
						}
						else{
							meaning = "<html>"+meaning+"</html>";
							genericFormula= "<html>"+genericFormula+"</html>";
						}
						
						if(goal_v == -1){
							goal_v = self.getGlobalPrivacyGoal_v();
						}
						
						rowproperties.setProperty("aspectType", "<html>"+aspectType+"</html>");
						rowproperties.setProperty("checked", ""+checked);
						rowproperties.setProperty("genericFormula", genericFormula);
						rowproperties.setProperty("goalv", ""+goal_v);
						rowproperties.setProperty("meaning", meaning);
					}
					else{
						String meaning = w.getMeaning();
						if(checked){
							meaning = "<html><font color='red'>"+meaning+"</font></html>";
						}
						else{
							meaning = "<html>"+meaning+"</html>";
						}
						
						if(goal_v == -1){
							goal_v = self.getGlobalPrivacyGoal_v();
						}

						rowproperties.setProperty("a_counter", ""+sinstance.a_counter);
						rowproperties.setProperty("checked", ""+checked);
						rowproperties.setProperty("w", w.toHtmlString());
						rowproperties.setProperty("goalv", ""+goal_v);
						rowproperties.setProperty("meaning", meaning);
					}		
					
					Properties [] tempproperties = new Properties[properties.length +1];
					for(int i=0;i< properties.length;i++){
						tempproperties[i] = properties[i];
					}
					tempproperties[properties.length] = rowproperties;
					properties = tempproperties;
					tempproperties = null;
										
//					av.model.fireTableDataChanged();  TODO: move to client
//					Rectangle cellBounds = av.table.getCellRect(av.table.getRowCount() - 1, 0, true);
//					av.table.scrollRectToVisible(cellBounds);
					
					sinstance.a_counter = sinstance.a_counter+1;
					in.close();
					fileIn.close();
			    }
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @displayAssertionsStore");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("class not found @displayAssertionsStore");
		}
		
		return properties;
			
	}
	
	public static World getAssertionInstanceWorld(String httpstring, String selfAgentName, String partialPath, String sessionid){
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertions/"+selfAgentName+"/"+partialPath;
		World assertion = null;
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				for (File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					if(w.toHtmlString().equals(httpstring)){
						assertion = w;
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
		return assertion;
	}
	
	public static World getAssertionAspect(String selfAgentName, String httpstring, 
										  ServerConfigInstance sinstance,ConfigInstance instance){
		String folderName2 = PSatAPI.datastore_file_path+"/"+sinstance.sessionid+"/assertionAspects/"+selfAgentName;
		
		//default agents
		Agent self = ServerAgentFactory.getAgent(selfAgentName, sinstance);
		Agent agent1 = new Agent("a1");
		Agent agent2 = new Agent("a2");
				
		Attribute h = new Attribute();
		h.setSubjectName(instance.sourceAgentName);
		h.setKey("f");
		Random rand = new Random();
		int val1 = rand.nextInt(10) + 1;
		h.setValue(""+val1);		
		self.addToPersonalAttributes(h);
				
		World assertion = null;
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){				
				for (File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					if(w.toHtmlString().equals(httpstring)){
												
						if(w instanceof K1){
							assertion = new K1(self, h);
						}
						else if(w instanceof K1a){
							assertion = new K1a(self, h);						
						}
						else if(w instanceof K1b){
							assertion = new K1b(self, h);					
						}
						else if(w instanceof K21){
							assertion = new K21(self, agent1, h);						
						}
						else if(w instanceof K21a){
							assertion = new K21a(self, agent1, h);						
						}
						else if(w instanceof K21b){
							assertion = new K21b(self, agent1, h);						
						}
						else if(w instanceof K22){
							assertion = new K22(self, agent2, h);						
						}
						else if(w instanceof K22a){
							assertion = new K22a(self, agent2, h);						
						}
						else if(w instanceof K22b){
							assertion = new K22b(self, agent2, h);						
						}						
						else if(w instanceof K23){
							assertion = new K23(self, agent1, h);					
						}
						else if(w instanceof K23a){
							assertion = new K23a(self, agent1, h);						
						}
						else if(w instanceof K23b){
							assertion = new K23b(self, agent1, h);						
						}
						else if(w instanceof K24){
							assertion = new K24(self, agent2, h);						
						}
						else if(w instanceof K24a){
							assertion = new K24a(self, agent2, h);						
						}
						else if(w instanceof K24b){
							assertion = new K24b(self, agent2, h);						
						}
						else if(w instanceof K31){
							assertion = new K31(self, agent1, h);						
						}
						else if(w instanceof K31a){
							assertion  = new K31a(self, agent1, h);
						}
						else if(w instanceof K31b){
							assertion = new K31b(self, agent1, h);						
						}
						else if(w instanceof K32){
							assertion = new K32(self, agent2, h);						
						}
						else if(w instanceof K32a){
							assertion  = new K32a(self, agent2, h);
						}
						else if(w instanceof K32b){
							assertion = new K32b(self, agent2, h);						
						}
						else if(w instanceof K41){
							assertion = new K41(self, agent1, agent2, h);					
						}
						else if(w instanceof K41b){
							assertion =new K41b(self, agent1, agent2, h);					
						}
						else if(w instanceof K41a){
							assertion = new K41a(self, agent1, agent2, h);					
						}
						else if(w instanceof K42){
							assertion = new K42(self, agent1, agent2, h);						
						}
						else if(w instanceof K42a){
							assertion = new K42a(self, agent1, agent2, h);						
						}
						else if(w instanceof K42b){
							assertion = new K42b(self, agent1, agent2, h);						
						}
						
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
		return assertion;
	}
		
	
	public boolean isUncertainWorld(World w){
		
		if(w instanceof K1){
			return true;
		}
		else if(w instanceof K21){
			return true;						
		}
		else if(w instanceof K22){
			return true;						
		}
		else if(w instanceof K23){
			return true;					
		}
		else if(w instanceof K24){
			return true;						
		}
		else if(w instanceof K31){
			return true;						
		}
		else if(w instanceof K32){
			return true;						
		}
		else if(w instanceof K41){
			return true;					
		}
		else if(w instanceof K42){
			return true;						
		}
		else{
			return false;
		}
	}
	
	
}
