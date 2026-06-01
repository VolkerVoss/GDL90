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

public class Control extends Gdl90Message {

    private final byte msgVersion;
    private boolean airborne;
    private final boolean tx1090ES, modeSReply, modeCReply, modeAReply, ident, baroChecked;
    private final int pressureAlt; // in metres
    private final int modeASquawk; // decimal -- 1200 = 0x4b0
    private final Priority priority;
    private final String callsign;

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
     * @return the baroChecked
     */
    public boolean isBaroChecked() {
	return this.baroChecked;
    }

    /**
     * @return the pressureAlt
     */
    public int getPressureAlt() {
	return this.pressureAlt;
    }

    /**
     * @return the modeASquawk
     */
    public int getModeASquawk() {
	return this.modeASquawk;
    }

    /**
     * @return the priority
     */
    public Priority getPriority() {
	return this.priority;
    }

    /**
     * @return the callsign
     */
    public String getCallsign() {
	return this.callsign;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public Control(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 10, (byte) 45);
	this.msgVersion = (byte) getByte();
	final byte b = (byte) getByte();
	this.tx1090ES = (b & 0x80) != 0;
	this.modeSReply = (b & 0x40) != 0;
	this.modeCReply = (b & 0x20) != 0;
	this.modeAReply = (b & 0x10) != 0;
	this.ident = (b & 0x08) != 0;
	final byte airGroundState = (byte) ((b & 0x6) >> 1);
	switch (airGroundState) {
	case 0, 1 -> this.airborne = true;
	case 2 -> this.airborne = false;
	}
	this.baroChecked = (b & 0x01) != 0;
	this.pressureAlt = (int) getInt();
	this.modeASquawk = getShort();
	final byte p = (byte) getByte();
	this.priority = Gdl90Message.priorityLookup[p < Priority.values().length ? p : Priority.values().length - 1];
	this.callsign = getString(8).trim();
	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "C%c: %s %d %c%c%c%c%c%c%c %.0fft %04d %s",
		crcValidChar(), this.callsign, this.msgVersion,
		this.tx1090ES ? 'T' : '.',
		this.modeSReply ? 'S' : '.',
		this.modeCReply ? 'C' : '.',
		this.modeAReply ? 'A' : '.',
		this.ident ? 'I' : '.',
		this.airborne ? 'A' : '.',
		this.baroChecked ? 'B' : '.',
		this.pressureAlt * 3.28084,
		this.modeASquawk, this.priority);
    }
}
