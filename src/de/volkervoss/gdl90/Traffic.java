/*
 * Copyright 2026 Volker Voss volker.v@gmx.net
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

public class Traffic extends OwnShipReport {

    /**
     *
     * @param point
     * @param is
     * @throws UnsupportedEncodingException
     */
    public Traffic(final Position point, final ByteArrayInputStream is) throws UnsupportedEncodingException {
	super(point, is, (byte) 20);
    }
}
