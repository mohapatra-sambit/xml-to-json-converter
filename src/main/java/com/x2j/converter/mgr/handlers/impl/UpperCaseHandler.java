package com.x2j.converter.mgr.handlers.impl;

import org.w3c.dom.Element;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.handlers.X2JStringHandler;
import com.x2j.converter.utils.X2JUtils;

/**
 * The handler for the X2JStringHandler interface for the <b>UPPER</b> keyword.
 * This handler will resolve the XPATH as provided in the schema and returns the
 * String value in upper case.
 */
public class UpperCaseHandler implements X2JStringHandler {

	/**
	 * Returns the resolved XPATH value string in upper case.
	 *
	 * @param rawValueString the raw JSON value string
	 * @param rootElement    the root element in the input XML
	 * @return the resolved XPATH value string in upper case
	 * @throws X2JException X2JException
	 */
	@Override
	public String handleString(String rawValueString, Element rootElement) throws X2JException {
		String value = rawValueString.substring(rawValueString.indexOf('(') + 1, rawValueString.lastIndexOf(')'));
		return X2JUtils.getStringValueForHandlers(value, rootElement).toUpperCase();
	}

}
