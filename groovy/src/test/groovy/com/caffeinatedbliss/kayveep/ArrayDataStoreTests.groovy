package com.caffeinatedbliss.kayveep

class ArrayDataStoreTests extends KayVeepTestCase {
    ArrayDataStore arrayDataStore

    @Override
    protected void setUp() {
        super.setUp()
        arrayDataStore = new ArrayDataStore("Student", "Teacher")
        arrayDataStore.dataDir = new File(tempDir)
    }

    @Override
    protected String getDataStoreFileName() {
        "student-array-to-teacher-id-map"
    }

    void testCorrectFileStored() {
        arrayDataStore.store("foo", ["bar", "baz"])
        def content = arrayDataStore.load("foo")
        assertEquals(2, content.size())
        assertEquals("bar", content[0])
        assertEquals("baz", content[1])
        File store = new File(actualDataDir, "student-array-to-teacher-id-map.json")
        assertTrue(store.exists())
    }
}
