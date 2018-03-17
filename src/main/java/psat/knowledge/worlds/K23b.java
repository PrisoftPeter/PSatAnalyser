package psat.knowledge.worlds;

import java.io.Serializable;

import psat.util.Agent;
import psat.util.Attribute;

public class K23b extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Attribute attribute;
	private boolean knows1 = false;
	private boolean knows2 = false;
	public static String htmlType1;
	
	public K23b(Agent self, Agent agent1, Attribute attribute){
		this.self = self;
		this.agent1 = agent1;
		this.attribute = attribute;
		
		super.type = "K23b";
		super.htmlType = "<i>k</i><sub>23b</sub>";
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
	public Agent getAgent1(){
		return agent1;
	}
	
	@Override
	public Agent getAgent2(){
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
				  "<i>K</i><sub>"+agent1.getAgentName()+"</sub>";
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
				  "<i>K</i><sub>"+agent1.getAgentName()+"</sub>";
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
				  "K_"+agent1.getAgentName()+"_";
		if(!knows2){
			res = res+"¬";
		}				
		res = res+"K_"+self.getAgentName()+"_"+attribute.toString();
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getMeaning() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		// TODO Auto-generated method stub
		return null;
	}
}
