package com.abc.domain.objects;

import java.time.LocalDateTime;

import com.abc.util.DateTimeProvider;

/**
 * Class that represents Transaction and contains amount and transaction date
 * 
 * @author Ihor
 *
 */
public class Transaction {
	private final double amount;
	private final LocalDateTime transactionDate;

	/**
	 * Constructor for transaction
	 * 
	 * @param amount
	 *            Amount
	 * @param dateTimeProvider
	 *            Date time provider for transaction
	 */
	public Transaction(double amount, DateTimeProvider dateTimeProvider) {
		this.amount = amount;
		transactionDate = dateTimeProvider.now();
	}

	/**
	 * Get transaction date
	 * 
	 * @return Transaction date
	 */
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Get amount for transaction
	 * 
	 * @return Amount for transaction
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * String representation for transaction
	 * 
	 * @return String representation for transaction
	 */
	@Override
	public String toString() {
		return "Transaction [amount=" + amount + ", transactionDate=" + transactionDate + "]";
	}

}
