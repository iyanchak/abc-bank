package com.abc.util.impl;

import java.util.Date;

import com.abc.util.DateProvider;

public enum DateProviderImpl implements DateProvider {
	INSTANCE;
	public Date now() {
	        return new Date();
	}
}
