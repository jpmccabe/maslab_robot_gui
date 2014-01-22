package robot;

import java.util.*;

import devices.actuators.*;
import devices.sensors.*;
import comm.*;


public class RobotModel {
    private final Cytron spiralLiftMotor;
    private final Cytron leftMotor;
    private final Cytron rightMotor;
    private final PWMOutput rollerMotor;
    private final Ultrasonic ultrasonic_1;
    private final Ultrasonic ultrasonic_2;
    private final Ultrasonic ultrasonic_3;
    private final Ultrasonic ultrasonic_4;
    private final Ultrasonic ultrasonic_5;
    private final Ultrasonic ultrasonic_6;
    private final AnalogInput irSensor_1;
    private final AnalogInput irSensor_2;
    private final AnalogInput irSensor_3;
    private final Encoder leftEncoder;
    private final Encoder rightEncoder;
    private final MapleComm comm;
    
    private static final int spiralLiftDirPin = 1;
    private static final int spiralLiftPWMPin = 0;
    private static final int leftMotorDirPin = 5;
    private static final int leftMotorPWMPin = 4;
    private static final int rightMotorDirPin = 7;
    private static final int rightMotorPWMPin = 6;
    private static final int rollerMotorPWMPin = 3;
    
    private static final int ultrasonic_1_triggerPin = 23;
    private static final int ultrasonic_1_echoPin = 37;
    private static final int ultrasonic_2_triggerPin =26;
    private static final int ultrasonic_2_echoPin = 25;
    private static final int ultrasonic_3_triggerPin = 28;
    private static final int ultrasonic_3_echoPin = 29;
    private static final int ultrasonic_4_triggerPin = 31;
    private static final int ultrasonic_4_echoPin = 30;
    private static final int ultrasonic_5_triggerPin = 33;
    private static final int ultrasonic_5_echoPin = 32;
    private static final int ultrasonic_6_triggerPin = 35;
    private static final int ultrasonic_6_echoPin = 34;
    
    private static final int IR_1_PIN = 15;
    private static final int IR_2_PIN = 16;
    private static final int IR_3_PIN = 17;
    
    private static final int leftEncoderPinA = 0;
    private static final int leftEncoderPinB = 0;
    private static final int rightEncoderPinA = 0;
    private static final int rightEncoderPinB = 0;
    
