package com.abc.domain.objects;

import org.junit.Test;

import com.abc.domain.constants.AccountType;
import com.abc.domain.constants.Precision;
import com.abc.domain.exceptions.BusinessException;
import com.abc.domain.exceptions.InvalidTransactionException;
import com.abc.domain.objects.Account;
import com.abc.domain.objects.Customer;
import com.abc.util.impl.DateProviderImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CustomerTest {

    @Test //Test customer statement generation
    public void testStatementGeneration() throws BusinessException{

        Account checkingAccount = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE);

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

    @Test
    public void testOneAccount(){
        Customer oscar = new Customer("Oscar").openAccount(new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE));
        assertEquals(1, oscar.getNumberOfAccounts());
    }

    @Test
    public void testTwoAccount(){
        Customer oscar = new Customer("Oscar")
                .openAccount(new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE));
        oscar.openAccount(new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE));
        assertEquals(2, oscar.getNumberOfAccounts());
    }

    @Test
    public void testThreeAcounts() {
        Customer oscar = new Customer("Oscar")
                .openAccount(new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE));
        oscar.openAccount(new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE));
        oscar.openAccount(new Account(AccountType.MAXI_SAVINGS,DateProviderImpl.INSTANCE));
        assertEquals(3, oscar.getNumberOfAccounts());
    }
    
    @Test
    //Test transfer complete checking account to savings
    public void testAccountTransferCompleteAmount() throws BusinessException{
        Account checkingAccount = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE);

        Customer william = new Customer("William").openAccount(checkingAccount).openAccount(savingsAccount);

        checkingAccount.deposit(1000.0);
        savingsAccount.deposit(2500.0);
    	
        william.transfer(checkingAccount,savingsAccount,1000.0);
        assertEquals(checkingAccount.sumTransactions(),0.0,Precision.DOUBLE_PRECISION);
        assertEquals(savingsAccount.sumTransactions(),3500.0,Precision.DOUBLE_PRECISION);
    }
    
    @Test
    //Test attempt to transfer over amount in checking to savings    
    public void testAccountTransferOverAmountInAccount() throws BusinessException{
        Account checkingAccount = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE);

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

    @Test
    //Test attempt to transfer negative in checking to savings    
    public void testAccountTransferNegativeAmountAccount() throws BusinessException{
        Account checkingAccount = new Account(AccountType.CHECKING,DateProviderImpl.INSTANCE);
        Account savingsAccount = new Account(AccountType.SAVINGS,DateProviderImpl.INSTANCE);

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
    @Test
    //Test attempt to transfer from maxiSaving to itself
    public void testAccountTransferAccountToItself() throws BusinessException{
        Account maxiSavingAccount = new Account(AccountType.MAXI_SAVINGS,DateProviderImpl.INSTANCE);

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
    
    
}
