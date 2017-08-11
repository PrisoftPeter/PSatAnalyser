package gla.server.kernel.behaviour.transactions;

import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.kernel.behaviour.processes.AckInformSuProcess;
import gla.server.kernel.behaviour.processes.InformSuProcess;
import gla.server.session.ServerConfigInstance;

public class Notice1SuTransaction extends Transaction {

	public Notice1SuTransaction(String subjectName, String senderName,String recipientName, Attribute message, String sessionid, ServerConfigInstance sinstance,ConfigInstance instance) {
		super("Notice1-su", subjectName, senderName, recipientName, message);
		
		addProcess(new InformSuProcess(subjectName, senderName, recipientName, message, sessionid, sinstance, instance));
		addProcess(new AckInformSuProcess(subjectName, senderName, recipientName, message, sessionid,  sinstance, instance));
	}
	
}
