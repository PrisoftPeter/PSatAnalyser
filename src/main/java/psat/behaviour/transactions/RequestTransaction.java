package psat.behaviour.transactions;

import psat.behaviour.processes.RequestProcess;
import psat.util.Attribute;

public class RequestTransaction extends Transaction {

	public RequestTransaction(String senderName,String recipientName, Attribute message, String sessionid) {
		super("Request", null, senderName, recipientName, message);
		
		addProcess(new RequestProcess(senderName,recipientName, message, sessionid));
	}
	
}
