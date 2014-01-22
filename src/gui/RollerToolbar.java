package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import robot.RobotModel;

public class RollerToolbar extends JPanel {
    private final JLabel rollerLabel;
    private final JButton stopButton;
    private final JSlider speedSlider;
    private final JLabel speedLabel;
    
    private RobotModel robotModel;
    
    private static final int MIN_SPEED = 0;
    private static final int MAX_SPEED = 10;
    private static final int SPEED_INIT = 0;
    
    
    
    public RollerToolbar(){
        rollerLabel = new JLabel("Roller Controls:");
        stopButton = new JButton("Stop");
        speedSlider = new JSlider(JSlider.HORIZONTAL, MIN_SPEED, MAX_SPEED, SPEED_INIT);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedLabel = new JLabel("Speed: 0.0");
        
        Border border = BorderFactory.createLineBorder(new Color(0,0,0));
        setBorder(border);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.weightx = 1;
        gc.weighty = 1;
        
        gc.gridx = 0;
        gc.gridy = 0;
        add(rollerLabel,gc);
    
        gc.gridx = 1;
        add(stopButton,gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        add(speedSlider,gc);
        
        gc.gridx = 1;
        add(speedLabel,gc);
        
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                robotModel.stopRoller();
                speedLabel.setText("Speed: 0.0");
                speedSlider.setValue(0);
            }
        });
           
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting()){
                    double speed = source.getValue()/10.0;
                    speedLabel.setText("Speed: " + speed);
                    robotModel.setRollerSpeed(speed);
                }
            }
        });
    }
    
   
    
    public void setRobotModel(RobotModel robotModel){
        this.robotModel = robotModel;
     }
}
