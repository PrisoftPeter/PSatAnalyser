package gla.prisoft.server.kernel.behaviour.transactions;

import gla.prisoft.server.kernel.behaviour.processes.InformRProcess;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;

public class Notice2RTransaction extends Transaction {

	public Notice2RTransaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid, ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Notice2-r", subjectName, senderName, recipientName, message);
		
		addProcess(new InformRProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
	}
	
}