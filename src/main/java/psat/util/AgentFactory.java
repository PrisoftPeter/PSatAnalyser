package psat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//import java.util.Random;


import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import psat.Display;
import psat.PSatAPI;
import psat.display.model.KNetworkGraph;

public class AgentFactory  implements Serializable{
	private static final long serialVersionUID = -6788783348838083929L;
		
	public static void genAgents(){
		
		PSatAPI.instance.agents = new Agent[0];
		if(PSatAPI.instance.kgraph == null){			
			PSatAPI.instance.kgraph = new KNetworkGraph();
		}
		PSatAPI.instance.kgraph.createGraph();
	}
	
	public static boolean initGraph(){
		boolean done = false;
		if(!(PSatAPI.instance.sessionid == null)){
			
			if(PSatAPI.instance.kgraph == null){			
				PSatAPI.instance.kgraph = new KNetworkGraph();
			}
			if(PSatAPI.instance.agents == null){
				PSatAPI.instance.agents = new Agent[0];
			}
			
			PSatAPI.instance.kgraph.createGraph();
			done = true;
		}
		return done;		
	}
	
	public static boolean addAgent(Agent agent){
		
		boolean added = false;
		boolean exist = false;
		for(Agent e: PSatAPI.instance.agents){
			if(e.getAgentName().equals(agent.getAgentName())){
				exist = true;
			}
		}
		if(!exist){
			Agent temp [] = new Agent[PSatAPI.instance.agents.length+1];			
			for(int i=0;i<PSatAPI.instance.agents.length;i++){
				temp[i] = PSatAPI.instance.agents[i];
			}
			temp[PSatAPI.instance.agents.length] = agent;			
			PSatAPI.instance.agents = temp;
			writeAgent(agent);

			added = true;
		}
		else{
			Agent temp [] = new Agent[PSatAPI.instance.agents.length];
			
			for(int i=0;i<PSatAPI.instance.agents.length;i++){
				if(PSatAPI.instance.agents[i].getAgentName().equals(agent.getAgentName())){
					temp[i] = agent;
					writeAgent(agent);
					added = true;
				}
				else{
					temp[i] = PSatAPI.instance.agents[i];					
				}
			}			
			PSatAPI.instance.agents = temp;
		}
		
		return added;
	}
			
	public static void removeAgent(String agentName){
		
		boolean exist = false;
		for(Agent e: PSatAPI.instance.agents){
			if(e.getAgentName().equals(agentName)){
				exist = true;
			}
		}
		if(exist){
			Agent temp [] = new Agent[PSatAPI.instance.agents.length-1];	
			int j= 0;
			for(int i=0;i<PSatAPI.instance.agents.length;i++){
				if(!PSatAPI.instance.agents[i].getAgentName().equals(agentName)){
					temp[j] = PSatAPI.instance.agents[i];
					j = j+1;
				}				
			}	
			
			PSatAPI.instance.agents = temp;
		}		
	}
		
	public static int getAgentIndex(String agentName){
		int index =-1;
		for(int i=0;i<PSatAPI.instance.agents.length;i++){
			if(PSatAPI.instance.agents[i].getAgentName().equals(agentName)){
				index = i;
				break;
			}
		}
		return index;
	}
	
	
	public static void deleteFromDatastore(String agentName, String sessionid){
		String fileName = PSatAPI.datastore_file_path+"/"+sessionid+"/agents/"+agentName+".agent";
		
		File if_file = new File(fileName);
        if(if_file.exists()){
        	if_file.delete();        	
        }
	}
	
	public static void deleteFromDatastorecs(String agentName, String sessionid){
		String fileName = PSatAPI.datastore_file_path+"/"+sessionid+"/agentscs/"+agentName+".agent";
		
		File if_file = new File(fileName);
        if(if_file.exists()){
        	if_file.delete();        	
        }
	}
	
	public static void deleteAgentscs(String sessionid){
		String path = PSatAPI.datastore_file_path+"/"+sessionid+"/agentscs/";
		File folder = new File(path);
		try {
			FileUtils.deleteDirectory(folder);
			//System.out.println("datastore deleted");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to delete agentscs");
		}
		finally{
			
		}
	}
	
