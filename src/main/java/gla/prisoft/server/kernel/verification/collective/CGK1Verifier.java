package gla.prisoft.server.kernel.verification.collective;

import java.util.ArrayList;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1;
import gla.prisoft.server.kernel.knowledge.worlds.K21;
import gla.prisoft.server.kernel.knowledge.worlds.K31;
import gla.prisoft.server.kernel.knowledge.worlds.K41;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;

public class CGK1Verifier {
	//Common knowledge of K1
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, K1 cg,ArrayList<Agent> agentsInPath){
		
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
		
		Attribute message = cg.k1a.getAttribute();
		Agent cg_reference = cg.getSelf();
		
		if(verifyinsubject){
			if(cg_reference.getAgentName().equals(subject.getAgentName())){
				Agent self = subject;
				Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
				
				//implication 1: K1
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
					if(cg_reference.getAgentName().equals(subject.getAgentName())){
						K1 k1 = new K1(self, message);
						if(m.contains(k1.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k1);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}
				}
				
				//implication 2: K31
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
					for(Agent agent: agentsInPath){
						if(!agent.getAgentName().equals(self.getAgentName())){
							K31 k31 = new K31(agent, self, message);
							if(m.contains(k31.toString())){
								noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k31);
							totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
						}
						
					}
				}
							
				//implication 3: K21			
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
					for(Agent agent: agentsInPath){
						if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(agent.getAgentName())){
							K21 k21 = new K21(self, agent, message);
							if(m.contains(k21.toString())){
								noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k21);
							totalnoofsubjectimplications = totalnoofsubjectimplications+1;	
						}
					}		
				}
				
				//implication 5: K41/K42
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
					for(int i=0; i<agentsInPath.size();i++){
						Agent r1 = agentsInPath.get(i);
						for(int j=0; j<agentsInPath.size();j++){
							Agent r2 = agentsInPath.get(j);
							
							if(!r1.getAgentName().equals(r2.getAgentName()) && !r2.getAgentName().equals(self.getAgentName())){
								K41 k41 = new K41(r1, r2, self, message);
								if(m.contains(k41.toString())){
									noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
								}
								PSatAPI.addHighOrderImplication(cg, k41);
								totalnoofsubjectimplications = totalnoofsubjectimplications+1;
							}						
						}
					}
				}					
			
			}
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){
			if(cg_reference.getAgentName().equals(sender.getAgentName())){
				Agent self = sender;
				Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
				
				//implication 1: K1
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
					if(cg_reference.getAgentName().equals(sender.getAgentName())){
						K1 k1 = new K1(self, message);
						if(m.contains(k1.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k1);
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}
				}
				
				//implication 2: K31
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
					for(Agent agent: agentsInPath){
						if(!agent.getAgentName().equals(self.getAgentName())){
							K31 k31 = new K31(agent, self, message);
							if(m.contains(k31.toString())){
								noofsenderimplicationsverified = noofsenderimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k31);
							totalnoofsenderimplications = totalnoofsenderimplications+1; 
						}
						
					}
				}
		
				//implication 3: K21		
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
					for(Agent agent: agentsInPath){
						if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(agent.getAgentName())){
							K21 k21 = new K21(self, agent, message);
							if(m.contains(k21.toString())){
								noofsenderimplicationsverified = noofsenderimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k21);
							totalnoofsenderimplications = totalnoofsenderimplications+1;	
						}
					}		
				}
				
				//implication 5: K41/K42
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
					for(int i=0; i<agentsInPath.size();i++){
						Agent r1 = agentsInPath.get(i);
						for(int j=0; j<agentsInPath.size();j++){
							Agent r2 = agentsInPath.get(j);
							
							//
							if(!r1.getAgentName().equals(r2.getAgentName()) && !r2.getAgentName().equals(self.getAgentName())){
								K41 k41 = new K41(r1, r2, self, message);
								if(m.contains(k41.toString())){
									noofsenderimplicationsverified = noofsenderimplicationsverified+1;
								}
								PSatAPI.addHighOrderImplication(cg, k41);
								totalnoofsenderimplications = totalnoofsenderimplications+1;
							}						
						}
					}
				}	
			}				
		}
		
		if(verifyinrecipient){
			if(cg_reference.getAgentName().equals(recipient.getAgentName())){
				Agent self = recipient;
				Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
				
				//implication 1: K1
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
					if(cg_reference.getAgentName().equals(recipient.getAgentName())){
						K1 k1 = new K1(self, message);
						if(m.contains(k1.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(cg, k1);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;
					}
				}
				
				//implication 2: K31
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
					for(Agent agent: agentsInPath){
						if(!agent.getAgentName().equals(self.getAgentName())){
							K31 k31 = new K31(agent, self, message);
							if(m.contains(k31.toString())){
								noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k31);
							totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
						}						
					}
				}
							
				//implication 3: K21
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
					for(Agent agent: agentsInPath){
						if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(agent.getAgentName())){
							K21 k21 = new K21(self, agent, message);
							if(m.contains(k21.toString())){
								noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
							}
							PSatAPI.addHighOrderImplication(cg, k21);
							totalnoofrecipientimplications = totalnoofrecipientimplications+1;	
						}
					}		
				}
				
				//implication 5: K41/K42
				if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
					for(int i=0; i<agentsInPath.size();i++){
						Agent r1 = agentsInPath.get(i);
						for(int j=0; j<agentsInPath.size();j++){
							Agent r2 = agentsInPath.get(j);
							
							//
							if(!r1.getAgentName().equals(r2.getAgentName()) && !r2.getAgentName().equals(self.getAgentName())){
								K41 k41 = new K41(r1, r2, self, message);
								if(m.contains(k41.toString())){
									noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
								}
								PSatAPI.addHighOrderImplication(cg, k41);
								totalnoofrecipientimplications = totalnoofrecipientimplications+1;
							}						
						}
					}
				}
			}							
		}
				
		int totalnoofimplications = totalnoofsubjectimplications+totalnoofsenderimplications+totalnoofrecipientimplications;
		int noofimplicationsverified = noofsubjectimplicationsverified + noofsenderimplicationsverified+noofrecipientimplicationsverified;
		
		if(totalnoofimplications == 0){
			return Double.NaN;
		}
		
		double sat = (double)noofimplicationsverified/(double)totalnoofimplications;
		return sat;
	}

}
