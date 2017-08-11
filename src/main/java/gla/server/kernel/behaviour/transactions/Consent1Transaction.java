package gla.server.kernel.behaviour.transactions;

import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.behaviour.processes.AckGrantConsentProcess;
import gla.server.kernel.behaviour.processes.GrantConsentProcess;
import gla.server.kernel.behaviour.processes.SeekConsentProcess;
import gla.server.session.ServerConfigInstance;

public class Consent1Transaction extends Transaction {

	public Consent1Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Consent1", subjectName, senderName, recipientName, message);
		
		addProcess(new SeekConsentProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
		addProcess(new GrantConsentProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
		addProcess(new AckGrantConsentProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));		
	}
	
}
