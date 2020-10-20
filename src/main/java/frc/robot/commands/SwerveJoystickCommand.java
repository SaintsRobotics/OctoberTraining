/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveDrivetrain;

public class SwerveJoystickCommand extends CommandBase {
  /**
   * Creates a new SwerveJoystickCommand.
   */
  private SwerveDrivetrain m_drivetrain;
  private XboxController m_controller;
  private Constants m_constants;

  public SwerveJoystickCommand(SwerveDrivetrain drivetrain, Constants constants) {
    m_drivetrain = drivetrain;
    addRequirements(drivetrain);
    m_controller = new XboxController(0);
    m_constants = constants;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double x = m_controller.getY(Hand.kLeft) * m_constants.maxMetersPerSecond;
    double y = m_controller.getX(Hand.kLeft) * m_constants.maxMetersPerSecond;
    double rot = m_controller.getX(Hand.kRight) * m_constants.maxRadiansPerSecond; 
    m_drivetrain.move(-x, y, rot, m_controller.getBumper(Hand.kRight));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.move(0,0,0, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
