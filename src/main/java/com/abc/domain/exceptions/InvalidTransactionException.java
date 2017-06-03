package com.abc.domain.exceptions;

public class InvalidTransactionException extends BusinessException {
	private static final long serialVersionUID = 3796741547535023801L;

	public InvalidTransactionException(String message) {
		super(message);
	}
}
