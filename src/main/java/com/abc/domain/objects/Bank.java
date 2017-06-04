package com.abc.domain.objects;

import java.util.ArrayList;
import java.util.List;

import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidCustomerException;

/**
 * Bank class contains list of customers that have accounts in the bank.
 * 
 * @author Ihor
 *
 */
public class Bank {
	private final List<Customer> customers;

	/**
	 * Constructor for Bank cass
	 */
	public Bank() {
		customers = new ArrayList<Customer>();
	}

	/**
	 * method to add customer
	 * 
	 * @param customer
	 *            Customer with account(s) in Bank
	 * @throws BusinessException
	 */
	public void addCustomer(Customer customer) throws BusinessException {
		if (customer == null) {
			throw new InvalidCustomerException("customer is null");
		}
		customers.add(customer);
	}

	/**
	 * Method to generate customer summary
	 * 
	 * @return Customer summary
	 */
	public String generateCustomerSummary() {
		String summary = "Customer Summary";
		for (Customer c : customers)
			summary += "\n - " + c.getName() + " (" + format(c.getNumberOfAccounts(), "account") + ")";
		return summary;
	}

	/**
	 * Method to format word
	 * 
	 * @param number
	 *            Number to format
	 * @param word
	 *            Word to format
	 * 
	 * @return Formatted word
	 * 
	 *         Make sure correct plural of word is created based on the number
	 *         passed in: If number passed in is 1 just return the word
	 *         otherwise add an 's' at the end
	 * 
	 */
	private String format(int number, String word) {
		return number + " " + (number == 1 ? word : word + "s");
	}

	/**
	 * Total interest paid for all customer
	 * 
	 * @return Total interest paid
	 * @throws BusinessException
	 */
	public double totalInterestPaid() throws BusinessException {
		double total = 0;
		for (Customer c : customers)
			total += c.totalInterestEarned();
		return total;
	}

	/**
	 * Method to get first customer name
	 * 
	 * @return First customer name
	 * @throws InvalidCustomerException
	 */
	public String getFirstCustomerName() throws InvalidCustomerException {
		if (customers.size() > 0) {
			return customers.get(0).getName();
		}
		throw new InvalidCustomerException("No customers yet");
	}

	/**
	 * String representation for Bank
	 * 
	 * @return String representation for Bank
	 */
	@Override
	public String toString() {
		return "Bank [customers=" + customers + "]";
	}
}
