package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;

import java.util.UUID;

/**
 * KeyVeepUtilities
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
public class KayVeepUtilities {
	public IdToNameMapper loadMapping(String name, DataStore meta) {
		IdToNameMapper map = new IdToNameMapper();
		String json = meta.load(name);

		if (json != null) {
			map = new Gson().fromJson(json, IdToNameMapper.class);
		}

		return map;
	}

	public void storeMapping(String name, IdToNameMapper map, DataStore meta) {
		meta.store(name, new Gson().toJson(map));
	}

	public String getKey() {
		return UUID.randomUUID().toString();
	}
}
