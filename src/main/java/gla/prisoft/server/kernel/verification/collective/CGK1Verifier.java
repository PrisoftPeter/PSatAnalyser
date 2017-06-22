package gla.prisoft.server.kernel.verification.collective;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1;
import gla.prisoft.server.kernel.knowledge.worlds.K21;
import gla.prisoft.server.kernel.knowledge.worlds.K22;
import gla.prisoft.server.kernel.knowledge.worlds.K31;
import gla.prisoft.server.kernel.knowledge.worlds.K41;
import gla.prisoft.server.kernel.knowledge.worlds.K42;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;

public class CGK1Verifier {
	//Common knowledge of K1
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, K1 cg){
		
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
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				if(cg_reference.getAgentName().equals(subject.getAgentName())){
					K1 k1 = new K1(self, message);
					if(m.contains(k1.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication 2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!cg_reference.getAgentName().equals(subject.getAgentName())){
					K31 k31 = new K31(self, cg_reference, message);
					if(m.contains(k31.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
				}	
			}	
						
			//implication 3: K21
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
					K21 k21 = new K21(self, sender, message);
					if(m.contains(k21.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;				
			}
			
			//implication 4: K22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
					K22 k22 = new K22(self, recipient, message);
					if(m.contains(k22.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}
			
			//implication 5: K41 (sender)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
					K41 k41 = new K41(self, sender, cg_reference, message);
					if(m.contains(k41.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}
				
			//implication 6: K42 (sender)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
					K42 k42 = new K42(self, sender, cg_reference, message);
					if(m.contains(k42.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;	
			}
			
			//implication 7: K41 (recipient)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
					K41 k41 = new K41(self, recipient, cg_reference, message);
					if(m.contains(k41.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
				}	
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}
				
			//implication 8: K42 (recipient)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(subject.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
					K42 k42 = new K42(self, recipient, cg_reference, message);
					if(m.contains(k42.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;	
			}			
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){
			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				if(cg_reference.getAgentName().equals(sender.getAgentName())){
					K1 k1 = new K1(self, message);
					if(m.contains(k1.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}
			
			//implication 2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!cg_reference.getAgentName().equals(sender.getAgentName())){
					K31 k31 = new K31(self, cg_reference, message);
					if(m.contains(k31.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1; 
			}
						
			//implication 3: K21
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
					K21 k21 = new K21(self, subject, message);
					if(m.contains(k21.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;				
			}
			
			//implication 4: K22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
					K22 k22 = new K22(self, recipient, message);
					if(m.contains(k22.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}
			
			//implication 5: K41 (subject)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
					K41 k41 = new K41(self, subject, cg_reference, message);
					if(m.contains(k41.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
				}	
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}
			
			//implication 6: K42 (subject)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
					K42 k42 = new K42(self, subject, cg_reference, message);
					if(m.contains(k42.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
				}	
				totalnoofsenderimplications = totalnoofsenderimplications+1;	
			}
			
			//implication 7: K41 (recipient)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
					K41 k41 = new K41(self, recipient, cg_reference, message);
					if(m.contains(k41.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
				}	
				totalnoofsenderimplications = totalnoofsenderimplications+1;
			}
			
			//implication 8: K42 (recipient)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(sender.getAgentName()) && !cg_reference.getAgentName().equals(recipient.getAgentName())){
					K42 k42 = new K42(self, recipient, cg_reference, message);
					if(m.contains(k42.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
				}
				totalnoofsenderimplications = totalnoofsenderimplications+1;	
			}		
		}
		
		if(verifyinrecipient){
			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				if(cg_reference.getAgentName().equals(recipient.getAgentName())){
					K1 k1 = new K1(self, message);
					if(m.contains(k1.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}
			
			//implication 2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!cg_reference.getAgentName().equals(recipient.getAgentName())){
					K31 k31 = new K31(self, cg_reference, message);
					if(m.contains(k31.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
			}
						
			//implication 3: K21
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
					K21 k21 = new K21(self, sender, message);
					if(m.contains(k21.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;				
			}
			
			//implication 4: K22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
					K22 k22 = new K22(self, subject, message);
					if(m.contains(k22.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}
			
			//implication 5: K41 (sender)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
					K41 k41 = new K41(self, sender, cg_reference, message);
					if(m.contains(k41.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}
			
			//implication 6: K42 (sender)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(sender.getAgentName())){
					K42 k42 = new K42(self, sender, cg_reference, message);
					if(m.contains(k42.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
				}
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;	
			}
			
			//implication 7: K41 (subject)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
					K41 k41 = new K41(self, subject, cg_reference, message);
					if(m.contains(k41.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
				}	
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;
			}
			
			//implication 8: K42 (subject)
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!cg_reference.getAgentName().equals(recipient.getAgentName()) && !cg_reference.getAgentName().equals(subject.getAgentName())){
					K42 k42 = new K42(self, subject, cg_reference, message);
					if(m.contains(k42.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
				}	
				totalnoofrecipientimplications = totalnoofrecipientimplications+1;	
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
