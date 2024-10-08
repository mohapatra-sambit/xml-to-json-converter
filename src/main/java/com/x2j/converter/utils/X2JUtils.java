package com.x2j.converter.utils;

import static com.x2j.converter.utils.X2JConstants.AMP;
import static com.x2j.converter.utils.X2JConstants.APOS;
import static com.x2j.converter.utils.X2JConstants.GT;
import static com.x2j.converter.utils.X2JConstants.LT;
import static com.x2j.converter.utils.X2JConstants.QUOT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.x2j.converter.excp.X2JException;

/**
 * This is a utility class which implements some common XML/String utilities
 * functionalities that are used across the XML-To-JSON Converter project.
 */
public class X2JUtils {

	private static final DocumentBuilderFactory docBuilderfactory = DocumentBuilderFactory.newInstance();

	private static final XPathFactory xpathFactory = XPathFactory.newInstance();

	private static final TransformerFactory transFactory = TransformerFactory.newInstance();

	private static Map<Character, String> splCharsMap;

	static {
		splCharsMap = new HashMap<Character, String>();
		splCharsMap.put('<', LT);
		splCharsMap.put('>', GT);
		splCharsMap.put('\"', QUOT);
		splCharsMap.put('\'', APOS);
		splCharsMap.put('&', AMP);
	}

	/**
	 * Null check for objects. <br>
	 * Null or empty check for String.
	 *
	 * @param obj Any object
	 * @return true/false
	 */
	public static boolean isVoid(Object obj) {
		if (obj instanceof String) {
			return ((String) obj).trim().length() == 0;
		}
		if (obj == null) {
			return true;
		}
		return false;
	}

	/**
	 * Reads and parses the file and returns the XML document.
	 *
	 * @param file the input file
	 * @return XML Document
	 * @throws X2JException if the file is not readable or the content is not a
	 *                      valid XML.
	 */
	public static Document getXmlFromFile(File file) throws X2JException {
		try {
			DocumentBuilder builder = docBuilderfactory.newDocumentBuilder();
			return builder.parse(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_001);
		}
	}

