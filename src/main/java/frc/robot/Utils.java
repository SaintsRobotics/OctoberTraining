package frc.robot;

public class Utils {

    public double deadZones(double input, double deadZone) {
        if (Math.abs(input) > deadZone) {
            return 0;
        }
        return input;
    }

    public double oddSquare(double input) {
        return input * Math.abs(input);
    }
}
