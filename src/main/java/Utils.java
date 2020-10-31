package frc.robot;

public class Utils {
    public static double deadZones(double input, double deadZone){
        if (Math.abs(input)<deadZone){
            return 0;
        }      
        return  input;
    }
    
    public static double oddSquare(double input) {
        return input * Math.abs(input);
    }

}