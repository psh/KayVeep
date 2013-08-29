package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/23/12 at 2:08 AM
 */
public class IdToNameMapper {
	private Map<String, String> id_to_name;
	private Map<String, String> name_to_id;

	public IdToNameMapper() {
		id_to_name = new LinkedHashMap<String, String>();
		name_to_id = new LinkedHashMap<String, String>();
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public void fromJson(String json) {
		IdToNameMapper m = new Gson().fromJson(json, IdToNameMapper.class);

		this.id_to_name = m.id_to_name;
		this.name_to_id = m.name_to_id;
	}

	public void put(StorableObject item) {
		this.putRaw(item.getId(), item.getName());
	}

	public void putRaw(String id, String name) {
		this.id_to_name.put(id, name);
		this.name_to_id.put(name, id);
	}

	public void remove(StorableObject item) {
		this.removeRaw(item.getId(), item.getName());
	}

	public void removeRaw(String id, String name) {
		this.id_to_name.remove(id);
		this.name_to_id.remove(name);
	}

	public String getName(String id) {
		return this.id_to_name.get(id);
	}

	public String getId(String name) {
		return this.name_to_id.get(name);
	}

	public List<String> allIds() {
		return new ArrayList<String>(this.id_to_name.keySet());
	}
}
