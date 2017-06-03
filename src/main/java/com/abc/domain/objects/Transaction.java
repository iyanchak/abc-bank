package com.abc.domain.objects;


import java.time.LocalDateTime;

import com.abc.util.DateTimeProvider;

public class Transaction {
    private final double amount;
    private final LocalDateTime transactionDate;

    public Transaction(double amount,DateTimeProvider dateTimeProvider) {
        this.amount = amount;
        transactionDate=dateTimeProvider.now();
    }

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public double getAmount() {
		return amount;
	}


}
