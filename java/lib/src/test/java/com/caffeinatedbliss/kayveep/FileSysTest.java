package com.caffeinatedbliss.kayveep;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * FileSysTest
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
public class FileSysTest {
	private final FileSys fileSys = new FileSys();
	private String tmp;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
	}

	@Test
	public void testMakingDirectories() {
		tmp = TestUtilities.getTmpDir();
		File oneDown = new File(tmp, "one.tmp");
		File twoDown = new File(oneDown, "two.tmp");

		fileSys.mkdir(twoDown.getAbsolutePath());

		assertTrue(oneDown.exists());
		assertTrue(twoDown.exists());
	}
}
