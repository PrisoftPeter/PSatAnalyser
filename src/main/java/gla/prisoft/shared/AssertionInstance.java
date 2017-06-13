package gla.prisoft.shared;

import java.io.Serializable;

public class AssertionInstance implements Serializable{
	private static final long serialVersionUID = 172147074299623098L;
	private String assertion;
	private double goalv; //desired level of assertion satisfaction
	private CollectiveStrategy cs;
	
	public AssertionInstance(String assertion, double goalv,CollectiveStrategy cs){
		this.setAssertion(assertion);
		this.setGoalv(goalv);
		this.setCollectiveStragegy(cs);
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

	public CollectiveStrategy getCollectiveStrategy() {
		return cs;
	}

	public void setCollectiveStragegy(CollectiveStrategy cs) {
		this.cs = cs;
	}
	
}
