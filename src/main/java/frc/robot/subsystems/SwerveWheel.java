package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.geometry.Translation2d;

public class SwerveWheel {
    private CANSparkMax m_driveMotor;
    private CANSparkMax m_turningMotor;
    private Translation2d m_location;

    public SwerveWheel(CANSparkMax driveMotor, CANSparkMax turningMotor, double x, double y) {
        m_driveMotor = driveMotor;
        m_turningMotor = turningMotor;
        m_location = new Translation2d(x, y);
    }

    public Translation2d getLocation() {
        return m_location;
    }
}