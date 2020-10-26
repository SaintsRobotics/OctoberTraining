package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Utils;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.Utils;

public class SwerveJoystickCommand extends CommandBase {
  private SwerveDrivetrain m_drivetrain;
  private XboxController m_controller;
  private Constants m_constants;
<<<<<<< HEAD

=======
  private Utils m_utils;
  /**
   * Creates a new SwerveJoystickCommand.
   */
>>>>>>> origin/master
  public SwerveJoystickCommand(SwerveDrivetrain drivetrain, Constants constants) {
    addRequirements(drivetrain);
    m_drivetrain = drivetrain;
    m_controller = new XboxController(0);
    m_constants = constants;
    m_utils = new Utils();
  }

  @Override
  public void initialize() {
  }

  @Override
<<<<<<< HEAD
  public void execute() {
    double x = Utils.oddSquare(Utils.deadZones(-m_controller.getY(Hand.kLeft) * m_constants.MAX_METERS_PER_SECOND, 0.2));
    double y = Utils.oddSquare(Utils.deadZones(m_controller.getX(Hand.kLeft) * m_constants.MAX_METERS_PER_SECOND, 0.2));
    double rotation = Utils.oddSquare(Utils.deadZones(m_controller.getX(Hand.kRight) * m_constants.MAX_RADIANS_PER_SECOND, 0.2));
=======
  public void execute() { //if dont apply deadzone, then relation between joystick/speed is linear and no deadzones, we need these
    double x = m_utils.oddSquare(m_utils.deadZones(-m_controller.getY(Hand.kLeft), 0.2)) * m_constants.maxMetersPerSecond; //apply functions to controller values to 1) check deadzone 2) apply quadratic relation between controller/speed
    double y =  m_utils.oddSquare(m_utils.deadZones(m_controller.getX(Hand.kLeft), 0.2)) * m_constants.maxMetersPerSecond;
    double rot = m_utils.oddSquare(m_utils.deadZones(m_controller.getX(Hand.kRight), 0.2)) * m_constants.maxRadiansPerSecond;
>>>>>>> origin/master

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
