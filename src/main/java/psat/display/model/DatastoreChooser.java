package psat.display.model;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;


public class DatastoreChooser extends JPanel{
	   
	  /**
	 * 
	 */
	private static final long serialVersionUID = -1839312863439654495L;

	JFileChooser chooser;
	   
	  static String choosen_dir;
	   
	  public DatastoreChooser(String choosertitle, boolean isopen) {
		    		  
		  chooser = new JFileChooser(); 
		  if(FeasibilityView.export_file_path != null){
			  chooser.setCurrentDirectory(new java.io.File(FeasibilityView.export_file_path));
		  }
		  else{
			  chooser.setCurrentDirectory(new java.io.File("."));			  
		  }
		  chooser.setDialogTitle(choosertitle);
		  chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		  chooser.setFileFilter( new FileFilter(){
              @Override
              public boolean accept(File f) {
                  return f.isDirectory();
              }
              @Override
              public String getDescription() {
                  return "Any folder";
              }
          });
		  chooser.setAcceptAllFileFilterUsed(false);
		  //  
		  if(isopen){
//			  chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			  if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
				  choosen_dir = chooser.getSelectedFile().toString();
			  }
		  }
		  else{
//			  chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			  if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { 
				File datastore = chooser.getSelectedFile();

				boolean exist1 = false;
				if (datastore.exists()) {
					if (datastore.isDirectory()) {
						exist1 = true;
					}
				}
				if (!exist1) {
					datastore.mkdir();
				}
					
				choosen_dir = chooser.getSelectedFile().toString();
			  }
		  }
		  
	   }
	    
	  public static String show(String choosertitle, boolean isopen) {
	    new DatastoreChooser(choosertitle, isopen);    
	    return choosen_dir;
	    }
	  
	  public static void main(String args[]){
		    new DatastoreChooser("testing", false);    

	  }
	  
	}
