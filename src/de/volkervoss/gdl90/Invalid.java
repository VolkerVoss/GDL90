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
import java.util.Locale;

public class Invalid extends Gdl90Message {

    final ArrayList<Byte> data;

    public Invalid(final byte messageId, final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(is, 0, messageId);
	this.data = new ArrayList<>();
	do {
	    final byte b = (byte) is.read();
	    if ((b & 0x7f) == 0x7e) {
		break;
	    }
	    this.data.add(b);
	} while (is.available() > 0);
    }

    public String toString() {
	final StringBuilder sb = new StringBuilder(String.format(Locale.ENGLISH, "%3d: ", this.messageId));
	for (final Byte b : this.data) {
	    sb.append(String.format("%02x", b));
	}
	return sb.toString();

    }
}
