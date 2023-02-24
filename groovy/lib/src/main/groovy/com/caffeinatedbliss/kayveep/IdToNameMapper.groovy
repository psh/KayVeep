package com.caffeinatedbliss.kayveep

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class IdToNameMapper {
    private idToName
    private nameToId

    IdToNameMapper() {
        this.idToName = [:]
        this.nameToId = [:]
    }

    def put(StorableObject item) {
        putRaw(item.getId(), item.getName())
    }

    def remove(StorableObject item) {
        removeRaw(item.getId(), item.getName())
    }

    def getName(id) {
        idToName[(id)]
    }

    def getId(name) {
        nameToId[(name)]
    }

    def allIds() {
        idToName.keySet()
    }

    def String toJson() {
        def data = [:]
        data.idToName = this.idToName
        data.nameToId = this.nameToId
        use(JsonOutput) {
            data.toJson()
        }
    }

    def fromJson(String json) {
        def json_data = new JsonSlurper().parseText(json)
        idToName = json_data.id_to_name
        nameToId = json_data.name_to_id
    }

    private putRaw(id, name) {
        idToName[(id)] = name
        nameToId[(name)] = id
    }

    private removeRaw(id, name) {
        idToName.remove(id)
        nameToId.remove(name)
    }
}