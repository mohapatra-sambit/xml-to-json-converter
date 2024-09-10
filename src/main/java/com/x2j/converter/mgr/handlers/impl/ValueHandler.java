package com.x2j.converter.mgr.handlers.impl;

import static com.x2j.converter.utils.X2JUtils.encodeText;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.handlers.X2JStringHandler;
import com.x2j.converter.utils.X2JUtils;

/**
 * The handler for the X2JStringHandler interface for the <b>VALUE</b> keyword.
 * As part of this:<br>
 * <ol>
 * <li>If the XPath defined in the schema is for an XML attribute, this will
 * have the same behavior as that of XPathHandler i.e. return the attribute's
 * value.</li>
 * <li>If the XPATH defined in the schema is for an XML element, then,<br>
 * <ul>
 * <li>This checks whether there is a text component for the element or not. For
 * example,
 * 
 * <pre>
 * &lt;ElementName&gt;TextValue&lt;/Element&gt;
 * </pre>
 * 
 * </li>
 * <li>If present, it returns the text value.</li>
 * <li>Otherwise, if there are one or multiple child elements, this will return
 * the all of the child element in an encoded String format.</li>
 * <li>For everything else, it will return a blank String</li>
 * </ul>
 * </li>
 * </ol>
 */
public class ValueHandler implements X2JStringHandler {

	/**
	 * Returns the value of the XPath which can be either the text component or
	 * child element(s) in String form.
	 *
	 * @param rawValueString the JSON value string
	 * @param rootElement    the root element in the input XML
	 * @return the value of the XPath defined in the schema
	 * @throws X2JException X2JException
	 */
	@Override
	public String handleString(String rawValueString, Element rootElement) throws X2JException {
		String xPath = rawValueString.substring(rawValueString.indexOf('(') + 1, rawValueString.lastIndexOf(')'));
		if (xPath.contains("@")) {
			return encodeText(X2JUtils.getXpathAttribute(rootElement, xPath));
		} else {
			Element elem = X2JUtils.getXpathElement(rootElement, xPath);
			if (!X2JUtils.isVoid(elem)) {
				if (elem.hasChildNodes()) {
					NodeList allChildNodes = elem.getChildNodes();
					if (allChildNodes.getLength() > 1) {
						StringBuilder builder = new StringBuilder();
						for (int i = 0; i < allChildNodes.getLength(); i++) {
							Node node = allChildNodes.item(i);
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								builder.append(encodeText(X2JUtils.getString(
										X2JUtils.getXpathElement(rootElement, xPath + "/" + node.getNodeName()))));
							}
						}
						return builder.toString();
					} else {
						Node child = allChildNodes.item(0);
						if (child.getNodeType() == Node.TEXT_NODE) {
							return encodeText(child.getNodeValue().trim());
						} else if (child.getNodeType() == Node.ELEMENT_NODE) {
							return encodeText(X2JUtils.getString(X2JUtils.getXpathElement(rootElement, xPath)));
						}
					}
				}
			}
		}
		return "";
	}

}
