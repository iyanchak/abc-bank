package com.abc.domain.exceptions;

public class InvalidCustomerException extends BusinessException {
	private static final long serialVersionUID = 1827978046616513675L;
	public InvalidCustomerException(String message) {
		super(message);
	}
}
