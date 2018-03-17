package psat.behaviour.transactions;

import psat.behaviour.processes.GrantConsentProcess;
import psat.util.Attribute;

public class Consent3Transaction extends Transaction {

	public Consent3Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Consent3", subjectName, senderName, recipientName, message);
		
		addProcess(new GrantConsentProcess(subjectName, senderName, recipientName, message, sessionid));
	}
	
}
