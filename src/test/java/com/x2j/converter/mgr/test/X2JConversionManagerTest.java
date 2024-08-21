package com.x2j.converter.mgr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.w3c.dom.Document;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.X2JConversionManager;
import com.x2j.converter.utils.X2JUtils;

public class X2JConversionManagerTest {

	@Test
	public void testXMLToJSONConversion() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/conv_mgr_test_input.xml");
		JSONObject schema = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/conv_mgr_test_schema.json"));
		JSONObject json = X2JConversionManager.getInstance().processJSONObject(schema, doc.getDocumentElement());
		assertEquals(json.get("ShipmentIdentifier"), "1202103301135561781564861");
		assertEquals(json.get("Type"), "COD_SHP");
		assertEquals(json.get("OrderNo"), "22268086");
		assertEquals(json.get("Carrier"), "XYZ-ABCD Logistics");
		assertEquals(((JSONArray) json.get("LineItems")).length(), 3);
		assertEquals(((JSONObject) ((JSONArray) json.get("LineItems")).get(0)).get("Identifier"), "I1");
		assertTrue(((JSONArray) json.get("Temp"))
				.similar(new JSONArray(Arrays.asList("Some Org Name", 1234, "ABCD", "22268086"))));
	}

	@Test
	public void testXMLToJSONConversionException() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/conv_mgr_test_input.xml");
		JSONObject schema = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/conv_mgr_test_schema_1.json"));
		X2JException expected = assertThrows(X2JException.class,
				() -> X2JConversionManager.getInstance().processJSONObject(schema, doc.getDocumentElement()));
		assertEquals(expected.getErrorMessage(), "Error while reading XML attribute value using XPaths.");
	}

	@Test
	public void testXMLToJSONDefaultConversion() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/conv_mgr_test_input.xml");
		JSONObject json = X2JConversionManager.getInstance().processJSONObject(doc.getDocumentElement());
		assertFalse(X2JUtils.isVoid(json));
		Object myShipmentsObj = json.get("MyShipments");
		assertTrue(myShipmentsObj instanceof JSONObject);
		JSONObject myShipments = (JSONObject) myShipmentsObj;
		JSONObject shipment = (JSONObject) myShipments.get("Shipment");
		assertEquals(shipment.get("Number"), 528630);
		assertEquals(shipment.get("Organization"), "Some Org Name");
		JSONObject lines = (JSONObject) shipment.get("Lines");
		Object line = lines.get("Line");
		assertTrue(line instanceof JSONArray);
	}

	@Test
	public void testXMLToJSONDefaultConversionException() {
		X2JException expected = assertThrows(X2JException.class,
				() -> X2JConversionManager.getInstance().processJSONObject(null));
		assertEquals(expected.getErrorMessage(), "Error while converting XML element/document to String.");
	}

}
