package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Utils;
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
    double x = Utils.oddSquare(Utils.deadZones(-m_controller.getY(Hand.kLeft) * m_constants.MAX_METERS_PER_SECOND, 0.2));
    double y = Utils.oddSquare(Utils.deadZones(m_controller.getX(Hand.kLeft) * m_constants.MAX_METERS_PER_SECOND, 0.2));
    double rotation = Utils.oddSquare(Utils.deadZones(m_controller.getX(Hand.kRight) * m_constants.MAX_RADIANS_PER_SECOND, 0.2));

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
