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

public class Position {

    private Double latitude;
    private Double longitude;
    private Double altitude;

    private Double speed;
    private Double course;

    private Boolean crcValid;
    private Boolean airborne;
    private Double accuracy;
    private Double mps;

    /**
     * @return the latitude
     */
    public Double getLatitude() {
	return this.latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(final Double latitude) {
	this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
	return this.longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(final Double longitude) {
	this.longitude = longitude;
    }

    /**
     * @return the altitude
     */
    public Double getAltitude() {
	return this.altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(final Double altitude) {
	this.altitude = altitude;
    }

    /**
     * @return the speed
     */
    public Double getSpeed() {
	return this.speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(final Double speed) {
	this.speed = speed;
    }

    /**
     * @return the course
     */
    public Double getCourse() {
	return this.course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(final Double course) {
	this.course = course;
    }

    /**
     * @return the crcValid
     */
    public boolean isCrcValid() {
	return this.crcValid;
    }

    /**
     * @param crcValid the crcValid to set
     */
    public void setCrcValid(final boolean crcValid) {
	this.crcValid = crcValid;
    }

    /**
     * @return the airborne
     */
    public boolean isAirborne() {
	return this.airborne;
    }

    /**
     * @param airborne the airborne to set
     */
    public void setAirborne(final boolean airborne) {
	this.airborne = airborne;
    }

    /**
     * @return the accuracy
     */
    public Double getAccuracy() {
	return this.accuracy;
    }

    /**
     * @param accuracy the accuracy to set
     */
    public void setAccuracy(final Double accuracy) {
	this.accuracy = accuracy;
    }

    /**
     * @return the mps
     */
    public Double getMps() {
	return this.mps;
    }

    /**
     * @param mps the mps to set
     */
    public void setMps(final Double mps) {
	this.mps = mps;
    }

}