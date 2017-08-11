package gla.server.kernel.verification.collective;

import java.util.ArrayList;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;
import gla.server.PSatAPI;
import gla.server.kernel.knowledge.Memory;
import gla.server.kernel.knowledge.worlds.K0aDepreciated;
import gla.server.kernel.knowledge.worlds.K1a;
import gla.server.kernel.knowledge.worlds.K21a;
import gla.server.kernel.knowledge.worlds.K31a;
import gla.server.kernel.knowledge.worlds.K41a;
import gla.server.kernel.knowledge.worlds.K42a;
import gla.server.session.ServerConfigInstance;

public class CGK0aVerifierDepreciated {
	//Common knowledge of f (every user knows that ever user beliefs f)
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, Attribute message,ArrayList<Agent> agentsInPath){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK0a implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		
		K0aDepreciated k0a = new K0aDepreciated(message);

		if(verifyinsubject){
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG || PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				PSatAPI.addHighOrderImplication(k0a, k1a);
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;					
			}
			
			//implication2: K31a/32a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				for(Agent agent: agentsInPath){
					if(!subject.getAgentName().equals(agent.getAgentName())){
						K31a k31a = new K31a(self, agent, message);
						if(m.contains(k31a.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k31a);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}
				}			
			}					
					
			
			//implication4: K21a/22a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(Agent agent: agentsInPath){
					if(!subject.getAgentName().equals(agent.getAgentName())){
						K21a k21a = new K21a(self, agent, message);
						if(m.contains(k21a.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k21a);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}	
				}
				
											
			}		
			
			//implication6: K41a/42a 
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				for(int i=1; i<agentsInPath.size();i++){
					Agent a1 = agentsInPath.get(i-1);
					Agent a2 = agentsInPath.get(i);
					
					if(!subject.getAgentName().equals(a1.getAgentName()) &&!a1.getAgentName().equals(a2.getAgentName())){
						K41a k41a = new K41a(self, a1, a2, message);
						if(m.contains(k41a.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k41a);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}	
					
					if(!subject.getAgentName().equals(a2.getAgentName()) &&!a2.getAgentName().equals(a1.getAgentName())){
						K42a k42a = new K42a(self, a1, a2, message);
						if(m.contains(k42a.toString())){
							noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k42a);
						totalnoofsubjectimplications = totalnoofsubjectimplications+1;
					}					
				}				
			}		
		}
		
		if(verifyinsender){
			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				PSatAPI.addHighOrderImplication(k0a, k1a);
				totalnoofsenderimplications = totalnoofsenderimplications+1;				
			}
			
			//implication2: K31a/K32a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				for(Agent agent: agentsInPath){
					if(!sender.getAgentName().equals(agent.getAgentName())){
						K31a k31a = new K31a(self, agent, message);
						if(m.contains(k31a.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k31a);
						totalnoofsenderimplications = totalnoofsenderimplications+1;	
					}
				}				
			}						
								
			
			//implication4: K21a/22a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				for(Agent agent: agentsInPath){
					if(!sender.getAgentName().equals(agent.getAgentName())){
						K21a k21a = new K21a(self, agent, message);
						if(m.contains(k21a.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k21a);
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}	
				}						
			}						
									
			
			//implication6: K41a/K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				for(int i=1; i<agentsInPath.size();i++){
					Agent a1 = agentsInPath.get(i-1);
					Agent a2 = agentsInPath.get(i);
					
					if(!sender.getAgentName().equals(a1.getAgentName()) && !a1.getAgentName().equals(a2.getAgentName())){
						K41a k41a = new K41a(self, a1, a2, message);
						if(m.contains(k41a.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k41a);
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}
					
					if(!sender.getAgentName().equals(a2.getAgentName()) && !a2.getAgentName().equals(a1.getAgentName())){
						K42a k42a = new K42a(self, a1, a2, message);
						if(m.contains(k42a.toString())){
							noofsenderimplicationsverified = noofsenderimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k42a);
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}
				}				
			}							
		}
		
		if(verifyinrecipient){
			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				PSatAPI.addHighOrderImplication(k0a, k1a);
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}			
			
			//implication2: K31a/K32a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				for(Agent agent: agentsInPath){
					if(!recipient.getAgentName().equals(agent.getAgentName())){
						K31a k31a = new K31a(self, agent, message);
						if(m.contains(k31a.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k31a);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;
					}
				}			
			}			
								
			
			//implication4: K21a/22a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				
				for(Agent agent: agentsInPath){
					if(!recipient.getAgentName().equals(agent.getAgentName())){
						K21a k21a = new K21a(self, agent, message);
						if(m.contains(k21a.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k21a);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;
					}
				}								
			}					
			
			//implication6: K41a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				for(int i=1; i<agentsInPath.size();i++){
					Agent a1 = agentsInPath.get(i-1);
					Agent a2 = agentsInPath.get(i);
					
					if(!recipient.getAgentName().equals(a1.getAgentName()) && !a1.getAgentName().equals(a2.getAgentName())){
						K41a k41a = new K41a(self, a1, a2, message);
						if(m.contains(k41a.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k41a);
						totalnoofrecipientimplications = totalnoofrecipientimplications+1;
					}
					
					if(!recipient.getAgentName().equals(a2.getAgentName()) && !a2.getAgentName().equals(a1.getAgentName())){
						K42a k42a = new K42a(self, a1, a2, message);
						if(m.contains(k42a.toString())){
							noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
						}
						PSatAPI.addHighOrderImplication(k0a, k42a);
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
