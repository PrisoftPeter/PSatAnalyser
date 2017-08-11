package gla.server.kernel.behaviour.processes;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.knowledge.Memory;
import gla.server.kernel.knowledge.worlds.*;
import gla.server.kernel.util.ServerAgentFactory;
import gla.server.session.ServerConfigInstance;

public class AckSendProcess extends Process {
	static final String processName = "ackSend";

	public AckSendProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute(sinstance,instance);		
	}

	@Override
	protected void execute(ServerConfigInstance sinstance,ConfigInstance instance){
		//Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = ServerAgentFactory.getAgent(senderName,sinstance);
		Agent recipient = ServerAgentFactory.getAgent(recipientName,sinstance);
		

		if(!sender.getAgentName().equals(recipient.getAgentName())){
		//State:s6 -> self = sender, agent 1=subject agent 2 =recipient			
			new Memory(sender, subjectName, sinstance,instance).substitute(new K31(sender, recipient, message), new K31a(sender, recipient, message),processName, instance);//1		
			new Memory(sender, subjectName, sinstance,instance).substitute(new K22(sender,recipient,message), new K22a(sender, recipient,message),processName, instance);//2	

		//State:r3 -> self = recipient, agent1 = sender, agent2 = subject
			new Memory(recipient, subjectName, sinstance,instance).substitute(new K21(recipient, sender, message), new K21a(recipient, sender, message),processName, instance);//1
		}
	}

}
