package psat.behaviour.processes;

import psat.knowledge.Memory;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;

public class InformRProcess extends Process {
	static final String processName = "inform-r";

	public InformRProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute();
	}

	@Override
	protected void execute(){
		
		Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = AgentFactory.getAgent(senderName);
		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:r4, self = recipient, agent 1=sender, agent 2 = subject
		if(!subject.getAgentName().equals(recipient.getAgentName())){
			if(!subject.getAgentName().equals(sender.getAgentName())){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					new Memory(recipient, subjectName).substitute(new K41(recipient,subject,sender, message), new K41a(recipient,subject,sender, message),processName);//1
					new Memory(recipient, subjectName).substitute(new K42(recipient,sender,subject, message), new K42a(recipient,sender,subject, message),processName);//2
				}
			}
			new Memory(recipient, subjectName).substitute(new K22(recipient, subject, message), new K22a(recipient,subject, message),processName);//3
		}
	}

}
