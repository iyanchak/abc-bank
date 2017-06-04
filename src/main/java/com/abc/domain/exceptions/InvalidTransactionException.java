package com.abc.domain.exceptions;

/**
 * The exception to indicate that transaction is invalid. It is Business
 * Exception subclass.
 * 
 * @author Ihor
 */
public class InvalidTransactionException extends BusinessException {
	private static final long serialVersionUID = 3796741547535023801L;

	/**
	 * The constructor for InvalidTransactionException
	 * 
	 * @param message
	 *            Error message
	 */
	public InvalidTransactionException(String message) {
		super(message);
	}
}
