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
import frc.robot.AbsoluteEncoder;

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

    public SwerveWheel(CANSparkMax driveMotor, CANSparkMax turningMotor, double x, double y, Constants constants, AbsoluteEncoder encoder){
        m_driveMotor = driveMotor;
        m_turningMotor = turningMotor;
        m_location = new Translation2d(x, y);
        m_turningPIDController = new PIDController(.3, 0, 0);
        m_turningPIDController.enableContinuousInput(0, 2*Math.PI);
        m_turningEncoder = encoder;
        m_constants = constants;
        //issue with above encoder, need hardware signal, need to make own encoder

    }

    public Translation2d getLocation() {
        return m_location;

    }

    private double smartInversion(SwerveModuleState targetState){ //state holds angle and velocity
      
        double diff = Math.abs(m_turningEncoder.getRadians() - targetState.angle.getRadians());
        double targetHeading = targetState.angle.getRadians(); //angle of swervemodulestate
        if(diff > Math.PI){ //see which way to turn (right/left) to get to ideal spot, use smaller difference
        //if over 180 degrees away in on direction, then 2pi - angle is between 180 and 0 degrees away on the other direction
            diff = 2*Math.PI - diff; /

        }
        //if easier to go other direction, invert velocity for shorter path
        if(diff > Math.PI/2){ //if below 90, can just turn normally, go in if not under 90
            targetHeading += Math.PI; //flip the target heading across circle to get closer to current heading
            targetHeading %= 2*Math.PI; //gives you remainder, quotient usually 0 so it shouldnt change targetHeading
            targetState.speedMetersPerSecond *= -1; //flipped heading, so must flip velocity sign to go desired direction

        }
        m_turningPIDController.setSetpoint((targetHeading % (2*Math.PI) + (Math.PI*2)) % (Math.PI*2));
        return targetState.speedMetersPerSecond;
    }

    public void setDesiredState(SwerveModuleState state){
        double driveOutput = smartInversion(state);

        m_driveMotor.set(driveOutput/m_constants.maxMetersPerSecond);
        
        //m_turningPIDController.setSetpoint(state.angle.getRadians()); //delete because use smart inversion instead
        double pidOutput = m_turningPIDController.calculate(m_turningEncoder.getRadians());

        m_turningMotor.set(pidOutput);
        
        
        
    }

}
