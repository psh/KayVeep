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
 *         On: 3/3/12 at 2:45 AM
 */
public class ShortNameDataStoreTest {
	private ShortNameDataStore store;
	private String tmp;
	private String metaDir;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		metaDir = new File(tmp, "meta").getAbsolutePath();
		store = new ShortNameDataStore();
		store.setDataDir(tmp);
	}

	@After
	public void tearDown() {
		TestUtilities.deleteFileIfExists(metaDir, "short-name-to-id-map.json");
		TestUtilities.deleteDir(metaDir);
	}

	@Test
	public void testCreateName_NoCollision() throws IOException {
		String id = "0000-0000-0000-0000-0000";
		String name = store.createShortName(id);
		assertEquals("0000", name);

		File mapping = new File(metaDir, "short-name-to-id-map.json");
		assertTrue(mapping.exists());

		IdToNameMapper mapper = new IdToNameMapper();
		mapper.fromJson(FileUtils.readFileToString(mapping));
		assertEquals(name, mapper.getName(id));
		assertEquals(id, mapper.getId(name));
	}

	@Test
	public void testCreateName_Collision() throws IOException {
		String id = "0000-0000-0000-0000-0000";
		String name = store.createShortName(id);
		assertEquals("0000", name);

		String id2 = "0000-0001-0001-0001-0001";
		String name2 = store.createShortName(id2);
		assertEquals("000d", name2);
	}

	@Test
	public void testCreateName_SameNameReturnsForSameId() throws IOException {
		String id = "0000-0000-0000-0000-0000";
		String name = store.createShortName(id);
		assertEquals("0000", name);

		String id2 = "0000-0000-0000-0000-0000";
		String name2 = store.createShortName(id2);
		assertEquals("0000", name2);
	}

	@Test
	public void testDeleteName() throws IOException {
		String id = "0000-0000-0000-0000-0000";
		String name = store.createShortName(id);
		assertEquals("0000", name);

		File mapping = new File(metaDir, "short-name-to-id-map.json");
		assertTrue(mapping.exists());

		IdToNameMapper mapper = new IdToNameMapper();
		mapper.fromJson(FileUtils.readFileToString(mapping));
		assertEquals(name, mapper.getName(id));

		store.deleteShortName(name);
		mapper.fromJson(FileUtils.readFileToString(mapping));
		assertNull(mapper.getName(id));
	}
}
