package gla.server.kernel.behaviour.transactions;

import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.behaviour.processes.RequestProcess;
import gla.server.session.ServerConfigInstance;

public class RequestTransaction extends Transaction {

	public RequestTransaction(String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Request", null, senderName, recipientName, message);
		
		addProcess(new RequestProcess(senderName,recipientName, message, sessionid, sinstance, instance));
	}
	
}
