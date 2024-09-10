package com.x2j.converter.mgr.handlers.impl;

import static com.x2j.converter.utils.X2JUtils.encodeText;

import org.w3c.dom.Element;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.handlers.X2JStringHandler;
import com.x2j.converter.utils.X2JUtils;

/**
 * The handler for the X2JStringHandler interface for the <b>XPATH</b> keyword.
 * This handler will resolve the XPATH as provided in the schema and returns the
 * String value for the same. <br>
 * <ol>
 * <li>If the XPATH is for an XML attribute, this will return the attribute's
 * value.</li>
 * <li>If the XPATH is for an XML Element, this will return the child element
 * (if present) for the given element in an encoded string format.</li>
 * </ol>
 */
public class XPathHandler implements X2JStringHandler {

	/**
	 * Returns the resolved XPATH value string.
	 *
	 * @param rawValueString the raw JSON value string
	 * @param rootElement    the root element in the input XML
	 * @return the resolved XPATH value string
	 * @throws X2JException X2JException
	 */
	@Override
	public String handleString(String rawValueString, Element rootElement) throws X2JException {
		String xPath = rawValueString.substring(rawValueString.indexOf('(') + 1, rawValueString.lastIndexOf(')'));
		if (xPath.contains("@")) {
			return encodeText(X2JUtils.getXpathAttribute(rootElement, xPath));
		} else {
			return encodeText(X2JUtils.getString(X2JUtils.getXpathElement(rootElement, xPath)));
		}
	}

}
