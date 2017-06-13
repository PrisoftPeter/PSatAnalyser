package gla.prisoft.client.kernel.display.util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gla.prisoft.client.Display;
import gla.prisoft.client.PSatClient;
import gla.prisoft.server.PSatAPI;

public class PreferentialAttachmentSettings {

	//	public static int no_agents;
	public static int numEdgesToAttach;
	public static int init_no_seeds;
	public static int no_iterations;

	public static void configure(){

		JLabel noedges_l = new JLabel("#edges to attach per time step:");
		final JTextField noedgel_tf = new JTextField(10);

		JLabel no_seeds_l = new JLabel("#init number of unconnected 'seed':");
		final JTextField no_seeds_tf = new JTextField(10);

		JLabel no_iterations_l = new JLabel("#evolution steps:");
		final JTextField no_iterations_tf = new JTextField(10);

		Object[] message = {noedges_l,noedgel_tf,no_seeds_l,no_seeds_tf,no_iterations_l,no_iterations_tf};	

		int option= JOptionPane.showOptionDialog(Display.iframeNet,message,"Config.preferential attachment", JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION, null, new Object[]{"cancel", "ok"}, null);
		if(option==0){

		}
		else{		
			String tinput = noedgel_tf.getText();
			if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
				PSatAPI.instance.numEdgesToAttach = new Integer(tinput);	
			}
			else{
				Display.updateLogPage("#number expected", true);
				return;
			}
			
			tinput = no_seeds_tf.getText();
			if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
				PSatAPI.instance.init_no_seeds = new Integer(tinput);
			}
			else{
				Display.updateLogPage("#number expected", true);
				return;
			}
			
			tinput = no_iterations_tf.getText();
			if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
				PSatAPI.instance.no_iterations = new Integer(tinput);	
			}
			else{
				Display.updateLogPage("#number expected", true);
				return;
			}

			PSatClient.netSerialiseConfigInstance();
		}

	}

}
