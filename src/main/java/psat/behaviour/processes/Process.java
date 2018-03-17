package psat.behaviour.processes;

import psat.util.Attribute;

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
	
	protected abstract void execute();
}
