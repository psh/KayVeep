package com.caffeinatedbliss.kayveep;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/24/12 at 10:36 PM
 */
public class IdToNameMapperTest {
	private IdToNameMapper mapper;
	private ExampleDataObject dataObject1;
	private ExampleDataObject dataObject2;

	@Before
	public void setUp() {
		mapper = new IdToNameMapper();
		dataObject1 = new ExampleDataObject("1", "a", "--z--");
		dataObject2 = new ExampleDataObject("2", "b", "--y--");
		mapper.put(dataObject1);
		mapper.put(dataObject2);
	}

	@Test
	public void testGettingAllIds() {
		List<String> all = mapper.allIds();
		assertEquals(dataObject1.getId(), all.get(0));
		assertEquals(dataObject2.getId(), all.get(1));
	}

	@Test
	public void testGettingNames() {
		assertEquals(dataObject1.getName(), mapper.getName(dataObject1.getId()));
		assertEquals(dataObject2.getName(), mapper.getName(dataObject2.getId()));
	}

	@Test
	public void testGettingIds() {
		assertEquals(dataObject1.getId(), mapper.getId(dataObject1.getName()));
		assertEquals(dataObject2.getId(), mapper.getId(dataObject2.getName()));
	}

	@Test
	public void testNotFound() {
		assertNull(mapper.getName("unknown id"));
		assertNull(mapper.getId("unknown name"));
	}

	@Test
	public void testRemoving() {
		mapper.remove(dataObject1);
		List<String> all = mapper.allIds();
		assertEquals(dataObject2.getId(), all.get(0));
		assertNull(mapper.getName(dataObject1.getId()));
		assertEquals(dataObject2.getName(), mapper.getName(dataObject2.getId()));
		assertNull(mapper.getId(dataObject1.getName()));
		assertEquals(dataObject2.getId(), mapper.getId(dataObject2.getName()));
	}

	@Test
	public void gettingJson() {
		String json = mapper.toJson();
		assertEquals("{\"id_to_name\":{\"1\":\"a\",\"2\":\"b\"},\"name_to_id\":{\"a\":\"1\",\"b\":\"2\"}}", json);
	}

	@Test
	public void initializingFromJson() {
		String json = "{\"id_to_name\":{\"1\":\"a\",\"2\":\"b\"},\"name_to_id\":{\"a\":\"1\",\"b\":\"2\"}}";
		mapper = new IdToNameMapper();
		assertTrue(mapper.allIds().isEmpty());

		mapper.fromJson(json);
		List<String> all = mapper.allIds();
		assertEquals(dataObject1.getId(), all.get(0));
		assertEquals(dataObject2.getId(), all.get(1));
		assertEquals(dataObject1.getId(), mapper.getId(dataObject1.getName()));
		assertEquals(dataObject2.getId(), mapper.getId(dataObject2.getName()));
		assertEquals(dataObject1.getName(), mapper.getName(dataObject1.getId()));
		assertEquals(dataObject2.getName(), mapper.getName(dataObject2.getId()));
	}
}
