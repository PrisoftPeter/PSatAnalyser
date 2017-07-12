package gla.prisoft.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gla.prisoft.client.Display;
import gla.prisoft.server.kernel.knowledge.worlds.World;
import gla.prisoft.shared.CollectiveMode;
import gla.prisoft.shared.ConfigInstance;

public class PSatAPI {
	public static ConfigInstance instance;
	public static String datastore_file_path ="datastore";
	public static int fvindex;
	public static boolean isnextpath;
	public static HashMap<World, ArrayList<World>> higherOrderKs = new HashMap<World, ArrayList<World>>();
	
	
	public static void logHighOrderImplications(){
				
		for (Map.Entry<World, ArrayList<World>> entry : higherOrderKs.entrySet()) {
		    World key = entry.getKey();
		    ArrayList<World> value = entry.getValue();

		    String htmlcgdesc = CollectiveMode.getModeLimitHtmlDesc(PSatAPI.instance.collectiveStrategy)+"("+key.toLimitHtmlString()+") &#8658; {";
		    
		    int count = 0;
		    int countTotal = 0;
		    for(Object loworder_o:value){
		    	if(loworder_o !=null){
		    		World loworder = (World)loworder_o;
		    		htmlcgdesc = htmlcgdesc + loworder.toLimitHtmlString()+", ";
					if(count == 10){
						htmlcgdesc = htmlcgdesc +"<br>";
						count = 0;
					}
					else{
						count = count +1;
					}
					countTotal = countTotal+1;
		    	}
		    }
		    htmlcgdesc = "<html>"+instance.currentPath+"<br>"+htmlcgdesc + "}</html>";
			htmlcgdesc = htmlcgdesc.replace(", }</html>", "}</html>");
			htmlcgdesc = htmlcgdesc.replace("</html>", "&#8712; A #:"+countTotal+"<br></html>");
			Display.updateLogPage(htmlcgdesc, false);
			
		}	
	}
	public static void addHighOrderImplication(World highorder, World loworder){
		if(!PSatAPI.isnextpath){
			return;
		}
		boolean exist = false;
		World key=null;
		ArrayList<World> value = null;
		for (Map.Entry<World, ArrayList<World>> entry : higherOrderKs.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    
		    if(highorder.toString().equals(key.toString())){
		    	exist = true;
		    	break;
		    }
		}
		
		if(!exist){
			ArrayList<World>loworders = new ArrayList<World>();
			loworders.add(loworder);
			higherOrderKs.put(highorder, loworders);
		}
		else{
			boolean contained = false;
			for(Object vx:value){
				
				if(vx == null && loworder == null){
					contained = true;
				}
				else{
					World v = (World)vx;
					if(v !=null && loworder !=null && v.toString().equals(loworder.toString())){
						contained = true;
					}
				}				
			}
			if(!contained){
				value.add(loworder);
				higherOrderKs.put(key, value);
			}			
		}
		
	}
	
	public static File writeGraphMlGmlToFile(String sessionid,String filename, byte [] bytes){
		String foldername0 = PSatAPI.datastore_file_path+"/filestore";
		String foldername1 = foldername0+"/"+sessionid;
		String filepath = foldername1+"/"+filename;
		
		File folder0 = new File(foldername0);
		boolean exist0 = false;
		if(folder0.exists()){
			if(folder0.isDirectory()){
				exist0 = true;
			}				
		}
		if(!exist0){
			folder0.mkdir();
		}
		
		File folder1 = new File(foldername1);
		boolean exist1 = false;
		if(folder1.exists()){
			if(folder1.isDirectory()){
				exist1 = true;
			}				
		}
		if(!exist1){
			folder1.mkdir();
		}
		File file = new File(filepath);
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file, true);
	   		for(int i=0;i<bytes.length;i++){
	    		writer.append((char)bytes[i]);
			}
	   		writer.flush();
	   		writer.close();
		} catch (IOException e) {

			e.printStackTrace();

		}
		return file;
	}

}
