package com.caffeinatedbliss.kayveep;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/26/12 at 9:58 PM
 */
public class DomainDataStore<T extends StorableObject> {
	private final Class<T> dataType;
	private final String mapName;
	private final List<String> rawFields;
	private final RawDataStore rawData;
	private final ObjectStore<T> data;
	private final DataStore meta;
	private final TimestampDataStore created;
	private final TimestampDataStore updated;
	private IdToNameMapper map;
	private final KayVeepUtilities kayVeepUtilities;

	public DomainDataStore(Class<T> type) {
		this(type, null);
	}

	public DomainDataStore(Class<T> type, String[] raw) {
		this.dataType = type;
		String mapType = dataType.getSimpleName();
		String basename = mapType.toLowerCase();
		this.mapName = basename + "-id-to-name-map";

		this.rawFields = extractRawFieldsFromTarget(type, raw);

		rawData = new RawDataStore();
		data = new ObjectStore<>();
		meta = new DataStore();
		created = new TimestampDataStore(basename + "-create");
		updated = new TimestampDataStore(basename + "-update");
		kayVeepUtilities = new KayVeepUtilities();
	}

	public void setDataDir(String dir) {
		this.data.setDataDir(dir);
		this.rawData.setDataDir(dir);
		this.created.setDataDir(dir);
		this.updated.setDataDir(dir);

		this.meta.setDataDir(new File(dir, "meta").getAbsolutePath());
		this.map = kayVeepUtilities.loadMapping(this.mapName, meta);
	}

	public void store(T obj) {
		if (obj.getId() == null) {
			create(obj);
			return;
		}
		this.data.store(obj);
		this.updateMapping(obj);
		Long updatedTime = System.currentTimeMillis();
		this.updated.store(obj.getId(), updatedTime);
		obj.set("updated_time", updatedTime);
	}

	public void create(T obj) {
		String key = kayVeepUtilities.getKey();
		obj.setId(key);
		this.data.store(obj);
		this.updateMapping(obj);
		Long createdTime = System.currentTimeMillis();
		this.created.store(obj.getId(), createdTime);
		obj.set("created_time", createdTime);
	}

	public T loadById(String id) {
		T obj = this.data.load(id, this.dataType);
		if (obj != null) {
			// this.load_raw_properties(obj);
			this.updateMapping(obj);
			obj.set("created_time", this.created.load(obj.getId()));
			obj.set("updated_time", this.updated.load(obj.getId()));
		}
		return obj;
	}

	public T loadByName(String name) {
		String id = this.map.getId(name);
		return (id != null) ? loadById(id) : null;
	}

	public void delete(T obj) {
		this.data.delete(obj);
		this.map.remove(obj);
		this.created.delete(obj.getId());
		this.updated.delete(obj.getId());
		kayVeepUtilities.storeMapping(this.mapName, this.map, meta);
	}

	public List<String> getAllIds() {
		return this.map.allIds();
	}

	public List<T> filter(FilterFunction<T> f) {
		List<T> ret = new ArrayList<>();
		for (String id : this.map.allIds()) {
			T obj = this.loadById(id);
			if (obj != null && f.filter(obj)) {
				ret.add(obj);
			}
		}
		return ret;
	}

	public List<String> getRawFields() {
		return rawFields;
	}

	@SuppressWarnings("rawtypes")
	private static List<String> extractRawFieldsFromTarget(Class type, String[] raw) {
		List<String> theRawFields = (raw != null) ? new ArrayList<>(Arrays.asList(raw)) : new ArrayList<>();
		for (Field field : type.getDeclaredFields()) {
			if (field.isAnnotationPresent(RawField.class)) {
				theRawFields.add(field.getName());
			}
		}
		return theRawFields;
	}

	private void updateMapping(StorableObject obj) {
		this.map.remove(obj);
		this.map.put(obj);
		kayVeepUtilities.storeMapping(this.mapName, this.map, meta);
	}

	public interface FilterFunction<U> {
		boolean filter(U item);
	}
/*

  public function create(obj, key = NULL) {
    if (!isset(key)) {
      key = this.raw_data.get_key();
    }
    obj.set_id(key);
    if (is_array(this.raw_fields) && count(this.raw_fields) > 0) {
      reflect = new ReflectionClass(this.map_type);
      foreach (this.raw_fields as name) {
        property = reflect.getProperty(name);
        raw_value = property.getValue(obj);
        if (isset(raw_value) && strlen(trim(raw_value)) > 0) {
          raw_key = key . "-" . name;
          this.raw_data.store(raw_key, trim(raw_value));
          property.setValue(obj, null);
        }
      }
    }
    this.data.store(obj);
    this.update_mapping(obj);
    created_time = time();
    this.created.store(obj.get_id(), created_time);
    obj.created_time = created_time;
  }

  private function load_raw_properties(obj) {
    if (is_array(this.raw_fields) && count(this.raw_fields) > 0) {
      reflect = new ReflectionClass(this.map_type);
      foreach (this.raw_fields as name) {
        raw_key = obj.get_id() . "-" . name;
        property = reflect.getProperty(name);
        raw_value = this.raw_data.load(raw_key);
        if (isset(raw_value)) {
          property.setValue(obj, raw_value);
        }
      }
    }
  }

  public function store(obj) {
    if (is_array(this.raw_fields) && count(this.raw_fields) > 0) {
      reflect = new ReflectionClass(this.map_type);
      foreach (this.raw_fields as name) {
        raw_key = obj.get_id() . "-" . name;
        to = reflect.getProperty(name);
        raw_value = to.getValue(obj);
        if (raw_value != null && strlen(trim(raw_value)) > 0) {
          this.raw_data.store(raw_key, trim(raw_value));
          to.setValue(obj, null);
        } else {
          this.raw_data.delete(raw_key);
        }
      }
    }
    this.data.store(obj);
    this.update_mapping(obj);
    this.updated.store(obj.get_id(), time());
  }

  public function filtered_list(filter) {
    ret = Array();
    arr = this.map.all_ids();
    for (i = 0; i < count(arr); i++) {
      obj = this.load_by_id(arr[i]);
      if (filter(obj)) {
        ret[] = obj;
      }
    }
    return ret;
  }
*/
}
