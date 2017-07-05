package gla.prisoft.server.kernel.verification.collective;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1;
import gla.prisoft.server.kernel.knowledge.worlds.K31;
import gla.prisoft.server.kernel.knowledge.worlds.K41;
import gla.prisoft.server.kernel.knowledge.worlds.K41a;
import gla.prisoft.server.kernel.knowledge.worlds.K42;
import gla.prisoft.server.kernel.knowledge.worlds.World;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;

public class CGK41CGK42Verifier {
	//Common knowledge of K41/K42
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, World w){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK41/K42 implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		
		Attribute message = null;
		Agent cg_reference = null;
		Agent cg_agent1 = null;
		Agent cg_agent2 = null;
		
		if(w instanceof K41a){
			K41 cg = (K41)w;
			message = cg.k41a.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent1();
			cg_agent2 = cg.getAgent2();
		}
		else if(w instanceof K42){
			K42 cg = (K42)w;
			message = cg.k42a.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent1();
			cg_agent2 = cg.getAgent2();
		}
				
		if(verifyinsubject){

			Agent self = subject;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent2.getAgentName().equals(subject.getAgentName())){
					K1 k1 = new K1(cg_agent2, message);
					if(m.contains(k1.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication 2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(subject.getAgentName())){
					K31 k31 = new K31(cg_agent1, cg_agent2, message);
					if(m.contains(k31.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
				}
			}	
			
			//implication 5: K41/K42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(self.getAgentName().equals(cg_reference.getAgentName())){
					K41 k41 = new K41(self, cg_agent1, cg_agent2, message);
					if(m.contains(k41.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k41);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}	
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){

			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent2.getAgentName().equals(sender.getAgentName())){
					K1 k1 = new K1(cg_agent2, message);
					if(m.contains(k1.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1);
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}
			
			//implication 2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(sender.getAgentName())){
					K31 k31 = new K31(cg_agent1, cg_agent2, message);
					if(m.contains(k31.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31);
					totalnoofsenderimplications = totalnoofsenderimplications+1; 
				}
			}
				
			//implication 5: K41/K42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(self.getAgentName().equals(cg_reference.getAgentName())){
					K41 k41 = new K41(self, cg_agent1, cg_agent2, message);
					if(m.contains(k41.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k41);
					totalnoofsenderimplications = totalnoofsenderimplications+1; 
				}
			}					
					
		}
		
		if(verifyinrecipient){

			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent2.getAgentName().equals(recipient.getAgentName())){
					K1 k1 = new K1(cg_agent2, message);
					if(m.contains(k1.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}
			
			//implication 2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(recipient.getAgentName())){
					K31 k31 = new K31(cg_agent1, cg_agent2, message);
					if(m.contains(k31.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
				}
			}						
			
			//implication 5: K41/K42
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(self.getAgentName().equals(cg_reference.getAgentName())){
					K41 k41 = new K41(self, cg_agent1, cg_agent2, message);
					if(m.contains(k41.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k41);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}
		}
						
		int totalnoofimplications = totalnoofsubjectimplications+totalnoofsenderimplications+totalnoofrecipientimplications;
		int noofimplicationsverified = noofsubjectimplicationsverified + noofsenderimplicationsverified+noofrecipientimplicationsverified;
		
		if(totalnoofimplications == 0){
			PSatAPI.addHighOrderImplication(w, null);
			return Double.NaN;
		}
		
		double sat = (double)noofimplicationsverified/(double)totalnoofimplications;
		return sat;
	}

}
