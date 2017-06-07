package gla.prisoft.server.session;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import edu.uci.ics.jung.graph.UndirectedGraph;
import gla.prisoft.server.kernel.util.ServerKNetworkGraph;
import gla.prisoft.server.kernel.util.ServerSatSerializer;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.KLink;
import gla.prisoft.shared.KNode;

public class ServerConfigInstance implements Serializable{
	
	static final long serialVersionUID = 1L;
		
	public String sessionid;
	
	//server specific variables
	public boolean is_generating_memory_store;
	public ServerSatSerializer serverSatSerializer;
	public boolean learningMaxSubs = false;	
	public UndirectedGraph<KNode, KLink> g;
	public ArrayList<String> pathAgentNames;
	public Agent [] agents;
	public ServerKNetworkGraph kgraph;
	
	public String assertionAspectsStorePath;
	
	public double expectedSelfUncertaintyLevel_su;
	public double expectedSelfUncertaintyLevel_s;
	public double expectedSelfUncertaintyLevel_r;
	
	public double currentSelfUncertaintyLevel_su;
	public double currentSelfUncertaintyLevel_s;
	public double currentSelfUncertaintyLevel_r;
	
	public boolean subjectdone2;
	
	public String [] validAgents;

	//assertions related
	public String agentName;
	public Agent agent;
	
	public int a_counter;
	
	public ServerSatSerializer serversatserializer;
	
	public File beliefUncertaintyLevelFile;
	public File satTrainingMeanStoreFile;
	public File satAnalysisMeanStoreFile;
	public File viableProtocolRatioAnalysisMeanStoreFile;
	
}
