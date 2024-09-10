package com.x2j.converter.excp;

import com.x2j.converter.utils.X2JErrorCodes;
import com.x2j.converter.utils.X2JMsgUtils;

/**
 * Custom exception class for X2J code base.
 */
public class X2JException extends Exception {

	private static final long serialVersionUID = 1800544087010859876L;

	/**
	 * Instantiates a new X2JException object.
	 *
	 * @param errorCode the error code
	 */
	public X2JException(X2JErrorCodes errorCode) {
		super(X2JMsgUtils.getInstance().getMessage(errorCode.name()));
	}

	/**
	 * Returns the error message.
	 *
	 * @return the error message
	 */
	public String getErrorMessage() {
		return super.getMessage();
	}

}
