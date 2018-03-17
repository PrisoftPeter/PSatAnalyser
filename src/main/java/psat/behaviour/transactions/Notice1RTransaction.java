package psat.behaviour.transactions;

import psat.behaviour.processes.AckInformRProcess;
import psat.behaviour.processes.InformRProcess;
import psat.util.Attribute;

public class Notice1RTransaction extends Transaction {

	public Notice1RTransaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Notice1-r", subjectName, senderName, recipientName, message);
		
		addProcess(new InformRProcess(subjectName, senderName, recipientName, message, sessionid));
		addProcess(new AckInformRProcess(subjectName, senderName, recipientName, message, sessionid));
	}	
}
