package gla.server.kernel.behaviour.processes;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.knowledge.Memory;
import gla.server.kernel.knowledge.worlds.*;
import gla.server.kernel.util.ServerAgentFactory;
import gla.server.session.ServerConfigInstance;

public class AckGrantConsentProcess extends Process {
	static final String processName = "ackGrantConsent";

	public AckGrantConsentProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute(sinstance, instance);
	}

	@Override
	protected void execute(ServerConfigInstance sinstance,ConfigInstance instance){
		Agent subject = ServerAgentFactory.getAgent(subjectName,sinstance);
		Agent sender = ServerAgentFactory.getAgent(senderName,sinstance);
//		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:su3 -> self = subject, agent1 =sender agent2 = recipient
		if(subject.getAgentName().equals(sender.getAgentName())){
//			new Memory(subject, subjectName).substitute(new K31(subject, sender, message), new K31a(subject,sender, message),processName);//1
//			new Memory(subject, subjectName).substitute(new K21(subject, sender, message), new K21a(subject,sender, message),processName);//2
			
//		//State:s4 -> self = recipient, agent1 = recipient, agent2 = subject
//			new Memory(recipient, subjectName).substitute(new K21(recipient, subject, message), new K21a(recipient,subject, message),processName);//1
		}
		else{
			new Memory(subject, subjectName, sinstance,instance).substitute(new K31(subject, sender, message), new K31a(subject,sender, message),processName, instance);//1
			new Memory(subject, subjectName, sinstance,instance).substitute(new K21(subject, sender, message), new K21a(subject,sender, message),processName, instance);//2
			
		//State:s4 -> self = sender, agent1 = recipient, agent2 = subject
			new Memory(sender, subjectName, sinstance,instance).substitute(new K21(sender, subject, message), new K21a(sender,subject, message),processName, instance);//1
		}
		
	}

}
