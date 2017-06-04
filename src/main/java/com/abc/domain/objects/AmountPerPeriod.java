package com.abc.domain.objects;

import com.abc.domain.exceptions.InvalidPeriodException;

/**
 * Data holder for amount and number days amount is held. Currently period can
 * be only days
 * 
 * @author Ihor
 *
 */
public class AmountPerPeriod {
	private final double amount;
	private final long days;

	/**
	 * Constructor for AmountPerPerido
	 * 
	 * @param amount
	 *            Amount
	 * @param days
	 *            Number of days held
	 * @throws InvalidPeriodException 
	 */
	public AmountPerPeriod(double amount, long days) throws InvalidPeriodException {
		this.amount = amount;
		this.days = days;
		if (days<0){
			throw new InvalidPeriodException("Number of days is invalid: "+days);
		}
	}

	/**
	 * Get amount
	 * 
	 * @return Amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Get number of days
	 * 
	 * @return Number of days
	 */
	public long getDays() {
		return days;
	}

	/**
	 * String Representation for amount per period
	 * 
	 * @return String for amount per period
	 */
	@Override
	public String toString() {
		return "AmountPerPeriod [amount=" + amount + ", days=" + days + "]";
	}

}
