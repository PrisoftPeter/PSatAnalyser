package psat.behaviour.transactions;

import psat.behaviour.processes.Process;
import psat.util.Attribute;

public abstract class Transaction {

	protected String transactionName;
	protected Process processes [];
	protected String subjectName;
	protected String senderName;
	protected String recipientName;
	protected Attribute message;
	
	public Transaction(String transactionName, String subjectName, String senderName,String recipientName, Attribute message){
		this.transactionName = transactionName;
		this.senderName = senderName;
		this.recipientName = recipientName;
		this.subjectName = subjectName;
		this.message = message;		
	}
	
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	
	public void addProcess(Process process){
		if(processes == null){
			processes = new Process[0];
		}
		
		Process [] temp = new Process[processes.length+1];
		for(int i=0;i<processes.length;i++){
			temp[i] = processes[i];
		}
		temp[processes.length] = process;		
		processes = temp;
	}
	
	public Process[] getProcesses(){
		return processes;
	}
	public String toString(){
		String desc = transactionName+"(";
		for(Process p:processes){
			desc = desc +p.toString()+",";
		}
		desc = desc +")";
		desc = desc.replace(",)", ")");
		
		return desc;
	}
}
