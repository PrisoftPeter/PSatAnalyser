package gla.prisoft.client;

import java.util.HashMap;
import java.util.Properties;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.KNode;
import gla.prisoft.client.session.ClientServerBroker;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.worlds.World;

public class PSatClient {
	
	public static ConfigInstance ninstance;
	public static ConfigInstance netGenNewSession(){
		ninstance = null;
		ClientServerBroker.messageEvent("PSatClient.netGenNewSession()","",  null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGenNewSessionDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
      				//Display.updateLogPage("Wait Time: Message Server not Responding-netGenNewSession()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGenNewSessionDone = false;
		
        return ninstance;
	}
	
	public static ConfigInstance rinstance;
	public static ConfigInstance netGetSession(String sessionid){
		rinstance = null;
		ClientServerBroker.messageEvent("PSatClient.netGetSession()", sessionid, null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetSessionDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
      				//Display.updateLogPage("Wait Time: Message Server not Responding-netGetSession()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetSessionDone = false;
		
        return rinstance;
	}
	
	public static String[] pathagentnames;
	public static String[] netGetPathAgentNames(){
		pathagentnames = null;
		ClientServerBroker.messageEvent("PSatClient.getpathagentnames()","", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetPathAgentNamesDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-getpathagentnames()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetPathAgentNamesDone = false;
		
        return pathagentnames;
	}
	
	public static boolean netWriteAgent;
	public static boolean netWriteAgent(Agent agent){
		netWriteAgent = false;
		ClientServerBroker.messageEvent("PSatClient.netWriteAgent()",null,null,agent);
    	int waittime= 0;
  		while(!ClientServerBroker.netWriteAgentDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netWriteAgent()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netWriteAgentDone = false;
		
		return netWriteAgent;	
	}
	
	public static Agent agent;
	public static Agent netGetAgent(String agentname){
		agent = null;
		ClientServerBroker.messageEvent("PSatClient.netGetAgent()", agentname, null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetAgentDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netGetAgent()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetAgentDone = false;
		
		return agent;	
	}
	
	public static String[] agentNames;
	public static String [] netGetAgentNames(){
		agentNames = null;
		ClientServerBroker.messageEvent("PSatClient.netGetAgentNames()", "", null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetAgentNamesDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netGetAgentNames()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetAgentNamesDone = false;
		
		return agentNames;
	}
	
	public static String [] allpossibleagentnames; 
	public static String [] netGetAllPossibleAgentNames(){
		allpossibleagentnames = null;
		
		ClientServerBroker.messageEvent("PSatClient.getAllPossibleNames()", "", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetAllPossibleNamesDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-getAllPossibleNames()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetAllPossibleNamesDone = false;
		return allpossibleagentnames;
	}
	
	public static World picks [];
	public static World [] netRetrieveAspectPicks(){
		picks = null;
		
		ClientServerBroker.messageEvent("PSatClient.retrieveAspectPicks()", "", null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netRetrieveAspectPicksDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-retrieveAspectPicks()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netRetrieveAspectPicksDone = false;
		return picks;
	}
	
//	public static boolean createanalysisvaibleprotocolratiostore = false;
//	public static boolean netCreateAnalysisVaibleProtocolRatioStore(){
//		createanalysisvaibleprotocolratiostore = false;
//		ClientServerBroker.messageEvent("PSatClient.netCreateAnalysisVaibleProtocolRatioStore()","", null,null);
//    	int waittime= 0;
//  		while(!ClientServerBroker.netCreateAnalysisVaibleProtocolRatioStoreDone){
//  			try {
//  				Thread.sleep(1000);
//  				if(waittime >ClientServerBroker.MAXWAITTIME){
////      				Display.updateLogPage("Wait Time: Message Server not Responding-netCreateAnalysisVaibleProtocolRatioStore()", true);
//      			}
//  				waittime = waittime+1;
//  			} catch (InterruptedException e) {
//  				e.printStackTrace();
//  			}			
//  		}
//  		ClientServerBroker.netCreateAnalysisVaibleProtocolRatioStoreDone = false;
//		return createanalysisvaibleprotocolratiostore;
//	}
	
	public static boolean pathsAnalysed = false;
	public static boolean netAnalysePaths(){
		pathsAnalysed = false;
		ClientServerBroker.messageEvent("PSatClient.netAnalysePaths()","", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netAnalysePathsDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netAnalysePaths()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netAnalysePathsDone = false;
		return pathsAnalysed; 
	}
	
	public static boolean agentsAutoGenerated = false;
	public static boolean netAutoGenAgents(){
		agentsAutoGenerated = false;
		
		ClientServerBroker.messageEvent("PSatClient.netAutoGenAgents()","", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netAutoGenAgentsDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netAutoGenAgents()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netAutoGenAgentsDone = false;
		return agentsAutoGenerated; 
	}
	
	public static void netSerialiseConfigInstance(){

		if(PSatAPI.instance.sessionid == null){
			return;
		}
		ClientServerBroker.messageEvent("PSatClient.netSerialiseConfigInstance()",null,null,PSatAPI.instance);
	}
		
	public static ConfigInstance dinstance = null;
	public static boolean netDeserialiseProcessPossibleWorldsPathToFile(String sessionid){
		dinstance = null;
		if(sessionid == null){
			return false;
		}
		
		ClientServerBroker.messageEvent("PSatClient.netDeserialiseProcessPossibleWorldsPathToFile()","", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netDeserialiseProcessPossibleWorldsPathToFileDone){
  			try {
  				if(dinstance != null){
  					PSatAPI.instance = dinstance;
  					return true;
  				}
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netDeserialiseProcessPossibleWorldsPathToFile()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netDeserialiseProcessPossibleWorldsPathToFileDone = false;
		return false;
	}
	
	public static boolean seralisedContentEmptied = false;
	public static boolean netEmptySerialisedContent(){
		seralisedContentEmptied = false;
		
		ClientServerBroker.messageEvent("PSatClient.netEmptySerialisedContent()","", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netEmptySerialisedContentDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netEmptySerialisedContent()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netEmptySerialisedContentDone = false;
		return seralisedContentEmptied;       
	}
	
	public static String listPathsData [];		
	public static String [] netFindKNearestneighbours(){
		listPathsData = null;

		ClientServerBroker.messageEvent("PSatClient.netFindKNearestneighbours()", "", null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netFindKNearestneighboursDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netFindKNearestneighbours()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netFindKNearestneighboursDone = false;
		return listPathsData;		
	}
	
	public static Properties properties;		
	public static Properties netFindSequenceSourceandTarget(){
		properties = null;

		ClientServerBroker.messageEvent("PSatClient.netFindSequenceSourceandTarget()", "", null, null);
    	int waittime= 0;
  		while(!ClientServerBroker.netFindSequenceSourceandTargetDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netFindKNearestneighbours()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netFindSequenceSourceandTargetDone = false;
		return properties;		
	}
	
	public static String[] paths;
	public static String [] netGetPaths(){
		paths = null;
		
		ClientServerBroker.messageEvent("PSatClient.netGetPaths()", "", null,null);
    	int waittime= 0;
  		while(!ClientServerBroker.netGetPathsDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netGetPaths()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netGetPathsDone = false;
		return paths;	
	}
	
//	public static boolean protolSuiteInitexecuted;
//	public static boolean netInitProtocolSuite(){
//		protolSuiteInitexecuted = false;
//		
//		ClientServerBroker.triggerPSatHostEvent("PSatClient.netInitProtocolSuite()","");
//    	int waittime= 0;
//  		while(!ClientServerBroker.netInitProtocolSuiteDone){
//  			try {
//  				Thread.sleep(1000);
//  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding", true);
//      			}
//  			} catch (InterruptedException e) {
//  				e.printStackTrace();
//  			}			
//  		}
//  		ClientServerBroker.netInitProtocolSuiteDone = false;
//		return protolSuiteInitexecuted;  
//	}
	
//	public static boolean evaluatedProtocolAdded;	
//	public static boolean netAddToEvaluatedProtocols(String pdesc){
//		evaluatedProtocolAdded = false;
//		
//		ClientServerBroker.triggerPSatHostEvent("PSatClient.netAddToEvaluatedProtocols()",null, pdesc);
//    	int waittime= 0;
//  		while(!ClientServerBroker.netAddToEvaluatedProtocolsDone){
//  			try {
//  				Thread.sleep(1000);
//  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netAddToEvaluatedProtocols()", true);
//      			}
//  				waittime = waittime+1;
//  			} catch (InterruptedException e) {
//  				e.printStackTrace();
//  			}			
//  		}
//  		ClientServerBroker.netAddToEvaluatedProtocolsDone = false;
//		
//  		return evaluatedProtocolAdded;        
//	}
	
	public static boolean edgesmutated;	
	public static boolean netMutateEdges(KNode source, KNode target, String mutationType){
		edgesmutated = false;
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("source", source);
		hm.put("target", target);
		
		Properties properties = new Properties();
		properties.setProperty("mutationType", mutationType);
		
		ClientServerBroker.messageEvent("PSatClient.netMutateEdges()","",properties, hm);
    	int waittime= 0;
  		while(!ClientServerBroker.netMutateEdgesDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netMutateEdges()", true);
      				break;
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netMutateEdgesDone = false;
		
  		return edgesmutated;        
	}
	
//	public static boolean evaluatedProtocolRemoved;
//	public static boolean netRemoveFromEvaluatedProtocols(String pdesc){
//		evaluatedProtocolRemoved = false;
//
//		ClientServerBroker.triggerPSatHostEvent("PSatClient.netRemoveFromEvaluatedProtocols()",null, pdesc);
//    	int waittime= 0;
//  		while(!ClientServerBroker.netRemoveFromEvaluatedProtocolsDone){
//  			try {
//  				Thread.sleep(1000);
//  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netRemoveFromEvaluatedProtocols()", true);
//      			}
//  				waittime = waittime+1;
//  			} catch (InterruptedException e) {
//  				e.printStackTrace();
//  			}			
//  		}
//  		ClientServerBroker.netRemoveFromEvaluatedProtocolsDone = false;
//		
//  		return evaluatedProtocolRemoved;      
//	}
	
	public static Properties [] listassertns;
	public static Properties [] netDisplayAssertionsStore(String agentName, String partialPath){
		listassertns = null;
		
		Properties input = new Properties();
		input.setProperty("agentName", agentName);
		input.setProperty("partialPath", partialPath);
		
		ClientServerBroker.messageEvent("PSatClient.netDisplayAssertionsStore()",null, null, input);
    	int waittime= 0;
  		while(!ClientServerBroker.netDisplayAssertionsStoreDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netDisplayAssertionsStore()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netDisplayAssertionsStoreDone = false;
		
  		return listassertns;
	}
	
	public static boolean newMemoryStoreCreated_single;
//	public static boolean netNewMemoryStore(String agentname){
	public static void netNewMemoryStore(final String agentname){

		Thread queryThreadx = new Thread() {
			public void run() {
				newMemoryStoreCreated_single = false;
				
				ClientServerBroker.messageEvent("PSatClient.netNewMemoryStoreSingle()",null, null,agentname);
		    	int waittime= 0;
		  		while(!ClientServerBroker.netNewMemoryStoreDoneSingleDone){
		  			try {
		  				Thread.sleep(1000);
		  				if(waittime >ClientServerBroker.MAXWAITTIME){
//		      				Display.updateLogPage("Wait Time: Message Server not Responding-netNewMemoryStoreSingle()", true);
		      			}
		  				waittime = waittime+1;
		  			} catch (InterruptedException e) {
		  				e.printStackTrace();
		  			}			
		  		}
		  		ClientServerBroker.netNewMemoryStoreDoneSingleDone = false;
				
//		  		return newMemoryStoreCreated_single;
			}
		};
		queryThreadx.start();
		
	}
	
//	public static boolean newMemoryStoreCreated_multiple;
//	public static boolean netNewMemoryStore(){
	public static void netNewMemoryStore(){

		Thread queryThreadx = new Thread() {
			public void run() {
				ClientServerBroker.messageEvent("PSatClient.netNewMemoryStoreMultiple()","", null, null);
//		    	int waittime= 0;
//		  		while(!ClientServerBroker.netNewMemoryStoreDoneMultipleDone){
//		  			try {
//		  				Thread.sleep(1000);
//		  				if(waittime >ClientServerBroker.MAXWAITTIME){
//		      				Display.updateLogPage("Wait Time: Message Server not Responding-netNewMemoryStoreMultiple()", true);
//		      			}
//		  				waittime = waittime+1;
//		  			} catch (InterruptedException e) {
//		  				e.printStackTrace();
//		  			}			
//		  		}
//		  		ClientServerBroker.netNewMemoryStoreDoneMultipleDone = false;
			}
		};
		queryThreadx.start();
		
//		newMemoryStoreCreated_multiple = false;
		
		
		
//  		return newMemoryStoreCreated_multiple;
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
	
	public static boolean privacyRequirementsAspectsExecuted;
	public static boolean netPrivacyRequirementAspects(String agentname){
		privacyRequirementsAspectsExecuted = false;
		
		ClientServerBroker.messageEvent("PSatClient.netPrivacyRequirementAspects()", null, null,agentname);
    	int waittime= 0;
  		while(!ClientServerBroker.netPrivacyRequirementAspectsDone){
  			try {
  				Thread.sleep(1000);
  				if(waittime >ClientServerBroker.MAXWAITTIME){
//      				Display.updateLogPage("Wait Time: Message Server not Responding-netPrivacyRequirementAspects()", true);
      			}
  				waittime = waittime+1;
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}			
  		}
  		ClientServerBroker.netPrivacyRequirementAspectsDone = false;
		return privacyRequirementsAspectsExecuted;
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
