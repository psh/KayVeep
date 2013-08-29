package com.caffeinatedbliss.kayveep

/**
 * StorableObject
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
abstract class StorableObject {
    private String id
    private String name
    private Map transientProperties = [:]

    def propertyMissing(String name, value) {
        transientProperties[name] = value
    }

    def propertyMissing(String name) {
        transientProperties[name]
    }

    def String getId() {
        return id
    }

    def setId(String id) {
        this.id = id
    }

    def String getName() {
        return name
    }

    def setName(String name) {
        this.name = name
    }

    abstract fromJson(String json)
}
