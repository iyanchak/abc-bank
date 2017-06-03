package com.abc.domain.constants;
/**
 * @author Ihor
 * The daily compound rate+1 based on 365 days
 * 
 * Following matrix was used to convert rates from non-leap year to daily
 * 0.1% => 1.0000027383608262443494848243471
 * 0.2% => 1.0000054739948799896979591374816
 * 2% =>   1.0000542552451767719379729880399
 * 5% =>   1.0001336806171134403505084797728
 * 10% =>  1.0002611578760678121616866817054 
 */
public interface DailyCompoundRate {
	interface Checking {	
		static final double ANY=1.0000027383608262443494848243471;
	}
	interface Savings {	
		static final double UNDER_1000=1.0000027383608262443494848243471;
		static final double OVER_1000=1.0000054739948799896979591374816;
	}
	interface MaxiSavings {	
		static final double UNDER_1000=1.0000542552451767719379729880399;
		static final double UNDER_2000=1.0001336806171134403505084797728;
		static final double OVER_2000=1.0002611578760678121616866817054;
	}
	
}
