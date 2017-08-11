package gla.server.kernel.util;

import java.io.InputStream;
import java.net.URL;

final public class ResourceLoader {
	
	public static InputStream load(String path){
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		
		if(input == null){
			input = ResourceLoader.class.getResourceAsStream("/"+path);
		}
		return input;
	}
	
	public static URL load1(String path){
		URL url = ResourceLoader.class.getResource(path);
		
		if(url == null){
			url = ResourceLoader.class.getResource("/"+path);
		}
		return url;
	}
}
