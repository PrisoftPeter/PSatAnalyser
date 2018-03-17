package psat.display.util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import psat.Display;
import psat.PSatAPI;

public class KleinbergSmallWorldSettings {
//	public static int no_agents;
	public static int no_edges;
	public static double clusteringExponent;
	
	public static void configure(){
		
		JLabel noedges_l = new JLabel("#edges:");
		final JTextField noedgel_tf = new JTextField(10);
		
		JLabel clusteringExponent_l = new JLabel("#clusteringExponent:");
		final JTextField clusteringExponent_tf = new JTextField(10);
				
		Object[] message = {noedges_l,noedgel_tf,clusteringExponent_l,clusteringExponent_tf};	



		int option= JOptionPane.showOptionDialog(Display.iframeNet,message,"Config.smallworlds", JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION, null, new Object[]{"cancel", "ok"}, null);
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
			
			tinput = clusteringExponent_tf.getText();
			if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
				PSatAPI.instance.clusteringExponent = new Double(tinput);
			}
			else{
				Display.updateLogPage("#number expected", true);
				return;
			}
						
			PSatAPI.netSerialiseConfigInstance();
		}
	}
}