    private double spiralLiftSpeed = 0.0;
    private double driveSpeed = 0.0;
    private double rollerSpeed = 0.0;
    private DriveDirection driveDirection = DriveDirection.STATIONARY;
    private SpiralSpinDirection spiralLiftDirection = SpiralSpinDirection.STATIONARY;
    
    
    /**
     * Constructs a new RobotModel with the pins of all actuators and sensors
     * set in stone.
     */
    public RobotModel(){
        spiralLiftMotor = new Cytron(spiralLiftDirPin,spiralLiftPWMPin);
        leftMotor = new Cytron(leftMotorDirPin, leftMotorPWMPin);
        rightMotor = new Cytron(rightMotorDirPin, rightMotorPWMPin);
        rollerMotor = new PWMOutput(rollerMotorPWMPin);
        
        ultrasonic_1 = new Ultrasonic(ultrasonic_1_triggerPin, ultrasonic_1_echoPin);
        ultrasonic_2 = new Ultrasonic(ultrasonic_2_triggerPin, ultrasonic_2_echoPin);
        ultrasonic_3 = new Ultrasonic(ultrasonic_3_triggerPin, ultrasonic_3_echoPin);
        ultrasonic_4 = new Ultrasonic(ultrasonic_4_triggerPin, ultrasonic_4_echoPin);
        ultrasonic_5 = new Ultrasonic(ultrasonic_5_triggerPin, ultrasonic_5_echoPin);
        ultrasonic_6 = new Ultrasonic(ultrasonic_6_triggerPin, ultrasonic_6_echoPin);
        
        irSensor_1 = new AnalogInput(IR_1_PIN);
        irSensor_2 = new AnalogInput(IR_2_PIN);
        irSensor_3 = new AnalogInput(IR_3_PIN);
        
        leftEncoder = new Encoder(leftEncoderPinA, leftEncoderPinB);
        rightEncoder = new Encoder(rightEncoderPinA, rightEncoderPinB);
        
        comm = new MapleComm();
        comm.registerDevice(spiralLiftMotor);
        comm.registerDevice(leftMotor);
        comm.registerDevice(rightMotor);
        comm.registerDevice(rollerMotor);
        comm.registerDevice(irSensor_1);
        comm.registerDevice(irSensor_2);
        comm.registerDevice(irSensor_3);
        //comm.registerDevice(leftEncoder);
        //comm.registerDevice(rightEncoder);
        
        comm.initialize();
    }
    
    
    /**
     * Sets the speed of the roller.
     * @param speed the speed of the roller from 0.0 to 1.0.
     */
    synchronized public void setRollerSpeed(double speed){
        speed = Math.abs(speed);
        rollerMotor.setValue(speed);
        rollerSpeed = speed;
        comm.transmit();
    }
       
    
    /**
     * Stops the roller.
     */
    synchronized public void stopRoller(){
        rollerMotor.setValue(0.0);
        rollerSpeed = 0.0;
        comm.transmit();
    }

    
    /**
     * Sets the spiral lift to spin upward with the given speed.
     * @param speed the speed of the spiral from 0.0 to 1.0.
     */
    synchronized public void spinSpiralUp(double speed){
        spiralLiftMotor.setSpeed(speed);
        spiralLiftSpeed = speed;
        spiralLiftDirection = SpiralSpinDirection.UP;
        comm.transmit();
    }
    
    
    /**
     * Sets the spiral lift to spin downward with the given speed.
     * @param speed the speed of the spiral from 0.0 to 1.0.
     */
    synchronized public void spinSpiralDown(double speed){
        spiralLiftMotor.setSpeed(-1*speed);
        spiralLiftSpeed = speed;
        spiralLiftDirection = SpiralSpinDirection.DOWN;
        comm.transmit();
    }
    
    
    /**
     * Stops the spiral lift.
     */
    synchronized public void stopSpiralLift(){
        spiralLiftMotor.setSpeed(0.0);
        spiralLiftSpeed = 0.0;
        spiralLiftDirection = SpiralSpinDirection.STATIONARY;
        comm.transmit();
    }
    
    
    /**
     * Stops the drive motors. (Those with wheels).
     */
    synchronized public void stopDriveMotors(){
        leftMotor.setSpeed(0.0);
        rightMotor.setSpeed(0.0);
        driveSpeed = 0.0;
        driveDirection = DriveDirection.STATIONARY;
        comm.transmit();
    }
    
    
    /**
     * Stops every motor on the robot.
     */
    synchronized public void stopAllMotors(){
        stopRoller();
        stopSpiralLift();
        stopDriveMotors();
    }
   
    
    /**
     * Makes the robot drive forward with the given speed.
     * @param speed the speed from 0.0 to 1.0.
     */
    synchronized public void forward(double speed){
        speed = Math.abs(speed);
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);
        driveSpeed = speed;
        driveDirection = DriveDirection.FORWARD;
        comm.transmit();
    }
    
    
    /**
     * Makes the robot drive in reverse with the given speed.
     * @param speed the speed from 0.0 to 1.0.
     */
    synchronized public void reverse(double speed){
        speed = Math.abs(speed);
        leftMotor.setSpeed(-1*speed);
        rightMotor.setSpeed(-1*speed);
        driveSpeed = speed;
        driveDirection = DriveDirection.REVERSE;
        comm.transmit();
    }
    
    
    /**
     * Makes the robot turn left with the given speed.
     * @param speed the speed from 0.0 to 1.0.
     */
    synchronized public void turnLeft(double speed){
        speed = Math.abs(speed);
        leftMotor.setSpeed(-1*speed);
        rightMotor.setSpeed(speed);
        driveSpeed = speed;
        driveDirection = DriveDirection.LEFT_TURN;
        comm.transmit();
    }
    
    
    /**
     * Makes the robot turn right with the given speed.
     * @param speed the speed from 0.0 to 1.0.
     */
    synchronized public void turnRight(double speed){
        speed = Math.abs(speed);
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(-1*speed);
        driveSpeed = speed;
        driveDirection = DriveDirection.RIGHT_TURN;
        comm.transmit();
    }
    
    
    /**
     * Returns the current speed of the spiral lift.
     * @return the current speed of the lift from 0.0 to 1.0.
     */
    synchronized public double getSpiralLiftSpeed(){
        return spiralLiftSpeed;
    }
    
    /**
     * Returns the current direction of the spiral lift.
     * @return the current direction of spin of the spiral lift.
     */
    synchronized public SpiralSpinDirection getSpiralLiftDirection(){
        return spiralLiftDirection;
    }
    
    /**
     * Returns the current drive speed of the robot.
     * @return the current drive speed of the robot from 0.0 to 1.0.
     */
    synchronized public double getDriveSpeed(){
        return driveSpeed;
    }
    
    /**
     * Returns the current drive direction of the robot.
     * @return the current drive direction of the robot.
     */
    synchronized public DriveDirection getDriveDirection(){
        return driveDirection;
    }
    
    /**
     * Returns the current speed of the rubber band roller.
     * @return the current speed of the roller from 0.0 to 1.0.
     */
    synchronized public double getRollerSpeed(){
        return rollerSpeed;
    }
    
    
    /**
     * Returns the readings of all ultrasonic sensors.
     * @return list of readings such that list.get(ultrasonic_index - 1) 
     * is the sensor reading for the ultrasonic sensor with index of ultrasonic_index.
     */
    synchronized public List<Double> getUltrasonicReadings(){
        List<Double> readings = new ArrayList<Double>();
        
        comm.updateSensorData();
        readings.add(ultrasonic_1.getDistance());
        readings.add(ultrasonic_2.getDistance());
        readings.add(ultrasonic_3.getDistance());
        readings.add(ultrasonic_4.getDistance());
        readings.add(ultrasonic_5.getDistance());
        readings.add(ultrasonic_6.getDistance());
        
        return readings;
    }
    
    
    /**
     * Returns the readings of all IR sensors.
     * @return list of readings such that list.get(IRSensor_index - 1)
     * is the sensor reading for the IR sensor with the index IRSesnor_index.
     */
    synchronized public List<Float> getIRReadings(){
        List<Float> readings = new ArrayList<Float>();
        
        comm.updateSensorData();
        readings.add(irSensor_1.getValue());
        readings.add(irSensor_2.getValue());
        readings.add(irSensor_3.getValue());
        
        return readings;
    }
    
    
    /**
     * Returns all of the different readings from the drive motors'
     * encoders.
     * @return encoder data, first three items in the list are the left
     *         motor's angular speed, delta angular distance, and total angular distance.
     *         The last three items in the list are the same readings but for the
     *         right motor encoder. All angular data is in radians.
     */
    synchronized public List<Double> getEncoderReadings(){
        List<Double> readings = new ArrayList<Double>();
        
        comm.updateSensorData();
        readings.add(leftEncoder.getAngularSpeed());
        readings.add(leftEncoder.getDeltaAngularDistance());
        readings.add(leftEncoder.getTotalAngularDistance());
        readings.add(rightEncoder.getAngularSpeed());
        readings.add(rightEncoder.getDeltaAngularDistance());
        readings.add(rightEncoder.getTotalAngularDistance());
        
        return readings;
    }
}
