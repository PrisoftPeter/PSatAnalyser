package gla.client.session;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import gla.client.Display;
import gla.client.PSatClient;
import gla.client.kernel.display.model.ClientKNetworkGraph;
import gla.client.kernel.display.model.FeasibilityView;
import gla.client.kernel.display.model.LayeredBarChart;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.KLink;
import gla.prisoft.shared.KNode;
import gla.prisoft.shared.PSatTableResult;
import gla.server.PSatAPI;
import gla.server.kernel.behaviour.InformationFlows;
import gla.server.kernel.behaviour.protocol.ServerProtocolFactory;
import gla.server.kernel.knowledge.ServerMemoryFactory;
import gla.server.kernel.knowledge.worlds.World;
import gla.server.kernel.util.GraphAnalyser;
import gla.server.kernel.util.PathsInGraph;
import gla.server.kernel.util.ServerAgentFactory;
import gla.server.kernel.util.ServerKNetworkGraph;
import gla.server.kernel.util.ServerSatSerializer;
import gla.server.kernel.verification.ServerAssertionsFactory;
import gla.server.session.Config;
import gla.server.session.ServerConfigInstance;

public class ClientServerBroker{
	
	public static final int MAXWAITTIME = 10;

	public static void messageEvent(final String actionType,final String actionValue,final Properties ppties, final Object obj){

		Thread bridgeThread = new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				String sendersSessionId = Display.hostname;
				//String intendedRecipientSessionId = psathost; 
				
				if(obj == null){					
					if(actionType.equals("PSatClient.netGenNewSession()")){
						String tsessionid = UUID.randomUUID().toString();
						ConfigInstance tinstance = new ConfigInstance();
						ServerProtocolFactory.initProtocolSuite(tinstance);
						
//						tinstance.sessionid = sendersSessionId+tsessionid;
						tinstance.sessionid = tsessionid;
						ServerConfigInstance tsinstance = new ServerConfigInstance();
						tsinstance.serverSatSerializer = new ServerSatSerializer();
					
//						tsinstance.sessionid = sendersSessionId+tsessionid;
						tsinstance.sessionid = tsessionid;
						Config.serialiseServerConfigInstance(tinstance.sessionid, tsinstance);
						
						if(Display.hostname.equals(sendersSessionId)){
							PSatClient.ninstance = tinstance;
							Display.hostname = tinstance.sessionid;
							netGenNewSessionDone = true;
						}
					}
					else if(actionType.equals("PSatClient.netRegenerateSequence()")){
						String path = actionValue;
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						sinstance.kgraph.createNewSequence(sinstance, path);
						netRegenerateSequenceDone = true;
					}
					else if(actionType.equals("PSatClient.netGetSession()")){
						String tsessionid = actionValue;
						ConfigInstance tinstance = Config.deserialiseConfigInstance(tsessionid);																		
						if(tinstance == null){
							tinstance = PSatAPI.instance;
						}
						if(Display.hostname.equals(sendersSessionId)){
							PSatClient.rinstance = tinstance;
							Display.hostname = tinstance.sessionid;
							netGetSessionDone = true;
						}	
					}
					else if(actionType.equals("PSatClient.getpathagentnames()")){
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);

						ArrayList<String> pathAgentNames = sinstance.pathAgentNames;
						String[] pan = new String[pathAgentNames.size()];
						pathAgentNames.toArray(pan);						
						
						PSatClient.pathagentnames = pan;
						netGetPathAgentNamesDone = true;
					}
					else if(actionType.equals("PSatClient.netGetAgent()")){
						String agentname = actionValue;
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						if(sinstance == null){
							sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						}
						Agent agent = ServerAgentFactory.getAgent(agentname,sinstance);
												
						PSatClient.agent = agent;
						netGetAgentDone = true;
					}
					else if(actionType.equals("PSatClient.netGetAgentNames()")){
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						String [] agentnames = ServerAgentFactory.getAgentNames(sinstance);
												
						PSatClient.agentNames = agentnames;
						netGetAgentNamesDone = true;
						Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
					}
					else if(actionType.equals("PSatClient.getAllPossibleNames()")){
						String[] names = ServerAgentFactory.getAllPossibleNames();
						
						PSatClient.allpossibleagentnames = names;
						netGetAllPossibleNamesDone = true;
					}
					else if(actionType.equals("PSatClient.retrieveRolePicks()")){
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);						
						String selfAgentName = instance.selfAgentName;
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						
						World[] picks  =ServerAssertionsFactory.retrieveRolePicks(selfAgentName, sinstance,instance);
						PSatClient.picks = picks;
						netRetrieveRolePicksDone = true;
					}
//					else if(actionType.equals("PSatClient.netCreateAnalysisVaibleProtocolRatioStore()")){						
//						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
//						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
//						if(sinstance.serversatserializer == null){
//							sinstance.serversatserializer = new ServerSatSerializer();
//						}
//						boolean created = sinstance.serversatserializer.createAnalysisVaibleProtocolRatioStore(instance,sinstance);
//						if(created){
//							PSatClient.createanalysisvaibleprotocolratiostore = true;
//						}
//						else{
//							PSatClient.createanalysisvaibleprotocolratiostore = false;
//							Display.updateLogPage("PSatClient.createanalysisvaibleprotocolratiostore():failed", true);	
//						}						
//						Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
//
//						netCreateAnalysisVaibleProtocolRatioStoreDone = true;
//					}
					else if(actionType.equals("PSatClient.netAnalysePaths()")){	
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
						if(instance == null){
							instance = Config.deserialiseConfigInstance(sendersSessionId);
						}
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);

