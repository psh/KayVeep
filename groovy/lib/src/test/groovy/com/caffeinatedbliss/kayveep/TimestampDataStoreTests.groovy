package com.caffeinatedbliss.kayveep

class TimestampDataStoreTests extends KayVeepTestCase {
    TimestampDataStore timestampDataStore

    @Override
    protected void setUp() {
        super.setUp()
        timestampDataStore = new TimestampDataStore("Modified")
        timestampDataStore.dataDir = new File(tempDir)
    }

    @Override
    protected String getDataStoreFileName() {
        "modified-time-to-id-map"
    }

    void testCorrectFileStored() {
        def date = new Date()
        timestampDataStore.store("foo", date)
        def content = timestampDataStore.load("foo")
        assertEquals(date.getTime(), content.getTime())
        File store = new File(actualDataDir, "modified-time-to-id-map.json")
        assertTrue(store.exists())
    }
}
