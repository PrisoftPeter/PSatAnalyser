package psat.knowledge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import psat.Display;
import psat.PSatAPI;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.ArrayCleaner;
import psat.util.AssertionInstance;
import psat.util.AssertionRole;
import psat.util.AssertionsFactory;
import psat.util.Attribute;
import psat.util.CollectiveMode;
import psat.util.CollectiveStrategy;
import psat.util.Config;
import psat.util.ConfigInstance;
import psat.util.Helper;
import psat.util.KnowledgeBase;
import psat.util.KnowledgeLevel;
import psat.util.SATResult;
import psat.util.SatSerializer;
import psat.verification.collective.CGK1InstanceVerifier;
import psat.verification.collective.CGK1RoleVerifier;
import psat.verification.collective.CGK1aInstanceVerifier;
import psat.verification.collective.CGK1aRoleVerifier;
import psat.verification.collective.CGK21CGK22InstanceVerifier;
import psat.verification.collective.CGK21CGK22RoleVerifier;
import psat.verification.collective.CGK21aCGK22aInstanceVerifier;
import psat.verification.collective.CGK21aCGK22aRoleVerifier;
import psat.verification.collective.CGK31CGK32InstanceVerifier;
import psat.verification.collective.CGK31CGK32RoleVerifier;
import psat.verification.collective.CGK31aCGK32aInstanceVerifier;
import psat.verification.collective.CGK31aCGK32aRoleVerifier;
import psat.verification.collective.CGK41CGK42InstanceVerifier;
import psat.verification.collective.CGK41CGK42RoleVerifier;
import psat.verification.collective.CGK41aCGK42aInstanceVerifier;
import psat.verification.collective.CGK41aCGK42aRoleVerifier;

public class MemoryFactory {
	
	public static boolean newMemoryStore(String selfName){

		Agent self = AgentFactory.getAgent(selfName);
		if(PSatAPI.instance.validAgents == null){
			setValidAgents();
		}
		new Memory(self, PSatAPI.instance.sourceAgentName).resetKnowledge();
		self.addToCreatedMemoryStores(PSatAPI.instance.sourceAgentName);
		AgentFactory.writeAgent(self);
		
		createOriginalMemoryClone(selfName,PSatAPI.instance.sourceAgentName, PSatAPI.instance.sessionid);
		
		return true;
	}
		
	public static String seq;
	public static void setValidAgents(){

		if(PSatAPI.instance.subjectName !=null){
			PSatAPI.instance.validAgents = new String[1];
			PSatAPI.instance.validAgents[0] = PSatAPI.instance.subjectName;
		}
		else{
			PSatAPI.instance.validAgents = new String[0];
		}
		
		//select all unique agents from paths list
//		for(String path:instance.selectedAgentPaths){
//			String path = PSatAPI.instance.selectedPath;
			String path = seq;
			if(path != null){
				if(path.contains(",")){
					path = path.replace(",", "");
				}
				String[] p2 =null;
				if(path.contains(":")){
					String[] p1 = path.split(": ");
					p2 = p1[1].split(" ");
				}
				else{
					p2 = path.split(" ");
				}

				for(String agentName1:p2){
					boolean validagentName = false;
					if(agentName1 != null){
						agentName1 = agentName1.trim();
						if(agentName1.length()>0){
							validagentName = true;
						}
					}
					if(validagentName){
						boolean exist1 = false;
						for(String agentName:PSatAPI.instance.validAgents){
							if(agentName.equals(agentName1)){
								exist1  = true;
								break;
							}
						}
						if(!exist1){
							String temp [] = new String[PSatAPI.instance.validAgents.length+1];
							for(int i=0;i<PSatAPI.instance.validAgents.length;i++){
								temp[i] = PSatAPI.instance.validAgents[i];
							}
							temp[PSatAPI.instance.validAgents.length] = agentName1;
							PSatAPI.instance.validAgents = temp;
						}
					}					
				}	
			}
			else{
				for(Agent a:PSatAPI.instance.agents){
					String temp [] = new String[PSatAPI.instance.validAgents.length+1];
					for(int i=0;i<PSatAPI.instance.validAgents.length;i++){
						temp[i] = PSatAPI.instance.validAgents[i];
					}
					temp[PSatAPI.instance.validAgents.length] = a.getAgentName();
					PSatAPI.instance.validAgents = temp;
				}
			}
									
//		}
	}
	

	public static boolean newMemoryStore(){
		boolean done = false;
		
		if(PSatAPI.instance.sourceAgentName == null || PSatAPI.instance.sourceAgentName.trim().length() == 0){
			Display.updateProgressComponent(100, "");
			PSatAPI.instance.busy = false;
			return done;
		}
		
		PSatAPI.instance.busy = true;
		
		if(PSatAPI.instance.is_dynamic_memory_store){
			Display.updateProgressComponent(-1, "");
			
			setValidAgents();
			
			double coverage = ((double)PSatAPI.instance.validAgents.length/(double)PSatAPI.instance.agents.length)*100;
			coverage = (double)(Math.round(coverage*100))/100;
			String response_info = "";
			if(PSatAPI.instance.is_role_run){
//				response_info = "@n-nearest neighbours="+instance.k+			
//				  	   " sat(pr) role affects "+sinstance.validAgents.length+" objects and covers "+coverage+"% of network";
				response_info = "@n-nearest neighbours="+PSatAPI.instance.k;
			}
			else{
//				response_info = "@source="+instance.sourceAgentName+", target="+instance.targetAgentName+
//						" and "+instance.listPathsData.length+" paths generated, then sat(pr) instance affects "+sinstance.validAgents.length+
//						" objects and covers "+coverage+"% of network";
				response_info = "@source="+PSatAPI.instance.sourceAgentName+", target="+PSatAPI.instance.targetAgentName+
						" and "+PSatAPI.instance.listPathsData.length+" paths generated";
			}
			Display.updateLogPage(response_info, false);
			Display.updateProgressComponent(-1, "");
			
			PSatAPI.instance.noMemoryStores = 0;
			
			for(String agentName:PSatAPI.instance.validAgents){
				newMemoryStore(agentName);
				PSatAPI.instance.noMemoryStores =PSatAPI.instance.noMemoryStores +1;

				if(PSatAPI.instance.noMemoryStores >= PSatAPI.instance.validAgents.length){
					Display.updateProgressComponent(100, "");
				}
				else{
					PSatAPI.instance.completness = ((double)PSatAPI.instance.noMemoryStores/(double)(PSatAPI.instance.validAgents.length))*100;
					Display.updateProgressComponent(new Double(PSatAPI.instance.completness).intValue(), ConfigInstance.df.format(PSatAPI.instance.completness)+"%");
				}	
			}
			
		}
		else{
			PSatAPI.instance.noMemoryStores = 0;
			for(String agentname: AgentFactory.getAgentNames()){

				newMemoryStore(agentname);
				PSatAPI.instance.noMemoryStores =PSatAPI.instance.noMemoryStores +1;

				if(PSatAPI.instance.noMemoryStores >= PSatAPI.instance.agents.length){
					Display.updateProgressComponent(100, "");
				}
				else{
					PSatAPI.instance.completness = ((double)PSatAPI.instance.noMemoryStores/(double)(PSatAPI.instance.agents.length))*100;
					Display.updateProgressComponent(new Double(PSatAPI.instance.completness).intValue(), ConfigInstance.df.format(PSatAPI.instance.completness)+"%");
				}			
			}
		}
		PSatAPI.instance.busy = false;
		Config.serialiseConfigInstance(PSatAPI.instance.sessionid);
		Display.updateProgressComponent(100, "");
		PSatAPI.instance.busy = false;
		
		done = true;
		return done;
	}
	
