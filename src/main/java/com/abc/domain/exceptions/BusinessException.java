package com.abc.domain.exceptions;

public class BusinessException extends Exception {
	private static final long serialVersionUID = 7156699628264008860L;	
	public BusinessException(String message) {
		super(message);
	}
}
