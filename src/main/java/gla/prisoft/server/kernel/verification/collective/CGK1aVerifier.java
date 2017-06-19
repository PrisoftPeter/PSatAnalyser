package gla.prisoft.server.kernel.verification.collective;

import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1a;
import gla.prisoft.server.kernel.knowledge.worlds.K21a;
import gla.prisoft.server.kernel.knowledge.worlds.K22a;
import gla.prisoft.server.kernel.knowledge.worlds.K31a;
import gla.prisoft.server.kernel.knowledge.worlds.K41a;
import gla.prisoft.server.kernel.knowledge.worlds.K42a;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class CGK1aVerifier {
	//Common knowledge of K1a
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, K1a cg){
		
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
		
		if(verifyinsubject){
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(cg_reference.getAgentName().equals(subject.getAgentName())){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}
			
			//implication 2: K31a
			if(!cg_reference.getAgentName().equals(subject.getAgentName())){
				K31a k31a = new K31a(self, cg_reference, message);
				if(m.contains(k31a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
			}			
						
			//implication 3: K21a
			if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K21a k21a = new K21a(self, sender, message);
				if(m.contains(k21a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;				
			}
			
			//implication 4: K22a
			if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K22a k22a = new K22a(self, recipient, message);
				if(m.contains(k22a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}
			
			//implication 5: K41a (sender)
			if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K41a k41a = new K41a(self, sender, cg_reference, message);
				if(m.contains(k41a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}			
			
			//implication 6: K42a (sender)
			if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K42a k42a = new K42a(self, sender, cg_reference, message);
				if(m.contains(k42a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;	
			}
			
			//implication 7: K41a (recipient)
			if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K41a k41a = new K41a(self, recipient, cg_reference, message);
				if(m.contains(k41a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}			
			
			//implication 8: K42a (recipient)
			if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K42a k42a = new K42a(self, recipient, cg_reference, message);
				if(m.contains(k42a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;	
			}			
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){
			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(cg_reference.getAgentName().equals(sender.getAgentName())){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}
			
			//implication 2: K31a
			if(!cg_reference.getAgentName().equals(sender.getAgentName())){
				K31a k31a = new K31a(self, cg_reference, message);
				if(m.contains(k31a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1; 
			}			
						
			//implication 3: K21a
			if(cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K21a k21a = new K21a(self, subject, message);
				if(m.contains(k21a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;				
			}
			
			//implication 4: K22a
			if(cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K22a k22a = new K22a(self, recipient, message);
				if(m.contains(k22a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}
			
			//implication 5: K41a (subject)
			if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K41a k41a = new K41a(self, subject, cg_reference, message);
				if(m.contains(k41a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}			
			
			//implication 6: K42a (subject)
			if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K42a k42a = new K42a(self, subject, cg_reference, message);
				if(m.contains(k42a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;	
			}
			
			//implication 7: K41a (recipient)
			if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K41a k41a = new K41a(self, recipient, cg_reference, message);
				if(m.contains(k41a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}			
			
			//implication 8: K42a (recipient)
			if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
				K42a k42a = new K42a(self, recipient, cg_reference, message);
				if(m.contains(k42a.toString())){
					noofsenderimplicationsverified = noofsenderimplicationsverified+1;
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;	
			}			
		}
		
		if(verifyinrecipient){
			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(cg_reference.getAgentName().equals(recipient.getAgentName())){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}
			
			//implication 2: K31a
			if(!cg_reference.getAgentName().equals(recipient.getAgentName())){
				K31a k31a = new K31a(self, cg_reference, message);
				if(m.contains(k31a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
			}			
						
			//implication 3: K21a
			if(cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K21a k21a = new K21a(self, sender, message);
				if(m.contains(k21a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;				
			}
			
			//implication 4: K22a
			if(cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K22a k22a = new K22a(self, subject, message);
				if(m.contains(k22a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}
			
			//implication 5: K41a (sender)
			if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K41a k41a = new K41a(self, sender, cg_reference, message);
				if(m.contains(k41a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}			
			
			//implication 6: K42a (sender)
			if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
				K42a k42a = new K42a(self, sender, cg_reference, message);
				if(m.contains(k42a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;	
			}
			
			//implication 7: K41a (subject)
			if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K41a k41a = new K41a(self, subject, cg_reference, message);
				if(m.contains(k41a.toString())){
					noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}			
			
			//implication 8: K42a (subject)
			if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
				K42a k42a = new K42a(self, subject, cg_reference, message);
				if(m.contains(k42a.toString())){
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
