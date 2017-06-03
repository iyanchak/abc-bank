package com.abc.domain.objects;

import org.junit.Test;

import com.abc.domain.constants.Precision;
import com.abc.domain.objects.Transaction;
import com.abc.util.DateProvider;

import static org.junit.Assert.assertEquals;

import java.util.Date;

public class TransactionTest {
    @Test
    public void testTransaction() {
    	final Date date=new Date();
    	DateProvider dt=new DateProvider(){
			public Date now() {
				return date;
			}
    	};
        Transaction t = new Transaction(5,dt);
        assertEquals(t.getAmount(),5.0,Precision.DOUBLE_PRECISION);
        assertEquals(t.getTransactionDate(),date);
    }
}
