package com.abc.domain.objects;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.abc.domain.constants.AccountType;
import com.abc.domain.constants.DailyCompoundRate;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;
import com.abc.util.DateTimeProvider;

/**
 * Account domain object contains following information: - type of account -
 * list of transaction involving account - date time provider to use for
 * transactions
 * 
 * @author Ihor
 */
public class Account {

	private final AccountType accountType;
	private final List<Transaction> transactions;
	private final DateTimeProvider dateTimeProvider;

	/**
	 * The constructor for Account
	 * 
	 * @param accountType
	 *            Account type
	 * @param dateTimeProvider
	 *            Date time provider for transactions
	 */
	public Account(AccountType accountType, DateTimeProvider dateTimeProvider) {
		this.accountType = accountType;
		this.transactions = new ArrayList<Transaction>();
		this.dateTimeProvider = dateTimeProvider;
	}

	/**
	 * Method to deposit amount
	 * 
	 * @param amount
	 *            Amount to deposit
	 * @throws BusinessException
	 */
	public void deposit(double amount) throws BusinessException {
		if (amount <= 0) {
			throw new InvalidTransactionException("amount must be greater than zero");
		} else {
			transactions.add(new Transaction(amount, dateTimeProvider));
		}
	}

	/**
	 * Method to deposit amount
	 * 
	 * @param amount
	 *            Amount to deposit
	 * @throws BusinessException
	 */
	public void withdraw(double amount) throws BusinessException {
		if (amount <= 0) {
			throw new InvalidTransactionException("amount must be greater than zero");
		} else {
			transactions.add(new Transaction(-amount, dateTimeProvider));
		}
	}

	/**
	 * Method to calculate interest factor for compounding
	 * 
	 * @param rate
	 *            Daily rate
	 * @param days
	 *            Number of days
	 * @return interest factor
	 */
	private double calculateInterestFactor(double rate, long days) {
		return Math.pow(rate, days) - 1;
	}

	/**
	 * Method to get amounts that were held and for how many days
	 * 
	 * @return Amounts and number of days
	 * @throws BusinessException 
	 */
	private List<AmountPerPeriod> getAmountsPerPeriod() throws BusinessException {
		List<AmountPerPeriod> amountsPerPeriod = new ArrayList<AmountPerPeriod>();
		LocalDateTime previousDateTime = null;
		double previousAmount = 0.0;
		for (Transaction transaction : transactions) {
			LocalDateTime currentDateTime = transaction.getTransactionDate();
			if (previousDateTime != null) {
				long days = ChronoUnit.DAYS.between(previousDateTime.toLocalDate(), currentDateTime.toLocalDate());
				AmountPerPeriod amountPerPeriod = new AmountPerPeriod(previousAmount, days);
				amountsPerPeriod.add(amountPerPeriod);
			}
			previousDateTime = currentDateTime;
			previousAmount = previousAmount + transaction.getAmount();
		}
		if (previousDateTime != null) {
			long days = ChronoUnit.DAYS.between(previousDateTime.toLocalDate(), dateTimeProvider.now().toLocalDate());
			AmountPerPeriod amountPerPeriod = new AmountPerPeriod(previousAmount, days);
			amountsPerPeriod.add(amountPerPeriod);
		}
		return amountsPerPeriod;
	}

	/**
	 * method to calculate interest earned
	 * 
	 * @return Interest earned on this account
	 * @throws BusinessException 
	 */
	public double calculateInterestEarned() throws BusinessException {
		List<AmountPerPeriod> amountsPerPeriod = getAmountsPerPeriod();
		double interest = 0;
		double rollingInterest = 0.0;
		// compound interest calculated each period and add to amount in each
		// period
		for (AmountPerPeriod amountPerPeriod : amountsPerPeriod) {
			long days = amountPerPeriod.getDays();
			double amount = amountPerPeriod.getAmount() + rollingInterest;
			interest = calculateInterestEarned(amount, days);
			rollingInterest += interest;

		}
		return rollingInterest;
	}

	/**
	 * The method to calculate interest rate earned
	 * 
	 * @param amount
	 *            amount
	 * @param days
	 *            number of days held
	 * @return Interest rate earned Please note for leap year (365 days) the
	 *         annual interest will be higher then non-leap year (365)
	 */
	private double calculateInterestEarned(double amount, long days) {
		switch (accountType) {
		case SAVINGS:
			if (amount <= 1000) {
				return amount * calculateInterestFactor(DailyCompoundRate.Savings.UNDER_1000, days);
			} else {
				return 1000 * calculateInterestFactor(DailyCompoundRate.Savings.UNDER_1000, days)
						+ (amount - 1000) * calculateInterestFactor(DailyCompoundRate.Savings.OVER_1000, days);
			}
		case MAXI_SAVINGS:
			if (amount <= 1000) {
				return amount * calculateInterestFactor(DailyCompoundRate.MaxiSavings.UNDER_1000, days);
			}
			if (amount <= 2000) {
				return 1000 * calculateInterestFactor(DailyCompoundRate.MaxiSavings.UNDER_1000, days)
						+ (amount - 1000) * calculateInterestFactor(DailyCompoundRate.MaxiSavings.UNDER_2000, days);
			}
			return 1000 * calculateInterestFactor(DailyCompoundRate.MaxiSavings.UNDER_1000, days)
					+ 1000 * calculateInterestFactor(DailyCompoundRate.MaxiSavings.UNDER_2000, days)
					+ +(amount - 2000) * calculateInterestFactor(DailyCompoundRate.MaxiSavings.OVER_2000, days);
		case CHECKING:
		default:
			return amount * calculateInterestFactor(DailyCompoundRate.Checking.ANY, days);
		}
	}

	/**
	 * Method to sum all transactions on account
	 * 
	 * @return Sum of all transaction amounts
	 */
	public double sumTransactions() {
		double amount = 0.0;
		for (Transaction t : transactions)
			amount += t.getAmount();
		return amount;
	}

	/**
	 * get account type
	 * 
	 * @return Account type
	 */
	public AccountType getAccountType() {
		return accountType;
	}

	/**
	 * get transactions
	 * 
	 * @return Transaction list
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * String Representation for account
	 * 
	 * @return String for account
	 */
	@Override
	public String toString() {
		return "Account [accountType=" + accountType + ", transactions=" + transactions + ", dateTimeProvider="
				+ dateTimeProvider + "]";
	}
}
