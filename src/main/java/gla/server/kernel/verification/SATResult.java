package gla.server.kernel.verification;

public class SATResult{
	double sat = -1;
	double satuncertainty =-1;
	double satbelief = -1;
	double aveExpectedSelfBeliefLevel = -1;
	double aveExpectedSelfUncertaintyLevel = -1;
	double currentSelfUncertaintyLevel = -1;
	double currentSelfBeliefLevel = -1;
	
	public double getSat() {
		return sat;
	}
	public void setSat(double sat) {
		this.sat = sat;
	}
	public double getSatuncertainty() {
		return satuncertainty;
	}
	public void setSatuncertainty(double satuncertainty) {
		this.satuncertainty = satuncertainty;
	}
	public double getSatbelief() {
		return satbelief;
	}
	public void setSatbelief(double satbelief) {
		this.satbelief = satbelief;
	}
	public double getAveExpectedSelfBeliefLevel() {
		return aveExpectedSelfBeliefLevel;
	}
	public void setAveExpectedSelfBeliefLevel(double aveExpectedSelfBeliefLevel) {
		this.aveExpectedSelfBeliefLevel = aveExpectedSelfBeliefLevel;
	}
	public double getAveExpectedSelfUncertaintyLevel() {
		return aveExpectedSelfUncertaintyLevel;
	}
	public void setAveExpectedSelfUncertaintyLevel(
			double aveExpectedSelfUncertaintyLevel) {
		this.aveExpectedSelfUncertaintyLevel = aveExpectedSelfUncertaintyLevel;
	}
	public double getCurrentSelfUncertaintyLevel() {
		return currentSelfUncertaintyLevel;
	}
	public void setCurrentSelfUncertaintyLevel(double currentSelfUncertaintyLevel) {
		this.currentSelfUncertaintyLevel = currentSelfUncertaintyLevel;
	}
	public double getCurrentSelfBeliefLevel() {
		return currentSelfBeliefLevel;
	}
	public void setCurrentSelfBeliefLevel(double currentSelfBeliefLevel) {
		this.currentSelfBeliefLevel = currentSelfBeliefLevel;
	}
}
