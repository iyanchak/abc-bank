package com.abc.domain.objects;

import org.junit.Test;

import com.abc.domain.constants.AccountType;
import com.abc.domain.constants.Precision;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;
import com.abc.domain.objects.Account;
import com.abc.util.impl.DateProviderImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

public class AccountTest {

	@Test // Test Account Positive Withdraw
	public void testAccountPositiveWithdraw() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
		account.withdraw(200.0);
		List<Transaction> transactions = account.getTransactions();
		assertEquals(1, transactions.size());
		Transaction transaction = transactions.get(0);
		assertEquals(transaction.getAmount(), -200.0, Precision.DOUBLE_PRECISION);
	}

	@Test // Test Zero Account Withdraw
	public void testAccountZeroWithdraw() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
		try {
			account.withdraw(0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test // Test Negative Account Withdraw
	public void testAccountNegativeWithdraw() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
		try {
			account.withdraw(-100.0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test // Test Account Positive Deposit
	public void testAccountPositiveDeposit() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
		account.deposit(100.0);
		List<Transaction> transactions = account.getTransactions();
		assertEquals(1, transactions.size());
		Transaction transaction = transactions.get(0);
		assertEquals(transaction.getAmount(), 100.0, Precision.DOUBLE_PRECISION);
	}

	@Test // Test Account Zero Deposit
	public void testAccountZeroDeposit() throws BusinessException {
		Account account = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);

		try {
			account.deposit(0.0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test // Test Account Negative Deposit
	public void testAccountNegativeDeposit() throws BusinessException {
		Account account = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);

		try {
			account.deposit(-1000.0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test //Test earned checking interest rate
    public void testCheckingEarnedInterestRate() throws BusinessException {
		Account account = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
		account.deposit(1500.0);
		assertEquals(1.5,account.interestEarned(),Precision.DOUBLE_PRECISION);
	}	
	
	@Test //Test earned savings interest rate under 1000
    public void testSavingsEarnedInterestRateUnder1000() throws BusinessException {
		Account savingAccount = new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE);
		savingAccount.deposit(750.0);
		assertEquals(0.75,savingAccount.interestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate over 1000
    public void testSavingsEarnedInterestRateOver1000() throws BusinessException {
		Account savingAccount  = new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE);
		savingAccount.deposit(1250.0);
		assertEquals(1.5,savingAccount.interestEarned(),Precision.DOUBLE_PRECISION);
	}	


	@Test //Test earned maxi savings interest rate under 1000
    public void testMaxiSavingsEarnedInterestRateUnder1000() throws BusinessException {
		Account maxiSavingAccount = new Account(AccountType.MAXI_SAVINGS,DateProviderImpl.INSTANCE);
		maxiSavingAccount.deposit(750.0);
		assertEquals(15.0,maxiSavingAccount.interestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate under 2000
    public void testMaxiSavingsEarnedInterestRateUnder2000() throws BusinessException {
		Account savingAccount  = new Account(AccountType.MAXI_SAVINGS,DateProviderImpl.INSTANCE);
		savingAccount.deposit(1700.0);
		assertEquals(55.0,savingAccount .interestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate over 2000
    public void testMaxiSavingsEarnedInterestRateOver2000() throws BusinessException {
		Account savingAccount  = new Account(AccountType.MAXI_SAVINGS,DateProviderImpl.INSTANCE);
		savingAccount.deposit(2500.0);
		assertEquals(120.0,savingAccount .interestEarned(),Precision.DOUBLE_PRECISION);
	}	
	
}
