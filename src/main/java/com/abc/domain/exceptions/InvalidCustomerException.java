package com.abc.domain.exceptions;

/**
 * The exception to indicate that Customer data is invalid. It is Business
 * Exception subclass.
 * 
 * @author Ihor
 */
public class InvalidCustomerException extends BusinessException {
	private static final long serialVersionUID = 1827978046616513675L;

	/**
	 * The constructor for InvalidCustomerException
	 * 
	 * @param message
	 *            Error message
	 */
	public InvalidCustomerException(String message) {
		super(message);
	}
}
