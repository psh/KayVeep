package com.caffeinatedbliss.kayveep

/**
 * StorableObjectTests
 *
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 */
class StorableObjectTests extends KayVeepTestCase {

	void testTransientProperties() {
		def obj = new TestObject()
		obj.foo = "one"
		assertEquals("one", obj.foo)
	}
}
