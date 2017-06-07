package gla.prisoft.shared;

import java.io.Serializable;

public class AssertionInstance implements Serializable{
	private static final long serialVersionUID = 172147074299623098L;
	private String assertion;
	private double goalv; //desired level of assertion satisfaction
	
	public AssertionInstance(String assertion, double goalv){
		this.setAssertion(assertion);
		this.setGoalv(goalv);
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

	
}
