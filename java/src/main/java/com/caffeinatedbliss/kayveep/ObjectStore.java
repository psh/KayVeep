package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/23/12 at 1:40 AM
 */
public class ObjectStore<T extends StorableObject> {
	private DataStore data;
	private DataStore types;
	private KayVeepUtilities kayVeepUtilities;

	public ObjectStore() {
		data = new DataStore();
		types = new DataStore();
		kayVeepUtilities = new KayVeepUtilities();
	}

	public void setDataDir(String dir) {
		data.setDataDir(dir);
		types.setDataDir(new File(dir, "types").getAbsolutePath());
	}

	public T load(String key, Class<T> cls) {
		String json = data.load(key);
		if (json != null) {
			return new Gson().fromJson(json, cls);
		}
		return null;
	}

	public void store(T object) {
		String key = object.getId();
		if (key == null) {
			key = kayVeepUtilities.getKey();
			object.setId(key);
		}

		data.store(key, object.toJson());
		storeKey(key, object.getClass());
	}

	public void delete(T object) {
		deleteKey(object.getId(), object.getClass());
		data.delete(object.getId());
	}

	private void storeKey(String id, Class type) {
		String typeName = type.getName();
		String json = types.load(typeName);
		ArrayList<String> keys = new ArrayList<String>();
		if (json != null) {
			keys = new Gson().fromJson(json, new TypeToken<ArrayList<String>>() {
			}.getType());
		}
		keys.add(id);
		types.store(typeName, new Gson().toJson(keys));
	}

	private void deleteKey(String id, Class type) {
		String typeName = type.getName();
		String json = types.load(typeName);
		ArrayList<String> keys = new ArrayList<String>();
		if (json != null) {
			keys = new Gson().fromJson(json, new TypeToken<ArrayList<String>>() {
			}.getType());
			keys.remove(id);
		}
		types.store(typeName, new Gson().toJson(keys));
	}
}
