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

public class Heartbeat extends Gdl90Message {

    private final byte status1;
    private final boolean validPos;
    private final boolean maintReq;
    private final boolean ident;
    private final boolean addrType;
    private final boolean lowBatt;
    private final boolean ratcs;
    private final boolean reserved;
    private final boolean init;
    private final byte status2;
    private final boolean timestampMS;
    private final boolean csaReq;
    private final boolean csaNA;
    private final boolean reserved4, reserved3, reserved2, reserved1;
    private final boolean utcOk;
    private final int hour, minute, second;
    private final int uplinkMessages;
    private final int reservedCounts;
    private final int basicLongMessageCounts;

    /**
     * @return the status1
     */
    public byte getStatus1() {
	return this.status1;
    }

    /**
     * @return the validPos
     */
    public boolean isValidPos() {
	return this.validPos;
    }

    /**
     * @return the maintReq
     */
    public boolean isMaintReq() {
	return this.maintReq;
    }

    /**
     * @return the ident
     */
    public boolean isIdent() {
	return this.ident;
    }

    /**
     * @return the addrType
     */
    public boolean isAddrType() {
	return this.addrType;
    }

    /**
     * @return the lowBatt
     */
    public boolean isLowBatt() {
	return this.lowBatt;
    }

    /**
     * @return the ratcs
     */
    public boolean isRatcs() {
	return this.ratcs;
    }

    /**
     * @return the reserved
     */
    public boolean isReserved() {
	return this.reserved;
    }

    /**
     * @return the init
     */
    public boolean isInit() {
	return this.init;
    }

    /**
     * @return the status2
     */
    public byte getStatus2() {
	return this.status2;
    }

    /**
     * @return the timestampMS
     */
    public boolean isTimestampMS() {
	return this.timestampMS;
    }

    /**
     * @return the csaReq
     */
    public boolean isCsaReq() {
	return this.csaReq;
    }

    /**
     * @return the csaNA
     */
    public boolean isCsaNA() {
	return this.csaNA;
    }

    /**
     * @return the reserved4
     */
    public boolean isReserved4() {
	return this.reserved4;
    }

    /**
     * @return the reserved3
     */
    public boolean isReserved3() {
	return this.reserved3;
    }

    /**
     * @return the reserved2
     */
    public boolean isReserved2() {
	return this.reserved2;
    }

    /**
     * @return the reserved1
     */
    public boolean isReserved1() {
	return this.reserved1;
    }

    /**
     * @return the utcOk
     */
    public boolean isUtcOk() {
	return this.utcOk;
    }

    /**
     * @return the hour
     */
    public int getHour() {
	return this.hour;
    }

    /**
     * @return the minute
     */
    public int getMinute() {
	return this.minute;
    }

    /**
     * @return the second
     */
    public int getSecond() {
	return this.second;
    }

    /**
     * @return the uplinkMessages
     */
    public int getUplinkMessages() {
	return this.uplinkMessages;
    }

    /**
     * @return the reservedCounts
     */
    public int getReservedCounts() {
	return this.reservedCounts;
    }

    /**
     * @return the basicLongMessageCounts
     */
    public int getBasicLongMessageCounts() {
	return this.basicLongMessageCounts;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public Heartbeat(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 7, (byte) 0);
	this.status1 = (byte) getByte();
	this.validPos = (this.status1 & 0x80) != 0;
	this.maintReq = (this.status1 & 0x40) != 0;
	this.ident = (this.status1 & 0x20) != 0;
	this.addrType = (this.status1 & 0x10) != 0;
	this.lowBatt = (this.status1 & 0x08) != 0;
	this.ratcs = (this.status1 & 0x04) != 0;
	this.reserved = (this.status1 & 0x02) != 0;
	this.init = (this.status1 & 0x01) != 0;
	this.status2 = (byte) getByte();
	this.timestampMS = (this.status2 & 0x80) != 0;
	this.csaReq = (this.status2 & 0x40) != 0;
	this.csaNA = (this.status2 & 0x20) != 0;
	this.reserved4 = (this.status2 & 0x10) != 0;
	this.reserved3 = (this.status2 & 0x08) != 0;
	this.reserved2 = (this.status2 & 0x04) != 0;
	this.reserved1 = (this.status2 & 0x02) != 0;
	this.utcOk = (this.status2 & 0x01) != 0;
	final int timestamp = getByte() + (getByte() << 8) + ((this.status2 & 0x80) << 9);
	this.hour = timestamp / 3600;
	this.minute = (timestamp % 3600) / 60;
	this.second = timestamp % 60;
	final byte message5 = (byte) getByte();
	this.uplinkMessages = message5 >> 3;
	this.reservedCounts = (message5 & 0x04) >> 2;
	this.basicLongMessageCounts = (message5 & 0x03) << (8 + getByte());
	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "H%c: %02x %c%c%c%c%c%c%c%c %02x %c%c%c%c%c%c%c%c %02d:%02d:%02d %d %d %d",
		crcValidChar(),
		this.status1,
		this.validPos ? 'V' : '.', this.maintReq ? 'M' : '.', this.ident ? 'I' : '.', this.addrType ? 'T' : '.',
		this.lowBatt ? 'B' : '.', this.ratcs ? 'A' : '.', this.reserved ? 'R' : '.', this.init ? 'I' : '.',
		this.status2,
		this.timestampMS ? 'T' : '.', this.csaReq ? 'C' : '.', this.csaNA ? 'N' : '.', this.reserved4 ? 'R' : '.',
		this.reserved3 ? 'R' : '.', this.reserved2 ? 'R' : '.', this.reserved1 ? 'R' : '.', this.utcOk ? 'U' : '.',
		this.hour, this.minute, this.second, this.uplinkMessages, this.reservedCounts, this.basicLongMessageCounts);
    }
}
