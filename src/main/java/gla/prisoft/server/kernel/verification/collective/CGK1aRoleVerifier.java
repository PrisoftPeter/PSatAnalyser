package gla.prisoft.server.kernel.verification.collective;

import java.util.ArrayList;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.behaviour.InformationFlows;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1a;
import gla.prisoft.server.kernel.knowledge.worlds.K21a;
import gla.prisoft.server.kernel.knowledge.worlds.K31a;
import gla.prisoft.server.kernel.knowledge.worlds.K41a;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;

public class CGK1aRoleVerifier {
	//Common knowledge of K1a
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, K1a cg, ArrayList<Agent> agentsInPath){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK1a implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		
		Attribute message = cg.getAttribute();
		Agent cg_reference = cg.getSelf();
		
		//analyse knowledge of only nodes that has been associated with information-flow
		boolean kObjectsProcessed = false;
		for(String objectName:InformationFlows.processedAgents){
			if(objectName.equals(cg_reference.getAgentName())){
				kObjectsProcessed = true;
				break;
			}
		}
		if(!kObjectsProcessed){
			return Double.NaN;	
		}
		/////
				
		if(verifyinsubject){
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(self, message);
					if(m.contains(k1a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(cg, k1a);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication 2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				for(Agent agent: agentsInPath){
					if(!agent.getAgentName().equals(self.getAgentName())){
						K31a k31a = new K31a(agent, self, message);
						if(m.contains(k31a.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k31a);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
					}
					
				}
			}
					
			
			//implication 3: K21a			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(Agent agent: agentsInPath){
					if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(agent.getAgentName())){
						K21a k21a = new K21a(self, agent, message);
						if(m.contains(k21a.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k21a);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;	
					}
				}		
			}
			
			//implication 5: K41a/K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(int i=0; i<agentsInPath.size();i++){
					Agent r1 = agentsInPath.get(i);
					for(int j=0; j<agentsInPath.size();j++){
						Agent r2 = agentsInPath.get(j);
						
						//
						if(!r1.getAgentName().equals(r2.getAgentName()) && !r2.getAgentName().equals(self.getAgentName())){
							K41a k41a = new K41a(r1, r2, self, message);
							if(m.contains(k41a.toString())){
								noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k41a);
							totalnoofsubjectimplications = totalnoofsubjectimplications+1;
						}						
					}
				}
			}	
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){
			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(self, message);
					if(m.contains(k1a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(cg, k1a);
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}
			
			//implication 2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				for(Agent agent: agentsInPath){
					if(!agent.getAgentName().equals(self.getAgentName())){
						K31a k31a = new K31a(agent, self, message);
						if(m.contains(k31a.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k31a);
						totalnoofsenderimplications = totalnoofsenderimplications+1; 
					}
					
				}
			}
						
			//implication 3: K21a			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(Agent agent: agentsInPath){
					if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(agent.getAgentName())){
						K21a k21a = new K21a(self, agent, message);
						if(m.contains(k21a.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k21a);
						totalnoofsenderimplications = totalnoofsenderimplications+1;	
					}
				}		
			}
			
			//implication 5: K41a/K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(int i=0; i<agentsInPath.size();i++){
					Agent r1 = agentsInPath.get(i);
					for(int j=0; j<agentsInPath.size();j++){
						Agent r2 = agentsInPath.get(j);
						
						//
						if(!r1.getAgentName().equals(r2.getAgentName()) && !r2.getAgentName().equals(self.getAgentName())){
							K41a k41a = new K41a(r1, r2, self, message);
							if(m.contains(k41a.toString())){
								noofsenderimplicationsverified = noofsenderimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k41a);
							totalnoofsenderimplications = totalnoofsenderimplications+1;
						}						
					}
				}
			}
		}
		
		if(verifyinrecipient){
			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(self, message);
					if(m.contains(k1a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(cg, k1a);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}
			
			//implication 2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				for(Agent agent: agentsInPath){
					if(!agent.getAgentName().equals(self.getAgentName())){
						K31a k31a = new K31a(agent, self, message);
						if(m.contains(k31a.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k31a);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
					}
					
				}
			}
						
			//implication 3: K21a			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(Agent agent: agentsInPath){
					if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(agent.getAgentName())){
						K21a k21a = new K21a(self, agent, message);
						if(m.contains(k21a.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k21a);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;	
					}
				}		
			}
			
			//implication 5: K41a/K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(int i=0; i<agentsInPath.size();i++){
					Agent r1 = agentsInPath.get(i);
					for(int j=0; j<agentsInPath.size();j++){
						Agent r2 = agentsInPath.get(j);
						
						//
						if(!r1.getAgentName().equals(r2.getAgentName()) && !r2.getAgentName().equals(self.getAgentName())){
							K41a k41a = new K41a(r1, r2, self, message);
							if(m.contains(k41a.toString())){
								noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k41a);
							totalnoofrecipientimplications = totalnoofrecipientimplications+1;
						}						
					}
				}
			}							
		}
				
		int totalnoofimplications = totalnoofsubjectimplications+totalnoofsenderimplications+totalnoofrecipientimplications;
		int noofimplicationsverified = noofsubjectimplicationsverified + noofsenderimplicationsverified+noofrecipientimplicationsverified;
		
		if(totalnoofimplications == 0){
			PSatAPI.addHighOrderImplication(cg, null);
			return Double.NaN;
		}
		
		double sat = (double)noofimplicationsverified/(double)totalnoofimplications;
		return sat;
	}

}
