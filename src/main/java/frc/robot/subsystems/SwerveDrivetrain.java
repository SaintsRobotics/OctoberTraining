/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.trajectory.constraint.SwerveDriveKinematicsConstraint;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SwerveDrivetrain extends SubsystemBase{
  private CANSparkMax m_frontLeftDriveMotor;
  private CANSparkMax m_frontRightDriveMotor;
  private CANSparkMax m_backLeftDriveMotor;
  private CANSparkMax m_backRightDriveMotor;

  private CANSparkMax m_frontLeftTurningMotor;
  private CANSparkMax m_frontRightTurningMotor;
  private CANSparkMax m_backLeftTurningMotor;
  private CANSparkMax m_backRightTurningMotor;

  private SwerveWheel m_frontLeftSwerveWheel;
  private SwerveWheel m_frontRightSwerveWheel;
  private SwerveWheel m_backLeftSwerveWheel;
  private SwerveWheel m_backRightSwerveWheel;

  private Constants m_constants;

  private double m_xSpeed;
  private double m_ySpeed;
  private double m_rotSpeed;
  private boolean m_isFieldRelative;

  private AHRS m_gyro;

  private SwerveDriveKinematics m_kinematics;

  
  
  /**
   * Creates a new SwerveDrivetrain.
   */
  public SwerveDrivetrain(Constants constants) {
    m_constants = constants;
    m_frontLeftDriveMotor = new CANSparkMax(m_constants.m_frontLeftDriveMotorPort, MotorType.kBrushless);
    m_frontRightDriveMotor = new CANSparkMax(m_constants.m_frontRightDriveMotorPort, MotorType.kBrushless);
    m_backLeftDriveMotor = new CANSparkMax(m_constants.m_backLeftDriveMotorPort, MotorType.kBrushless);
    m_backRightDriveMotor = new CANSparkMax(m_constants.m_backRightDriveMotorPort, MotorType.kBrushless);
    
    m_frontLeftTurningMotor = new CANSparkMax(m_constants.m_frontLeftTurningMotorPort, MotorType.kBrushless);
    m_frontRightTurningMotor = new CANSparkMax(m_constants.m_frontRightTurningMotorPort, MotorType.kBrushless);
    m_backLeftTurningMotor = new CANSparkMax(m_constants.m_backLeftTurningMotorPort, MotorType.kBrushless);
    m_backRightTurningMotor = new CANSparkMax(m_constants.m_backRightTurningMotorPort, MotorType.kBrushless);
  
  
    m_frontLeftDriveMotor.setInverted(true);
    m_backLeftDriveMotor.setInverted(true);
    m_frontRightDriveMotor.setInverted(false);
    m_backRightDriveMotor.setInverted(false);

    m_frontLeftSwerveWheel = new SwerveWheel(m_frontLeftDriveMotor, m_frontLeftTurningMotor, -m_constants.swerveX, m_constants.swerveY);
    m_backLeftSwerveWheel = new SwerveWheel(m_backLeftDriveMotor, m_backLeftTurningMotor, -m_constants.swerveX, -m_constants.swerveY);
    m_frontRightSwerveWheel = new SwerveWheel(m_frontRightDriveMotor, m_frontRightTurningMotor, m_constants.swerveX, m_constants.swerveY);
    m_backRightSwerveWheel = new SwerveWheel(m_backRightDriveMotor, m_backRightTurningMotor, m_constants.swerveX, -m_constants.swerveY);
  
    m_kinematics = new SwerveDriveKinematics(m_frontLeftSwerveWheel.getLocation(), m_frontRightSwerveWheel.getLocation(), m_backLeftSwerveWheel.getLocation(), m_backRightSwerveWheel.getLocation());
  
    m_gyro = new AHRS();
  }

public void move(double xSpeed, double ySpeed, double rotSpeed, Boolean isFieldRelative){
  m_xSpeed = xSpeed;
  m_ySpeed = ySpeed;
  m_rotSpeed = rotSpeed;
  m_isFieldRelative = isFieldRelative;
}



@Override
public void periodic() {
  // This method will be called once per scheduler run
  SwerveModuleState[] swerveModuleStates;
  if(m_isFieldRelative){
    swerveModuleStates = m_kinematics.toSwerveModuleStates(
      ChassisSpeeds.fromFieldRelativeSpeeds(m_xSpeed, m_ySpeed, m_rotSpeed, new Rotation2d(2* Math.PI - ((Math.toRadians(m_gyro.getAngle()) %(Math.PI *2)) + (Math.PI *2)) %(Math.PI *2))));
  }else{  
  swerveModuleStates = m_kinematics.toSwerveModuleStates(new ChassisSpeeds(m_xSpeed, m_ySpeed, m_rotSpeed));
  }
  m_kinematics.normalizeWheelSpeeds(swerveModuleStates, m_constants.maxMetersPerSecond);
  
  m_frontLeftSwerveWheel.setDesiredState(swerveModuleStates[0]);
  m_frontRightSwerveWheel.setDesiredState(swerveModuleStates[1]);
  m_backLeftSwerveWheel.setDesiredState(swerveModuleStates[2]);
  m_backRightSwerveWheel.setDesiredState(swerveModuleStates[3]);

  

}

}
