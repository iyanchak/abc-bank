package com.abc.domain.objects;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

import com.abc.domain.constants.TransferState;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;

public class Customer {
    private String name;
    private List<Account> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<Account>();
    }

    public String getName() {
        return name;
    }

    public Customer openAccount(Account account) {
        accounts.add(account);
        return this;
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public double totalInterestEarned() throws BusinessException {
        double total = 0;
        for (Account a : accounts)
            total += a.calculateInterestEarned();
        return total;
    }

    public String getStatement() {
        String statement = null;
        statement = "Statement for " + name + "\n";
        double total = 0.0;
        for (Account a : accounts) {
            statement += "\n" + statementForAccount(a) + "\n";
            total += a.sumTransactions();
        }
        statement += "\nTotal In All Accounts " + toDollars(total);
        return statement;
    }

    private String statementForAccount(Account a) {
        String s = "";

       //Translate to pretty account type
        switch(a.getAccountType()){
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

        //Now total up all the transactions
        double total = 0.0;
        for (Transaction t : a.getTransactions()) {
            s += "  " + (t.getAmount() < 0 ? "withdrawal" : "deposit") + " " + toDollars(t.getAmount()) + "\n";
            total += t.getAmount();
        }
        s += "Total " + toDollars(total);
        return s;
    }

    private String toDollars(double d){
        return String.format("$%,.2f", abs(d));
    }
    
    //Check whether account belongs to customer
    private void checkAccountOwnership (Account account) throws InvalidTransactionException{
    	if (!accounts.contains(account)){
			throw new InvalidTransactionException("account is not owned by customer: "+account);
    	}
    }

	public void transfer(Account accountFrom, Account accountTo, double amount) throws BusinessException {
		checkAccountOwnership(accountFrom);
		checkAccountOwnership(accountTo);
		
		if (accountFrom.equals(accountTo)){
			throw new InvalidTransactionException("Cannot transfer to the same account");
		}
		if (accountFrom.sumTransactions()<amount){
			throw new InvalidTransactionException("Cannot transfer more then current amount"+amount);			
		}
		
		TransferState transferState=TransferState.INITIAL;
		try{
			accountFrom.withdraw(amount);
			transferState=TransferState.WITHDRAWN_COMPLETE;
			accountTo.deposit(amount);
		}
		// compensate withdraw if deposit fail
		catch(Exception exception){
			if (TransferState.WITHDRAWN_COMPLETE.equals(transferState)){
				accountFrom.deposit(amount);
			}
			throw exception;
		}
	}
}
