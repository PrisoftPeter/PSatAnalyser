package psat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import psat.behaviour.InformationFlows;
import psat.behaviour.protocol.ProtocolFactory;
import psat.display.model.FeasibilityView;
import psat.knowledge.MemoryFactory;
import psat.knowledge.worlds.World;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.AssertionsFactory;
import psat.util.CollectiveMode;
import psat.util.Config;
import psat.util.ConfigInstance;
import psat.util.GraphAnalyser;
import psat.util.KNode;
import psat.util.PathsInGraph;
import psat.util.SatSerializer;

public class PSatAPI {
	public static ConfigInstance instance;
	public static String datastore_file_path ="datastore";
	public static int fvindex;
	public static boolean isnextpath;
	public static boolean roleAssertionsPrinted;
	public static HashMap<World, ArrayList<World>> higherOrderKs = new HashMap<World, ArrayList<World>>();
	public static boolean forcenewsession;
	
	static String sendersSessionId = Display.hostname;

	public static void logHighOrderImplications(){
				
		for (Map.Entry<World, ArrayList<World>> entry : higherOrderKs.entrySet()) {
		    World key = entry.getKey();
		    ArrayList<World> value = entry.getValue();

		    String htmlcgdesc ="";
		    if(PSatAPI.instance.is_role_run){
		    	if(roleAssertionsPrinted){
		    		return;
		    	}
		    	htmlcgdesc = CollectiveMode.getModeLimitHtmlDesc(PSatAPI.instance.collectiveStrategy)+"("+FeasibilityView.prdesc+") &#8658; {";

		    	htmlcgdesc= htmlcgdesc.replace("pr:  ", "");
			    htmlcgdesc= htmlcgdesc.replace("Pr=", "");
			    htmlcgdesc= htmlcgdesc.replace("<html>", " ");
			    htmlcgdesc= htmlcgdesc.replace("<body>", " ");
			    htmlcgdesc= htmlcgdesc.replace("</html>", " ");
			    htmlcgdesc= htmlcgdesc.replace("</body>", " ");
			    
			    roleAssertionsPrinted = true;
		    }
		    else{
		    	htmlcgdesc = CollectiveMode.getModeLimitHtmlDesc(PSatAPI.instance.collectiveStrategy)+"("+key.toLimitHtmlString()+") &#8658; {";
		    }
		    
    		
		    int count = 0;
		    int countTotal = 0;
		    for(Object loworder_o:value){
		    	if(loworder_o !=null){
		    		World loworder = (World)loworder_o;
		    		htmlcgdesc = htmlcgdesc + loworder.toLimitHtmlString()+", ";
					if(count == 10){
						htmlcgdesc = htmlcgdesc +"<br>";
						count = 0;
					}
					else{
						count = count +1;
					}
					countTotal = countTotal+1;
		    	}
		    }
		    htmlcgdesc = "<html>"+instance.currentPath+"<br>"+htmlcgdesc + "}</html>";
			htmlcgdesc = htmlcgdesc.replace(", }</html>", "}</html>");
			htmlcgdesc = htmlcgdesc.replace("</html>", "&#8712; A #:"+countTotal+"<br></html>");
			Display.updateLogPage(htmlcgdesc, false);
			
		}	
	}
	public static void addHighOrderImplication(World highorder, World loworder){
		if(!PSatAPI.isnextpath){
			return;
		}
		boolean exist = false;
		World key=null;
		ArrayList<World> value = null;
		for (Map.Entry<World, ArrayList<World>> entry : higherOrderKs.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    
		    if(highorder.toString().equals(key.toString())){
		    	exist = true;
		    	break;
		    }
		}
		
		if(!exist){
			ArrayList<World>loworders = new ArrayList<World>();
			loworders.add(loworder);
			higherOrderKs.put(highorder, loworders);
		}
		else{
			boolean contained = false;
			for(Object vx:value){
				
				if(vx == null && loworder == null){
					contained = true;
				}
				else{
					World v = (World)vx;
					if(v !=null && loworder !=null && v.toString().equals(loworder.toString())){
						contained = true;
					}
				}				
			}
			if(!contained){
				value.add(loworder);
				higherOrderKs.put(key, value);
			}			
		}
		
	}
	
