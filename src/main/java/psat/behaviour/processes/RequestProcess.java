package psat.behaviour.processes;

import psat.util.Attribute;

public class RequestProcess extends Process {
	static final String processName = "request";

	public RequestProcess(String senderName,String recipientName, Attribute message, String sessionid) {
		super(processName, null, senderName, recipientName, message);
		
		execute();
	}

	@Override
	public void execute(){
		//a request process is inconsequential
	}

}
