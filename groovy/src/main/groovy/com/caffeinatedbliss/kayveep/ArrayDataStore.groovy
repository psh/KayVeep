package com.caffeinatedbliss.kayveep

/**
 * ArrayDataStore - used to model a one-to-many relationship where a single
 * ID returns an array of related IDs when queried.
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
class ArrayDataStore extends MapDataStore {
    ArrayDataStore(String many, String one, String fileExtn = "json") {
        super(many.toLowerCase() + "-array-to-" + one.toLowerCase() + "-id-map", fileExtn)
    }
}
