/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.subsystems.SwerveWheel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import com.kauailabs.navx.frc.AHRS;
public class SwerveDrivetrain extends SubsystemBase {
  private Constants m_constants;
  //front left
  private CANSparkMax m_frontLeftDrive;
  private CANSparkMax m_frontLeftTurn;
  //front right
  private CANSparkMax m_frontRightDrive;
  private CANSparkMax m_frontRightTurn;
  //back left
  private CANSparkMax m_backLeftDrive;
  private CANSparkMax m_backLeftTurn;
  //back right
  private CANSparkMax m_backRightDrive;
  private CANSparkMax m_backRightTurn;
  //highlight/ctrl d to change multiple

  private SwerveWheel m_frontLeftSwerveWheel;
  private SwerveWheel m_frontRightSwerveWheel;
  private SwerveWheel m_backLeftSwerveWheel;
  private SwerveWheel m_backRightSwerveWheel;

  //move values
  private double m_xSpeed;
  private double m_ySpeed;
  private double m_rotSpeed;
  private boolean m_isFieldRelative;
  private SwerveDriveKinematics m_kinematics;

  private AHRS m_gyro;
  /**
   * Creates a new SwerveDrivetrain.
   */
  public SwerveDrivetrain(Constants constants) {
    m_constants = constants;

    m_frontLeftDrive = new CANSparkMax(m_constants.m_frontLeftDrivePort, Motortype.kBrushless);
    m_frontLeftTurn = new CANSparkMax(m_constants.m_frontLeftTurnPort, Motortype.kBrushless);

    m_frontRightDrive = new CANSparkMax(m_constants.m_frontRightDrivePort, Motortype.kBrushless);
    m_frontRightTurn = new CANSparkMax(m_constants.m_frontRightTurnPort, Motortype.kBrushless);

    m_backLeftDrive = new CANSparkMax(m_constants.m_backLeftDrivePort, Motortype.kBrushless);
    m_backLeftTurn = new CANSparkMax(m_constants.m_backLeftTurnPort, Motortype.kBrushless);

    m_backRightDrive = new CANSparkMax(m_constants.m_backRightDrivePort, Motortype.kBrushless);
    m_backRightTurn = new CANSparkMax(m_constants.m_backRightTurnPort, Motortype.kBrushless);

    m_frontLeftDrive.setInverted(true);
    m_backLeftDrive.setInverted(true);
    //constructing swerve wheel using two motors
    m_frontLeftSwerveWheel = new SwerveWheel(m_frontLeftDrive, m_frontLeftTurn, -m_constants.swerveX, m_constants.swerveY, m_constants);
    m_frontRightSwerveWheel = new SwerveWheel(m_frontRightDrive, m_frontRightTurn, m_constants.swerveX, m_constants.swerveY, m_constants);
    m_backLeftSwerveWheel = new SwerveWheel(m_backLeftDrive, m_backLeftTurn, -m_constants.swerveX, -m_constants.swerveY, m_constants);
    m_backRightSwerveWheel = new SwerveWheel(m_backRightDrive, m_backRightTurn, m_constants.swerveX, -m_constants.swerveY, m_constants);
  
    //because needs positions of all wheels, add get pose to swerve wheel class
    m_kinematics = new SwerveDriveKinematics(m_frontLeftSwerveWheel.getPosition(), m_frontRightSwerveWheel.getPosition(), m_backLeftSwerveWheel.getPosition(), m_backRightSwerveWheel.getPosition());
  
    m_gyro = new AHRS();
  }



public void move(double xSpeed, double ySpeed, double rotSpeed, isFieldRelative){
  m_xSpeed = xSpeed;
  m_ySpeed = ySpeed;
  m_rotSpeed = rotSpeed;
  m_isFieldRelative = isFieldRelative;
}



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SwerveModuleState[] swerveModuleStates; //holds what wheels need to do
    if(m_isFieldRelative){
      swerveModuleStates = m_kinematics.toSwerveModuleStates.(
        ChassisSpeeds.fromFieldRelativeSpeeds(m_xSpeed, m_ySpeed, m_rotSpeed, new Rotation2d(2*Math.PI - Math.toRadians(m_gyro.getAngle))%(Math.PI*2) + ((2*Math.PI)%(2*Math.PI)))
      );
    }
    else{
      swerveModuleStates = m_kinematics.toSwerveModuleStates(new ChassisSpeeds(m_xSpeed, m_ySpeed, m_rotSpeed)); //swervemodulestates -> converts m/s and rotation speed swerve must do, unique speeds are found with translation2d pose
    }
   
    
    m_kinematics.normalizeWheelSpeeds(swerveModuleStates, m_constants.maxMetersPerSecond); //max speed, converts raw m/s to scaled m/s

    m_frontLeftSwerveWheel.setDesiredState(swerveModuleState[0]); 
    m_frontRightSwerveWheel.setDesiredState(swerveModuleState[1]);
    m_backLeftSwerveWheel.setDesiredState(swerveModuleState[2]);
    m_backRightSwerveWheel.setDesiredState(swerveModuleState[3]);
  }
}
