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
import java.io.File;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.ZipJarCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.models.earth.GeoMagneticField;
import org.orekit.models.earth.GeoMagneticFieldFactory;

public class Tester {

    private static byte rawData1[] = {
	    (byte) 0x7E, (byte) 0x0A, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	    (byte) 0xFF, (byte) 0xF1, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	    (byte) 0x00, (byte) 0x00, (byte) 0x2A, (byte) 0x2A, (byte) 0x2A, (byte) 0x2A,
	    (byte) 0x2A, (byte) 0x2A, (byte) 0x2A, (byte) 0x2A, (byte) 0x00, (byte) 0x7A,
	    (byte) 0xA8, (byte) 0x7E, (byte) 0x7E, (byte) 0x14, (byte) 0x00, (byte) 0xC0,
	    (byte) 0x12, (byte) 0x71, (byte) 0x26, (byte) 0x15, (byte) 0xF1, (byte) 0x05,
	    (byte) 0xFC, (byte) 0x1D, (byte) 0x2D, (byte) 0x49, (byte) 0x9A, (byte) 0x14,
	    (byte) 0x90, (byte) 0x02, (byte) 0x91, (byte) 0x02, (byte) 0x53, (byte) 0x4B,
	    (byte) 0x59, (byte) 0x48, (byte) 0x57, (byte) 0x4B, (byte) 0x32, (byte) 0x20,
	    (byte) 0x06, (byte) 0x45, (byte) 0xF7, (byte) 0x7E
    };

    private static byte rawData2[] = {
	    (byte) 0x7E, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	    (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x6D, (byte) 0x7E, (byte) 0x7E,
	    (byte) 0x25, (byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x30, (byte) 0x15,
	    (byte) 0xA1, (byte) 0x28, (byte) 0xD4, (byte) 0x30, (byte) 0x00, (byte) 0x00,
	    (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
	    (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
	    (byte) 0xFF, (byte) 0xFF, (byte) 0xDA, (byte) 0xBC, (byte) 0x7E
    };

    public static void main(final String[] args) {

	Gdl90Message message = Gdl90Message.getMessage(new ByteArrayInputStream(Tester.rawData1));
	System.out.println("Data1:" + message);

	message = Gdl90Message.getMessage(new ByteArrayInputStream(Tester.rawData2));
	System.out.println("Data2:" + message);
    
    	try {
            DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
            
            // Pfad zu deiner neuen, selbst gepackten ZIP-Datei
            File orekitDataZip = new File("libs/orekit-data.zip");

            if (orekitDataZip.exists()) {
         
                manager.addProvider(new ZipJarCrawler(orekitDataZip));
                System.out.println("Orekit data successfully loaded via ZipJarCrawler: " + orekitDataZip.getAbsolutePath());
            } else {
                System.err.println("Error: orekit-data.zip was not found in: " + orekitDataZip.getAbsolutePath());
                return;
            }

            // =================================================================
            // Berechnungs-Block
            // =================================================================
            System.out.println("Loading the World Magnetic Model (WMM) for the year 2026...");
            
            // Da du die WMM.COF aktualisiert hast, läuft das Jahr 2026 jetzt sauber durch
            GeoMagneticField wmm = GeoMagneticFieldFactory.getWMM(2026.0);
            
            System.out.println("SUCCESS! WMM model loaded: " + wmm.getModelName());

        } catch (OrekitException e) {
            System.err.println("An Orekit error has occurred:");
            e.printStackTrace();
        }

    
    System.out.println(GeoMagneticFieldFactory.getWMM(2025.0).calculateField(55, 8, 0).getDeclination());
    
    }
}
