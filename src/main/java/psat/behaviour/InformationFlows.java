package psat.behaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import psat.Display;
import psat.PSatAPI;
import psat.behaviour.protocol.ProtocolFactory;
import psat.behaviour.transactions.Consent1Transaction;
import psat.behaviour.transactions.Consent2Transaction;
import psat.behaviour.transactions.Consent3Transaction;
import psat.behaviour.transactions.Consent4Transaction;
import psat.behaviour.transactions.Notice1RTransaction;
import psat.behaviour.transactions.Notice1SuTransaction;
import psat.behaviour.transactions.Notice2RTransaction;
import psat.behaviour.transactions.Notice2SuTransaction;
import psat.behaviour.transactions.RequestTransaction;
import psat.behaviour.transactions.Sent1Transaction;
import psat.behaviour.transactions.Sent2Transaction;
import psat.display.model.FeasibilityView;
import psat.display.model.KNetworkGraph;
import psat.display.model.LayeredBarChart;
import psat.knowledge.MemoryFactory;
import psat.knowledge.worlds.World;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.AssertionInstance;
import psat.util.AssertionRole;
import psat.util.Attribute;
import psat.util.CollectiveStrategy;
import psat.util.CombinationStrategy;
import psat.util.ConfigInstance;
import psat.util.PSatTableResult;
import psat.util.RowType;
import psat.util.SATResult;
import psat.util.SafeZone;
import psat.util.SatSerializer;

public class InformationFlows {
	
	private Attribute message;
	public String pathAgents[];
	public static String [] successfulSubs;
	public static int successfulSubsCount;
	public static double currentKnowledgeEntropy;
	public static double currentCommonKnowledge;

	
	public static String [] successfulSubsLearning;
	public static int successfulSubsCountLearning;
	
	public static double sumlocalsat;
	public static double countlocalsat;
	public static double sumglobalsat;
	public static double countglobalsat;
		
	public ArrayList<Long> executionTimes;
	
	public static ArrayList<String> processedAgents;
		
	private static ArrayList<String> processedSequences;
		
	public static ArrayList<SafeZone> safeZones;
		
	public boolean sat_treshold_reached = false;
	public boolean containRequest = false;
	public boolean containConsent = false;
	public boolean containNotice = false;
				
	public boolean run(String path){
		
    	PSatAPI.fvindex = 0;
		
		PSatAPI.instance.satSerializer.resetLongSatVals();
				
		executionTimes = new ArrayList<Long>();
		doRun(path);	
		Display.updateProgressComponent(100, "");
				
		return true;
	}

	public void doRun(String path){
		String sessionid = PSatAPI.instance.sessionid;	

		String evaluationProtocols [] = ProtocolFactory.getEvaluatedProtocols(sessionid);
		
		sumglobalsat =0;
		countglobalsat =0;
		
		//clean  evaluationProtocols
		String temp [] = new String[0];
		for(String s:evaluationProtocols){
			if(s == null){
				continue;
			}
			boolean contained = false;
			for(String s2: temp){
				if(s.equals(s2)){
					contained = true;
					break;
				}
			}
			if(!contained){
				String temp1 [] = new String[temp.length +1];
				for(int i=0;i<temp.length;i++){
					temp1[i] = temp[i];
				}
				temp1[temp.length] = s;
				temp = temp1;
			}
		}
		evaluationProtocols = temp;
		
		Display.updateProgressComponent(-1, "");
		if(path == null){
			path = KNetworkGraph.getSeq();
		}
//		int pathsIndex =0;
		if(path != null && path.trim().length()>0){
			PSatAPI.isnextpath = true;
			PSatAPI.higherOrderKs = new HashMap<World, ArrayList<World>>();
			
			sat_treshold_reached = false;
			containRequest = false;
			containConsent = false;
			containNotice = false;
			
			String pathId = "1";
			if(path.contains(":")){
				String pathAgents1[] =path.split(": ");
				pathId = pathAgents1[0];
				String pathAgents2 = pathAgents1[1];
				pathAgents =pathAgents2.split(" ");
			}
			else{
				pathAgents =path.split(" ");
			}
			
			
			PSatAPI.instance.currentPath = path;
			PSatAPI.netSerialiseConfigInstance();
			if(PSatAPI.instance.isTraining){
				Display.feasibilityView.timeSeriesChart.addMarker(":"+pathId);				
			}
			
			else{
				Display.feasibilityView.timeSeriesChart.addMarker(":"+pathId);				
			}
			
			if(PSatAPI.instance.isTraining){
//				spectrum = new ViabilitySpectrum(selectedPath,pathId, pathAgents);						

				doLocalTrainingRun(path,evaluationProtocols);
				
//				ViabilitySpectrum.serializeSpectrum(spectrum, instance);
			}							
			
//			pathsIndex = pathsIndex +1;
//			instance.completness = ((double)pathsIndex/(double)selectedPaths.size())*100;
			PSatAPI.instance.completness = 100;
			Display.updateProgressComponent(new Double(PSatAPI.instance.completness).intValue(), ConfigInstance.df.format(PSatAPI.instance.completness)+"%");
						
			PSatAPI.instance.runningTraining = false;
			LayeredBarChart lbc = new LayeredBarChart();
			Display.window.createLayeredDecisionBarViewPage(lbc);
			FeasibilityView.ptrs = new ArrayList<PSatTableResult>();
		}
		
	}
	
	
	
	private void doLocalTrainingRun(String path,String evaluationProtocols []){
		//String sessionid = sinstance.sessionid;
		message = AgentFactory.getAgent(PSatAPI.instance.subjectName).getPersonalAttributes()[0];
		
		PSatAPI.instance.selectedAgentPath = pathAgents;
		
//		ClientServerBroker.messageEvent("ClientKNetworkGraph.resetColoredLinks()", "", null,null);
//		ClientServerBroker.messageEvent("ClientKNetworkGraph.resetColoredNodes()", "", null,null);
//		ClientServerBroker.messageEvent("Display.updateNetworkNode()", "", null,null); 
				
		
        int n = evaluationProtocols.length; 
        processedSequences = new ArrayList<String>();
        runNProtocols(path, evaluationProtocols, n,PSatAPI.instance.satSerializer);

			
	}


