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

public class OwnShipGeometricAltitude extends Gdl90Message {

    private final boolean warning;
    private final int alt;
    private final int vfom;

    /**
     * @return the warning
     */
    public boolean isWarning() {
	return this.warning;
    }

    /**
     * @return the alt
     */
    public int getAlt() {
	return this.alt;
    }

    /**
     * @return the vfom
     */
    public int getVfom() {
	return this.vfom;
    }

    /**
     * uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf
     *
     * @param is
     * @throws UnsupportedEncodingException
     */
    public OwnShipGeometricAltitude(final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 4, (byte) 11);
	this.alt = getShort();
	final byte message3 = (byte) getByte();
	this.warning = (message3 & 0x80) != 0;
	this.vfom = (message3 & 0x7f) << (8 + getByte());
	checkCrc();
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "A%c: %d %c %s",
		crcValidChar(), this.alt, this.warning ? 'W' : '.',
		this.vfom == 0x7fff ? "Unknown" : ("" + this.vfom));
    }
}
