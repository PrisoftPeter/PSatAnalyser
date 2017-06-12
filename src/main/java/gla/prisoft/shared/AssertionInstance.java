package gla.prisoft.shared;

import java.io.Serializable;

public class AssertionInstance implements Serializable{
	private static final long serialVersionUID = 172147074299623098L;
	private String assertion;
	private double goalv; //desired level of assertion satisfaction
	private boolean isCommonKnowledgeAssertion;
	
	public AssertionInstance(String assertion, double goalv,boolean isCommonKnowledgeAssertion){
		this.setAssertion(assertion);
		this.setGoalv(goalv);
		this.setCommonKnowledgeAssertion(isCommonKnowledgeAssertion);
	}
	
	public double getGoalv() {
		return goalv;
	}

	public void setGoalv(double goalv) {
		this.goalv = goalv;
	}


	public String getAssertion() {
		return assertion;
	}


	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}

	public boolean isCommonKnowledgeAssertion() {
		return isCommonKnowledgeAssertion;
	}

	public void setCommonKnowledgeAssertion(boolean isCommonKnowledgeAssertion) {
		this.isCommonKnowledgeAssertion = isCommonKnowledgeAssertion;
	}
	
}