	private int getMaxNoOfPossibleKnowledgeSubstutitions(String [] tempPathAgents){
		int maxSuccessfulPathSubsCount = 0;
		boolean found =false;
		
		String pp = "";
	    for(String p: pathAgents){
	    	pp = pp+ p;
	    }
		for (Map.Entry<String, Integer> entry : PSatAPI.instance.maxPossiblePathKnowledgeSubsContainer.entrySet()) {
		    String key = entry.getKey();					    
		    if(key.equals(pp)){
		    	maxSuccessfulPathSubsCount = entry.getValue();
		    	found = true;
		    	break;
		    }
		}
		if(!found){
			String sessionid = PSatAPI.instance.sessionid;

			for (Map.Entry<Integer, Integer> entry : PSatAPI.instance.PossibleKnowledgeSubstutitions.entrySet()) {
			    Integer key = entry.getKey();
			    
			    if(key.intValue() == tempPathAgents.length){
			    	return entry.getValue();
			    }		    
			}
			
			//MaxNoOfPossibleKnowledgeSubstutitions not known
			PSatAPI.instance.learningMaxSubs = true;

			PSatAPI.instance.learningMaxSubs = PSatAPI.instance.learningMaxSubs;
			Display.updateLogPage("calculating max path knowledge transformations...", false);
			
			String evaluationProtocols [] = ProtocolFactory.getProtocolSuite(sessionid);
			
			//clean  evaluationProtocols
			String temp [] = new String[0];
			for(String s:evaluationProtocols){
				if(s == null){
					continue;
				}
				boolean contained = false;
				for(String s2: temp){
					if(s.equals(s2)){
						contained = true;
						break;
					}
				}
				if(!contained){
					String temp1 [] = new String[temp.length +1];
					for(int i=0;i<temp.length;i++){
						temp1[i] = temp[i];
					}
					temp1[temp.length] = s;
					temp = temp1;
				}
			}
			evaluationProtocols = temp;
						
			
			for(String protocolDesc:evaluationProtocols){

				successfulSubsLearning = new String[0];
				successfulSubsCountLearning = 0;
					
				String pd[] = protocolDesc.split(" \\(");
				
				protocolDesc = pd[1];
				protocolDesc = protocolDesc.replace(")", "");
								
				String protocol[] = protocolDesc.split(",");
				
				for(int k=0;k<tempPathAgents.length-1;k++){
										
					if(PSatAPI.instance.stop){
						break;
					}
					
					String senderName = tempPathAgents[k];
					String recipientName = tempPathAgents[k+1];
									
					message = AgentFactory.getAgent(PSatAPI.instance.subjectName).getPersonalAttributes()[0];
					execute(PSatAPI.instance.subjectName, senderName, recipientName, message, protocol, sessionid);

					if(successfulSubsCountLearning > maxSuccessfulPathSubsCount){
						maxSuccessfulPathSubsCount = successfulSubsCountLearning;
					}
				}	
				
				for(int k=0;k<tempPathAgents.length;k++){
					MemoryFactory.restoreMemoryFromClone(tempPathAgents[k], PSatAPI.instance.subjectName, sessionid);	
				}
				
				for(int k=0;k<tempPathAgents.length;k++){
					MemoryFactory.createMemoryClone(tempPathAgents[k], PSatAPI.instance.subjectName, sessionid);	
				}
					
			}
			
			PSatAPI.instance.PossibleKnowledgeSubstutitions.put(tempPathAgents.length, maxSuccessfulPathSubsCount);
			PSatAPI.instance.learningMaxSubs = false;

			PSatAPI.instance.maxPossiblePathKnowledgeSubsContainer.put(pp, maxSuccessfulPathSubsCount);
		}
		
		

		return maxSuccessfulPathSubsCount;
	}
	
