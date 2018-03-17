package gla.client.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import gla.client.Display;

public class ClientConfig {
		
	@SuppressWarnings("unchecked")
	public static ArrayList<String> deserialiseSessionIds(){
		String fileName2 = Display.sessionids_store_file_path+"/sessionids.ser";
		ArrayList<String> sessionids = null;
		
		try {
			File file = new File(fileName2);

			if(file.exists()){
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				sessionids = (ArrayList<String>) in.readObject();
				in.close();
				fileIn.close();			
			}
			
		} 
		catch (IOException i) {
			System.err.println("IO exception @deserialiseSessionIds");
		} 
		catch (ClassNotFoundException c) {
			System.err.println("String class not found");
		}
		finally{			
		}
		
		return sessionids;
	}
	
	public static boolean serialiseSessionIds(ArrayList<String> sessionids){
						
		String folderName1 = Display.sessionids_store_file_path;
		String fileName2 = Display.sessionids_store_file_path+"/sessionids.ser";	
		
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
	
				
			File if_file = new File(fileName2);
	        if(if_file.exists()){
	         if_file.delete();
	        }
	        if_file.createNewFile();
	        FileOutputStream fileOut = new FileOutputStream(fileName2);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(sessionids);
	        out.close();
	        fileOut.close();	         
	      }
		catch(IOException i){
	          i.printStackTrace();
	    }
		
		return true;
	}
}
