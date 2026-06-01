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

public class TransponderStatus extends Gdl90Message {

    private final byte msgVersion;
    private boolean airborne, fault, intSinceLast;
    private final boolean tx1090ES, modeSReply, modeCReply, modeAReply, ident;
    private int modeARepliesPerSec;
    private int modeCRepliesPerSec;
    private int modeSRepliesPerSec;
    private final int modeASquawk;
    private int nic;
    private int nac;
    private double lat, lon, alt, speed, vVel;
    private int boardTemp;

    /**
     * @return the msgVersion
     */
    public byte getMsgVersion() {
	return this.msgVersion;
    }

    /**
     * @return the airborne
     */
    public boolean isAirborne() {
	return this.airborne;
    }

    /**
     * @return the fault
     */
    public boolean isFault() {
	return this.fault;
    }

    /**
     * @return the intSinceLast
     */
    public boolean isIntSinceLast() {
	return this.intSinceLast;
    }

    /**
     * @return the tx1090ES
     */
    public boolean isTx1090ES() {
	return this.tx1090ES;
    }

    /**
     * @return the modeSReply
     */
    public boolean isModeSReply() {
	return this.modeSReply;
    }

    /**
     * @return the modeCReply
     */
    public boolean isModeCReply() {
	return this.modeCReply;
    }

    /**
     * @return the modeAReply
     */
    public boolean isModeAReply() {
	return this.modeAReply;
    }

    /**
     * @return the ident
     */
    public boolean isIdent() {
	return this.ident;
    }

    /**
     * @return the modeARepliesPerSec
     */
    public int getModeARepliesPerSec() {
	return this.modeARepliesPerSec;
    }

    /**
     * @return the modeCRepliesPerSec
     */
    public int getModeCRepliesPerSec() {
	return this.modeCRepliesPerSec;
    }

    /**
     * @return the modeSRepliesPerSec
     */
    public int getModeSRepliesPerSec() {
	return this.modeSRepliesPerSec;
    }

    /**
     * @return the modeASquawk
     */
    public int getModeASquawk() {
	return this.modeASquawk;
    }

    /**
     * @return the nic
     */
    public int getNic() {
	return this.nic;
    }

    /**
     * @return the nac
     */
    public int getNac() {
	return this.nac;
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
     * @return the speed
     */
    public double getSpeed() {
	return this.speed;
    }

    /**
     * @return the vVel
     */
    public double getvVel() {
	return this.vVel;
    }

    /**
     * @return the boardTemp
     */
    public int getBoardTemp() {
	return this.boardTemp;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     * 
     * @param is
     * @throws UnsupportedEncodingException
     */
    public TransponderStatus(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 9, (byte) 47);
	this.msgVersion = (byte) getByte();
	final int msgSize = this.msgVersion == 1 ? 9 : this.msgVersion == 2 ? 15 : 16;
	if (is.available() < (msgSize + 1)) {
	    throw new UnsupportedEncodingException("Message too short: expected " + msgSize + " but received " + (is.available() - 1));
	}
	byte b = (byte) getByte();
	this.tx1090ES = (b & 0x80) != 0;
	this.modeSReply = (b & 0x40) != 0;
	this.modeCReply = (b & 0x20) != 0;
	this.modeAReply = (b & 0x10) != 0;
	this.ident = (b & 0x08) != 0;
	if (this.msgVersion == 1) {
	    this.modeARepliesPerSec = getShort();
	    this.modeCRepliesPerSec = getShort();
	    this.modeSRepliesPerSec = getShort();
	    this.modeASquawk = getShort();
	} else {
	    this.fault = (b & 0x04) != 0; // version 2,3
	    this.intSinceLast = (b & 0x02) != 0; // version 2,3
	    this.airborne = (b & 0x01) != 0; // version 2,3
	    this.lat = get3BytesDegrees();
	    this.lon = get3BytesDegrees();
	    final long l = getLong();
	    this.alt = (((l >> 20) & 0x7ff) * 25) - 1000;
	    this.speed = ((l >> 8) & 0xfff);
	    this.vVel = ((l & 0xff) * 360.0) / 256;
	    this.modeASquawk = getShort();
	    b = (byte) getByte();
	    this.nac = b >> 4;
	    this.nic = b & 0x0f;
	    if (this.msgVersion > 2) {
		this.boardTemp = getByte();
	    }
	}
	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "S%c: %d (%f, %f)@%fft %f %f %c%c%c%c%c%c%c%c %04d %d %d %dC %d %d %d",
		crcValidChar(), this.msgVersion, this.lat, this.lon, this.alt * 3.28084, this.speed, this.vVel,
		this.tx1090ES ? 'T' : '.',
		this.modeSReply ? 'S' : '.',
		this.modeCReply ? 'C' : '.',
		this.modeAReply ? 'A' : '.',
		this.ident ? 'I' : '.',
		this.fault ? 'F' : '.',
		this.intSinceLast ? 'I' : '.',
		this.airborne ? 'A' : '.',
		this.modeASquawk, this.nac, this.nic, this.boardTemp,
		this.modeARepliesPerSec, this.modeCRepliesPerSec, this.modeSRepliesPerSec);
    }
}
