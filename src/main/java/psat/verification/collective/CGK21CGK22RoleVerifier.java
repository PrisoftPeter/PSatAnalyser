package psat.verification.collective;

import psat.PSatAPI;
import psat.behaviour.InformationFlows;
import psat.knowledge.Memory;
import psat.knowledge.worlds.K1;
import psat.knowledge.worlds.K21;
import psat.knowledge.worlds.K22;
import psat.knowledge.worlds.K31;
import psat.knowledge.worlds.World;
import psat.util.Agent;
import psat.util.Attribute;
import psat.util.CollectiveStrategy;

public class CGK21CGK22RoleVerifier {
	//Common knowledge of K21/K22
	public static double verify(Agent subject, Agent sender, Agent recipient, World w){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK21/CK22 implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		
		Attribute message = null;
		Agent cg_reference = null;
		Agent cg_agent1 = null;
		
		if(w instanceof K21){
			K21 cg = (K21)w;
			message = cg.k21a.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent1();
		}
		else if(w instanceof K22){
			K22 cg = (K22)w;
			message = cg.k22a.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent2();
		}
		
		//analyse knowledge of only nodes that has been associated with information-flow
		boolean kObjectsProcessed = false;
		boolean cgagent1found = false;
		boolean cgreffound = false;
		for(String objectName:InformationFlows.processedAgents){
			if(objectName.equals(cg_reference.getAgentName())){
				cgreffound = true;
			}
			else if(objectName.equals(cg_agent1.getAgentName())){
				cgagent1found = true;
			}			
			if(cgagent1found && cgreffound){
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
			Memory m = new Memory(self, subject.getAgentName());
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1 k1 = new K1(cg_reference, message);
					if(m.contains(k1.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication 2: K31/K32
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(self.getAgentName())){
					K31 k31 = new K31(cg_agent1, cg_reference, message);
					if(m.contains(k31.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
				}
			}		
			
			//implication 3: K21/K22			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K21 k21 = new K21(self, cg_agent1, message);
					if(m.contains(k21.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k21);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}		
			}	
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){

			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName());
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1 k1 = new K1(cg_reference, message);
					if(m.contains(k1.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1);
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}
			
			//implication 2: K31/32
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(self.getAgentName())){
					K31 k31 = new K31(cg_agent1, cg_reference, message);
					if(m.contains(k31.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31);
					totalnoofsenderimplications = totalnoofsenderimplications+1; 
				}
			}
				
			//implication 3: K21		
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K21 k21 = new K21(self, cg_agent1, message);
					if(m.contains(k21.toString())){
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}
					PSatAPI.addHighOrderImplication(w, k21);
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}		
			}					
					
		}
		
		if(verifyinrecipient){

			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName());
			
			//implication 1: K1
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1 k1 = new K1(cg_reference, message);
					if(m.contains(k1.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}
			
			//implication 2: K31
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(self.getAgentName())){
					K31 k31 = new K31(cg_agent1, cg_reference, message);
					if(m.contains(k31.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
				}
			}
						
			//implication 3: K21/K22			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K21 k21 = new K21(self, cg_agent1, message);
					if(m.contains(k21.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k21);
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
