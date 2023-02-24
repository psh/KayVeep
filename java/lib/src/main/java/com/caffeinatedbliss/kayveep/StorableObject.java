package com.caffeinatedbliss.kayveep;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/22/12 at 11:57 PM
 */
public interface StorableObject {
	String getId();

	void setId(String id);

	String getName();

	void setName(String name);

	String toJson();

	Object get(String name);

	void set(String name, Object value);
}
