package com.caffeinatedbliss.kayveep;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/23/12 at 12:00 AM
 */
public abstract class AbstractStorableObject implements StorableObject {
	transient Map<String, Object> _additional_vars = new HashMap<String, Object>();
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object get(String name) {
		return this._additional_vars.get(name);
	}

	public void set(String name, Object value) {
		this._additional_vars.put(name, value);
	}

}
