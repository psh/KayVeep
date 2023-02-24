package com.caffeinatedbliss.kayveep;

import com.google.gson.Gson;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/23/12 at 2:30 AM
 */
public class ExampleDataObject extends AbstractStorableObject {
	private
	@RawField
	String data;

	public ExampleDataObject() {
	}

	public ExampleDataObject(String id, String name, String data) {
		setId(id);
		setName(name);
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public void fromJson(String json) {
		ExampleDataObject d = new Gson().fromJson(json, ExampleDataObject.class);
		setId(d.getId());
		setName(d.getName());
		this.data = d.data;
	}
}
