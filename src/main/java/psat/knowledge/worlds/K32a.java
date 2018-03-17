package psat.knowledge.worlds;

import java.io.Serializable;

import psat.util.Agent;
import psat.util.Attribute;

public class K32a extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent2;
	private Attribute attribute;
	private boolean knows = true;
	public static String htmlType1;
	
	public K32a(Agent self, Agent agent2, Attribute attribute){
		this.self = self;
		this.agent2 = agent2;
		this.attribute = attribute;
		
		super.type = "K32a";
		super.htmlType = "<i>k</i><sub>32a</sub>";
		htmlType1 = super.htmlType;
	}

	public boolean knows() {
		return knows;
	}
	
	@Override
	public Agent getSelf() {
		return self;
	}
	
	public Agent getAgent2(){
		return agent2;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		if(!knows){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+self.getAgentName()+"</sub>"+
				  "<i>K</i><sub>"+agent2.getAgentName()+"</sub>"+attribute.toHtmlString();
		res = res+"</html>";
				
		return res;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+self.getAgentName()+"</sub>"+
				  "<i>K</i><sub>"+agent2.getAgentName()+"</sub>"+attribute.toHtmlString();
	
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"B_"+self.getAgentName()+"_"+
				  "K_"+agent2.getAgentName()+"_"+attribute.toString();
	
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" beliefs that "+genericAgent2+" knows "+attribute.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" beliefs that "+agent2.getAgentName()+" knows "+attribute.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+genericSelf+"</sub>"+
				  "<i>K</i><sub>"+genericAgent2+"</sub>"+attribute.toHtmlString();
		
		return res;
	}

	@Override
	public Agent getAgent1() {
		return null;
	}
}
