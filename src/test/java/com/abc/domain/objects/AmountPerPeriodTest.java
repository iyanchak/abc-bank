package com.abc.domain.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.abc.domain.constants.Precision;
import com.abc.domain.exceptions.InvalidPeriodException;

/**
 * Class to test amount per period domain object
 * 
 * @author Ihor
 *
 */
public class AmountPerPeriodTest {

	// Test positive amount and period. It should succeed
	@Test
	public void testPositiveAmountAndPeriod() throws InvalidPeriodException {
		AmountPerPeriod amountPerPeriod = new AmountPerPeriod(5.0, 3);
		assertEquals(amountPerPeriod.getAmount(), 5.0, Precision.DOUBLE_PRECISION);
		assertEquals(amountPerPeriod.getDays(), 3);
	}

	// Test negative amount and period. It should fail
	@Test
	public void testNegativeAmountAndPeriod() throws InvalidPeriodException {
		try{
			new AmountPerPeriod(-5.0, -3);
			fail();
		}
		catch(InvalidPeriodException e){
			//Excepted
		}
	}

	// Test negative amount and positive period. It should succeed
	@Test
	public void testNegativeAmountAndPositivePeriod() throws InvalidPeriodException {
		AmountPerPeriod amountPerPeriod = new AmountPerPeriod(-5.0, 3);
		assertEquals(amountPerPeriod.getAmount(), -5.0, Precision.DOUBLE_PRECISION);
		assertEquals(amountPerPeriod.getDays(), 3);
	}

	// Test positive amount and negative period. It should fail
	@Test
	public void testPositiveAmountAndNegativePeriod() throws InvalidPeriodException {
		try{
			new AmountPerPeriod(5.0, -3);
			fail();
		}
		catch(InvalidPeriodException e){
			//Excepted
		}
	}
	
}
