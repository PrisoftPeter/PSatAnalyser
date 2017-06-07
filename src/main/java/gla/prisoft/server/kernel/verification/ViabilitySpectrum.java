package gla.prisoft.server.kernel.verification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.shared.CombinationStrategy;
import gla.prisoft.shared.ConfigInstance;

public class ViabilitySpectrum implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String path;
	private String pathId;
	private String [] pathAgents;
	private Flow [] pathFlows;
	
	public static double lastSat;
	
	public ViabilitySpectrum(String path, String pathId,String[] pathAgents){
		this.setPath(path);
		this.pathId = pathId;
		this.pathAgents = pathAgents;
		
		pathFlows = new Flow[0];
		initPathFlows();
	}
	
	private void initPathFlows(){
		Flow [] tempPathFlows = new Flow[pathAgents.length-1];
		
		for(int i=0;i< pathAgents.length-1;i++){
			String from = pathAgents[i];
			String to = pathAgents[i+1];
			
			Flow f =new Flow();
			f.from = from;
			f.to = to;
			tempPathFlows[i] = f;
		}
		pathFlows = tempPathFlows;
	}
	
	public Flow getFlow(String from, String to){
		Flow f = null;
		
		for(int i=0;i<pathFlows.length;i++){
			f = pathFlows[i];
			if(f.to.equals(to) && f.from.equals(from)){
				break;
			}
		}
		
		return f;
	}
	
	public Flow getNextFlow(String from, String to){
		Flow nextf = null;
		
		for(int i=0;i<pathFlows.length;i++){
			Flow f = pathFlows[i];
			if(f.to.equals(to) && f.from.equals(from)){
				if(i+1< pathFlows.length){
					nextf = pathFlows[i+1];
					break;
				}
			}
		}
		
		return nextf;
	}
	
	public void updatePathFlows(String from, String to, String protocolDesc, String protocolId,
								double satActual,double uncertaintyDesired, double uncertaintyActual,
								double entropyDesired, double entropyActual, Operator operator, 
								CombinationStrategy uncertaintyCombinationStrategy, ConfigInstance instance){
		
		Flow [] tempPathFlows = new Flow[pathFlows.length];
		for(int i=0;i<pathFlows.length;i++){
			Flow f = pathFlows[i];
			if(f.to.equals(to) && f.from.equals(from)){
				f.updateSpectrum(protocolDesc, protocolId, satActual,uncertaintyDesired,
						         uncertaintyActual, entropyDesired, entropyActual, 
						         operator,uncertaintyCombinationStrategy, instance);
				
				tempPathFlows[i] = f;
			}
			else{
				tempPathFlows[i] = f;
			}
		}
		pathFlows = tempPathFlows;
	}
	
	public String getPath() {
		return path;
	}

	private void setPath(String path) {
		this.path = path;
	}
		
	public class Flow implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public String from;
		public String to;
		
		public String protocolDesc;
		public String protocolId;		
		public double value;
				
		PMap [] satspectrum0 = new PMap[0];
		PMap [] satspectrum1 = new PMap[0];
		PMap [] satspectrum2 = new PMap[0];
		PMap [] satspectrum3 = new PMap[0];
		PMap [] satspectrum4 = new PMap[0];
		PMap [] satspectrum5 = new PMap[0];
		PMap [] satspectrum6 = new PMap[0];
		PMap [] satspectrum7 = new PMap[0];
		PMap [] satspectrum8 = new PMap[0];
		PMap [] satspectrum9 = new PMap[0];
		PMap [] satspectrum10 = new PMap[0];
		
		PMap [] uspectrum0 = new PMap[0];
		PMap [] uspectrum1 = new PMap[0];
		PMap [] uspectrum2 = new PMap[0];
		PMap [] uspectrum3 = new PMap[0];
		PMap [] uspectrum4 = new PMap[0];
		PMap [] uspectrum5 = new PMap[0];
		PMap [] uspectrum6 = new PMap[0];
		PMap [] uspectrum7 = new PMap[0];
		PMap [] uspectrum8 = new PMap[0];
		PMap [] uspectrum9 = new PMap[0];
		PMap [] uspectrum10 = new PMap[0];
		
		PMap [] espectrum0 = new PMap[0];
		PMap [] espectrum1 = new PMap[0];
		PMap [] espectrum2 = new PMap[0];
		PMap [] espectrum3 = new PMap[0];
		PMap [] espectrum4 = new PMap[0];
		PMap [] espectrum5 = new PMap[0];
		PMap [] espectrum6 = new PMap[0];
		PMap [] espectrum7 = new PMap[0];
		PMap [] espectrum8 = new PMap[0];
		PMap [] espectrum9 = new PMap[0];
		PMap [] espectrum10 = new PMap[0];
				
		private PMap [] addToSpectrum(String protocolDesc, String protocolId, double satActual, 
							double uncertaintyDesired,double uncertaintyActual,double entropyDesired,
							double entropyActual, Operator operator, PMap [] spectrum, CombinationStrategy uncertaintyCombinationStrategy){
						
			if(satActual > this.value){
				this.protocolId = protocolId;
				this.protocolDesc = protocolDesc;
				this.value = satActual;	
			}			
			
			PMap pmap = new PMap();
			pmap.protocolDesc = protocolDesc;
			pmap.protocolId = protocolId;
			pmap.operator = operator;
			pmap.satActual = satActual;
			pmap.uncertaintyDesired = uncertaintyDesired;
			pmap.uncertaintyActual = uncertaintyActual;
			pmap.entropyDesired = entropyDesired;
			pmap.entropyActual = entropyActual;
			pmap.uncertaintyCombinationStrategy = uncertaintyCombinationStrategy;
			
			PMap [] temp = new PMap[spectrum.length+1];
			for(int i=0; i<spectrum.length;i++){
				temp[i] = spectrum[i];
			}
			temp[spectrum.length] = pmap;
			
			spectrum = temp;
			
			return spectrum;
		}
		
		
		private void updateSpectrum(String protocolDesc, String protocolId, double satActual,double uncertaintyDesired, double uncertaintyActual,
				double entropyDesired, double entropyActual, Operator operator, 
				CombinationStrategy uncertaintyCombinationStrategy, ConfigInstance instance){
						
			if(satActual >=0 && satActual <0.1){
				satspectrum0 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum0,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.1 && satActual <0.2){
				satspectrum1 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum1,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.2 && satActual <0.3){
				satspectrum2 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum2,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.3 && satActual <0.4){
				satspectrum3 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum3,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.4 && satActual <0.5){
				satspectrum4 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum4,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.5 && satActual <0.6){
				satspectrum5 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum5,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.6 && satActual <0.7){
				satspectrum6 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum6,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.7 && satActual <0.8){
				satspectrum7 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum7,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.8 && satActual <0.9){
				satspectrum8 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum8,uncertaintyCombinationStrategy);
			}
			else if(satActual >=0.9 && satActual <1){
				satspectrum9 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum9,uncertaintyCombinationStrategy);
			}
			else if(satActual ==1){
				satspectrum10 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, satspectrum10,uncertaintyCombinationStrategy);
			}	
			
			if(instance.isModeUncertainty){
				if(uncertaintyActual >=0 && uncertaintyActual <0.1){
					uspectrum0 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum0,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.1 && uncertaintyActual <0.2){
					uspectrum1 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum1,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.2 && uncertaintyActual <0.3){
					uspectrum2 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum2,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.3 && uncertaintyActual <0.4){
					uspectrum3 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum3,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.4 && uncertaintyActual <0.5){
					uspectrum4 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum4,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.5 && uncertaintyActual <0.6){
					uspectrum5 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum5,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.6 && uncertaintyActual <0.7){
					uspectrum6 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum6,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.7 && uncertaintyActual <0.8){
					uspectrum7 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum7,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.8 && uncertaintyActual <0.9){
					uspectrum8 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum8,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual >=0.9 && uncertaintyActual <1){
					uspectrum9 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum9,uncertaintyCombinationStrategy);
				}
				else if(uncertaintyActual ==1){
					uspectrum10 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, uspectrum10,uncertaintyCombinationStrategy);
				}
			}
			else if(instance.isModeEntropy){
				if(entropyActual >=0 && entropyActual <0.1){
					espectrum0 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum0,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.1 && entropyActual <0.2){
					espectrum1 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum1,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.2 && entropyActual <0.3){
					espectrum2 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum2,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.3 && entropyActual <0.4){
					espectrum3 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum3,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.4 && entropyActual <0.5){
					espectrum4 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum4,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.5 && entropyActual <0.6){
					espectrum5 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum5,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.6 && entropyActual <0.7){
					espectrum6 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum6,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.7 && entropyActual <0.8){
					espectrum7 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum7,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.8 && entropyActual <0.9){
					espectrum8 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum8,uncertaintyCombinationStrategy);
				}
				else if(entropyActual >=0.9 && entropyActual <1){
					espectrum9 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum9,uncertaintyCombinationStrategy);
				}
				else if(entropyActual ==1){
					espectrum10 = addToSpectrum(protocolDesc,protocolId, satActual,uncertaintyDesired, uncertaintyActual, entropyDesired, entropyActual,operator, espectrum10,uncertaintyCombinationStrategy);
				}
			}
		}
	}
	
	private class PMap implements Serializable{
		private static final long serialVersionUID = 1L;
		@SuppressWarnings("unused")
		String protocolDesc;
		String protocolId;
		Operator operator;
		
		double satActual;
		double satDesired = 1;
		
		//for uncertainty mode
		double uncertaintyDesired;
		double uncertaintyActual;
		
		//for entropy mode
		double entropyDesired;
		double entropyActual;
		
		CombinationStrategy uncertaintyCombinationStrategy;
		
//		//for feasibility
//		double disclosureCost;
//		double marginalFeasibility;
		
	}
	
	private SpectrumDescription formatSatSpectrum(Flow f, int level){
		
		PMap [] satspectrum = null;
		String range = "";
		if(level ==0){
			range = "0≤sat&lt;0.1";
			satspectrum = f.satspectrum0;
		}
		else if(level == 1){
			range = "0.1≤sat&lt;0.2";
			satspectrum = f.satspectrum1;
		}
		else if(level == 2){
			range = "0.2≤sat&lt;0.3";
			satspectrum = f.satspectrum2;
		}
		else if(level == 3){
			range = "0.3≤sat&lt;0.4";
			satspectrum = f.satspectrum3;
		}
		else if(level == 4){
			range = "0.4≤sat&lt;0.5";
			satspectrum = f.satspectrum4;
		}
		else if(level == 5){
			range = "0.5≤sat&lt;0.6";
			satspectrum = f.satspectrum5;
		}
		else if(level == 6){
			range = "0.6≤sat&lt;0.7";
			satspectrum = f.satspectrum6;
		}
		else if(level == 7){
			range = "0.7≤sat&lt;0.8";
			satspectrum = f.satspectrum7;
		}
		else if(level == 8){
			range = "0.8≤sat&lt;0.9";
			satspectrum = f.satspectrum8;
		}
		else if(level == 9){
			range = "0.9≤sat&lt;1";
			satspectrum = f.satspectrum9;
		}
		else if(level == 10){
			range = "sat=1";
			satspectrum = f.satspectrum10;
		}
				
		String description = "";
		description = description +"<i>sat spectrum </i>"+range+":";
		if(satspectrum.length>0){
			description = description +"<br>";
		}
		double passcount = 0;
		double failcount = 0;
		
		for(PMap pmap:satspectrum){
			String pid = pmap.protocolId;
			double value = pmap.satActual;
			value = Math.round(value * 100.0) / 100.0;
			String pid1 = "&#945;<sub>"+pid+"</sub>="+value;
			
			if(pmap.satActual >= pmap.satDesired){
				description = description+ "<font color=#196F3D>"+pid1+" </font>";
				passcount = passcount+1;
			}
			else{
				failcount = failcount+1;
				description = description+ "<font color=#A93226>"+pid1+" </font>";
			}				
		}
		
		double satvratio  = passcount/(double)112;
		satvratio = Math.round(satvratio * 100.0) / 100.0;
		double protocolRatioInSpectrum = satspectrum.length/(double)112;
		protocolRatioInSpectrum = Math.round(protocolRatioInSpectrum * 100.0) / 100.0;
		//System.out.println(f.from+"->"+f.to);
		if(protocolRatioInSpectrum>0){
			description = description + "<br>protocol ratio in spectrum="+protocolRatioInSpectrum+", spectrum pass count="+passcount+", spectrum fail count="+failcount+", satisfaction viability ratio="+satvratio+"<br>";
			//System.out.println("protocol ratio in spectrum="+protocolRatioInSpectrum+", spectrum pass count="+passcount+", spectrum fail count="+failcount+", satisfaction viability ratio="+satvratio);
		}
		else{
			description = description + "protocol ratio in spectrum="+protocolRatioInSpectrum+"<br>";
			//System.out.println("protocol ratio in spectrum="+protocolRatioInSpectrum);
		}
		description = description + "---<br>";
		
		SpectrumDescription sd = new SpectrumDescription();
		sd.description = description;
		sd.passcount = passcount;
		sd.failcount = failcount;
		sd.viabilityRatio = satvratio;
		sd.protocolRatioInSpectrum = protocolRatioInSpectrum;
		sd.range = range;
		sd.from = f.from;
		sd.to = f.to;
		sd.path = path;
		sd.descType = "sat";
				
		return sd;
	}
	
		
	private SpectrumDescription formatUncertaintySpectrum(Flow f, int level){
		
		PMap [] uspectrum = null;
		String range = "";
		if(level ==0){
			range = "0≤sat&lt;0.1";
			uspectrum = f.uspectrum0;
		}
		else if(level == 1){
			range = "0.1≤sat&lt;0.2";
			uspectrum = f.uspectrum1;
		}
		else if(level == 2){
			range = "0.2≤sat&lt;0.3";
			uspectrum = f.uspectrum2;
		}
		else if(level == 3){
			range = "0.3≤sat&lt;0.4";
			uspectrum = f.uspectrum3;
		}
		else if(level == 4){
			range = "0.4≤sat&lt;0.5";
			uspectrum = f.uspectrum4;
		}
		else if(level == 5){
			range = "0.5≤sat&lt;0.6";
			uspectrum = f.uspectrum5;
		}
		else if(level == 6){
			range = "0.6≤sat&lt;0.7";
			uspectrum = f.uspectrum6;
		}
		else if(level == 7){
			range = "0.7≤sat&lt;0.8";
			uspectrum = f.uspectrum7;
		}
		else if(level == 8){
			range = "0.8≤sat&lt;0.9";
			uspectrum = f.uspectrum8;
		}
		else if(level == 9){
			range = "0.9≤sat&lt;1";
			uspectrum = f.uspectrum9;
		}
		else if(level == 10){
			range = "sat=1";
			uspectrum = f.uspectrum10;
		}
		SpectrumDescription sd = new SpectrumDescription();
		String description = "";
		description = description +"<i>uncertainty spectrum </i>"+range+":";
		if(uspectrum.length>0){
			PMap p0 = uspectrum[0];
			CombinationStrategy cs = p0.uncertaintyCombinationStrategy;
			double uncertaintyBenchmark = p0.uncertaintyDesired;
			
			description = description +" uncertainty combination strategy:"+cs+" uncertainty threshold:"+uncertaintyBenchmark;
			description = description +"<br>";
		}
		double passcount = 0;
		double failcount = 0;
		
		
		for(PMap pmap:uspectrum){
			sd.addPMap(pmap);
			
			String pid = pmap.protocolId;
			double value = pmap.uncertaintyActual;
			value = Math.round(value * 100.0) / 100.0;
			String pid1 = "&#945;<sub>"+pid+"</sub>="+value;
			
			if(pmap.operator == Operator.GREATERTHANOREQUALTO){
				if(pmap.uncertaintyActual >=pmap.uncertaintyDesired){
					description = description+ "<font color=#196F3D>"+pid1+" </font>";
					passcount = passcount +1;
				}
				else{
					failcount = failcount +1;
					description = description+ "<font color=#A93226>"+pid1+" </font>";
				}
			}
			else if(pmap.operator == Operator.LESSTHANOREQUALTO){
				if(pmap.uncertaintyActual <=pmap.uncertaintyDesired){
					description = description+ "<font color=#196F3D>"+pid1+" </font>";
					passcount = passcount +1;
				}
				else{
					failcount = failcount +1;
					description = description+ "<font color=#A93226>"+pid1+" </font>";
				}
			}
		}
		double uvratio  = passcount/(double)112;
		uvratio = Math.round(uvratio * 100.0) / 100.0;
		double protocolRatioInSpectrum = uspectrum.length/(double)112;
		protocolRatioInSpectrum = Math.round(protocolRatioInSpectrum * 100.0) / 100.0;
		if(protocolRatioInSpectrum>0){
			description = description + "<br>protocol ratio in spectrum="+protocolRatioInSpectrum+", spectrum pass count="+passcount+", spectrum fail count="+failcount+", uncertainty viability ratio="+uvratio+"<br>";
		}
		else{
			description = description + "protocol ratio in spectrum="+protocolRatioInSpectrum+"<br>";
		}
		description = description + "---<br>";
				
		
		sd.description = description;
		sd.passcount = passcount;
		sd.failcount = failcount;
		sd.viabilityRatio = uvratio;
		sd.protocolRatioInSpectrum = protocolRatioInSpectrum;
		sd.range = range;
		sd.from = f.from;
		sd.to = f.to;
		sd.path = path;
		sd.descType = "sat";
		
		return sd;
	}
	
	private SpectrumDescription formatEntropySpectrum(Flow f, int level){
		
		PMap [] espectrum = null;
		String range = "";
		if(level ==0){
			range = "0≤sat&lt;0.1";
			espectrum = f.espectrum0;
		}
		else if(level == 1){
			range = "0.1≤sat&lt;0.2";
			espectrum = f.espectrum1;
		}
		else if(level == 2){
			range = "0.2≤sat&lt;0.3";
			espectrum = f.espectrum2;
		}
		else if(level == 3){
			range = "0.3≤sat&lt;0.4";
			espectrum = f.espectrum3;
		}
		else if(level == 4){
			range = "0.4≤sat&lt;0.5";
			espectrum = f.espectrum4;
		}
		else if(level == 5){
			range = "0.5≤sat&lt;0.6";
			espectrum = f.espectrum5;
		}
		else if(level == 6){
			range = "0.6≤sat&lt;0.7";
			espectrum = f.espectrum6;
		}
		else if(level == 7){
			range = "0.7≤sat&lt;0.8";
			espectrum = f.espectrum7;
		}
		else if(level == 8){
			range = "0.8≤sat&lt;0.9";
			espectrum = f.espectrum8;
		}
		else if(level == 9){
			range = "0.9≤sat&lt;1";
			espectrum = f.espectrum9;
		}
		else if(level == 10){
			range = "sat=1";
			espectrum = f.espectrum10;
		}
		
		String description = "";
		description = description +"<i>entropy spectrum </i>"+range+":";
		if(espectrum.length>0){
			PMap p0 = espectrum[0];
			double entropyBenchmark = p0.entropyDesired;
			
			description = description +" entropy threshold:"+entropyBenchmark;
			description = description +"<br>";
		}
		double passcount = 0;
		double failcount = 0;
		
		for(PMap pmap:espectrum){
			String pid = pmap.protocolId;
			double value = pmap.entropyActual;
			value = Math.round(value * 100.0) / 100.0;
			String pid1 = "&#945;<sub>"+pid+"</sub>="+value;
			
			if(pmap.operator == Operator.GREATERTHANOREQUALTO){
				if(pmap.entropyActual >=pmap.entropyDesired){
					description = description+ "<font color=#196F3D>"+pid1+" </font>";
					passcount = passcount +1;
				}
				else{
					failcount = failcount +1;
					description = description+ "<font color=#A93226>"+pid1+" </font>";
				}
			}
			else if(pmap.operator == Operator.LESSTHANOREQUALTO){
				if(pmap.entropyActual <=pmap.entropyDesired){
					description = description+ "<font color=#196F3D>"+pid1+" </font>";
					passcount = passcount +1;
				}
				else{
					failcount = failcount +1;
					description = description+ "<font color=#A93226>"+pid1+" </font>";
				}
			}
		}
		double evratio  = passcount/(double)112;
		evratio = Math.round(evratio * 100.0) / 100.0;
		double protocolRatioInSpectrum = espectrum.length/(double)112;
		protocolRatioInSpectrum = Math.round(protocolRatioInSpectrum * 100.0) / 100.0;
		if(protocolRatioInSpectrum>0){
			description = description + "<br>protocol ratio in spectrum="+protocolRatioInSpectrum+", spectrum pass count="+passcount+", spectrum fail count="+failcount+", entropy viability ratio="+evratio+"<br>";			
		}
		else{
			description = description + "protocol ratio in spectrum="+protocolRatioInSpectrum+"<br>";
		}
		description = description + "---<br>";
		
		SpectrumDescription sd = new SpectrumDescription();
		sd.description = description;
		sd.passcount = passcount;
		sd.failcount = failcount;
		sd.viabilityRatio = evratio;
		sd.protocolRatioInSpectrum = protocolRatioInSpectrum;
		sd.range = range;
		sd.from = f.from;
		sd.to = f.to;
		sd.path = path;
		sd.descType = "sat";
		
		return sd;
	}
	
	public SpectrumDescription[] getSpectrumHtmlProtocolIds(String from, String to, ConfigInstance instance){
		
		SpectrumDescription sds [] = new SpectrumDescription[0];
		
		for(int i=0;i<pathFlows.length;i++){
			Flow f = pathFlows[i];
			if(f.to.equals(to) && f.from.equals(from)){

				//iterate through each spectrum
				for(int j=0;j<=10;j++){
					//(1)
					SpectrumDescription satsd = formatSatSpectrum(f,j);					
					SpectrumDescription [] stemp = new SpectrumDescription[sds.length+1];
					for(int k=0;k< sds.length;k++){
						stemp[k] = sds[k];
					}
					stemp[sds.length] = satsd;
					sds = stemp;
				}
				for(int j=0;j<=10;j++){
					//(2)
					if(instance.isModeUncertainty){
						SpectrumDescription usd = formatUncertaintySpectrum(f,j);
						SpectrumDescription [] stemp1 = new SpectrumDescription[sds.length+1];
						for(int k=0;k< sds.length;k++){
							stemp1[k] = sds[k];
						}
						stemp1[sds.length] = usd;
						sds = stemp1;
					}
				}
				for(int j=0;j<=10;j++){
					//(3)
					if(instance.isModeEntropy){
						SpectrumDescription esd = formatEntropySpectrum(f,j);
						SpectrumDescription [] stemp1 = new SpectrumDescription[sds.length+1];
						for(int k=0;k< sds.length;k++){
							stemp1[k] = sds[k];
						}
						stemp1[sds.length] = esd;
						sds = stemp1;
					}
				}
			}
		}
		return sds;
	}
	
	public class SpectrumDescription{
		public String description;
		public double passcount;
		public double failcount;
		public double viabilityRatio;
		public double protocolRatioInSpectrum;
		public String range;
		
		public String from;
		public String to;
		public String path;
		
		public String descType;
		
		public PMap [] pmaps;
		
		public SpectrumDescription(){
			pmaps = new PMap[0];
		}
				
		public void addPMap(PMap pmap){
			PMap temp[] = new PMap[pmaps.length+1];
			
			for(int i=0;i<pmaps.length;i++){
				temp[i] = pmaps[i];
			}
			temp[pmaps.length] = pmap;
			pmaps = temp;			
		}
	}
	
	public static void serializeSpectrum(ViabilitySpectrum spectrum, ConfigInstance instance){
		
		String ref = "";
		if(instance.is_aspect_run){
			ref = "aspect";
		}
		else{
			ref = "instance";
		}
		
		String folderName1 = PSatAPI.datastore_file_path;
		String folderName2 = PSatAPI.datastore_file_path+"/"+instance.sessionid;
		String folderName3 = PSatAPI.datastore_file_path+"/"+instance.sessionid+"/"+ref+"spectrum";
		String fileName2 = PSatAPI.datastore_file_path+"/"+instance.sessionid+"/"+ref+"spectrum/"+spectrum.pathId+".ser";	
		
		try{			
			File folder1 = new File(folderName1);
			boolean exist1 = false;
			if(folder1.exists()){
				if(folder1.isDirectory()){
					exist1 = true;
				}				
			}
			if(!exist1){
				folder1.mkdir();
			}			
	
			File folder2 = new File(folderName2);
			boolean exist2 = false;
			if(folder2.exists()){
				if(folder2.isDirectory()){
					exist2 = true;
				}				
			}
			if(!exist2){
				folder2.mkdir();
			}
			
			File folder3 = new File(folderName3);
			boolean exist3 = false;
			if(folder3.exists()){
				if(folder3.isDirectory()){
					exist3 = true;
				}				
			}
			if(!exist3){
				folder3.mkdir();
			}
								
			File if_file = new File(fileName2);
	        if(if_file.exists()){
	         if_file.delete();
	        }
	        if_file.createNewFile();
	        FileOutputStream fileOut = new FileOutputStream(fileName2);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(spectrum);
	        out.close();
	        fileOut.close();	         
	      }
		catch(IOException i){
	          i.printStackTrace();
	    }
	
	}
	
	public static ViabilitySpectrum deserializeSpectrum(String pathId, ConfigInstance instance){
		ViabilitySpectrum spectrum= null;
		String ref = "";
		if(instance.is_aspect_run){
			ref = "aspect";
		}
		else{
			ref = "instance";
		}
		
		String fileName2 = PSatAPI.datastore_file_path+"/"+instance.sessionid+"/"+ref+"spectrum/"+pathId+".ser";	
		
		try {
			File file = new File(fileName2);
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			spectrum = (ViabilitySpectrum) in.readObject();
			
			in.close();
			fileIn.close();
		} 
		catch (IOException i) {
			System.err.println("IO exception @deserialiseViabilitySpectrum");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("ViabilitySpectrum class not found");
		}
		finally{
			
		}
			
		return spectrum;
	}
}
