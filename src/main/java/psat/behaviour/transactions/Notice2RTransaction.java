package psat.behaviour.transactions;

import psat.behaviour.processes.InformRProcess;
import psat.util.Attribute;

public class Notice2RTransaction extends Transaction {

	public Notice2RTransaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Notice2-r", subjectName, senderName, recipientName, message);
		
		addProcess(new InformRProcess(subjectName, senderName, recipientName, message, sessionid));
	}
	
}
