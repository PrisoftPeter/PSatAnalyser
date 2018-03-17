package psat.behaviour.transactions;

import psat.behaviour.processes.GrantConsentProcess;
import psat.behaviour.processes.SeekConsentProcess;
import psat.util.Attribute;

public class Consent4Transaction extends Transaction {

	public Consent4Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Consent4", subjectName, senderName, recipientName, message);
		
		addProcess(new SeekConsentProcess(subjectName, senderName, recipientName, message, sessionid));
		addProcess(new GrantConsentProcess(subjectName, senderName, recipientName, message, sessionid));
	}
	
}