	public static String [] getMemoryStorePaths(String agentName, String sessionid){
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/memory/"+agentName;
		File folder2 = new File(folderName2);
		String [] memoryStorePaths = new String[0];
		if(folder2.listFiles() != null){
			memoryStorePaths = new String[folder2.listFiles().length];	
		}
		
		if(memoryStorePaths.length >0){
			int i=0;
			for (final File fileEntry : folder2.listFiles()) {
				String [] pb = fileEntry.getName().split("/");
				memoryStorePaths[i]= pb[pb.length-1];
				i= i+1;
		    }	
		}
		
		return memoryStorePaths;
	}
	
	public static String [] getOriginalMemoryStorePaths(String agentName, String sessionid){
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/originalMemoryclone/"+agentName;

		File folder2 = new File(folderName2);
		String [] memoryStorePaths = new String[0];
		if(folder2.listFiles() != null){
			memoryStorePaths = new String[folder2.listFiles().length];	
		}
		
		if(memoryStorePaths.length >0){
			int i=0;
			for (final File fileEntry : folder2.listFiles()) {
				String [] pb = fileEntry.getName().split("/");
				memoryStorePaths[i]= pb[pb.length-1];
				i= i+1;
		    }	
		}
		
		return memoryStorePaths;
	}
	
	public static boolean createMemoryClone(String selfName, String subjectName, String sessionid){
		String srcFolderName = PSatAPI.datastore_file_path+"/"+sessionid+"/memory/"+selfName+"/"+selfName+"_"+subjectName;
		String cloneFolderName = PSatAPI.datastore_file_path+"/"+sessionid+"/memoryclone/"+selfName+"/"+selfName+"_"+subjectName;
		
		File cloneFolder = new File(cloneFolderName);
		boolean cexist = false;
		if(cloneFolder.exists()){
			if(cloneFolder.isDirectory()){
				cexist = true;
			}				
		}
		if(!cexist){
			cloneFolder.mkdir();
		}		
		File srcFolder = new File(srcFolderName);
		
		try {
			FileUtils.copyDirectory(srcFolder,cloneFolder);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean createOriginalMemoryClone(String selfName, String subjectName, String sessionid){
		String srcFolderName = PSatAPI.datastore_file_path+"/"+sessionid+"/memory/"+selfName+"/"+selfName+"_"+subjectName;
		String cloneFolderName = PSatAPI.datastore_file_path+"/"+sessionid+"/originalMemoryclone/"+selfName+"/"+selfName+"_"+subjectName;
		
		File cloneFolder = new File(cloneFolderName);
		boolean cexist = false;
		if(cloneFolder.exists()){
			if(cloneFolder.isDirectory()){
				cexist = true;
			}				
		}
		if(!cexist){
			cloneFolder.mkdir();
		}		
		else{
			try {
				FileUtils.forceDelete(cloneFolder);
				cloneFolder.mkdir();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File srcFolder = new File(srcFolderName);
		
		try {
			FileUtils.copyDirectory(srcFolder,cloneFolder);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean restoreMemoryFromClone(String selfName, String subjectName, String sessionid){
		String srcFolderName = PSatAPI.datastore_file_path+"/"+sessionid+"/memory/"+selfName+"/"+selfName+"_"+subjectName;
		String cloneFolderName = PSatAPI.datastore_file_path+"/"+sessionid+"/memoryclone/"+selfName+"/"+selfName+"_"+subjectName;
		
		File cloneFolder = new File(cloneFolderName);		
		File srcFolder = new File(srcFolderName);
		boolean sexist = false;
		if(srcFolder.exists()){
			if(srcFolder.isDirectory()){
				sexist = true;
			}				
		}
		if(!sexist){
			srcFolder.mkdir();
		}
		else{
			try {
				FileUtils.forceDelete(srcFolder);
				srcFolder.mkdir();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileUtils.copyDirectory(cloneFolder,srcFolder);
			FileUtils.forceDelete(cloneFolder);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean originalWorld(String agentName, World world, String sessionid){
		String [] originalPartialPaths = getOriginalMemoryStorePaths(agentName, sessionid);
		
		for(String originalPartialPath:originalPartialPaths){

			
			String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/originalMemoryclone/"+agentName+"/"+originalPartialPath;
			try {
				File folder2 = new File(folderName2);
				if(folder2.isDirectory()){
					for (final File fileEntry : folder2.listFiles()) {
						FileInputStream fileIn = new FileInputStream(fileEntry);
						ObjectInputStream in = new ObjectInputStream(fileIn);
						World w = (World) in.readObject();
						if(w.toString().equals(world.toString())){
							in.close();
							fileIn.close();
							
							return true;
						}
						in.close();
						fileIn.close();
				    }
				}			
			} 
			catch (IOException i) {
				System.err.println("IO exception @readAgents");
			} 
			catch (ClassNotFoundException c) {
				System.err.println("Agent class not found");
			}
		}
		return false;
	}
	
	public static void dumpMemoryStoreOnDisplay(String agentName, String partialPath, double pathsat){
		String sessionid = PSatAPI.instance.sessionid;
		
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/memory/"+agentName+"/"+partialPath;
		int counter = 0;
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				if(PSatAPI.instance.log_agent_knowledge_state){
					Display.updateLogPage("**"+partialPath+"**", false);
					String x_desc = "<html><font style='color:black;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>P<sub>"+agentName+"<sub></i></font></html>";
					Display.updateLogPage(x_desc, false);
				}
				
				double totalElements =0;
				double atomicElements =0;
				double compositeElements=0;
				for (final File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					World w = (World) in.readObject();
					boolean isComposite = isCompositeWorld(w);
					counter = counter +1;

					if(PSatAPI.instance.log_agent_knowledge_state || PSatAPI.instance.isMemoryStoreMode){
//						if(instance.runningTraining || instance.runningAnalysis || instance.isMemoryStoreMode){
						if(PSatAPI.instance.runningTraining || PSatAPI.instance.isMemoryStoreMode){
							if(originalWorld(agentName, w, sessionid)){
								Display.updateLogPage(counter+":"+w.toHtmlString(), false);
							}
							else{
								String w_desc = w.toLimitHtmlString();
								if(isComposite){
									w_desc = "<html><font style='background-color:#AED6F1;'>"+w_desc+"</font></html>";	
								}
								else{
									w_desc = "<html><font style='background-color:#7EE183;'>"+w_desc+"</font></html>";
								}
								Display.updateLogPage(w_desc, false);
							}		
						}
						else{
							Display.updateLogPage(counter+":"+w.toHtmlString(), false);
						}
					}
					
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
//				double p_uncertainty = (compositeElements/totalElements)*100;
//				double p_belief = (atomicElements/totalElements)*100;
				double p_uncertainty = compositeElements/totalElements;
				double p_belief = atomicElements/totalElements;
				if(PSatAPI.instance.log_agent_knowledge_state){
					String p_uncertainty_desc = "<html>,<br><font style='color:black;'>&nbsp;&nbsp;&nbsp;UncertaintyLevel="+Helper.RoundTo2Decimals(p_uncertainty)+"%</font></html>";
					String p_belief_desc = "<html><font style='color:black;'>&nbsp;&nbsp;&nbsp;BeliefLevel="+Helper.RoundTo2Decimals(p_belief)+"%</font></html>";
					
					Display.updateLogPage(p_uncertainty_desc, false);
					Display.updateLogPage(p_belief_desc, false);
					Display.updateLogPage("/", false);
				}
				
//				if(instance.log_entropy_belief_uncertainty){
////					if((instance.runningTraining || instance.runningAnalysis) && !instance.isModeEntropy){
////						sinstance.serverSatSerializer.writeBeliefUncertaintyLevel(agentName,p_uncertainty, p_belief, -10, pathsat, instance, sinstance);
////					}
////					else if((instance.runningTraining || instance.runningAnalysis) && instance.isModeEntropy){
////						sinstance.serverSatSerializer.writeBeliefUncertaintyLevel(agentName,p_uncertainty, p_belief, InformationFlows.currentKnowledgeEntropy, pathsat, instance, sinstance);
////					}	
//					if((instance.runningTraining) && !instance.isModeEntropy){
//						sinstance.serverSatSerializer.writeBeliefUncertaintyLevel(agentName,p_uncertainty, p_belief, -10, pathsat, instance, sinstance);
//					}
//					else if((instance.runningTraining) && instance.isModeEntropy){
//						sinstance.serverSatSerializer.writeBeliefUncertaintyLevel(agentName,p_uncertainty, p_belief, InformationFlows.currentKnowledgeEntropy, pathsat, instance, sinstance);
//					}
//				}				
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readAgents");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("Agent class not found");
		}
		finally{
//			Display.updateProgressComponent(100, "");
		}			
	}
	
	
	public static boolean isAtomicWorld(World world){
		if(world instanceof K1a || world instanceof K1b){
			return true;
		}
		else if(world instanceof K21a){
			return true;					
		}
		else if(world instanceof K21b){
			return true;				
		}
		else if(world instanceof K22a){
			return true;					
		}
		else if(world instanceof K22b){
			return true;					
		}
		else if(world instanceof K23a){
			return true;					
		}
		else if(world instanceof K23b){
			return true;					
		}
		else if(world instanceof K24a){
			return true;					
		}
		else if(world instanceof K24b){
			return true;					
		}
		else if(world instanceof K31a){
			return true;					
		}
		else if(world instanceof K31b){
			return true;					
		}
		else if(world instanceof K32a){
			return true;					
		}	
		else if(world instanceof K32b){
			return true;					
		}
		else if(world instanceof K41b){
			return true;
		}
		else if(world instanceof K41a){
			return true;
		}
		else if(world instanceof K42a){
			return true;
		}
		else if(world instanceof K42b){
			return true;
		}	
		return false;
	}
	
	public static boolean isCompositeWorld(World world){
		if(world instanceof K1){
			return true;
		}
		else if(world instanceof K21){
			return true;		
		}
		else if(world instanceof K22){
			return true;
		}
		else if(world instanceof K23){
			return true;
		}
		else if(world instanceof K24){
			return true;
		}
		else if(world instanceof K31){
			return true;
		}
		else if(world instanceof K32){
			return true;
		}
		else if(world instanceof K41){
			return true;
		}
		else if(world instanceof K42){
			return true;
		}	
		
		return false;
	}
	
	public static void dumpMemoryStoreOnDisplay(String agentName, double pathsat){
		String [] partialPaths = getMemoryStorePaths(agentName, PSatAPI.instance.sessionid);
		
		for(String partialPath: partialPaths){
			String folderName2 = PSatAPI.datastore_file_path+"/"+PSatAPI.instance.sessionid+"/memory/"+agentName+"/"+partialPath;
			
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				if(folder2.listFiles().length >0){
					dumpMemoryStoreOnDisplay(agentName,partialPath, pathsat);

				}
			}
				
		}
	}
	
//	
	public static boolean privacyRequirementRoles(String agentName){
		return fillAssertionRolesStore(agentName);
//		Display.updateAssertionsPage(agentName, "privacy requirement Roles");
	}
	
	public static String [] getAssertionsStorePaths(String agentName, String sessionid){
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertions/"+agentName;
		File folder2 = new File(folderName2);
		String [] memoryStorePaths = new String[0];
		if(folder2.listFiles() != null){
			memoryStorePaths = new String[folder2.listFiles().length];	
		}
		
		if(memoryStorePaths.length >0){
			int i=0;
			for (final File fileEntry : folder2.listFiles()) {
				String [] pb = fileEntry.getName().split("/");
				memoryStorePaths[i]= pb[pb.length-1];
		    }	
		}
		
		return memoryStorePaths;
	}
	
	public static ArrayList<World> collectiveassertions;
	public static void extractCollectiveAssertions(String subjectName, String[] pathAgents){
		
		collectiveassertions = new ArrayList<World>();
		World applicableReqs [] = new World[0];

//		for(int k=0;k<pathAgents.length-1;k++){	
		for(String agentName1:pathAgents){
			for(String agentName2:pathAgents){
				if(!agentName1.equals(agentName2)){
					
					boolean extractFromSubject = true;
					boolean extractFromAgentName1 = true;
					boolean extractFromAgentName2 = true;
					
					if(extractFromAgentName2){
						String selfAgentName = agentName1;
						Agent self = AgentFactory.getAgent(selfAgentName);
						
						if(PSatAPI.instance.isModePick){
							if(!PSatAPI.instance.is_role_run){
								
								AssertionInstance f[] = self.getAssertionInstances();
								
								World reqs [] = new World[f.length];
								for(int i=0;i<f.length;i++){
									reqs[i] = AssertionsFactory.getAssertionInstanceWorld(f[i].getAssertion(), selfAgentName, selfAgentName+"_"+subjectName, PSatAPI.instance.sessionid);
								}
								
								for(World req:reqs){
									if(isInstanceApplicable(req, selfAgentName,subjectName,agentName1, agentName2)){
										World temp [] = new World[applicableReqs.length +1];
										for(int i=0;i< applicableReqs.length;i++){
											temp[i] = applicableReqs[i];
										}
										temp[applicableReqs.length] = req;
										applicableReqs = temp;
									}
								}
							}
							else{
								AssertionRole roles[] = new AssertionRole[0];
								if(self.getRoles() !=null){
									for(AssertionRole a: self.getRoles()){
										boolean applicableRole = false;
										if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
										   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
											applicableRole = true;
										}
										if(applicableRole){
											AssertionRole temp[] = new AssertionRole[roles.length+1];
											for(int i=0;i<roles.length;i++){
												temp[i] = roles[i];
											}
											temp[roles.length] = a;
											roles = temp;
										}						
									}	
								}
								
								if(!selfAgentName.equals(subjectName)){
									Agent su = AgentFactory.getAgent(subjectName);
									if(su.getRoles() !=null){
										for(AssertionRole a: su.getRoles()){
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;
											}							
										}	
									}
								}
								
								if(!selfAgentName.equals(subjectName) && !agentName1.equals(subjectName)){
									Agent s = AgentFactory.getAgent(agentName1);
									if(s.getRoles() !=null){
										for(AssertionRole a: s.getRoles()){
																		
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											
											else if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;	
											}							
										}	
									}
								}
								
								if(!selfAgentName.equals(agentName2) ){
									Agent r = AgentFactory.getAgent(agentName2);
									if(r.getRoles() !=null){
										for(AssertionRole a: r.getRoles()){
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;	
											}
										}	
									}
								}
								
								for(AssertionRole role: roles){
									//String roleSelfAgentName = role.getSelfAgentName();
									String roleType = role.getRoleType();
									roleType = roleType.replace("<html>", "");
									roleType = roleType.replace("</html>", "");
									String[] roleZoneAgents = role.getZoneAgents();
									KnowledgeBase knowledgeBase = role.getKnowledgeBase();
									
									boolean su_inzone = false;
									boolean s_inzone = false;
									boolean r_inzone = false;
									for(String agentName:roleZoneAgents){
										if(agentName.equals(subjectName)){
											su_inzone = true;							
										}
									}
									for(String agentName:roleZoneAgents){
										if(agentName.equals(agentName2)){
											r_inzone = true;
										}
									}
									for(String agentName:roleZoneAgents){
										if(agentName.equals(agentName1)){
											s_inzone = true;
										}
									}
									//World reqs [] = new World[0];

									if(s_inzone && su_inzone && r_inzone){
										World  roleReq = World.createWorld(knowledgeBase, roleType, AgentFactory.getAgent(subjectName), 
												 AgentFactory.getAgent(agentName1), AgentFactory.getAgent(agentName2), 
												 AgentFactory.getAgent(subjectName).getPersonalAttributes()[0]);
										if(roleReq != null){
											World temp[] = new World[applicableReqs.length + 1];
											for(int i=0;i < applicableReqs.length; i++){
												temp[i] = applicableReqs[i];
											}
											temp[applicableReqs.length] = roleReq;
											applicableReqs = temp;	
										}						
									}					
								}
							}					
						}
					
					}
					
					if(extractFromAgentName1){
						String selfAgentName = agentName1;
						
						Agent self = AgentFactory.getAgent(selfAgentName);
						//Memory m = new Memory(self, subjectName, sinstance, instance);
						
						if(PSatAPI.instance.isModePick){
							if(!PSatAPI.instance.is_role_run){
								
								AssertionInstance f[] = self.getAssertionInstances();
								
								World reqs [] = new World[f.length];
								for(int i=0;i<f.length;i++){
									reqs[i] = AssertionsFactory.getAssertionInstanceWorld(f[i].getAssertion(), selfAgentName, selfAgentName+"_"+subjectName, PSatAPI.instance.sessionid);
								}
								
								for(World req:reqs){
									if(isInstanceApplicable(req, selfAgentName,subjectName,agentName1, agentName2)){
										World temp [] = new World[applicableReqs.length +1];
										for(int i=0;i< applicableReqs.length;i++){
											temp[i] = applicableReqs[i];
										}
										temp[applicableReqs.length] = req;
										applicableReqs = temp;
									}
								}
							}
							else{
								AssertionRole roles[] = new AssertionRole[0];
								if(self.getRoles() !=null){
									for(AssertionRole a: self.getRoles()){
										boolean applicableRole = false;
										if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
										   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
											applicableRole = true;
										}
										if(applicableRole){
											AssertionRole temp[] = new AssertionRole[roles.length+1];
											for(int i=0;i<roles.length;i++){
												temp[i] = roles[i];
											}
											temp[roles.length] = a;
											roles = temp;
										}						
									}	
								}
								
								if(!selfAgentName.equals(subjectName)){
									Agent su = AgentFactory.getAgent(subjectName);
									if(su.getRoles() !=null){
										for(AssertionRole a: su.getRoles()){
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;
											}							
										}	
									}
								}
								
								if(!selfAgentName.equals(subjectName) && !agentName1.equals(subjectName)){
									Agent s = AgentFactory.getAgent(agentName1);
									if(s.getRoles() !=null){
										for(AssertionRole a: s.getRoles()){
																		
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											
											else if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;	
											}							
										}	
									}
								}
								
								if(!selfAgentName.equals(agentName2) ){
									Agent r = AgentFactory.getAgent(agentName2);
									if(r.getRoles() !=null){
										for(AssertionRole a: r.getRoles()){
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;	
											}
										}	
									}
								}
								
								for(AssertionRole role: roles){
									//String roleSelfAgentName = role.getSelfAgentName();
									String roleType = role.getRoleType();
									roleType = roleType.replace("<html>", "");
									roleType = roleType.replace("</html>", "");
									String[] roleZoneAgents = role.getZoneAgents();
									KnowledgeBase knowledgeBase = role.getKnowledgeBase();
									
									boolean su_inzone = false;
									boolean s_inzone = false;
									boolean r_inzone = false;
									for(String agentName:roleZoneAgents){
										if(agentName.equals(subjectName)){
											su_inzone = true;							
										}
									}
									for(String agentName:roleZoneAgents){
										if(agentName.equals(agentName2)){
											r_inzone = true;
										}
									}
									for(String agentName:roleZoneAgents){
										if(agentName.equals(agentName1)){
											s_inzone = true;
										}
									}
									//World reqs [] = new World[0];

									if(s_inzone && su_inzone && r_inzone){
										World  roleReq = World.createWorld(knowledgeBase, roleType, AgentFactory.getAgent(subjectName), 
												 AgentFactory.getAgent(agentName1), AgentFactory.getAgent(agentName2), 
												 AgentFactory.getAgent(subjectName).getPersonalAttributes()[0]);
										if(roleReq != null){
											World temp[] = new World[applicableReqs.length + 1];
											for(int i=0;i < applicableReqs.length; i++){
												temp[i] = applicableReqs[i];
											}
											temp[applicableReqs.length] = roleReq;
											applicableReqs = temp;	
										}						
									}					
								}
							}					
						}
					
					}
					
					if(extractFromSubject){
						String selfAgentName = subjectName;
						
						Agent self = AgentFactory.getAgent(selfAgentName);
						//Memory m = new Memory(self, subjectName, sinstance, instance);
						
						if(PSatAPI.instance.isModePick){
							if(!PSatAPI.instance.is_role_run){
								
								AssertionInstance f[] = self.getAssertionInstances();
								
								World reqs [] = new World[f.length];
								for(int i=0;i<f.length;i++){
									reqs[i] = AssertionsFactory.getAssertionInstanceWorld(f[i].getAssertion(), selfAgentName, selfAgentName+"_"+subjectName, PSatAPI.instance.sessionid);
								}
													
								for(World req:reqs){
									if(isInstanceApplicable(req, selfAgentName,subjectName,agentName1, agentName2)){
										World temp [] = new World[applicableReqs.length +1];
										for(int i=0;i< applicableReqs.length;i++){
											temp[i] = applicableReqs[i];
										}
										temp[applicableReqs.length] = req;
										applicableReqs = temp;
									}
								}
							}
							else{
								AssertionRole roles[] = new AssertionRole[0];
								if(self.getRoles() !=null){
									for(AssertionRole a: self.getRoles()){
										boolean applicableRole = false;
										if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
										   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
											applicableRole = true;
										}
										else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
											applicableRole = true;
										}
										if(applicableRole){
											AssertionRole temp[] = new AssertionRole[roles.length+1];
											for(int i=0;i<roles.length;i++){
												temp[i] = roles[i];
											}
											temp[roles.length] = a;
											roles = temp;
										}						
									}	
								}
								
								if(!selfAgentName.equals(subjectName)){
									Agent su = AgentFactory.getAgent(subjectName);
									if(su.getRoles() !=null){
										for(AssertionRole a: su.getRoles()){
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;
											}							
										}	
									}
								}
								
								if(!selfAgentName.equals(subjectName) && !agentName1.equals(subjectName)){
									Agent s = AgentFactory.getAgent(agentName1);
									if(s.getRoles() !=null){
										for(AssertionRole a: s.getRoles()){
																		
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											
											else if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;	
											}							
										}	
									}
								}
								
								if(!selfAgentName.equals(agentName2) ){
									Agent r = AgentFactory.getAgent(agentName2);
									if(r.getRoles() !=null){
										for(AssertionRole a: r.getRoles()){
											boolean applicableRole = false;
											if(a.getRoleType().equals("<html><i>k</i><sub>0a</sub></html>") ||
													   a.getRoleType().equals("<html><b>K</b><sub>0</sub></html>")){
														applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(agentName2)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
												applicableRole = true;
											}
											else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(agentName1)){
												applicableRole = true;
											}
											
											if(applicableRole){
												AssertionRole temp[] = new AssertionRole[roles.length+1];
												for(int i=0;i<roles.length;i++){
													temp[i] = roles[i];
												}
												temp[roles.length] = a;
												roles = temp;	
											}
										}	
									}
								}
								
								for(AssertionRole role: roles){
									//String roleSelfAgentName = role.getSelfAgentName();
									String roleType = role.getRoleType();
									roleType = roleType.replace("<html>", "");
									roleType = roleType.replace("</html>", "");
									String[] roleZoneAgents = role.getZoneAgents();
									KnowledgeBase knowledgeBase = role.getKnowledgeBase();
									
									boolean su_inzone = false;
									boolean s_inzone = false;
									boolean r_inzone = false;
									for(String agentName:roleZoneAgents){
										if(agentName.equals(subjectName)){
											su_inzone = true;							
										}
									}
									for(String agentName:roleZoneAgents){
										if(agentName.equals(agentName2)){
											r_inzone = true;
										}
									}
									for(String agentName:roleZoneAgents){
										if(agentName.equals(agentName1)){
											s_inzone = true;
										}
									}
									//World reqs [] = new World[0];

									if(s_inzone && su_inzone && r_inzone){
										World  roleReq = World.createWorld(knowledgeBase, roleType, AgentFactory.getAgent(subjectName), 
												 AgentFactory.getAgent(agentName1), AgentFactory.getAgent(agentName2), 
												 AgentFactory.getAgent(subjectName).getPersonalAttributes()[0]);
										if(roleReq != null){
											World temp[] = new World[applicableReqs.length + 1];
											for(int i=0;i < applicableReqs.length; i++){
												temp[i] = applicableReqs[i];
											}
											temp[applicableReqs.length] = roleReq;
											applicableReqs = temp;	
										}						
									}					
								}
							}					
						}
					}
				
				}
				
			}
		}
		applicableReqs = ArrayCleaner.clean(applicableReqs); //remove replicated assertions from aspects
		
		for(World w:applicableReqs){
			collectiveassertions.add(w);
		}		
	}
	
	public static SATResult collectivesat(String selfAgentName,String subjectName, String senderName, 
			String recipientName, SatSerializer satserializer, Attribute message, ArrayList<Agent> agentsInPath){
		
		SATResult sat = new SATResult();
		World [] applicableReqs = new World[collectiveassertions.size()];
		applicableReqs = collectiveassertions.toArray(applicableReqs);		
		
		for(int i=0;i<applicableReqs.length;i++){
			String xdesc = CollectiveMode.getModeLimitHtmlDesc(PSatAPI.instance.collectiveStrategy)+"("+applicableReqs[i].toLimitHtmlString()+")";
			
			if(!PSatAPI.instance.satSerializer.requirementHtmlDesc.contains(xdesc)){
				if(PSatAPI.instance.satSerializer.requirementHtmlDesc.length() >0){
					PSatAPI.instance.satSerializer.requirementHtmlDesc = PSatAPI.instance.satSerializer.requirementHtmlDesc +" ; ";
					PSatAPI.instance.satSerializer.requirementRawDesc = PSatAPI.instance.satSerializer.requirementRawDesc + ";";
				}
				PSatAPI.instance.satSerializer.requirementHtmlDesc = PSatAPI.instance.satSerializer.requirementHtmlDesc +xdesc;	
				PSatAPI.instance.satSerializer.requirementRawDesc = PSatAPI.instance.satSerializer.requirementRawDesc + xdesc;							
			}
			PSatAPI.instance.satSerializer.updateRequirementHtmlFullDesc(xdesc);
			
		}
		

		Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = AgentFactory.getAgent(senderName);
		Agent recipient = AgentFactory.getAgent(recipientName);

		HashMap<World, Double> satvalues =new HashMap<World, Double>();
		
		for(World w: applicableReqs){
//			if(w instanceof K0){
//				double satvalue = CGK0Verifier.verify(subject, sender, recipient, sinstance,instance, message, agentsInPath);
//				satvalues.put(w, satvalue);
//			}
//			else if(w instanceof K0a){
//				double satvalue = CGK0aVerifier.verify(subject, sender, recipient, sinstance,instance, message,agentsInPath);
//				satvalues.put(w, satvalue);
//			}
//			else 
			if(w instanceof K1){
				double satvalue =0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK1RoleVerifier.verify(subject, sender, recipient, (K1)w,agentsInPath);
				}
				else{
					satvalue = CGK1InstanceVerifier.verify(subject, sender, recipient, (K1)w,agentsInPath);
				}
				satvalues.put(w, satvalue);
			}
			else if(w instanceof K1a){
				double satvalue = 0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK1aRoleVerifier.verify(subject, sender, recipient, (K1a)w, agentsInPath);
				}
				else{
					satvalue = CGK1aInstanceVerifier.verify(subject, sender, recipient, (K1a)w, agentsInPath);
				}
				satvalues.put(w, satvalue);
			}
			else if(w instanceof K31a | w instanceof K32a){
				double satvalue =0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK31aCGK32aRoleVerifier.verify(subject, sender, recipient, w, agentsInPath);
				}
				else{
					satvalue = CGK31aCGK32aInstanceVerifier.verify(subject, sender, recipient, w, agentsInPath);
				}
				satvalues.put(w, satvalue);
			}
			else if(w instanceof K31 | w instanceof K32){
				double satvalue =0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK31CGK32RoleVerifier.verify(subject, sender, recipient, w, agentsInPath);
				}
				else{
					satvalue = CGK31CGK32InstanceVerifier.verify(subject, sender, recipient, w, agentsInPath);
				}
				satvalues.put(w, satvalue);
			}
			else if(w instanceof K21a | w instanceof K22a){
				double satvalue=0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK21aCGK22aRoleVerifier.verify(subject, sender, recipient, w, agentsInPath);
				}
				else{
					satvalue = CGK21aCGK22aInstanceVerifier.verify(subject, sender, recipient, w, agentsInPath);
				}
				satvalues.put(w, satvalue);
			}
			else if(w instanceof K21 | w instanceof K22){
				double satvalue=0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK21CGK22RoleVerifier.verify(subject, sender, recipient, w);
				}
				else{
					satvalue = CGK21CGK22InstanceVerifier.verify(subject, sender, recipient, w);
				}
				satvalues.put(w, satvalue);
			}
			else if(w instanceof K41a | w instanceof K42a){
				double satvalue=0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK41aCGK42aRoleVerifier.verify(subject, sender, recipient, w);
				}
				else{
					satvalue = CGK41aCGK42aInstanceVerifier.verify(subject, sender, recipient, w);
				}
				satvalues.put(w, satvalue);
			}
			else if(w instanceof K41 | w instanceof K42){
				double satvalue=0;
				if(PSatAPI.instance.is_role_run){
					satvalue = CGK41CGK42RoleVerifier.verify(subject, sender, recipient, w);
				}
				else{
					satvalue = CGK41CGK42InstanceVerifier.verify(subject, sender, recipient, w);
				}
				satvalues.put(w, satvalue);
			}
			
		}
		
		double sumsatvalues = 0;
		double sumsatapplicable = 0;
		for (Map.Entry<World, Double> entry : satvalues.entrySet()) {
		    Double satvalue = entry.getValue();
		    //World w = entry.getKey();
		    if(!Double.isNaN(satvalue)){
		    	sumsatvalues = sumsatvalues+ satvalue;
		    	sumsatapplicable  =sumsatapplicable +1;
			}
		}
		if(sumsatapplicable == 0){
			sat.setSat(-1);	
		}
		else{
			sat.setSat(sumsatvalues/sumsatapplicable);
		}
		
		return sat;
	}
	
	public static SATResult sat(String selfAgentName,String subjectName, String senderName, 
								String recipientName, SatSerializer satserializer, Attribute message){

		SATResult sat = new SATResult();
		Agent self = AgentFactory.getAgent(selfAgentName);
		World applicableReqs [] = new World[0];
		Memory m = new Memory(self, subjectName);
		
		if(PSatAPI.instance.isModePick){
			if(!PSatAPI.instance.is_role_run){
				
				AssertionInstance f[] = self.getAssertionInstances();
				
				World reqs [] = new World[f.length];
				for(int i=0;i<f.length;i++){
					reqs[i] = AssertionsFactory.getAssertionInstanceWorld(f[i].getAssertion(), selfAgentName, selfAgentName+"_"+subjectName, PSatAPI.instance.sessionid);
				}
				
				if(reqs.length == 0){
					return sat;	
				}
				
				for(World req:reqs){
					if(isInstanceApplicable(req, selfAgentName,subjectName,senderName, recipientName)){
						World temp [] = new World[applicableReqs.length +1];
						for(int i=0;i< applicableReqs.length;i++){
							temp[i] = applicableReqs[i];
						}
						temp[applicableReqs.length] = req;
						applicableReqs = temp;
					}
				}
			}
			else{
				AssertionRole roles[] = new AssertionRole[0];
				if(self.getRoles() !=null){
					for(AssertionRole a: self.getRoles()){
						boolean applicableRole = false;
						if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(recipientName)){
							applicableRole = true;
						}
						else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
							applicableRole = true;
						}
						else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(senderName)){
							applicableRole = true;
						}
						if(applicableRole){
							AssertionRole temp[] = new AssertionRole[roles.length+1];
							for(int i=0;i<roles.length;i++){
								temp[i] = roles[i];
							}
							temp[roles.length] = a;
							roles = temp;
						}						
					}	
				}
				
				if(!selfAgentName.equals(subjectName)){
					Agent su = AgentFactory.getAgent(subjectName);
					if(su.getRoles() !=null){
						for(AssertionRole a: su.getRoles()){
							boolean applicableRole = false;
							if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(recipientName)){
								applicableRole = true;
							}
							else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
								applicableRole = true;
							}
							else if(a.getKnowledgeBase() !=null && a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(senderName)){
								applicableRole = true;
							}
							
							if(applicableRole){
								AssertionRole temp[] = new AssertionRole[roles.length+1];
								for(int i=0;i<roles.length;i++){
									temp[i] = roles[i];
								}
								temp[roles.length] = a;
								roles = temp;
							}							
						}	
					}
				}
				
				if(!selfAgentName.equals(subjectName) && !senderName.equals(subjectName)){
					Agent s = AgentFactory.getAgent(senderName);
					if(s.getRoles() !=null){
						for(AssertionRole a: s.getRoles()){
														
							boolean applicableRole = false;
							if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(recipientName)){
								applicableRole = true;
							}
							else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
								applicableRole = true;
							}
							else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(senderName)){
								applicableRole = true;
							}
							
							if(applicableRole){
								AssertionRole temp[] = new AssertionRole[roles.length+1];
								for(int i=0;i<roles.length;i++){
									temp[i] = roles[i];
								}
								temp[roles.length] = a;
								roles = temp;	
							}							
						}	
					}
				}
				
				if(!selfAgentName.equals(recipientName) ){
					Agent r = AgentFactory.getAgent(recipientName);
					if(r.getRoles() !=null){
						for(AssertionRole a: r.getRoles()){
							boolean applicableRole = false;
							if(a.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT) && selfAgentName.equals(recipientName)){
								applicableRole = true;
							}
							else if(a.getKnowledgeBase().equals(KnowledgeBase.SUBJECT) && selfAgentName.equals(subjectName)){
								applicableRole = true;
							}
							else if(a.getKnowledgeBase().equals(KnowledgeBase.SENDER) && selfAgentName.equals(senderName)){
								applicableRole = true;
							}
							
							if(applicableRole){
								AssertionRole temp[] = new AssertionRole[roles.length+1];
								for(int i=0;i<roles.length;i++){
									temp[i] = roles[i];
								}
								temp[roles.length] = a;
								roles = temp;	
							}
						}	
					}
				}
				
				for(AssertionRole role: roles){
					//String roleSelfAgentName = role.getSelfAgentName();
					String roleType = role.getRoleType();
					roleType = roleType.replace("<html>", "");
					roleType = roleType.replace("</html>", "");
					String[] roleZoneAgents = role.getZoneAgents();
					KnowledgeBase knowledgeBase = role.getKnowledgeBase();
					
					boolean su_inzone = false;
					boolean s_inzone = false;
					boolean r_inzone = false;
					for(String agentName:roleZoneAgents){
						if(agentName.equals(subjectName)){
							su_inzone = true;							
						}
					}
					for(String agentName:roleZoneAgents){
						if(agentName.equals(recipientName)){
							r_inzone = true;
						}
					}
					for(String agentName:roleZoneAgents){
						if(agentName.equals(senderName)){
							s_inzone = true;
						}
					}
					//World reqs [] = new World[0];

					if(s_inzone && su_inzone && r_inzone){
						World  roleReq = World.createWorld(knowledgeBase, roleType, AgentFactory.getAgent(subjectName), 
								 AgentFactory.getAgent(senderName), AgentFactory.getAgent(recipientName), 
								 AgentFactory.getAgent(subjectName).getPersonalAttributes()[0]);
						if(roleReq != null){
							World temp[] = new World[applicableReqs.length + 1];
							for(int i=0;i < applicableReqs.length; i++){
								temp[i] = applicableReqs[i];
							}
							temp[applicableReqs.length] = roleReq;
							applicableReqs = temp;	
						}						
					}					
				}
			}
						
			applicableReqs = ArrayCleaner.clean(applicableReqs);
						
			if(applicableReqs.length == 0){
				return sat;
			}

			//verification of applicable assertions based on selected collective strategy
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE){
				for(int i=0;i<applicableReqs.length;i++){
//					if(applicableReqs[i] instanceof K0 || applicableReqs[i] instanceof K0a){
//						continue;
//					}

					if(!PSatAPI.instance.satSerializer.requirementHtmlDesc.contains(applicableReqs[i].toLimitHtmlString())){
						if(PSatAPI.instance.satSerializer.requirementHtmlDesc.length() >0){
							PSatAPI.instance.satSerializer.requirementHtmlDesc = PSatAPI.instance.satSerializer.requirementHtmlDesc +" ; ";
							PSatAPI.instance.satSerializer.requirementRawDesc = PSatAPI.instance.satSerializer.requirementRawDesc + ";";
						}
						PSatAPI.instance.satSerializer.requirementHtmlDesc = PSatAPI.instance.satSerializer.requirementHtmlDesc +applicableReqs[i].toLimitHtmlString();	
						PSatAPI.instance.satSerializer.requirementRawDesc = PSatAPI.instance.satSerializer.requirementRawDesc + applicableReqs[i].toLimitHtmlString();							
					}
					PSatAPI.instance.satSerializer.updateRequirementHtmlFullDesc(applicableReqs[i].toLimitHtmlString());
					
				}		
				
				double f_in_p = 0;			
				for(World req:applicableReqs){		
					boolean contained = m.contains(req.toString());
					if(req !=null && contained){
						f_in_p = f_in_p+1;				
					}				
				}
				
				if(f_in_p == applicableReqs.length){
					sat.setSat(1);
					return sat;
				}
				else if(f_in_p ==0){
					sat.setSat(0);
					return sat;
				}
				else{
					sat.setSat(f_in_p/applicableReqs.length);
					return sat;
				}
			}
		}
		
		// do for uncertainty and belief levels
		else if(PSatAPI.instance.isModeUncertainty){
			
			String principalName = "";
			String reference1 = "";
			String reference2 = "";
			
			if(selfAgentName.equals(subjectName)){
				principalName = subjectName;
				reference1 = senderName;
				reference2 = recipientName;
			}
			else if(selfAgentName.equals(senderName)){
				principalName = senderName;
				reference1 = subjectName;
				reference2 = recipientName;
			}
			else if(selfAgentName.equals(recipientName)){
				principalName = recipientName;
				reference1 = senderName;
				reference2 = subjectName;
			}
			
			World[] beliefs = m.getBeliefs(principalName, reference1, reference2);
			World[] uncertainities = m.getUncertainties(principalName, reference1, reference2);
			
//			double currentSelfBeliefLevel = (double)beliefs.length/(double)(beliefs.length + uncertainities.length);
			double currentSelfUncertaintyLevel = (double)uncertainities.length/(double)(beliefs.length + uncertainities.length);
			
			if(selfAgentName.equals(subjectName)){
				PSatAPI.instance.currentSelfUncertaintyLevel_su = currentSelfUncertaintyLevel;
			}
			else if(selfAgentName.equals(senderName)){
				PSatAPI.instance.currentSelfUncertaintyLevel_s = currentSelfUncertaintyLevel;
			}
			else if(selfAgentName.equals(recipientName)){
				PSatAPI.instance.currentSelfUncertaintyLevel_r = currentSelfUncertaintyLevel;
			}
			
						
			ArrayList<KnowledgeLevel> knowledgeLevels = new ArrayList<KnowledgeLevel>();
						
			for(KnowledgeLevel kl: AgentFactory.getAgent(subjectName).getKnowledgeLevels()){
				knowledgeLevels.add(kl);
				satserializer.updateRequirementHtmlFullDesc(kl.getKldescription());
			}
			for(KnowledgeLevel kl: AgentFactory.getAgent(senderName).getKnowledgeLevels()){
				knowledgeLevels.add(kl);					
				satserializer.updateRequirementHtmlFullDesc(kl.getKldescription());
			}			
			for(KnowledgeLevel kl: AgentFactory.getAgent(recipientName).getKnowledgeLevels()){
				knowledgeLevels.add(kl);
				satserializer.updateRequirementHtmlFullDesc(kl.getKldescription());
			}
			
			if(selfAgentName.equals(subjectName)){
				for(KnowledgeLevel kl: knowledgeLevels){
					if(kl.getKnowledgeBase() != null && kl.getKnowledgeBase().equals(KnowledgeBase.SUBJECT)){
						PSatAPI.instance.expectedSelfUncertaintyLevel_su = kl.getUncertaintyLevel();
						break;
					}
				}
				
			}
			if(selfAgentName.equals(senderName)){
				for(KnowledgeLevel kl: knowledgeLevels){
					if(kl.getKnowledgeBase() != null && kl.getKnowledgeBase().equals(KnowledgeBase.SENDER)){
						PSatAPI.instance.expectedSelfUncertaintyLevel_s = kl.getUncertaintyLevel();
						break;
					}
				}
			}
			
			if(selfAgentName.equals(recipientName)){
				for(KnowledgeLevel kl: knowledgeLevels){
					if(kl.getKnowledgeBase() != null && kl.getKnowledgeBase().equals(KnowledgeBase.RECIPIENT)){
						PSatAPI.instance.expectedSelfUncertaintyLevel_r = kl.getUncertaintyLevel();
						break;
					}
				}
			}
			
			double expectedSelfUncertaintyLevel = 0;	

			double satuncertainty = -1;
			
			if(selfAgentName.equals(subjectName)){
				if(subjectName.equals(senderName)){
					if(PSatAPI.instance.subjectdone2){
						expectedSelfUncertaintyLevel = PSatAPI.instance.expectedSelfUncertaintyLevel_s;
						PSatAPI.instance.subjectdone2 = false;
					}
					else{
						expectedSelfUncertaintyLevel = PSatAPI.instance.expectedSelfUncertaintyLevel_su;
						PSatAPI.instance.subjectdone2 = true;
					}
				}
				else{
					expectedSelfUncertaintyLevel = PSatAPI.instance.expectedSelfUncertaintyLevel_su;
				}				
			}
			else if(selfAgentName.equals(senderName)){
				expectedSelfUncertaintyLevel = PSatAPI.instance.expectedSelfUncertaintyLevel_s;
			}			
			else if(selfAgentName.equals(recipientName)){
				expectedSelfUncertaintyLevel = PSatAPI.instance.expectedSelfUncertaintyLevel_r;
			}
			
			if(expectedSelfUncertaintyLevel ==0){
				satuncertainty = 0;
			}
			else{					
				if(PSatAPI.instance.greaterThanOrEqualTo){
					if(currentSelfUncertaintyLevel >=  expectedSelfUncertaintyLevel){
						satuncertainty = 1;
					}
					
					else{
						double diff = expectedSelfUncertaintyLevel-currentSelfUncertaintyLevel;
						satuncertainty = 1-(diff/expectedSelfUncertaintyLevel);							
					}
				}
				
				else if(PSatAPI.instance.lessThanOrEqualTo){
					if( currentSelfUncertaintyLevel<= expectedSelfUncertaintyLevel){
						satuncertainty = 1;
					}
					else{
						double diff = currentSelfUncertaintyLevel-expectedSelfUncertaintyLevel;
						satuncertainty = 1-(diff/currentSelfUncertaintyLevel);							
//						satuncertainty = 1-(diff/expectedSelfUncertaintyLevel);							
					}
				}				
			}
						
			double fsat = -1;
			if(satuncertainty ==0){
				//do nothing
			}
			else if(satuncertainty >0){
				fsat = satuncertainty;
			}
									
			sat.setSat(fsat);
			
			sat.setAveExpectedSelfUncertaintyLevel(expectedSelfUncertaintyLevel);
			sat.setCurrentSelfUncertaintyLevel(currentSelfUncertaintyLevel);
			sat.setSatuncertainty(satuncertainty);
			
			String desc = "";
			if(PSatAPI.instance.greaterThanOrEqualTo){
				desc = selfAgentName+"[U: Exp≥"+(Math.round(expectedSelfUncertaintyLevel * 100.0) / 100.0)
			      +" Act="+(Math.round(currentSelfUncertaintyLevel * 100.0) / 100.0)+"]";	
			}
			else if(PSatAPI.instance.lessThanOrEqualTo){
				desc = selfAgentName+"[U: Exp≤"+(Math.round(expectedSelfUncertaintyLevel * 100.0) / 100.0)
					      +" Act="+(Math.round(currentSelfUncertaintyLevel * 100.0) / 100.0)+"]";	
			}
			else{
				desc = selfAgentName+"[U: Exp="+(Math.round(expectedSelfUncertaintyLevel * 100.0) / 100.0)
					      +" Act="+(Math.round(currentSelfUncertaintyLevel * 100.0) / 100.0)+"]";	
			}
						
			if(!PSatAPI.instance.satSerializer.requirementHtmlDesc.contains(desc)){
				PSatAPI.instance.satSerializer.requirementHtmlDesc = PSatAPI.instance.satSerializer.requirementHtmlDesc +"; "	+desc;	
				PSatAPI.instance.satSerializer.requirementRawDesc = PSatAPI.instance.satSerializer.requirementRawDesc + "; "+ desc;
			}	
			
			return sat;				
		
		}
		return null;
			
	}
	
			
	public static boolean isInstanceApplicable(World world, String selfAgentName, String subjectName, String senderName, String recipientName){
//		if(world instanceof K0 || world instanceof K0a || world instanceof K0b){
//			return true;
//		}
//		else 
		if(!(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE)){
			return true;
		}
		else if(world instanceof K1 || world instanceof K1a || world instanceof K1b){
			if(world.getSelf().getAgentName().equals(selfAgentName)){
				return true;
			}
		}
		else if(world instanceof K21){
			K21 w = (K21)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k21a.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k21a.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k21a.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K21a){
			K21a w = (K21a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K21b){
			K21b w = (K21b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K22){
			K22 w = (K22)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k22a.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k22a.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k22a.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}
		}
		else if(world instanceof K22a){
			K22a w = (K22a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K22b){
			K22b w = (K22b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K23){
			K23 w = (K23)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k23a.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k23a.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k23a.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}
		}
		else if(world instanceof K23a){
			K23a w = (K23a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K23b){
			K23b w = (K23b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K24){
			K24 w = (K24)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k24a.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k24a.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k24a.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}
		}
		else if(world instanceof K24a){
			K24a w = (K24a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K24b){
			K24b w = (K24b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K31){
			K31 w = (K31)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k31a.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k31a.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k31a.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}
		}
		else if(world instanceof K31a){
			K31a w = (K31a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K31b){
			K31b w = (K31b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K32){
			K32 w = (K32)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k32a.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k32a.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k32a.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}
		}
		else if(world instanceof K32a){
			K32a w = (K32a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}					
		}	
		else if(world instanceof K32b){
			K32b w = (K32b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent2().getAgentName().equals(subjectName))){
				return true;
			}					
		}
		else if(world instanceof K41){
			K41 w = (K41)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k41a.getAgent1().getAgentName().equals(senderName) && w.k41a.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k41a.getAgent1().getAgentName().equals(recipientName) && w.k41a.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k41a.getAgent1().getAgentName().equals(subjectName) && w.k41a.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k41a.getAgent1().getAgentName().equals(recipientName) && w.k41a.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k41a.getAgent1().getAgentName().equals(senderName) && w.k41a.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k41a.getAgent1().getAgentName().equals(subjectName) && w.k41a.getAgent2().getAgentName().equals(senderName))){
				return true;
			}
		}
		else if(world instanceof K41b){
			K41b w = (K41b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(senderName))){
				return true;
			}
		}
		else if(world instanceof K41a){
			K41a w = (K41a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(senderName))){
				return true;
			}
		}
		else if(world instanceof K42){
			K42 w = (K42)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.k42a.getAgent1().getAgentName().equals(senderName) && w.k42a.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k42a.getAgent1().getAgentName().equals(recipientName) && w.k42a.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k42a.getAgent1().getAgentName().equals(subjectName) && w.k42a.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k42a.getAgent1().getAgentName().equals(recipientName) && w.k42a.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k42a.getAgent1().getAgentName().equals(senderName) && w.k42a.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.k42a.getAgent1().getAgentName().equals(subjectName) && w.k42a.getAgent2().getAgentName().equals(senderName))){
				return true;
			}
		}
		else if(world instanceof K42a){
			K42a w = (K42a)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(senderName))){
				return true;
			}
		}
		else if(world instanceof K42b){
			K42b w = (K42b)world;
			if((w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(senderName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(recipientName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(recipientName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(senderName) && w.getAgent2().getAgentName().equals(subjectName))||
			   (w.getSelf().getAgentName().equals(selfAgentName) && w.getAgent1().getAgentName().equals(subjectName) && w.getAgent2().getAgentName().equals(senderName))){
				return true;
			}
		}	
//		else{
//			System.out.println("No world type found");
//		}
		
		return false;		
	}
	
	public static void removeAllWorldsFromAssertionRolesStore(String selfAgentName, String sessionid){
		try {
			File f = new File(PSatAPI.datastore_file_path+"/"+sessionid+"/assertionRoles/"+selfAgentName);
			if(f.isDirectory()){
				FileUtils.forceDelete(f);	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createAssertionRolesStorePath(String selfAgentName){
		String sessionid = PSatAPI.instance.sessionid;
		
		String folderName1 = PSatAPI.datastore_file_path+"/"+sessionid;
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertions";
		String folderName3 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertionRoles";
		String folderName4 = PSatAPI.datastore_file_path+"/"+sessionid+"/assertionRoles/"+selfAgentName;
		
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
		
		
		File folder4 = new File(folderName4);
		boolean exist4 = false;
		if(folder4.exists()){
			if(folder4.isDirectory()){
				exist4 = true;
			}				
		}
		if(!exist4){
			folder4.mkdir();
		}
				
		PSatAPI.instance.assertionRolesStorePath = folderName4;
	}

	
	public static boolean fillAssertionRolesStore(String selfAgentName){

		removeAllWorldsFromAssertionRolesStore(selfAgentName, PSatAPI.instance.sessionid);
		createAssertionRolesStorePath(selfAgentName);
		
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
		
//		if(PSatAPI.instance.collectiveStrategy != CollectiveStrategy.NONE){
//			addToAssertionRolesStore(new K0a(h),selfAgentName, sinstance);
//			addToAssertionRolesStore(new K0(h),selfAgentName, sinstance);
//		}
		
//		addToAssertionRolesStore(new K0a(h),selfAgentName, sinstance);
//		addToAssertionRolesStore(new K0(h),selfAgentName, sinstance);
		
		addToAssertionRolesStore(new K1a(self, h),selfAgentName);
		addToAssertionRolesStore(new K1(self, h),selfAgentName);
		
		addToAssertionRolesStore(new K23a(self, agent1, h),selfAgentName);
		addToAssertionRolesStore(new K23(self, agent1, h),selfAgentName);	
		
		addToAssertionRolesStore(new K31a(self, agent1, h),selfAgentName);
		addToAssertionRolesStore(new K31(self, agent1, h),selfAgentName);

		addToAssertionRolesStore(new K21a(self, agent1, h),selfAgentName);
		addToAssertionRolesStore(new K21(self, agent1, h),selfAgentName);
		
		addToAssertionRolesStore(new K24a(self, agent2, h),selfAgentName);
		addToAssertionRolesStore(new K24(self, agent2, h),selfAgentName);
		
		addToAssertionRolesStore(new K32a(self, agent2, h),selfAgentName);
		addToAssertionRolesStore(new K32(self, agent2, h),selfAgentName);
		
		addToAssertionRolesStore(new K22a(self, agent2, h),selfAgentName);
		addToAssertionRolesStore(new K22(self, agent2, h),selfAgentName);
		
		addToAssertionRolesStore(new K41a(self, agent1, agent2, h),selfAgentName);
		addToAssertionRolesStore(new K41(self, agent1, agent2, h),selfAgentName);
		
		addToAssertionRolesStore(new K42a(self, agent1, agent2, h),selfAgentName);
		addToAssertionRolesStore(new K42(self, agent1, agent2, h),selfAgentName);
		
		return true;
	}
	
	private static boolean addToAssertionRolesStore(World world,String selfAgentName){		
		if(PSatAPI.instance.assertionRolesStorePath ==null){
			createAssertionRolesStorePath(selfAgentName);
		}
		try{
			String fileName = PSatAPI.instance.assertionRolesStorePath+"/"+world.toString();

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
	        
	        return true;
	      }
		catch(IOException i){
	          i.printStackTrace();
	          return false;
	    }
		
	}
	
}
