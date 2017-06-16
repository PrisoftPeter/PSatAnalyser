package gla.prisoft.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K21 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Attribute att;
	public K21a k21a;
	public K21b k21b;
	public static String htmlType1;
	
	public K21(Agent self,Agent agent1, Attribute att){
		k21a = new K21a(self,agent1, att);
		k21b = new K21b(self,agent1, att);

		super.type = "K21";
		super.htmlType = "<b>K</b><sub>21</sub>";
		htmlType1 = super.htmlType;
		this.self = self;
		this.agent1 = agent1;
		this.att = att;
	}
	
	@Override
	public Agent getSelf() {
		return self;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k21a.toLimitHtmlString()+"|"+k21b.toLimitHtmlString();		
		res = res+"</html>";
				
		return res;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		res = res+k21a.toLimitHtmlString()+"|"+k21b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k21a.toString()+"I"+k21b.toString();
		}
		else{
			res = res+k21a.toString()+"|"+k21b.toString();	
		}			
		
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {		
		return genericSelf+" is uncertain if "+genericAgent1+" knows that "+genericSelf+" knows "+att.toHtmlString();
	}
	

	@Override
	public String getMeaning() {		
		return self.getAgentName()+" is uncertain if "+agent1.getAgentName()+" knows that "+self.getAgentName()+" knows "+att.toHtmlString();
	}
	
	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1, String genericAgent2) {
		String s= "<i>B</i><sub>"+genericSelf+"</sub>"+
				  "<i>K</i><sub>"+genericAgent1+"</sub>"+
				  "<i>K</i><sub>"+genericSelf+"</sub>"+att.toHtmlString()+
				  "|¬"+
				  "<i>B</i><sub>"+genericSelf+"</sub>"+
				  "<i>K</i><sub>"+genericAgent1+"</sub>"+
				  "<i>K</i><sub>"+genericSelf+"</sub>"+att.toHtmlString();
				  
		return s;
	}

	@Override
	public Agent getAgent1() {
		return agent1;
	}

	@Override
	public Agent getAgent2() {
		return null;
	}
}
