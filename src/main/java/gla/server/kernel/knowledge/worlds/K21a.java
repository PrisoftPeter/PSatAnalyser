package gla.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K21a extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Attribute attribute;
	private boolean knows = true;
	public static String htmlType1;
	
	public K21a(Agent self, Agent agent1, Attribute attribute){
		this.self = self;
		this.agent1 = agent1;
		this.attribute = attribute;
		
		super.type = "K21a";
		super.htmlType = "<i>k</i><sub>21a</sub>";
		htmlType1 = super.htmlType;
	}

	public boolean knows() {
		return knows;
	}
	
	@Override
	public Agent getSelf() {
		return self;
	}
	
	@Override
	public Agent getAgent1(){
		return agent1;
	}
	@Override
	public Agent getAgent2() {
		return null;
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
				  "<i>K</i><sub>"+agent1.getAgentName()+"</sub>"+
				  "<i>K</i><sub>"+self.getAgentName()+"</sub>"+attribute.toHtmlString();
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
				  "<i>K</i><sub>"+agent1.getAgentName()+"</sub>"+
				  "<i>K</i><sub>"+self.getAgentName()+"</sub>"+attribute.toHtmlString();
	
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"B_"+self.getAgentName()+"_"+
				  "K_"+agent1.getAgentName()+"_"+
				  "K_"+self.getAgentName()+"_"+attribute.toString();
	
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {		
		return genericSelf+" beliefs that "+genericAgent1+" knows that "+genericSelf+" knows "+attribute.toHtmlString();
	}
	
	@Override
	public String getMeaning() {		
		return self.getAgentName()+" beliefs that "+agent1.getAgentName()+" knows that "+self.getAgentName()+" knows "+attribute.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+genericSelf+"</sub>"+
				  "<i>K</i><sub>"+genericAgent1+"</sub>"+
				  "<i>K</i><sub>"+genericSelf+"</sub>"+attribute.toHtmlString();
	
		return res;
	}

}
