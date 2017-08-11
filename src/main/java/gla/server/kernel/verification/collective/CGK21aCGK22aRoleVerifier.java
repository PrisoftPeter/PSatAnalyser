package gla.server.kernel.verification.collective;

import java.util.ArrayList;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;
import gla.server.PSatAPI;
import gla.server.kernel.behaviour.InformationFlows;
import gla.server.kernel.knowledge.Memory;
import gla.server.kernel.knowledge.worlds.K1a;
import gla.server.kernel.knowledge.worlds.K21a;
import gla.server.kernel.knowledge.worlds.K22a;
import gla.server.kernel.knowledge.worlds.K31a;
import gla.server.kernel.knowledge.worlds.World;
import gla.server.session.ServerConfigInstance;

public class CGK21aCGK22aRoleVerifier {
	//Common knowledge of K21a/K22a
	public static double verify(Agent subject, Agent sender, Agent recipient, ServerConfigInstance sinstance,
			ConfigInstance instance, World w,ArrayList<Agent> agentsInPath){
		
		int totalnoofsubjectimplications =0;
		int noofsubjectimplicationsverified = 0;
		int totalnoofsenderimplications =0;
		int noofsenderimplicationsverified = 0;
		int totalnoofrecipientimplications =0;
		int noofrecipientimplicationsverified = 0;
				
		//verify CK21a/CK22a implications in subject, sender and{or} recipient
		boolean verifyinsubject = true;
		boolean verifyinsender = true;
		boolean verifyinrecipient = true;
		
		Attribute message = null;
		Agent cg_reference = null;
		Agent cg_agent1 = null;
		
		if(w instanceof K21a){
			K21a cg = (K21a)w;
			message = cg.getAttribute();
			cg_reference = cg.getSelf();
			cg_agent1 = cg.getAgent1();
		}
		else if(w instanceof K22a){
			K22a cg = (K22a)w;
			message = cg.getAttribute();
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
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(cg_reference, message);
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
					K31a k31a = new K31a(cg_agent1, cg_reference, message);
					if(m.contains(k31a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31a);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1; 
				}
			}		
			
			//implication 3: K21a			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K21a k21a = new K21a(self, cg_agent1, message);
					if(m.contains(k21a.toString())){
						noofsubjectimplicationsverified = noofsubjectimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k21a);
					totalnoofsubjectimplications = totalnoofsubjectimplications+1;
				}		
			}	
		}
		
		if(verifyinsender && !subject.getAgentName().equals(sender.getAgentName())){

			Agent self = sender;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(cg_reference, message);
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
					K31a k31a = new K31a(cg_agent1, cg_reference, message);
					if(m.contains(k31a.toString())){
						noofsenderimplicationsverified = noofsenderimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31a);
					totalnoofsenderimplications = totalnoofsenderimplications+1; 
				}
			}
				
			//implication 3: K21a			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K21a k21a = new K21a(self, cg_agent1, message);
					if(m.contains(k21a.toString())){
						totalnoofsenderimplications = totalnoofsenderimplications+1;
					}
					PSatAPI.addHighOrderImplication(w, k21a);
					totalnoofsenderimplications = totalnoofsenderimplications+1;
				}		
			}					
					
		}
		
		if(verifyinrecipient){

			Agent self = recipient;
			Memory m = new Memory(self, subject.getAgentName(), sinstance, instance);
			
			//implication 1: K1a
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K1a k1a = new K1a(cg_reference, message);
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
					K31a k31a = new K31a(cg_agent1, cg_reference, message);
					if(m.contains(k31a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k31a);
					totalnoofrecipientimplications = totalnoofrecipientimplications+1; 
				}
			}
						
			//implication 3: K21a			
			if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.CG){
				if(cg_reference.getAgentName().equals(self.getAgentName())){
					K21a k21a = new K21a(self, cg_agent1, message);
					if(m.contains(k21a.toString())){
						noofrecipientimplicationsverified = noofrecipientimplicationsverified+1;
					}
					PSatAPI.addHighOrderImplication(w, k21a);
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