	public static void autoGenAgentsTest(){
		genAgents();
	}
	
	public static Agent autoGenAgentWoutConnections(String agentName){
//		Random rand = new Random();
		Agent agent = new Agent(agentName);
		
//		Attribute h = new Attribute();
//		h.setSubjectName(agentName);
//		h.setKey("h");
//		int val1 = rand.nextInt(10) + 1;
//		h.setValue(""+val1);		
//		agent.addToPersonalAttributes(h);
		
		return agent;
	}
	
	public static boolean autoGenAgents(){
		boolean successful = true;

		
		if(PSatAPI.instance.networkType != NetworkType.CUSTOM){
			PSatAPI.instance.kgraph = new KNetworkGraph();
		}
		
		if(PSatAPI.instance.networkType == NetworkType.BARABASIALBERT){
						
			if(PSatAPI.instance.numEdgesToAttach ==0 ||PSatAPI.instance.init_no_seeds==0|| PSatAPI.instance.no_iterations ==0){	
				Display.updateLogPage("ClientAgentFactory: call- PreferentialAttachmentSettings.configure()", true);
				successful = false;
			}
			else{
				PSatAPI.instance.agents = new Agent[0];
				clearAgents();
				
				PSatAPI.instance.kgraph = new KNetworkGraph();
				PSatAPI.instance.kgraph.createBarabasiAlbertGraph(PSatAPI.instance.init_no_seeds, PSatAPI.instance.numEdgesToAttach, PSatAPI.instance.no_agents, PSatAPI.instance.no_iterations, PSatAPI.instance);
			}
			
		}
		else if(PSatAPI.instance.networkType == NetworkType.EPPESTEINPOWERLAW){
			
			if(PSatAPI.instance.no_edges ==0 ||PSatAPI.instance.degreeExponent ==0){
				Display.updateLogPage("ClientAgentFactory: call- EppsteinPowerLawSettings.configure()", true);
				successful = false;
			}
			else{
				PSatAPI.instance.agents = new Agent[0];
				clearAgents();
				
				PSatAPI.instance.kgraph = new KNetworkGraph();
				PSatAPI.instance.kgraph.createEppsteinPowerLawGraph(PSatAPI.instance.no_agents, PSatAPI.instance.no_edges, PSatAPI.instance.degreeExponent);
			}
			
		}
		else if(PSatAPI.instance.networkType == NetworkType.KLEINBERGSMALLWORLD){
			if(PSatAPI.instance.no_edges ==0 ||PSatAPI.instance.clusteringExponent ==0){
				Display.updateLogPage("ClientAgentFactory: call- KleinbergSmallWorldSettings.configure()", true);
				successful = false;
			}
			else{
				PSatAPI.instance.agents = new Agent[0];
				clearAgents();
				
				PSatAPI.instance.kgraph = new KNetworkGraph();
				PSatAPI.instance.kgraph.createKleinbergSmallWorldGraph(PSatAPI.instance.no_agents, PSatAPI.instance.no_edges, PSatAPI.instance.clusteringExponent, PSatAPI.instance);
			}
		}		
		else if(PSatAPI.instance.networkType == NetworkType.SEQUENTIAL){
			PSatAPI.instance.kgraph = new KNetworkGraph();			
			PSatAPI.instance.kgraph.createSequentialGraph();
		}
		else if(PSatAPI.instance.networkType == NetworkType.RANDOM){
			PSatAPI.instance.kgraph = new KNetworkGraph();
			PSatAPI.instance.kgraph.createRandomGraph();
		}
		else if(PSatAPI.instance.networkType == NetworkType.NODESONLY){
			PSatAPI.instance.kgraph = new KNetworkGraph();

			PSatAPI.instance.kgraph.createNodesOnlyGraph();

		}
//		else if(gPSatAPI.instance.networkType == NetworkType.CUSTOM){
//			PSatAPI.instance.agents = new Agent[0];
//			clearAgents(sessionid);
//			PSatAPI.instance.kgraph = new ServerKNetworkGraph();
//			Config.serialiseServerConfigInstance(sessionid, PSatAPI.instance);
//			
//			PSatAPI.instance.kgraph.createNetworkFromGmlOrGraphML(sessionid);
//		}
		
		setAgentsPersonalAttributes();
		
		for(int i=0;i<PSatAPI.instance.agents.length;i++){
			writeAgent(PSatAPI.instance.agents[i]);
		}
		
		return successful;
	}
	
