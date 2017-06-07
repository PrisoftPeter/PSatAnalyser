package gla.prisoft.server.kernel.behaviour.transactions;

import gla.prisoft.server.kernel.behaviour.processes.RequestProcess;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class RequestTransaction extends Transaction {

	public RequestTransaction(String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Request", null, senderName, recipientName, message);
		
		addProcess(new RequestProcess(senderName,recipientName, message, sessionid, sinstance, instance));
	}
	
}
