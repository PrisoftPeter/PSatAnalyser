package gla.server.kernel.behaviour.processes;

import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.session.ServerConfigInstance;

public class RequestProcess extends Process {
	static final String processName = "request";

	public RequestProcess(String senderName,String recipientName, Attribute message, String sessionid,ServerConfigInstance sinstance,ConfigInstance instance) {
		super(processName, null, senderName, recipientName, message);
		
		execute(sinstance,instance);
	}

	@Override
	public void execute(ServerConfigInstance sinstance,ConfigInstance instance){
		//a request process is inconsequential
	}

}
