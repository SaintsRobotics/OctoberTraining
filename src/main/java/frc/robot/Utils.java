/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Add your docs here.
 */
public class Utils {
    // if universal variables, and all static methods, then default constructor can
    // be used

    // no variables, so no constructor needed
    public static double deadZones(double input, double deadZone) { // controller deadzone
        if (Math.abs(input) < deadZone) {
            return 0;
        }
        return input; // dont integrate oddSquare bc dont always want
    }

    public static double oddSquare(double input) {
        return input * Math.abs(input);
    }
}
