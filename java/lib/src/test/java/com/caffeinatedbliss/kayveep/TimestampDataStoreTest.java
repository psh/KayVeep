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
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 * On: 2/25/12 at 4:25 PM
 */
public class TimestampDataStoreTest {
    private TimestampDataStore store;
    private String metaDir;
    private Date theDate;
    private Date otherDate;

    @Before
    public void setUp() {
        String tmp = TestUtilities.getTmpDir();
        metaDir = new File(tmp, "meta").getAbsolutePath();
        store = new TimestampDataStore("creation");
        store.setDataDir(tmp);
        theDate = new Date();
        otherDate = new Date();
    }

    @After
    public void tearDown() {
        TestUtilities.deleteFileIfExists(metaDir, "creation-time-to-id-map.json");
        TestUtilities.deleteDir(metaDir);
    }

    @Test
    public void testStore() throws IOException {
        store.store("123", theDate.getTime());

        File dataFile = new File(metaDir, "creation-time-to-id-map.json");
        assertTrue(dataFile.exists());

        String content = FileUtils.readFileToString(dataFile, Charset.defaultCharset());
        Map<String, Long> data = new Gson().fromJson(content, new TypeToken<Map<String, Long>>() {
        }.getType());
        assertNotNull(data);
        Long mappedObject = data.get("123");
        assertNotNull(mappedObject);
        assertEquals(Long.valueOf(theDate.getTime()), mappedObject);
    }

    @Test
    public void testLoad() {
        store.store("123", theDate.getTime());

        Long obj = store.load("123");
        assertNotNull(obj);

        assertEquals(Long.valueOf(theDate.getTime()), obj);
    }

    @Test
    public void testLoadAll() {
        long fooTime = theDate.getTime();
        long otherTime = otherDate.getTime();

        store.store("foo", fooTime);
        store.store("bar", otherTime);

        Map<String, Long> obj = store.loadAll();
        assertNotNull(obj);

        assertNotNull(obj.get("foo"));
        assertEquals(fooTime, (long) obj.get("foo"));
        assertNotNull(obj.get("bar"));
        assertEquals(otherTime, (long) obj.get("bar"));
    }

    @Test
    public void testDelete() {
        store.store("foo", theDate.getTime());
        store.store("bar", otherDate.getTime());

        store.delete("foo");

        assertNull(store.load("foo"));
        assertEquals(Long.valueOf(otherDate.getTime()), store.load("bar"));
    }
}
