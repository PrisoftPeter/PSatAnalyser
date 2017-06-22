package gla.prisoft.server.kernel.verification.collective;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1;
import gla.prisoft.server.kernel.knowledge.worlds.K1a;
import gla.prisoft.server.kernel.knowledge.worlds.K21;
import gla.prisoft.server.kernel.knowledge.worlds.K22;
import gla.prisoft.server.kernel.knowledge.worlds.K31;
import gla.prisoft.server.kernel.knowledge.worlds.K31a;
import gla.prisoft.server.kernel.knowledge.worlds.K32;
import gla.prisoft.server.kernel.knowledge.worlds.K32a;
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
			ConfigInstance instance, Attribute message){
		
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
		
		if(verifyinsubject){
			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
				K1 k1 = new K1(self, message);
				if(m.contains(k1.toString())){
					noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
				}
				totalnoofsubjectimplications = totalnoofsubjectimplications+1;
			}
			
			//implication2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K31 k31 = new K31(self, sender, message);
					if(m.contains(k31.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication3: K32
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEG){
				if(!subject.getAgentName().equals(recipient.getAgentName())){
					K32 k32 = new K32(self, recipient, message);
					if(m.contains(k32.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication4: K21
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K21 k21 = new K21(self, sender, message);
					if(m.contains(k21.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;				
				}
			}
			
			//implication5: K22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(recipient.getAgentName())){
					K22 k22 = new K22(self, recipient, message);
					if(m.contains(k22.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication6: K41
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(sender.getAgentName()) &&!sender.getAgentName().equals(recipient.getAgentName())){
					K41 k41 = new K41(self, sender, recipient, message);
					if(m.contains(k41.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication7: K42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(!subject.getAgentName().equals(recipient.getAgentName()) &&!recipient.getAgentName().equals(sender.getAgentName())){
					K42 k42 = new K42(self, sender, recipient, message);
					if(m.contains(k42.toString())){
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
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
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
			
			//implication4: K21
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!sender.getAgentName().equals(subject.getAgentName())){
					K21 k21 = new K21(self, subject, message);
					if(m.contains(k21.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}	
			}
			
			//implication5: K22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!sender.getAgentName().equals(recipient.getAgentName())){
					K22 k22 = new K22(self, recipient, message);
					if(m.contains(k22.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}		
			
			//implication6: K41
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!sender.getAgentName().equals(subject.getAgentName()) && !subject.getAgentName().equals(recipient.getAgentName())){
					K41 k41 = new K41(self, subject, recipient, message);
					if(m.contains(k41.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					totalnoofsenderimplications = totalnoofsenderimplications+1;	
				}	
			}
			
			//implication7: K42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!sender.getAgentName().equals(recipient.getAgentName()) && !recipient.getAgentName().equals(subject.getAgentName())){
					K42 k42 = new K42(self, subject, recipient, message);
					if(m.contains(k42.toString())){
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
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EG){
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
			
			//implication4: K21
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					K21 k21 = new K21(self, sender, message);
					if(m.contains(k21.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}	
			}
			
			//implication5: K22
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!recipient.getAgentName().equals(subject.getAgentName())){
					K22 k22 = new K22(self, subject, message);
					if(m.contains(k22.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}	
			}
			
			//implication6: K41
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!recipient.getAgentName().equals(sender.getAgentName()) && !sender.getAgentName().equals(subject.getAgentName())){
					K41 k41 = new K41(self, sender, subject, message);
					if(m.contains(k41.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}	
			}				
			
			//implication7: K42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG ||PSatAPI.instance.collectiveStrategy == CollectiveStrategy.EEEG){
				if(!recipient.getAgentName().equals(subject.getAgentName()) && !subject.getAgentName().equals(sender.getAgentName())){
					K42 k42 = new K42(self, sender, subject, message);
					if(m.contains(k42.toString())){
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
