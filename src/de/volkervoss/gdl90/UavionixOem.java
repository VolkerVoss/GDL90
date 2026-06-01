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

public class UavionixOem extends Gdl90Message {

    private final char signature;
    private final int subType;
    private byte msgVersion;
    private final int icao;
    private final Emitter emitterType;
    private final String callsign;
    private final float stallSpeed;
    private final AircraftLengthWeight avLw;
    private final LateralGpsOfs antOfslat;
    private final int antOfsLon;
    private final boolean qiMode;

    /**
     * @return the signature
     */
    public char getSignature() {
	return this.signature;
    }

    /**
     * @return the subType
     */
    public int getSubType() {
	return this.subType;
    }

    /**
     * @return the msgVersion
     */
    public byte getMsgVersion() {
	return this.msgVersion;
    }

    /**
     * @return the icao
     */
    public int getIcao() {
	return this.icao;
    }

    /**
     * @return the emitterType
     */
    public Emitter getEmitterType() {
	return this.emitterType;
    }

    /**
     * @return the callsign
     */
    public String getCallsign() {
	return this.callsign;
    }

    /**
     * @return the stallSpeed
     */
    public float getStallSpeed() {
	return this.stallSpeed;
    }

    /**
     * @return the avLw
     */
    public AircraftLengthWeight getAvLw() {
	return this.avLw;
    }

    /**
     * @return the antOfslat
     */
    public LateralGpsOfs getAntOfslat() {
	return this.antOfslat;
    }

    /**
     * @return the antOfsLon
     */
    public int getAntOfsLon() {
	return this.antOfsLon;
    }

    /**
     * @return the qiMode
     */
    public boolean isQiMode() {
	return this.qiMode;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public UavionixOem(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 4, (byte) 117);
	this.signature = Character.highSurrogate(getByte());
	this.subType = getByte();
	switch (this.subType) {
	case 38:
	    // QI Mode
	    this.msgVersion = (byte) getByte();
	    int msgSize = this.msgVersion == 1 ? 4 : 29;
	    if (is.available() < (msgSize + 1)) {
		throw new UnsupportedEncodingException("Message too short: expected " + msgSize + " but received " + (is.available() - 1));
	    }
	    this.qiMode = getByte() == 0;
	    if (this.msgVersion > 1) {
		getInt();
		getInt();
		getInt();
		getInt();
		getInt();
		getInt();
		getInt();
	    }

	    this.stallSpeed = -1;
	    this.icao = -1;
	    this.emitterType = Emitter.Unknown;
	    this.callsign = "";
	    this.avLw = AircraftLengthWeight.NO_DATA;
	    this.antOfslat = LateralGpsOfs.NO_DATA;
	    this.antOfsLon = -1;
	    break;
	case 0xfe:
	    // System Command - Enter Update Mode
	    this.msgVersion = (byte) getByte();
	    msgSize = 8;
	    if (is.available() < (msgSize + 1)) {
		throw new UnsupportedEncodingException("Message too short: expected " + msgSize + " but received " + (is.available() - 1));
	    }

	default:
	    this.qiMode = false;
	    this.icao = (getByte() << 16) + (getByte() << 8) + getByte();
	    this.emitterType = Gdl90Message.emitterLookup[(byte) getByte()];
	    final StringBuilder sb = new StringBuilder();
	    for (int i = 8; i < 16; i++) {
		sb.append((char) getByte());
	    }
	    this.callsign = sb.toString().trim();
	    this.stallSpeed = getByte() / 100f;
	    this.avLw = Gdl90Message.AircraftLengthWeightLookup[getByte()];
	    final byte b = (byte) getByte();
	    this.antOfslat = Gdl90Message.lateralGpsOfsLookup[b >> 5];
	    this.antOfsLon = b & 0x1f;
	}
	checkCrc();
    }

    public String toString() {
	final String antOffsetLon = this.antOfsLon == 0 ? "NO_DATA" : this.antOfsLon == 1 ? "Applied by sensor" : String.format(Locale.ENGLISH, "%dm", (this.antOfsLon * 2) - 1);
	return String.format(Locale.ENGLISH, "I%c: %c %d %d %o %s %s %.0f %s %s %s %c",
		crcValidChar(),
		this.signature, this.subType, this.msgVersion, this.icao, this.emitterType, this.callsign, this.stallSpeed,
		this.avLw, this.antOfslat, antOffsetLon, this.qiMode ? 'Q' : ' ');
    }
}
