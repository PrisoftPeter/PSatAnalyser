package gla.client.kernel.display.model;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class KProgress extends JPanel {
	private static final long serialVersionUID = -792351501911847629L;

	JProgressBar pbar;

	  static final int MY_MINIMUM = 0;

	  static final int MY_MAXIMUM = 100;

	  public KProgress() {
	    // initialize Progress Bar
	    pbar = new JProgressBar();
//	    pbar.setBackground(Color.darkGray);
	    pbar.setMinimum(MY_MINIMUM);
	    pbar.setMaximum(MY_MAXIMUM);
	    pbar.setBorderPainted(false);
//	    setBackground(Color.darkGray);
	    // add to JPanel
	    add(pbar);
	    
	  }

	  public void updateBar(int newValue) {
		pbar.setIndeterminate(false);
	    pbar.setValue(newValue);
	  }
	  
	  public void updateBar(){
		pbar.setIndeterminate(true);
	  }
	  public static void main(String args[]) {

	    final KProgress it = new KProgress();

	    JFrame frame = new JFrame("kcore");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setContentPane(it);
	    frame.pack();
	    frame.setVisible(true);

	    // run a loop to demonstrate raising
	    for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
	      final int percent = i;
	      try {
	        SwingUtilities.invokeLater(new Runnable() {
	          public void run() {
	            it.updateBar(percent);
	          }
	        });
	        java.lang.Thread.sleep(100);
	      } catch (InterruptedException e) {
	        ;
	      }
	    }
	  }
	}