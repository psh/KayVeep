package com.caffeinatedbliss.kayveep

/**
 * KayVeepException
 * 
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 */
class KayVeepException extends RuntimeException {
	KayVeepException(String message) {
		super(message)
	}

	KayVeepException(String message, Throwable cause) {
		super(message, cause)
	}
}
