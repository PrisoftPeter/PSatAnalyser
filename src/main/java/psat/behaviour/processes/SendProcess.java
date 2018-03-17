package psat.behaviour.processes;

import psat.PSatAPI;
import psat.knowledge.Memory;
import psat.knowledge.worlds.*;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;

public class SendProcess extends Process {
	static final String processName = "send";

	public SendProcess(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, subjectName, senderName, recipientName, message);
		
		execute();
	}

	@Override
	protected void execute(){

//		Agent subject = AgentFactory.getAgent(subjectName);
		Agent sender = AgentFactory.getAgent(senderName);
		Agent recipient = AgentFactory.getAgent(recipientName);
		
		//State:s5 -> self = sender, agent 1=subject agent 2 =recipient
		new Memory(sender, subjectName).substitute(new K1(sender, message), new K1a(sender, message),processName);//1
//		if(!sender.getAgentName().equals(subject.getAgentName())){
//			new Memory(sender, subjectName).substitute(new K23(sender,subject,message), new K21(sender, subject,message));//2	
//		}
//		if(!sender.getAgentName().equals(recipient.getAgentName())){
//			new Memory(sender, subjectName).substitute(new K24(sender,recipient,message), new K22(sender, recipient,message));//3	
//		}
		for(String agentName:PSatAPI.instance.selectedAgentPath){
			if(!sender.getAgentName().equals(agentName)){
				Agent agent = AgentFactory.getAgent(agentName);
				boolean passed = new Memory(sender, subjectName).substitute(new K23(sender,agent,message), new K21(sender, agent,message),processName);//2
				if(!passed){
					new Memory(sender, subjectName).substitute(new K24(sender,agent,message), new K22(sender, agent,message),processName);//3	
				}
			}	
		}
		
		//State:r2 -> self = recipient, agent1 = sender, agent2 = subject
		new Memory(recipient, subjectName).substitute(new K1(recipient, message), new K1a(recipient, message),processName);//1
		if(!recipient.getAgentName().equals(sender.getAgentName())){
			new Memory(recipient, subjectName).substitute(new K31(recipient,sender, message), new K31a(recipient,sender, message),processName);//2
//			new Memory(recipient, subjectName).substitute(new K23(recipient,sender,message), new K21(recipient, sender,message));//3	
		}
//		if(!recipient.getAgentName().equals(subject.getAgentName())){
//			new Memory(recipient, subjectName).substitute(new K24(recipient,subject,message), new K22(recipient, subject,message));//4			
//		}
		for(String agentName:PSatAPI.instance.selectedAgentPath){
			if(!recipient.getAgentName().equals(agentName)){
				Agent agent = AgentFactory.getAgent(agentName);
				boolean passed = new Memory(recipient, subjectName).substitute(new K23(recipient,agent,message), new K21(recipient, agent,message),processName);//2
				if(!passed){
					new Memory(recipient, subjectName).substitute(new K24(recipient,agent,message), new K22(recipient, agent,message),processName);//3	
				}
			}	
		}
	}

}
