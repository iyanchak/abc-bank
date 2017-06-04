package com.abc.domain.exceptions;

/**
 * The superclass for all business exception in Bank
 * 
 * @author Ihor
 */
public class BusinessException extends Exception {
	private static final long serialVersionUID = 7156699628264008860L;

	/**
	 * The constructor for BusinessException
	 * 
	 * @param message
	 *            Error message
	 */
	public BusinessException(String message) {
		super(message);
	}
}
