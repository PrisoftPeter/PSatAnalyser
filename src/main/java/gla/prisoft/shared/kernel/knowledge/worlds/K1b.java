package gla.prisoft.shared.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K1b extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Attribute attribute;
	private Agent self;
	private boolean knows = false;
	public static String htmlType1;
	
	public K1b(Agent self, Attribute attribute){
		this.self = self;
		this.attribute = attribute;
		
		super.type = "K1b";
		super.htmlType = "<i>k</i><sub>1b</sub>";
		htmlType1 = super.htmlType;
	}

	public boolean knows() {
		return knows;
	}
	
	@Override
	public Agent getSelf() {
		return self;
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
		
		res = res+"<i>B</i><sub>"+self.getAgentName()+"</sub>"+attribute.toHtmlString();
		res = res+"</html>";

				
		return res;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		
		res = res+"<i>B</i><sub>"+self.getAgentName()+"</sub>"+attribute.toHtmlString();

				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		if(!knows){
			res = res+"¬";
		}
		res = res+"B_"+self.getAgentName()+"_"+attribute.toString();
	
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return null;
	}
	
	@Override
	public String getMeaning() {
		return null;
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		return null;
	}

	@Override
	public Agent getAgent1() {
		return null;
	}

	@Override
	public Agent getAgent2() {
		return null;
	}
}
