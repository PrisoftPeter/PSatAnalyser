package gla.prisoft.shared;

import java.io.Serializable;

public class AssertionAspect implements Serializable{
	private static final long serialVersionUID = 172147074299623098L;
	private String selfAgentName;
	private String aspectType;
	private String[] zoneAgents;
	private KnowledgeBase knowledgeBase; //used for uncertainty/belief level reasoning
	private double goalv; //desired level of aspect satisfaction
	
	public AssertionAspect(String selfAgentName, String aspectType, String[] zoneAgents, KnowledgeBase knowledgeBase, double goalv){
		this.selfAgentName = selfAgentName;
		this.aspectType = aspectType;
		this.zoneAgents = zoneAgents;
		this.knowledgeBase = knowledgeBase;
		this.setGoalv(goalv);
	}
	
	public String getSelfAgentName(){
		return selfAgentName;
	}
	
	public String getAspectType(){
		return aspectType;
	}
	
	public String[] getZoneAgents(){
		return zoneAgents;
	}
	
	public KnowledgeBase getKnowledgeBase(){
		return knowledgeBase;
	}

	public double getGoalv() {
		return goalv;
	}

	public void setGoalv(double goalv) {
		this.goalv = goalv;
	}

	
}
