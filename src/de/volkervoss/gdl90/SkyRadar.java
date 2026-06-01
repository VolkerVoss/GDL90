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

public class SkyRadar extends Gdl90Message {

    private final byte fwVersion, debugData, hours, minutes;
    private final char fixQuality;
    private final long numMessages;
    private final int debugData2;
    private final byte hwVersion, hwStatus;
    private final int reserved;
    private final long recvHwId;
    private final double hdop;

    /**
     * @return the fwVersion
     */
    public byte getFwVersion() {
	return this.fwVersion;
    }

    /**
     * @return the debugData
     */
    public byte getDebugData() {
	return this.debugData;
    }

    /**
     * @return the hours
     */
    public byte getHours() {
	return this.hours;
    }

    /**
     * @return the minutes
     */
    public byte getMinutes() {
	return this.minutes;
    }

    /**
     * @return the fixQuality
     */
    public char getFixQuality() {
	return this.fixQuality;
    }

    /**
     * @return the numMessages
     */
    public long getNumMessages() {
	return this.numMessages;
    }

    /**
     * @return the debugData2
     */
    public int getDebugData2() {
	return this.debugData2;
    }

    /**
     * @return the hwVersion
     */
    public byte getHwVersion() {
	return this.hwVersion;
    }

    /**
     * @return the hwStatus
     */
    public byte getHwStatus() {
	return this.hwStatus;
    }

    /**
     * @return the recvHwId
     */
    public long getRecvHwId() {
	return this.recvHwId;
    }

    /**
     * @return the hdop
     */
    public double getHdop() {
	return this.hdop;
    }

    /**
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public SkyRadar(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 9, (byte) 101);
	this.fwVersion = (byte) getByte();
	final int msgSize = this.fwVersion < 42 ? 9 : this.fwVersion < 45 ? 11 : 20;

	if (is.available() < (msgSize + 1)) {
	    throw new UnsupportedEncodingException("Message too short: expected " + msgSize + " but received " + (is.available() - 1));
	}
	this.debugData = (byte) getByte();
	this.fixQuality = getChar();
	this.numMessages = getByte() + (getByte() << 8) + (getByte() << 16); // 3 bytes LSB first
	this.hours = (byte) getByte();
	this.minutes = (byte) getByte();
	this.debugData2 = getShort();

	if (this.fwVersion >= 45) {

	    this.hwVersion = (byte) getByte();
	    final int i = getShort();
	    this.hdop = i == 5000 ? Double.NaN : i / 10.0;
	    this.reserved = getShort();
	    this.recvHwId = getLong();
	    this.hwStatus = (byte) getByte();

	} else if (this.fwVersion >= 42) {

	    this.hwVersion = (byte) getByte();
	    this.hdop = -1;
	    this.reserved = -1;
	    this.recvHwId = -1;
	    this.hwStatus = -1;

	} else {
	    this.hwVersion = -1;
	    this.hdop = -1;
	    this.reserved = -1;
	    this.recvHwId = -1;
	    this.hwStatus = -1;
	}

	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "I%c: %d %02x %c %d %02d:%02d %02x %s",
		crcValidChar(), this.fwVersion, this.debugData, this.fixQuality, this.numMessages, this.hours, this.minutes, this.debugData2,
		this.fwVersion < 42 ? ""
			: String.format(Locale.ENGLISH, " %d %s", this.hwVersion,
				this.fwVersion < 45 ? "" : String.format(Locale.ENGLISH, " %f %04x %08x %02x", this.hdop, this.reserved, this.recvHwId, this.hwStatus)));
    }
}
