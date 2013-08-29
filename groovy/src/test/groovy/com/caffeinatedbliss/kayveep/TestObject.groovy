package com.caffeinatedbliss.kayveep

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

public class TestObject extends StorableObject {
    String foo
    int bar
    double baz

    TestObject() {
    }

    TestObject(String foo, int bar, double baz) {
        this.foo = foo
        this.bar = bar
        this.baz = baz
    }

    TestObject(id, name) {
        setId(id)
        setName(name)
    }

    @Override
    def fromJson(String json) {
        def data = new JsonSlurper().parseText(json)
        setId(data.id)
        setName(data.name)
        this.foo = data.foo
        this.bar = data.bar
        this.baz = data.baz
        return this
    }

}
