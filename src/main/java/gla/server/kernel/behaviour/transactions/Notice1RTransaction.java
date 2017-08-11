package gla.server.kernel.behaviour.transactions;

import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.behaviour.processes.AckInformRProcess;
import gla.server.kernel.behaviour.processes.InformRProcess;
import gla.server.session.ServerConfigInstance;

public class Notice1RTransaction extends Transaction {

	public Notice1RTransaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid, ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Notice1-r", subjectName, senderName, recipientName, message);
		
		addProcess(new InformRProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
		addProcess(new AckInformRProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
	}	
}
