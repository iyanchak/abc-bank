package com.abc.domain.objects;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import com.abc.domain.constants.Precision;
import com.abc.util.DateTimeProvider;

public class TransactionTest {
    @Test
    public void testTransaction() {
    	final LocalDateTime date=LocalDateTime.now();
    	DateTimeProvider dt=new DateTimeProvider(){
			public LocalDateTime now() {
				return date;
			}
    	};
        Transaction t = new Transaction(5,dt);
        assertEquals(t.getAmount(),5.0,Precision.DOUBLE_PRECISION);
        assertEquals(t.getTransactionDate(),date);
    }
}
