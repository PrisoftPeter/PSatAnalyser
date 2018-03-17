package psat.behaviour.processes;

import psat.PSatAPI;
import psat.knowledge.Memory;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;

public class SeekConsentProcess extends Process {
	static final String processName = "seekConsent";

	public SeekConsentProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute();
			
	}

	@Override
	protected void execute(){
		
		Agent subject = AgentFactory.getAgent(subjectName);
//		Agent sender = AgentFactory.getAgent(senderName);
//		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:su1, self = subject, agent 1=sender, agent 2 = recipient
		new Memory(subject, subjectName).substitute(new K1(subject, message), new K1a(subject, message),processName);//1
		
		for(String agentName:PSatAPI.instance.selectedAgentPath){
			if(!subject.getAgentName().equals(agentName)){
				Agent agent = AgentFactory.getAgent(agentName);
				boolean passed = new Memory(subject, subjectName).substitute(new K23(subject,agent,message), new K21(subject, agent,message),processName);//2
				if(!passed){
					new Memory(subject, subjectName).substitute(new K24(subject,agent,message), new K22(subject, agent,message),processName);//3	
				}
			}	
		}
		
//		if(!subject.getAgentName().equals(sender.getAgentName())){
//			new Memory(subject, subjectName).substitute(new K23(subject,sender,message), new K21(subject, sender,message));//2			
//		}
//		if(!subject.getAgentName().equals(recipient.getAgentName())){
//			new Memory(subject, subjectName).substitute(new K24(subject,recipient,message), new K22(subject, recipient,message));//3			
//		}
	}
}
