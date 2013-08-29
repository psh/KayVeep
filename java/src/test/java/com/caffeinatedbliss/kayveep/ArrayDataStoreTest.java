package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/25/12 at 4:25 PM
 */
public class ArrayDataStoreTest {
	private ArrayDataStore store;
	private String tmp;
	private String metaDir;
	private List<String> ids;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		metaDir = new File(tmp, "meta").getAbsolutePath();
		store = new ArrayDataStore("many", "one");
		store.setDataDir(tmp);
		ids = new ArrayList<String>();
		ids.add("foo");
		ids.add("bar");
	}

	@After
	public void tearDown() {
		TestUtilities.deleteFileIfExists(metaDir, "many-array-to-one-id-map.json");
		TestUtilities.deleteDir(metaDir);
	}

	@Test
	public void testStore() throws IOException {
		store.store("123", ids);

		File dataFile = new File(metaDir, "many-array-to-one-id-map.json");
		assertTrue(dataFile.exists());

		String content = FileUtils.readFileToString(dataFile);
		Map<String, List<String>> data = new Gson().fromJson(content, new TypeToken<Map<String, List<String>>>() {
		}.getType());
		assertNotNull(data);
		List<String> mappedObject = data.get("123");
		assertNotNull(mappedObject);
		assertEquals("foo", mappedObject.get(0));
		assertEquals("bar", mappedObject.get(1));
	}

	@Test
	public void testLoad() {
		store.store("123", ids);

		Object obj = store.load("123");
		assertNotNull(obj);

		List<String> foo = (List<String>) obj;
		assertEquals("foo", foo.get(0));
		assertEquals("bar", foo.get(1));
	}

	@Test
	public void testLoadAll() {
		List<String> other = new ArrayList<String>();
		other.add("baz");
		store.store("123", ids);
		store.store("456", other);

		Object obj = store.loadAll();
		assertNotNull(obj);

		Map<String, List<String>> map = (Map<String, List<String>>) obj;
		assertNotNull(map.get("123"));
		assertEquals("foo", map.get("123").get(0));
		assertEquals("bar", map.get("123").get(1));
		assertNotNull(map.get("456"));
		assertEquals("baz", map.get("456").get(0));
	}

	@Test
	public void testDelete() {
		List<String> other = new ArrayList<String>();
		other.add("baz");
		store.store("123", ids);
		store.store("456", other);

		store.delete("123");

		assertNull(store.load("123"));
		assertEquals("baz", store.load("456").get(0));
	}
}
