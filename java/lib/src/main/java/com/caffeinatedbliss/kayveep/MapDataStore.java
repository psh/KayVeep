package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/24/12 at 10:58 PM
 */
public abstract class MapDataStore<T> {

	private final DataStore meta;
	private final String mapName;

	public MapDataStore(String name) {
		this.meta = new DataStore();
		this.mapName = name;
	}

	public void setDataDir(String dataDir) {
		this.meta.setDataDir(new File(dataDir, "meta").getAbsolutePath());
	}

	public void store(String id, T data) {
		Map<String, T> map = this.loadMapping(this.mapName);
		map.put(id, data);
		this.meta.store(this.mapName, new Gson().toJson(map));
	}

	public T load(String id) {
		Map<String, T> map = this.loadMapping(this.mapName);
		return map.get(id);
	}

	public Map<String, T> loadAll() {
		return this.loadMapping(this.mapName);
	}

	public void delete(String id) {
		Map<String, T> map = this.loadMapping(this.mapName);
		map.remove(id);
		this.meta.store(this.mapName, new Gson().toJson(map));
	}

	private Map<String, T> loadMapping(String name) {
		Map<String, T> map = new LinkedHashMap<>();

		String json = this.meta.load(name);
		if (json != null) {
			Gson gson = new Gson();
			map = gson.fromJson(json, getType());
		}

		return map;
	}

	protected abstract Type getType();
}
