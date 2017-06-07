package gla.prisoft.server.kernel.behaviour.transactions;

import gla.prisoft.server.kernel.behaviour.processes.AckGrantConsentProcess;
import gla.prisoft.server.kernel.behaviour.processes.GrantConsentProcess;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class Consent2Transaction extends Transaction {

	public Consent2Transaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Consent2", subjectName, senderName, recipientName, message);
		
		addProcess(new GrantConsentProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
		addProcess(new AckGrantConsentProcess(subjectName, senderName, recipientName, message, sessionid,  sinstance, instance));		
	}
	
}
