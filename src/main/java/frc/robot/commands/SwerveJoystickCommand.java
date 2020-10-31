/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SwerveJoystickCommand extends CommandBase {
  /**
   * Creates a new SwerveJoystickCommand.
   */
  public SwerveJoystickCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double x = Utils.oddSquare(Utils.deadZones(-m_controller.getY(Hand.kLeft, .2)) * m_constants.maxMetersPerSecond;
    double y = Utils.oddSquare(Utils.deadZones(m_controller.getX(Hand.kLeft, .2)) * m_constants.maxMetersPerSecond;
    double rot= Utils.oddSquare(Utils.deadZones(m_controller.getX(Hand.kRight, .2)) *m_constants.maxRadiansPerSecond;

    m_drivetrain.move(x, y, rot, m_controller.getBumper(Hand.kRight));
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
