package psat.util;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

import psat.PSatAPI;
import psat.display.model.AssertionsView;
import psat.knowledge.MemoryFactory;
import psat.knowledge.worlds.*;

public class AssertionsFactory implements Serializable{
	private static final long serialVersionUID = 1L;
				
	private String agentName;

	@SuppressWarnings("unused")
	private AssertionsView av;

	public AssertionsFactory(String agentName){
		PSatAPI.instance.agentName = agentName;
		this.agentName = agentName;
	}
	
	public  boolean init(){
		boolean done =false;
		if(!(PSatAPI.instance.agentName == null)){
			PSatAPI.instance.agent = AgentFactory.getAgent(PSatAPI.instance.agentName);
			done = true;
		}
		Config.serialiseConfigInstance();

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
	
		
	public void displayAssertions(final AssertionsView av){
		
		if(PSatAPI.instance.isModePick){
			this.av = av;
			
			if(PSatAPI.instance.is_role_run){
				String partialPath = agentName+"_"+PSatAPI.instance.sourceAgentName+"_Role";
				if(partialPath != null){
					displayAssertionsStore(agentName,partialPath,av);						
				}
//				Thread queryThread2 = new Thread() {
//					public void run() {						
//						//Display.updateProgressComponent(100,"");
//					}					
//				};
//				queryThread2.start();
			}
			else{
				Agent a = PSatAPI.netGetAgent(agentName);
				if(!a.containedInMemoryStores(PSatAPI.instance.sourceAgentName)){
					PSatAPI.netNewMemoryStore(a.getAgentName());
				}						
				String [] partialPaths = PSatAPI.netGetAssertionsStorePaths(agentName);
				for(String partialPath: partialPaths){	
					if(partialPath != null){
						displayAssertionsStore(agentName,partialPath,av);							
					}
				}
//				Thread queryThread2 = new Thread() {
//					public void run() {						
						//Display.updateProgressComponent(100,"");
//					}
//				};
//				queryThread2.start();
			}
				
		}
	}
	
	private void displayAssertionsStore(String agentName, String partialPath, AssertionsView av) {
//		PSatAPI.netDeseraliseConfigInstance();
		Properties [] ppties = PSatAPI.netDisplayAssertionsStore(agentName, partialPath);
		
		if(PSatAPI.instance.is_role_run){
			for(Properties ppty: ppties){
				String roleType = ppty.getProperty("roleType");
				boolean checked = false;
				if(ppty.getProperty("checked").equals("true")){
					checked =true;
				}
				String genericFormula = ppty.getProperty("genericFormula");
				double goal_v = new Double(ppty.getProperty("goalv"));
				String meaning = ppty.getProperty("meaning");
				String collectiveStrDesc = ppty.getProperty("collectiveStrategy"); 
				
				CollectiveStrategy cs = CollectiveMode.getCollectiveStrategy(collectiveStrDesc);
				String cs_limithtmldesc = CollectiveMode.getModeLimitHtmlDesc(cs);
				if(cs != CollectiveStrategy.NONE){
					meaning = collectiveStrDesc+" "+meaning;
					meaning = meaning.replace("<html>", "");
					meaning = meaning.replace("</html>", "");
					if(checked){
						meaning = "<html><font color='red'>"+meaning+"</font></html>";
					}
					else{
						meaning = "<html>"+meaning+"</html>";
					}
					
					genericFormula = cs_limithtmldesc+"("+genericFormula+")";
					genericFormula =genericFormula.replace("<html>", "");
					genericFormula =genericFormula.replace("</html>", "");
					if(checked){
						genericFormula = "<html><font color='red'>"+genericFormula+"</font></html>";
					}
					else{
						genericFormula = "<html>"+genericFormula+"</html>";
					}
					
				}
				if(cs == CollectiveStrategy.NONE){
					if(genericFormula.equals("<html><i>f</i></html>")){
						checked = false;
					}
				}
				
				av.model.addRow(new Object[]{roleType,checked,genericFormula,goal_v,meaning});
				av.model.fireTableDataChanged();
				Rectangle cellBounds = av.table.getCellRect(av.table.getRowCount() - 1, 0, true);
				av.table.scrollRectToVisible(cellBounds);
			}
		}
		else{
			for(Properties ppty: ppties){
				String a_counter = ppty.getProperty("a_counter");
				boolean checked = false;
				if(ppty.getProperty("checked").equals("true")){
					checked =true;
				}
				String w = ppty.getProperty("w");
				double goal_v = new Double(ppty.getProperty("goalv"));
				String meaning = ppty.getProperty("meaning");
				String collectiveStrDesc = ppty.getProperty("collectiveStrategy"); 
				
				CollectiveStrategy cs = CollectiveMode.getCollectiveStrategy(collectiveStrDesc);
				String cs_limithtmldesc = CollectiveMode.getModeLimitHtmlDesc(cs);
				if(cs != CollectiveStrategy.NONE){
					meaning = collectiveStrDesc+" "+meaning;
					meaning = meaning.replace("<html>", "");
					meaning = meaning.replace("</html>", "");
					if(checked){
						meaning = "<html><font color='red'>"+meaning+"</font></html>";
					}
					else{
						meaning = "<html>"+meaning+"</html>";
					}
					
					w = cs_limithtmldesc+"("+w+")";
					w =w.replace("<html>", "");
					w =w.replace("</html>", "");
					if(checked){
						w = "<html><font color='red'>"+w+"</font></html>";
					}
					else{
						w = "<html>"+w+"</html>";
					}

				}
				if(cs == CollectiveStrategy.NONE){
					if(w.equals("<html><i>f</i></html>")){
						checked = false;
					}
				}
				
				av.model.addRow(new Object[]{a_counter,checked,w, goal_v,meaning});
				av.model.fireTableDataChanged();
				Rectangle cellBounds = av.table.getCellRect(av.table.getRowCount() - 1, 0, true);
				av.table.scrollRectToVisible(cellBounds);
			}

		}
		
	}
	
	public static World[] retrieveRolePicks(String agentName){
		
		World []rolepicks = new World[0];
		String folderName2 = PSatAPI.instance.assertionRolesStorePath;
		
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				Agent self = AgentFactory.getAgent(agentName);
				Agent agent1 = new Agent("a1");
				Agent agent2 = new Agent("a2");
						
				Attribute h = new Attribute();
				h.setSubjectName(PSatAPI.instance.sourceAgentName);
				h.setKey("f");
				Random rand = new Random();
				int val1 = rand.nextInt(10) + 1;
				h.setValue(""+val1);		
				self.addToPersonalAttributes(h);
				
				for (File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					
					if(PSatAPI.instance.is_role_run){
//						if(w instanceof K0a){
//							w = new K0a(h);
//						}
//						else if(w instanceof K0b){
//							w = new K0b(h);
//						}
//						else if(w instanceof K0){
//							w = new K0(h);
//						}
//						else 
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
						
						World [] temp = new World[rolepicks.length+1];
						for(int i=0;i <rolepicks.length;i++){
							temp[i] = rolepicks[i];
						}
						temp[rolepicks.length] = w;
						rolepicks = temp;						
					}
					in.close();
					fileIn.close();
			    }
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readRolePicks");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("World class not found");
		}
		return rolepicks;
	}
	
