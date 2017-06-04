/**
 * 
 */
package com.abc.domain.util.impl;

import static org.junit.Assert.assertFalse;

import java.time.LocalDateTime;

import org.junit.Test;

import com.abc.util.impl.DateTimeProviderImpl;

/**
 * Class to test date time provider
 * 
 * @author Ihor
 */
public class DateTimeProviderImplTest {
	// Test that now from date time provider is not before current time
	@Test
	public void testNow() {
		LocalDateTime now = DateTimeProviderImpl.INSTANCE.now();
		assertFalse(LocalDateTime.now().isBefore(now));
	}
}
