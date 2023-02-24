package com.caffeinatedbliss.kayveep;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/26/12 at 1:40 PM
 */
public class ArrayDataStore extends MapDataStore<List<String>> {
	public ArrayDataStore(String typeMany, String typeOne) {
		super(typeMany.toLowerCase() + "-array-to-" + typeOne.toLowerCase() + "-id-map");
	}

	@Override
	protected Type getType() {
		return new TypeToken<Map<String, List<String>>>() {
		}.getType();
	}
}
