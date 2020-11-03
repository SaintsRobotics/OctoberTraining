/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.trajectory.Trajectory.State;
import frc.robot.AbsoluteEncoder;
import frc.robot.Constants;

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

    private double smartInversion(SwerveModuleState targetState) {
        double diff = Math.abs(m_turningEncoder.getRadians() - targetState.angle.getRadians());
        double targetHeading = targetState.angle.getRadians();
        // Making sure we have the smallest difference of the two paths around the
        // circle
        if (diff > Math.PI) {
            diff = Math.PI * 2 - diff;
        }
        // inverting the velocity to achieve a shorter path
        if (diff > Math.PI / 2) {
            targetHeading += Math.PI;
            targetHeading %= (Math.PI * 2);
            targetState.speedMetersPerSecond *= -1;
        }

        m_turningPIDController.setSetpoint((targetHeading % (Math.PI * 2) + (Math.PI * 2)) % (Math.PI * 2));
        return targetState.speedMetersPerSecond;
    }

    public void setDesiredState(SwerveModuleState state) {

        double driveOutput = smartInversion(state);
        m_driveMotor.set(driveOutput / m_constants.maxMetersPerSecond);
        double pidOutput = m_turningPIDController.calculate(m_turningEncoder.getRadians());

        m_turningMotor.set(pidOutput);

    }

}
