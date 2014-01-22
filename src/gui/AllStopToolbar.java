package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import robot.RobotModel;

public class AllStopToolbar extends JPanel {
    
    private final JButton allStopButton;
    private RobotModel robotModel;
    
    public AllStopToolbar(){
        allStopButton = new JButton("All Stop");
        
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
        gc.fill = GridBagConstraints.BOTH;
        add(allStopButton,gc);
        
        allStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                robotModel.stopAllMotors();
            }
        });
    }
    
    
    public void setRobotModel(RobotModel robotModel){
        this.robotModel = robotModel;
    }
}
