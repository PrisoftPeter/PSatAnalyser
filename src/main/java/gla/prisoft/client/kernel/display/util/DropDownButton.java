package gla.prisoft.client.kernel.display.util;

import java.awt.event.*;

import javax.swing.*;

import java.awt.*;

public class DropDownButton extends AbstractButton
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   JButton actionButton;
   JToggleButton menuButton;
   JPopupMenu popupMenu;
   
   ImageIcon downarrowIcon_active = new ImageIcon(getClass().getResource("/down.jpg"));
   ImageIcon downarrowIcon_inactive = new ImageIcon(getClass().getResource("/down2.jpg"));
  
   public DropDownButton(JButton _actionButton, JPopupMenu _popupMenu) {
      this.popupMenu = _popupMenu;
      this.actionButton = _actionButton;
  
      setLayout(new BorderLayout());
      actionButton.setBorderPainted(false);
      add(BorderLayout.CENTER, actionButton);
     
      menuButton = new JToggleButton(downarrowIcon_active);
      menuButton.setPreferredSize(new Dimension(15, 10));
      add(BorderLayout.EAST, menuButton);
      menuButton.setBorderPainted(false);
   
      MouseAdapter ma = new MouseAdapter() {
         public void mouseClicked(MouseEvent me) { }
         public void mousePressed(MouseEvent me) {
            if (me.getSource() == actionButton) {
               menuButton.setSelected(true);
            }
         }
         public void mouseReleased(MouseEvent me) {
            if (me.getSource() == actionButton) {
               menuButton.setSelected(false);
            }
         }
         public void mouseEntered(MouseEvent me) {
            setRolloverBorder();
         }
         public void mouseExited(MouseEvent me) {
            unsetRolloverBorder();
         }
      };
  
      actionButton.addMouseListener(ma);
      menuButton.addMouseListener(ma);
  
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            popupMenu.show(actionButton, 0, actionButton.getSize().height);
         }
      });
   } 
  
   protected void setRolloverBorder() {
      actionButton.setBorderPainted(true);
      menuButton.setBorderPainted(true);
   }
  
   protected void unsetRolloverBorder() {
      actionButton.setBorderPainted(false);
      menuButton.setBorderPainted(false);
   }
   
   public void setEnabled(boolean enable){
	   actionButton.setEnabled(enable);
	   menuButton.setEnabled(enable);
	   popupMenu.setEnabled(enable);
	   if(!enable){
		   menuButton.setIcon(downarrowIcon_inactive);
	   }
	   else{
		   menuButton.setIcon(downarrowIcon_active);
	   }
   }
   
   
}
