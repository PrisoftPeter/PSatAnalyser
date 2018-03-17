package psat.knowledge.worlds;

import java.io.Serializable;

import psat.util.Agent;
import psat.util.Attribute;

public class K1 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Attribute att;
	public K1a k1a;
	public K1b k1b;
	public static String htmlType1;
		
	public K1(Agent self, Attribute att){
		k1a = new K1a(self, att);
		k1b = new K1b(self, att);
		
		super.type = "K1";
		super.htmlType = "<b>K</b><sub>1</sub>";
		htmlType1 = "<b>K</b><sub>1</sub>";
		this.self = self;
		this.att =att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k1a.toLimitHtmlString()+"|"+k1b.toLimitHtmlString();		
		res = res+"</html>";
				
		return res;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		res = res+k1a.toLimitHtmlString()+"|"+k1b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k1a.toString()+"I"+k1b.toString();
		}
		else{
			res = res+k1a.toString()+"|"+k1b.toString();	
		}				
				
		return res;
	}

	@Override
	public Agent getSelf() {
		return self;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" is uncertain of "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" is uncertain of "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1, String genericAgent2) {
		return "<i>B</i><sub>"+genericSelf+"</sub>"+att.toHtmlString()+"|¬<i>B</i><sub>"+genericSelf+"</sub>"+att.toHtmlString();
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
