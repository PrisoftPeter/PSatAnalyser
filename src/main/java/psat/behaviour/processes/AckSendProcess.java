package psat.behaviour.processes;

import psat.knowledge.Memory;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;


public class AckSendProcess extends Process {
	static final String processName = "ackSend";

	public AckSendProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute();		
	}

	@Override
	protected void execute(){
		//Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = AgentFactory.getAgent(senderName);
		Agent recipient = AgentFactory.getAgent(recipientName);
		

		if(!sender.getAgentName().equals(recipient.getAgentName())){
		//State:s6 -> self = sender, agent 1=subject agent 2 =recipient			
			new Memory(sender, subjectName).substitute(new K31(sender, recipient, message), new K31a(sender, recipient, message),processName);//1		
			new Memory(sender, subjectName).substitute(new K22(sender,recipient,message), new K22a(sender, recipient,message),processName);//2	

		//State:r3 -> self = recipient, agent1 = sender, agent2 = subject
			new Memory(recipient, subjectName).substitute(new K21(recipient, sender, message), new K21a(recipient, sender, message),processName);//1
		}
	}

}
