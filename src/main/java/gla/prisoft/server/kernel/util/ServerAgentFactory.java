package gla.prisoft.server.kernel.util;

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

import gla.prisoft.client.session.ClientServerBroker;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.session.Config;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.NetworkType;

public class ServerAgentFactory  implements Serializable{
	private static final long serialVersionUID = -6788783348838083929L;
		
	public static void genAgents(ServerConfigInstance instance){
		
		instance.agents = new Agent[0];
		if(instance.kgraph == null){			
			instance.kgraph = new ServerKNetworkGraph();
		}
		instance.kgraph.createGraph(instance);
	}
	
	public static boolean initGraph(ServerConfigInstance instance){
		boolean done = false;
		if(!(instance.sessionid == null)){
			
			if(instance.kgraph == null){			
				instance.kgraph = new ServerKNetworkGraph();
			}
			if(instance.agents == null){
				instance.agents = new Agent[0];
			}
			
			instance.kgraph.createGraph(instance);
			done = true;
		}
		return done;		
	}
	
	public static boolean addAgent(Agent agent, ServerConfigInstance sinstance){
		
		boolean added = false;
		boolean exist = false;
		for(Agent e: sinstance.agents){
			if(e.getAgentName().equals(agent.getAgentName())){
				exist = true;
			}
		}
		if(!exist){
			Agent temp [] = new Agent[sinstance.agents.length+1];			
			for(int i=0;i<sinstance.agents.length;i++){
				temp[i] = sinstance.agents[i];
			}
			temp[sinstance.agents.length] = agent;			
			sinstance.agents = temp;
			writeAgent(agent, sinstance);

			added = true;
		}
		else{
			Agent temp [] = new Agent[sinstance.agents.length];
			
			for(int i=0;i<sinstance.agents.length;i++){
				if(sinstance.agents[i].getAgentName().equals(agent.getAgentName())){
					temp[i] = agent;
					writeAgent(agent, sinstance);
					added = true;
				}
				else{
					temp[i] = sinstance.agents[i];					
				}
			}			
			sinstance.agents = temp;
		}
		
		return added;
	}
			
	public static void removeAgent(String agentName, ServerConfigInstance instance){
		
		boolean exist = false;
		for(Agent e: instance.agents){
			if(e.getAgentName().equals(agentName)){
				exist = true;
			}
		}
		if(exist){
			Agent temp [] = new Agent[instance.agents.length-1];	
			int j= 0;
			for(int i=0;i<instance.agents.length;i++){
				if(!instance.agents[i].getAgentName().equals(agentName)){
					temp[j] = instance.agents[i];
					j = j+1;
				}				
			}	
			
			instance.agents = temp;
		}		
	}
		
