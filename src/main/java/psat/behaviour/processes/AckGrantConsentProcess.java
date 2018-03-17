package psat.behaviour.processes;

import psat.knowledge.Memory;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;

public class AckGrantConsentProcess extends Process {
	static final String processName = "ackGrantConsent";

	public AckGrantConsentProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute();
	}

	@Override
	protected void execute(){
		Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = AgentFactory.getAgent(senderName);
//		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:su3 -> self = subject, agent1 =sender agent2 = recipient
		if(subject.getAgentName().equals(sender.getAgentName())){
//			new Memory(subject, subjectName).substitute(new K31(subject, sender, message), new K31a(subject,sender, message),processName);//1
//			new Memory(subject, subjectName).substitute(new K21(subject, sender, message), new K21a(subject,sender, message),processName);//2
			
//		//State:s4 -> self = recipient, agent1 = recipient, agent2 = subject
//			new Memory(recipient, subjectName).substitute(new K21(recipient, subject, message), new K21a(recipient,subject, message),processName);//1
		}
		else{
			new Memory(subject, subjectName).substitute(new K31(subject, sender, message), new K31a(subject,sender, message),processName);//1
			new Memory(subject, subjectName).substitute(new K21(subject, sender, message), new K21a(subject,sender, message),processName);//2
			
		//State:s4 -> self = sender, agent1 = recipient, agent2 = subject
			new Memory(sender, subjectName).substitute(new K21(sender, subject, message), new K21a(sender,subject, message),processName);//1
		}
		
	}

}
