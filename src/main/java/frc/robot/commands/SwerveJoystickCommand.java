package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveDrivetrain;

public class SwerveJoystickCommand extends CommandBase {
  private SwerveDrivetrain m_drivetrain;
  private XboxController m_controller;
  private Constants m_constants;

  public SwerveJoystickCommand(SwerveDrivetrain drivetrain, Constants constants) {
    addRequirements(drivetrain);
    m_drivetrain = drivetrain;
    m_controller = new XboxController(0);
    m_constants = constants;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    m_drivetrain.move(-m_controller.getY(Hand.kLeft) * m_constants.MAX_METERS_PER_SECOND,
        m_controller.getX(Hand.kLeft) * m_constants.MAX_METERS_PER_SECOND,
        m_controller.getX(Hand.kRight) * m_constants.MAX_RADIANS_PER_SECOND, m_controller.getBumper(Hand.kRight));
  }

  @Override
  public void end(boolean interrupted) {
    m_drivetrain.move(0, 0, 0, false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
