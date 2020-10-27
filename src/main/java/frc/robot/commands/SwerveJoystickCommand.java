package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Utils;
import frc.robot.subsystems.SwerveDrivetrain;

public class SwerveJoystickCommand extends CommandBase {
  private Constants m_constants;

  private SwerveDrivetrain m_drivetrain;
  private XboxController m_controller;
  private Utils m_utils;

  public SwerveJoystickCommand(SwerveDrivetrain drivetrain) {
    addRequirements(drivetrain);
    m_drivetrain = drivetrain;
    m_controller = new XboxController(m_constants.XBOX_CONTROLLER_PORT);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    double x = m_utils.oddSquare(m_utils.deadZones(-m_controller.getY(Hand.kLeft), 0.2))
        * m_constants.MAX_METERS_PER_SECOND;
    double y = m_utils.oddSquare(m_utils.deadZones(m_controller.getX(Hand.kLeft), 0.2))
        * m_constants.MAX_METERS_PER_SECOND;
    double rotation = m_utils.oddSquare(m_utils.deadZones(m_controller.getX(Hand.kRight), 0.2))
        * m_constants.MAX_RADIANS_PER_SECOND;

    m_drivetrain.move(x, y, rotation, m_controller.getBumper(Hand.kRight));
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