	private int localcount = 1;
	public void ProtocolPerm(String path, String[] inputs, int currentFocus, SatSerializer sat_output){
		String sessionid = PSatAPI.instance.sessionid;

		if(localcount > 3){			
			localcount = localcount+1;
		}
		else{
			if(PSatAPI.instance !=null && PSatAPI.instance.stop){
				return;
			}
			
			localcount = 1;
		}
		
		
		PSatAPI.instance.runningTraining = true;	
				
		if(inputs.length != pathAgents.length){
			String[] tinputs = new String[pathAgents.length];
			int count = 0;
			while(count < pathAgents.length){
				for(int i=0; i<inputs.length;i++){
					if(count < pathAgents.length){
						tinputs[count] = inputs[i];
					}
					count = count+1;							
				}
			}
			inputs = tinputs;
		}
		
		if (currentFocus == inputs.length - 1) {			
			boolean contained = false;
			String sy ="";
			for(String ss:inputs){
				sy = sy+ss;
			}
			for(String s:processedSequences){
				if(s.equals(sy)){
					contained = true;
				}
			}
			
			if(!contained){ 
				long startTime = System.nanoTime();	
				double meancollectivegoalsum =0;
				double meancollectivegoalcount = 0;
				
				for(int k=0;k<pathAgents.length;k++){
					Agent a = AgentFactory.getAgent(pathAgents[k]);
					if(!a.containedInMemoryStores(PSatAPI.instance.subjectName)){
						MemoryFactory.newMemoryStore(a.getAgentName());
					}	
					
					//patchup
					if(!(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE)){
						double cgoalv = retrieveCollectiveMeanGoal(a);
						if(cgoalv>0){
							meancollectivegoalsum = meancollectivegoalsum+ cgoalv;
							meancollectivegoalcount = meancollectivegoalcount+1;
						}

					}
				}
				
				for(int k=0;k<pathAgents.length;k++){
					MemoryFactory.createMemoryClone(pathAgents[k], PSatAPI.instance.subjectName, sessionid);	
				}

				int protocolIndex = 0;

				String protocol_st = "(";
				String path_ss ="";
				for(String s:pathAgents){
					path_ss = path_ss+"-"+s;
				}
				
				int maxPossiblePathKnowledgeSubs  =0;
				if(PSatAPI.instance.isModeEntropy){
					maxPossiblePathKnowledgeSubs = getMaxNoOfPossibleKnowledgeSubstutitions(pathAgents);	

				}
				
				successfulSubs = new String[0];
				successfulSubsCount = 0;
				currentCommonKnowledge = 0;
				sumlocalsat = 0;
				countlocalsat = 0;
				double pathSat = 0; //local pathsat (involves a single path)
								
				Agent su = null;
				ArrayList<Agent> agentsInPath = new ArrayList<Agent>();
				for(String agentName:pathAgents){
					Agent a = PSatAPI.netGetAgent(agentName);
					if(a ==null){
						a = PSatAPI.netGetAgent(agentName); //try again
					}
					agentsInPath.add(a);
					
					if(agentName.equals(PSatAPI.instance.subjectName)){
						su = a;
					}
				}
				
				if(PSatAPI.instance.collectiveStrategy != CollectiveStrategy.NONE){					
					MemoryFactory.extractCollectiveAssertions(PSatAPI.instance.subjectName, pathAgents);
				}
				
				processedAgents = new ArrayList<String>();
				processedAgents.add(PSatAPI.instance.subjectName);
				
				for(int k=0;k<pathAgents.length-1;k++){										
					
					String senderName = pathAgents[k];
					String recipientName = pathAgents[k+1];
					
					processedAgents.add(senderName);
					processedAgents.add(recipientName);
									
					Agent s = null;
					Agent r = null;
					for(Agent a:agentsInPath){
						if(a.getAgentName().equals(senderName)){
							s = a;
						}
						else if(a.getAgentName().equals(recipientName)){
							r = a;
						}
						if(s!= null && r!=null){
							break;
						}
						
					}
					String protocolDesc = inputs[protocolIndex];	
					protocolIndex = protocolIndex+1;
					String pd[] = protocolDesc.split(" \\(");
					
					String alpha = pd[0];
					protocol_st = protocol_st+alpha+" ";
					protocolDesc = pd[1];
					protocolDesc = protocolDesc.replace(")", "");
									
					String protocol[] = protocolDesc.split(",");
					execute(PSatAPI.instance.subjectName, senderName, recipientName, message, protocol, sessionid);

					PSatAPI.instance.satSerializer.resetRequirementDesc();
					
					
					if(PSatAPI.instance.isModeEntropy){
						currentKnowledgeEntropy = (double)successfulSubsCount/(double)maxPossiblePathKnowledgeSubs;
						
						double collectiveDesiredEntropy = 0;
						
						double desiredEntropy_su = su.getDesiredEntropy();
						double desiredEntropy_s = s.getDesiredEntropy();
						double desiredEntropy_r = r.getDesiredEntropy();
						
						if(PSatAPI.instance.combinationStrategy == CombinationStrategy.MINIMUM){
							collectiveDesiredEntropy = desiredEntropy_su;
							if(collectiveDesiredEntropy > desiredEntropy_s){
								collectiveDesiredEntropy = desiredEntropy_s;
							}
							if(collectiveDesiredEntropy > desiredEntropy_r){
								collectiveDesiredEntropy = desiredEntropy_r;
							}
						}
						else if(PSatAPI.instance.combinationStrategy == CombinationStrategy.MAXIMUM){
							collectiveDesiredEntropy = desiredEntropy_su;
							if(collectiveDesiredEntropy < desiredEntropy_s){
								collectiveDesiredEntropy = desiredEntropy_s;
							}
							if(collectiveDesiredEntropy < desiredEntropy_r){
								collectiveDesiredEntropy = desiredEntropy_r;
							}
						}
						else if(PSatAPI.instance.combinationStrategy == CombinationStrategy.AVERAGE){
							double sumDesiredEntropy = desiredEntropy_su+desiredEntropy_s+desiredEntropy_r;
							collectiveDesiredEntropy = sumDesiredEntropy/3;

						}
						
						if(PSatAPI.instance.greaterThanOrEqualTo){
							if(currentKnowledgeEntropy >= collectiveDesiredEntropy){
								pathSat = 1;
							}
							else{
								double difference = Math.abs(collectiveDesiredEntropy - currentKnowledgeEntropy);
								pathSat = 1-difference;
							}	
						}
						else if(PSatAPI.instance.lessThanOrEqualTo){
							if(currentKnowledgeEntropy <= collectiveDesiredEntropy){
								pathSat = 1;
							}
							else{
								double difference = Math.abs(collectiveDesiredEntropy - currentKnowledgeEntropy);
								pathSat = 1-difference;
							}
						}
						
//						if(instance.greaterThanOrEqualTo){ //10 is a placeholder
//							spectrum.updatePathFlows(senderName, recipientName, protocolDesc, alpha, pathSat, 10, 10, collectiveDesiredEntropy, currentKnowledgeEntropy, Operator.GREATERTHANOREQUALTO, null, instance);
//						}
//						else if(instance.lessThanOrEqualTo){
//							spectrum.updatePathFlows(senderName, recipientName, protocolDesc, alpha, pathSat, 10, 10, collectiveDesiredEntropy, currentKnowledgeEntropy, Operator.LESSTHANOREQUALTO, null, instance);
//						}						
						
						
						pathSat = new Double(ConfigInstance.df.format(pathSat));
						pathSat = Display.RoundTo2Decimals(pathSat);
						
						PSatAPI.instance.satSerializer.protocolDesc = "a-"+alpha;
						PSatAPI.instance.satSerializer.iflow = senderName+"->"+recipientName;
						
						
						String desc = "[Desired Knowledge Entropy";
						if(PSatAPI.instance.greaterThanOrEqualTo){
							desc = desc+"≥";
						}
						else if(PSatAPI.instance.lessThanOrEqualTo){
							desc = desc+"≤";
						}
						desc = desc+(Math.round(collectiveDesiredEntropy * 100.0) / 100.0);
						desc = desc+" Actual Knowledge Entropy="+(Math.round(currentKnowledgeEntropy * 100.0) / 100.0)+"]";
						if(!PSatAPI.instance.satSerializer.requirementHtmlDesc.contains(desc)){
							if(PSatAPI.instance.satSerializer.requirementHtmlDesc.length() >0){
								PSatAPI.instance.satSerializer.requirementHtmlDesc = PSatAPI.instance.satSerializer.requirementHtmlDesc +" ; ";
								PSatAPI.instance.satSerializer.requirementRawDesc = PSatAPI.instance.satSerializer.requirementRawDesc + ";";
							}
							PSatAPI.instance.satSerializer.requirementHtmlDesc = PSatAPI.instance.satSerializer.requirementHtmlDesc +desc;	
							PSatAPI.instance.satSerializer.requirementRawDesc = PSatAPI.instance.satSerializer.requirementRawDesc + desc;							
						}			
						
						String alphaProtocol = "&#945;<sub>"+alpha+"</sub>=["+protocolDesc+"]";
						PSatAPI.instance.satSerializer.updateProtocolHtmlFullDesc(alphaProtocol);
						
						String protocol_pattern = alpha+" ("+protocolDesc+")";
						double protocol_cost = PSatAPI.instance.protocolCost.get(protocol_pattern);
						double max_protocol_cost = ProtocolFactory.getMaxCost();
						double normalised_cost = PSatAPI.instance.costTradeoff*(protocol_cost/max_protocol_cost);
						double normalised_cost_no_tradeoff = protocol_cost/max_protocol_cost;
						
						double collectiveGoalValue = suggestCollectiveGoalValue(su,  s, r,PSatAPI.instance.subjectName, senderName, recipientName); //v						
						double benefit = 0;
						if(pathSat == -1 ||collectiveGoalValue==0){ //when goal=0, feasibility is determined based on only cost
							benefit =1;
						}
						else{
							benefit =1- Math.abs(pathSat-collectiveGoalValue);
							if(benefit ==0){
								benefit = 0.0000000006; //to avoid divide by 0
							}
						}
												
						double feasibility = normalised_cost/benefit;
						
						normalised_cost = Display.RoundTo3Decimals(normalised_cost);
						benefit = Display.RoundTo3Decimals(benefit);
						feasibility = Display.RoundTo3Decimals(feasibility);
						normalised_cost_no_tradeoff = Display.RoundTo3Decimals(normalised_cost_no_tradeoff);
						
						String decision = "";
						if(feasibility < 1){
							decision = "YES";
						}
						else if(feasibility == 1){
							decision = "MAYBE";
						}
						else if(feasibility > 1){
							decision = "NO";
						}
						
						sat_output.displaySat(path, PSatAPI.instance.subjectName, 
											  senderName, recipientName, alphaProtocol, -10, -10, -10, pathSat, 
											  null,null, normalised_cost_no_tradeoff, collectiveGoalValue,benefit,feasibility, decision);						
//						sat_output.displaySat(sinstance.serverSatSerializer.currentPath, instance.selfAgentName, senderName, recipientName, alphaProtocol, -10, -10, -10, pathSat, null,null, instance);						
//												
					}
					else{

						SATResult self_result;
						SATResult r_result;
						SATResult s_result;
						
						double self_sat =0;
						double r_sat=0;
						double s_sat=0;
						
						if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE){
							//compute satisfiability of su privacy requirements
							self_result = MemoryFactory.sat(PSatAPI.instance.subjectName, PSatAPI.instance.subjectName, senderName, recipientName,sat_output, message);
							self_sat = Math.round(self_result.getSat() * 100.0) / 100.0;
											
							//compute satisfiability of s privacy requirements
							s_result = MemoryFactory.sat(senderName, PSatAPI.instance.subjectName,senderName, recipientName, sat_output, message);
							s_sat = Math.round(s_result.getSat() * 100.0) / 100.0;

							//compute satisfiability of r privacy requirements
							r_result = MemoryFactory.sat(recipientName, PSatAPI.instance.subjectName,senderName, recipientName, sat_output, message);
							r_sat = Math.round(r_result.getSat() * 100.0) / 100.0;	
						}
						else{
							//compute satisfiability of su privacy requirements
							self_result = MemoryFactory.collectivesat(PSatAPI.instance.subjectName, PSatAPI.instance.subjectName, senderName, recipientName,sat_output, message,agentsInPath);
							self_sat = Math.round(self_result.getSat() * 100.0) / 100.0;
											
							//compute satisfiability of s privacy requirements
							s_result = MemoryFactory.collectivesat(senderName, PSatAPI.instance.subjectName,senderName, recipientName, sat_output, message,agentsInPath);
							s_sat = Math.round(s_result.getSat() * 100.0) / 100.0;

							//compute satisfiability of r privacy requirements
							r_result = MemoryFactory.collectivesat(recipientName, PSatAPI.instance.subjectName,senderName, recipientName, sat_output, message,agentsInPath);
							r_sat = Math.round(r_result.getSat() * 100.0) / 100.0;	
						}
						
						
						double max = 0;
						double maxcount = 0;
						if(self_sat >-1){
							max = max + self_sat;
							maxcount = maxcount +1;
						}
						if(r_sat >-1){
							max = max + r_sat;
							maxcount = maxcount +1;
						}
						if(s_sat >-1){
							max = max + s_sat;
							maxcount = maxcount +1;
						}
						
						//display/write sat
						String alphaProtocol = "&#945;<sub>"+alpha+"</sub>=["+protocolDesc+"]";
						PSatAPI.instance.satSerializer.updateProtocolHtmlFullDesc(alphaProtocol);


						if(self_sat >-1){
							sumlocalsat = sumlocalsat +self_sat;
							countlocalsat = countlocalsat+1;
							
							sumglobalsat = sumglobalsat +self_sat;
							countglobalsat = countglobalsat+1;
						}
						if(r_sat >-1){
							sumlocalsat = sumlocalsat +r_sat;
							countlocalsat = countlocalsat+1;
							
							sumglobalsat = sumglobalsat +r_sat;
							countglobalsat = countglobalsat+1;
						}
						if(s_sat >-1){
							sumlocalsat = sumlocalsat +s_sat;
							countlocalsat = countlocalsat+1;
							
							sumglobalsat = sumglobalsat +s_sat;
							countglobalsat = countglobalsat+1;
						}
						
						if(self_sat >-1 ||r_sat >-1||s_sat >-1){
							pathSat = sumlocalsat/ countlocalsat;
						}
						else{
							pathSat = -1;
						}
					
						
						pathSat = new Double(ConfigInstance.df.format(pathSat));
						pathSat = Display.RoundTo2Decimals(pathSat);
						
						String protocol_pattern = alpha+" ("+protocolDesc+")";
						double protocol_cost = PSatAPI.instance.protocolCost.get(protocol_pattern);
						double max_protocol_cost = ProtocolFactory.getMaxCost();
						double normalised_cost_no_tradeoff = protocol_cost/max_protocol_cost;
						double normalised_cost = PSatAPI.instance.costTradeoff*(protocol_cost/max_protocol_cost);
						
												
						double collectiveGoalValue = 0;
						
						if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE){
							collectiveGoalValue	= suggestCollectiveGoalValue( su,  s, r,PSatAPI.instance.subjectName, senderName, recipientName); //v
						}
						else{
							collectiveGoalValue = meancollectivegoalsum/ meancollectivegoalcount;
						}
						
						double benefit = 0;
						if(pathSat == -1 ||collectiveGoalValue==0){ //when goal=0, feasibility is determined based on only cost
							benefit =1;
						}
						else{
							benefit =1- Math.abs(pathSat-collectiveGoalValue);
							if(benefit ==0){
								benefit = 0.0000000006; //to avoid divide by 0
							}
						}
						
						double feasibility = normalised_cost/benefit;
						
						benefit = Display.RoundTo3Decimals(benefit);
						normalised_cost = Display.RoundTo3Decimals(normalised_cost);
						feasibility = Display.RoundTo3Decimals(feasibility);
						normalised_cost_no_tradeoff = Display.RoundTo3Decimals(normalised_cost_no_tradeoff);
						
						String decision = "";
						if(feasibility < 1){
							decision = "YES";
						}
						else if(feasibility == 1){
							decision = "MAYBE";
						}
						else if(feasibility > 1){
							decision = "NO";
						}
																	
						sat_output.displaySat(path, PSatAPI.instance.subjectName, 
											  senderName, recipientName, alphaProtocol, self_sat, s_sat, 
											  r_sat, pathSat, null,null, normalised_cost_no_tradeoff, collectiveGoalValue,benefit,feasibility, 
											  decision);
					}


//					if(instance.log_agent_knowledge_state || instance.log_entropy_belief_uncertainty){
					if(PSatAPI.instance.log_agent_knowledge_state){
						PSatAPI.instance.satSerializer.protocolDesc = "a-"+alpha;
						PSatAPI.instance.satSerializer.iflow = senderName+"->"+recipientName;
						
						MemoryFactory.dumpMemoryStoreOnDisplay(PSatAPI.instance.subjectName, pathSat);
						if(!senderName.equals(PSatAPI.instance.subjectName)){
							MemoryFactory.dumpMemoryStoreOnDisplay(senderName, pathSat);
						}
						if(!recipientName.equals(PSatAPI.instance.subjectName)){
							MemoryFactory.dumpMemoryStoreOnDisplay(recipientName, pathSat); 
						}
					}
				}
				
				long endTime = System.nanoTime();
				long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
				
				executionTimes.add(duration);
				
				PSatTableResult ptr_row = new PSatTableResult();
				ptr_row.setIndex(PSatAPI.fvindex);
				PSatAPI.fvindex = PSatAPI.fvindex+1;
				ptr_row.setRowType(RowType.EMPTY);
					
				Display.feasibilityView.addTableRow(PSatAPI.instance, ptr_row);
										
				if(!PSatAPI.instance.isModeEntropy){
					updateBenchmarks(sumlocalsat/countlocalsat, inputs[protocolIndex], sessionid);
				}
				
				processedSequences.add(sy);

				for(int k=0;k<pathAgents.length;k++){
					MemoryFactory.restoreMemoryFromClone(pathAgents[k], PSatAPI.instance.subjectName, sessionid);	
				}	
			}
			
