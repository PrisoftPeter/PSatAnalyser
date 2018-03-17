package psat.behaviour.transactions;

import psat.behaviour.processes.AckSendProcess;
import psat.behaviour.processes.SendProcess;
import psat.util.Attribute;

public class Sent1Transaction extends Transaction {

	public Sent1Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Sent1", subjectName, senderName, recipientName, message);
		
		addProcess(new SendProcess(subjectName, senderName, recipientName, message, sessionid));
		addProcess(new AckSendProcess(subjectName, senderName, recipientName, message, sessionid));
	}
	
}
