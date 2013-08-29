package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/25/12 at 4:25 PM
 */
public class TimestampDataStoreTest {
	private TimestampDataStore store;
	private String tmp;
	private String metaDir;
	private Date theDate;
	private Date otherDate;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		metaDir = new File(tmp, "meta").getAbsolutePath();
		store = new TimestampDataStore("creation");
		store.setDataDir(tmp);
		theDate = new Date();
		otherDate = new Date();
	}

	@After
	public void tearDown() {
		TestUtilities.deleteFileIfExists(metaDir, "creation-time-to-id-map.json");
		TestUtilities.deleteDir(metaDir);
	}

	@Test
	public void testStore() throws IOException {
		store.store("123", theDate.getTime());

		File dataFile = new File(metaDir, "creation-time-to-id-map.json");
		assertTrue(dataFile.exists());

		String content = FileUtils.readFileToString(dataFile);
		Map<String, Long> data = new Gson().fromJson(content, new TypeToken<Map<String, Long>>() {
		}.getType());
		assertNotNull(data);
		Long mappedObject = data.get("123");
		assertNotNull(mappedObject);
		assertEquals(new Long(theDate.getTime()), mappedObject);
	}

	@Test
	public void testLoad() {
		store.store("123", theDate.getTime());

		Object obj = store.load("123");
		assertNotNull(obj);

		Long foo = (Long) obj;
		assertEquals(new Long(theDate.getTime()), foo);
	}

	@Test
	public void testLoadAll() {
		store.store("foo", theDate.getTime());
		store.store("bar", otherDate.getTime());

		Object obj = store.loadAll();
		assertNotNull(obj);

		Map<String, Date> map = (Map<String, Date>) obj;
		assertNotNull(map.get("foo"));
		assertEquals(new Long(theDate.getTime()), map.get("foo"));
		assertNotNull(map.get("bar"));
		assertEquals(new Long(otherDate.getTime()), map.get("bar"));
	}

	@Test
	public void testDelete() {
		store.store("foo", theDate.getTime());
		store.store("bar", otherDate.getTime());

		store.delete("foo");

		assertNull(store.load("foo"));
		assertEquals(new Long(otherDate.getTime()), store.load("bar"));
	}
}
