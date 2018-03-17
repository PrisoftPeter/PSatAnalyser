package psat.behaviour.transactions;

import psat.behaviour.processes.InformSuProcess;
import psat.util.Attribute;

public class Notice2SuTransaction extends Transaction {

	public Notice2SuTransaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Notice2-su", subjectName, senderName, recipientName, message);
		
		addProcess(new InformSuProcess(subjectName, senderName, recipientName, message, sessionid));
	}
	
}
