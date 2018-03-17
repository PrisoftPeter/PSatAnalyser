package psat.behaviour.transactions;

import psat.behaviour.processes.SendProcess;
import psat.util.Attribute;

public class Sent2Transaction extends Transaction {

	public Sent2Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Sent2", subjectName, senderName, recipientName, message);
		
		addProcess(new SendProcess(subjectName, senderName, recipientName, message, sessionid));
	}
	
}
