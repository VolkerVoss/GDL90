/*
 * Copyright 2022 Frank van der Hulst drifter.frank@gmail.com
 * Modified by Volker Voss volker.v@gmx.net
 *
 * This software is made available under a Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0) License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 * You are free to share (copy and redistribute the material in any medium or format) and
 * adapt (remix, transform, and build upon the material) this software under the following terms:
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made.
 * You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes.
 */
package de.volkervoss.gdl90;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class Barometer extends Gdl90Message {

    private final byte sensorType;
    private final double pressureAlt; // in metres
    private final double pressMBar;
    private final double sensorTemp;

    /**
     * @return the sensorType
     */
    public byte getSensorType() {
	return this.sensorType;
    }

    /**
     * @return the pressureAlt
     */
    public double getPressureAlt() {
	return this.pressureAlt;
    }

    /**
     * @return the pressMBar
     */
    public double getPressMBar() {
	return this.pressMBar;
    }

    /**
     * @return the sensorTemp
     */
    public double getSensorTemp() {
	return this.sensorTemp;
    }

    /**
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public Barometer(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 10, (byte) 40);
	this.sensorType = (byte) getByte();
	long p = getInt();
	this.pressMBar = p == 0xffffffff ? Double.NaN : p / 100.0;
	p = getInt();
	this.pressureAlt = p == 0xffffffff ? Double.NaN : p / 1000.0;
	p = getShort();
	this.sensorTemp = p == 0xffff ? Double.NaN : p / 100.0;
	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "B%c: %d %fmBar %fft %fC",
		crcValidChar(), this.sensorType, this.pressMBar, this.pressureAlt * 3.28084, this.sensorTemp);
    }
}
