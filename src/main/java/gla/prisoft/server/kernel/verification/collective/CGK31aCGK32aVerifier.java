package gla.prisoft.server.kernel.verification.collective;

import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1a;
import gla.prisoft.server.kernel.knowledge.worlds.K21a;
import gla.prisoft.server.kernel.knowledge.worlds.K31a;
import gla.prisoft.server.kernel.knowledge.worlds.K32a;
import gla.prisoft.server.kernel.knowledge.worlds.K41a;
import gla.prisoft.server.kernel.knowledge.worlds.World;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class CGK31aCGK32aVerifier {
	//Common knowledge of K31a/K32a
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, World w){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK31a/CK32a implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		
		Attribute message = null;
		Agent cg_reference = null;
		Agent cg_agent1 = null;
		
		if(w instanceof K31a){
			K31a cg = (K31a)w;
			message = cg.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent1();
		}
		else if(w instanceof K32a){
			K32a cg = (K32a)w;
			message = cg.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent2();
		}
				
		if(verifyinsubject){
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(cg_agent1.getAgentName().equals(subject.getAgentName())){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}
			
			//implication 2: K31a
			if(!cg_agent1.getAgentName().equals(subject.getAgentName()) && cg_reference.getAgentName().equals(subject.getAgentName())){
				K31a k31a = new K31a(self, cg_agent1, message);
				if(m.contains(k31a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
			}			
						
			//implication 3: K21a
			if(cg_agent1.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K21a k21a = new K21a(self, cg_reference, message);
				if(m.contains(k21a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;				
			}		
			
			//implication 4: K41a
			if(!cg_agent1.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K41a k41a = new K41a(self, cg_reference, cg_agent1, message);
				if(m.contains(k41a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}	
		}
		
		if(verifyinsender){
			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(cg_agent1.getAgentName().equals(sender.getAgentName())){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}
			
			//implication 2: K31a
			if(!cg_agent1.getAgentName().equals(sender.getAgentName()) && cg_reference.getAgentName().equals(sender.getAgentName())){
				K31a k31a = new K31a(self, cg_agent1, message);
				if(m.contains(k31a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1; 
			}			
						
			//implication 3: K21a
			if(cg_agent1.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K21a k21a = new K21a(self, cg_reference, message);
				if(m.contains(k21a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;				
			}		
			
			//implication 4: K41a
			if(!cg_agent1.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K41a k41a = new K41a(self, cg_reference, cg_agent1, message);
				if(m.contains(k41a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}
		}
		
		if(verifyinrecipient){
			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(cg_agent1.getAgentName().equals(recipient.getAgentName())){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}
			
			//implication 2: K31a
			if(!cg_agent1.getAgentName().equals(recipient.getAgentName()) && cg_reference.getAgentName().equals(recipient.getAgentName())){
				K31a k31a = new K31a(self, cg_agent1, message);
				if(m.contains(k31a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
			}			
						
			//implication 3: K21a
			if(cg_agent1.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K21a k21a = new K21a(self, cg_reference, message);
				if(m.contains(k21a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;				
			}		
			
			//implication 4: K41a
			if(!cg_agent1.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K41a k41a = new K41a(self, cg_reference, cg_agent1, message);
				if(m.contains(k41a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}	
		}
						
		int totalnoofimplications = totalnoofsubjectimplications+totalnoofsenderimplications+totalnoofrecipientimplications;
		int noofimplicationsverified = noofsubjectimplicationsverified + noofsenderimplicationsverified+noofrecipientimplicationsverified;
		
		double sat = (double)noofimplicationsverified/(double)totalnoofimplications;
		return sat;
	}

}