	/**
	 * Reads and parses the file and returns the JSON object.
	 *
	 * @param file the input file
	 * @return JSON Object
	 * @throws X2JException if the file is not readable or the content is not a
	 *                      valid JSON.
	 */
	public static JSONObject getJsonFromFile(File file) throws X2JException {
		try {
			String content = new String(Files.readAllBytes(file.toPath()));
			return new JSONObject(content);
		} catch (JSONException | IOException e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_002);
		}
	}

	/**
	 * Reads and parses the string and returns the JSON object.
	 *
	 * @param jsonString the JSON string
	 * @return JSON Object
	 * @throws X2JException if the String is not a valid JSON.
	 */
	public static JSONObject getJsonFromString(String jsonString) throws X2JException {
		try {
			return X2JUtils.isVoid(jsonString) ? null : new JSONObject(jsonString);
		} catch (JSONException e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_003);
		}
	}

	/**
	 * Reads and parses the string and returns the XML document.
	 *
	 * @param inputXml the input XML
	 * @return XML Document
	 * @throws X2JException if the String is not a valid XML.
	 */
	public static Document getXmlFromString(String inputXml) throws X2JException {
		try {
			DocumentBuilder builder = docBuilderfactory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(inputXml)));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_009);
		}
	}

	/**
	 * Returns the list of nodes in the XML element as per the path specified in the
	 * XPath.
	 *
	 * @param element the XML element
	 * @param xpath   the xpath
	 * @return the list of nodes
	 * @throws X2JException if the xpath is not a valid XPATH expression
	 */
	public static NodeList getNodesFromXPath(Element element, String xpath) throws X2JException {
		return (NodeList) executeXPath(element, xpath, XPathConstants.NODESET, X2JErrorCodes.X2J_ERR_004);
	}

	/**
	 * Returns the value of an attribute in the XML element as per the path
	 * specified in the XPath.
	 *
	 * @param element the XML element
	 * @param xpath   the xpath
	 * @return the value of the attribute
	 * @throws X2JException if the xpath is not a valid XPATH expression
	 */
	public static String getXpathAttribute(Element element, String xpath) throws X2JException {
		return (String) executeXPath(element, xpath, XPathConstants.STRING, X2JErrorCodes.X2J_ERR_005);
	}

	/**
	 * Returns the element in the XML element as per the path specified in the
	 * XPath.
	 *
	 * @param element the XML element
	 * @param xpath   the xpath
	 * @return the element
	 * @throws X2JException if the xpath is not a valid XPATH expression
	 */
	public static Element getXpathElement(Element element, String xpath) throws X2JException {
		return (Element) executeXPath(element, xpath, XPathConstants.NODE, X2JErrorCodes.X2J_ERR_006);
	}

	/**
	 * Parses the XML document and returns the same as a String object.
	 *
	 * @param document the XML document
	 * @return the string equivalent
	 * @throws X2JException if the argument is null or there is an error while
	 *                      transforming XML to String
	 */
	public static String getString(Document document) throws X2JException {
		if (document == null) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_007);
		}
		return getString(document.getDocumentElement());
	}

	/**
	 * Parses the XML element and returns the same as a String object.
	 *
	 * @param element the XML element
	 * @return the string equivalent
	 * @throws X2JException if the argument is null or there is an error while
	 *                      transforming XML to String
	 */
	public static String getString(Element element) throws X2JException {
		try {
			if (element == null) {
				throw new IllegalArgumentException("Element is null");
			}
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(element), new StreamResult(buffer));
			return buffer.toString();
		} catch (IllegalArgumentException | TransformerException e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_007);
		}
	}

	/**
	 * Writes the JSON object to a file.
	 *
	 * @param json the JSON Object
	 * @param file the target file
	 * @throws X2JException if there is any error during the file writing process
	 */
	public static void writeJsonToFile(JSONObject json, File file) throws X2JException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(file));
			writer.write(json.toString(4));
		} catch (Exception e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_008);
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Encodes the special characters in the given text. <br>
	 * <ul>
	 * <li>&lt; : &amp;lt;</li>
	 * <li>&gt; : &amp;gt;</li>
	 * <li>&apos; : &amp;apos;</li>
	 * <li>&quot; : &amp;quot;</li>
	 * <li>&amp; : &amp;amp;</li>
	 * </ul>
	 *
	 * @param text any input String
	 * @return the encoded string
	 */
	public static String encodeText(String text) {
		if (X2JUtils.isVoid(text)) {
			return "";
		}
		return IntStream.range(0, text.length()).mapToObj(i -> {
			char c = (char) text.charAt(i);
			return splCharsMap.containsKey(c) ? splCharsMap.get(c) : "" + c;
		}).collect(Collectors.joining());
	}

	/**
	 * Verifies whether the given expression is a valid XPath or not. <br>
	 * In the context of this application, a simple string is not a valid XPath
	 * expression.
	 *
	 * @param expression the XPATH expression
	 * @return true, if the expression is valid a XPath, otherwise false
	 */
	public static boolean isValidXPath(String expression) {
		if (expression == null || expression.trim().isEmpty()) {
			return false;
		}
		if (!expression.contains("/") && !expression.contains("@") && !expression.contains("[")) {
			return false;
		}
		XPath xPath = xpathFactory.newXPath();
		try {
			xPath.compile(expression);
			return true;
		} catch (XPathExpressionException e) {
			return false;
		}
	}

	/**
	 * Returns the string value as per specified schema for the String handlers.
	 *
	 * @param value       the XPath string
	 * @param rootElement the root element in the input XML
	 * @return the value after processing the XPath
	 * @throws X2JException X2JException
	 */
	public static String getStringValueForHandlers(String value, Element rootElement) throws X2JException {
		value = value.trim();
		if (isValidXPath(value)) {
			if (value.contains("@")) {
				return encodeText(getXpathAttribute(rootElement, value));
			} else {
				Element elem = getXpathElement(rootElement, value);
				if (!isVoid(elem)) {
					if (elem.hasChildNodes()) {
						NodeList allChildNodes = elem.getChildNodes();
						for (int i = 0; i < allChildNodes.getLength(); i++) {
							Node child = allChildNodes.item(i);
							if (child.getNodeType() == Node.TEXT_NODE) {
								return encodeText(child.getNodeValue().trim());
							}
						}
					}
				}
			}
		} else {
			return value;
		}
		return "";
	}

	private static Object executeXPath(Element element, String xpath, QName type, X2JErrorCodes errCode)
			throws X2JException {
		XPath xPath = xpathFactory.newXPath();
		try {
			XPathExpression expr = xPath.compile(xpath);
			return expr.evaluate(element, type);
		} catch (XPathExpressionException e) {
			throw new X2JException(errCode);
		}
	}

}
