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

import robot.*;

public class DriveMotorsToolbar extends JPanel {
    
    private final JLabel driveMotorLabel;
    private final JButton forwardButton;
    private final JButton reverseButton;
    private final JButton leftButton;
    private final JButton rightButton;
    private final JButton stopButton;
    private final JSlider speedSlider;
    private final JLabel speedLabel;
    
    private RobotModel robotModel;
    
    private static final int MIN_SPEED = 0;
    private static final int MAX_SPEED = 10;
    private static final int SPEED_INIT = 0;
    
    public DriveMotorsToolbar(){
        driveMotorLabel = new JLabel("Drive Motors Control:");
        forwardButton = new JButton("forward");
        reverseButton = new JButton("reverse");
        leftButton = new JButton("left");
        rightButton = new JButton("right");
        stopButton = new JButton("stop");
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
        add(driveMotorLabel,gc);

        gc.gridx = 2;
        gc.gridy = 0;
        add(forwardButton,gc);
            
        gc.gridx = 1;
        gc.gridy = 1;
        add(leftButton,gc);
        
        gc.gridx = 2;
        gc.gridy = 1;
        add(stopButton,gc);
        
        gc.gridx = 3;
        gc.gridy = 1;
        add(rightButton,gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        add(speedSlider);
        
        gc.gridx = 1;
        gc.gridy = 2;
        add(speedLabel); 
        
        gc.gridx = 2;
        gc.gridy = 2;
        add(reverseButton,gc);
        
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                robotModel.stopDriveMotors();
                speedLabel.setText("Speed: 0.0");
                speedSlider.setValue(0);
            }
        });
        
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed = (speedSlider.getValue()/10.0);
                robotModel.forward(speed);
            }
        });
        
        reverseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed = speedSlider.getValue()/10.0;
                robotModel.reverse(speed);
            }
        });
        
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed = speedSlider.getValue()/10.0;
                robotModel.turnLeft(speed);
            }
        });
       
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed = speedSlider.getValue()/10.0;
                robotModel.turnRight(speed);
            }
        });
        
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                
                if(!source.getValueIsAdjusting()){
                    double speed = source.getValue()/10.0;
                    speedLabel.setText("Speed: " + speed);
                    
                    switch(robotModel.getDriveDirection()){
                        case FORWARD: robotModel.forward(speed); break;
                        case REVERSE: robotModel.reverse(speed); break;
                        case LEFT_TURN : robotModel.turnLeft(speed); break;
                        case RIGHT_TURN: robotModel.turnRight(speed); break;
                        default: break;
                    }
                            
                }
            }
        });
    }
    
    
    public void setRobotModel(RobotModel robotModel){
        this.robotModel = robotModel;
     }
}
