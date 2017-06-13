package gla.prisoft.client.kernel.behaviour.protocol;

import java.awt.Rectangle;

import gla.prisoft.client.kernel.display.model.ProtocolView;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.shared.ArrayCleaner;
import gla.prisoft.shared.ConfigInstance;

public class ClientProtocolFactory {
	
	public static void displayProtocols(final ProtocolView pv){
//		PSatClient.netDeseraliseConfigInstance();
//		if(Display.instance.protocolSuite == null){
//			PSatClient.netInitProtocolSuite();
//			
//			PSatClient.netDeseraliseConfigInstance();
//		}
		Thread queryThread2 = new Thread() {
			public void run() {				
				int k=1;
				for(String p:PSatAPI.instance.protocolSuite){
					boolean checked = false;
					for(String ev:PSatAPI.instance.evaluatedProtocols){
						String s1 ="";
						if(ev != null){
							s1 = ev.split(" \\(")[1];
						}
						String s2 = p.split(" \\(")[1];
						if(s1.equals(s2)){
							checked = true;
							break;
						}
					}

					pv.model.addRow(new Object[]{k,checked,p});
					pv.model.fireTableDataChanged();
					Rectangle cellBounds = pv.table.getCellRect(pv.table.getRowCount() - 1, 0, true);
					pv.table.scrollRectToVisible(cellBounds);
					
					k = k+1;
				}				
			}
		};
		queryThread2.start();
	}
	

	public static boolean addToEvaluatedProtocols(String protocol, ConfigInstance instance){

		ArrayCleaner.clean(instance.evaluatedProtocols);

		if(protocol !=null){
			boolean exist = false;
			for(String s:instance.evaluatedProtocols){
				if(s.equals(protocol)){
					exist = true;
					break;
				}
			}
			if(!exist){
				String temp [] = new String[instance.evaluatedProtocols.length+1];			
				for(int i=0;i<instance.evaluatedProtocols.length;i++){
					temp[i] = instance.evaluatedProtocols[i];
				}
				temp[instance.evaluatedProtocols.length] = protocol;			
				instance.evaluatedProtocols = temp;	

				return true;
			}
			
		}
		return false;
	}
	
	public static boolean removeFromEvaluatedProtocols(String protocol, ConfigInstance instance){
		ArrayCleaner.clean(instance.evaluatedProtocols);
		
		boolean exist = false;
		for(String s:instance.evaluatedProtocols){
			if(s.equals(protocol)){
				exist = true;
				break;
			}
		}
		
		if(exist){
			String temp [] = new String[instance.evaluatedProtocols.length-1];	
			int j= 0;
			for(int i=0;i<instance.evaluatedProtocols.length;i++){
				String s = instance.evaluatedProtocols[i];
				if(!s.equals(protocol)){
					temp[j] = instance.evaluatedProtocols[i];
					j = j+1;
				}			
			}	
			
			instance.evaluatedProtocols = temp;

			return true;
		}	
		else{
			return false;
		}
	}
	
}
