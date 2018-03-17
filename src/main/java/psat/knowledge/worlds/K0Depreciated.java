package psat.knowledge.worlds;

import java.io.Serializable;

import psat.util.Agent;
import psat.util.Attribute;

public class K0Depreciated extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Attribute att;
	public K0aDepreciated k0a;
	public K0bDepreciated k0b;
	public static String htmlType1;
		
	public K0Depreciated(Attribute att){
		k0a = new K0aDepreciated(att);
		k0b = new K0bDepreciated(att);
		
		super.type = "K0";
		super.htmlType = "<b>K</b><sub>0</sub>";
		htmlType1 = "<b>K</b><sub>0</sub>";
		this.self = null;
		this.att =att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k0a.toLimitHtmlString()+"|"+k0b.toLimitHtmlString();		
		res = res+"</html>";
				
		return res;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		res = res+k0a.toLimitHtmlString()+"|"+k0b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k0a.toString()+"I"+k0b.toString();
		}
		else{
			res = res+k0a.toString()+"|"+k0b.toString();	
		}				
				
		return res;
	}

	@Override
	public Agent getSelf() {
		return self;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return " every user is uncertain of "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return " every user is uncertain of "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1, String genericAgent2) {
		return att.toHtmlString()+"|¬"+att.toHtmlString();
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
