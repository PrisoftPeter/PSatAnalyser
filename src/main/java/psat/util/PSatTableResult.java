package psat.util;

import java.io.Serializable;

public class PSatTableResult implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int index;
	private String flowColumn; 
	private String protocolColumn;
	private String suSatColumn;
	private String sSatColumn;
	private String rSatColumn;
	private String pathSatColumn;
	private String assertionColumn;
	private String costColumn;
	private String feasibilityColumn;
	private String decisionColumn;
	
	private String path;
	
	private String requirementHtmlFullDesc;
	
	private RowType rowType;
	
	
	public String getFlowColumn() {
		return flowColumn;
	}
	public void setFlowColumn(String flowColumn) {
		this.flowColumn = flowColumn;
	}
	public String getProtocolColumn() {
		return protocolColumn;
	}
	public void setProtocolColumn(String protocolColumn) {
		this.protocolColumn = protocolColumn;
	}
	public String getSuSatColumn() {
		return suSatColumn;
	}
	public void setSuSatColumn(String selfSatColumn) {
		this.suSatColumn = selfSatColumn;
	}
	public String getsSatColumn() {
		return sSatColumn;
	}
	public void setsSatColumn(String sSatColumn) {
		this.sSatColumn = sSatColumn;
	}
	public String getrSatColumn() {
		return rSatColumn;
	}
	public void setrSatColumn(String rSatColumn) {
		this.rSatColumn = rSatColumn;
	}
	public String getPathSatColumn() {
		return pathSatColumn;
	}
	public void setPathSatColumn(String pathSatColumn) {
		this.pathSatColumn = pathSatColumn;
	}
	public String getAssertionColumn() {
		return assertionColumn;
	}
	public void setAssertionColumn(String assertionColumn) {
		this.assertionColumn = assertionColumn;
	}
	public String getCostColumn() {
		return costColumn;
	}
	public void setCostColumn(String costColumn) {
		this.costColumn = costColumn;
	}
	public String getFeasibilityColumn() {
		return feasibilityColumn;
	}
	public void setFeasibilityColumn(String mfColumn) {
		this.feasibilityColumn = mfColumn;
	}
	public String getDecisionColumn() {
		return decisionColumn;
	}
	public void setDecisionColumn(String decisionColumn) {
		this.decisionColumn = decisionColumn;
	}
	public RowType getRowType() {
		return rowType;
	}
	public void setRowType(RowType rowType) {
		this.rowType = rowType;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	private String flow; 
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public double getSuSat() {
		return suSat;
	}
	public void setSuSat(double selfSat) {
		this.suSat = selfSat;
	}
	public double getsSat() {
		return sSat;
	}
	public void setsSat(double sSat) {
		this.sSat = sSat;
	}
	public double getrSat() {
		return rSat;
	}
	public void setrSat(double rSat) {
		this.rSat = rSat;
	}
	public double getPathSat() {
		return pathSat;
	}
	public void setPathSat(double pathSat) {
		this.pathSat = pathSat;
	}
	public String getAssertion() {
		return assertion;
	}
	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getFeasibility() {
		return feasibility;
	}
	public void setFeasibility(double mf) {
		this.feasibility = mf;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getRequirementHtmlFullDesc() {
		return requirementHtmlFullDesc;
	}
	public void setRequirementHtmlFullDesc(String requirementHtmlFullDesc) {
		this.requirementHtmlFullDesc = requirementHtmlFullDesc;
	}

	public double getVgoal() {
		return vgoal;
	}
	public void setVgoal(double vgoal) {
		this.vgoal = vgoal;
	}

	public int getDecisionCategory() {
		return decisionCategory;
	}
	public void setDecisionCategory(int decisionCategory) {
		this.decisionCategory = decisionCategory;
	}

	public double getBenefit() {
		return benefit;
	}
	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}

	private String protocol;
	private double suSat;
	private double sSat;
	private double rSat;
	private double pathSat;
	private String assertion;
	private double cost;
	private double feasibility;
	private String decision;
	private int decisionCategory;
	private double vgoal =-1;
	private double benefit;
}
