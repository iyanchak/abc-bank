package com.abc.domain.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.abc.domain.constants.AccountType;
import com.abc.domain.constants.Precision;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;
import com.abc.domain.util.impl.DateTestHelperImpl;
import com.abc.util.DateTimeProvider;
import com.abc.util.impl.DateTimeProviderImpl;
/**
 * Test Account domain object
 * For daily interest rates, the expected rate was calculated manually using Windows 10 calculator. 
 * E.g. for multi day transaction in maxi-savings account done on three days 365 ago, 182 ago, 91 ago
 * the interest rate accrued is:
 * (9.9778910204323415116791735909692+
   24.763565242231429895420724745376+
   24.472895787683816075965225393111)+
   (4.9493009078485256048770098422766+
   9.291574211057275642684627949108)+
   4.0755277034013996840802408902768
 * @author Ihor
 *
 */
public class AccountTest {
	private DateTimeProvider dateTimeProvider365DaysAgo;
	private DateTimeProvider dateTimeProvider182DaysAgo;
	private DateTimeProvider dateTimeProviderTwoDaysAgo;
	private DateTimeProvider dateTimeProviderThreeDaysAgo;

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
		dateTimeProvider182DaysAgo=new DateTimeProvider(){
			int counter=0;			
			public LocalDateTime now() {
				LocalDateTime date=new DateTestHelperImpl().getMultiDaysAgoOrNowBasedOnCounter(counter,new long[] {182});
				counter++;
				return date;
			}
    	};
		dateTimeProviderTwoDaysAgo=new DateTimeProvider(){
			int counter=0;			
			public LocalDateTime now() {
				LocalDateTime date=new DateTestHelperImpl().getMultiDaysAgoOrNowBasedOnCounter(counter,new long[]{365,182});
				counter++;
				return date;
			}
    	};
    	dateTimeProviderThreeDaysAgo=new DateTimeProvider(){
			int counter=0;			
			public LocalDateTime now() {
				LocalDateTime date=new DateTestHelperImpl().getMultiDaysAgoOrNowBasedOnCounter(counter,new long[]{365,182,91});
				counter++;
				return date;
			}
    	};
    	
	}
	
	@Test // Test Account Positive Withdraw
	public void testAccountPositiveWithdraw() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
		account.withdraw(200.0);
		List<Transaction> transactions = account.getTransactions();
		assertEquals(1, transactions.size());
		Transaction transaction = transactions.get(0);
		assertEquals(transaction.getAmount(), -200.0, Precision.DOUBLE_PRECISION);
	}

	@Test // Test Zero Account Withdraw
	public void testAccountZeroWithdraw() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
		try {
			account.withdraw(0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test // Test Negative Account Withdraw
	public void testAccountNegativeWithdraw() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
		try {
			account.withdraw(-100.0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test // Test Account Positive Deposit
	public void testAccountPositiveDeposit() throws BusinessException {

		Account account = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
		account.deposit(100.0);
		List<Transaction> transactions = account.getTransactions();
		assertEquals(1, transactions.size());
		Transaction transaction = transactions.get(0);
		assertEquals(transaction.getAmount(), 100.0, Precision.DOUBLE_PRECISION);
	}

	@Test // Test Account Zero Deposit
	public void testAccountZeroDeposit() throws BusinessException {
		Account account = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);

		try {
			account.deposit(0.0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test // Test Account Negative Deposit
	public void testAccountNegativeDeposit() throws BusinessException {
		Account account = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);

		try {
			account.deposit(-1000.0);
			fail();
		} catch (InvalidTransactionException e) {
			// expected
		}
	}

	@Test //Test earned checking interest rate after 365 Days
    public void testCheckingEarnedInterestRateAfter365Days() throws BusinessException {
		
		Account account = new Account(AccountType.CHECKING,dateTimeProvider365DaysAgo);
		account.deposit(1500.0);
		assertEquals(1.5,account.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	
	
	@Test //Test earned savings interest rate under 1000 after 365 Days
    public void testSavingsEarnedInterestRateUnder1000After365Days() throws BusinessException {
		Account savingAccount = new Account(AccountType.SAVINGS,dateTimeProvider365DaysAgo);
		savingAccount.deposit(750.0);
		assertEquals(0.75,savingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate over 1000 after 365 Days
    public void testSavingsEarnedInterestRateOver1000After365Days() throws BusinessException {
		Account savingAccount  = new Account(AccountType.SAVINGS,dateTimeProvider365DaysAgo);
		savingAccount.deposit(1250.0);
		assertEquals(1.5,savingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	


	@Test //Test earned maxi savings interest rate under 1000 after 365 Days
    public void testMaxiSavingsEarnedInterestRateUnder1000After365Days() throws BusinessException {
		Account maxiSavingAccount = new Account(AccountType.MAXI_SAVINGS,dateTimeProvider365DaysAgo);
		maxiSavingAccount.deposit(750.0);
		assertEquals(15.0,maxiSavingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate under 2000 after 365 Days
    public void testMaxiSavingsEarnedInterestRateUnder2000After365Days() throws BusinessException {
		Account savingAccount  = new Account(AccountType.MAXI_SAVINGS,dateTimeProvider365DaysAgo);
		savingAccount.deposit(1700.0);
		assertEquals(55.0,savingAccount .calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate over 2000 after 365 Days
    public void testMaxiSavingsEarnedInterestRateOver2000After365Days() throws BusinessException {
		Account savingAccount  = new Account(AccountType.MAXI_SAVINGS,dateTimeProvider365DaysAgo);
		savingAccount.deposit(2500.0);
		assertEquals(120.0,savingAccount .calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned checking interest rate after 182 Days
    public void testCheckingEarnedInterestRateAfter182Days() throws BusinessException {
		
		Account account = new Account(AccountType.CHECKING,dateTimeProvider182DaysAgo);
		account.deposit(1500.0);
		assertEquals(0.74775780066311976621199576727046,account.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	
	
	@Test //Test earned savings interest rate under 1000 after 182 Days
    public void testSavingsEarnedInterestRateUnder1000After182Days() throws BusinessException {
		Account savingAccount = new Account(AccountType.SAVINGS,dateTimeProvider182DaysAgo);
		savingAccount.deposit(750.0);
		assertEquals(0.37387890033155988310599788363523,savingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate over 1000 after 182 Days
    public void testSavingsEarnedInterestRateOver1000After182Days() throws BusinessException {
		Account savingAccount  = new Account(AccountType.SAVINGS,dateTimeProvider182DaysAgo);
		savingAccount.deposit(1250.0);
		assertEquals(0.74769539483045061054276355856288,savingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	


	@Test //Test earned maxi savings interest rate under 1000 after 182 Days
    public void testMaxiSavingsEarnedInterestRateUnder1000After182Days() throws BusinessException {
		Account maxiSavingAccount = new Account(AccountType.MAXI_SAVINGS,dateTimeProvider182DaysAgo);
		maxiSavingAccount.deposit(750.0);
		assertEquals(7.4423230463801110871215505228452,maxiSavingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate under 2000 after 182 Days
    public void testMaxiSavingsEarnedInterestRateUnder2000After182Days() throws BusinessException {
		Account savingAccount  = new Account(AccountType.MAXI_SAVINGS,dateTimeProvider182DaysAgo);
		savingAccount.deposit(1700.0);
		assertEquals(27.161712164095583757370851084434,savingAccount .calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate over 2000 after 182Days
    public void testMaxiSavingsEarnedInterestRateOver2000After182Days() throws BusinessException {
		Account savingAccount  = new Account(AccountType.MAXI_SAVINGS,dateTimeProvider182DaysAgo);
		savingAccount.deposit(2500.0);
		assertEquals(58.885651243884128036848612945029,savingAccount .calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned checking interest rate after multiple Days
    public void testCheckingEarnedInterestRateAfterMultiDays() throws BusinessException {
		
		Account account = new Account(AccountType.CHECKING,dateTimeProviderTwoDaysAgo);
		account.deposit(1500.0);
		account.deposit(1500.0);
		assertEquals(2.2477578006631197662119957411426,account.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate after multiple Days
    public void testSavingsEarnedInterestRateAfterMultipleDays() throws BusinessException {
		Account savingAccount = new Account(AccountType.SAVINGS,dateTimeProviderTwoDaysAgo);
		savingAccount.deposit(750.0);
		savingAccount.deposit(500.0);
		assertEquals(1.1240038055589322706501103217248,savingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);
	}	

	@Test //Test earned savings interest rate after multiple Days
    public void testMaxiSavingsEarnedInterestRateAlfterMultipleDays() throws BusinessException {
		Account savingAccount  = new Account(AccountType.MAXI_SAVINGS,dateTimeProviderThreeDaysAgo);
		savingAccount.deposit(2500.0);
		savingAccount.withdraw(800.0);
		savingAccount.withdraw(950.0);
		assertEquals(77.530754872654788414707002411118,savingAccount.calculateInterestEarned(),Precision.DOUBLE_PRECISION);

	}	
	
}
