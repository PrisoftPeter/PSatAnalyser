package gla.prisoft.server.kernel.verification.collective;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.behaviour.InformationFlows;
import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.K1a;
import gla.prisoft.server.kernel.knowledge.worlds.K31a;
import gla.prisoft.server.kernel.knowledge.worlds.K41a;
import gla.prisoft.server.kernel.knowledge.worlds.K42a;
import gla.prisoft.server.kernel.knowledge.worlds.World;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;

public class CGK41aCGK42aRoleVerifier {
	//Common knowledge of K41a/K42a
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, World w){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK41a/CK42a implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		
		Attribute message = null;
		Agent cg_reference = null;
		Agent cg_agent1 = null;
		Agent cg_agent2 = null;
		
		if(w instanceof K41a){
			K41a cg = (K41a)w;
			message = cg.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent1();
			cg_agent2 = cg.getAgent2();
		}
		else if(w instanceof K42a){
			K42a cg = (K42a)w;
			message = cg.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent1();
			cg_agent2 = cg.getAgent2();
		}
		
		//analyse knowledge of only nodes that has been associated with information-flow
		boolean kObjectsProcessed = false;
		boolean cgagent1found = false;
		boolean cgagent2found = false;
		boolean cgreffound = false;
		for(String objectName:InformationFlows.processedAgents){
			
			if(objectName.equals(cg_reference.getAgentName())){
				cgreffound = true;
			}
			else if(objectName.equals(cg_agent1.getAgentName())){
				cgagent1found = true;
			}	
			else if(objectName.equals(cg_agent2.getAgentName())){
				cgagent2found = true;
			}
			if(cgagent2found && cgagent1found && cgreffound){
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
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent2.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(cg_agent2, message);
					if(m.contains(k1a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1a);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}
			
			//implication 2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(self.getAgentName())){
					K31a k31a = new K31a(cg_agent1, cg_agent2, message);
					if(m.contains(k31a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31a);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
				}
			}	
			
			//implication 5: K41a/K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(self.getAgentName().equals(cg_reference.getAgentName())){
					K41a k41a = new K41a(self, cg_agent1, cg_agent2, message);
					if(m.contains(k41a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k41a);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}
			}	
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){

			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent2.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(cg_agent2, message);
					if(m.contains(k1a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1a);
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}
			}
			
			//implication 2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(self.getAgentName())){
					K31a k31a = new K31a(cg_agent1, cg_agent2, message);
					if(m.contains(k31a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31a);
					totalnoofsenderimplications = totalnoofsenderimplications+1; 
				}
			}
				
			//implication 5: K41a/K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(self.getAgentName().equals(cg_reference.getAgentName())){
					K41a k41a = new K41a(self, cg_agent1, cg_agent2, message);
					if(m.contains(k41a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k41a);
					totalnoofsenderimplications = totalnoofsenderimplications+1; 
				}
			}					
					
		}
		
		if(verifyinrecipient){

			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent2.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(cg_agent2, message);
					if(m.contains(k1a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k1a);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1;
				}
			}
			
			//implication 2: K31a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_agent1.getAgentName().equals(self.getAgentName())){
					K31a k31a = new K31a(cg_agent1, cg_agent2, message);
					if(m.contains(k31a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31a);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
				}
			}						
			
			//implication 5: K41a/K42a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(self.getAgentName().equals(cg_reference.getAgentName())){
					K41a k41a = new K41a(self, cg_agent1, cg_agent2, message);
					if(m.contains(k41a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k41a);
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
