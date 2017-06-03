package com.abc.util.impl;

import java.time.LocalDateTime;

import com.abc.util.DateTimeProvider;

public enum DateTimeProviderImpl implements DateTimeProvider {
	INSTANCE;
	public LocalDateTime now() {
	        return LocalDateTime.now();
	}
}
