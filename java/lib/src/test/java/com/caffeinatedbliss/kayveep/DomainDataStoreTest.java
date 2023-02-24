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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/27/12 at 5:15 PM
 */
public class DomainDataStoreTest {
	private String tmp;
	private String generatedId;
	private File metaDir;

	@Before
	public void setUp() {
		tmp = TestUtilities.getTmpDir();
		metaDir = new File(tmp, "meta");
	}

	@After
	public void tearDown() {
		TestUtilities.deleteDir(new File(tmp, "types").getAbsolutePath());
		String metaPath = new File(tmp, "meta").getAbsolutePath();
		TestUtilities.deleteDir(metaPath);
		TestUtilities.deleteDir(new File(tmp, "raw").getAbsolutePath());
		TestUtilities.deleteFileIfExists(tmp, generatedId + ".json");
		TestUtilities.deleteFileIfExists(metaPath, "exampledataobject-id-to-name-map.json");
		TestUtilities.deleteFileIfExists(metaPath, "exampledataobject-create-time-to-id-map.json");
	}

	@Test
	public void testRawFields_NothingGiven() {
		DomainDataStore<NoAnnotatedFields> store = new DomainDataStore<>(NoAnnotatedFields.class);
		assertTrue(store.getRawFields().isEmpty());
	}

	@Test
	public void testRawFields_OneGiven() {
		DomainDataStore<NoAnnotatedFields> store = new DomainDataStore<>(NoAnnotatedFields.class, new String[]{"foo"});
		assertEquals("foo", store.getRawFields().get(0));
	}

	@Test
	public void testRawFields_OneAnnotated() {
		DomainDataStore<SingleAnnotatedField> store = new DomainDataStore<>(SingleAnnotatedField.class);
		assertEquals("bar", store.getRawFields().get(0));
	}

	@Test
	public void testRawFields_OneGiveOneAnnotated() {
		DomainDataStore<SingleAnnotatedField> store = new DomainDataStore<>(SingleAnnotatedField.class, new String[]{"foo"});
		assertEquals("foo", store.getRawFields().get(0));
		assertEquals("bar", store.getRawFields().get(1));
	}

