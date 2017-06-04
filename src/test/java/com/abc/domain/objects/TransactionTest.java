package com.abc.domain.objects;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import com.abc.domain.constants.Precision;
import com.abc.util.DateTimeProvider;

/**
 * Class to test transaction domain object
 * 
 * @author Ihor
 *
 */
public class TransactionTest {

	// Test positive amount transaction
	@Test
	public void testPositiveAmountTransaction() {
		final LocalDateTime date = LocalDateTime.now();
		DateTimeProvider dt = new DateTimeProvider() {
			public LocalDateTime now() {
				return date;
			}
		};
		Transaction t = new Transaction(5.0, dt);
		assertEquals(t.getAmount(), 5.0, Precision.DOUBLE_PRECISION);
		assertEquals(t.getTransactionDate(), date);
	}

	// Test negative amount transaction
	@Test
	public void testNegativeAmountTransaction() {
		final LocalDateTime date = LocalDateTime.now();
		DateTimeProvider dt = new DateTimeProvider() {
			public LocalDateTime now() {
				return date;
			}
		};
		Transaction t = new Transaction(-3.0, dt);
		assertEquals(t.getAmount(), -3.0, Precision.DOUBLE_PRECISION);
		assertEquals(t.getTransactionDate(), date);
	}

}
