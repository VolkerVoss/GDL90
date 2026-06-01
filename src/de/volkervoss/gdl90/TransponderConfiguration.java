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
import java.util.ArrayList;

public class TransponderConfiguration extends Gdl90Message {

    private final byte msgVersion;
    private final int sil, sda, maxSpeed;
    private final boolean extBarometer, testMode;
    private final int participantAddr;
    private final boolean tx1090ES, modeSReply, modeCReply, modeAReply;
    private final AdsbIn adsbIn;
    private final AircraftLengthWeight avLw;
    private final LateralGpsOfs antOfslat;
    private final int antOfsLon;
    private final String callsign;
    private final float stallSpeed;
    private final Emitter emitterType;
    private final int baudRate;
    private long validityMask;
    private boolean baro100ftResolution;
    private final int modeASquawk;

    final static int[] maxSpeedLookup = { 0, 75, 150, 300, 600, 1200, 10000 };
    final static int[] baudrateLookup = { 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200, 921600 };

    public enum AdsbIn {
	None, MHz1090, MHz978, Both
    }

    public final AdsbIn[] adsbInLookup = { AdsbIn.None, AdsbIn.MHz1090, AdsbIn.MHz978, AdsbIn.Both };

    public enum Protocol {
	UCP, UCP_HD, Apollo, Mavlink
    }

    public ArrayList<Protocol> inputProtocol, outputProtocol;

    /**
     * @return the msgVersion
     */
    public byte getMsgVersion() {
	return this.msgVersion;
    }

    /**
     * @return the sil
     */
    public int getSil() {
	return this.sil;
    }

    /**
     * @return the sda
     */
    public int getSda() {
	return this.sda;
    }

    /**
     * @return the maxSpeed
     */
    public int getMaxSpeed() {
	return this.maxSpeed;
    }

    /**
     * @return the extBarometer
     */
    public boolean isExtBarometer() {
	return this.extBarometer;
    }

    /**
     * @return the testMode
     */
    public boolean isTestMode() {
	return this.testMode;
    }

    /**
     * @return the participantAddr
     */
    public int getParticipantAddr() {
	return this.participantAddr;
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
     * @return the adsbIn
     */
    public AdsbIn getAdsbIn() {
	return this.adsbIn;
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
     * @return the emitterType
     */
    public Emitter getEmitterType() {
	return this.emitterType;
    }

    /**
     * @return the baudRate
     */
    public int getBaudRate() {
	return this.baudRate;
    }

    /**
     * @return the validityMask
     */
    public long getValidityMask() {
	return this.validityMask;
    }

    /**
     * @return the baro100ftResolution
     */
    public boolean isBaro100ftResolution() {
	return this.baro100ftResolution;
    }

    /**
     * @return the modeASquawk
     */
    public int getModeASquawk() {
	return this.modeASquawk;
    }

    /**
     * @return the maxspeedlookup
     */
    public static int[] getMaxspeedlookup() {
	return TransponderConfiguration.maxSpeedLookup;
    }

    /**
     * @return the baudratelookup
     */
    public static int[] getBaudratelookup() {
	return TransponderConfiguration.baudrateLookup;
    }

    /**
     * @return the adsbInLookup
     */
    public AdsbIn[] getAdsbInLookup() {
	return this.adsbInLookup;
    }

    /**
     * @return the inputProtocol
     */
    public ArrayList<Protocol> getInputProtocol() {
	return this.inputProtocol;
    }

    /**
     * @return the outputProtocol
     */
    public ArrayList<Protocol> getOutputProtocol() {
	return this.outputProtocol;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public TransponderConfiguration(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 19, (byte) 43);
	this.msgVersion = (byte) getByte();
	// noinspection ConditionalExpressionWithIdenticalBranches
	final int msgSize = this.msgVersion == 1 ? 19 : this.msgVersion == 2 ? 21 : this.msgVersion == 3 ? 22 : this.msgVersion == 4 ? 29 : 29;
	if (is.available() < (msgSize + 1)) {
	    throw new UnsupportedEncodingException("Message too short: expected " + msgSize + " but received " + (is.available() - 1));
	}
	this.participantAddr = (getByte() << 16) + (getByte() << 8) + getByte();
	byte b = (byte) getByte();
	this.sil = (b >> 6) & 0x03;
	this.sda = (b >> 4) & 0x03;
	this.extBarometer = (b & 0x08) != 0;
	this.maxSpeed = TransponderConfiguration.maxSpeedLookup[(b & 0x07)];
	b = (byte) getByte();
	this.testMode = ((b >> 6) & 0x03) != 0;
	this.adsbIn = this.adsbInLookup[(b >> 4) & 0x03];
	this.avLw = Gdl90Message.AircraftLengthWeightLookup[b & 0x0f];
	b = (byte) getByte();
	this.antOfslat = Gdl90Message.lateralGpsOfsLookup[b >> 5];
	this.antOfsLon = b & 0x1f;
	this.callsign = getString(8).trim();
	this.stallSpeed = getByte() / 100f;
	this.emitterType = Gdl90Message.emitterLookup[(byte) getByte()];
	b = (byte) getByte();
	this.tx1090ES = (b & 0x80) != 0;
	this.modeSReply = (b & 0x40) != 0;
	this.modeCReply = (b & 0x20) != 0;
	this.modeAReply = (b & 0x10) != 0;
	this.baudRate = TransponderConfiguration.baudrateLookup[b & 0x0f];
	if (this.msgVersion > 1) {
	    this.modeASquawk = getShort();
	    if (this.msgVersion > 2) {
		this.validityMask = getInt();
		if (this.msgVersion > 3) {
		    this.baro100ftResolution = (getByte() & 0x80) != 0;
		    this.inputProtocol = protocols(getShort());
		    this.outputProtocol = protocols(getShort());
		    // noinspection StatementWithEmptyBody
		    if (this.msgVersion > 4) {
			// 5 is the same as 4
		    }
		}
	    }
	} else {
	    this.modeASquawk = -1;
	}

	checkCrc();
    }

    private ArrayList<Protocol> protocols(final int b) {
	final ArrayList<Protocol> result = new ArrayList<>();
	if ((b & 0x02) != 0) {
	    result.add(Protocol.UCP);
	}
	if ((b & 0x0400) != 0) {
	    result.add(Protocol.UCP_HD);
	}
	if ((b & 0x0200) != 0) {
	    result.add(Protocol.Apollo);
	}
	if ((b & 0x01) != 0) {
	    result.add(Protocol.Mavlink);
	}
	return result;
    }

    public String toString() {
	return String.format("C%c: %d %d %d %d %06x %s %c%c%c%c%c%c %d %s %.0fkts %s %s %s %s %s",
		crcValidChar(), this.msgVersion, this.sil, this.sda, this.maxSpeed,
		this.participantAddr, this.callsign,
		this.tx1090ES ? 'T' : '.',
		this.modeSReply ? 'S' : '.',
		this.modeCReply ? 'C' : '.',
		this.modeAReply ? 'A' : '.',
		this.testMode ? 'T' : '.',
		this.extBarometer ? 'B' : '.',
		this.baudRate,
		this.emitterType, this.stallSpeed,
		this.avLw, this.antOfslat, this.antOfsLon, this.adsbIn,
		this.msgVersion < 2 ? ""
			: String.format("%04d %s", this.modeASquawk,
				this.msgVersion < 3 ? ""
					: String.format("%08x %s %s", this.validityMask, this.baro100ftResolution ? "100ft" : "25ft",
						this.msgVersion < 4 ? "" : "" + this.inputProtocol.size() + ", " + this.outputProtocol.size())));
    }
}
