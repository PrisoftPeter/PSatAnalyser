package gla.server.kernel.behaviour.processes;

import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.server.session.ServerConfigInstance;

public abstract class Process {
	protected final String processName;
	protected String subjectName;
	protected String senderName;
	protected String recipientName;
	protected Attribute message;
	
	public Process(String processName, String subjectName, String senderName, String recipientName, Attribute message){
		this.processName = processName;
		this.subjectName = subjectName;
		this.senderName = senderName;
		this.recipientName = recipientName;
		this.message = message;
	}
	
	public String getProcessName() {
		return processName;
	}
		
	public String getSubjectName() {
		return subjectName;
	}

	public String getSenderName() {
		return senderName;
	}

	public String getRecipientName() {
		return recipientName;
	}
	
	public Attribute getMessage() {
		return message;
	}

	public String toString(){
		return processName;
	}
	
	protected abstract void execute(ServerConfigInstance sinstance, ConfigInstance instance);
}
