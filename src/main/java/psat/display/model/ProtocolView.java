package psat.display.model;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import psat.behaviour.protocol.ProtocolFactory;

import java.util.Vector;

import javax.swing.JSplitPane;

public class ProtocolView extends JPanel {
	private static final long serialVersionUID = -4185867590936639241L;

	public JTable table;
	public ProtocolsTableModel model;
	
	/**
	 * Create the panel.
	 */
	
	public ProtocolView() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0,0));
		
		JPanel panelaw1 = new JPanel(new BorderLayout(0,0));
				
		ImageIcon protocolSuiteIcon = new ImageIcon(getClass().getResource("/protocolsuite.png"));
		JLabel protocolSuiteLabel =new JLabel();
		protocolSuiteLabel.setIcon(protocolSuiteIcon);
		protocolSuiteLabel.setHorizontalAlignment(JLabel.CENTER);
		protocolSuiteLabel.setVerticalAlignment(JLabel.CENTER);
		panelaw1.setBackground(Color.white);
		panelaw1.add(protocolSuiteLabel, BorderLayout.CENTER);
	    
		final JCheckBox selectAll= new JCheckBox("select all");
		selectAll.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
        	  int rowCount = model.getRowCount();
        	  if(selectAll.isSelected()){
        		  for(int i=0;i<rowCount;i++){
            		  model.setManualValueAt(true, i,1);
            	  }  
        	  }
        	  else{
        		  for(int i=0;i<rowCount;i++){
            		  model.setManualValueAt(false, i,1);
            	  }
        	  }
        	  
          }          
		});
		panelaw1.add(selectAll, BorderLayout.SOUTH);
	    
		
		model = new ProtocolsTableModel();
						
		model.setRowCount(0);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -8305142193885321738L;
			@Override
	        public Component getTableCellRendererComponent(JTable table, 
	                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, 
	                value, isSelected, hasFocus, row, column);
	            c.setBackground(row%2==0 ? Color.white : new Color(226,225,213));                        
	            return c;
	        };
	    });
		table.setFont(new Font("Verdana", Font.PLAIN, 10));
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 10));
		
		TableColumn column = null;
        for (int i = 0; i < 3; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) 
                column.setMaxWidth(35);
            if (i == 1)
                column.setMaxWidth(40);
            if (i == 2)
                column.setMaxWidth(6000);
        }
				
		JPanel panelaw2 = new JPanel(new BorderLayout(0,0));
		
		
		JScrollPane scrollPane_1 = new JScrollPane(table);
		panelaw2.add(scrollPane_1, BorderLayout.CENTER);
//		scrollPane_1.setViewportView(viableKSTable);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,panelaw1, panelaw2);
		add(splitPane, BorderLayout.CENTER);
		
	}
	
	
	 public class ProtocolsTableModel extends DefaultTableModel {
		private static final long serialVersionUID = -4775356027813366490L;
			
			public ProtocolsTableModel() {
		      super(new Object[]{"No", "", "disclosure protocol"}, 0);
		    }
			
		    @SuppressWarnings("rawtypes")
			@Override
		    public Class<?> getColumnClass(int columnIndex) {
		      Class clazz = String.class;
		      switch (columnIndex) {
		        case 0:
		          clazz = Integer.class;
		          break;
		        case 1:
		          clazz = Boolean.class;
		          break;
		        case 2:
			      clazz = String.class;
			      break;
		      }
		      return clazz;
		    }

		    @Override
		    public boolean isCellEditable(int row, int column) {
		      return column == 1;
		    }

		    @SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
		    public void setValueAt(Object aValue, int row, int column) {
		      if (aValue instanceof Boolean && column == 1) {
		        boolean b = new Boolean(aValue.toString()).booleanValue();

		        Vector rowData = (Vector)getDataVector().get(row);
		        
		        String p = (String)rowData.get(2);
		        
		        if(p !=null){
		        	if(b){
//		        		PSatClient.netAddToEvaluatedProtocols(p);
		        		ProtocolFactory.addToEvaluatedProtocols(p);	
			        }
			        else{
//			        	PSatClient.netRemoveFromEvaluatedProtocols(p);
		        		ProtocolFactory.removeFromEvaluatedProtocols(p);	

			        }
		        }
		        rowData.set(1, (Boolean)aValue);
		        fireTableCellUpdated(row, column);
		      }
		    }
		    
		    @SuppressWarnings({ "unchecked", "rawtypes" })
			public void setManualValueAt(boolean value, int row, int column) {
			      if (column == 1) {
			        Vector rowData = (Vector)getDataVector().get(row);
			        
			        String p = (String)rowData.get(2);
			        
			        if(p !=null){
			        	if(value){
//			        		PSatClient.netAddToEvaluatedProtocols(p);
			        		ProtocolFactory.addToEvaluatedProtocols(p);	
				        }
				        else{
//				        	PSatClient.netRemoveFromEvaluatedProtocols(p);
			        		ProtocolFactory.removeFromEvaluatedProtocols(p);	
				        }
			        }
			        rowData.set(1,value);
			        fireTableCellUpdated(row, column);
			      }
			    }

		  }
	
}
