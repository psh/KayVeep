package com.caffeinatedbliss.kayveep;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/23/12 at 12:04 AM
 */
public class AbstractStorableObjectTest {
	@Test
	public void testPropertyAccess() throws Exception {
		ExampleDataObject obj = new ExampleDataObject();
		obj.setId("id");
		obj.setName("name");
		obj.setData("something");
		obj.set("property", "zz9pluralAlpha");

		assertEquals("id", obj.getId());
		assertEquals("name", obj.getName());
		assertEquals("something", obj.getData());
		assertEquals("zz9pluralAlpha", obj.get("property"));
	}

	@Test
	public void testToJson() {
		ExampleDataObject obj = new ExampleDataObject();
		obj.setId("1");
		obj.setName("2");
		obj.setData("3");

		String json = obj.toJson();

		StringBuilder sb = new StringBuilder();
		sb.append("{").
				append(jsonProperty("data", "3")).append(",").
				append(jsonProperty("id", "1")).append(",").
				append(jsonProperty("name", "2")).append("").
				append("}");
		assertEquals(sb.toString(), json);
	}

	@Test
	public void testFromJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").
				append(jsonProperty("id", "1")).append(",").
				append(jsonProperty("name", "2")).append(",").
				append(jsonProperty("data", "3")).append("").
				append("}");
		String json = sb.toString();

		ExampleDataObject obj = new ExampleDataObject();
		obj.fromJson(json);

		assertEquals("1", obj.getId());
		assertEquals("2", obj.getName());
		assertEquals("3", obj.getData());
	}

	private String jsonProperty(String name, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(name).append("\":\"").append(value).append("\"");
		return sb.toString();
	}

}
