package com.abc.util;

import java.time.LocalDateTime;

/**
 * Interface to provide date time information
 * 
 * @author Ihor
 *
 */
public interface DateTimeProvider {
	/**
	 * Method to get date time for current date time
	 * 
	 * @return current date time
	 */
	LocalDateTime now();
}
