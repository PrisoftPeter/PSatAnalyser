package psat.behaviour.transactions;

import psat.behaviour.processes.AckGrantConsentProcess;
import psat.behaviour.processes.GrantConsentProcess;
import psat.behaviour.processes.SeekConsentProcess;
import psat.util.Attribute;

public class Consent1Transaction extends Transaction {

	public Consent1Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Consent1", subjectName, senderName, recipientName, message);
		
		addProcess(new SeekConsentProcess(subjectName, senderName, recipientName, message, sessionid));
		addProcess(new GrantConsentProcess(subjectName, senderName, recipientName, message, sessionid));
		addProcess(new AckGrantConsentProcess(subjectName, senderName, recipientName, message, sessionid));		
	}
	
}