						boolean fin = new InformationFlows().run(instance.selectedPath, sinstance,instance);
						
						if(fin){
							PSatClient.pathsAnalysed = true;
						}
						else{
							PSatClient.pathsAnalysed = false;
							Display.updateLogPage("PSatClient.netAnalysePaths():failed", true);		
						}
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
						netAnalysePathsDone = true;
					}
					else if(actionType.equals("PSatClient.netAutoGenAgents()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
						boolean fin = ServerAgentFactory.autoGenAgents(sinstance, instance);

						if(fin){
							PSatClient.agentsAutoGenerated = true;						
						}
						else{
							PSatClient.agentsAutoGenerated = false;
							Display.updateLogPage("PSatClient.netAutoGenAgents():failed", true);	
						}
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);						
						netAutoGenAgentsDone = true;
					}
					else if(actionType.equals("PSatClient.netDeserialiseProcessPossibleWorldsPathToFile()")){
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
						Config.deserialiseProcessPossibleWorldsPathToFile(instance);
						Config.serialiseConfigInstance(instance.sessionid, instance);
						
						PSatClient.dinstance = instance;
						netDeserialiseProcessPossibleWorldsPathToFileDone = true;
					}
					else if(actionType.equals("PSatClient.netEmptySerialisedContent()")){	
						boolean fin = Config.emptySerialisedContent(sendersSessionId);

						if(fin){
							PSatClient.seralisedContentEmptied = true;							
						}
						else{
							PSatClient.seralisedContentEmptied = false;
//							Display.updateLogPage("PSatClient.netEmptySerialisedContent():failed", true);	
						}
						netEmptySerialisedContentDone = true;
					}
					else if(actionType.equals("PSatClient.netFindKNearestneighbours()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

						String [] listPathsData = new PathsInGraph().findKNearestNeighbours(sinstance.g,sinstance,instance);
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
						
						PSatClient.listPathsData = listPathsData;
						netFindKNearestneighboursDone = true;
					}
					else if(actionType.equals("PSatClient.netFindSequenceSourceandTarget()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

						PSatClient.properties = new PathsInGraph().getSequenceSourceandTarget(sinstance.g,sinstance,instance);
						netFindSequenceSourceandTargetDone = true;
					}
					else if(actionType.equals("PSatClient.netGetPaths()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

						String [] listPathsData = PathsInGraph.getPaths(sinstance.g,sinstance,instance);						
						PSatClient.paths = listPathsData;
						Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
						netGetPathsDone = true;
					}
					else if(actionType.equals("PSatClient.netNewMemoryStoreMultiple()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

						boolean done = ServerMemoryFactory.newMemoryStore(sinstance,instance);
						
						if(done){
							//PSatClient.newMemoryStoreCreated_multiple = true;							
						}
						else{
							//PSatClient.newMemoryStoreCreated_multiple = false;
//							Display.updateLogPage("PSatClient.netNewMemoryStoreMultiple():failed", true);	
						}
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
						
						netNewMemoryStoreDoneMultipleDone = true;
					}
					else if(actionType.equals("PSatClient.netClientAssertionsFactoryInit()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);

						boolean done = ServerAssertionsFactory.init(sinstance);
								
						if(done){
							PSatClient.clientafi = true;						
						}
						else{
							PSatClient.clientafi = false;
//							Display.updateLogPage("PSatClient.netClientAssertionsFactoryInit():failed", true);	
						}
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
						netClientAssertionsFactoryInitDone = true;						
					}
					else if(actionType.equals("PSatClient.netAgentFactoryInitGraph()")){							
						boolean done = false;
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						if(sinstance.kgraph == null){
							done = ServerAgentFactory.initGraph(sinstance);
						}													
						if(done){
							PSatClient.agentFactoryInitGraphExecuted = true;						
						}
						else{
							PSatClient.agentFactoryInitGraphExecuted = false;
//							Display.updateLogPage("PSatClient.netAgentFactoryInitGraph():failed", true);	
						}
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
				
						netAgentFactoryInitGraphDone = true;
					}
					else if(actionType.equals("PSatClient.netGetNoAgents()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						int noagents = sinstance.agents.length;												
						
						PSatClient.noagents = noagents;
						netGetNoAgentsDone = true;
					}
					else if(actionType.equals("PSatClient.netAverageofAverageDistance()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						double acc = GraphAnalyser.averageofAverageDistance(sinstance.g);
						
						PSatClient.averageOfAverageDistance = acc;
						netAverageofAverageDistanceDone = true;
					}
					else if(actionType.equals("PSatClient.netAverageClusteringCoefficient()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						double acc = GraphAnalyser.averageClusteringCoefficient(sinstance.g);
																		
						PSatClient.averageClusteringCoefficient = acc;
						netAverageClusteringCoefficientDone = true;
					}
					else if(actionType.equals("PSatClient.netDiameter()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						double acc = GraphAnalyser.diameter(sinstance.g);

						PSatClient.diameter = acc;
						netDiameterDone = true;
					}
					else if(actionType.equals("ServerKNetworkGraph.createGraph()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						sinstance.kgraph.createGraph(sinstance);
						
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);					
						creategraphdone = true;
					}
					else if(actionType.equals("ServerConfigInstance.g")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ClientKNetworkGraph.g = sinstance.g;
					}
					else if(actionType.equals("ServerKNetworkGraph.networkNodes")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						KNode [] networknodes= sinstance.kgraph.networkNodes;						
						ClientKNetworkGraph.networkNodes = networknodes;
					}
					else if(actionType.equals("updateLogPage")){
						final String av = actionValue;
						String s[] = av.split("₦");
						String info = s[0];
						boolean status = new Boolean(s[1]);
						Display.updateLogPage(info, status);
										
					}
					else if(actionType.equals("updateProgressComponent")){	
						final String av = actionValue;
						String s[] = av.split("₦");
						int progress = new Integer(s[0]);
						String info = "";
						if(s.length>1){
							info = s[1];
						}			
						Display.updateProgressComponent(progress, info);
					}
					else if(actionType.equals("Display.updatePathsList()")){	
						Display.updatePathsList();				
					}
					else if(actionType.equals("Display.listbox.setSelectedIndex()")){	
						int listIndex = new Integer(actionValue);
						Display.listbox.setSelectedIndex(listIndex);
					}
					else if(actionType.equals("Display.trainMaxTimeSeriesChart.addMarker()")){	
						String pathId = actionValue;
						Display.feasibilityView.timeSeriesChart.addMarker(":"+pathId);
					}
					else if(actionType.equals("Display.analysisMaxTimeSeriesChart.addMarker()")){	
						String pathId = actionValue;
						Display.feasibilityView.timeSeriesChart.addMarker(":"+pathId);
					}
					else if(actionType.equals("ClientKNetworkGraph.resetColoredLinks()")){	
						ClientKNetworkGraph.resetColoredLinks();				
					}
					else if(actionType.equals("ClientKNetworkGraph.resetColoredNodes()")){	
						ClientKNetworkGraph.resetColoredNodes();
					}
					else if(actionType.equals("Display.updateNetworkNode()")){	
						Display.updateNetworkNode();
					}
					else if(actionType.equals("info.apathcompleted")){
						PSatAPI.instance.runningTraining = false;
						PSatClient.netSerialiseConfigInstance();
						LayeredBarChart lbc = new LayeredBarChart();
						Display.window.createLayeredDecisionBarViewPage(lbc);
						FeasibilityView.ptrs = new ArrayList<PSatTableResult>();

					}
				}
				else{
					
					if(actionType.equals("PSatClient.netWriteAgent()")){
						
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						boolean done = ServerAgentFactory.writeAgent((Agent)obj, sinstance);	
						if(done){
							PSatClient.netWriteAgent = true;						
						}
						else{
							PSatClient.netWriteAgent = false;
//							Display.updateLogPage("PSatClient.netWriteAgent():failed", true);	
						}						
						netWriteAgentDone = true;
					}
					else if(actionType.equals("PSatClient.netSerialiseConfigInstance()")){	
						ConfigInstance instance = (ConfigInstance)obj;
						boolean fin = Config.serialiseConfigInstance(sendersSessionId, instance);

						if(fin){
							//PSatClient.configInstanceSeralised = true;							
						}
						else{
//							Display.updateLogPage("PSatClient.netSerialiseConfigInstance():failed", true);
						}	
						netSerialiseConfigInstanceDone = true;
					}
					else if(actionType.equals("PSatClient.netMutateEdges()")){	
						HashMap<String, Object> hm = (HashMap<String, Object>)obj;
						KNode source = (KNode)hm.get("source");
						KNode target = (KNode)hm.get("target");
						
						String mutationType = ppties.getProperty("mutationType");
						
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						sinstance.kgraph.mutateEdges(source, target, sinstance,mutationType);
						
						netMutateEdgesDone = true;
					}
					else if(actionType.equals("PSatClient.netDisplayAssertionsStore()")){	
						Properties input = (Properties)obj;
						String agentName = input.getProperty("agentName");
						String partialPath = input.getProperty("partialPath");
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
						if(instance == null){
							instance = Config.deserialiseConfigInstance(sendersSessionId);
						}
						Properties [] ppties = new ServerAssertionsFactory(agentName, sinstance).displayAssertionsStore(agentName, partialPath, sinstance,instance);
												
						PSatClient.listassertns = ppties;
						netDisplayAssertionsStoreDone = true;
						
					}
					else if(actionType.equals("PSatClient.netNewMemoryStoreSingle()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

						boolean done = ServerMemoryFactory.newMemoryStore((String)obj,sinstance,instance);
						
						if(done){
							PSatClient.newMemoryStoreCreated_single = true;						
						}
						else{
							PSatClient.newMemoryStoreCreated_single = false;
//							Display.updateLogPage("PSatClient.netNewMemoryStoreSingle():failed", true);		
						}
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);						
						
						netNewMemoryStoreDoneSingleDone = true;
					}
					else if(actionType.equals("PSatClient.netGetAssertionsStorePaths()")){	
						String[] memorystorepaths = ServerMemoryFactory.getAssertionsStorePaths((String)obj, sendersSessionId);
						
						PSatClient.assertionstorepaths = memorystorepaths;
						netGetAssertionsStorePathsDone = true;
						
					}
					else if(actionType.equals("PSatClient.netGetMemoryStorePaths()")){	
						String[] memorystorepaths = ServerMemoryFactory.getMemoryStorePaths((String)obj, sendersSessionId);

						PSatClient.memorystorepaths = memorystorepaths;
						netGetMemoryStorePathsDone = true;
					}
					else if(actionType.equals("PSatClient.netAddAgent()")){	
						ServerConfigInstance instance = Config.deserialiseServerConfigInstance(sendersSessionId);
						boolean done=ServerAgentFactory.addAgent((Agent)obj, instance);
						Config.serialiseServerConfigInstance(sendersSessionId, instance);
						
						if(done){
							PSatClient.agentAdded = true;						
						}
						else{
							PSatClient.agentAdded = false;
//							Display.updateLogPage("PSatClient.netAddAgent():failed", true);	
						}
						
						if(actionValue !=null && actionValue.equals("failed")){
							PSatClient.agentAdded = false;
							Display.updateLogPage("PSatClient.netAddAgent():failed", true);					
						}
						netAddAgentDone = true;
						
					}
					else if(actionType.equals("PSatClient.netClientAssertionsFactory()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						sinstance.agentName = (String)obj;
						boolean done = Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
												
						if(done){
							PSatClient.clientaf = true;							
						}
						else{
							PSatClient.clientaf = false;
//							Display.updateLogPage("PSatClient.netClientAssertionsFactory():failed", true);	
						}
						netClientAssertionsFactoryDone = true;					
						
					}
					else if(actionType.equals("PSatClient.netPrivacyRequirementRoles()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);

						boolean done = ServerMemoryFactory.privacyRequirementRoles((String)obj, sinstance,instance);
			
						if(done){
							PSatClient.privacyRequirementsRolesExecuted = true;						
						}
						else{
							PSatClient.privacyRequirementsRolesExecuted = false;
							Display.updateLogPage("PSatClient.netPrivacyRequirementRoles():failed", true);
						}
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
						netPrivacyRequirementRolesDone = true;
					}
//					else if(actionType.equals("PSatClient.netWriteToSatPathTrainingStore()")){	
//						Properties ppties = (Properties)obj;
//						String pathDesc = ppties.getProperty("pathDesc");
//						String protocolDesc = ppties.getProperty("protocolDesc");
//						double sat_s = new Double(ppties.getProperty("sat"));
//						
//						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
//						if(sinstance.serversatserializer == null){
//							sinstance.serversatserializer = new ServerSatSerializer();
//							Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
//							
//						}		
//						boolean done = sinstance.serversatserializer.writeToSatPathTrainingStore(pathDesc, protocolDesc, new Double(sat_s),sinstance);
//															
//						if(done){
//							//						
//						}
//						else{
////							Display.updateLogPage("PSatClient.netWriteToSatPathTrainingStore():failed", true);	
//						}
//						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);						
//						//netWriteToSatPathTrainingStoreDone = true;
//					
//					}
//					else if(actionType.equals("PSatClient.netWriteToSatPathAnalysisStore()")){	
//						Properties ppties = (Properties)obj;
//						String pathDesc = ppties.getProperty("pathDesc");
//						String protocolDesc = ppties.getProperty("protocolDesc");
//						double sat_s = new Double(ppties.getProperty("sat"));
//						
//						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
//						ConfigInstance instance = Config.deserialiseConfigInstance(sendersSessionId);
//						
//						if(sinstance.serversatserializer == null){
//							sinstance.serversatserializer = new ServerSatSerializer();
//							Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
//						}		
//						boolean done = sinstance.serversatserializer.writeToSatPathAnalysisStore(pathDesc, protocolDesc, new Double(sat_s),instance,sinstance);
//																	
//						if(done){
//							//PSatClient.writeToSatPathAnalysisStoreExecuted = true;						
//						}
//						else{
//							//PSatClient.writeToSatPathAnalysisStoreExecuted = false;
////							Display.updateLogPage("PSatClient.netWriteToSatPathAnalysisStore():failed", true);	
//						}
//						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
//					
//						netWriteToSatPathAnalysisStoreDone = true;
//					}
					else if(actionType.equals("ServerKNetworkGraph.createNetworkFromGmlOrGraphML()")){	
						ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(sendersSessionId);
						ConfigInstance cinstance = Config.deserialiseConfigInstance(sendersSessionId);
						sinstance.agents = new Agent[0];
						ServerAgentFactory.clearAgents(sinstance);
						sinstance.kgraph = new ServerKNetworkGraph();
						
						String filename = ppties.getProperty("filename");
						File gfile = PSatAPI.writeGraphMlGmlToFile(sendersSessionId,filename,(byte[])obj);
												
						sinstance.kgraph.createNetworkFromGmlOrGraphML( sinstance,cinstance, gfile);
						
						ServerAgentFactory.setAgentsPersonalAttributes(sinstance);
						
						for(int i=0;i<sinstance.agents.length;i++){
							ServerAgentFactory.writeAgent(sinstance.agents[i], sinstance);
						}				
						Config.serialiseServerConfigInstance(sendersSessionId, sinstance);
						
						createNetworkFromGmlOrGraphMLDone = true;
					}
					else if(actionType.equals("FeasibilityView.addRow()")){	
						PSatTableResult ptr_row = (PSatTableResult)obj;
						Display.feasibilityView.addTableRow(PSatAPI.instance, ptr_row);
					}
					else if(actionType.equals("PSatClient.ConfigInstanceUpdateRequest()")){
						String propertyToUpdate = ppties.getProperty("instanceproperty");
						if(propertyToUpdate.equals("processedPossibleWorldsPaths")){
							String[]processedPossibleWorldsPaths = (String[])obj;
							PSatAPI.instance.processedPossibleWorldsPaths = processedPossibleWorldsPaths;
							PSatClient.netSerialiseConfigInstance();
						}
						else if(propertyToUpdate.equals("busy")){
							PSatAPI.instance.busy = (Boolean)obj;
						}
						else if(propertyToUpdate.equals("selectedAgentPath")){
							PSatAPI.instance.selectedAgentPath = (String[])obj;
						}
						else if(propertyToUpdate.equals("runningTraining")){
							PSatAPI.instance.runningTraining = (Boolean)obj;
						}
//						else if(propertyToUpdate.equals("runningAnalysis")){
//							Display.instance.runningAnalysis = (Boolean)obj;
//						}
						else if(propertyToUpdate.equals("learningMaxSubs")){
							PSatAPI.instance.learningMaxSubs = (Boolean)obj;
						}
						else if(propertyToUpdate.equals("evaluatedProtocols")){
							PSatAPI.instance.evaluatedProtocols = (String[])obj;
						}
						else if(propertyToUpdate.equals("maxPathSats")){
							PSatAPI.instance.maxPathSats = (HashMap<String, Double>)obj;
						}
						else if(propertyToUpdate.equals("removeEdge")){
							HashMap<String, Object> hm = (HashMap<String, Object>)obj;
							KNode ss = (KNode)hm.get("source");							
							KNode source = ClientKNetworkGraph.getNode(ss.id);
							
							KNode tt = (KNode)hm.get("target");
							KNode target = ClientKNetworkGraph.getNode(tt.id);
							
							//KLink klink = (KLink)hm.get("klink");
							KLink klink = ClientKNetworkGraph.getEdge(source, target);
							if(klink != null){
//								boolean successful = ClientKNetworkGraph.g.removeEdge(klink);
								ClientKNetworkGraph.g.removeEdge(klink);
								netMutateEdgesDone = true;
								PSatClient.edgesmutated = true;
							}
							else{
								PSatClient.edgesmutated = false;
							}
							
							ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(PSatAPI.instance.sessionid);
							sinstance.g =ClientKNetworkGraph.g;
							Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);

						}
						else if(propertyToUpdate.equals("addEdge")){
							HashMap<String, Object> hm = (HashMap<String, Object>)obj;
							KNode ss = (KNode)hm.get("source");							
							KNode source = ClientKNetworkGraph.getNode(ss.id);
							
							KNode tt = (KNode)hm.get("target");
							KNode target = ClientKNetworkGraph.getNode(tt.id);
							
							int edgeCount = (int)hm.get("edgeCount");
//							boolean added = ClientKNetworkGraph.addEdge(source, target, edgeCount);
							ClientKNetworkGraph.addEdge(source, target, edgeCount);
							netMutateEdgesDone = true;
							PSatClient.edgesmutated = true;
							
							ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(PSatAPI.instance.sessionid);
							sinstance.g =ClientKNetworkGraph.g;
							Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
							
						}
						else if(propertyToUpdate.equals("repaintListbox")){
							Display.repaintListbox();
						}

					}
					else if(actionType.equals("PSatClient.ConfigInstanceUpdateRequest()")){	
						
					}

				}
			}
		};
		bridgeThread.start(); 
	}
	
	
	public static void triggerDumpMemoryStoreOnDisplay(String agentName, String partialPath, double pathsat){
		ServerConfigInstance sinstance = Config.deserialiseServerConfigInstance(Display.hostname);
		ConfigInstance instance = Config.deserialiseConfigInstance(Display.hostname);

		ServerMemoryFactory.dumpMemoryStoreOnDisplay(agentName, partialPath, pathsat, instance, sinstance);		
	}
	
	public static boolean creategraphdone = false;
	public static boolean createEppsteinPowerLawGraphDone = false;
	public static boolean createKleinbergSmallWorldGraphDone = false;
	public static boolean createBarabasiAlbertGraphDone = false;
	public static boolean createSequentialGraphDone = false;
	public static boolean createNetworkFromGmlOrGraphMLDone = false;
	public static boolean createRandomGraphDone = false;
	public static boolean netGetPathAgentNamesDone = false;
	public static boolean netWriteAgentDone = false;
	public static boolean netGetAgentDone = false;
	public static boolean netGetAgentNamesDone = false;
	public static boolean netGetAllPossibleNamesDone = false;
	public static boolean netRetrieveRolePicksDone = false;
	public static boolean netCreateAnalysisVaibleProtocolRatioStoreDone = false;
	public static boolean netAnalysePathsDone = false;
	public static boolean netAutoGenAgentsDone = false;
	public static boolean netSerialiseConfigInstanceDone = false;
	public static boolean netDeseraliseConfigInstanceDone = false;
	public static boolean netDeserialiseProcessPossibleWorldsPathToFileDone = false;
	public static boolean netEmptySerialisedContentDone = false;
	public static boolean netFindKNearestneighboursDone = false;
	public static boolean netFindSequenceSourceandTargetDone = false;
	public static boolean netGetPathsDone = false;
	public static boolean netInitProtocolSuiteDone = false;
	public static boolean netAddToEvaluatedProtocolsDone = false;
	public static boolean netRemoveFromEvaluatedProtocolsDone = false;
	public static boolean netDisplayAssertionsStoreDone = false;
	public static boolean netNewMemoryStoreDoneSingleDone = false;
	public static boolean netNewMemoryStoreDoneMultipleDone = false;
	public static boolean netGetAssertionsStorePathsDone = false;
	public static boolean netGetMemoryStorePathsDone = false;
	public static boolean netAddAgentDone = false;
	public static boolean netClientAssertionsFactoryDone = false;
	public static boolean netClientAssertionsFactoryInitDone = false;
	public static boolean netAgentFactoryInitGraphDone = false;
	public static boolean netGetNoAgentsDone = false;
	public static boolean netPrivacyRequirementRolesDone = false;
	public static boolean netWriteToSatPathTrainingStoreDone = false;
	public static boolean netWriteToSatPathAnalysisStoreDone = false;
	public static boolean netAverageClusteringCoefficientDone = false;
	public static boolean netAverageofAverageDistanceDone = false;
	public static boolean netDiameterDone = false;
	public static boolean netGenNewSessionDone = false;
	public static boolean netGetSessionDone = false;
	public static boolean netMutateEdgesDone = false;
	public static boolean netRegenerateSequenceDone = false;

}
