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

public class Units {

    public final String label;
    public final float factor;

    private Units(final String label, final float factor) {
	this.label = label;
	this.factor = factor;
    }

    private String toString(double value) {
	if (Double.isNaN(value)) {
	    return "----";
	}
	value /= this.factor;
	return String.format(Math.abs(value) < 10 ? "%.1f%s" : "%,.0f%s", value, this.label);
    }

    private String toString(final String format, final double value) {
	if (Double.isNaN(value)) {
	    return "----";
	}
	return String.format(format, value / this.factor, this.label);
    }

    private double toStandard(final double value) {
	if (Double.isNaN(value)) {
	    return Double.NaN;
	}
	return value * this.factor;
    }

    private double fromStandard(final double value) {
	if (Double.isNaN(value)) {
	    return Double.NaN;
	}
	return value / this.factor;
    }

    public enum Speed {
	MPS("mps", 1),
	KPH("kph", 1000f / 3600),
	KNOTS("kts", 0.5144444f);

	public final Units units;

	Speed(final String label, final float factor) {
	    this.units = new Units(label, factor);
	}

	public String toString(final float value) {
	    return this.units.toString(value);
	}

	public double toMps(final double value) {
	    return this.units.toStandard(value);
	}

	public double fromMps(final double value) {
	    return this.units.fromStandard(value);
	}
    }

    public enum VertSpeed {
	MPS("mps", 1),
	FPM("fpm", 0.00508f);

	public final Units units;

	VertSpeed(final String label, final float factor) {
	    this.units = new Units(label, factor);
	}

	public String toString(final double value) {
	    return this.units.toString("%+.0f", value);
	}

	public double toMps(final float value) {
	    return this.units.toStandard(value);
	}

	public double fromMps(final float value) {
	    return this.units.fromStandard(value);
	}
    }

    public enum Height {
	M("m", 1),
	FT("ft", .3048f);

	public final Units units;

	Height(final String label, final float factor) {
	    this.units = new Units(label, factor);
	}

	public double toM(final double value) {
	    return this.units.toStandard(value);
	}

	public double fromM(final double value) {
	    return this.units.fromStandard(value);
	}

	public String toString(final double value) {
	    return this.units.toString("%+,.0f%s", value);
	}

	public String toString(final String format, final double value) {
	    return this.units.toString(format, value);
	}
    }

    public enum Distance {
	M("m", 1),
	KM("km", 1000f),
	NM("nm", 1852f);

	public final Units units;

	Distance(final String label, final float factor) {
	    this.units = new Units(label, factor);
	}

	public double toM(final float value) {
	    return this.units.toStandard(value);
	}

	public double fromM(final double value) {
	    return this.units.fromStandard(value);
	}

	public String toString(final double value) {
	    return this.units.toString(value);
	}
    }

}
