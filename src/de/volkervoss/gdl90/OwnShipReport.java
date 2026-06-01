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

import org.orekit.models.earth.GeoMagneticFieldFactory;

public class OwnShipReport extends Gdl90Message {

    protected final Emitter emitterType;
    protected final boolean ownShip;
    protected final int alertStatus;
    protected final AddrType addrType;
    protected final int participantAddr;
    protected final Position point;
    protected final boolean extrapolated;
    protected final int nic;
    protected final int nac;
    protected final String callsign;
    protected final Priority priority;
    protected final boolean airborne;

    public enum TrackType {
	Invalid, TRK, Mag, True
    }

    public enum AddrType {
	ICAO_ADSB, self_ADSB, ICAO_TISB, file_TISB, SFC_Vehicle, GND_Beacon, Reserved
    }

    final static protected AddrType[] AddrTypeLookup = new AddrType[] { AddrType.ICAO_ADSB, AddrType.self_ADSB, AddrType.ICAO_TISB, AddrType.file_TISB, AddrType.SFC_Vehicle, AddrType.GND_Beacon, AddrType.Reserved };
    final static protected TrackType[] trackTypeLookup = new TrackType[] { TrackType.Invalid, TrackType.TRK, TrackType.Mag, TrackType.True };

    // uAvionix - uAvionix-UCP-Transponder-ICD-Rev-Q.pdf 6.21 (Ownship) & 6.2.

    /**
     * @return the emitterType
     */
    public Emitter getEmitterType() {
	return this.emitterType;
    }

    /**
     * @return the ownShip
     */
    public boolean isOwnShip() {
	return this.ownShip;
    }

    /**
     * @return the alertStatus
     */
    public int getAlertStatus() {
	return this.alertStatus;
    }

    /**
     * @return the addrType
     */
    public AddrType getAddrType() {
	return this.addrType;
    }

    /**
     * @return the participantAddr
     */
    public int getParticipantAddr() {
	return this.participantAddr;
    }

    /**
     * @return the point
     */
    public Position getPoint() {
	return this.point;
    }

    /**
     * @return the extrapolated
     */
    public boolean isExtrapolated() {
	return this.extrapolated;
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
     * @return the callsign
     */
    public String getCallsign() {
	return this.callsign;
    }

    /**
     * @return the priority
     */
    public Priority getPriority() {
	return this.priority;
    }

    /**
     * @return the airborne
     */
    public boolean isAirborne() {
	return this.airborne;
    }

    /**
     *
     * @param point
     * @param is
     * @throws UnsupportedEncodingException
     */
    public OwnShipReport(final Position point, final ByteArrayInputStream is) throws UnsupportedEncodingException {
	this(point, is, (byte) 10);
    }

    /**
     * For internal use (Traffic message)
     *
     * @param point
     * @param is
     * @param messageID
     * @throws UnsupportedEncodingException
     */
    protected OwnShipReport(final Position point, final ByteArrayInputStream is, final byte messageID) throws UnsupportedEncodingException {
	super(is, 28, messageID);
	this.point = point;
	this.ownShip = this.messageId == 10;
	short b = getByte();
	this.alertStatus = b >>> 4;
	b &= 0x0f;
	final int addrTypeNum = b < AddrType.values().length ? b : AddrType.values().length - 1;
	this.addrType = OwnShipReport.AddrTypeLookup[addrTypeNum];
	this.participantAddr = (addrTypeNum << 24) + (getByte() << 16) + (getByte() << 8) + getByte();
	double lat = get3BytesDegrees();
	double lon = get3BytesDegrees();

	int alt = getByte() << 4;
	b = getByte();
	alt += (b & 0xf0) >> 4; // MSB first
	// The 0xFFF value represents that the pressure altitude is invalid.
	if (alt == 0xfff) {
	    alt = -100000;
	} else {
	    alt = (alt * 25) - 1000;
	}

	// Misc bitmap
	final TrackType trackType = OwnShipReport.trackTypeLookup[b & 0x03];
	this.extrapolated = (b & 0x04) != 0;
	this.airborne = (b & 0x08) != 0;

	b = getByte();
	this.nic = b >> 4;
	this.nac = b & 0x0f;
	// A target with no valid position has Latitude, Longitude, and NIC all set to zero.
	if ((this.nic == 0) && (lat == 0) && (lon == 0)) {
	    lat = Double.NaN;
	    lon = Double.NaN;
	}
	int hSpeed = getByte() << 4; // MSB first
	b = getByte();
	hSpeed += ((b & 0xf0) >> 4);
	int vVel = ((b & 0x0f) << 8) + getByte(); // MSB first, signed
	if ((vVel & 0x800) != 0) {
	    vVel -= 0x1000;
	}
	vVel *= 64;

	final float track = (getByte() * 360.0f) / 256;
	this.emitterType = Gdl90Message.emitterLookup[getByte()];
	this.callsign = getString(8).trim();
	b = getByte();
	final int p = b >> 4;
	this.priority = Gdl90Message.priorityLookup[p < Priority.values().length ? p : Priority.values().length - 1];
	checkCrc();
	point.setLatitude(lat);
	point.setLongitude(lon);
	if (Double.isNaN(lat) || Double.isNaN(lon)) {
	    point.setAccuracy(null);
	} else {
	    point.setAccuracy(20.0);
	}
	if (alt < -1000) {
	    point.setAltitude(null);
	} else {
	    point.setAltitude(Units.Height.FT.toM(alt));
	}
	if (hSpeed == 0xfff) {
	    point.setSpeed(null);
	    point.setCourse(null);
	} else {
	    point.setSpeed(Units.Speed.KNOTS.toMps(hSpeed));
	    point.setCourse(trueTrack(track, trackType, lat, lon, alt));
	}
//	point.setVVel(Units.VertSpeed.FPM.toMps(vVel));
	point.setCrcValid(this.crcValid);
	point.setAirborne(this.airborne);

    }

    protected static double trueTrack(final double track, final TrackType trackType, final double lat, final double lon, final int alt) {
	return switch (trackType) {
	// Heading rather than track
	case True -> track;
	case Mag -> {
	    yield (track + (float) GeoMagneticFieldFactory.getWMM(2025.0).calculateField(lat, lon, alt).getDeclination()) % 360;
	}

	// Track
	case TRK -> track;
	case Invalid -> Float.NaN;
	};
    }

    public String toString() {
	return String.format(Locale.ENGLISH, "%c%c: %8s %s %s %s NIC=%2d NAC=%2d %s %s %o",
		this.ownShip ? 'O' : 'T', crcValidChar(),
		this.callsign, this.point,
		this.priority, (this.alertStatus == 0 ? "No alert" : "Traffic Alert"), this.nic, this.nac, (this.extrapolated ? "Extrap" : "Report"),
		this.addrType, this.participantAddr);
    }
}
