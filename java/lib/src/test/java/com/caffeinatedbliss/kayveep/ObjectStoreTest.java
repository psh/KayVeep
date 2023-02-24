package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/22/12 at 10:55 PM
 */
public class ObjectStoreTest {
	protected String generatedId;
	protected String tmp;
	protected String dataDir;
	protected String metaDir;
	protected String typesDir;
	private ObjectStore<ExampleDataObject> store;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		dataDir = tmp;
		metaDir = new File(tmp, "meta").getAbsolutePath();
		typesDir = new File(tmp, "types").getAbsolutePath();
		TestUtilities.deleteFileIfExists(typesDir, ExampleDataObject.class.getName() + ".json");
		store = new ObjectStore<>();
		store.setDataDir(tmp);
	}

	@After
	public void tearDown() {
		TestUtilities.deleteFileIfExists(dataDir, "1.json");
		TestUtilities.deleteFileIfExists(dataDir, generatedId + ".json");
		TestUtilities.deleteFileIfExists(typesDir, ExampleDataObject.class.getName() + ".json");

		TestUtilities.deleteDir(metaDir);
		TestUtilities.deleteDir(typesDir);
	}

	@Test
	public void testStore() throws IOException {
		ExampleDataObject obj = new ExampleDataObject("1", "2", "3");

		store.store(obj);

		verifyStoredObject(obj, "1.json");
	}

	@Test
	public void testStoreWithGeneratedId() throws IOException {
		ExampleDataObject obj = new ExampleDataObject(null, "2", "3");

		store.store(obj);
		generatedId = obj.getId();
		assertNotNull(generatedId);

		verifyStoredObject(obj, generatedId + ".json");
	}

	@Test
	public void testAllIdsGetMappedToTypes() throws IOException {
		ExampleDataObject obj1 = new ExampleDataObject("1", "2", "3");
		ExampleDataObject obj2 = new ExampleDataObject(null, "2", "3");

		store.store(obj1);
		store.store(obj2);

		File theTypeFile = new File(typesDir, ExampleDataObject.class.getName() + ".json");
		assertTrue(theTypeFile.exists());

		String content = FileUtils.readFileToString(theTypeFile, Charset.defaultCharset());
		ArrayList<String> types = new Gson().fromJson(content, new TypeToken<ArrayList<String>>() {
		}.getType());
		assertNotNull(types);
		assertEquals(obj1.getId(), types.get(0));
		assertEquals(obj2.getId(), types.get(1));
	}

	@Test
	public void testLoad() {
		store.store(new ExampleDataObject("1", "2", "3"));

		ExampleDataObject obj = store.load("1", ExampleDataObject.class);
		assertEquals("1", obj.getId());
		assertEquals("2", obj.getName());
		assertEquals("3", obj.getData());
	}

	@Test
	public void testDelete() throws IOException {
		ExampleDataObject dataObject = new ExampleDataObject("1", "2", "3");
		store.store(dataObject);
		store.delete(dataObject);

		File theFile = new File(tmp, "1.json");
		assertFalse(theFile.exists());

		assertNull(store.load("1", ExampleDataObject.class));

		File theTypeFile = new File(typesDir, ExampleDataObject.class.getName() + ".json");
		assertTrue(theTypeFile.exists());
		String content = FileUtils.readFileToString(theTypeFile, Charset.defaultCharset());
		ArrayList<String> types = new Gson().fromJson(content, new TypeToken<ArrayList<String>>() {
		}.getType());
		assertNotNull(types);
		assertTrue(types.isEmpty());
	}

//    @Test
//    public void testGettingKeys() {
//        String key1 = store.getKey();
//        assertNotNull(key1);
//
//        String key2 = store.getKey();
//        assertNotNull(key2);
//
//        assertFalse(key1.equals(key2));
//    }
//
//    @Test
//    public void testStoreAndLoadBytes() {
//        byte[] data = new byte[]{0, 1, 2};
//        store.store("key", data);
//
//        byte[] input = store.loadBytes("key");
//        assertNotNull(input);
//        assertEquals(data.length, input.length);
//
//        assertArrayEquals(data, input);
//    }


	private void verifyStoredObject(ExampleDataObject obj, String filename) throws IOException {
		File theFile = new File(dataDir, filename);
		assertTrue(theFile.exists());

		String content = FileUtils.readFileToString(theFile, Charset.defaultCharset());
		ExampleDataObject input = new Gson().fromJson(content, ExampleDataObject.class);
		assertEquals(obj.getId(), input.getId());
		assertEquals(obj.getName(), input.getName());
		assertEquals(obj.getData(), input.getData());

		File theTypeFile = new File(typesDir, ExampleDataObject.class.getName() + ".json");
		assertTrue(theTypeFile.exists());

		content = FileUtils.readFileToString(theTypeFile, Charset.defaultCharset());
		ArrayList<String> types = new Gson().fromJson(content, new TypeToken<ArrayList<String>>() {
		}.getType());
		assertNotNull(types);
		assertEquals(obj.getId(), types.get(0));
	}

}
