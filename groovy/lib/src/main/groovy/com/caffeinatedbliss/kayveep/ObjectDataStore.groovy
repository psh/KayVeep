package com.caffeinatedbliss.kayveep

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class ObjectDataStore {
    private data
    private types

    public ObjectDataStore() {
        this.data = new DataStore()
        this.types = new DataStore()
    }


    def setDataDir(File dir) {
        this.data.setDataDir(dir)
        this.types.setDataDir(new File(dir, "types"))
    }

    /**
     * Load an object from the data store.
     *
     * @param ( mixed ) $id the ID of the object to load
     * @param String $type the type of object being loaded
     * @return ( mixed )
     */
    def load(id, type) {
        def json = this.data.load(id)
        if (json) {
            def clz = type instanceof String ? (type as Class) : type
            def clzName = type instanceof String ? type : getClass(type)
            def object = clz.newInstance()
            object.fromJson(json)
            this.store_key(id, clzName)
            return object
        }
    }

    /**
     * Performs an "insert or update" type of operation on the StorableObject
     * passed in.  If the object has no ID of its own, one will be generated
     * by the data store.
     *
     * @param ( mixed ) $object
     */
    def store(StorableObject object) {
        if (object.getId() == null) {
            object.setId(DataStoreKeys.getKey())
        }
        this.data.store(object.getId(), this.data.encodeJson(object))
        this.store_key(object.getId(), getClass(object))
    }

    def delete(StorableObject object) {
        this.delete_key(object.getId(), getClass(object))
        this.data.delete(object.getId())
    }

    private String getClass(Object obj) {
        obj != null ? obj.getClass().getSimpleName() : "Object"
    }

    private store_key(id, type) {
        def json = this.types.load(type)
        def keys = [:]
        if (json) {
            keys = new JsonSlurper().parseText(json)
        }
        keys[(id)] = 1
        def output = use(JsonOutput) {
            keys.toJson()
        }
        this.types.store(type, output)
    }

    private delete_key(id, type) {
        def json = this.types.load(type)
        def keys = [:]
        if (json) {
            keys = new JsonSlurper().parseText(json)
            keys.remove(id)
        }
        def output = use(JsonOutput) {
            keys.toJson()
        }
        this.types.store(type, output)
    }
}