	@Test
	public void testSetDataDirCascases() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);

		File types = new File(tmp, "types");
		assertTrue(types.exists());
		File meta = new File(tmp, "meta");
		assertTrue(meta.exists());
		File raw = new File(tmp, "raw");
		assertTrue(raw.exists());
	}

	@Test
	public void testCreation() throws IOException {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);
		ExampleDataObject edo = new ExampleDataObject("1", "2", "3");
		store.create(edo);
		generatedId = edo.getId();

		File data = new File(tmp, edo.getId() + ".json");
		assertTrue(data.exists());

		String content = FileUtils.readFileToString(data, Charset.defaultCharset());
		ExampleDataObject storedObject = new Gson().fromJson(content, ExampleDataObject.class);
		assertNotNull(storedObject);
		assertEquals(edo.getId(), storedObject.getId());
		assertEquals(edo.getName(), storedObject.getName());
		assertEquals(edo.getData(), storedObject.getData());

		File mapping = new File(metaDir, "exampledataobject-id-to-name-map.json");
		assertTrue(mapping.exists());
		content = FileUtils.readFileToString(mapping, Charset.defaultCharset());
		IdToNameMapper mapper = new Gson().fromJson(content, IdToNameMapper.class);
		assertNotNull(mapper);
		assertEquals(edo.getName(), mapper.getName(edo.getId()));
		assertEquals(edo.getId(), mapper.getId(edo.getName()));

		File createTimeMap = new File(metaDir, "exampledataobject-create-time-to-id-map.json");
		assertTrue(createTimeMap.exists());
		content = FileUtils.readFileToString(createTimeMap, Charset.defaultCharset());
		Map<String, Long> map = new Gson().fromJson(content, new TypeToken<Map<String, Long>>() {
		}.getType());
		assertNotNull(map);
		assertNotNull(map.get(edo.getId()));
		assertNotNull(edo.get("created_time"));
		assertEquals(edo.get("created_time"), map.get(edo.getId()));
	}

	@Test
	public void testLoadingById() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);
		ExampleDataObject edo = new ExampleDataObject("1", "2", "3");
		store.create(edo);
		generatedId = edo.getId();

		ExampleDataObject storedObject = store.loadById(edo.getId());
		assertEquals(edo.getId(), storedObject.getId());
		assertEquals(edo.getName(), storedObject.getName());
		assertEquals(edo.getData(), storedObject.getData());
		assertEquals(edo.get("created_time"), storedObject.get("created_time"));
		assertEquals(edo.get("updated_time"), storedObject.get("updated_time"));
	}

	@Test
	public void testLoadingByName() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);
		ExampleDataObject edo = new ExampleDataObject("1", "2", "3");
		store.create(edo);
		generatedId = edo.getId();

		ExampleDataObject storedObject = store.loadByName(edo.getName());
		assertEquals(edo.getId(), storedObject.getId());
		assertEquals(edo.getName(), storedObject.getName());
		assertEquals(edo.getData(), storedObject.getData());
		assertEquals(edo.get("created_time"), storedObject.get("created_time"));
		assertEquals(edo.get("updated_time"), storedObject.get("updated_time"));
	}

	@Test
	public void testStore() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);
		ExampleDataObject edo = new ExampleDataObject("1", "2", "3");
		store.store(edo);
		generatedId = edo.getId();

		ExampleDataObject storedObject = store.loadById("1");
		assertEquals(edo.getId(), storedObject.getId());
		assertEquals(edo.getName(), storedObject.getName());
		assertEquals(edo.getData(), storedObject.getData());
		assertEquals(edo.get("updated_time"), storedObject.get("updated_time"));
	}

	@Test
	public void testStoreWithNullIdCallsCreate() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);
		ExampleDataObject edo = new ExampleDataObject(null, "2", "3");
		store.store(edo);
		assertNotNull(edo.getId());
		generatedId = edo.getId();

		ExampleDataObject storedObject = store.loadById(edo.getId());
		assertEquals(edo.getId(), storedObject.getId());
		assertEquals(edo.getName(), storedObject.getName());
		assertEquals(edo.getData(), storedObject.getData());
		assertEquals(edo.get("created_time"), storedObject.get("created_time"));
		assertNull(storedObject.get("updated_time"));
	}

	@Test
	public void testDeleting() throws IOException {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);
		ExampleDataObject edo = new ExampleDataObject(null, "2", "3");
		store.create(edo);
		generatedId = edo.getId();

		File data = new File(tmp, edo.getId() + ".json");
		assertTrue(data.exists());

		store.delete(edo);
		assertFalse(data.exists());

		File mapping = new File(metaDir, "exampledataobject-id-to-name-map.json");
		assertTrue(mapping.exists());
		String content = FileUtils.readFileToString(mapping, Charset.defaultCharset());
		IdToNameMapper mapper = new Gson().fromJson(content, IdToNameMapper.class);
		assertNotNull(mapper);
		assertNull(edo.getName(), mapper.getName(edo.getId()));
		assertNull(edo.getId(), mapper.getId(edo.getName()));
	}

	@Test
	public void testGettingAllIds() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);

		ExampleDataObject edo1 = new ExampleDataObject("100", "2", "3");
		ExampleDataObject edo2 = new ExampleDataObject("200", "2", "3");

		store.store(edo1);
		store.store(edo2);

		List<String> ids = store.getAllIds();
		assertNotNull(ids);
		Set<String> idSet = new HashSet<>(ids);
		assertTrue(idSet.contains(edo1.getId()));
		assertTrue(idSet.contains(edo2.getId()));
	}

	@Test
	public void testFiltering_Empty() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);

		ExampleDataObject edo1 = new ExampleDataObject("100", "2", "3333");
		ExampleDataObject edo2 = new ExampleDataObject("200", "22", "333");
		ExampleDataObject edo3 = new ExampleDataObject("300", "222", "33");
		ExampleDataObject edo4 = new ExampleDataObject("400", "2222", "3");
		store.store(edo1);
		store.store(edo2);
		store.store(edo3);
		store.store(edo4);

		List<ExampleDataObject> emptyFiltered = store.filter(item -> false);
		assertNotNull(emptyFiltered);
		assertEquals(0, emptyFiltered.size());
	}

	@Test
	public void testFiltering_ValidFilter() {
		DomainDataStore<ExampleDataObject> store = new DomainDataStore<>(ExampleDataObject.class);
		store.setDataDir(tmp);

		ExampleDataObject edo1 = new ExampleDataObject("100", "2", "3333");
		ExampleDataObject edo2 = new ExampleDataObject("200", "22", "333");
		ExampleDataObject edo3 = new ExampleDataObject("300", "222", "33");
		ExampleDataObject edo4 = new ExampleDataObject("400", "2222", "3");
		store.store(edo1);
		store.store(edo2);
		store.store(edo3);
		store.store(edo4);

		List<ExampleDataObject> emptyFiltered = store.filter(item -> Integer.parseInt(item.getId()) < 300);
		assertNotNull(emptyFiltered);
		assertEquals(2, emptyFiltered.size());
		assertEquals("100", emptyFiltered.get(0).getId());
		assertEquals("200", emptyFiltered.get(1).getId());
	}

	private static class SingleAnnotatedField implements StorableObject {
		private String name;
		private
		@RawField
		int bar;
		private int baz;

		public String getId() {
			return null;
		}

		public void setId(String id) {
		}

		public String getName() {
			return null;
		}

		public void setName(String name) {
		}

		public String toJson() {
			return null;
		}

		public Object get(String name) {
			return null;
		}

		public void set(String name, Object value) {
		}
	}

	private static class NoAnnotatedFields implements StorableObject {
		public String getId() {
			return null;
		}

		public void setId(String id) {
		}

		public String getName() {
			return null;
		}

		public void setName(String name) {
		}

		public String toJson() {
			return null;
		}

		public Object get(String name) {
			return null;
		}

		public void set(String name, Object value) {
		}
	}
}
