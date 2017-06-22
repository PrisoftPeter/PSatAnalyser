package gla.prisoft.server.kernel.verification.collective;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1a;
import gla.prisoft.server.kernel.knowledge.worlds.K21a;
import gla.prisoft.server.kernel.knowledge.worlds.K22a;
import gla.prisoft.server.kernel.knowledge.worlds.K31a;
import gla.prisoft.server.kernel.knowledge.worlds.K32a;
import gla.prisoft.server.kernel.knowledge.worlds.K41a;
import gla.prisoft.server.kernel.knowledge.worlds.K42a;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;

public class CGK0aVerifier {
	//Common knowledge of f (every user knows that ever user beliefs f)
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, Attribute message){
		
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
		
		if(verifyinsubject){
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG || PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1a k1a = new K1a(self, message);
				if(m.contains(k1a.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;					
			}
			
			//implication2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K31a k31a = new K31a(self, sender, message);
					if(m.contains(k31a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}	
			}					
			
			//implication3: K32a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!subject.getAgentName().equals(recipient.getAgentName())){
					K32a k32a = new K32a(self, recipient, message);
					if(m.contains(k32a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}	
			}			
			
			//implication4: K21a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K21a k21a = new K21a(self, sender, message);
					if(m.contains(k21a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;				
				}
			}			
			
			//implication5: K22a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(recipient.getAgentName())){
					K22a k22a = new K22a(self, recipient, message);
					if(m.contains(k22a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}			
			
			//implication6: K41a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(sender.getAgentName()) &&!sender.getAgentName().equals(recipient.getAgentName())){
					K41a k41a = new K41a(self, sender, recipient, message);
					if(m.contains(k41a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}			
			
			//implication7: K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(recipient.getAgentName()) &&!recipient.getAgentName().equals(sender.getAgentName())){
					K42a k42a = new K42a(self, sender, recipient, message);
					if(m.contains(k42a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
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
				totalnoofsenderimplications = totalnoofsenderimplications+1;				
			}
			
			//implication2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!sender.getAgentName().equals(subject.getAgentName())){
					K31a k31a = new K31a(self, subject, message);
					if(m.contains(k31a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;	
				}
			}						
			
			//implication3: K32a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!sender.getAgentName().equals(recipient.getAgentName())){
					K32a k32a = new K32a(self, recipient, message);
					if(m.contains(k32a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}						
			
			//implication4: K21a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!sender.getAgentName().equals(subject.getAgentName())){
					K21a k21a = new K21a(self, subject, message);
					if(m.contains(k21a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}	
			}						
			
			//implication5: K22a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!sender.getAgentName().equals(recipient.getAgentName())){
					K22a k22a = new K22a(self, recipient, message);
					if(m.contains(k22a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}						
			
			//implication6: K41a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!sender.getAgentName().equals(subject.getAgentName()) && !subject.getAgentName().equals(recipient.getAgentName())){
					K41a k41a = new K41a(self, subject, recipient, message);
					if(m.contains(k41a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;	
				}
			}						
			
			//implication7: K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!sender.getAgentName().equals(recipient.getAgentName()) && !recipient.getAgentName().equals(subject.getAgentName())){
					K42a k42a = new K42a(self, subject, recipient, message);
					if(m.contains(k42a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;
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
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}			
			
			//implication2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					K31a k31a = new K31a(self, sender, message);
					if(m.contains(k31a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}			
						
			//implication3: K32a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!recipient.getAgentName().equals(subject.getAgentName())){
					K32a k32a = new K32a(self, subject, message);
					if(m.contains(k32a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}	
			}					
			
			//implication4: K21a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					K21a k21a = new K21a(self, sender, message);
					if(m.contains(k21a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}						
			
			//implication5: K22a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!recipient.getAgentName().equals(subject.getAgentName())){
					K22a k22a = new K22a(self, subject, message);
					if(m.contains(k22a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}	
			}						
			
			//implication6: K41a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!recipient.getAgentName().equals(sender.getAgentName()) && !sender.getAgentName().equals(subject.getAgentName())){
					K41a k41a = new K41a(self, sender, subject, message);
					if(m.contains(k41a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}						
			
			//implication7: K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!recipient.getAgentName().equals(subject.getAgentName()) && !subject.getAgentName().equals(sender.getAgentName())){
					K42a k42a = new K42a(self, sender, subject, message);
					if(m.contains(k42a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;	
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
