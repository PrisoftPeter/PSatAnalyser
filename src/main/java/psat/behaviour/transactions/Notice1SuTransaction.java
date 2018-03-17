package psat.behaviour.transactions;

import psat.behaviour.processes.AckInformSuProcess;
import psat.behaviour.processes.InformSuProcess;
import psat.util.Attribute;

public class Notice1SuTransaction extends Transaction {

	public Notice1SuTransaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Notice1-su", subjectName, senderName, recipientName, message);
		
		addProcess(new InformSuProcess(subjectName, senderName, recipientName, message, sessionid));
		addProcess(new AckInformSuProcess(subjectName, senderName, recipientName, message, sessionid));
	}
	
}
