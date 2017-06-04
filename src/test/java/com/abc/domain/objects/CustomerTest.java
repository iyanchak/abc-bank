package com.abc.domain.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;

import org.junit.Test;

import com.abc.domain.constants.AccountType;
import com.abc.domain.constants.Precision;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;
import com.abc.util.DateTimeProvider;
import com.abc.util.impl.DateTimeProviderImpl;
/**
 * Class to test customer domain object
 * @author Ihor
 *
 */
public class CustomerTest {

	//Test customer statement generation
    @Test 
    public void testStatementGeneration() throws BusinessException{

        Account checkingAccount = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE);

        Customer henry = new Customer("Henry").openAccount(checkingAccount).openAccount(savingsAccount);

        checkingAccount.deposit(100.0);
        savingsAccount.deposit(4000.0);
        savingsAccount.withdraw(200.0);

        assertEquals("Statement for Henry\n" +
                "\n" +
                "Checking Account\n" +
                "  deposit $100.00\n" +
                "Total $100.00\n" +
                "\n" +
                "Savings Account\n" +
                "  deposit $4,000.00\n" +
                "  withdrawal $200.00\n" +
                "Total $3,800.00\n" +
                "\n" +
                "Total In All Accounts $3,900.00", henry.getStatement());
    }

    //Test opening of one savings account
    @Test
    public void testOneAccount(){
        Customer oscar = new Customer("Oscar").openAccount(new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE));
        assertEquals(1, oscar.getNumberOfAccounts());
    }

    //Test opening of savings and checking accounts    
    @Test
    public void testTwoAccount(){
        Customer oscar = new Customer("Oscar")
                .openAccount(new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE));
        oscar.openAccount(new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE));
        assertEquals(2, oscar.getNumberOfAccounts());
    }

  //Test opening of savings, checking and maxi savings accounts
    @Test
    public void testThreeAcounts() {
        Customer oscar = new Customer("Oscar")
                .openAccount(new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE));
        oscar.openAccount(new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE));
        oscar.openAccount(new Account(AccountType.MAXI_SAVINGS,DateTimeProviderImpl.INSTANCE));
        assertEquals(3, oscar.getNumberOfAccounts());
    }
    
    @Test
    //Test transfer complete amount from checking account to savings
    public void testAccountTransferCompleteAmount() throws BusinessException{
        Account checkingAccount = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE);

        Customer william = new Customer("William").openAccount(checkingAccount).openAccount(savingsAccount);

        checkingAccount.deposit(1000.0);
        savingsAccount.deposit(2500.0);
    	
        william.transfer(checkingAccount,savingsAccount,1000.0);
        assertEquals(checkingAccount.sumTransactions(),0.0,Precision.DOUBLE_PRECISION);
        assertEquals(savingsAccount.sumTransactions(),3500.0,Precision.DOUBLE_PRECISION);
    }

    //Test attempt to transfer over amount in checking to savings        
    @Test
    public void testAccountTransferOverAmountInAccount() throws BusinessException{
        Account checkingAccount = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE);

        Customer william = new Customer("William").openAccount(checkingAccount).openAccount(savingsAccount);

        checkingAccount.deposit(250.0);
        savingsAccount.deposit(1500.0);
        try{
        	william.transfer(checkingAccount,savingsAccount,300.0);
        	fail();
        }
        catch (InvalidTransactionException e){
        	//Expected
        }
    }

    //Test attempt to transfer negative in checking to savings        
    @Test
    public void testAccountTransferNegativeAmountAccount() throws BusinessException{
        Account checkingAccount = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE);

        Customer william = new Customer("William").openAccount(checkingAccount).openAccount(savingsAccount);

        checkingAccount.deposit(250.0);
        savingsAccount.deposit(1500.0);
        try{
        	william.transfer(checkingAccount,savingsAccount,-10.0);
        	fail();
        }
        catch (InvalidTransactionException e){
        	//Expected
        }
    }
    
    //Test attempt to transfer from maxi savings to itself    
    @Test
    public void testAccountTransferAccountToItself() throws BusinessException{
        Account maxiSavingAccount = new Account(AccountType.MAXI_SAVINGS,DateTimeProviderImpl.INSTANCE);

        Customer william = new Customer("William").openAccount(maxiSavingAccount);

        maxiSavingAccount.deposit(2000.0);
        try{
        	william.transfer(maxiSavingAccount,maxiSavingAccount,10.0);
        	fail();
        }
        catch (InvalidTransactionException e){
        	//Expected
        }
    }
    
    //Test attempt to transfer from savings to checking when deposit fails    
    @Test
    public void testAccountTransferWhenDepositFails() throws BusinessException{
        Account savingsAccount = new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE);
    	DateTimeProvider dt=new DateTimeProvider(){
			public LocalDateTime now() {
				throw new IllegalArgumentException("Test error");
			}
    	};
        
        Account checkingAccount = new Account(AccountType.CHECKING,dt);
        Customer mary = new Customer("Mary").openAccount(checkingAccount).openAccount(savingsAccount);

        savingsAccount.deposit(2000.0);
        try{
        	mary.transfer(savingsAccount,checkingAccount,1.0);
        	fail();
        }
        catch (IllegalArgumentException e){
        	//Expected
        }
        assertEquals(2000.0,savingsAccount.sumTransactions(),Precision.DOUBLE_PRECISION);
        assertEquals(0.0,checkingAccount.sumTransactions(),Precision.DOUBLE_PRECISION);
        
    }

    //Test attempt to transfer from account not owned by customer    
    @Test
    public void testAccountTransferWhenFromAccountIsNotOwnedByCustomer() throws BusinessException{
        Account bobSavingsAccount = new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE);
        Account bobMaxiSavingsAccount = new Account(AccountType.MAXI_SAVINGS,DateTimeProviderImpl.INSTANCE);
        Account aliceCheckingAccount = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
        Customer bob = new Customer("Bob").openAccount(bobSavingsAccount);
        bobSavingsAccount.deposit(100.0);
        aliceCheckingAccount.deposit(2000.0);
        new Customer("Alice").openAccount(aliceCheckingAccount);
        try{
        	bob.transfer(aliceCheckingAccount, bobMaxiSavingsAccount, 1000.0);
        	fail();
        }
        catch (InvalidTransactionException e){
        	//Expected
        }
        assertEquals(100.0,bobSavingsAccount.sumTransactions(),Precision.DOUBLE_PRECISION);
        assertEquals(2000.0,aliceCheckingAccount.sumTransactions(),Precision.DOUBLE_PRECISION);
    }

    //Test attempt to transfer to account not owned by customer    
    @Test
    public void testAccountTransferWhenToAccountIsNotOwnedByCustomer() throws BusinessException{
        Account bobSavingsAccount = new Account(AccountType.SAVINGS,DateTimeProviderImpl.INSTANCE);
        Account bobMaxiSavingsAccount = new Account(AccountType.MAXI_SAVINGS,DateTimeProviderImpl.INSTANCE);
        Account aliceCheckingAccount = new Account(AccountType.CHECKING,DateTimeProviderImpl.INSTANCE);
        Customer bob = new Customer("Bob").openAccount(bobSavingsAccount);
        bobSavingsAccount.deposit(100.0);
        aliceCheckingAccount.deposit(2000.0);
        new Customer("Alice").openAccount(aliceCheckingAccount);
        try{
        	bob.transfer(bobMaxiSavingsAccount, aliceCheckingAccount, 50.0);
        	fail();
        }
        catch (InvalidTransactionException e){
        	//Expected
        }
        assertEquals(100.0,bobSavingsAccount.sumTransactions(),Precision.DOUBLE_PRECISION);
        assertEquals(2000.0,aliceCheckingAccount.sumTransactions(),Precision.DOUBLE_PRECISION);
    }
    
}
