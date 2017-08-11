package gla.server.kernel.behaviour.processes;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.knowledge.Memory;
import gla.server.kernel.knowledge.worlds.*;
import gla.server.kernel.util.ServerAgentFactory;
import gla.server.session.ServerConfigInstance;

public class InformSuProcess extends Process {
	static final String processName = "inform-su";

	public InformSuProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute(sinstance,instance);
		
	}

	@Override
	protected void execute(ServerConfigInstance sinstance,ConfigInstance instance){
		Agent subject = ServerAgentFactory.getAgent(subjectName,sinstance);
		Agent sender = ServerAgentFactory.getAgent(senderName,sinstance);
		Agent recipient = ServerAgentFactory.getAgent(recipientName,sinstance);
		
		//State:su4, self = subject, agent 1=sender, agent 2 = recipient
		if(!subject.getAgentName().equals(recipient.getAgentName())){
			if(!subject.getAgentName().equals(sender.getAgentName())){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					new Memory(subject, subjectName, sinstance, instance).substitute(new K41(subject,sender,recipient, message), new K41a(subject,sender,recipient, message),processName, instance);//1
					new Memory(subject, subjectName, sinstance, instance).substitute(new K42(subject,recipient,sender, message), new K42a(subject,recipient,sender, message),processName, instance);//2
				}				
			}		
			new Memory(subject, subjectName, sinstance, instance).substitute(new K22(subject, recipient, message), new K22a(subject,recipient, message),processName, instance);//3			
		}
	}
}
