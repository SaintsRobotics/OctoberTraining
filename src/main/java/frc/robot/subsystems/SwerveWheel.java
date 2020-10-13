/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.geometry.Translation2d;

public class SwerveWheel {
    private CANSparkMax m_driveMotor;
    private CANSparkMax m_turningMotor;
    private Translation2d m_location;
  
  /**
   * Creates a new SwerveDrivetrain.
   */
  public SwerveWheel(CANSparkMax driveMotor, CANSparkMax turningMotor, double x, double y) {
      m_driveMotor = driveMotor;
      m_turningMotor = turningMotor;
      m_location = new Translation2d(x, y);
  }

  public Translation2d getLocation() {
    return m_location;
  }

  
}
