/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import frc.robot.AbsoluteEncoder;
import frc.robot.Constants;

import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;

/**
 * Add your docs here.
 */
public class SwerveWheel {
    private CANSparkMax m_driveMotor;
    private CANSparkMax m_turningMotor;
    private Translation2d m_location;
    private PIDController m_turningPIDController;
    private AbsoluteEncoder m_turningEncoder;
    private Constants m_constants;

    public SwerveWheel(CANSparkMax driveMotor, CANSparkMax turningMotor, double x, double y, AbsoluteEncoder encoder,
            Constants constants) {
        m_driveMotor = driveMotor;
        m_turningMotor = turningMotor;
        m_location = new Translation2d(x, y);
        m_turningPIDController = new PIDController(.3, 0, 0);
        m_turningPIDController.enableContinuousInput(0, 2 * Math.PI);
        m_turningEncoder = encoder;
        m_constants = constants;

    }

    public Translation2d getLocation() {
        return m_location;

    }

    public void setDesiredState(SwerveModuleState state) {
        m_driveMotor.set(state.speedMetersPerSecond / m_constants.maxMetersPerSecond);

        m_turningPIDController.setSetpoint(state.angle.getRadians());
        double pidOutput = m_turningPIDController.calculate(m_turningEncoder.getRadians());

        m_turningMotor.set(pidOutput);

    }

}