			if(PSatAPI.isnextpath){
				PSatAPI.logHighOrderImplications();
				PSatAPI.isnextpath = false;
			}
			
			return;
		}
		ProtocolPerm(path,inputs, currentFocus + 1, sat_output);

//		for (int i = currentFocus + 1; i < inputs.length; i++) {
//			String temp = inputs[currentFocus];
//			inputs[currentFocus] = inputs[i];
//			inputs[i] = temp;
//			ProtocolPerm(path,inputs, currentFocus + 1, sat_output, sinstance, instance);
//
//		}		
		
	}
	
	private double retrieveCollectiveMeanGoal(Agent agent){
		
		ArrayList<Double> vlocalgoals = new ArrayList<Double>();

		if(PSatAPI.instance.isModePick){
			if(PSatAPI.instance.is_role_run){
				AssertionRole[] assertionRoles1 = agent.getRoles();
				if(assertionRoles1 !=null){
					for(AssertionRole ap:assertionRoles1){
						vlocalgoals.add(ap.getGoalv());
					}
				}							
			}
			else{				
				AssertionInstance[] assertionInstance1 = agent.getAssertionInstances();
				if(assertionInstance1 !=null){
					for(AssertionInstance ap:assertionInstance1){
						vlocalgoals.add(ap.getGoalv());
					}
				}								
			}				
		}
		else if(PSatAPI.instance.isModeUncertainty){
			if(agent.getKnowledgeLevels().length >0){
				vlocalgoals.add(agent.getGlobalPrivacyGoal_v());
			}
		}	
		else if(PSatAPI.instance.isModeEntropy){
			if(agent.getDesiredEntropy() >0){
				vlocalgoals.add(agent.getGlobalPrivacyGoal_v());
			}
		}
		
		double collectivevgoal = 0;

		//collective goal strategy
		if(PSatAPI.instance.combinationStrategy == CombinationStrategy.MINIMUM){
			double vmingoal = 0;
			for(double d:vlocalgoals){
				if(d <vmingoal){
					vmingoal = d;
				}
			}		
			collectivevgoal = vmingoal;
		}
		else if(PSatAPI.instance.combinationStrategy == CombinationStrategy.MAXIMUM){
			double vmaxgoal = 0;
			for(double d:vlocalgoals){
				if(d >vmaxgoal){
					vmaxgoal = d;
				}
			}		
			collectivevgoal = vmaxgoal;
		}
		else if(PSatAPI.instance.combinationStrategy == CombinationStrategy.AVERAGE){
			double vsumgoals = 0;
			for(double d:vlocalgoals){
				vsumgoals = vsumgoals+d;
			}		
			collectivevgoal = vsumgoals/vlocalgoals.size();

		}

		//TODO: apply minmax algorithm strategy	
		return collectivevgoal;
	}
	
	private double suggestCollectiveGoalValue(Agent subject, Agent sender, Agent recipient, String subjectName, String senderName, String recipientName){
		if(subject == null){
			subject = PSatAPI.netGetAgent(subjectName);
		}
		if(sender == null){
			sender = PSatAPI.netGetAgent(senderName);
		}
		if(recipient == null){
			recipient = PSatAPI.netGetAgent(recipientName);
		}
		
		ArrayList<Double> vlocalgoals = new ArrayList<Double>();
		
		if(PSatAPI.instance.isModePick){
			if(PSatAPI.instance.is_role_run){
				AssertionRole[] assertionRoles1 = subject.getRoles();
				if(assertionRoles1 !=null){
					for(AssertionRole ap:assertionRoles1){
						vlocalgoals.add(ap.getGoalv());
					}
				}
				
				AssertionRole[] assertionRoles2 = sender.getRoles();
				if(assertionRoles2 !=null){
					for(AssertionRole ap:assertionRoles2){
						vlocalgoals.add(ap.getGoalv());
					}
				}
				AssertionRole[] assertionRoles3 = recipient.getRoles();
				if(assertionRoles3 !=null){
					for(AssertionRole ap:assertionRoles3){
						vlocalgoals.add(ap.getGoalv());
					}		
				}
											
			}
			else{				
				AssertionInstance[] assertionInstance1 = subject.getAssertionInstances();
				if(assertionInstance1 !=null){
					for(AssertionInstance ap:assertionInstance1){
						vlocalgoals.add(ap.getGoalv());
					}
				}
				AssertionInstance[] assertionInstance2 = sender.getAssertionInstances();
				if(assertionInstance2 !=null){
					for(AssertionInstance ap:assertionInstance2){
						vlocalgoals.add(ap.getGoalv());
					}
				}
				AssertionInstance[] assertionInstance3 = recipient.getAssertionInstances();
				if(assertionInstance3 !=null){
					for(AssertionInstance ap:assertionInstance3){
						vlocalgoals.add(ap.getGoalv());
					}
				}								
			}				
		}
		else if(PSatAPI.instance.isModeUncertainty){
			if(subject.getKnowledgeLevels().length >0){
				vlocalgoals.add(subject.getGlobalPrivacyGoal_v());
			}
			if(sender.getKnowledgeLevels().length >0){
				vlocalgoals.add(sender.getGlobalPrivacyGoal_v());
			}
			if(recipient.getKnowledgeLevels().length >0){
				vlocalgoals.add(recipient.getGlobalPrivacyGoal_v());
			}
		}	
		else if(PSatAPI.instance.isModeEntropy){
			if(subject.getDesiredEntropy() >0){
				vlocalgoals.add(subject.getGlobalPrivacyGoal_v());
			}
			if(sender.getDesiredEntropy() >0){
				vlocalgoals.add(sender.getGlobalPrivacyGoal_v());			
			}
			if(recipient.getDesiredEntropy() >0){
				vlocalgoals.add(recipient.getGlobalPrivacyGoal_v());
			}
		}
		
		double collectivevgoal = 0;

		//collective goal strategy
		if(PSatAPI.instance.combinationStrategy == CombinationStrategy.MINIMUM){
			double vmingoal = 0;
			for(double d:vlocalgoals){
				if(d <vmingoal){
					vmingoal = d;
				}
			}		
			collectivevgoal = vmingoal;
		}
		else if(PSatAPI.instance.combinationStrategy == CombinationStrategy.MAXIMUM){
			double vmaxgoal = 0;
			for(double d:vlocalgoals){
				if(d >vmaxgoal){
					vmaxgoal = d;
				}
			}		
			collectivevgoal = vmaxgoal;
		}
		else if(PSatAPI.instance.combinationStrategy == CombinationStrategy.AVERAGE){
			double vsumgoals = 0;
			for(double d:vlocalgoals){
				vsumgoals = vsumgoals+d;
			}		
			collectivevgoal = vsumgoals/vlocalgoals.size();

		}

		//TODO: apply minmax algorithm strategy	
		return collectivevgoal;
	}
	
	
	public static void resetGlobalGoalForAllPathAgents(ConfigInstance instance,String path, double newgoalvalue){
		
		String[] pathAgents = null;
		if(path.contains(":")){
			String pathAgents1[] =path.split(": ");
			String pathAgents2 = pathAgents1[1];
			pathAgents =pathAgents2.split(" ");
		}
		else{
			pathAgents =path.split(" ");
		}
		
		ArrayList<Agent> agentsInPath = new ArrayList<Agent>();
		for(String agentName:pathAgents){
			if(agentName != null && agentName.trim().length()>0){
				Agent a = PSatAPI.netGetAgent(agentName.trim());
				agentsInPath.add(a);
			}
		}
		
		if(instance.isModePick){
			if(instance.is_role_run){
				for(Agent a:agentsInPath){
					AssertionRole[] assertionRoles1 = a.getRoles();
					if(assertionRoles1 !=null){
						for(AssertionRole ap:assertionRoles1){
							ap.setGoalv(newgoalvalue);
							a.updateRole(ap);
						}
					}	
					if(assertionRoles1 != null){
						if(assertionRoles1.length >0){
							PSatAPI.netWriteAgent(a);
						}
					}
					
				}
											
			}
			else{			
				for(Agent a:agentsInPath){
					AssertionInstance[] assertionInstance1 = a.getAssertionInstances();
					if(assertionInstance1 !=null){
						for(AssertionInstance ap:assertionInstance1){
							a.updateAssertionInstance(ap.getAssertion(),newgoalvalue,PSatAPI.instance.collectiveStrategy);
						}
					}
					if(assertionInstance1.length >0){
						PSatAPI.netWriteAgent(a);
					}
				}							
			}				
		}
		else if(instance.isModeUncertainty){
			for(Agent a:agentsInPath){
				if(a.getKnowledgeLevels().length >0){
					a.setGlobalPrivacyGoal_v(newgoalvalue);
					PSatAPI.netWriteAgent(a);
				}
			}
		}	
		else if(instance.isModeEntropy){
			for(Agent a:agentsInPath){
				if(a.getDesiredEntropy() >0){
					a.setGlobalPrivacyGoal_v(newgoalvalue);
					PSatAPI.netWriteAgent(a);
				}
			}
		}
		
	}
	
	public static double suggestOriginalCollectiveGoalValue(ConfigInstance instance){
		
		ArrayList<Double> vlocalgoals = new ArrayList<Double>();
		
		ArrayList<Agent> agentsInPath = new ArrayList<Agent>();
		for(String agentName:instance.selectedAgentPath){
			Agent a = PSatAPI.netGetAgent(agentName);
			agentsInPath.add(a);
		}
		
		if(instance.isModePick){
			if(instance.is_role_run){
				for(Agent a:agentsInPath){
					AssertionRole[] assertionRoles1 = a.getRoles();
					if(assertionRoles1 !=null){
						for(AssertionRole ap:assertionRoles1){
							vlocalgoals.add(ap.getGoalv());
						}
					}					
				}
											
			}
			else{			
				for(Agent a:agentsInPath){
					AssertionInstance[] assertionInstance1 = a.getAssertionInstances();
					if(assertionInstance1 !=null){
						for(AssertionInstance ap:assertionInstance1){
							vlocalgoals.add(ap.getGoalv());
						}
					}
				}							
			}				
		}
		else if(instance.isModeUncertainty){
			for(Agent a:agentsInPath){
				if(a.getKnowledgeLevels().length >0){
					vlocalgoals.add(a.getGlobalPrivacyGoal_v());
				}
			}
		}	
		else if(instance.isModeEntropy){
			for(Agent a:agentsInPath){
				if(a.getDesiredEntropy() >0){
					vlocalgoals.add(a.getGlobalPrivacyGoal_v());
				}
			}
		}
		
		double collectivevgoal = 0;

		//collective goal strategy
		if(instance.combinationStrategy == CombinationStrategy.MINIMUM){
			double vmingoal = 0;
			for(double d:vlocalgoals){
				if(d <vmingoal){
					vmingoal = d;
				}
			}		
			collectivevgoal = vmingoal;
		}
		else if(instance.combinationStrategy == CombinationStrategy.MAXIMUM){
			double vmaxgoal = 0;
			for(double d:vlocalgoals){
				if(d >vmaxgoal){
					vmaxgoal = d;
				}
			}		
			collectivevgoal = vmaxgoal;
		}
		else if(instance.combinationStrategy == CombinationStrategy.AVERAGE){
			double vsumgoals = 0;
			for(double d:vlocalgoals){
				vsumgoals = vsumgoals+d;
			}		
			collectivevgoal = vsumgoals/vlocalgoals.size();

		}

		//TODO: apply minmax algorithm strategy		
		return collectivevgoal;
	}
	
	private void updateBenchmarks(double pathSat, String protocolDesc, String sessionid){

		if(pathSat >=PSatAPI.instance.sat_treshold){
			sat_treshold_reached = true;
			
			if(protocolDesc.contains("Request")){
				containRequest = true;
			}
			if(protocolDesc.contains("Consent")){
				containConsent = true;
			}
			if(protocolDesc.contains("Notice")){
				containNotice = true;
			}
		}
	}
	
	
	private boolean tresholdsSatisfied(){

		boolean tresholdSatisfied = false;
		
		if(!PSatAPI.instance.unlimitedPathSatAnalysis){
			if(sat_treshold_reached){
				if(PSatAPI.instance.consentMandatory && PSatAPI.instance.noticeMandatory && PSatAPI.instance.requestMandatory){
					if(containRequest && containConsent && containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(PSatAPI.instance.consentMandatory && PSatAPI.instance.noticeMandatory && !PSatAPI.instance.requestMandatory){
					if(containConsent && containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(PSatAPI.instance.consentMandatory && !PSatAPI.instance.noticeMandatory && PSatAPI.instance.requestMandatory){
					if(containRequest && containConsent){
						tresholdSatisfied = true;
					}
				}
				else if(!PSatAPI.instance.consentMandatory && PSatAPI.instance.noticeMandatory && PSatAPI.instance.requestMandatory){
					if(containRequest && containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(PSatAPI.instance.consentMandatory && !PSatAPI.instance.noticeMandatory && !PSatAPI.instance.requestMandatory){
					if(containConsent){
						tresholdSatisfied = true;
					}
				}
				else if(!PSatAPI.instance.consentMandatory && PSatAPI.instance.noticeMandatory && !PSatAPI.instance.requestMandatory){
					if(containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(!PSatAPI.instance.consentMandatory && !PSatAPI.instance.noticeMandatory && PSatAPI.instance.requestMandatory){
					if(containRequest){
						tresholdSatisfied = true;
					}
				}
				else if(!PSatAPI.instance.consentMandatory && !PSatAPI.instance.noticeMandatory && !PSatAPI.instance.requestMandatory){
					tresholdSatisfied = true;
				}
			}
		}
		return tresholdSatisfied;
	}
	
	private void selectNProtocols(String path, String arr[], int n, int index, String data[], int i, SatSerializer sat_output){
		
		if(tresholdsSatisfied()){
			return;
		}
		
		if (index == PSatAPI.instance.protocol_combination) {
			if(PSatAPI.instance.activeProtocolFilter){
				boolean validProtocol = true;
				if(PSatAPI.instance.withoutConsent){
					for(String p:data){
						if(p.contains("Consent")){
							validProtocol = false;
							break;
						}
					}
				}
				if(PSatAPI.instance.withoutRequest){
					for(String p:data){
						if(p.contains("Request")){
							validProtocol = false;
							break;
						}
					}
				}
				if(PSatAPI.instance.withoutNotice){
					for(String p:data){
						if(p.contains("Notice")){
							validProtocol = false;
							break;
						}
					}
				}
				
				if(validProtocol){
					ProtocolPerm(path, data,0, sat_output);
				}
				return;
			}
			else{
				ProtocolPerm(path, data,0, sat_output);
				return;
			}
			
		}
	
		if (i >= n)
			return;
	
		data[index] = arr[i];
		selectNProtocols(path,arr, n,index + 1, data, i + 1, sat_output);
		selectNProtocols(path,arr, n,index, data, i + 1, sat_output);
	}	
	
	private void runNProtocols(String path, String arr[], int n, SatSerializer sat_output){
		String data[]=new String[PSatAPI.instance.protocol_combination];
				
		if(PSatAPI.instance.protocol_combination ==1){
			for(int z=0;z<arr.length;z++){
				data[0] = arr[z];
				ProtocolPerm(path, data,0, sat_output);
			}
		}
		else{
		    this.selectNProtocols(path, arr, n, 0, data, 0, sat_output);
		}		
	}
	
	public static String executedTransaction;
	public void execute(String subjectName, String senderName, String recipientName, Attribute message, String[] protocol, String sessionid){
		for(String trans:protocol){
			switch (trans) {
				case "Request":
					executedTransaction = "Request";
					new RequestTransaction(senderName,recipientName, message, sessionid);		
					break;
				case "Consent1":
					executedTransaction = "Consent1";
					new Consent1Transaction(subjectName, senderName,recipientName, message, sessionid);
					break;
				case "Consent2":
					executedTransaction = "Consent2";
					new Consent2Transaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Consent3":
					executedTransaction = "Consent3";
					new Consent3Transaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Consent4":
					executedTransaction = "Consent4";
					new Consent4Transaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Sent1":
					executedTransaction = "Sent1";
					new Sent1Transaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Sent2":
					executedTransaction = "Sent2";
					new Sent2Transaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Notice1-su":
					executedTransaction = "Notice1-su";
					new Notice1SuTransaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Notice2-su":
					executedTransaction = "Notice2-su";
					new Notice2SuTransaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Notice1-r":
					executedTransaction = "Notice1-r";
					new Notice1RTransaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				case "Notice2-r":
					executedTransaction = "Notice2-r";
					new Notice2RTransaction(subjectName, senderName,recipientName, message, sessionid);	
					break;
				default:
		             throw new IllegalArgumentException("Transaction: " + trans);
			}
		}
	}
	
	public String[] addItem(String item, String[] list){
		String temp [] = new String[list.length +1];
		for(int i=0;i<list.length;i++){
			temp[i] = list[i];
		}
		temp[list.length] = item;
		
		return temp;
	}
	
	public boolean containItem(String item, String[] list){
		boolean contained = false;
		String temp [] = new String[list.length +1];
		for(int i=0;i<list.length;i++){
			if(temp[i].equals(list[i])){
				contained = true;
				break;
			}
		}
		return contained;
	}
	
	public boolean containRequent(String protocolDesc){
		if(protocolDesc.contains("Requent")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean containConsent(String protocolDesc){
		if(protocolDesc.contains("Consent")){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean containNotice(String protocolDesc){
		if(protocolDesc.contains("Notice")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean pathSatTresholdReached(double pathSat,ConfigInstance instance){
		if(pathSat >= instance.sat_treshold){
			return true;
		}
		else{
			return false;
		}
	}
}
