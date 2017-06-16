package gla.prisoft.server.kernel.behaviour.processes;

import gla.prisoft.server.kernel.knowledge.Memory;
import gla.prisoft.server.kernel.knowledge.worlds.*;
import gla.prisoft.server.kernel.util.ServerAgentFactory;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class GrantConsentProcess extends Process {
	static final String processName = "grantConsent";

	public GrantConsentProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute(sinstance,instance);
		
	}

	@Override
	protected void execute(ServerConfigInstance sinstance,ConfigInstance instance){
		
		Agent subject = ServerAgentFactory.getAgent(subjectName,sinstance);
		Agent sender = ServerAgentFactory.getAgent(senderName,sinstance);
//		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:s3 -> self = sender, agent 1=subject agent 2 =recipient
		if(subject.getAgentName().equals(sender.getAgentName())){
//			new Memory(sender, subjectName).substitute(new K1(sender, message), new K1a(sender, message),processName);//1
//			new Memory(sender, subjectName).substitute(new K32(sender,subject, message), new K32a(sender,subject, message),processName);//2		
//			
			for(String agentName:instance.selectedAgentPath){
				if(!sender.getAgentName().equals(agentName)){
					Agent agent = ServerAgentFactory.getAgent(agentName,sinstance);
					boolean passed = new Memory(sender, subjectName, sinstance,instance).substitute(new K23(sender,agent,message), new K21(sender, agent,message),processName, instance);//3
					if(!passed){
						new Memory(sender, subjectName, sinstance,instance).substitute(new K24(sender,agent,message), new K22(sender, agent,message),processName, instance);//4
					}
				}	
			}
		}
		else{
			new Memory(sender, subjectName, sinstance,instance).substitute(new K1(sender, message), new K1a(sender, message),processName, instance);//1
			new Memory(sender, subjectName, sinstance,instance).substitute(new K32(sender,subject, message), new K32a(sender,subject, message),processName, instance);//2	
//			
			for(String agentName:instance.selectedAgentPath){
				if(!sender.getAgentName().equals(agentName)){
					Agent agent = ServerAgentFactory.getAgent(agentName,sinstance);
					boolean passed = new Memory(sender, subjectName, sinstance,instance).substitute(new K23(sender,agent,message), new K21(sender, agent,message),processName, instance);//3
					if(!passed){
						new Memory(sender, subjectName, sinstance,instance).substitute(new K24(sender,agent,message), new K22(sender, agent,message),processName, instance);//4
					}
				}	
			}
		}
//		new Memory(sender, subjectName).substitute(new K1(sender, message), new K1a(sender, message),processName);//1
//		if(!sender.getAgentName().equals(subject.getAgentName())){
//			new Memory(sender, subjectName).substitute(new K32(sender,subject, message), new K32a(sender,subject, message),processName);//2	
////			new Memory(sender, subjectName).substitute(new K23(sender,subject,message), new K21(sender, subject,message));//3	
//		}
////		if(!sender.getAgentName().equals(recipient.getAgentName())){
////			new Memory(sender, subjectName).substitute(new K24(sender,recipient,message), new K22(sender, recipient,message));//4			
////		}
////		
//		for(String agentName:Display.selectedAgentPath){
//			if(!sender.getAgentName().equals(agentName)){
//				Agent agent = AgentFactory.getAgent(agentName);
//				boolean passed = new Memory(sender, subjectName).substitute(new K23(sender,agent,message), new K21(sender, agent,message),processName);//3
//				if(!passed){
//					new Memory(sender, subjectName).substitute(new K24(sender,agent,message), new K22(sender, agent,message),processName);//4
//				}
//			}	
//		}
	}

}
