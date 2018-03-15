package gla.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.KNode;
import gla.server.PSatAPI;
import gla.server.kernel.behaviour.InformationFlows;
import gla.server.kernel.behaviour.protocol.ProtocolFactory;
import gla.server.kernel.knowledge.ServerMemoryFactory;
import gla.server.kernel.knowledge.worlds.World;
import gla.server.kernel.util.AgentFactory;
import gla.server.kernel.util.PathsInGraph;
import gla.server.kernel.util.SatSerializer;
import gla.server.kernel.verification.ServerAssertionsFactory;
import gla.server.session.Config;
import gla.client.session.ClientServerBroker;
import gla.client.session.ServerConfigInstance;

public class PSatClient {
	static String sendersSessionId = Display.hostname;

	public static ConfigInstance netGenNewSession(){
				
		String tsessionid = UUID.randomUUID().toString();
		ConfigInstance tinstance = new ConfigInstance();
		ProtocolFactory.initProtocolSuite(tinstance);
		
		tinstance.sessionid = tsessionid;
		tinstance.satSerializer = new SatSerializer();
	
		Config.serialiseServerConfigInstance(tinstance);
		
		return tinstance;
	}
	
	public static ConfigInstance netGetSession(String sessionid){
		ConfigInstance tinstance = Config.deserialiseConfigInstance(sessionid);																		
		if(tinstance != null){
			Display.hostname = tinstance.sessionid;
		}
		return tinstance;
	}
	
	public static String[] netGetPathAgentNames(){
		ConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);

		ArrayList<String> pathAgentNames = sinstance.pathAgentNames;
		String[] pan = new String[pathAgentNames.size()];
		pathAgentNames.toArray(pan);						
		
