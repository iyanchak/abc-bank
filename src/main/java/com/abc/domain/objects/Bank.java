package com.abc.domain.objects;

import java.util.ArrayList;
import java.util.List;

import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidCustomerException;

public class Bank {
    private final List<Customer> customers;

    public Bank() {
        customers = new ArrayList<Customer>();
    }

    public void addCustomer(Customer customer) throws BusinessException {
    	if (customer==null){
    		throw new InvalidCustomerException("customer is null");
    	}
        customers.add(customer);
    }

    public String customerSummary() {
        String summary = "Customer Summary";
        for (Customer c : customers)
            summary += "\n - " + c.getName() + " (" + format(c.getNumberOfAccounts(), "account") + ")";
        return summary;
    }

    //Make sure correct plural of word is created based on the number passed in:
    //If number passed in is 1 just return the word otherwise add an 's' at the end
    private String format(int number, String word) {
        return number + " " + (number == 1 ? word : word + "s");
    }

    public double totalInterestPaid() throws BusinessException {
        double total = 0;
        for(Customer c: customers)
            total += c.totalInterestEarned();
        return total;
    }

    public String getFirstCustomerName() throws InvalidCustomerException {
    	  if (customers.size()>0){
    		  return customers.get(0).getName();
    	  }
    	  throw new InvalidCustomerException("No customers yet");
    }
}
