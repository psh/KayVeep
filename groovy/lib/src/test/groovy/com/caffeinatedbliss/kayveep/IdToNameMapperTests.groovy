package com.caffeinatedbliss.kayveep

import groovy.json.JsonSlurper

class IdToNameMapperTests extends GroovyTestCase {
    IdToNameMapper mapper

    @Override
    protected void setUp() {
        mapper = new IdToNameMapper()
    }

    void testStore() {
        def obj = new TestObject("foo", "bar")

        mapper.put(obj)
        assertEquals("bar", mapper.getName("foo"))
        assertEquals("foo", mapper.getId("bar"))
    }

    void testAllIds() {
        mapper.put(new TestObject("foo", "bar"))
        mapper.put(new TestObject("baz", "zot"))
        def content = mapper.allIds()
        assertTrue(content.contains("foo"))
        assertTrue(content.contains("baz"))
    }

    void testToJson() {
        mapper.put(new TestObject("foo", "bar"))
        mapper.put(new TestObject("baz", "zot"))
        def json = mapper.toJson()
        def content = new JsonSlurper().parseText(json)
        assertNotNull(content.idToName)
        assertNotNull(content.nameToId)
    }

    void testFromJson() {
        mapper.put(new TestObject("foo", "bar"))
        mapper.put(new TestObject("baz", "zot"))
        def json = mapper.toJson()

        def other = new IdToNameMapper()
        other.fromJson(json)
        assertEquals("bar", mapper.getName("foo"))
        assertEquals("foo", mapper.getId("bar"))
        assertEquals("zot", mapper.getName("baz"))
        assertEquals("baz", mapper.getId("zot"))
    }
}
