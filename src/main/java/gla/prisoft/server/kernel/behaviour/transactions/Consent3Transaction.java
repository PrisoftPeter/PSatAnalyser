package gla.prisoft.server.kernel.behaviour.transactions;

import gla.prisoft.server.kernel.behaviour.processes.GrantConsentProcess;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class Consent3Transaction extends Transaction {

	public Consent3Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Consent3", subjectName, senderName, recipientName, message);
		
		addProcess(new GrantConsentProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
	}
	
}
