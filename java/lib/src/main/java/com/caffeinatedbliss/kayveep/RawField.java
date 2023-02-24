package com.caffeinatedbliss.kayveep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/26/12 at 10:14 PM
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RawField {
}
