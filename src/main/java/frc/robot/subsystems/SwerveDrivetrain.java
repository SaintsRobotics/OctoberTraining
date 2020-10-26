package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Utils;

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
  private AHRS m_gyro;
  private PIDController m_PIDController;

  private boolean m_isTurning;
  private double m_xSpeed;
  private double m_ySpeed;
  private double m_rotationSpeed;
  private boolean m_isFieldRelative;

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
        -m_constants.SWERVE_Y, constants);
    m_frontRightSwerveWheel = new SwerveWheel(m_frontRightDriveMotor, m_frontRightTurningMotor, -m_constants.SWERVE_X,
        m_constants.SWERVE_Y, constants);
    m_backLeftSwerveWheel = new SwerveWheel(m_backLeftDriveMotor, m_backLeftTurningMotor, m_constants.SWERVE_X,
        -m_constants.SWERVE_Y, constants);
    m_backRightSwerveWheel = new SwerveWheel(m_backRightDriveMotor, m_backRightTurningMotor, m_constants.SWERVE_X,
        m_constants.SWERVE_Y, constants);

    m_kinematics = new SwerveDriveKinematics(m_frontLeftSwerveWheel.getLocation(),
        m_frontRightSwerveWheel.getLocation(), m_backLeftSwerveWheel.getLocation(),
        m_backRightSwerveWheel.getLocation());

    m_gyro = new AHRS();
    m_PIDController = new PIDController(Math.toRadians((constants.MAX_METERS_PER_SECOND / 180) * 5), 0, 0);
    m_PIDController.enableContinuousInput(0, Math.PI * 2);
    m_PIDController.setTolerance(1/36);
  }

  public void move(double xSpeed, double ySpeed, double rotationSpeed, boolean isFieldRelative) {
    m_xSpeed = xSpeed;
    m_ySpeed = ySpeed;
    m_rotationSpeed = rotationSpeed;
    m_isFieldRelative = isFieldRelative;
  }

  @Override
  public void periodic() {
    if (Utils.deadZones(m_gyro.getRate(), 0.05) != 0) {
      m_isTurning = true;
    } else if (m_isTurning == true && Utils.deadZones(m_gyro.getRate(), 0.05) == 0) {
      m_isTurning = false;
      m_PIDController.setSetpoint(m_gyro.getAngle() % (Math.PI * 2) + (Math.PI * 2) % (Math.PI * 2));
      m_rotationSpeed = m_PIDController
          .calculate(Math.toRadians(m_gyro.getAngle() % (Math.PI * 2) + (Math.PI * 2) % (Math.PI * 2)));
    } else if (m_xSpeed != 0 || m_ySpeed != 0) {
      m_rotationSpeed = m_PIDController
          .calculate(Math.toRadians(m_gyro.getAngle()) % (Math.PI * 2) + (Math.PI * 2) % (Math.PI * 2));
    }

    SwerveModuleState[] swerveModuleStates;
    if (m_isFieldRelative) {
      swerveModuleStates = m_kinematics.toSwerveModuleStates(
          ChassisSpeeds.fromFieldRelativeSpeeds(m_xSpeed, m_ySpeed, m_rotationSpeed, new Rotation2d(
              2 * Math.PI - ((Math.toRadians(m_gyro.getAngle()) % (Math.PI * 2)) + (Math.PI * 2)) % (Math.PI * 2))));
    } else {
      swerveModuleStates = m_kinematics.toSwerveModuleStates(new ChassisSpeeds(m_xSpeed, m_ySpeed, m_rotationSpeed));
    }
    m_kinematics.normalizeWheelSpeeds(swerveModuleStates, m_constants.MAX_METERS_PER_SECOND);
    m_frontLeftSwerveWheel.setDesiredState(swerveModuleStates[0]);
    m_frontRightSwerveWheel.setDesiredState(swerveModuleStates[1]);
    m_backLeftSwerveWheel.setDesiredState(swerveModuleStates[2]);
    m_backRightSwerveWheel.setDesiredState(swerveModuleStates[3]);
  }
}
