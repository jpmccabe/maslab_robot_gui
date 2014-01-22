package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import robot.*;

public class EncoderToolbar extends JPanel{
    
    private final JTable encoderTable;
    private final DefaultTableModel tableModel;
    private RobotModel robotModel;
    private final static int timeBetweenUpdates = 100;
    
    
    public EncoderToolbar(){
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Encoder");
        tableModel.addColumn("Angular Speed");
        tableModel.addColumn("Delta Anglular Distance");
        tableModel.addColumn("Total AngularDistance");
        
        tableModel.addRow(new Object[]{"Left",0,0,0});
        tableModel.addRow(new Object[]{"Right",0,0,0});
        
        encoderTable = new JTable(tableModel);
        encoderTable.setName("Encoder Data");
        encoderTable.getTableHeader().setVisible(true);
        encoderTable.setEnabled(false);
        
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
        add(encoderTable,gc);
    }
    
    /**
     * Starts a new thread to read the encoders in the given robotModel
     * and update the table in the toolbar at a few hertz.
     * @param robotModel the RobotModel this toolbar should be associated with.
     */
    public void setRobotModel(final RobotModel robotModel){
        Thread updateReadings = new Thread(new Runnable(){
            public void run(){
                while(true){
                    final List<Double> readings = robotModel.getEncoderReadings();

                    SwingUtilities.invokeLater(new Runnable(){
                        public void run(){
                            for(int i=0; i < 3; i++){
                                tableModel.setValueAt(readings.get(i), 0, i);
                                tableModel.setValueAt(readings.get(i+3), 1, i);
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
