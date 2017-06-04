package com.abc.util.impl;

import java.time.LocalDateTime;

import com.abc.util.DateTimeProvider;

/**
 * Singleton class to provide date time
 * 
 * @author Ihor
 *
 */
public enum DateTimeProviderImpl implements DateTimeProvider {
	/**
	 * Singleton instance for date time provider
	 */
	INSTANCE;
	/**
	 * Private constructor
	 */
	private DateTimeProviderImpl(){		
	}
	/**
	 * @see interface
	 */	
	@Override
	public LocalDateTime now() {
		return LocalDateTime.now();
	}
}
