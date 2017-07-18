package gla.prisoft.server.kernel.behaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import gla.prisoft.client.Display;
import gla.prisoft.client.PSatClient;
import gla.prisoft.client.session.ClientServerBroker;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.behaviour.protocol.ServerProtocolFactory;
import gla.prisoft.server.kernel.behaviour.transactions.Consent1Transaction;
import gla.prisoft.server.kernel.behaviour.transactions.Consent2Transaction;
import gla.prisoft.server.kernel.behaviour.transactions.Consent3Transaction;
import gla.prisoft.server.kernel.behaviour.transactions.Consent4Transaction;
import gla.prisoft.server.kernel.behaviour.transactions.Notice1RTransaction;
import gla.prisoft.server.kernel.behaviour.transactions.Notice1SuTransaction;
import gla.prisoft.server.kernel.behaviour.transactions.Notice2RTransaction;
import gla.prisoft.server.kernel.behaviour.transactions.Notice2SuTransaction;
import gla.prisoft.server.kernel.behaviour.transactions.RequestTransaction;
import gla.prisoft.server.kernel.behaviour.transactions.Sent1Transaction;
import gla.prisoft.server.kernel.behaviour.transactions.Sent2Transaction;
import gla.prisoft.server.kernel.knowledge.ServerMemoryFactory;
import gla.prisoft.server.kernel.knowledge.worlds.World;
import gla.prisoft.server.kernel.util.SafeZone;
import gla.prisoft.server.kernel.util.ServerAgentFactory;
import gla.prisoft.server.kernel.util.ServerKNetworkGraph;
import gla.prisoft.server.kernel.util.ServerSatSerializer;
import gla.prisoft.server.kernel.verification.SATResult;
import gla.prisoft.server.session.Config;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.AssertionRole;
import gla.prisoft.shared.AssertionInstance;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.CombinationStrategy;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.PSatTableResult;
import gla.prisoft.shared.RowType;

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
		
	private static ArrayList<String> processedSequences;
		
	public static ArrayList<SafeZone> safeZones;
		
	public boolean sat_treshold_reached = false;
	public boolean containRequest = false;
	public boolean containConsent = false;
	public boolean containNotice = false;
				
	public boolean run(String path, ServerConfigInstance sinstance,ConfigInstance instance){
		
    	PSatAPI.fvindex = 0;
		
		if(sinstance.serverSatSerializer == null){
			sinstance.serverSatSerializer = new ServerSatSerializer();
		}
		sinstance.serverSatSerializer.resetLongSatVals();
				
		executionTimes = new ArrayList<Long>();
		doRun(path, sinstance, instance);	
		
		ClientServerBroker.messageEvent("updateProgressComponent", 100+"₦"+"", null, null);
		
		return true;
	}

	public void doRun(String path, ServerConfigInstance sinstance,ConfigInstance instance){
		String sessionid = sinstance.sessionid;	

		String evaluationProtocols [] = ServerProtocolFactory.getEvaluatedProtocols(sessionid);
		
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
		
		ClientServerBroker.messageEvent("updateProgressComponent", -1+"₦"+"", null,null);
		if(path == null){
			path = ServerKNetworkGraph.getSeq();
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
			PSatClient.netSerialiseConfigInstance();
			if(instance.isTraining){
				ClientServerBroker.messageEvent("Display.trainMaxTimeSeriesChart.addMarker()", pathId, null,null);
			}
			
			else{
				ClientServerBroker.messageEvent("Display.analysisMaxTimeSeriesChart.addMarker()", pathId, null,null);
			}
			
			if(instance.isTraining){
//				spectrum = new ViabilitySpectrum(selectedPath,pathId, pathAgents);						

				doLocalTrainingRun(path,evaluationProtocols, sinstance, instance);
				
//				ViabilitySpectrum.serializeSpectrum(spectrum, instance);
			}							
			
//			pathsIndex = pathsIndex +1;
//			instance.completness = ((double)pathsIndex/(double)selectedPaths.size())*100;
			instance.completness = 100;
			ClientServerBroker.messageEvent("updateProgressComponent", new Double(instance.completness).intValue()+"₦"+ConfigInstance.df.format(instance.completness)+"%", null,null);
			ClientServerBroker.messageEvent("info.apathcompleted", null, null,null);				

		}
		
	}
	
	
	
	private void doLocalTrainingRun(String path,String evaluationProtocols [], ServerConfigInstance sinstance,ConfigInstance instance){
		//String sessionid = sinstance.sessionid;
		message = ServerAgentFactory.getAgent(instance.subjectName, sinstance).getPersonalAttributes()[0];
		
		instance.selectedAgentPath = pathAgents;
				
		Properties ppties = new Properties();
		ppties.setProperty("instanceproperty", "selectedAgentPath");
		ClientServerBroker.messageEvent("PSatClient.ConfigInstanceUpdateRequest()", null, ppties, pathAgents);

//		ClientServerBroker.messageEvent("ClientKNetworkGraph.resetColoredLinks()", "", null,null);
//		ClientServerBroker.messageEvent("ClientKNetworkGraph.resetColoredNodes()", "", null,null);
//		ClientServerBroker.messageEvent("Display.updateNetworkNode()", "", null,null); 
				
		
        int n = evaluationProtocols.length; 
        processedSequences = new ArrayList<String>();
        runNProtocols(path, evaluationProtocols, n, sinstance.serverSatSerializer, sinstance, instance);

			
	}


	private int getMaxNoOfPossibleKnowledgeSubstutitions(String [] tempPathAgents, ServerConfigInstance sinstance,ConfigInstance instance){
		int maxSuccessfulPathSubsCount = 0;
		boolean found =false;
		
		String pp = "";
	    for(String p: pathAgents){
	    	pp = pp+ p;
	    }
		for (Map.Entry<String, Integer> entry : instance.maxPossiblePathKnowledgeSubsContainer.entrySet()) {
		    String key = entry.getKey();					    
		    if(key.equals(pp)){
		    	maxSuccessfulPathSubsCount = entry.getValue();
		    	found = true;
		    	break;
		    }
		}
		if(!found){
			String sessionid = sinstance.sessionid;

			for (Map.Entry<Integer, Integer> entry : instance.PossibleKnowledgeSubstutitions.entrySet()) {
			    Integer key = entry.getKey();
			    
			    if(key.intValue() == tempPathAgents.length){
			    	return entry.getValue();
			    }		    
			}
			
			//MaxNoOfPossibleKnowledgeSubstutitions not known
			instance.learningMaxSubs = true;

			Properties ppties = new Properties();
			ppties.setProperty("instanceproperty", "learningMaxSubs");
			ClientServerBroker.messageEvent("PSatClient.ConfigInstanceUpdateRequest()", "", ppties, instance.learningMaxSubs);
			
			ClientServerBroker.messageEvent("updateLogPage", "calculating max path knowledge transformations..."+"₦"+false, null,null);
			
			String evaluationProtocols [] = ServerProtocolFactory.getProtocolSuite(sessionid);
			
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
					
					ConfigInstance instancez = Config.deserialiseConfigInstance(sessionid);
					
					if(instancez.stop){
						break;
					}
					
					String senderName = tempPathAgents[k];
					String recipientName = tempPathAgents[k+1];
									
					message = ServerAgentFactory.getAgent(instance.subjectName, sinstance).getPersonalAttributes()[0];
					execute(instance.subjectName, senderName, recipientName, message, protocol, sessionid, sinstance, instance);

					if(successfulSubsCountLearning > maxSuccessfulPathSubsCount){
						maxSuccessfulPathSubsCount = successfulSubsCountLearning;
					}
				}	
				
				for(int k=0;k<tempPathAgents.length;k++){
					ServerMemoryFactory.restoreMemoryFromClone(tempPathAgents[k], instance.subjectName, sessionid);	
				}
				
				for(int k=0;k<tempPathAgents.length;k++){
					ServerMemoryFactory.createMemoryClone(tempPathAgents[k], instance.subjectName, sessionid);	
				}
					
			}
			
			instance.PossibleKnowledgeSubstutitions.put(tempPathAgents.length, maxSuccessfulPathSubsCount);
			
			
			instance.learningMaxSubs = false;
			Properties ppties1 = new Properties();
			ppties1.setProperty("instanceproperty", "learningMaxSubs");
			ClientServerBroker.messageEvent("PSatClient.ConfigInstanceUpdateRequest()", null, ppties1, instance.learningMaxSubs);

			instance.maxPossiblePathKnowledgeSubsContainer.put(pp, maxSuccessfulPathSubsCount);
		}
		
		

		return maxSuccessfulPathSubsCount;
	}
	
	private int localcount = 1;
	public void ProtocolPerm(String path, String[] inputs, int currentFocus, ServerSatSerializer sat_output, ServerConfigInstance sinstance, ConfigInstance instance){
		String sessionid = sinstance.sessionid;

		if(localcount > 3){			
			localcount = localcount+1;
		}
		else{
			ConfigInstance instancex = Config.deserialiseConfigInstance(sessionid);
			if(instancex !=null && instancex.stop){
				return;
			}
			
			localcount = 1;
		}
		
		
		instance.runningTraining = true;
		Config.serialiseConfigInstance(sessionid, instance);
		
		Properties ppties3 = new Properties();
		ppties3.setProperty("instanceproperty", "runningTraining");
		ClientServerBroker.messageEvent("PSatClient.ConfigInstanceUpdateRequest()", null, ppties3, instance.runningTraining);
		
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
					Agent a = ServerAgentFactory.getAgent(pathAgents[k], sinstance);
					if(!a.containedInMemoryStores(instance.subjectName)){
						ServerMemoryFactory.newMemoryStore(a.getAgentName(), sinstance,instance);
					}	
					
					//patchup
					if(!(instance.collectiveStrategy == CollectiveStrategy.NONE)){
						double cgoalv = retrieveCollectiveMeanGoal(instance, a);
						if(cgoalv>0){
							meancollectivegoalsum = meancollectivegoalsum+ cgoalv;
							meancollectivegoalcount = meancollectivegoalcount+1;
						}

					}
				}
				
				for(int k=0;k<pathAgents.length;k++){
					ServerMemoryFactory.createMemoryClone(pathAgents[k], instance.subjectName, sessionid);	
				}

				int protocolIndex = 0;

				String protocol_st = "(";
				String path_ss ="";
				for(String s:pathAgents){
					path_ss = path_ss+"-"+s;
				}
				
				int maxPossiblePathKnowledgeSubs  =0;
				if(instance.isModeEntropy){
					maxPossiblePathKnowledgeSubs = getMaxNoOfPossibleKnowledgeSubstutitions(pathAgents, sinstance,instance);	

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
					Agent a = PSatClient.netGetAgent(agentName);
					if(a ==null){
						a = PSatClient.netGetAgent(agentName); //try again
					}
					agentsInPath.add(a);
					
					if(agentName.equals(instance.subjectName)){
						su = a;
					}
				}
				
				if(instance.collectiveStrategy != CollectiveStrategy.NONE){					
					ServerMemoryFactory.extractCollectiveAssertions(instance.subjectName, pathAgents, sinstance,instance);
				}
				
				
				for(int k=0;k<pathAgents.length-1;k++){										
					
					String senderName = pathAgents[k];
					String recipientName = pathAgents[k+1];
									
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
					execute(instance.subjectName, senderName, recipientName, message, protocol, sessionid, sinstance, instance);

					sinstance.serverSatSerializer.resetRequirementDesc();
					
					
					if(instance.isModeEntropy){
						currentKnowledgeEntropy = (double)successfulSubsCount/(double)maxPossiblePathKnowledgeSubs;
						
						double collectiveDesiredEntropy = 0;
						
						double desiredEntropy_su = su.getDesiredEntropy();
						double desiredEntropy_s = s.getDesiredEntropy();
						double desiredEntropy_r = r.getDesiredEntropy();
						
						if(instance.combinationStrategy == CombinationStrategy.MINIMUM){
							collectiveDesiredEntropy = desiredEntropy_su;
							if(collectiveDesiredEntropy > desiredEntropy_s){
								collectiveDesiredEntropy = desiredEntropy_s;
							}
							if(collectiveDesiredEntropy > desiredEntropy_r){
								collectiveDesiredEntropy = desiredEntropy_r;
							}
						}
						else if(instance.combinationStrategy == CombinationStrategy.MAXIMUM){
							collectiveDesiredEntropy = desiredEntropy_su;
							if(collectiveDesiredEntropy < desiredEntropy_s){
								collectiveDesiredEntropy = desiredEntropy_s;
							}
							if(collectiveDesiredEntropy < desiredEntropy_r){
								collectiveDesiredEntropy = desiredEntropy_r;
							}
						}
						else if(instance.combinationStrategy == CombinationStrategy.AVERAGE){
							double sumDesiredEntropy = desiredEntropy_su+desiredEntropy_s+desiredEntropy_r;
							collectiveDesiredEntropy = sumDesiredEntropy/3;

						}
						
						if(instance.greaterThanOrEqualTo){
							if(currentKnowledgeEntropy >= collectiveDesiredEntropy){
								pathSat = 1;
							}
							else{
								double difference = Math.abs(collectiveDesiredEntropy - currentKnowledgeEntropy);
								pathSat = 1-difference;
							}	
						}
						else if(instance.lessThanOrEqualTo){
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
						
						sinstance.serverSatSerializer.protocolDesc = "a-"+alpha;
						sinstance.serverSatSerializer.iflow = senderName+"->"+recipientName;
						
						
						String desc = "[Desired Knowledge Entropy";
						if(instance.greaterThanOrEqualTo){
							desc = desc+"≥";
						}
						else if(instance.lessThanOrEqualTo){
							desc = desc+"≤";
						}
						desc = desc+(Math.round(collectiveDesiredEntropy * 100.0) / 100.0);
						desc = desc+" Actual Knowledge Entropy="+(Math.round(currentKnowledgeEntropy * 100.0) / 100.0)+"]";
						if(!sinstance.serverSatSerializer.requirementHtmlDesc.contains(desc)){
							if(sinstance.serverSatSerializer.requirementHtmlDesc.length() >0){
								sinstance.serverSatSerializer.requirementHtmlDesc = sinstance.serverSatSerializer.requirementHtmlDesc +" ; ";
								sinstance.serverSatSerializer.requirementRawDesc = sinstance.serverSatSerializer.requirementRawDesc + ";";
							}
							sinstance.serverSatSerializer.requirementHtmlDesc = sinstance.serverSatSerializer.requirementHtmlDesc +desc;	
							sinstance.serverSatSerializer.requirementRawDesc = sinstance.serverSatSerializer.requirementRawDesc + desc;							
						}			
						
						String alphaProtocol = "&#945;<sub>"+alpha+"</sub>=["+protocolDesc+"]";
						sinstance.serverSatSerializer.updateProtocolHtmlFullDesc(alphaProtocol);
						
						String protocol_pattern = alpha+" ("+protocolDesc+")";
						double protocol_cost = instance.protocolCost.get(protocol_pattern);
						double max_protocol_cost = ServerProtocolFactory.getMaxCost(instance);
						double normalised_cost = instance.costTradeoff*(protocol_cost/max_protocol_cost);
						double normalised_cost_no_tradeoff = protocol_cost/max_protocol_cost;
						
						double collectiveGoalValue = suggestCollectiveGoalValue( instance,  su,  s, r,instance.subjectName, senderName, recipientName); //v						
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
						
						sat_output.displaySat(path, instance.subjectName, 
											  senderName, recipientName, alphaProtocol, -10, -10, -10, pathSat, 
											  null,null, normalised_cost_no_tradeoff, collectiveGoalValue,benefit,feasibility, decision,
											  instance);						
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
						
						if(instance.collectiveStrategy == CollectiveStrategy.NONE){
							//compute satisfiability of su privacy requirements
							self_result = ServerMemoryFactory.sat(instance.subjectName, instance.subjectName, senderName, recipientName, sinstance,instance,sat_output, message);
							self_sat = Math.round(self_result.getSat() * 100.0) / 100.0;
											
							//compute satisfiability of s privacy requirements
							s_result = ServerMemoryFactory.sat(senderName, instance.subjectName,senderName, recipientName, sinstance,instance, sat_output, message);
							s_sat = Math.round(s_result.getSat() * 100.0) / 100.0;

							//compute satisfiability of r privacy requirements
							r_result = ServerMemoryFactory.sat(recipientName, instance.subjectName,senderName, recipientName, sinstance,instance, sat_output, message);
							r_sat = Math.round(r_result.getSat() * 100.0) / 100.0;	
						}
						else{
							//compute satisfiability of su privacy requirements
							self_result = ServerMemoryFactory.collectivesat(instance.subjectName, instance.subjectName, senderName, recipientName, sinstance,instance,sat_output, message,agentsInPath);
							self_sat = Math.round(self_result.getSat() * 100.0) / 100.0;
											
							//compute satisfiability of s privacy requirements
							s_result = ServerMemoryFactory.collectivesat(senderName, instance.subjectName,senderName, recipientName, sinstance,instance, sat_output, message,agentsInPath);
							s_sat = Math.round(s_result.getSat() * 100.0) / 100.0;

							//compute satisfiability of r privacy requirements
							r_result = ServerMemoryFactory.collectivesat(recipientName, instance.subjectName,senderName, recipientName, sinstance,instance, sat_output, message,agentsInPath);
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
						sinstance.serverSatSerializer.updateProtocolHtmlFullDesc(alphaProtocol);


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
						double protocol_cost = instance.protocolCost.get(protocol_pattern);
						double max_protocol_cost = ServerProtocolFactory.getMaxCost(instance);
						double normalised_cost_no_tradeoff = protocol_cost/max_protocol_cost;
						double normalised_cost = instance.costTradeoff*(protocol_cost/max_protocol_cost);
						
												
						double collectiveGoalValue = 0;
						
						if(instance.collectiveStrategy == CollectiveStrategy.NONE){
							collectiveGoalValue	= suggestCollectiveGoalValue( instance,  su,  s, r,instance.subjectName, senderName, recipientName); //v
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
																	
						sat_output.displaySat(path, instance.subjectName, 
											  senderName, recipientName, alphaProtocol, self_sat, s_sat, 
											  r_sat, pathSat, null,null, normalised_cost_no_tradeoff, collectiveGoalValue,benefit,feasibility, 
											  decision,instance);
					}


//					if(instance.log_agent_knowledge_state || instance.log_entropy_belief_uncertainty){
					if(instance.log_agent_knowledge_state){
						sinstance.serverSatSerializer.protocolDesc = "a-"+alpha;
						sinstance.serverSatSerializer.iflow = senderName+"->"+recipientName;
						
						ServerMemoryFactory.dumpMemoryStoreOnDisplay(instance.subjectName, pathSat, instance, sinstance);
						if(!senderName.equals(instance.subjectName)){
							ServerMemoryFactory.dumpMemoryStoreOnDisplay(senderName, pathSat, instance, sinstance);
						}
						if(!recipientName.equals(instance.subjectName)){
							ServerMemoryFactory.dumpMemoryStoreOnDisplay(recipientName, pathSat, instance, sinstance); 
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
							
				ClientServerBroker.messageEvent("FeasibilityView.addRow()",null,null,ptr_row);
				
							
				if(!instance.isModeEntropy){
					updateBenchmarks(sumlocalsat/countlocalsat, inputs[protocolIndex], sessionid, instance);
				}
				
				processedSequences.add(sy);

				for(int k=0;k<pathAgents.length;k++){
					ServerMemoryFactory.restoreMemoryFromClone(pathAgents[k], instance.subjectName, sessionid);	
				}	
			}
			
			if(PSatAPI.isnextpath){
				PSatAPI.logHighOrderImplications();
				PSatAPI.isnextpath = false;
			}
			
			return;
		}
		ProtocolPerm(path,inputs, currentFocus + 1, sat_output, sinstance, instance);

//		for (int i = currentFocus + 1; i < inputs.length; i++) {
//			String temp = inputs[currentFocus];
//			inputs[currentFocus] = inputs[i];
//			inputs[i] = temp;
//			ProtocolPerm(path,inputs, currentFocus + 1, sat_output, sinstance, instance);
//
//		}		
		
	}
	
	private double retrieveCollectiveMeanGoal(ConfigInstance instance, Agent agent){
		
		ArrayList<Double> vlocalgoals = new ArrayList<Double>();

		if(instance.isModePick){
			if(instance.is_role_run){
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
		else if(instance.isModeUncertainty){
			if(agent.getKnowledgeLevels().length >0){
				vlocalgoals.add(agent.getGlobalPrivacyGoal_v());
			}
		}	
		else if(instance.isModeEntropy){
			if(agent.getDesiredEntropy() >0){
				vlocalgoals.add(agent.getGlobalPrivacyGoal_v());
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
	
	private double suggestCollectiveGoalValue(ConfigInstance instance, Agent subject, Agent sender, Agent recipient, String subjectName, String senderName, String recipientName){
		if(subject == null){
			subject = PSatClient.netGetAgent(subjectName);
		}
		if(sender == null){
			sender = PSatClient.netGetAgent(senderName);
		}
		if(recipient == null){
			recipient = PSatClient.netGetAgent(recipientName);
		}
		
		ArrayList<Double> vlocalgoals = new ArrayList<Double>();
		
		if(instance.isModePick){
			if(instance.is_role_run){
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
		else if(instance.isModeUncertainty){
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
		else if(instance.isModeEntropy){
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
				Agent a = PSatClient.netGetAgent(agentName.trim());
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
							PSatClient.netWriteAgent(a);
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
						PSatClient.netWriteAgent(a);
					}
				}							
			}				
		}
		else if(instance.isModeUncertainty){
			for(Agent a:agentsInPath){
				if(a.getKnowledgeLevels().length >0){
					a.setGlobalPrivacyGoal_v(newgoalvalue);
					PSatClient.netWriteAgent(a);
				}
			}
		}	
		else if(instance.isModeEntropy){
			for(Agent a:agentsInPath){
				if(a.getDesiredEntropy() >0){
					a.setGlobalPrivacyGoal_v(newgoalvalue);
					PSatClient.netWriteAgent(a);
				}
			}
		}
		
	}
	
	public static double suggestOriginalCollectiveGoalValue(ConfigInstance instance){
		
		ArrayList<Double> vlocalgoals = new ArrayList<Double>();
		
		ArrayList<Agent> agentsInPath = new ArrayList<Agent>();
		for(String agentName:instance.selectedAgentPath){
			Agent a = PSatClient.netGetAgent(agentName);
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
	
	private void updateBenchmarks(double pathSat, String protocolDesc, String sessionid, ConfigInstance instance){

		if(pathSat >=instance.sat_treshold){
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
	
	
	private boolean tresholdsSatisfied(ConfigInstance instance){

		boolean tresholdSatisfied = false;
		
		if(!instance.unlimitedPathSatAnalysis){
			if(sat_treshold_reached){
				if(instance.consentMandatory && instance.noticeMandatory && instance.requestMandatory){
					if(containRequest && containConsent && containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(instance.consentMandatory && instance.noticeMandatory && !instance.requestMandatory){
					if(containConsent && containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(instance.consentMandatory && !instance.noticeMandatory && instance.requestMandatory){
					if(containRequest && containConsent){
						tresholdSatisfied = true;
					}
				}
				else if(!instance.consentMandatory && instance.noticeMandatory && instance.requestMandatory){
					if(containRequest && containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(instance.consentMandatory && !instance.noticeMandatory && !instance.requestMandatory){
					if(containConsent){
						tresholdSatisfied = true;
					}
				}
				else if(!instance.consentMandatory && instance.noticeMandatory && !instance.requestMandatory){
					if(containNotice){
						tresholdSatisfied = true;
					}
				}
				else if(!instance.consentMandatory && !instance.noticeMandatory && instance.requestMandatory){
					if(containRequest){
						tresholdSatisfied = true;
					}
				}
				else if(!instance.consentMandatory && !instance.noticeMandatory && !instance.requestMandatory){
					tresholdSatisfied = true;
				}
			}
		}
		return tresholdSatisfied;
	}
	
	private void selectNProtocols(String path, String arr[], int n, int index, String data[], 
								int i, ServerSatSerializer sat_output,
								ServerConfigInstance sinstance,ConfigInstance instance){
		
		if(tresholdsSatisfied(instance)){
			return;
		}
		
		if (index == instance.protocol_combination) {
			if(instance.activeProtocolFilter){
				boolean validProtocol = true;
				if(instance.withoutConsent){
					for(String p:data){
						if(p.contains("Consent")){
							validProtocol = false;
							break;
						}
					}
				}
				if(instance.withoutRequest){
					for(String p:data){
						if(p.contains("Request")){
							validProtocol = false;
							break;
						}
					}
				}
				if(instance.withoutNotice){
					for(String p:data){
						if(p.contains("Notice")){
							validProtocol = false;
							break;
						}
					}
				}
				
				if(validProtocol){
					ProtocolPerm(path, data,0, sat_output, sinstance, instance);
				}
				return;
			}
			else{
				ProtocolPerm(path, data,0, sat_output, sinstance, instance);
				return;
			}
			
		}
	
		if (i >= n)
			return;
	
		data[index] = arr[i];
		selectNProtocols(path,arr, n,index + 1, data, i + 1, sat_output, sinstance, instance);
		selectNProtocols(path,arr, n,index, data, i + 1, sat_output, sinstance, instance);
	}	
	
	private void runNProtocols(String path, String arr[], int n, ServerSatSerializer sat_output, 
							   ServerConfigInstance sinstance, ConfigInstance instance){
		String data[]=new String[instance.protocol_combination];
				
		if(instance.protocol_combination ==1){
			for(int z=0;z<arr.length;z++){
				data[0] = arr[z];
				ProtocolPerm(path, data,0, sat_output, sinstance, instance);
			}
		}
		else{
		    this.selectNProtocols(path, arr, n, 0, data, 0, sat_output, sinstance, instance);
		}		
	}
	
	public static String executedTransaction;
	public void execute(String subjectName, String senderName, String recipientName, Attribute message, String[] protocol, String sessionid, ServerConfigInstance sinstance,ConfigInstance instance){
		for(String trans:protocol){
			switch (trans) {
				case "Request":
					executedTransaction = "Request";
					new RequestTransaction(senderName,recipientName, message, sessionid, sinstance, instance);		
					break;
				case "Consent1":
					executedTransaction = "Consent1";
					new Consent1Transaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);
					break;
				case "Consent2":
					executedTransaction = "Consent2";
					new Consent2Transaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Consent3":
					executedTransaction = "Consent3";
					new Consent3Transaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Consent4":
					executedTransaction = "Consent4";
					new Consent4Transaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Sent1":
					executedTransaction = "Sent1";
					new Sent1Transaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Sent2":
					executedTransaction = "Sent2";
					new Sent2Transaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Notice1-su":
					executedTransaction = "Notice1-su";
					new Notice1SuTransaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Notice2-su":
					executedTransaction = "Notice2-su";
					new Notice2SuTransaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Notice1-r":
					executedTransaction = "Notice1-r";
					new Notice1RTransaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
					break;
				case "Notice2-r":
					executedTransaction = "Notice2-r";
					new Notice2RTransaction(subjectName, senderName,recipientName, message, sessionid, sinstance, instance);	
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
