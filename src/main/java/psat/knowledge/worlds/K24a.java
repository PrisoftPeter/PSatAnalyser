package psat.knowledge.worlds;

import java.io.Serializable;

import psat.util.Agent;
import psat.util.Attribute;

public class K24a extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent2;
	private Attribute attribute;
	private boolean knows1 = true;
	private boolean knows2 = false;
	public static String htmlType1;
	
	public K24a(Agent self, Agent agent2, Attribute attribute){
		this.self = self;
		this.agent2 = agent2;
		this.attribute = attribute;
		
		super.type = "K24a";
		super.htmlType = "<i>k</i><sub>24a</sub>";
		htmlType1 = super.htmlType;
	}

	public boolean knows1() {
		return knows1;
	}
	public boolean knows2() {
		return knows2;
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
	public Agent getAgent1(){
		return null;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		if(!knows1){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+self.getAgentName()+"</sub>"+
				  "<i>K</i><sub>"+agent2.getAgentName()+"</sub>";
		if(!knows2){
			res = res+"¬";
		}				
		res = res+"<i>K</i><sub>"+self.getAgentName()+"</sub>"+attribute.toHtmlString();
		
		res = res+"</html>";
				
		return res;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		if(!knows1){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+self.getAgentName()+"</sub>"+
				  "<i>K</i><sub>"+agent2.getAgentName()+"</sub>";
		if(!knows2){
			res = res+"¬";
		}				
		res = res+"<i>K</i><sub>"+self.getAgentName()+"</sub>"+attribute.toHtmlString();
		
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		if(!knows1){
			res = res+"¬";
		}
		res = res+"B_"+self.getAgentName()+"_"+
				  "K_"+agent2.getAgentName()+"_";
		if(!knows2){
			res = res+"¬";
		}				
		res = res+"K_"+self.getAgentName()+"_"+attribute.toString();
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" beliefs that "+genericAgent2+" knows that "+genericSelf+" does not know "+attribute.toHtmlString();
	}

	@Override
	public String getMeaning() {
		return self.getAgentName()+" beliefs that "+agent2.getAgentName()+" knows that "+self.getAgentName()+" does not know "+attribute.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String res = "";
		if(!knows1){
			res = res+"¬";
		}
		res = res+"<i>B</i><sub>"+genericSelf+"</sub>"+
				  "<i>K</i><sub>"+genericAgent2+"</sub>";
		if(!knows2){
			res = res+"¬";
		}				
		res = res+"<i>K</i><sub>"+genericSelf+"</sub>"+attribute.toHtmlString();
		
		return res;
	}
}
