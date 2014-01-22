package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import robot.RobotModel;

public class IRToolbar extends JPanel{
    private final JTable IRTable;
    private final DefaultTableModel tableModel;
    private final static int timeBetweenUpdates = 100;
    
    public IRToolbar(){
        tableModel = new DefaultTableModel();
        tableModel.addColumn("IR Sensor");
        tableModel.addColumn("Sensor Reading");
        
        for(int i= 1; i<= 3; i++){
            tableModel.addRow(new Object[]{i,0});
        }
        
        IRTable = new JTable(tableModel);
        IRTable.setName("IR Values");
        IRTable.getTableHeader().setVisible(true);
        IRTable.setEnabled(false);
        
               
        Border border = BorderFactory.createLineBorder(new Color(0,0,0));
        setBorder(border);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        add(IRTable,gc);
        
    }
    
    
    /**
     * Starts a new thread to read the IR sensors in the given robotModel
     * and update the table in the toolbar at a few hertz.
     * @param robotModel the RobotModel this toolbar should be associated with.
     */
    public void setRobotModel(final RobotModel robotModel){
        Thread updateReadings = new Thread(new Runnable(){
            public void run(){
                while(true){
                    final List<Float> readings = robotModel.getIRReadings();
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run(){
                            for(int i=0; i < 3; i++){
                                tableModel.setValueAt(readings.get(i), i, 1);
                                tableModel.fireTableDataChanged();
                            }
                        }
                    });
                    try {
                        Thread.sleep(timeBetweenUpdates);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        updateReadings.start();
     }
    
}