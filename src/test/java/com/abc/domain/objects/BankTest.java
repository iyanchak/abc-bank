package com.abc.domain.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.abc.domain.constants.AccountType;
import com.abc.domain.constants.Precision;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidCustomerException;
import com.abc.domain.util.impl.DateTestHelperImpl;
import com.abc.util.DateTimeProvider;
import com.abc.util.impl.DateTimeProviderImpl;

public class BankTest {
	private DateTimeProvider dateTimeProvider365DaysAgo;

	@Before
	public void setup(){
		dateTimeProvider365DaysAgo=new DateTimeProvider(){
			int counter=0;
			public LocalDateTime now() {
				LocalDateTime date=new DateTestHelperImpl().getMultiDaysAgoOrNowBasedOnCounter(counter,new long[] {365});
				counter++;
				return date;
			}
    	};
	}
    @Test
    public void testCustomerSummary() throws BusinessException {
        Bank bank = new Bank();
        Customer john = new Customer("John");
        john.openAccount(new Account(AccountType.CHECKING,dateTimeProvider365DaysAgo));
        bank.addCustomer(john);

        assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
    }

    @Test
    public void testCheckingAccount() throws BusinessException {
        Bank bank = new Bank();
        Account checkingAccount = new Account(AccountType.CHECKING,dateTimeProvider365DaysAgo);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);

        checkingAccount.deposit(100.0);

        assertEquals(0.1, bank.totalInterestPaid(), Precision.DOUBLE_PRECISION);
    }

    @Test
    public void testSavingsAccount() throws BusinessException {
        Bank bank = new Bank();
        Account savingsAccount = new Account(AccountType.SAVINGS,dateTimeProvider365DaysAgo);
        bank.addCustomer(new Customer("Bill").openAccount(savingsAccount));

        savingsAccount.deposit(1500.0);

        assertEquals(2.0, bank.totalInterestPaid(), Precision.DOUBLE_PRECISION);
    }

    @Test
    public void testMaxiSavingsAccount() throws BusinessException {
        Bank bank = new Bank();
        Account maxiSavingsAccount = new Account(AccountType.MAXI_SAVINGS,dateTimeProvider365DaysAgo);
        bank.addCustomer(new Customer("Bill").openAccount(maxiSavingsAccount));

        maxiSavingsAccount.deposit(3000.0);

        assertEquals(170.0, bank.totalInterestPaid(), Precision.DOUBLE_PRECISION);
    }
    @Test
    //Test that retrieval of first customer name gives error if no customers yet
    public void testGetFirstCustomerNameEmpty(){
        Bank bank = new Bank();
        try{
        	bank.getFirstCustomerName();
        	fail();
        }
        catch(InvalidCustomerException e){
        	//expected
        }
    }

    @Test
    //Test that retrieval of first customer name succeeds if there is one customer
    public void testGetFirstCustomerNameOneCustomer() throws BusinessException{
        Bank bank = new Bank();
        Account checkingAccount = new Account(AccountType.CHECKING,dateTimeProvider365DaysAgo);
        Customer george = new Customer("George").openAccount(checkingAccount);
        bank.addCustomer(george);
        assertEquals("George",bank.getFirstCustomerName());
    }
    
    @Test
    //Test that first customer remains the same when second customer is added
    public void testGetFirstCustomerNameRemainsTheSameWhenSecond() throws BusinessException{
        Bank bank = new Bank();
        Account georgeCheckingAccount = new Account(AccountType.CHECKING,dateTimeProvider365DaysAgo);
        Customer george = new Customer("George").openAccount(georgeCheckingAccount);
        bank.addCustomer(george);
        
        Account karenSavingsAccount = new Account(AccountType.SAVINGS,dateTimeProvider365DaysAgo);
        Customer karen = new Customer("Karen").openAccount(karenSavingsAccount);
        bank.addCustomer(karen);
        
        assertEquals("George",bank.getFirstCustomerName());
    }
    
}
