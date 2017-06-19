package gla.prisoft.shared;

import java.io.Serializable;

public class AssertionRole implements Serializable{
	private static final long serialVersionUID = 172147074299623098L;
	private String selfAgentName;
	private String roleType;
	private String[] zoneAgents;
	private KnowledgeBase knowledgeBase; //used for uncertainty/belief level reasoning
	private double goalv; //desired level of role satisfaction
	private CollectiveStrategy cs;
	
	public AssertionRole(String selfAgentName, String roleType, String[] zoneAgents, KnowledgeBase knowledgeBase, double goalv, CollectiveStrategy cs){
		this.selfAgentName = selfAgentName;
		this.roleType = roleType;
		this.zoneAgents = zoneAgents;
		this.knowledgeBase = knowledgeBase;
		this.setCollectiveStrategy(cs);
		this.setGoalv(goalv);
	}
	
	public String getSelfAgentName(){
		return selfAgentName;
	}
	
	public String getRoleType(){
		return roleType;
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

	public CollectiveStrategy getCollectiveStrategy() {
		return cs;
	}

	public void setCollectiveStrategy(CollectiveStrategy cs) {
		this.cs = cs;
	}

	
}
