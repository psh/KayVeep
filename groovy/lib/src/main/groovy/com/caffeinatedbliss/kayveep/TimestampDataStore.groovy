package com.caffeinatedbliss.kayveep

/**
 * TimestampDataStore
 *
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 */
class TimestampDataStore extends MapDataStore {
    TimestampDataStore(String type, String fileExtn = "json") {
        super(type.toLowerCase() + "-time-to-id-map", fileExtn)
    }

    def Date load(String key) {
        new Date((long) super.load(key))
    }

    def store(String key, Date value) {
        super.store(key, value.getTime())
    }
}
