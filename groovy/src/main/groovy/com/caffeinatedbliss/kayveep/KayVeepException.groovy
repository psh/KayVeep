package com.caffeinatedbliss.kayveep

/**
 * KayVeepException
 * 
 * @author Paul Hawke (phawke@rgare.com)
 */
class KayVeepException extends RuntimeException {
	KayVeepException(String message) {
		super(message)
	}

	KayVeepException(String message, Throwable cause) {
		super(message, cause)
	}
}
