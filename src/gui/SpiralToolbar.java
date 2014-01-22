package gui;

import robot.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpiralToolbar extends JPanel {
    private final JLabel spiralLabel;
    private final JButton upSpinButton;
    private final JButton downSpinButton;
    private final JButton stopButton;
    private final JSlider speedSlider;
    private final JLabel speedLabel;
    private RobotModel robotModel;
    
    private static final int MIN_SPEED = 0;
    private static final int MAX_SPEED = 10;
    private static final int SPEED_INIT = 0;
    
    
    public SpiralToolbar(){
        spiralLabel = new JLabel("Spiral Lift Controls:");
        upSpinButton = new JButton("Spin Up");
        downSpinButton = new JButton("Spin Down");
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
        add(spiralLabel,gc);

        gc.gridx = 1;
        add(upSpinButton,gc);
        
        gc.gridx = 2;
        add(downSpinButton,gc);
        
        gc.gridx = 3;
        add(stopButton,gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        add(speedSlider,gc);
        
        gc.gridx = 1;
        add(speedLabel,gc);
        
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                robotModel.stopSpiralLift();
                speedLabel.setText("Speed: 0.0");
                speedSlider.setValue(0);
            }
        });
        
        upSpinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed = (speedSlider.getValue()/10.0);
                robotModel.spinSpiralUp(speed);
            }
        });
        
        downSpinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed = -1*(speedSlider.getValue()/10.0);
                robotModel.spinSpiralDown(speed);
            }
        });
       
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting()){
                    double speed = source.getValue()/10.0;
                    speedLabel.setText("Speed: " + speed);
                    
                    switch(robotModel.getSpiralLiftDirection()){
                    case UP: robotModel.spinSpiralUp(speed); break;
                    case DOWN: robotModel.spinSpiralDown(speed); break;
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
