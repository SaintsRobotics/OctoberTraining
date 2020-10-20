/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

public class SwerveJoystickCommand extends CommandBase {
  private SwerveDrivetrain m_drivetrain;
  private XboxController m_controller;
  private Constants m_constants;
  /**
   * Creates a new SwerveJoystickCommand.
   */
  public SwerveJoystickCommand(SwerveDrivetrain drivetrain, Constants constants) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    m_drivetrain = drivetrain;
    m_controller = new XboxController(0); //only intialize for commands w/multiple buttons
    m_constants = constants;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() { //basically gets how far its pushed * max speed
    double xSpeed = -m_controller.getY(Hand.kLeft) * m_constants.maxMetersPerSecond; //forward is x direction, so y on stick
    double ySpeed = m_controller.getX(Hand.kLeft) * m_constants.maxMetersPerSecond; //kLeft means right or left joystick
    double rotSpeed = m_controller.getX(Hand.kRight) * m_constants.maxMetersPerSecond;

    m_drivetrain.move(xSpeed, ySpeed, rotSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.move(0, 0, 0, m_controller.getBumper(Hand.kRight));
    //if suddenly stops, want as safety
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