	public static void serialiseAllAgents(){
		
		for(Agent e:PSatAPI.instance.agents){
			writeAgent(e);
		}
		System.out.println("#state serialised");//, false);
	}
	
	
	public static void serialiseAllAgentsAsTemplate(){
		
		JFileChooser chooser = new JFileChooser(".");
        FileNameExtensionFilter xFilter = new FileNameExtensionFilter("kcore network templates (*.kcore)", "kcore");
        chooser.addChoosableFileFilter(xFilter);
        chooser.setFileFilter(xFilter);
        
        int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	    	try{
	    		FileOutputStream fileOut = new FileOutputStream(chooser.getSelectedFile()+".kcore");
		        ObjectOutputStream out = new ObjectOutputStream(fileOut);
		        out.writeObject(PSatAPI.instance.agents);
		        out.close();
		        fileOut.close();
			}
			catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }	
	}
		
	public static boolean writeAgent(Agent agent){
		boolean exist = false;
		for(Agent e: PSatAPI.instance.agents){
			if(e.getAgentName().equals(agent.getAgentName())){
				exist = true;
			}
		}
		if(!exist){
			Agent temp [] = new Agent[PSatAPI.instance.agents.length+1];			
			for(int i=0;i<PSatAPI.instance.agents.length;i++){
				temp[i] = PSatAPI.instance.agents[i];
			}
			temp[PSatAPI.instance.agents.length] = agent;			
			PSatAPI.instance.agents = temp;

		}
		else{
			Agent temp [] = new Agent[PSatAPI.instance.agents.length];
			
			for(int i=0;i<PSatAPI.instance.agents.length;i++){
				if(PSatAPI.instance.agents[i].getAgentName().equals(agent.getAgentName())){
					temp[i] = agent;
				}
				else{
					temp[i] = PSatAPI.instance.agents[i];					
				}
			}			
			PSatAPI.instance.agents = temp;
		}
		
//		if(PSatAPI.instance.agent.getAgentName().equals(agent.getAgentName())){
//			PSatAPI.instance.agent = agent;
//		}
		Config.serialiseConfigInstance();
		
		boolean successful = false;
		try{
			String folderName1 = PSatAPI.datastore_file_path+"/"+PSatAPI.instance.sessionid;
			String folderName2 = PSatAPI.datastore_file_path+"/"+PSatAPI.instance.sessionid+"/agents";
			
			String fileName = PSatAPI.datastore_file_path+"/"+PSatAPI.instance.sessionid+"/agents/"+agent.getAgentName()+".agent";
					
	
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
								
			File if_file = new File(fileName);
	        if(if_file.exists()){
	         if_file.delete();
	        }
	        if_file.createNewFile();
	        FileOutputStream fileOut = new FileOutputStream(fileName);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(agent);
	        out.close();
	        fileOut.close();
	        successful = true;
	      }
		catch(IOException i){
			Display.updateLogPage(i.getMessage(), true);
	    }
		return successful;
	}
	
	
	public void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}
	
	public static void loadAgents(){

		String sessionid = PSatAPI.instance.sessionid;
		clearAgents();
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/agents";
		
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				for (final File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					Agent agent = (Agent) in.readObject();
					
					addAgent(agent);
					
					in.close();
					fileIn.close();
			    }
				if(PSatAPI.instance.kgraph == null){			
					PSatAPI.instance.kgraph = new KNetworkGraph();
				}
				PSatAPI.instance.kgraph.createGraph();
			}			
		} 
		catch (IOException i) {
			System.err.println("IO exception @readAgents");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("Agent class not found");
		}
		finally{
			
		}
	}

	public static void listAgents() {
		if(PSatAPI.instance.agents != null){
			for(int i=0;i<PSatAPI.instance.agents.length;i++){
				System.out.println("("+i+") "+PSatAPI.instance.agents[i].getAgentName());
			}	
		}
		else{
			System.err.println("Agents not loaded");
		}
	}
	
	public static void clearAgents(){
		PSatAPI.instance.agents = new Agent[0];
	}
	
	
	public static String[] getAgentNames(){

		String [] agentNames = new String[PSatAPI.instance.agents.length];
		for(int i=0;i<PSatAPI.instance.agents.length;i++){
			agentNames[i] = PSatAPI.instance.agents[i].getAgentName();
		}
		
		return agentNames;
	}
	
	public static String[] getOtherAgentNames(String exceptAgentName){
		
		String [] agentNames = new String[PSatAPI.instance.agents.length-1];
		int j =0;
		for(int i=0;i<PSatAPI.instance.agents.length;i++){
			if(!PSatAPI.instance.agents[i].getAgentName().equals(exceptAgentName)){
				agentNames[j] = PSatAPI.instance.agents[i].getAgentName();
				j = j+1;

			}
		}
		
		return agentNames;
	}
	
	public static String[] getOtherAgentNamesAlongPath(String exceptAgentName){

		String [] agentNames = new String[PSatAPI.instance.selectedAgentPath.length-1];
		int j =0;
		for(String agentName: PSatAPI.instance.selectedAgentPath){
			Agent a = AgentFactory.getAgent(agentName);
			if(!a.getAgentName().equals(exceptAgentName)){
				agentNames[j] = a.getAgentName();
				j = j+1;
			}
		}		
		return agentNames;
	}
	
	public static Agent getAgent(String agentName){

		Agent agent = null;
		for(int i=0;i<PSatAPI.instance.agents.length;i++){
			if(PSatAPI.instance.agents[i].getAgentName().equals(agentName)){
				agent = PSatAPI.instance.agents[i]; 
			}
		}
		
		return agent;
	}
	
	public static boolean contained(String [] otherAgents, String agentName){
		boolean contained = false;
		for(String act:otherAgents){
			if(act !=null){
				if(act.toString().equals(agentName.toString())){
					contained = true;
					break;
				}	
			}			
		}
		
		return contained;
	}
	
	public static String [] getAllPossibleNames(){
		String [] names = new String[0];
		
		try {
			InputStream f = ResourceLoader.load("config_info/names");
			InputStreamReader reader = new InputStreamReader(f);
			BufferedReader buff = new BufferedReader (reader);
			String name;
			while((name=buff.readLine())!=null){
				
				String [] temp = new String[names.length+1];
				for(int i=0;i<names.length;i++){
					temp[i] = names[i];
				}
				temp[names.length] = name;		
				names = temp;				
			}
		} 
		catch (IOException i) {
			System.err.println("IO exception @readNames");
		} 
		
		return names;
	}
	
	public static void clearPersonalAttributesFromPathAgents(){
		//clear agent personal attributes for initial path
		Agent agent = getAgent(PSatAPI.instance.sourceAgentName);
		agent.resetPersonalAttributes();
		writeAgent(agent);	
	}
	public static void setSourcePersonalAttributeForPath(){
		Agent agent = getAgent(PSatAPI.instance.sourceAgentName);
		
		Attribute h = new Attribute();
		h.setSubjectName(PSatAPI.instance.sourceAgentName);
		h.setKey("f");
		Random rand = new Random();
		int val1 = rand.nextInt(10) + 1;
		h.setValue(""+val1);		
		agent.addToPersonalAttributes(h);
		writeAgent(agent);
	}
	
	public static void setAgentsPersonalAttributes(){
		for(String agentName: getAgentNames()){
			Agent agent = getAgent(agentName);
			
			Attribute h = new Attribute();
			h.setSubjectName(agentName);
			h.setKey("f");
			Random rand = new Random();
			int val1 = rand.nextInt(10) + 1;
			h.setValue(""+val1);		
			agent.addToPersonalAttributes(h);
			writeAgent(agent);
		}
		
	}

}
