package gla.prisoft.shared.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K22a extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent2;
	private Attribute attribute;
	private boolean knows = true;
	public static String htmlType1;
	
	public K22a(Agent self, Agent agent2, Attribute attribute){
		this.self = self;
		this.agent2 = agent2;
		this.attribute = attribute;
		
		super.type = "K22a";
		super.htmlType = "<i>k</i><sub>22a</sub>";
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
	public Agent getAgent2(){
		return agent2;
	}

	@Override
	public Agent getAgent1() {
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
				  "<i>K</i><sub>"+agent2.getAgentName()+"</sub>"+
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
				  "<i>K</i><sub>"+agent2.getAgentName()+"</sub>"+
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
				  "K_"+agent2.getAgentName()+"_"+
				  "K_"+self.getAgentName()+"_"+attribute.toString();
	
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" beliefs that "+genericAgent2+" knows that "+genericSelf+" knows "+attribute.toHtmlString();
	}
	
	public String getMeaning() {
		return self.getAgentName()+" beliefs that "+agent2.getAgentName()+" knows that "+self.getAgentName()+" knows "+attribute.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String res= "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+genericSelf+"</sub>"+
				  "<i>K</i><sub>"+genericAgent2+"</sub>"+
				  "<i>K</i><sub>"+genericSelf+"</sub>"+attribute.toHtmlString();
				
		return res;
	}

}
