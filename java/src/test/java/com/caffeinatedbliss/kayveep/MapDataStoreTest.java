package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/25/12 at 4:25 PM
 */
public class MapDataStoreTest {
	private TestMapStore store;
	private String tmp;
	private String metaDir;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		metaDir = new File(tmp, "meta").getAbsolutePath();
		store = new TestMapStore();
		store.setDataDir(tmp);
	}

	@After
	public void tearDown() {
		TestUtilities.deleteFileIfExists(metaDir, "TestMapStore.json");
		TestUtilities.deleteDir(metaDir);
	}

	@Test
	public void testStore() throws IOException {
		store.store("123", new ExampleDataObject("123", "456", "789"));

		File dataFile = new File(metaDir, "TestMapStore.json");
		assertTrue(dataFile.exists());

		String content = FileUtils.readFileToString(dataFile);
		Map<String, ExampleDataObject> data = new Gson().fromJson(content, new TypeToken<Map<String, ExampleDataObject>>() {
		}.getType());
		assertNotNull(data);
		ExampleDataObject mappedObject = data.get("123");
		assertNotNull(mappedObject);
	}

	@Test
	public void testLoad() {
		store.store("123", new ExampleDataObject("123", "456", "789"));

		Object obj = store.load("123");
		assertNotNull(obj);

		ExampleDataObject foo = (ExampleDataObject) obj;
		assertEquals("123", foo.getId());
		assertEquals("456", foo.getName());
		assertEquals("789", foo.getData());
	}

	@Test
	public void testLoadAll() {
		store.store("foo", new ExampleDataObject("123", "456", "789"));
		store.store("bar", new ExampleDataObject("1234", "56", "7890"));

		Object obj = store.loadAll();
		assertNotNull(obj);

		Map<String, ExampleDataObject> map = (Map<String, ExampleDataObject>) obj;
		assertNotNull(map.get("foo"));
		assertEquals("123", map.get("foo").getId());
		assertNotNull(map.get("bar"));
		assertEquals("1234", map.get("bar").getId());
	}

	@Test
	public void testDelete() {
		store.store("foo", new ExampleDataObject("123", "456", "789"));
		store.store("bar", new ExampleDataObject("1234", "56", "7890"));

		store.delete("foo");

		assertNull(store.load("foo"));
		assertEquals("1234", store.load("bar").getId());
	}

	private class TestMapStore extends MapDataStore<ExampleDataObject> {
		public TestMapStore() {
			super("TestMapStore");
		}

		@Override
		protected Type getType() {
			return new TypeToken<Map<String, ExampleDataObject>>() {
			}.getType();
		}
	}
}
