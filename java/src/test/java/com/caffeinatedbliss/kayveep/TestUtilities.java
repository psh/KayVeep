package com.caffeinatedbliss.kayveep;

import java.io.File;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/25/12 at 4:46 PM
 */
public abstract class TestUtilities {
	public static String getTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}

	public static void deleteFileIfExists(String dir, String filename) {
		File theFile = new File(dir, filename);
		if (theFile.exists()) {
			theFile.delete();
		}
	}

	static void deleteDir(String dir) {
		new File(dir).delete();
	}
}
