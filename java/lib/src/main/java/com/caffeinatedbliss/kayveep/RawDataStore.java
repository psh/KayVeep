package com.caffeinatedbliss.kayveep;

import java.io.File;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/22/12 at 11:36 PM
 */
public class RawDataStore extends DataStore {
	public RawDataStore() {
		super("raw");
	}

	@Override
	public void setDataDir(String dir) {
		super.setDataDir(new File(dir, "raw").getAbsolutePath());
	}
}