	public Properties[] displayAssertionsStore(String agentName, String partialPath){

		Properties [] properties = new Properties[0];
		
		
		PSatAPI.instance.a_counter = 1;
		
		String folderName2 = "";
		if(!PSatAPI.instance.is_role_run){
			Agent a = AgentFactory.getAgent(agentName);
			if(!a.containedInMemoryStores(PSatAPI.instance.sourceAgentName)){
				MemoryFactory.newMemoryStore(a.getAgentName());
			}
			folderName2 = PSatAPI.datastore_file_path+"/"+PSatAPI.instance.sessionid+"/assertions/"+agentName+"/"+partialPath;
		}
		else{
			folderName2 = PSatAPI.instance.assertionRolesStorePath;
		}
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				Agent self = AgentFactory.getAgent(agentName);
				Agent agent1 = new Agent("a1");
				Agent agent2 = new Agent("a2");
				String roleType ="";
						
				Attribute h = new Attribute();
				h.setSubjectName(PSatAPI.instance.sourceAgentName);
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
					
					if(PSatAPI.instance.is_role_run){

//						if(w instanceof K0a){
//							w = new K0a(h);
//							roleType = w.htmlType;
//						}
//						else if(w instanceof K0b){
//							w = new K0b(h);
//							roleType = w.htmlType;
//						}
//						else if(w instanceof K0){
//							w = new K0(h);
//							roleType = w.htmlType;
//						}
//						else 
						if(w instanceof K1){
							w = new K1(self, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K1a){
							w = new K1a(self, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K1b){
							w = new K1b(self, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K21){
							w = new K21(self, agent1, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K21a){
							w = new K21a(self, agent1, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K21b){
							w = new K21b(self, agent1, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K22){
							w = new K22(self, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K22a){
							w = new K22a(self, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K22b){
							w = new K22b(self, agent2, h);	
							roleType = w.htmlType;
						}	
						else if(w instanceof K23){
							w = new K23(self, agent1, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K23a){
							w = new K23a(self, agent1, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K23b){
							w = new K23b(self, agent1, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K24){
							w = new K24(self, agent2, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K24a){
							w = new K24a(self, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K24b){
							w = new K24b(self, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K31){
							w = new K31(self, agent1, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K31a){
							w  = new K31a(self, agent1, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K31b){
							w = new K31b(self, agent1, h);		
							roleType = w.htmlType;
						}
						else if(w instanceof K32){
							w = new K32(self, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K32a){
							w  = new K32a(self, agent2, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K32b){
							w = new K32b(self, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K41){
							w = new K41(self, agent1, agent2, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K41b){
							w =new K41b(self, agent1, agent2, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K41a){
							w = new K41a(self, agent1, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K42){
							w = new K42(self, agent1, agent2, h);	
							roleType = w.htmlType;
						}
						else if(w instanceof K42a){
							w = new K42a(self, agent1, agent2, h);
							roleType = w.htmlType;
						}
						else if(w instanceof K42b){
							w = new K42b(self, agent1, agent2, h);	
							roleType = w.htmlType;
						}	
						
					}
					
					boolean checked = false;
					double goal_v = -1;
					CollectiveStrategy cs = PSatAPI.instance.collectiveStrategy;
					if(!PSatAPI.instance.is_role_run){
						for(AssertionInstance assertion: PSatAPI.instance.agent.getAssertionInstances()){
							if(assertion !=null && w.toHtmlString().equals(assertion.getAssertion())){
								checked = true;
								goal_v = assertion.getGoalv();
								break;
							}
						}
					}
					else{
						String atype = "<html>"+roleType+"</html>";
						if(self.roleExist(agentName, atype, PSatAPI.instance.knowledgeBase)){
							String [] zoneAgents = null;
							zoneAgents = new String[PSatAPI.instance.pathAgentNames.size()];
							int i=0;
							for(String an: PSatAPI.instance.pathAgentNames){
								zoneAgents[i] = an;
								i = i+1;		        			
							}   
							goal_v = self.getRoleVGoal(agentName, atype, PSatAPI.instance.knowledgeBase);
			        		AssertionRole anew = new AssertionRole(agentName, atype, zoneAgents,PSatAPI.instance.knowledgeBase, goal_v, PSatAPI.instance.collectiveStrategy);

			        		self.addRole(anew);
			        		AgentFactory.writeAgent(self);
			        		
							checked = true;
						}						
					}
					if(PSatAPI.instance.is_role_run){
						String meaning = "";
						String genericFormula = "";
						if(PSatAPI.instance.knowledgeBase == null){
							PSatAPI.instance.knowledgeBase = KnowledgeBase.SENDER;
						}
						if(PSatAPI.instance.knowledgeBase.equals(KnowledgeBase.SUBJECT)){
							meaning = w.getGenericMeaning("[su]", "[s]", "[r]");
							genericFormula = w.getGenericFormula("su", "s", "r");
						}
						else if(PSatAPI.instance.knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
							meaning = w.getGenericMeaning("[r]", "[s]", "[su]");
							genericFormula = w.getGenericFormula("r", "s", "su");
						}
						else if(PSatAPI.instance.knowledgeBase.equals(KnowledgeBase.SENDER)){
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
						
						rowproperties.setProperty("roleType", "<html>"+roleType+"</html>");
						rowproperties.setProperty("checked", ""+checked);
						rowproperties.setProperty("genericFormula", genericFormula);
						rowproperties.setProperty("goalv", ""+goal_v);
						rowproperties.setProperty("meaning", meaning);
						rowproperties.setProperty("collectiveStrategy", CollectiveMode.getModeDesc(cs));
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

						rowproperties.setProperty("a_counter", ""+PSatAPI.instance.a_counter);
						rowproperties.setProperty("checked", ""+checked);
						rowproperties.setProperty("w", w.toHtmlString());
						rowproperties.setProperty("goalv", ""+goal_v);
						rowproperties.setProperty("meaning", meaning);
						rowproperties.setProperty("collectiveStrategy", CollectiveMode.getModeDesc(cs));

					}		
					
					Properties [] tempproperties = new Properties[properties.length +1];
					for(int i=0;i< properties.length;i++){
						tempproperties[i] = properties[i];
					}
					tempproperties[properties.length] = rowproperties;
					properties = tempproperties;
					tempproperties = null;
															
					PSatAPI.instance.a_counter = PSatAPI.instance.a_counter+1;
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
		
		//reorder constructs
//		if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE){
//			ArrayList<Properties> pptiesarray = new ArrayList<Properties>();
//			for(Properties ppty: properties){
//				if(instance.is_role_run){
//					if(!ppty.get("genericFormula").equals(new K0a(new Attribute()).toHtmlString())){
//						if(!ppty.get("genericFormula").equals(new K0(new Attribute()).toHtmlString())){
//							pptiesarray.add(ppty);
//						}
//					}
//				}
//				else{
//					if(!ppty.get("w").equals(new K0a(new Attribute()).toHtmlString())){
//						if(!ppty.get("w").equals(new K0(new Attribute()).toHtmlString())){
//							pptiesarray.add(ppty);
//						}
//					}					   
//				}
//			}
//			
//			properties = new Properties[pptiesarray.size()];
//			properties = pptiesarray.toArray(properties);
//		}
//		else{
//			ArrayList<Properties> k0pptiesarray = new ArrayList<Properties>();
//			ArrayList<Properties> otherpptiesarray = new ArrayList<Properties>();
//
//			for(Properties ppty: properties){
//				if(instance.is_role_run){
//					
//					if(ppty.get("roleType").equals("<html><i>k</i><sub>0a</sub></html>")){//K0a
//						ppty.setProperty("a_counter", "");
//						k0pptiesarray.add(ppty);						
//					}
//					else if(ppty.get("roleType").equals("<html><b>K</b><sub>0</sub></html>")){ //K0
//						ppty.setProperty("a_counter", "");
//						k0pptiesarray.add(ppty);
//					}
//					else{
//						otherpptiesarray.add(ppty);
//					}
//				}
//				else{					
//					if(ppty.get("w").equals(new K0a(new Attribute()).toHtmlString())){
//						ppty.setProperty("a_counter", "");
//						k0pptiesarray.add(ppty);						
//					}
//					else if(ppty.get("w").equals(new K0(new Attribute()).toHtmlString())){
//						ppty.setProperty("a_counter", "");
//						k0pptiesarray.add(ppty);	
//					}
//					else{
//						otherpptiesarray.add(ppty);
//					}
//				}
//			}
//			
//			Properties k0ppties[] = new Properties[k0pptiesarray.size()];
//			k0ppties = k0pptiesarray.toArray(k0ppties);
//			
//			Properties otherppties[] = new Properties[otherpptiesarray.size()];		
//			otherppties = otherpptiesarray.toArray(otherppties);
//			
//			Properties combinedppties[] = new Properties[k0ppties.length+otherppties.length];
//			int ocounter = 0;
//			for(Properties p:k0ppties){
//				combinedppties[ocounter] = p;
//				ocounter = ocounter+1;
//			}
//			for(Properties p:otherppties){
//				combinedppties[ocounter] = p;
//				ocounter = ocounter+1;
//			}
//			properties = combinedppties;
//		}
		return properties;
		
	}
	
	public static void clearAllAgentAssertions(){
		if(PSatAPI.instance.agents != null) {
			for(Agent a:PSatAPI.instance.agents){
				a.resetAssertionInstances();
				a.resetRoles();
				PSatAPI.netWriteAgent(a);
			}
		}
		
		
//		String [] agentNames = ServerAgentFactory.getAgentNames(sinstance);
//		for(String agentName: agentNames){
//			Agent a = PSatClient.netGetAgent(agentName);
//			a.resetAssertionInstances();
//			a.resetRoles();
//			PSatClient.netWriteAgent(a);
//		}		
	}
	
	public static int getTotalNoOfAssertionsForAllAgents(){
		int count = 0;
		String [] agentNames = AgentFactory.getAgentNames();
		
		for(String agentName: agentNames){
			Agent a = PSatAPI.netGetAgent(agentName);
			if(PSatAPI.instance.is_role_run){
				if(a.getRoles() != null){
					count = count + a.getRoles().length;
				}
			}
			else{
				if(a.getAssertionInstances() != null){
					count = count + a.getAssertionInstances().length;
				}
			}	
		}
		return count;
	}
	public static World getAssertionInstanceWorld(String httpstring, String selfAgentName, String partialPath, String sessionid){
		if(httpstring.contains("(")){
			String [] h3 = httpstring.split("\\(");
			String [] h2 = h3[1].split("\\)");
			String h1 = h2[0];
			httpstring = "<html>"+h1+"</html>";
			
		}
		
		
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
	
	public static World getAssertionRole(String selfAgentName, String httpstring){
		String folderName2 = PSatAPI.datastore_file_path+"/"+PSatAPI.instance.sessionid+"/assertionRoles/"+selfAgentName;
		
		//default agents
		Agent self = AgentFactory.getAgent(selfAgentName);
		Agent agent1 = new Agent("a1");
		Agent agent2 = new Agent("a2");
				
		Attribute h = new Attribute();
		h.setSubjectName(PSatAPI.instance.sourceAgentName);
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
												
//						if(w instanceof K0a){
//							assertion = new K0a(h);
//						}
//						else if(w instanceof K0b){
//							assertion = new K0b(h);
//						}
//						else if(w instanceof K0){
//							assertion = new K0(h);
//						}
//						else 
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
		
//		if(w instanceof K0a){
//			return false;
//		}
//		else 
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
