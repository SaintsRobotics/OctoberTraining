/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;
/**
 * Add your docs here.
 */
public class AbsoluteEncoder {
    //outputs volts, 1 volt gives 72 degrees, its a signal for the position encoder is out
    //normal encoders take two inputs, we only take in one input (analog encoder)
    private AnalogInput analogIn;
    private boolean isInverted;
    private double voltageToDegrees = 72;
    private double m_offset;
    public AbsoluteEncoder(int channel, boolean inverted, double offset){ //analog ports are called channels
        analogIn = new AnalogInput(channel); //refers to port, absolute encoder is the thing plugged in, analog input reads voltage from encoder
        isInverted = inverted;
        m_offset = offset;
    }

    private double getDegrees(){ //use voltage, find degree amount being signalled
        if(isInverted){ //but if inverted, want account for different positions' voltage
            return (5-analogIn.getVoltage()-m_offset)*voltageToDegrees; //offset is voltage on encoder when wheel points in correct direction, so proper "0" degrees direction
        }
        return (analogIn.getVoltage()-m_offset)*voltageToDegrees;
        
    }

    private double getRadians(){
        return Math.toRadians(getDegrees());
    }

}
