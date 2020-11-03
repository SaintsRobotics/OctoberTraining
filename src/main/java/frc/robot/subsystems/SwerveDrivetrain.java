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

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.trajectory.constraint.SwerveDriveKinematicsConstraint;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.AbsoluteEncoder;
import frc.robot.Constants;
import frc.robot.Utils;

public class SwerveDrivetrain extends SubsystemBase {

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

    private AbsoluteEncoder m_frontLeftTurningEncoder;
    private AbsoluteEncoder m_frontRightTurningEncoder;
    private AbsoluteEncoder m_backLeftTurningEncoder;
    private AbsoluteEncoder m_backRightTurningEncoder;

    private Constants m_constants;

    private double m_xSpeed;
    private double m_ySpeed;
    private double m_rotSpeed;
    private boolean m_isFieldRelative;

    private SwerveDriveKinematics m_kinematics;

    private AHRS m_gyro;
    private boolean m_isTurning; // dont want it to reset each time a method is called, want to save it

    // need pid to save headings/dynamic controls
    private PIDController m_pidController;

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

        m_frontLeftTurningEncoder = new AbsoluteEncoder(m_constants.m_frontLeftTurningEncoderPort, true,
                constants.frontLeftSwerveOffset);
        m_frontRightTurningEncoder = new AbsoluteEncoder(m_constants.m_frontRightTurningEncoderPort, true,
                constants.frontRightSwerveOffset);
        m_backLeftTurningEncoder = new AbsoluteEncoder(m_constants.m_backLeftTurningEncoderPort, true,
                constants.backLeftSwerveOffset);
        m_backRightTurningEncoder = new AbsoluteEncoder(m_constants.m_backRightTurningEncoderPort, true,
                constants.backRightSwerveOffset);

        m_frontLeftSwerveWheel = new SwerveWheel(m_frontLeftDriveMotor, m_frontLeftTurningMotor, -m_constants.swerveX,
                m_constants.swerveY, m_frontLeftTurningEncoder, constants);
        m_backLeftSwerveWheel = new SwerveWheel(m_backLeftDriveMotor, m_backLeftTurningMotor, -m_constants.swerveX,
                -m_constants.swerveY, m_backLeftTurningEncoder, constants);
        m_frontRightSwerveWheel = new SwerveWheel(m_frontRightDriveMotor, m_frontRightTurningMotor, m_constants.swerveX,
                m_constants.swerveY, m_frontRightTurningEncoder, constants);
        m_backRightSwerveWheel = new SwerveWheel(m_backRightDriveMotor, m_backRightTurningMotor, m_constants.swerveX,
                -m_constants.swerveY, m_backRightTurningEncoder, constants);

        m_kinematics = new SwerveDriveKinematics(m_frontLeftSwerveWheel.getLocation(),
                m_frontRightSwerveWheel.getLocation(), m_backLeftSwerveWheel.getLocation(),
                m_backRightSwerveWheel.getLocation());

        m_gyro = new AHRS();

        m_pidController = new PIDController(Math.toRadians((m_constants.maxMetersPerSecond / 180) * 5), 0, 0); // needs
                                                                                                               // import
        m_pidController.enableContinuousInput(0, Math.PI * 2);
        m_pidController.setTolerance(1 / 36); // if off by a lil bit, then dont do anything (is in radians)
    }

    public void move(double xSpeed, double ySpeed, double rotSpeed, boolean isFieldRelative) {
        m_xSpeed = xSpeed;
        m_xSpeed += m_constants.translationalFriction * m_xSpeed / Math.abs(m_xSpeed);
        m_ySpeed = ySpeed;
        m_ySpeed += m_constants.translationalFriction * m_ySpeed / Math.abs(m_ySpeed);
        m_rotSpeed = rotSpeed;
        m_isFieldRelative = isFieldRelative;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        // heading correction
        // getRate is checking rotation in deg/sec, if <0.05 then no change needed
        if (Utils.deadZones(m_gyro.getRate(), 0.05) != 0) { // checks rotation, always is a value bc vibrate -> need
                                                            // deadzone to eliminate common vibrations
            m_isTurning = true;
        } else if (m_isTurning = true && Utils.deadZones(m_gyro.getRate(), 0.05) == 0) { // if deadzone/getRate is 0, so
                                                                                         // not
                                                                                         // turning, but m_isTurning is
                                                                                         // true
                                                                                         // (we were just turning), then
                                                                                         // want to do smth
            m_isTurning = false;
            m_pidController
                    .setSetpoint(Math.toRadians(m_gyro.getAngle()) % (Math.PI * 2) + (Math.PI * 2) % (Math.PI * 2)); // store
                                                                                                                     // heading,
                                                                                                                     // keep
                                                                                                                     // degrees
                                                                                                                     // for
                                                                                                                     // now
            m_rotSpeed = m_pidController
                    .calculate(Math.toRadians(m_gyro.getAngle()) % (Math.PI * 2) + (Math.PI * 2) % (Math.PI * 2)); // gets
                                                                                                                   // the
                                                                                                                   // error
                                                                                                                   // to
                                                                                                                   // correct
                                                                                                                   // heading
                                                                                                                   // times
                                                                                                                   // kp,
                                                                                                                   // using
                                                                                                                   // gyro
                                                                                                                   // angle
        } else if (m_xSpeed != 0 || m_ySpeed != 0) { // if moving at all, assume drift, but if rotating, then first
                                                     // conditional just sets isTurning as true, no heading correction
            m_rotSpeed = m_pidController
                    .calculate(Math.toRadians(m_gyro.getAngle()) % (Math.PI * 2) + (Math.PI * 2) % (Math.PI * 2)); // if
                                                                                                                   // off,
                                                                                                                   // gives
                                                                                                                   // correction
                                                                                                                   // as
                                                                                                                   // rotation
                                                                                                                   // speed
            // if value less than tolerance (1/36), then calculate is just 0 (no rotate)

        }

        SwerveModuleState[] swerveModuleStates;
        if (m_isFieldRelative) { // chassisspeeds for first conditional is from wpilib, doesnt need instantiated
            swerveModuleStates = m_kinematics.toSwerveModuleStates(
                    ChassisSpeeds.fromFieldRelativeSpeeds(m_xSpeed, m_ySpeed, m_rotSpeed, new Rotation2d(
                            ((Math.toRadians(m_gyro.getAngle()) % (Math.PI * 2)) + (Math.PI * 2)) % (Math.PI * 2)))); // instead
                                                                                                                      // of
                                                                                                                      // new
                                                                                                                      // chassisspeeds,
                                                                                                                      // use
                                                                                                                      // method
                                                                                                                      // from
                                                                                                                      // chassisspeeds
                                                                                                                      // to
                                                                                                                      // field
                                                                                                                      // relative
        } else {
            swerveModuleStates = m_kinematics.toSwerveModuleStates(new ChassisSpeeds(m_xSpeed, m_ySpeed, m_rotSpeed));
        }
        m_kinematics.normalizeWheelSpeeds(swerveModuleStates, m_constants.maxMetersPerSecond);

        m_frontLeftSwerveWheel.setDesiredState(swerveModuleStates[0]);
        m_frontRightSwerveWheel.setDesiredState(swerveModuleStates[1]);
        m_backLeftSwerveWheel.setDesiredState(swerveModuleStates[2]);
        m_backRightSwerveWheel.setDesiredState(swerveModuleStates[3]);

    }
}
