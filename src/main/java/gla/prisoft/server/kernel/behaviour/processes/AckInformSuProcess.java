package gla.prisoft.server.kernel.behaviour.processes;

import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.*;
import gla.prisoft.server.kernel.util.ServerAgentFactory;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class AckInformSuProcess extends Process {
	static final String processName = "ackInform-su";

	public AckInformSuProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute(sinstance, instance);
	}

	@Override
	protected void execute(ServerConfigInstance sinstance,ConfigInstance instance){
		
		Agent subject = ServerAgentFactory.getAgent(subjectName,sinstance);
		Agent sender = ServerAgentFactory.getAgent(senderName,sinstance);
		Agent recipient = ServerAgentFactory.getAgent(recipientName,sinstance);
		
		//State:s8, self = subject, agent 1=sender, agent 2 = recipient
		if(!subject.getAgentName().equals(recipient.getAgentName())){
			if(!subject.getAgentName().equals(sender.getAgentName())){
				if(!recipient.getAgentName().equals(sender.getAgentName())){
					new Memory(sender, subjectName, sinstance,instance).substitute(new K42(sender,recipient,subject, message), new K42a(sender,recipient,subject, message),processName, instance);//1									
				}
			}
		}
	}

}
