/**
 * 
 */
package com.abc.domain.exceptions;

/**
 * @author Ihor
 *
 */
public class InvalidPeriodException extends BusinessException {

	private static final long serialVersionUID = 8836080878991008928L;

	/**
	 * Constructor to create InvalidPeriodException
	 * 
	 * @param message
	 *            Error message
	 */
	public InvalidPeriodException(String message) {
		super(message);
	}

}
