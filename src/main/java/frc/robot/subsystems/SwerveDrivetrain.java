package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class SwerveDrivetrain extends SubsystemBase {

  private Constants m_constants;

  private CANSparkMax m_frontLeftDriveMotor;
  private CANSparkMax m_frontLeftTurningMotor;
  private CANSparkMax m_frontRightDriveMotor;
  private CANSparkMax m_frontRightTurningMotor;
  private CANSparkMax m_backLeftDriveMotor;
  private CANSparkMax m_backLeftTurningMotor;
  private CANSparkMax m_backRightDriveMotor;
  private CANSparkMax m_backRightTurningMotor;

  private SwerveWheel m_frontLeftSwerveWheel;
  private SwerveWheel m_frontRightSwerveWheel;
  private SwerveWheel m_backLeftSwerveWheel;
  private SwerveWheel m_backRightSwerveWheel;

  private SwerveDriveKinematics m_kinematics;

  private double m_xSpeed;
  private double m_ySpeed;
  private double m_rotationSpeed;

  public SwerveDrivetrain(Constants constants) {
    m_constants = constants;

    m_frontLeftDriveMotor = new CANSparkMax(m_constants.FRONT_LEFT_DRIVE_MOTOR_PORT, MotorType.kBrushless);
    m_frontLeftTurningMotor = new CANSparkMax(m_constants.FRONT_LEFT_TURNING_MOTOR_PORT, MotorType.kBrushless);
    m_frontRightDriveMotor = new CANSparkMax(m_constants.FRONT_RIGHT_DRIVE_MOTOR_PORT, MotorType.kBrushless);
    m_frontRightTurningMotor = new CANSparkMax(m_constants.FRONT_RIGHT_TURNING_MOTOR_PORT, MotorType.kBrushless);
    m_backLeftDriveMotor = new CANSparkMax(m_constants.BACK_LEFT_DRIVE_MOTOR_PORT, MotorType.kBrushless);
    m_backLeftTurningMotor = new CANSparkMax(m_constants.BACK_LEFT_TURNING_MOTOR_PORT, MotorType.kBrushless);
    m_backRightDriveMotor = new CANSparkMax(m_constants.BACK_RIGHT_DRIVE_MOTOR_PORT, MotorType.kBrushless);
    m_backRightTurningMotor = new CANSparkMax(m_constants.BACK_RIGHT_TURNING_MOTOR_PORT, MotorType.kBrushless);

    m_frontLeftDriveMotor.setInverted(true);
    m_backLeftDriveMotor.setInverted(true);

    m_frontLeftSwerveWheel = new SwerveWheel(m_frontLeftDriveMotor, m_frontLeftTurningMotor, -m_constants.SWERVE_X,
        -m_constants.SWERVE_Y);
    m_frontRightSwerveWheel = new SwerveWheel(m_frontRightDriveMotor, m_frontRightTurningMotor, -m_constants.SWERVE_X,
        m_constants.SWERVE_Y);
    m_backLeftSwerveWheel = new SwerveWheel(m_backLeftDriveMotor, m_backLeftTurningMotor, m_constants.SWERVE_X,
        -m_constants.SWERVE_Y);
    m_backRightSwerveWheel = new SwerveWheel(m_backRightDriveMotor, m_backRightTurningMotor, m_constants.SWERVE_X,
        m_constants.SWERVE_Y);

    m_kinematics = new SwerveDriveKinematics(m_frontLeftSwerveWheel.getLocation(),
        m_frontRightSwerveWheel.getLocation(), m_backLeftSwerveWheel.getLocation(),
        m_backRightSwerveWheel.getLocation());
  }

  public void move(double xSpeed, double ySpeed, double rotationSpeed) {
    m_xSpeed = xSpeed;
    m_ySpeed = ySpeed;
    m_rotationSpeed = rotationSpeed;
  }

  @Override
  public void periodic() {
    SwerveModuleState[] swerveModuleStates;
    swerveModuleStates = m_kinematics.toSwerveModuleStates(new ChassisSpeeds(m_xSpeed, m_ySpeed, m_rotationSpeed));
    m_kinematics.normalizeWheelSpeeds(swerveModuleStates, 1);
    m_frontLeftSwerveWheel.setDesiredState(swerveModuleStates[0]);
    m_frontRightSwerveWheel.setDesiredState(swerveModuleStates[1]);
    m_backLeftSwerveWheel.setDesiredState(swerveModuleStates[2]);
    m_backRightSwerveWheel.setDesiredState(swerveModuleStates[3]);

  }
}
