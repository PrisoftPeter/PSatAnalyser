package gla.server.kernel.behaviour.transactions;

import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.behaviour.processes.SendProcess;
import gla.server.session.ServerConfigInstance;

public class Sent2Transaction extends Transaction {

	public Sent2Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid, ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Sent2", subjectName, senderName, recipientName, message);
		
		addProcess(new SendProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
	}
	
}
