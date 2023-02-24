package com.caffeinatedbliss.kayveep;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/22/12 at 10:55 PM
 */
public class DataStoreTest {
	private DataStore store;
	protected String tmp;
	protected String dataDir;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		dataDir = tmp;
		store = new DataStore();
		store.setDataDir(tmp);
	}

	@After
	public void tearDown() {
		TestUtilities.deleteFileIfExists(dataDir, "key.json");
	}

	@Test
	public void testStore() throws IOException {
		store.store("key", "data");

		File theFile = new File(tmp, "key.json");
		assertTrue(theFile.exists());

		String content = FileUtils.readFileToString(theFile, Charset.defaultCharset());
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

		File theFile = new File(tmp, "key.json");
		assertFalse(theFile.exists());

		assertNull(store.load("key"));
	}

	@Test
	public void testGettingKeys() {
		KayVeepUtilities kayVeepUtilities = new KayVeepUtilities();

		String key1 = kayVeepUtilities.getKey();
		assertNotNull(key1);

		String key2 = kayVeepUtilities.getKey();
		assertNotNull(key2);

		assertFalse(key1.equals(key2));
	}

	@Test
	public void testStoreAndLoadBytes() {
		byte[] data = new byte[]{0, 1, 2};
		store.store("key", data);

		byte[] input = store.loadBytes("key");
		assertNotNull(input);
		assertEquals(data.length, input.length);

		assertArrayEquals(data, input);
	}
}
