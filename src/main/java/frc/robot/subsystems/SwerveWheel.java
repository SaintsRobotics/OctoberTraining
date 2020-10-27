package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import frc.robot.AbsoluteEncoder;
import frc.robot.Constants;

public class SwerveWheel {
    private CANSparkMax m_driveMotor;
    private CANSparkMax m_turningMotor;
    private Translation2d m_location;
    private PIDController m_turningPIDController;
    private AbsoluteEncoder m_turningEncoder;
    private Constants m_constants;

    public SwerveWheel(CANSparkMax driveMotor, CANSparkMax turningMotor, double x, double y, Constants constants) {
        m_driveMotor = driveMotor;
        m_turningMotor = turningMotor;
        m_location = new Translation2d(x, y);
        m_turningPIDController = new PIDController(0.3, 0, 0);
        m_turningPIDController.enableContinuousInput(0, 2 * Math.PI);
        m_turningEncoder = new AbsoluteEncoder(0, true);
        m_constants = constants;
    }

    public Translation2d getLocation() {
        return m_location;
    }

    public void setDesiredState(SwerveModuleState state) {
        m_driveMotor.set(state.speedMetersPerSecond / m_constants.MAX_METERS_PER_SECOND);
        m_turningPIDController.setSetpoint(state.angle.getRadians());
        double PIDOutput = m_turningPIDController.calculate(m_turningEncoder.getRadians());
        m_turningMotor.set(PIDOutput);
    }
}
