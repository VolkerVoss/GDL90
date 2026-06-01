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

public class Identification extends Gdl90Message {

    private final byte msgVersion;
    private final byte priFwMajorVersion, priFwMinorVersion, priFwBuildVersion, priHwId;
    private final byte secFwMajorVersion, secFwMinorVersion, secFwBuildVersion, secHwId;
    private final long priSerialNo, secSerialNo;
    private byte priFwId, secFwId;
    private long priFwCrc, secFwCrc;
    private String priFwPartNo, secFwPartNo;

    /**
     * @return the priFwId
     */
    public byte getPriFwId() {
	return this.priFwId;
    }

    /**
     * @param priFwId the priFwId to set
     */
    public void setPriFwId(final byte priFwId) {
	this.priFwId = priFwId;
    }

    /**
     * @return the secFwId
     */
    public byte getSecFwId() {
	return this.secFwId;
    }

    /**
     * @param secFwId the secFwId to set
     */
    public void setSecFwId(final byte secFwId) {
	this.secFwId = secFwId;
    }

    /**
     * @return the priFwCrc
     */
    public long getPriFwCrc() {
	return this.priFwCrc;
    }

    /**
     * @param priFwCrc the priFwCrc to set
     */
    public void setPriFwCrc(final long priFwCrc) {
	this.priFwCrc = priFwCrc;
    }

    /**
     * @return the secFwCrc
     */
    public long getSecFwCrc() {
	return this.secFwCrc;
    }

    /**
     * @param secFwCrc the secFwCrc to set
     */
    public void setSecFwCrc(final long secFwCrc) {
	this.secFwCrc = secFwCrc;
    }

    /**
     * @return the priFwPartNo
     */
    public String getPriFwPartNo() {
	return this.priFwPartNo;
    }

    /**
     * @param priFwPartNo the priFwPartNo to set
     */
    public void setPriFwPartNo(final String priFwPartNo) {
	this.priFwPartNo = priFwPartNo;
    }

    /**
     * @return the secFwPartNo
     */
    public String getSecFwPartNo() {
	return this.secFwPartNo;
    }

    /**
     * @param secFwPartNo the secFwPartNo to set
     */
    public void setSecFwPartNo(final String secFwPartNo) {
	this.secFwPartNo = secFwPartNo;
    }

    /**
     * @return the msgVersion
     */
    public byte getMsgVersion() {
	return this.msgVersion;
    }

    /**
     * @return the priFwMajorVersion
     */
    public byte getPriFwMajorVersion() {
	return this.priFwMajorVersion;
    }

    /**
     * @return the priFwMinorVersion
     */
    public byte getPriFwMinorVersion() {
	return this.priFwMinorVersion;
    }

    /**
     * @return the priFwBuildVersion
     */
    public byte getPriFwBuildVersion() {
	return this.priFwBuildVersion;
    }

    /**
     * @return the priHwId
     */
    public byte getPriHwId() {
	return this.priHwId;
    }

    /**
     * @return the secFwMajorVersion
     */
    public byte getSecFwMajorVersion() {
	return this.secFwMajorVersion;
    }

    /**
     * @return the secFwMinorVersion
     */
    public byte getSecFwMinorVersion() {
	return this.secFwMinorVersion;
    }

    /**
     * @return the secFwBuildVersion
     */
    public byte getSecFwBuildVersion() {
	return this.secFwBuildVersion;
    }

    /**
     * @return the secHwId
     */
    public byte getSecHwId() {
	return this.secHwId;
    }

    /**
     * @return the priSerialNo
     */
    public long getPriSerialNo() {
	return this.priSerialNo;
    }

    /**
     * @return the secSerialNo
     */
    public long getSecSerialNo() {
	return this.secSerialNo;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public Identification(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 22, (byte) 37);
	this.msgVersion = (byte) getByte();
	final int msgSize = this.msgVersion == 1 ? 18 : this.msgVersion == 2 ? 36 : 66;
	if (is.available() < (msgSize + 1)) {
	    throw new UnsupportedEncodingException(String.format("Message too short: expected %d but received %d", msgSize, is.available() - 1));
	}
	this.priFwMajorVersion = (byte) getByte();
	this.priFwMinorVersion = (byte) getByte();
	this.priFwBuildVersion = (byte) getByte();
	this.priHwId = (byte) getByte();
	this.priSerialNo = getLong();
	this.secFwMajorVersion = (byte) getByte();
	this.secFwMinorVersion = (byte) getByte();
	this.secFwBuildVersion = (byte) getByte();
	this.secHwId = (byte) getByte();
	this.secSerialNo = getLong();
	if (this.msgVersion > 1) {
	    this.priFwId = (byte) getByte();
	    this.priFwCrc = getInt();
	    this.secFwId = (byte) getByte();
	    this.secFwCrc = getInt();
	    if (this.msgVersion > 2) {
		this.priFwPartNo = getString(15).trim();
		this.secFwPartNo = getString(15).trim();
	    }
	}
	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "I%c: msg v%d FW v%d.%d.%d HW %d ser %08x, v%d.%d.%d HW %d ser %08x %s",
		crcValidChar(), this.msgVersion,
		this.priFwMajorVersion, this.priFwMinorVersion, this.priFwBuildVersion, this.priHwId, this.priSerialNo,
		this.secFwMajorVersion, this.secFwMinorVersion, this.secFwBuildVersion, this.secHwId, this.secSerialNo,
		this.msgVersion < 2 ? ""
			: (String.format(Locale.ENGLISH, "FW %d %04x %d %04x ", this.priFwId, this.priFwCrc, this.secFwId, this.secFwCrc) +
				(this.msgVersion < 3 ? "" : String.format(Locale.ENGLISH, "Part# %s,%s", this.priFwPartNo, this.secFwPartNo))));
    }
}
