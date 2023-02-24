package com.caffeinatedbliss.kayveep

import groovy.json.JsonSlurper

/**
 * MapDataStore
 *
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 */
class MapDataStoreTests extends KayVeepTestCase {
	MapDataStore mapDataStore

	@Override
	protected void setUp() {
        super.setUp()
		mapDataStore = new MapDataStore("test")
		mapDataStore.dataDir = new File(tempDir)
	}

	void testStore() {
		mapDataStore.store("foo", "bar")
		assertTrue(actualDataDir.exists())
		assertTrue(data.exists())
        def content = new JsonSlurper().parseText(data.text)
		assertNotNull(content)
		assertEquals("bar", content.foo)
	}

    void testLoad() {
        mapDataStore.store("foo", "bar")
        assertTrue(actualDataDir.exists())
        assertTrue(data.exists())
        assertEquals("bar", mapDataStore.load("foo"))
    }

    void testLoadAll() {
        mapDataStore.store("foo", "bar")
        mapDataStore.store("baz", "zot")
        assertTrue(actualDataDir.exists())
        assertTrue(data.exists())
        def content = mapDataStore.loadAll()
        assertEquals("bar", content.foo)
        assertEquals("zot", content.baz)
    }

    void testDelete() {
        mapDataStore.store("foo", "bar")
        mapDataStore.store("baz", "zot")
        assertTrue(actualDataDir.exists())
        assertTrue(data.exists())

        mapDataStore.delete("baz")
        assertTrue(actualDataDir.exists())
        assertTrue(data.exists())
        def content = mapDataStore.loadAll()
        assertEquals("bar", content.foo)
        assertNull(content.baz)

        mapDataStore.delete("foo")
        assertTrue(actualDataDir.exists())
        assertTrue(data.exists())
        content = mapDataStore.loadAll()
        assertEquals(0, content.size())
    }
}
