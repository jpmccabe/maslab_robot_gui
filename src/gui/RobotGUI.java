package gui;

import java.awt.BorderLayout;
import robot.*;
import javax.swing.*;

public class RobotGUI extends JFrame{
    private final SpiralToolbar spiralToolbar;
    private final RollerToolbar rollerToolbar;
    private final DriveMotorsToolbar driveMotorsToolbar;
    private final AllStopToolbar allStopToolbar;
    private final IRToolbar irToolbar;
    private final RobotModel robotModel;
  
    
    public RobotGUI(){
        spiralToolbar = new SpiralToolbar();
        rollerToolbar = new RollerToolbar();
        driveMotorsToolbar = new DriveMotorsToolbar();
        allStopToolbar = new AllStopToolbar();
        irToolbar = new IRToolbar();

        this.add(spiralToolbar,BorderLayout.NORTH);
        this.add(rollerToolbar, BorderLayout.WEST);
        this.add(irToolbar, BorderLayout.CENTER);
        this.add(driveMotorsToolbar,BorderLayout.SOUTH);
        this.add(allStopToolbar, BorderLayout.EAST);
        
        robotModel = new RobotModel();
        
        this.setTitle("Robot Mission Control");
        this.pack();   
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        spiralToolbar.setRobotModel(robotModel);
        rollerToolbar.setRobotModel(robotModel);
        driveMotorsToolbar.setRobotModel(robotModel);
        allStopToolbar.setRobotModel(robotModel);
        irToolbar.setRobotModel(robotModel);
    }
    
    
    
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RobotGUI main = new RobotGUI();
                main.setVisible(true);
            }
        });
        
    }
}
