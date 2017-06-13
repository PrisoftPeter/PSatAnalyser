package gla.prisoft.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import gla.prisoft.shared.ConfigInstance;

public class PSatAPI {
	public static ConfigInstance instance;
	public static String datastore_file_path ="datastore";
	public static int fvindex;
	
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