	public static File writeGraphMlGmlToFile(String sessionid,String filename, byte [] bytes){
		String foldername0 = PSatAPI.datastore_file_path+"/filestore";
		String foldername1 = foldername0+"/"+sessionid;
		String filepath = foldername1+"/"+filename;
		
		File folder0 = new File(foldername0);
		boolean exist0 = false;
		if(folder0.exists()){
			if(folder0.isDirectory()){
				exist0 = true;
			}				
		}
		if(!exist0){
			folder0.mkdir();
		}
		
		File folder1 = new File(foldername1);
		boolean exist1 = false;
		if(folder1.exists()){
			if(folder1.isDirectory()){
				exist1 = true;
			}				
		}
		if(!exist1){
			folder1.mkdir();
		}
		File file = new File(filepath);
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file, true);
	   		for(int i=0;i<bytes.length;i++){
	    		writer.append((char)bytes[i]);
			}
	   		writer.flush();
	   		writer.close();
		} catch (IOException e) {

			e.printStackTrace();

		}
		return file;
	}

	
	public static void netGenNewSession(){
		
		String tsessionid = UUID.randomUUID().toString();
		instance = new ConfigInstance();
		ProtocolFactory.initProtocolSuite();
		
		instance.sessionid = tsessionid;
		instance.satSerializer = new SatSerializer();
	
		Config.serialiseServerConfigInstance();
		
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
		boolean done = AgentFactory.writeAgent(agent);			
		return done;
	}
	
	public static Agent netGetAgent(String agentname){		
		Agent agent = AgentFactory.getAgent(agentname);
		return agent;	
	}
	
	public static String [] netGetAgentNames(){
		String [] agentnames = AgentFactory.getAgentNames();
								
		return agentnames;		
	}
	
	public static String [] netGetAllPossibleAgentNames(){
		String[] names = AgentFactory.getAllPossibleNames();
		
		return names;
	}
	
	public static World [] netRetrieveRolePicks(){
		String selfAgentName = instance.selfAgentName;
		
		World[] picks  = AssertionsFactory.retrieveRolePicks(selfAgentName);
		return picks;
	}
	
	
	public static boolean netAnalysePaths(){	
		boolean fin = new InformationFlows().run(instance.selectedPath);
		return fin;		
	}
	
	public static boolean netAutoGenAgents(){
		boolean fin = AgentFactory.autoGenAgents();

		if(fin){
			Config.serialiseServerConfigInstance();						
		}
		return fin;
	}
	
	public static void netRegenerateSequence(String path){
		instance.kgraph.createNewSequence(path);
		
	}
	
	public static void netSerialiseConfigInstance(){
		Config.serialiseConfigInstance(instance.sessionid);

	}
		
	public static void netDeserialiseProcessPossibleWorldsPathToFile(){
		Config.deserialiseProcessPossibleWorldsPathToFile();		
	}
	
	public static boolean netEmptySerialisedContent(){
		boolean fin = Config.emptySerialisedContent(sendersSessionId);

		return fin;     
	}
	
	public static String [] netFindKNearestneighbours(){
		String [] listPathsData = new PathsInGraph().findKNearestNeighbours();
		return listPathsData;
			
	}
	
	public static Properties netFindSequenceSourceandTarget(){
		Properties properties = new PathsInGraph().getSequenceSourceandTarget();
		
		return properties;	
	}
	
	public static String [] netGetPaths(){
		String [] listPathsData = PathsInGraph.getPaths();						
		String[] paths = listPathsData;
		
		return paths;	
	}
	
	
	public static void netMutateEdges(KNode source, KNode target, String mutationType){
		PSatAPI.instance.kgraph.mutateEdges(source, target,mutationType);
		       
	}
	
	public static Properties [] netDisplayAssertionsStore(String agentName, String partialPath){		
		Properties [] ppties = new AssertionsFactory(agentName).displayAssertionsStore(agentName, partialPath);
		return ppties;
	}
	
	public static void netNewMemoryStore(final String agentname){
		boolean done = MemoryFactory.newMemoryStore(agentname);
		
		if(done){
			Config.serialiseServerConfigInstance();						
		}
	}
	

	public static void netNewMemoryStore(){
		boolean done = MemoryFactory.newMemoryStore();
		
		if(done){
			Config.serialiseServerConfigInstance();
		}
	}
	
	
	
	public static String [] netGetAssertionsStorePaths(String agentName){
		String[] memorystorepaths = MemoryFactory.getAssertionsStorePaths(agentName, sendersSessionId);
		return memorystorepaths;
		
	}
	
	public static String [] netGetMemoryStorePaths(String agentName){
		String[] memorystorepaths = MemoryFactory.getMemoryStorePaths(agentName, sendersSessionId);
		return memorystorepaths;
	}
	
	
	public static boolean netAddAgent(Agent agent1){
		boolean done=AgentFactory.addAgent(agent1);
		Config.serialiseServerConfigInstance();
		
		return done;    
	}
	
	
	public static boolean netAgentFactoryInitGraph(){
		boolean done = false;
		if(PSatAPI.instance.kgraph == null){
			done = AgentFactory.initGraph();
			Config.serialiseServerConfigInstance();
		}													
		
		return done;		       
	}
	
	public static int netGetNoAgents(){
		int noagents = instance.agents.length;												
		if(noagents <=0){
			Display.updateLogPage("empty graph-", true);
  		}
		return noagents;
	}
	
	public static boolean netPrivacyRequirementRoles(String agentname){
		boolean done = MemoryFactory.privacyRequirementRoles(agentname);
		Config.serialiseServerConfigInstance();

		if(!done){
			Display.updateLogPage("PSatClient.netPrivacyRequirementRoles():failed", true);
		}
		return done;
	}


	
	public static double netAverageClusteringCoefficient(){
		double acc = GraphAnalyser.averageClusteringCoefficient(PSatAPI.instance.g);
														
		return acc;
	}
	
	
	public static double netAverageofAverageDistance(){
		double acc = GraphAnalyser.averageofAverageDistance(PSatAPI.instance.g);
		
		return acc;   
	}
	
	public static double netDiameter(){
		double acc = GraphAnalyser.diameter(PSatAPI.instance.g);

		return acc;     
	}
}
