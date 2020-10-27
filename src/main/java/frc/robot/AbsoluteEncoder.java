package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class AbsoluteEncoder {
    private AnalogInput m_analogInput;
    private boolean m_isInverted;
    private double m_offset;

    public AbsoluteEncoder(int channel, boolean isInverted, double offset) {
        m_analogInput = new AnalogInput(channel);
        m_isInverted = isInverted;
        m_offset = offset;
    }

    public double getDegrees() {
        if (m_isInverted) {
            return (5 - m_analogInput.getVoltage()) * 72;
        } else {
            return m_analogInput.getVoltage() * 72;
        }
    }

    public double getRadians() {
        return Math.toRadians(getDegrees());
    }
}