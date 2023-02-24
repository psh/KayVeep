package com.caffeinatedbliss.kayveep

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * DataStore
 *
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 */
class DataStore {
    private File dataDir
    private String fileExtn

    DataStore(String fileExtn = "json") {
        this.fileExtn = fileExtn
    }

    def File getDataDir() {
        return dataDir
    }

    def setDataDir(File dir) {
        this.dataDir = dir
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new KayVeepException("Failed to create data directory.")
            }
        }
    }

    def load(String key) {
        def dataFile = constructFilename(key)
        if (dataFile.exists()) {
            return dataFile.text
        }
        return null
    }

    def store(String key, Object value) {
        def dataFile = constructFilename(key)
        dataFile.text = value
    }

    def delete(String key) {
        def dataFile = constructFilename(key)
        dataFile.delete()
    }

    private constructFilename(String key) {
        new File(this.dataDir, key + "." + this.fileExtn)
    }

    protected String encodeJson(data) {
        use(JsonOutput) {
            data.toJson()
        }
    }

    protected Map decodeJson(String json) {
        new JsonSlurper().parseText(json)
    }
}
