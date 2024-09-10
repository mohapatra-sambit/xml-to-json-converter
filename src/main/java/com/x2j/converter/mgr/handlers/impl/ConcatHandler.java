package com.x2j.converter.mgr.handlers.impl;

import org.w3c.dom.Element;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.handlers.X2JStringHandler;
import com.x2j.converter.utils.X2JUtils;

/**
 * The handler for the X2JStringHandler interface for the <b>CONCAT</b> keyword.
 * <br>
 * There is no limit to the number of strings that needs to be concatenated
 * here.<br>
 * All the string that needs to concatenated must be separated by a comma
 * (,).<br>
 * For a XPath string, this will resolve the value and then perform the
 * concatenation on the value.<br>
 * Any normal string will be directly used for concatenation.
 */
public class ConcatHandler implements X2JStringHandler {

	/**
	 * Returns the concatenated string of values (resolved values/JSON values).
	 *
	 * @param rawValueString the raw JSON value string
	 * @param rootElement    the root element in the input XML
	 * @return the concatenated value string
	 * @throws X2JException X2JException
	 */
	@Override
	public String handleString(String rawValueString, Element rootElement) throws X2JException {
		String concatValueString = rawValueString.substring(rawValueString.indexOf('(') + 1,
				rawValueString.lastIndexOf(')'));
		String[] values = concatValueString.split(",");
		StringBuilder builder = new StringBuilder();
		for (String value : values) {
			builder.append(X2JUtils.getStringValueForHandlers(value, rootElement));
		}
		return builder.toString().replaceAll("\\$\\{COMMA\\}", ",");
	}

}
