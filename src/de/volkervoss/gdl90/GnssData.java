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

public class GnssData extends Gdl90Message {

    public enum FixQuality {
	Unknown, No_Fix, Fix_2D, Fix_3D, Fix_Differential, Fix_RTK
    }

    private final static FixQuality[] fixQualityLookup = new FixQuality[] { FixQuality.Unknown, FixQuality.No_Fix, FixQuality.Fix_2D, FixQuality.Fix_3D, FixQuality.Fix_Differential, FixQuality.Fix_RTK };
    private final byte msgVersion;
    private final long seconds; // since Epoch, UTC
    private final double lat, lon, alt, horizProtectLevel, vertProtectLevel, horizMerit, vertMerit, horizSpeedMerit, vVelMerit, vVel, NSVel, EWVel;
    private final int numSatellites;
    private final FixQuality fixQuality;
    private final boolean hplActive, fault, magNorthRef;

    /**
     * @return the fixqualitylookup
     */
    public static FixQuality[] getFixqualitylookup() {
	return GnssData.fixQualityLookup;
    }

    /**
     * @return the msgVersion
     */
    public byte getMsgVersion() {
	return this.msgVersion;
    }

    /**
     * @return the seconds
     */
    public long getSeconds() {
	return this.seconds;
    }

    /**
     * @return the lat
     */
    public double getLat() {
	return this.lat;
    }

    /**
     * @return the lon
     */
    public double getLon() {
	return this.lon;
    }

    /**
     * @return the alt
     */
    public double getAlt() {
	return this.alt;
    }

    /**
     * @return the horizProtectLevel
     */
    public double getHorizProtectLevel() {
	return this.horizProtectLevel;
    }

    /**
     * @return the vertProtectLevel
     */
    public double getVertProtectLevel() {
	return this.vertProtectLevel;
    }

    /**
     * @return the horizMerit
     */
    public double getHorizMerit() {
	return this.horizMerit;
    }

    /**
     * @return the vertMerit
     */
    public double getVertMerit() {
	return this.vertMerit;
    }

    /**
     * @return the horizSpeedMerit
     */
    public double getHorizSpeedMerit() {
	return this.horizSpeedMerit;
    }

    /**
     * @return the vVelMerit
     */
    public double getvVelMerit() {
	return this.vVelMerit;
    }

    /**
     * @return the vVel
     */
    public double getvVel() {
	return this.vVel;
    }

    /**
     * @return the nSVel
     */
    public double getNSVel() {
	return this.NSVel;
    }

    /**
     * @return the eWVel
     */
    public double getEWVel() {
	return this.EWVel;
    }

    /**
     * @return the numSatellites
     */
    public int getNumSatellites() {
	return this.numSatellites;
    }

    /**
     * @return the fixQuality
     */
    public FixQuality getFixQuality() {
	return this.fixQuality;
    }

    /**
     * @return the hplActive
     */
    public boolean isHplActive() {
	return this.hplActive;
    }

    /**
     * @return the fault
     */
    public boolean isFault() {
	return this.fault;
    }

    /**
     * @return the magNorthRef
     */
    public boolean isMagNorthRef() {
	return this.magNorthRef;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public GnssData(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 44, (byte) 46);
	this.msgVersion = (byte) getByte();
	final int msgSize = this.msgVersion == 2 ? 48 : 44;
	if (is.available() < (msgSize + 1)) {
	    throw new UnsupportedEncodingException("Message too short: expected " + msgSize + " but received " + (is.available() - 1));
	}
	this.seconds = getInt() & 0xffff;
	int i = (int) getInt();
	this.lat = i == Integer.MAX_VALUE ? Double.NaN : i / 1e7;
	i = (int) getInt();
	this.lon = i == Integer.MAX_VALUE ? Double.NaN : i / 1e7;
	i = (int) getInt();
	this.alt = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	i = (int) getInt();
	this.horizProtectLevel = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	i = (int) getInt();
	this.vertProtectLevel = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	i = (int) getInt();
	this.horizMerit = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	i = getShort();
	this.vertMerit = i == Integer.MAX_VALUE ? Double.NaN : i / 1e2;
	i = getShort();
	this.horizSpeedMerit = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	i = getShort();
	this.vVelMerit = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	i = getShort();
	this.vVel = i == Integer.MAX_VALUE ? Double.NaN : i / 1e2;
	if (this.msgVersion == 1) {
	    i = getShort();
	    this.NSVel = i == Integer.MAX_VALUE ? Double.NaN : i / 1e1;
	    i = getShort();
	    this.EWVel = i == Integer.MAX_VALUE ? Double.NaN : i / 1e1;
	} else {
	    i = (int) getInt();
	    this.NSVel = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	    i = (int) getInt();
	    this.EWVel = i == Integer.MAX_VALUE ? Double.NaN : i / 1e3;
	}
	final byte p = (byte) getByte();
	this.fixQuality = p < FixQuality.values().length ? GnssData.fixQualityLookup[p] : FixQuality.Unknown;
	final byte n = (byte) getByte();
	this.hplActive = (n & 0x01) != 0;
	this.fault = (n & 0x02) != 0;
	this.magNorthRef = (n & 0x04) != 0;
	this.numSatellites = (byte) getByte();
	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "C%c: msg v%d %dsecs (%f, %f)@%fft %f %f %f, %f %f, %f %f, %f %f, %c%c%c %d %s",
		crcValidChar(), this.msgVersion, this.seconds, this.lat, this.lon, this.alt * 3.28084, this.vVel, this.NSVel, this.EWVel,
		this.horizProtectLevel, this.vertProtectLevel, this.horizMerit, this.vertMerit, this.horizSpeedMerit, this.vVelMerit,
		this.hplActive ? 'H' : '.',
		this.fault ? 'F' : '.',
		this.magNorthRef ? 'M' : '.',
		this.numSatellites, this.fixQuality);
    }
}
