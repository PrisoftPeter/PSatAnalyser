package gla.prisoft.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K31b extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Attribute attribute;
	private boolean knows = false;
	public static String htmlType1;
	
	public K31b(Agent self, Agent agent1, Attribute attribute){
		this.self = self;
		this.agent1 = agent1;
		this.attribute = attribute;
		
		super.type = "K31b";
		super.htmlType = "<i>k</i><sub>31b</sub>";
		htmlType1 = super.htmlType;
	}

	public boolean knows() {
		return knows;
	}
	
	@Override
	public Agent getSelf() {
		return self;
	}
	
	public Agent getAgent1(){
		return agent1;
	}
	public Agent getAgent2(){
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
				  "<i>K</i><sub>"+agent1.getAgentName()+"</sub>"+attribute.toHtmlString();
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
				  "<i>K</i><sub>"+agent1.getAgentName()+"</sub>"+attribute.toHtmlString();
	
		return res;
	}
	
	@Override
	public String toString() {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"B_"+self.getAgentName()+"_"+
				  "K_"+agent1.getAgentName()+"_"+attribute.toString();
	
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
