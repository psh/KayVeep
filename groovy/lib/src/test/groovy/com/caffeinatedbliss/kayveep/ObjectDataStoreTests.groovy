package com.caffeinatedbliss.kayveep

import groovy.json.JsonSlurper

class ObjectDataStoreTests extends KayVeepTestCase {
    ObjectDataStore objectDataStore
    File typesDir
    File typeFile
    File dataFile
    def obj

    @Override
    protected void setUp() {
        super.setUp()
        objectDataStore = new ObjectDataStore()
        objectDataStore.dataDir = new File(tempDir)
        typesDir = new File(tempDir, "types")
        typeFile = new File(typesDir, "TestObject.json")
        dataFile = new File(tempDir, "the-id.json")

        obj = new TestObject("the-id", "the-name")
        obj.bar = 100
        obj.foo = "bat"
        obj.baz = 3.75
        obj.json = "the-json"
    }

    @Override
    protected void tearDown() {
        super.tearDown()

        if (dataFile.exists()) {
            dataFile.delete();
        }
        if (typeFile.exists()) {
            typeFile.delete();
        }
        if (typesDir.exists()) {
            typesDir.deleteDir()
        }
    }

    void testStore() {
        objectDataStore.store(obj)

        assertTrue(typesDir.exists())
        assertTrue(typeFile.exists())

        def content = new JsonSlurper().parseText(typeFile.text)
        assertEquals(1, content["the-id"])

        assertTrue(dataFile.exists())
        content = new JsonSlurper().parseText(dataFile.text)
        assertEquals("the-id", content.id)
        assertEquals("the-name", content.name)
        assertEquals("bat", content.foo)
        assertEquals(100, content.bar)
        assertEquals(3.75, content.baz)
    }

    void testLoad() {
        objectDataStore.store(obj)
        def content = objectDataStore.load("the-id", TestObject)
        assertEquals("the-id", content.id)
        assertEquals("the-name", content.name)
        assertEquals("bat", content.foo)
        assertEquals(100, content.bar)
        assertEquals(3.75, content.baz)
    }

    void testDelete() {
        objectDataStore.store(obj)
        def content = new JsonSlurper().parseText(typeFile.text)
        assertTrue(content.containsKey("the-id"))
        assertTrue(dataFile.exists())

        objectDataStore.delete(obj)
        content = new JsonSlurper().parseText(typeFile.text)
        assertFalse(content.containsKey("the-id"))
        assertFalse(dataFile.exists())
    }
}
