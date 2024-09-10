package com.x2j.converter.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.utils.X2JUtils;

public class X2JUtilsTest {

	@BeforeClass
	public static void init() throws IOException, SAXException, ParserConfigurationException, TransformerException {
		createTestXmlFile();
		createTestJsonFile();
	}

	private static void createTestJsonFile() throws IOException {
		JSONObject json = new JSONObject(
				"{\"id\": \"0001\",\"type\": \"donut\",\"name\": \"Cake\",\"ppu\": 0.55,\"batters\":{"
						+ "\"batter\":[{ \"id\": \"1001\", \"type\": \"Regular\" },{ \"id\": \"1002\", \"type\": \"Chocolate\" },{ \"id\": \"1003\","
						+ " \"type\": \"Blueberry\" },{ \"id\": \"1004\", \"type\": \"Devil's Food\" }]},\"topping\":[{ \"id\": \"5001\", \"type\":"
						+ " \"None\" },{ \"id\": \"5002\", \"type\": \"Glazed\" },{ \"id\": \"5005\", \"type\": \"Sugar\" },{ \"id\": \"5007\","
						+ " \"type\": \"Powdered Sugar\" },{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },{ \"id\": \"5003\","
						+ " \"type\": \"Chocolate\" },{ \"id\": \"5004\", \"type\": \"Maple\" }]}");
		FileWriter file = new FileWriter(new File(System.getProperty("user.home"), "x2j-sample-test.json"));
		file.write(json.toString());
		file.flush();
		file.close();
	}

	private static void createTestXmlFile()
			throws SAXException, IOException, ParserConfigurationException, TransformerException {
		String xml = "<catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developer's Guide</title>"
				+ "<genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description lang=\"eng\">An in-depth "
				+ "look at creating applications with XML.</description></book><book id=\"bk102\"><author>Ralls, Kim</author>"
				+ "<title>Midnight Rain</title><genre>Fantasy</genre><price>5.95</price><publish_date>2000-12-16</publish_date>"
				+ "<description>A former architect battles corporate zombies, an evil sorceress, and her own childhood to become "
				+ "queen of the world.</description></book><book id=\"bk103\"><author>Corets, Eva</author><title>Maeve Ascendant"
				+ "</title><genre>Fantasy</genre><price>5.95</price><publish_date>2000-11-17</publish_date><description>After the "
				+ "collapse of a nanotechnology society in England, the young survivors lay the foundation for a new society."
				+ "</description></book></catalog>";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		File xmlFile = new File(System.getProperty("user.home"), "x2j-sample-test.xml");
		xmlFile.createNewFile();
		FileOutputStream output = new FileOutputStream(xmlFile);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(output);
		transformer.transform(source, result);
	}

	@Test
	public void testNullObject() {
		assertTrue(X2JUtils.isVoid(null));
	}

	@Test
	public void testNullString() {
		String s = null;
		assertTrue(X2JUtils.isVoid(s));
	}

	@Test
	public void testEmptyString() {
		String s = "";
		assertTrue(X2JUtils.isVoid(s));
	}

	@Test
	public void testNonEmptyString() {
		String s = "abcd";
		assertFalse(X2JUtils.isVoid(s));
	}

	@Test
	public void testNullJSONObject() {
		JSONObject o = null;
		assertTrue(X2JUtils.isVoid(o));
	}

	@Test
	public void testNonNullJSONObject() {
		JSONObject o = new JSONObject();
		assertFalse(X2JUtils.isVoid(o));
	}

	@Test
	public void testNullJSONArray() {
		JSONArray o = null;
		assertTrue(X2JUtils.isVoid(o));
	}

	@Test
	public void testNonNullJSONArray() {
		JSONArray o = new JSONArray();
		assertFalse(X2JUtils.isVoid(o));
	}

	@Test
	public void testNullXMLDocument() {
		Document o = null;
		assertTrue(X2JUtils.isVoid(o));
	}

	@Test
	public void testNonNullXMLDocument() throws ParserConfigurationException {
		Document o = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		assertFalse(X2JUtils.isVoid(o));
	}

