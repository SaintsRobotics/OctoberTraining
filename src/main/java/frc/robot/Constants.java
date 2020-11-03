/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    public double maxMetersPerSecond = 3.66;
    public double maxRadiansPerSecond = 8.76;

    public int m_frontLeftDriveMotorPort = 8;
    public int m_frontLeftTurningMotorPort = 1;

    public int m_backLeftDriveMotorPort = 2;
    public int m_backLeftTurningMotorPort = 3;

    public int m_frontRightDriveMotorPort = 4;
    public int m_frontRightTurningMotorPort = 5;

    public int m_backRightDriveMotorPort = 6;
    public int m_backRightTurningMotorPort = 7;

    public double swerveX = .67 / 2;
    public double swerveY = .25;

    public int m_frontLeftTurningEncoderPort = 0;
    public int m_frontRightTurningEncoderPort = 1;
    public int m_backLeftTurningEncoderPort = 2;
    public int m_backRightTurningEncoderPort = 3;

    public double frontLeftSwerveOffset = 2.254991;
    public double frontRightSwerveOffset = 2.466641;
    public double backLeftSwerveOffset = .279;
    public double backRightSwerveOffset = 3.25;

    public double translationalFriction = 0.0205;

}
