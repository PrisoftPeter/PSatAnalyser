package psat.behaviour.processes;

import psat.knowledge.Memory;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;

public class AckInformRProcess extends Process {
	static final String processName = "ackInform-r";

	public AckInformRProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute();
	}

	@Override
	protected void execute(){
		
		Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = AgentFactory.getAgent(senderName);
		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:s8, self = subject, agent 1=sender, agent 2 = recipient
		if(!subject.getAgentName().equals(recipient.getAgentName())){
			if(!subject.getAgentName().equals(sender.getAgentName())){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					new Memory(sender, subjectName).substitute(new K41(sender,subject,recipient, message), new K41a(sender,subject,recipient, message),processName);//1									
				}
			}
		}
	}

}
