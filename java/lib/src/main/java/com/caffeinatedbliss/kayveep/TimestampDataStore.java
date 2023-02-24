package com.caffeinatedbliss.kayveep;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/26/12 at 12:21 PM
 */
public class TimestampDataStore extends MapDataStore<Long> {
	public TimestampDataStore(String type) {
		super(type.toLowerCase() + "-time-to-id-map");
	}

	@Override
	protected Type getType() {
		return new TypeToken<Map<String, Long>>() {
		}.getType();
	}
}
