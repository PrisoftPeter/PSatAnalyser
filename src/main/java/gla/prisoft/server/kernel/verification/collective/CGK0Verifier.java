package gla.prisoft.server.kernel.verification.collective;

import java.util.ArrayList;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K0;
import gla.prisoft.server.kernel.knowledge.worlds.K1;
import gla.prisoft.server.kernel.knowledge.worlds.K21;
import gla.prisoft.server.kernel.knowledge.worlds.K31;
import gla.prisoft.server.kernel.knowledge.worlds.K41;
import gla.prisoft.server.kernel.knowledge.worlds.K42;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;

public class CGK0Verifier {
	//Common knowledge of f uncertainty (every user knows that ever user is uncertain of f)
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, Attribute message,ArrayList<Agent> agentsInPath){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK0 implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		K0 k0 = new K0(message);
		if(verifyinsubject){
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG || PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1 k1 = new K1(self, message);
				if(m.contains(k1.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				PSatAPI.addHighOrderImplication(k0, k1);
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;					
			}
			
			//implication2: K31/32
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				for(Agent agent: agentsInPath){
					if(!subject.getAgentName().equals(agent.getAgentName())){
						K31 k31 = new K31(self, agent, message);
						if(m.contains(k31.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k31);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}
				}			
			}					
					
			
			//implication4: K21/22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(Agent agent: agentsInPath){
					if(!subject.getAgentName().equals(agent.getAgentName())){
						K21 k21 = new K21(self, agent, message);
						if(m.contains(k21.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k21);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}	
				}
				
											
			}		
			
			//implication6: K41/42 
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(int i=1; i<agentsInPath.size();i++){
					Agent a1 = agentsInPath.get(i-1);
					Agent a2 = agentsInPath.get(i);
					
					if(!subject.getAgentName().equals(a1.getAgentName()) &&!a1.getAgentName().equals(a2.getAgentName())){
						K41 k41 = new K41(self, a1, a2, message);
						if(m.contains(k41.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k41);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}	
					
					if(!subject.getAgentName().equals(a2.getAgentName()) &&!a2.getAgentName().equals(a1.getAgentName())){
						K42 k42 = new K42(self, a1, a2, message);
						if(m.contains(k42.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k42);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}					
				}				
			}		
		}
		
		if(verifyinsender){
			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1 k1 = new K1(self, message);
				if(m.contains(k1.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				PSatAPI.addHighOrderImplication(k0, k1);
				totalnoofsenderimplications = totalnoofsenderimplications+1;				
			}
			
			//implication2: K31/K32
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				for(Agent agent: agentsInPath){
					if(!sender.getAgentName().equals(agent.getAgentName())){
						K31 k31 = new K31(self, agent, message);
						if(m.contains(k31.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k31);
						totalnoofsenderimplications = totalnoofsenderimplications+1;	
					}
				}				
			}						
								
			
			//implication4: K21/22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				for(Agent agent: agentsInPath){
					if(!sender.getAgentName().equals(agent.getAgentName())){
						K21 k21 = new K21(self, agent, message);
						if(m.contains(k21.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k21);
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}	
				}						
			}						
									
			
			//implication6: K41/K42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				for(int i=1; i<agentsInPath.size();i++){
					Agent a1 = agentsInPath.get(i-1);
					Agent a2 = agentsInPath.get(i);
					
					if(!sender.getAgentName().equals(a1.getAgentName()) && !a1.getAgentName().equals(a2.getAgentName())){
						K41 k41 = new K41(self, a1, a2, message);
						if(m.contains(k41.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k41);
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}
					
					if(!sender.getAgentName().equals(a2.getAgentName()) && !a2.getAgentName().equals(a1.getAgentName())){
						K42 k42 = new K42(self, a1, a2, message);
						if(m.contains(k42.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k42);
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}
				}				
			}							
		}
		
		if(verifyinrecipient){
			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1 k1 = new K1(self, message);
				if(m.contains(k1.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				PSatAPI.addHighOrderImplication(k0, k1);
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}			
			
			//implication2: K31/K32
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				for(Agent agent: agentsInPath){
					if(!recipient.getAgentName().equals(agent.getAgentName())){
						K31 k31 = new K31(self, agent, message);
						if(m.contains(k31.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k31);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;
					}
				}			
			}			
								
			
			//implication4: K21/22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				
				for(Agent agent: agentsInPath){
					if(!recipient.getAgentName().equals(agent.getAgentName())){
						K21 k21 = new K21(self, agent, message);
						if(m.contains(k21.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k21);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;
					}
				}								
			}					
			
			//implication6: K41/42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				for(int i=1; i<agentsInPath.size();i++){
					Agent a1 = agentsInPath.get(i-1);
					Agent a2 = agentsInPath.get(i);
					
					if(!recipient.getAgentName().equals(a1.getAgentName()) && !a1.getAgentName().equals(a2.getAgentName())){
						K41 k41 = new K41(self, a1, a2, message);
						if(m.contains(k41.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k41);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;
					}
					
					if(!recipient.getAgentName().equals(a2.getAgentName()) && !a2.getAgentName().equals(a1.getAgentName())){
						K42 k42 = new K42(self, a1, a2, message);
						if(m.contains(k42.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0, k42);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;	
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
