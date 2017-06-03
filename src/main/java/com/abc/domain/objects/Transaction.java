package com.abc.domain.objects;

import java.util.Date;

import com.abc.util.DateProvider;

public class Transaction {
    private final double amount;
    private final Date transactionDate;

    public Transaction(double amount,DateProvider dateProvider) {
        this.amount = amount;
        transactionDate=dateProvider.now();
    }

	public Date getTransactionDate() {
		return transactionDate;
	}

	public double getAmount() {
		return amount;
	}


}
