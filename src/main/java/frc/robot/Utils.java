package frc.robot;

public class Utils {
    public static double deadZones (double input, double deadzone) {
        if (Math.abs(input) < deadzone) {
            return 0;
        }
        return input;
    }

    public static double oddSquare (double input) {
        return input*Math.abs(input);
    }
}