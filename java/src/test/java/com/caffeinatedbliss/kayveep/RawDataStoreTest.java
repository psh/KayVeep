package com.caffeinatedbliss.kayveep;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/22/12 at 11:38 PM
 */
public class RawDataStoreTest {
	private RawDataStore store;
	protected String tmp;
	protected String dataDir;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		dataDir = new File(tmp, "raw").getAbsolutePath();
		store = new RawDataStore();
		store.setDataDir(tmp);
	}

	@After
	public void tearDown() {
		TestUtilities.deleteFileIfExists(dataDir, "key.json");
		TestUtilities.deleteDir(dataDir);
	}

	@Test
	public void testStore() throws IOException {
		store.store("key", "data");

		File theFile = new File(dataDir, "key.raw");
		assertTrue(theFile.exists());

		String content = FileUtils.readFileToString(theFile);
		assertEquals("data", content);
	}

	@Test
	public void testLoad() {
		store.store("key", "data");

		assertEquals("data", store.load("key"));
	}

	@Test
	public void testDelete() {
		store.store("key", "data");
		store.delete("key");

		File theFile = new File(dataDir, "key.raw");
		assertFalse(theFile.exists());
		assertNull(store.load("key"));
	}

	@Test
	public void testStoreAndLoadBytes() {
		byte[] data = new byte[]{0, 1, 2};
		store.store("key", data);

		File theFile = new File(dataDir, "key.raw");
		assertTrue(theFile.exists());

		byte[] input = store.loadBytes("key");
		assertNotNull(input);
		assertEquals(data.length, input.length);
		assertArrayEquals(data, input);
	}
}
