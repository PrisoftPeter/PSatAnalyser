package psat.util;

import java.io.Serializable;

public class KnowledgeLevel implements Serializable{
	private static final long serialVersionUID = 2833230624147184776L;
	private String selfAgentName;
	private double beliefLevel = 0;
	private double uncertaintyLevel = 1;
	private String[] zoneAgents;
	private KnowledgeBase knowledgeBase;
	
	private String kldescription;
	
	public KnowledgeLevel(String selfAgentName, String[] zoneAgents, KnowledgeBase knowledgeBase){
		this.selfAgentName = selfAgentName;
		this.zoneAgents = zoneAgents;
		this.knowledgeBase = knowledgeBase;
	}
	
	public void setBeliefLevel(double beliefLevel){
		this.beliefLevel = beliefLevel;
	}
	
	public void setUncertaintyLevel(double uncertaintyLevel){
		this.uncertaintyLevel = uncertaintyLevel;
	}
	
	public String getSelfAgentName(){
		return selfAgentName;
	}
	
	public double getBeliefLevel(){
		return beliefLevel;
	}
	
	public double getUncertaintyLevel(){
		return uncertaintyLevel;
	}
	
	public String[] getZoneAgents(){
		return zoneAgents;
	}
	public KnowledgeBase getKnowledgeBase(){
		return knowledgeBase;
	}

	public String getKldescription() {
		return kldescription;
	}

	public void setKldescription(String kldescription) {
		this.kldescription = kldescription;
	}
}
