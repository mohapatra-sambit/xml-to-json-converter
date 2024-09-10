package com.x2j.converter.mgr.handlers;

import org.w3c.dom.Element;

import com.x2j.converter.excp.X2JException;

/**
 * The interface X2JStringHandler.
 */
public interface X2JStringHandler {

	/**
	 * Every String handler will have to implement this method for processing the
	 * JSON value as per the definition given in the schema.
	 *
	 * @param rawValueString the raw JSON value string
	 * @param rootElement    the root element of the input XML
	 * @return the string post the processing is done by the handler
	 * @throws X2JException X2JException
	 */
	String handleString(String rawValueString, Element rootElement) throws X2JException;

}