	public static int getAgentIndex(String agentName, ServerConfigInstance instance){
		int index =-1;
		for(int i=0;i<instance.agents.length;i++){
			if(instance.agents[i].getAgentName().equals(agentName)){
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
	
	public static void autoGenAgentsTest(ServerConfigInstance instance){
		genAgents(instance);
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
	
	public static boolean autoGenAgents(ServerConfigInstance instance,ConfigInstance ginstance ){
		boolean successful = true;

		
		if(ginstance.networkType != NetworkType.CUSTOM){
			instance.kgraph = new ServerKNetworkGraph();
		}
		
		if(ginstance.networkType == NetworkType.BARABASIALBERT){
						
			if(ginstance.numEdgesToAttach ==0 ||ginstance.init_no_seeds==0|| ginstance.no_iterations ==0){	
				ClientServerBroker.messageEvent("updateLogPage", "ClientAgentFactory: call- PreferentialAttachmentSettings.configure()"+"₦"+true,null,null);
				successful = false;
			}
			else{
				instance.agents = new Agent[0];
				clearAgents(instance);
				
				instance.kgraph = new ServerKNetworkGraph();
				instance.kgraph.createBarabasiAlbertGraph(ginstance.init_no_seeds, ginstance.numEdgesToAttach, ginstance.no_agents, ginstance.no_iterations, instance);
			}
			
		}
		else if(ginstance.networkType == NetworkType.EPPESTEINPOWERLAW){
			
			if(ginstance.no_edges ==0 ||ginstance.degreeExponent ==0){
				ClientServerBroker.messageEvent("updateLogPage", "ClientAgentFactory: call- EppsteinPowerLawSettings.configure()"+"₦"+true,null,null);
				successful = false;
			}
			else{
				instance.agents = new Agent[0];
				clearAgents(instance);
				
				instance.kgraph = new ServerKNetworkGraph();
				instance.kgraph.createEppsteinPowerLawGraph(ginstance.no_agents, ginstance.no_edges, ginstance.degreeExponent, instance);
			}
			
		}
		else if(ginstance.networkType == NetworkType.KLEINBERGSMALLWORLD){
			if(ginstance.no_edges ==0 ||ginstance.clusteringExponent ==0){
				ClientServerBroker.messageEvent("updateLogPage","ClientAgentFactory: call- KleinbergSmallWorldSettings.configure()"+"₦"+true,null,null);
				successful = false;
			}
			else{
				instance.agents = new Agent[0];
				clearAgents(instance);
				
				instance.kgraph = new ServerKNetworkGraph();
				instance.kgraph.createKleinbergSmallWorldGraph(ginstance.no_agents, ginstance.no_edges, ginstance.clusteringExponent, instance);
			}
		}		
		else if(ginstance.networkType == NetworkType.SEQUENTIAL){
			instance.kgraph = new ServerKNetworkGraph();			
			instance.kgraph.createSequentialGraph(ginstance, instance);
		}
		else if(ginstance.networkType == NetworkType.RANDOM){
			instance.kgraph = new ServerKNetworkGraph();
			instance.kgraph.createRandomGraph(instance, ginstance);
		}
		else if(ginstance.networkType == NetworkType.NODESONLY){
			instance.kgraph = new ServerKNetworkGraph();

			instance.kgraph.createNodesOnlyGraph(instance, ginstance);

		}
//		else if(ginstance.networkType == NetworkType.CUSTOM){
//			instance.agents = new Agent[0];
//			clearAgents(sessionid);
//			instance.kgraph = new ServerKNetworkGraph();
//			Config.serialiseServerConfigInstance(sessionid, instance);
//			
//			instance.kgraph.createNetworkFromGmlOrGraphML(sessionid);
//		}
		
		setAgentsPersonalAttributes(instance);
		
		for(int i=0;i<instance.agents.length;i++){
			writeAgent(instance.agents[i], instance);
		}
		
		return successful;
	}
	
	public static void serialiseAllAgents(ServerConfigInstance instance){
		
		for(Agent e:instance.agents){
			writeAgent(e, instance);
		}
		System.out.println("#state serialised");//, false);
	}
	
	
	public static void serialiseAllAgentsAsTemplate(ServerConfigInstance instance){
		
		JFileChooser chooser = new JFileChooser(".");
        FileNameExtensionFilter xFilter = new FileNameExtensionFilter("kcore network templates (*.kcore)", "kcore");
        chooser.addChoosableFileFilter(xFilter);
        chooser.setFileFilter(xFilter);
        
        int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	    	try{
	    		FileOutputStream fileOut = new FileOutputStream(chooser.getSelectedFile()+".kcore");
		        ObjectOutputStream out = new ObjectOutputStream(fileOut);
		        out.writeObject(instance.agents);
		        out.close();
		        fileOut.close();
			}
			catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }	
	}
		
	public static boolean writeAgent(Agent agent, ServerConfigInstance sinstance){
		boolean exist = false;
		for(Agent e: sinstance.agents){
			if(e.getAgentName().equals(agent.getAgentName())){
				exist = true;
			}
		}
		if(!exist){
			Agent temp [] = new Agent[sinstance.agents.length+1];			
			for(int i=0;i<sinstance.agents.length;i++){
				temp[i] = sinstance.agents[i];
			}
			temp[sinstance.agents.length] = agent;			
			sinstance.agents = temp;

		}
		else{
			Agent temp [] = new Agent[sinstance.agents.length];
			
			for(int i=0;i<sinstance.agents.length;i++){
				if(sinstance.agents[i].getAgentName().equals(agent.getAgentName())){
					temp[i] = agent;
				}
				else{
					temp[i] = sinstance.agents[i];					
				}
			}			
			sinstance.agents = temp;
		}
		
//		if(sinstance.agent.getAgentName().equals(agent.getAgentName())){
//			sinstance.agent = agent;
//		}
		Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
		
		boolean successful = false;
		try{
			String folderName1 = PSatAPI.datastore_file_path+"/"+sinstance.sessionid;
			String folderName2 = PSatAPI.datastore_file_path+"/"+sinstance.sessionid+"/agents";
			
			String fileName = PSatAPI.datastore_file_path+"/"+sinstance.sessionid+"/agents/"+agent.getAgentName()+".agent";
					
	
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
			ClientServerBroker.messageEvent("updateLogPage", i.getMessage()+"₦"+true,null,null);

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
	
	public static void loadAgents(ServerConfigInstance instance){

		String sessionid = instance.sessionid;
		clearAgents(instance);
		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/agents";
		
		try {
			File folder2 = new File(folderName2);
			if(folder2.isDirectory()){
				for (final File fileEntry : folder2.listFiles()) {
					FileInputStream fileIn = new FileInputStream(fileEntry);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					Agent agent = (Agent) in.readObject();
					
					addAgent(agent, instance);
					
					in.close();
					fileIn.close();
			    }
				if(instance.kgraph == null){			
					instance.kgraph = new ServerKNetworkGraph();
				}
				instance.kgraph.createGraph(instance);
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

	public static void listAgents(ServerConfigInstance instance) {
		if(instance.agents != null){
			for(int i=0;i<instance.agents.length;i++){
				System.out.println("("+i+") "+instance.agents[i].getAgentName());
			}	
		}
		else{
			System.err.println("Agents not loaded");
		}
	}
	
	public static void clearAgents(ServerConfigInstance instance){
		instance.agents = new Agent[0];
	}
	
	
	public static String[] getAgentNames(ServerConfigInstance instance){

		String [] agentNames = new String[instance.agents.length];
		for(int i=0;i<instance.agents.length;i++){
			agentNames[i] = instance.agents[i].getAgentName();
		}
		
		return agentNames;
	}
	
	public static String[] getOtherAgentNames(String exceptAgentName, ServerConfigInstance instance){
		
		String [] agentNames = new String[instance.agents.length-1];
		int j =0;
		for(int i=0;i<instance.agents.length;i++){
			if(!instance.agents[i].getAgentName().equals(exceptAgentName)){
				agentNames[j] = instance.agents[i].getAgentName();
				j = j+1;

			}
		}
		
		return agentNames;
	}
	
	public static String[] getOtherAgentNamesAlongPath(String exceptAgentName,
														ServerConfigInstance sinstance, ConfigInstance instance){

		String [] agentNames = new String[instance.selectedAgentPath.length-1];
		int j =0;
		for(String agentName: instance.selectedAgentPath){
			Agent a = ServerAgentFactory.getAgent(agentName, sinstance);
			if(!a.getAgentName().equals(exceptAgentName)){
				agentNames[j] = a.getAgentName();
				j = j+1;
			}
		}		
		return agentNames;
	}
	
	public static Agent getAgent(String agentName, ServerConfigInstance instance){

		Agent agent = null;
		for(int i=0;i<instance.agents.length;i++){
			if(instance.agents[i].getAgentName().equals(agentName)){
				agent = instance.agents[i]; 
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
	
	public static void clearPersonalAttributesFromPathAgents(ServerConfigInstance sinstance, ConfigInstance instance){
		//clear agent personal attributes for initial path
		Agent agent = getAgent(instance.sourceAgentName, sinstance);
		agent.resetPersonalAttributes();
		writeAgent(agent, sinstance);	
	}
	public static void setSourcePersonalAttributeForPath(ServerConfigInstance sinstance, ConfigInstance instance){
		Agent agent = getAgent(instance.sourceAgentName, sinstance);
		
		Attribute h = new Attribute();
		h.setSubjectName(instance.sourceAgentName);
		h.setKey("f");
		Random rand = new Random();
		int val1 = rand.nextInt(10) + 1;
		h.setValue(""+val1);		
		agent.addToPersonalAttributes(h);
		writeAgent(agent, sinstance);
	}
	
	public static void setAgentsPersonalAttributes(ServerConfigInstance instance){
		for(String agentName: getAgentNames(instance)){
			Agent agent = getAgent(agentName, instance);
			
			Attribute h = new Attribute();
			h.setSubjectName(agentName);
			h.setKey("f");
			Random rand = new Random();
			int val1 = rand.nextInt(10) + 1;
			h.setValue(""+val1);		
			agent.addToPersonalAttributes(h);
			writeAgent(agent, instance);
		}
		
	}

}
