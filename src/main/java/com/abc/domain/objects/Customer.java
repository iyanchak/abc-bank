package com.abc.domain.objects;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

import com.abc.domain.constants.TransferState;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;

/**
 * Class for customer that contains customer name and list of accounts
 * 
 * @author Ihor
 */
public class Customer {
	private final String name;
	private final List<Account> accounts;

	/**
	 * Constructor to create customer class
	 * 
	 * @param name
	 *            Name of customer
	 */
	public Customer(String name) {
		this.name = name;
		this.accounts = new ArrayList<Account>();
	}

	/**
	 * Method to get name of customer
	 * 
	 * @return Name of customer
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method to open account
	 * 
	 * @param account
	 *            Account to open
	 * @return the class itself
	 */
	public Customer openAccount(Account account) {
		accounts.add(account);
		return this;
	}

	/**
	 * Get number of Accouns for the customer
	 * 
	 * @return Number of accounts for the customer
	 */
	public int getNumberOfAccounts() {
		return accounts.size();
	}

	/**
	 * Method to get total interest earned
	 * 
	 * @return Total interest earned
	 * @throws BusinessException
	 */
	public double totalInterestEarned() throws BusinessException {
		double total = 0;
		for (Account a : accounts)
			total += a.calculateInterestEarned();
		return total;
	}

	/**
	 * Method to get Statement
	 * 
	 * @return Statement
	 */
	public String getStatement() {
		String statement = "Statement for " + name + "\n";
		double total = 0.0;
		for (Account a : accounts) {
			statement += "\n" + getStatementForAccount(a) + "\n";
			total += a.sumTransactions();
		}
		statement += "\nTotal In All Accounts " + formatToDollars(total);
		return statement;
	}

	/**
	 * Method to get statement for account
	 * 
	 * @param account
	 *            Account
	 * @return Statement for account
	 */
	private String getStatementForAccount(Account account) {
		String s = "";

		// Translate to pretty account type
		switch (account.getAccountType()) {
		case CHECKING:
			s += "Checking Account\n";
			break;
		case SAVINGS:
			s += "Savings Account\n";
			break;
		case MAXI_SAVINGS:
			s += "Maxi Savings Account\n";
			break;
		}

		// Now total up all the transactions
		double total = 0.0;
		for (Transaction t : account.getTransactions()) {
			s += "  " + (t.getAmount() < 0 ? "withdrawal" : "deposit") + " " + formatToDollars(t.getAmount()) + "\n";
			total += t.getAmount();
		}
		s += "Total " + formatToDollars(total);
		return s;
	}

	/**
	 * Format to dollars
	 * 
	 * @param amount
	 *            Amount
	 * @return formatted amount
	 */
	private String formatToDollars(double amount) {
		return String.format("$%,.2f", abs(amount));
	}

	/**
	 * Method to check whether account belongs to customer
	 * 
	 * @param account
	 *            Account
	 * @throws InvalidTransactionException
	 *             when account does not belong to customer
	 */
	private void checkAccountOwnership(Account account) throws InvalidTransactionException {
		if (!accounts.contains(account)) {
			throw new InvalidTransactionException("account is not owned by customer: " + account);
		}
	}

	/**
	 * Method to transfer between accounts
	 * 
	 * @param accountFrom
	 *            Account from
	 * @param accountTo
	 *            Account to
	 * @param amount
	 *            Amount
	 * @throws BusinessException
	 */
	public void transfer(Account accountFrom, Account accountTo, double amount) throws BusinessException {
		checkAccountOwnership(accountFrom);
		checkAccountOwnership(accountTo);

		if (accountFrom.equals(accountTo)) {
			throw new InvalidTransactionException("Cannot transfer to the same account");
		}
		if (accountFrom.sumTransactions() < amount) {
			throw new InvalidTransactionException("Cannot transfer more then current amount" + amount);
		}

		TransferState transferState = TransferState.INITIAL;
		try {
			accountFrom.withdraw(amount);
			transferState = TransferState.WITHDRAWN_COMPLETE;
			accountTo.deposit(amount);
		}
		// compensate withdraw if deposit fail
		catch (Exception exception) {
			if (TransferState.WITHDRAWN_COMPLETE.equals(transferState)) {
				accountFrom.deposit(amount);
			}
			throw exception;
		}
	}

	/**
	 * String representation of customer
	 * 
	 * @return String representation of customer
	 */
	@Override
	public String toString() {
		return "Customer [name=" + name + ", accounts=" + accounts + "]";
	}
}
