package gla.server.kernel.behaviour.processes;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.knowledge.Memory;
import gla.server.kernel.knowledge.worlds.*;
import gla.server.kernel.util.ServerAgentFactory;
import gla.server.session.ServerConfigInstance;

public class SeekConsentProcess extends Process {
	static final String processName = "seekConsent";

	public SeekConsentProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute(sinstance,instance);
			
	}

	@Override
	protected void execute(ServerConfigInstance sinstance,ConfigInstance instance){
		
		Agent subject = ServerAgentFactory.getAgent(subjectName,sinstance);
//		Agent sender = AgentFactory.getAgent(senderName);
//		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:su1, self = subject, agent 1=sender, agent 2 = recipient
		new Memory(subject, subjectName, sinstance, instance).substitute(new K1(subject, message), new K1a(subject, message),processName, instance);//1
		
		for(String agentName:instance.selectedAgentPath){
			if(!subject.getAgentName().equals(agentName)){
				Agent agent = ServerAgentFactory.getAgent(agentName,sinstance);
				boolean passed = new Memory(subject, subjectName,sinstance, instance).substitute(new K23(subject,agent,message), new K21(subject, agent,message),processName, instance);//2
				if(!passed){
					new Memory(subject, subjectName, sinstance, instance).substitute(new K24(subject,agent,message), new K22(subject, agent,message),processName, instance);//3	
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
