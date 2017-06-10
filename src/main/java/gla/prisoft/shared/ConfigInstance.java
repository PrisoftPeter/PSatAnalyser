package gla.prisoft.shared;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ConfigInstance implements Serializable{
	
	static final long serialVersionUID = 1L;
	public String [] evaluatedProtocols;
	
	public int no_agents = 5; //default
	public NetworkType networkType;
	public String sourceAgentName;
	public String targetAgentName;
	public String subjectName;
	public String selfAgentName;
	public ArrayList<String> selectedAgentPaths;
	public String [] selectedAgentPath;

	public boolean verifyAllAssertions;
	public String [] protocolSuite;
	public HashMap<String, Double> protocolCost;
	
	public boolean isTraining;
	public boolean runningTraining;
//	public boolean runningAnalysis;
		
	public boolean beliefReasoningActive;
	public boolean requestMandatory;
	public boolean consentMandatory;
	public boolean noticeMandatory;
	public boolean unlimitedPathSatAnalysis = true;
	public String [] processedPossibleWorldsPaths;
	public String s_path;
	
	public boolean withoutRequest;
	public boolean withoutConsent;
	public boolean withoutNotice;
	public boolean activeProtocolFilter;
	
	public int maxNoOfknowAgents;
	public int minNoOfknowAgents;
	public Random noOfKnownAgentsGenerator;
	
	public String listPathsData[];
	public int kNeighbours;
	
	public boolean is_aspect_run =true;
	public boolean is_dynamic_memory_store = true;
	public boolean log_knowledge_transformation = false;
	public boolean log_viable_disclosure_protocols = true;
	public boolean log_agent_knowledge_state = false;
//	public boolean log_entropy_belief_uncertainty = false;
	public boolean is_new_principal;
	public boolean is_new_target;
		
	public int max_analysis_path_length = 5;
	public int max_no_analysis_paths = 20;
	public int protocol_combination = 1;
	public double sat_treshold = 0.5;
	public boolean externalViewMode;
	public boolean networkMutationMode;
	
	public double costTradeoff = 1.0;
	public HashMap<String, Double> currentPrivacyGoal = new HashMap<String, Double>();
	public HashMap<String, Double> originalPrivacyGoal =new HashMap<String, Double>();
	
	public HashMap<String, Integer> maxPossiblePathKnowledgeSubsContainer =new HashMap<String, Integer>();
	public int categoryLimit;
	
	public static DecimalFormat df = new DecimalFormat(".###");
	
	public double completness;
	
//	public boolean isFiniteAnalysis;
//	public boolean pause;
	public boolean stop;
	public boolean isModePick = true;
	public boolean isModeUncertainty = false;
	public boolean isModeEntropy = false;
	public boolean isModeCommonKnowledge = false;
	public boolean isMemoryStoreMode = false;
	
	public String desiredEntropyDesc;
	public HashMap<Integer, Integer> PossibleKnowledgeSubstutitions = new HashMap<Integer, Integer>();
	public CombinationStrategy combinationStrategy = CombinationStrategy.MAXIMUM; //TODO: check for privacy goals maximisation paper using minmax algorithm
	
	public int noPaths;
	public String currentPath;
	
	public KnowledgeBase knowledgeBase;
	
	public int numEdgesToAttach;
	public int init_no_seeds;
	public int no_iterations;
	
	public int no_edges;
	public int degreeExponent;
	
	public double clusteringExponent;
	
	public boolean greaterThanOrEqualTo= false;
	public boolean lessThanOrEqualTo = true;
	
	public KnowledgeBase finiteAnalysisKb;
	public String aspectTypeHtml = "";
	public String aspectTypeRaw = "";
	
	public String sessionid;
	public boolean is_generating_memory_store;

	public boolean learningMaxSubs = false;	
	
	public int k = 5;
	public int old_k;
	
	public int noMemoryStores  =0;
	
	//iflow analysis
	public HashMap<String, Double> maxPathSats;	
	
	public String desiredCommonKnowledgeDesc;

//	//server specific variables
//	public ServerSatSerializer serverSatSerializer;
//	public UndirectedGraph<KNode, KLink> g;
//	public ArrayList<String> pathAgentNames;
//	public Agent [] agents;
//	public ServerKNetworkGraph kgraph;


}
