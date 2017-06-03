package com.abc.domain.objects;

public class AmountPerPeriod {
	private final double amount;
	private final long days;
	public AmountPerPeriod(double amount, long day) {
		this.amount = amount;
		this.days = day;
	}
	public double getAmount() {
		return amount;
	}
	public long getDays() {
		return days;
	}
	@Override
	public String toString() {
		return "AmountPerPeriod [amount=" + amount + ", days=" + days + "]";
	}

}
