package gla.prisoft.client.kernel.display.util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gla.prisoft.client.Display;
import gla.prisoft.client.PSatClient;
import gla.prisoft.server.PSatAPI;

public class EppsteinPowerLawSettings {
//	public static int no_agents;
	public static int no_edges;
	public static int degreeExponent;
	
	public static void configure(){
				
		JLabel noedges_l = new JLabel("#edges:");
		final JTextField noedgel_tf = new JTextField(10);
		
		JLabel degreeExponent_l = new JLabel("#degreeExponent:");
		final JTextField degreeExponent_tf = new JTextField(10);
		
		
		Object[] message = {noedges_l,noedgel_tf,degreeExponent_l,degreeExponent_tf};	

		int option= JOptionPane.showOptionDialog(Display.iframeNet,message,"Config.powerlaw", JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION, null, new Object[]{"cancel", "ok"}, null);
		if(option==0){

		}
		else{
			String tinput = noedgel_tf.getText();
			if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
				PSatAPI.instance.no_edges = new Integer(tinput);		
			}
			else{
				Display.updateLogPage("#number expected", true);
				return;
			}
			
			tinput = degreeExponent_tf.getText();
			if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
				PSatAPI.instance.degreeExponent = new Integer(tinput);
			}
			else{
				Display.updateLogPage("#number expected", true);
				return;
			}

			PSatClient.netSerialiseConfigInstance();
		}
	}
}
