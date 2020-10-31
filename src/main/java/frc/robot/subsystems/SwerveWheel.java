/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wbilibj.controller.PIDController;
import edu.wpi.first.wbilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.geometry.Translation2d;


/**
 * Add your docs here.
 */
public class SwerveWheel {
    private CANSparkMax m_driveMotor;
    private CANSparkMax m_turningMotor;
    private Translation2d m_location;
    private PIDController m_turningPidController;
    private Encoder m_turningEncoder;

    public SwerveWheel(CANSparkMax driveMotor, CANSparkMax turningMotor, double x, double y){
        m_driveMotor = driveMotor;
        m_turningMotor = turningMotor;
        m_location = new Translation2d(x, y);
        m_turningPidController = m_turningMotor.getPIDController();
        m_turningPidController = new edu.wpi.first.wpilibj.controller.PIDController(.3,0,0);
        m_turningPidController.enableContinuousInput(0, 2*Math.PI);
        m_turningEncoder = encoder;
        m_constants = constants;


    }

    public Translation2d getLocation(){
        return m_location;
    }

    private void setDesiredState(SwerveModuleState state){
        m_driveMotor.set(state.speedMetersPerSecond);
        
        m_turningPidController.setSetpoint(state.angle.getRadians());
        double pidOutput = m_turningPidController.calculate(m_turningEncoder.getDistance());
        
        m_turningMotor.set(pidOutput);
    }
    
}
