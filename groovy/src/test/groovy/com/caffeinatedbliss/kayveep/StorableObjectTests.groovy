package com.caffeinatedbliss.kayveep

/**
 * StorableObjectTests
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
class StorableObjectTests extends KayVeepTestCase {

	void testTransientProperties() {
		def obj = new TestObject()
		obj.foo = "one"
		assertEquals("one", obj.foo)
	}
}
