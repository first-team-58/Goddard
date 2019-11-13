/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 2;
  private static final int kRearLeftChannel = 0;
  private static final int kFrontRightChannel = 3;
  private static final int kRearRightChannel = 1;

  private static final int kJoystickChannel = 0;

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;

  private AnalogInput m_gyro;
  private AnalogInput m_angleSensor;

  private Talon m_liftFront;
  private Talon m_liftRear;

  @Override
  public void robotInit() {
    Talon frontLeft = new Talon(kFrontLeftChannel);
    Talon rearLeft = new Talon(kRearLeftChannel);
    Talon frontRight = new Talon(kFrontRightChannel);
    Talon rearRight = new Talon(kRearRightChannel);

    m_liftFront = new Talon(7);
    m_liftRear = new Talon(6);

    // Invert the left side motors.
    // You may need to change or remove this to match your robot.
    frontLeft.setInverted(true);
    rearLeft.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
    m_stick = new Joystick(kJoystickChannel);
    m_gyro = new AnalogInput(0);
    m_angleSensor = new AnalogInput(1);

    SmartDashboard.putData("Gyro Sensor", m_gyro);
    SmartDashboard.putData("Angle Sensor", m_angleSensor);

    SmartDashboard.putData("Lift Front Motor", m_liftFront);
    SmartDashboard.putData("Lift Rear Motor", m_liftRear);
  }

  @Override
  public void teleopPeriodic() {
    // left stick
    //   left 0 LX axis -1
    //   right 0 LX axis 1
    //   up    1 LY axis -1
    //   down  1 LY axis 1
    // Left Trigger axis 2 0->1
    // Right Trigger axis 3 0->1
    // A button = 1
    // Y button = 4

    double xSpeed = m_stick.getX();
    double ySpeed = m_stick.getY();
    double rotation = m_stick.getZ() + m_stick.getThrottle() * -1;

    // Reverse x-stick values
    xSpeed *= -1;

    // Limit speed
    xSpeed *= 0.5;
    ySpeed *= 0.5;
    rotation *= 0.5;

    if (Math.abs(xSpeed) < 0.1)
    {
      xSpeed = 0;
    }

    if (Math.abs(ySpeed) < 0.1)
    {
      ySpeed = 0;
    }

    if (Math.abs(rotation) < 0.1)
    {
      rotation = 0;
    }

    m_robotDrive.driveCartesian(xSpeed, ySpeed, rotation, 0.0);

    boolean downButton = m_stick.getRawButton(1);
    boolean upButton = m_stick.getRawButton(4);

    if (upButton)
    {
      m_liftRear.set(0.5);
      m_liftFront.set(0.5);
    }
    else if (downButton)
    {
      m_liftRear.set(-0.5);
      m_liftFront.set(-0.5);
    }
    else
    {
      m_liftRear.stopMotor();
      m_liftFront.stopMotor();
    }
  }
}
