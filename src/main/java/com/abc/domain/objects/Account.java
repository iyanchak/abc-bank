package com.abc.domain.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.abc.domain.constants.AccountType;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;
import com.abc.util.DateProvider;

public class Account {

	private final AccountType accountType;
	private final List<Transaction> transactions;
	private final DateProvider dateProvider;

	public Account(AccountType accountType,DateProvider dateProvider) {
		this.accountType = accountType;
		this.transactions = new ArrayList<Transaction>();
		this.dateProvider=dateProvider;
	}

	public void deposit(double amount) throws BusinessException {
		if (amount <= 0) {
			throw new InvalidTransactionException("amount must be greater than zero");
		} else {
			transactions.add(new Transaction(amount, dateProvider));
		}
	}

	public void withdraw(double amount) throws BusinessException {
		if (amount <= 0) {
			throw new InvalidTransactionException("amount must be greater than zero");
		} else {
			transactions.add(new Transaction(-amount, dateProvider));
		}
	}
	public double interestEarned() {
		double amount = sumTransactions();
		switch (accountType) {
		case SAVINGS:
			if (amount <= 1000){
				return amount * 0.001;
			}
			else{
				return 1 + (amount - 1000) * 0.002;
			}
		case MAXI_SAVINGS:
			if (amount <= 1000){
				return amount * 0.02;
			}
			if (amount <= 2000){
				return 20 + (amount - 1000) * 0.05;
			}
			return 70 + (amount - 2000) * 0.1;
		case CHECKING:
		default:			
			return amount * 0.001;
		}
	}

	public double sumTransactions() {
		double amount = 0.0;
		for (Transaction t : transactions)
			amount += t.getAmount();
		return amount;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}
}