		return pan;		
	}
	
	public static boolean netWriteAgent(Agent agent){
		
		ConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
		boolean done = AgentFactory.writeAgent(agent, sinstance);	
		
		return done;
	}
	
	public static Agent netGetAgent(String agentname){
		ConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
		
		Agent agent = AgentFactory.getAgent(agentname,sinstance);
		return agent;	
	}
	
	public static String [] netGetAgentNames(){
		ConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
		String [] agentnames = AgentFactory.getAgentNames(sinstance);
								
		return agentnames;		
	}
	
	public static String [] netGetAllPossibleAgentNames(){
		String[] names = AgentFactory.getAllPossibleNames();
		
		return names;
	}
	
	public static World [] netRetrieveRolePicks(){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);						
		String selfAgentName = instance.selfAgentName;
		
		World[] picks  =ServerAssertionsFactory.retrieveRolePicks(selfAgentName, instance);
		return picks;
	}
	
	
	public static boolean netAnalysePaths(){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
		
		boolean fin = new InformationFlows().run(instance.selectedPath,instance);
		return fin;		
	}
	
	public static boolean netAutoGenAgents(){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
		boolean fin = AgentFactory.autoGenAgents(instance);

		if(fin){
			Config.serialiseServerConfigInstance(instance);						
		}
		return fin;
	}
	
	public static void netRegenerateSequence(String path){
		ConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
		sinstance.kgraph.createNewSequence(sinstance, path);
		
	}
	
	public static void netSerialiseConfigInstance(){

		if(PSatAPI.instance.sessionid == null){
			return;
		}
		ClientServerBroker.messageEvent("PSatClient.netSerialiseConfigInstance()",null,null,PSatAPI.instance);
	}
		
	public static void netDeserialiseProcessPossibleWorldsPathToFile(String sessionid){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
		Config.deserialiseProcessPossibleWorldsPathToFile(instance);
		Config.serialiseConfigInstance(instance.sessionid, instance);
		
	}
	
	public static boolean netEmptySerialisedContent(){
		boolean fin = Config.emptySerialisedContent(sendersSessionId);

		return fin;     
	}
	
	public static String [] netFindKNearestneighbours(){
		
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

		String [] listPathsData = new PathsInGraph().findKNearestNeighbours(instance);
		return listPathsData;
			
	}
	
	public static Properties netFindSequenceSourceandTarget(){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
		Properties properties = new PathsInGraph().getSequenceSourceandTarget(instance);
		
		return properties;	
	}
	
	public static String [] netGetPaths(){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

		String [] listPathsData = PathsInGraph.getPaths(instance);						
		String[] paths = listPathsData;
		
		return paths;	
	}
	
	
	public static void netMutateEdges(KNode source, KNode target, String mutationType){
		ConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
		sinstance.kgraph.mutateEdges(source, target, sinstance,mutationType);
		       
	}
	
	public static Properties [] netDisplayAssertionsStore(String agentName, String partialPath){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
		
		Properties [] ppties = new ServerAssertionsFactory(agentName, instance).displayAssertionsStore(agentName, partialPath,instance);
		return ppties;
	}
	
	public static void netNewMemoryStore(final String agentname){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

		boolean done = ServerMemoryFactory.newMemoryStore(agentname,instance);
		
		if(done){
			Config.serialiseServerConfigInstance(instance);						
		}
	}
	

	public static void netNewMemoryStore(){
		ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

		boolean done = ServerMemoryFactory.newMemoryStore(instance);
		
		if(done){
			Config.serialiseServerConfigInstance(instance);
		}
	}
	
	
	
	public static String [] assertionstorepaths;
	public static String [] netGetAssertionsStorePaths(String agentName){
		assertionstorepaths = null;
		
		ClientServerBroker.messageEvent("PSatClient.netGetAssertionsStorePaths()", null, null,agentName);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetAssertionsStorePathsDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netGetAssertionsStorePaths()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetAssertionsStorePathsDone = false;
		return assertionstorepaths;	
	}
	
	public static String [] memorystorepaths;
	public static String [] netGetMemoryStorePaths(String agentName){
		memorystorepaths = null;
		
		ClientServerBroker.messageEvent("PSatClient.netGetMemoryStorePaths()", null, null,agentName);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetMemoryStorePathsDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netGetMemoryStorePaths()", true);
      			}
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetMemoryStorePathsDone = false;
		return memorystorepaths;
	}
	
	
	public static boolean agentAdded;	
	public static boolean netAddAgent(Agent agent1){
		agentAdded = false;
		
		ClientServerBroker.messageEvent("PSatClient.netAddAgent()", null,null,agent1);
    	int waittime= 0;
  		while(!ClientServerBroker.netAddAgentDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netAddAgent()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netAddAgentDone = false;
		
		return agentAdded;      
	}
	
	public static boolean clientaf;
	public static boolean netClientAssertionsFactory(String agentname){
		clientaf = false;
		
		ClientServerBroker.messageEvent("PSatClient.netClientAssertionsFactory()", null,null,agentname);
    	int waittime= 0;
  		while(!ClientServerBroker.netClientAssertionsFactoryDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netClientAssertionsFactory()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netClientAssertionsFactoryDone = false;
		
		return clientaf;        
	}
	
	public static boolean clientafi;
	public static boolean netClientAssertionsFactoryInit(){
		clientafi = false;
		
		ClientServerBroker.messageEvent("PSatClient.netClientAssertionsFactoryInit()", "", null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netClientAssertionsFactoryInitDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netClientAssertionsFactoryInit()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netClientAssertionsFactoryInitDone = false;
		
		return clientafi;     
	}
	
	public static boolean agentFactoryInitGraphExecuted;
	public static boolean netAgentFactoryInitGraph(){
		agentFactoryInitGraphExecuted = false;

		if(PSatAPI.instance.sessionid == null){
			return agentFactoryInitGraphExecuted;
		}
		
		ClientServerBroker.messageEvent("PSatClient.netAgentFactoryInitGraph()", "", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netAgentFactoryInitGraphDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding:-netAgentFactoryInitGraph()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netAgentFactoryInitGraphDone = false;
		
		return agentFactoryInitGraphExecuted;       
	}
	
	public static int noagents;
	public static int netGetNoAgents(){
		
		noagents = -1;
		ClientServerBroker.messageEvent("PSatClient.netGetNoAgents()", "", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetNoAgentsDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netGetNoAgents()", true);
      				waittime = waittime+1;
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetNoAgentsDone = false;	
  		if(noagents <=0){
			Display.updateLogPage("empty graph-", true);
  		}
		return noagents; 
	}
	
	public static boolean privacyRequirementsRolesExecuted;
	public static boolean netPrivacyRequirementRoles(String agentname){
		privacyRequirementsRolesExecuted = false;
		
		ClientServerBroker.messageEvent("PSatClient.netPrivacyRequirementRoles()", null, null,agentname);
    	int waittime= 0;
  		while(!ClientServerBroker.netPrivacyRequirementRolesDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netPrivacyRequirementRoles()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netPrivacyRequirementRolesDone = false;
		return privacyRequirementsRolesExecuted;
	}
	
//	public static boolean writeToSatPathTrainingStoreExecuted;
//	public static boolean netWriteToSatPathTrainingStore(String pathDesc, String protocolDesc, double sat){
//	public static void netWriteToSatPathTrainingStore(String pathDesc, String protocolDesc, double sat){
//
//		
////		writeToSatPathTrainingStoreExecuted = false;
//		Properties ppties = new Properties();
//		ppties.setProperty("pathDesc", pathDesc);
//		ppties.setProperty("protocolDesc", protocolDesc);
//		ppties.setProperty("sat", ""+sat);
//		
//		ClientServerBroker.messageEvent("PSatClient.netWriteToSatPathTrainingStore()", null, null,ppties);
////    	int waittime= 0;
////  		while(!ClientServerBroker.netWriteToSatPathTrainingStoreDone){
////  			try {
////  				Thread.sleep(1000);
////  				if(waittime >ClientServerBroker.MAXWAITTIME){
////      				Display.updateLogPage("Wait Time: Message Server not Responding-netWriteToSatPathTrainingStore()", true);
////      			}
////  				waittime = waittime+1;
////  			} catch (InterruptedException e) {
////  				e.printStackTrace();
////  			}			
////  		}
////  		ClientServerBroker.netWriteToSatPathTrainingStoreDone = false;
////		return writeToSatPathTrainingStoreExecuted;
//		
//	}
	
//	public static boolean writeToSatPathAnalysisStoreExecuted;
//	public static boolean netWriteToSatPathAnalysisStore(String pathDesc, String protocolDesc, double sat){
//	public static void netWriteToSatPathAnalysisStore(String pathDesc, String protocolDesc, double sat){
//		
////		writeToSatPathAnalysisStoreExecuted = false;
//		Properties ppties = new Properties();
//		ppties.setProperty("pathDesc", pathDesc);
//		ppties.setProperty("protocolDesc", protocolDesc);
//		ppties.setProperty("sat", ""+sat);
//		
//		ClientServerBroker.messageEvent("PSatClient.netWriteToSatPathAnalysisStore()", null, null,ppties);
////    	int waittime= 0;
////  		while(!ClientServerBroker.netWriteToSatPathAnalysisStoreDone){
////  			try {
////  				Thread.sleep(1000);
////  				if(waittime >ClientServerBroker.MAXWAITTIME){
////      				Display.updateLogPage("Wait Time: Message Server not Responding-netWriteToSatPathAnalysisStore()", true);
////      			}
////  				waittime = waittime+1;
////  			} catch (InterruptedException e) {
////  				e.printStackTrace();
////  			}			
////  		}
////  		ClientServerBroker.netWriteToSatPathAnalysisStoreDone = false;
////		return writeToSatPathAnalysisStoreExecuted;     
//	}
	
	public static double averageClusteringCoefficient;
	public static double netAverageClusteringCoefficient(){
		
		averageClusteringCoefficient = -1;
		
		ClientServerBroker.messageEvent("PSatClient.netAverageClusteringCoefficient()", "", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netAverageClusteringCoefficientDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
      				Display.updateLogPage("Wait Time: Message Server not Responding-netAverageClusteringCoefficient()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netAverageClusteringCoefficientDone = false;
		
		return averageClusteringCoefficient;      
	}
	
	public static double averageOfAverageDistance;
	public static double netAverageofAverageDistance(){
		
		averageOfAverageDistance = -1;
		
		ClientServerBroker.messageEvent("PSatClient.netAverageofAverageDistance()", "", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netAverageofAverageDistanceDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netAverageofAverageDistance()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netAverageofAverageDistanceDone = false;
		
		return averageOfAverageDistance;    
	}
	
	public static double diameter;
	public static double netDiameter(){
		diameter = -1;
		
		ClientServerBroker.messageEvent("PSatClient.netDiameter()", "", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netDiameterDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netDiameter()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netDiameterDone = false;
		
		return diameter;       
	}
	
}
