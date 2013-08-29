package com.caffeinatedbliss.kayveep

import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/**
 * DataStoreTests
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
class DataStoreTests extends KayVeepTestCase {
	DataStore dataStore

	@Override
	protected void setUp() {
        super.setUp()
        data = new File(tempDir, getDataStoreFileName() + ".json")
		dataStore = new DataStore()
		dataStore.dataDir = new File(tempDir)
	}

    @Override
    protected String getDataStoreFileName() {
        "foo"
    }

    void testDataDirectoryGetsCreated() {
		def dataDir = new File(tempDir, "foo")
		try {
			def theDataStore = new DataStore()
			theDataStore.dataDir = dataDir
			assertTrue(dataDir.exists())
		} finally {
			dataDir.deleteDir()
		}
	}

	void testStoreNew() {
		dataStore.store("foo", "bar")
		assertTrue(data.exists())
		def content = data.text
		assertEquals("bar", content)
	}

	void testStoreExisting() {
		dataStore.store("foo", "bar")
		assertTrue(data.exists())
		assertEquals("bar", data.text)

		dataStore.store("foo", "baz")
		assertEquals("baz", data.text)
	}

	void testStoreCustomExtension() {
		data = new File(tempDir, "foo.txt")
		dataStore = new DataStore("txt")
		dataStore.dataDir = new File(tempDir)
		dataStore.store("foo", "bar")
		assertTrue(data.exists())
	}

	void testLoad() {
		dataStore.store("foo", "bar")
		assertTrue(data.exists())
		def content = dataStore.load("foo")
		assertEquals("bar", content)
	}

	void testLoadNotFound() {
		assertFalse(data.exists())
		def content = dataStore.load("foo")
		assertNull(content)
	}

	void testDelete() {
		dataStore.store("foo", "bar")
		assertTrue(data.exists())
		dataStore.delete("foo")
		assertFalse(data.exists())
	}

	void testEncodeJson() {
		def obj = new TestObject("xxxx", 2, 3.75)
		def store = new MyStore()
		String json = store.encodeJson(obj)

        def output = new JsonSlurper().parseText(json)
        assertEquals("xxxx", output.foo)
        assertEquals(2, output.bar)
        assertEquals(3.75, output.baz)
	}

    void testDecodeJson() {
        def obj = new TestObject("xxxx", 2, 3.75)
        def json = use(JsonOutput) {
            obj.toJson()
        }

        def store = new MyStore()
        def output = store.decodeJson(json)
        assertEquals("xxxx", output.foo)
        assertEquals(2, output.bar)
        assertEquals(3.75, output.baz)
    }

	class MyStore extends DataStore {
		@Override
		def String encodeJson(Map data) {
			return super.encodeJson(data)
		}

		@Override
		def Map decodeJson(String json) {
			return super.decodeJson(json)
		}

	}
}