	@Test
	public void testReadXMLFromFile() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		assertFalse(X2JUtils.isVoid(d));
		assertTrue(((Element) d.getDocumentElement().getElementsByTagName("book").item(0)).getAttribute("id")
				.startsWith("bk10"));
	}

	@Test
	public void testReadXMLFromFileException() {
		File file = new File("src/test/java/com/x2j/converter/common/test/utils_test_input_1.xml");
		file.setReadable(false);
		X2JException expected = assertThrows(X2JException.class, () -> X2JUtils.getXmlFromFile(file));
		file.setReadable(true);
		assertEquals(expected.getErrorMessage(), "Error while reading XML from file.");
	}

	@Test
	public void testReadJSONFromFile() throws X2JException {
		JSONObject json = X2JUtils.getJsonFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.json"));
		assertFalse(X2JUtils.isVoid(json));
		assertEquals(json.get("name"), "Cake");
		assertEquals(((JSONArray) json.get("topping")).length(), 7);
	}

	@Test
	public void testReadJSONFromFileException() {
		X2JException expected = assertThrows(X2JException.class, () -> X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/utils_test_input_2.json")));
		assertEquals(expected.getErrorMessage(), "Error while reading JSON from file/string.");
	}

	@Test
	public void testReadJSONFromString() throws X2JException {
		JSONObject json = X2JUtils.getJsonFromString(
				"{\"id\": \"0001\",\"type\": \"donut\",\"name\": \"Cake\",\"ppu\": 0.55,\"batters\":{"
						+ "\"batter\":[{ \"id\": \"1001\", \"type\": \"Regular\" },{ \"id\": \"1002\", \"type\": \"Chocolate\" },{ \"id\": \"1003\","
						+ " \"type\": \"Blueberry\" },{ \"id\": \"1004\", \"type\": \"Devil's Food\" }]},\"topping\":[{ \"id\": \"5001\", \"type\":"
						+ " \"None\" },{ \"id\": \"5002\", \"type\": \"Glazed\" },{ \"id\": \"5005\", \"type\": \"Sugar\" },{ \"id\": \"5007\","
						+ " \"type\": \"Powdered Sugar\" },{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },{ \"id\": \"5003\","
						+ " \"type\": \"Chocolate\" },{ \"id\": \"5004\", \"type\": \"Maple\" }]}");
		assertFalse(X2JUtils.isVoid(json));
		assertEquals(json.get("name"), "Cake");
		assertEquals(((JSONArray) json.get("topping")).length(), 7);
	}

	@Test
	public void testReadJSONFromStringException() {
		X2JException expected = assertThrows(X2JException.class, () -> X2JUtils.getJsonFromString(
				"{\"id\": \"0001\",\"type\": \"donut\",\"name\": \"Cake\",\"ppu\": 0.55,\"batters\":{"
						+ "\"batter\":[{ \"id\": \"1001\", \"type\": \"Regular\" },{ \"id\": \"1002\", \"type\": \"Chocolate\" },{ \"id\": \"1003\","
						+ " \"type\": \"Blueberry\" },{ \"id\": \"1004\", \"type\": \"Devil's Food\" }]},\"topping\":[{ \"id\": \"5001\", \"type\":"
						+ " \"None\" },{ \"id\": \"5002\", \"type\": \"Glazed\" },{ \"id\": \"5005\", \"type\": \"Sugar\" },{ \"id\": \"5007\","
						+ " \"type\": \"Powdered Sugar\" },{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },{ \"id\": \"5003\","
						+ " \"type\": \"Chocolate },{ \"id\": \"5004\", \"type\": \"Maple\" }]}"));
		assertEquals(expected.getErrorMessage(), "Error while parsing JSON from file/string.");
	}

	@Test
	public void testNodeListUsingXPath() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		NodeList nodes = X2JUtils.getNodesFromXPath(d.getDocumentElement(), "/catalog/book");
		assertFalse(X2JUtils.isVoid(nodes));
		assertEquals(nodes.getLength(), 3);
	}

	@Test
	public void testNodeListUsingXPathException() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		X2JException expected = assertThrows(X2JException.class,
				() -> X2JUtils.getNodesFromXPath(d.getDocumentElement(), "/catalog/book["));
		assertEquals(expected.getErrorMessage(), "Error while reading XML data using XPaths.");
	}

	@Test
	public void testAttributeUsingXPath() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		String attr = X2JUtils.getXpathAttribute(d.getDocumentElement(),
				"/catalog/book[@id='bk101']/description/@lang");
		assertFalse(X2JUtils.isVoid(attr));
		assertEquals(attr, "eng");
	}

	@Test
	public void testAttributeUsingXPathException() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		X2JException expected = assertThrows(X2JException.class,
				() -> X2JUtils.getXpathAttribute(d.getDocumentElement(), "/catalog/book[@id='bk101']/description/@"));
		assertEquals(expected.getErrorMessage(), "Error while reading XML attribute value using XPaths.");
	}

	@Test
	public void testElementUsingXPath() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		Element elem = X2JUtils.getXpathElement(d.getDocumentElement(), "/catalog/book[@id='bk102']/author");
		assertFalse(X2JUtils.isVoid(elem));
		assertEquals(elem.getTextContent(), "Ralls, Kim");
	}

	@Test
	public void testElementUsingXPathException() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		X2JException expected = assertThrows(X2JException.class,
				() -> X2JUtils.getXpathElement(d.getDocumentElement(), "/catalog/book[@id='bk101']/"));
		assertEquals(expected.getErrorMessage(), "Error while reading XML element using XPaths.");
	}

	@Test
	public void testDocumentFromString() throws X2JException {
		String xml = "<catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developer's Guide</title>"
				+ "<genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description lang=\"eng\">An in-depth "
				+ "look at creating applications with XML.</description></book><book id=\"bk102\"><author>Ralls, Kim</author>"
				+ "<title>Midnight Rain</title><genre>Fantasy</genre><price>5.95</price><publish_date>2000-12-16</publish_date>"
				+ "<description>A former architect battles corporate zombies, an evil sorceress, and her own childhood to become "
				+ "queen of the world.</description></book><book id=\"bk103\"><author>Corets, Eva</author><title>Maeve Ascendant"
				+ "</title><genre>Fantasy</genre><price>5.95</price><publish_date>2000-11-17</publish_date><description>After the "
				+ "collapse of a nanotechnology society in England, the young survivors lay the foundation for a new society."
				+ "</description></book></catalog>";
		Document d = X2JUtils.getXmlFromString(xml);
		assertFalse(X2JUtils.isVoid(d));
		assertTrue(d.getDocumentElement().getElementsByTagName("author").item(0).getFirstChild().getNodeValue()
				.equals("Gambardella, Matthew"));
	}

	@Test
	public void testDocumentFromStringException() {
		String xml = "<catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developer's Guide</title>"
				+ "<genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description lang=\"eng\">An in-depth "
				+ "look at creating applications with XML.</description></book><book id=\"bk102\"><author>Ralls, Kim</author>"
				+ "<title>Midnight Rain</title><genre>Fantasy</genre><price>5.95</price><publish_date>2000-12-16</publish_date>"
				+ "<description>A former architect battles corporate zombies, an evil sorceress, and her own childhood to become "
				+ "queen of the world.</description></book><book id=\"bk103\"><author>Corets, Eva</author><title>Maeve Ascendant"
				+ "</title><genre>Fantasy</genre><price>5.95</price><publish_date>2000-11-17</publish_date><description>After the "
				+ "collapse of a nanotechnology society in England, the young survivors lay the foundation for a new society."
				+ "</description></book></catalog";
		X2JException expected = assertThrows(X2JException.class, () -> X2JUtils.getXmlFromString(xml));
		assertEquals(expected.getErrorMessage(), "Error while parsing XML from String.");
	}

	@Test
	public void testStringFromDocument() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		String xml = X2JUtils.getString(d);
		assertFalse(X2JUtils.isVoid(xml));
		assertTrue(xml.contains("<description>A former architect battles corporate zombies, an evil sorceress,"
				+ " and her own childhood to become queen of the world.</description>"));
	}

	@Test
	public void testStringFromDocumentException() {
		Document d = null;
		X2JException expected = assertThrows(X2JException.class, () -> X2JUtils.getString(d));
		assertEquals(expected.getErrorMessage(), "Error while converting XML element/document to String.");
	}

	@Test
	public void testElementFromString() throws X2JException {
		Document d = X2JUtils.getXmlFromFile(new File(System.getProperty("user.home"), "x2j-sample-test.xml"));
		String xml = X2JUtils.getString(d.getDocumentElement());
		assertFalse(X2JUtils.isVoid(xml));
		assertTrue(xml.contains("<description>After the collapse of a nanotechnology society in England,"
				+ " the young survivors lay the foundation for a new society.</description>"));
	}

	@Test
	public void testElementFromStringException() {
		Element e = null;
		X2JException expected = assertThrows(X2JException.class, () -> X2JUtils.getString(e));
		assertEquals(expected.getErrorMessage(), "Error while converting XML element/document to String.");
	}

	@Test
	public void testWriteJSONToFile() throws X2JException {
		JSONObject json = X2JUtils.getJsonFromString(
				"{\"id\": \"0001\",\"type\": \"donut\",\"name\": \"Cake\",\"ppu\": 0.55,\"batters\":{"
						+ "\"batter\":[{ \"id\": \"1001\", \"type\": \"Regular\" },{ \"id\": \"1002\", \"type\": \"Chocolate\" },{ \"id\": \"1003\","
						+ " \"type\": \"Blueberry\" },{ \"id\": \"1004\", \"type\": \"Devil's Food\" }]},\"topping\":[{ \"id\": \"5001\", \"type\":"
						+ " \"None\" },{ \"id\": \"5002\", \"type\": \"Glazed\" },{ \"id\": \"5005\", \"type\": \"Sugar\" },{ \"id\": \"5007\","
						+ " \"type\": \"Powdered Sugar\" },{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },{ \"id\": \"5003\","
						+ " \"type\": \"Chocolate\" },{ \"id\": \"5004\", \"type\": \"Maple\" }]}");
		File file = new File(System.getProperty("user.home"), "x2j-sample-test-27.json");
		X2JUtils.writeJsonToFile(json, file);
		assertTrue(file.exists());
		JSONObject jsonFromFile = X2JUtils.getJsonFromFile(file);
		assertTrue(jsonFromFile.similar(json));
	}

	@Test
	public void testWriteJSONToFileException() throws X2JException, IOException {
		JSONObject json = X2JUtils.getJsonFromString(
				"{\"id\": \"0001\",\"type\": \"donut\",\"name\": \"Cake\",\"ppu\": 0.55,\"batters\":{"
						+ "\"batter\":[{ \"id\": \"1001\", \"type\": \"Regular\" },{ \"id\": \"1002\", \"type\": \"Chocolate\" },{ \"id\": \"1003\","
						+ " \"type\": \"Blueberry\" },{ \"id\": \"1004\", \"type\": \"Devil's Food\" }]},\"topping\":[{ \"id\": \"5001\", \"type\":"
						+ " \"None\" },{ \"id\": \"5002\", \"type\": \"Glazed\" },{ \"id\": \"5005\", \"type\": \"Sugar\" },{ \"id\": \"5007\","
						+ " \"type\": \"Powdered Sugar\" },{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },{ \"id\": \"5003\","
						+ " \"type\": \"Chocolate\" },{ \"id\": \"5004\", \"type\": \"Maple\" }]}");
		File file = new File(System.getProperty("user.home"), "x2j-sample-test-28.json");
		file.createNewFile();
		file.setReadOnly();
		file.setWritable(false);
		X2JException expected = assertThrows(X2JException.class, () -> X2JUtils.writeJsonToFile(json, file));
		assertEquals(expected.getErrorMessage(), "Error while writing JSON to file.");
	}

	@Test
	public void testBlankEncode() throws X2JException, IOException {
		assertEquals(X2JUtils.encodeText(""), "");
	}

	@Test
	public void testXPathValidation() {
		String xpath = "abcd";
		assertFalse(X2JUtils.isValidXPath(xpath));
	}

	@Test
	public void testXPathValidationOne() {
		String xpath = "/MyShipments/Shipment";
		assertTrue(X2JUtils.isValidXPath(xpath));
	}

	@Test
	public void testXPathValidationTwo() {
		String xpath = "/MyShipments/Shipment/@Shipment_Key";
		assertTrue(X2JUtils.isValidXPath(xpath));
	}

	@Test
	public void testXPathValidationThree() {
		String xpath = "";
		assertFalse(X2JUtils.isValidXPath(xpath));
	}

	@Test
	public void testXPathValidationFour() {
		String xpath = null;
		assertFalse(X2JUtils.isValidXPath(xpath));
	}

	@Test
	public void testXPathValidationFive() {
		String xpath = "/MyShipments/Shipment/#@Shipment_Key";
		assertFalse(X2JUtils.isValidXPath(xpath));
	}

	@AfterClass
	public static void destroy() {
		File xmlFile = new File(System.getProperty("user.home"), "x2j-sample-test.xml");
		if (xmlFile.exists()) {
			xmlFile.delete();
		}
		File jsonFile = new File(System.getProperty("user.home"), "x2j-sample-test.json");
		if (jsonFile.exists()) {
			jsonFile.delete();
		}
		jsonFile = new File(System.getProperty("user.home"), "x2j-sample-test-27.json");
		if (jsonFile.exists()) {
			jsonFile.delete();
		}
		jsonFile = new File(System.getProperty("user.home"), "x2j-sample-test-28.json");
		if (jsonFile.exists()) {
			jsonFile.delete();
		}
	}

}
