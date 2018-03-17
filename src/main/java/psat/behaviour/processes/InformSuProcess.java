package psat.behaviour.processes;

import psat.knowledge.Memory;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;

public class InformSuProcess extends Process {
	static final String processName = "inform-su";

	public InformSuProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute();
		
	}

	@Override
	protected void execute(){
		Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = AgentFactory.getAgent(senderName);
		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:su4, self = subject, agent 1=sender, agent 2 = recipient
		if(!subject.getAgentName().equals(recipient.getAgentName())){
			if(!subject.getAgentName().equals(sender.getAgentName())){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					new Memory(subject, subjectName).substitute(new K41(subject,sender,recipient, message), new K41a(subject,sender,recipient, message),processName);//1
					new Memory(subject, subjectName).substitute(new K42(subject,recipient,sender, message), new K42a(subject,recipient,sender, message),processName);//2
				}				
			}		
			new Memory(subject, subjectName).substitute(new K22(subject, recipient, message), new K22a(subject,recipient, message),processName);//3			
		}
	}
}
