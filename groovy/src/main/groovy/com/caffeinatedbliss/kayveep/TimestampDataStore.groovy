package com.caffeinatedbliss.kayveep

/**
 * TimestampDataStore
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
class TimestampDataStore extends MapDataStore {
    TimestampDataStore(String type, String fileExtn = "json") {
        super(type.toLowerCase() + "-time-to-id-map", fileExtn)
    }

    @Override
    def Date load(String key) {
        new Date(super.load(key))
    }

    @Override
    def store(String key, Date value) {
        super.store(key, value.getTime())
    }
}
