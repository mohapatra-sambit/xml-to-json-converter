package com.x2j.converter.mgr.handlers.impl;

import org.w3c.dom.Element;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.handlers.X2JStringHandler;
import com.x2j.converter.utils.X2JErrorCodes;
import com.x2j.converter.utils.X2JUtils;

/**
 * The handler for the X2JStringHandler interface for the <b>SUBSTR</b> keyword.
 * <br>
 * If the given string is an XPath expression, this will resolve the same and
 * the extract the sub-string as per the given input.<br>
 * If the given string is not an XPath expression, then the sub-string will be
 * extracted directly from the string.<br>
 */
public class SubStringHandler implements X2JStringHandler {

	private int start = 0;

	private int end = 0;

	/**
	 * Returns the sub-string as per the given input.
	 *
	 * @param rawValueString the raw JSON value string
	 * @param rootElement    the root element in the input XML
	 * @return the sub-string value
	 * @throws X2JException X2JException
	 */
	@Override
	public String handleString(String rawValueString, Element rootElement) throws X2JException {
		String subStrValueString = rawValueString.substring(rawValueString.indexOf('(') + 1,
				rawValueString.lastIndexOf(')'));
		String[] values = subStrValueString.split(",");
		String value = X2JUtils.getStringValueForHandlers(values[0], rootElement);
		if (X2JUtils.isVoid(value)) {
			return "";
		} else {
			validateArgs(values, value);
			return value.substring(start, end + 1);
		}
	}

	private void validateArgs(String[] values, String text) throws X2JException {
		try {
			start = Integer.valueOf(values[1].trim());
			end = Integer.valueOf(values[2].trim());
			if (start < 0 || start >= text.length() || end < 0 || end >= text.length() || start > end) {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_011);
		}
	}

}
