package com.caffeinatedbliss.kayveep;

import java.io.File;
import java.util.StringTokenizer;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 3/3/12 at 1:05 AM
 */
public class ShortNameDataStore {
	private String mapName;
	private DataStore meta;
	private KayVeepUtilities kayVeepUtilities;

	public ShortNameDataStore() {
		this.mapName = "short-name-to-id-map";
		this.meta = new DataStore();
		kayVeepUtilities = new KayVeepUtilities();
	}

	public void setDataDir(String dataDir) {
		this.meta.setDataDir(new File(dataDir, "meta").getAbsolutePath());
	}

	public String createShortName(String id) {
		IdToNameMapper map = kayVeepUtilities.loadMapping(this.mapName, this.meta);
		String name = map.getName(id);
		if (name != null) {
			return name;
		}
		name = this.generateName(id, map);
		map.putRaw(id, name);
		kayVeepUtilities.storeMapping(this.mapName, map, meta);
		return name;
	}

	public void deleteShortName(String name) {
		IdToNameMapper map = kayVeepUtilities.loadMapping(this.mapName, this.meta);
		String id = map.getId(name);
		if (id != null) {
			map.removeRaw(id, name);
			kayVeepUtilities.storeMapping(this.mapName, map, meta);
		}
	}

	private String generateName(String id, IdToNameMapper map) {
		StringTokenizer tok = new StringTokenizer(id, "-");
		int value = 0;
		while (tok.hasMoreTokens()) {
			value ^= Integer.parseInt(tok.nextToken(), 16);
		}

		String name = String.format("%04x", value);
		String lookup = map.getId(name);
		while (lookup != null) {
			value += 13;
			name = String.format("%04x", value);
			lookup = map.getId(name);
		}

		return name;
	}

/*
  public void create_short_name(id) {
    name = this.generate_name(id);
    this.map.put_raw(id, name);
    this.meta.store(this.mapName, this.map.to_json());
    return name;
  }

  public void get_id(name) {
    return this.map.get_id(name);
  }

  public void delete_short_name(name) {
    id = this.map.get_id(name);
    this.map.remove_raw(id, name);
    this.meta.store(this.mapName, this.map.to_json());
  }

  private void load_mapping(name) {
    map = new IdToNameMapper();
    json = this.meta.load(name);

    if (json) {
      map.from_json(json);
    }

    return map;
  }

  private void store_mapping(name, map) {
    this.meta.store(name, map.to_json());
  }

  private void generate_name(id) {
    arr = sscanf(id, '%04x%04x-%04x-%04x-%04x-%04x%04x%04x');
    short = 0;
    for (i = 0; i < count(arr); i++) {
      short ^= arr[i];
    }

    name = sprintf('%04x', short);
    id = this.map.get_id(name);
    while (isset(id)) {
      short += 13;
      name = sprintf('%04x', short);
      id = this.map.get_id(name);
    }

    return name;
  }

 */
}