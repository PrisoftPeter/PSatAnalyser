package psat.behaviour.transactions;

import psat.behaviour.processes.AckGrantConsentProcess;
import psat.behaviour.processes.GrantConsentProcess;
import psat.util.Attribute;

public class Consent2Transaction extends Transaction {

	public Consent2Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid) {
		super("Consent2", subjectName, senderName, recipientName, message);
		
		addProcess(new GrantConsentProcess(subjectName, senderName, recipientName, message, sessionid));
		addProcess(new AckGrantConsentProcess(subjectName, senderName, recipientName, message, sessionid));		
	}
	
}
