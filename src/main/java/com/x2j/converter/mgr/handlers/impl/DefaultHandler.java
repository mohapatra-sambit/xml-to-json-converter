package com.x2j.converter.mgr.handlers.impl;

import org.w3c.dom.Element;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.handlers.X2JStringHandler;

/**
 * The default handler for the X2JStringHandler interface. This is a fallback
 * handler in case an unknown keyword is used in the JSON schema. This handler
 * will return the JSON value string as is.
 */
public class DefaultHandler implements X2JStringHandler {

	/**
	 * Returns the same JSON value string as is.
	 *
	 * @param rawValueString the raw JSON value string
	 * @param rootElement    the root element in the input XML
	 * @return the raw JSON value string
	 * @throws X2JException X2JException
	 */
	@Override
	public String handleString(String rawValueString, Element rootElement) throws X2JException {
		return rawValueString;
	}

}
