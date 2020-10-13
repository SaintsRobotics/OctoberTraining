/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.geometry.Translation2d;
/**
 * Add your docs here.
 */
public class SwerveWheel {

    private CANSparkMax m_driveMotor;
    private CANSparkMax m_turnMotor;
    private Translation2d m_location;

    public SwerveWheel(CANSparkMax driveMotor, CANSparkMax turnMotor, double x, double y){
        m_driveMotor = driveMotor;
        m_turnMotor = turnMotor;
        m_location = new Translation2d(x, y); //translation2d is where module on the robot
    }

    public Translation2d getPosition(){
        return m_location;
    }
}
