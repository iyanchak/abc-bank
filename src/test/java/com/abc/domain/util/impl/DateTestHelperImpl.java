package com.abc.domain.util.impl;

import java.time.LocalDateTime;

public class DateTestHelperImpl {
	public LocalDateTime getDaysAgo(long days){
		LocalDateTime localDateTime=LocalDateTime.now();
		localDateTime=localDateTime.minusDays(days);
		localDateTime=localDateTime.minusMinutes(1);// to make sure it is days
		return localDateTime;		
	}
	//Get days ago for matching counter or now for remaining calls
	public LocalDateTime getMultiDaysAgoOrNowBasedOnCounter(int counter,long[] days) {
		for (int i=0;i<days.length;i++){
			if (counter==i){
				return getDaysAgo(days[i]);
			}
		}
		return LocalDateTime.now();

	}	
}
