package com.x2j.converter.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.w3c.dom.Document;

import com.x2j.converter.XMLToJSONConverter;
import com.x2j.converter.utils.X2JUtils;

public class XMLToJSONConverterTest {

	@Test
	public void testX2JConversionUsingSchemaInsideXML() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_1.xml");
		JSONObject json = new XMLToJSONConverter().convertToJson(doc);
		assertResults(json);
	}

	@Test
	public void testX2JDefaultConversion() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_1-1.xml");
		JSONObject actual = new XMLToJSONConverter().convertToJson(doc);
		JSONObject expected = X2JUtils.getJsonFromFile(
				new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_results_1-1.json"));
		assertTrue(expected.similar(actual));
	}

	@Test
	public void testX2JDefaultConversionWithEmptyJSONSchema() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_1-2.xml");
		JSONObject actual = new XMLToJSONConverter().convertToJson(doc);
		JSONObject expected = X2JUtils.getJsonFromFile(
				new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_results_1-2.json"));
		assertTrue(expected.similar(actual));
	}

	@Test
	public void testX2JConversionUsingSchemaAsString() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_2.xml");
		String schema = "{\n\"ShipmentIdentifier\": \"XPATH(/MyShipments/Shipment/@Shipment_Key)\",\n"
				+ "		\"Type\": \"XPATH(/MyShipments/Shipment/Lines/Line/Order/@Type)\",\n"
				+ "		\"OrderNo\": \"VALUE(/MyShipments/Shipment/Lines/Line/Order/@Order_Num)\",\n"
				+ "		\"Carrier\": \"XYZ-ABCD Logistics\",\n" + "		\"LineItems\" : [{\n"
				+ "			\"recurrent_path\": \"/MyShipments/Shipment/Lines/Line\",\n"
				+ "			\"Identifier\": \"RECUR_ELEM(/Item/@Id)\",\n"
				+ "			\"Quantity\": \"RECUR_ELEM(/Item/@Qty):INT\",\n"
				+ "			\"Description\": \"RECUR_ELEM(/Item/@Desc)\",\n"
				+ "			\"Status\": \"RECUR_ELEM(/Order/@Status)\",\n"
				+ "			\"Price\": \"RECUR_ELEM(/Item/@Cost):DBL\",\n" + "			\"Field1\": \"CONSTANT1\",\n"
				+ "			\"Field2\": 1000,\n" + "			\"Field3\": \"XPATH(/MyShipments/Shipment/@Number)\",\n"
				+ "			\"Field4\": \"VALUE(/MyShipments/Shipment/@Number):INT\"\n" + "		}],\n"
				+ "		\"Temp\": [\"VALUE(/MyShipments/Shipment/Organization)\", 1234, \"ABCD\","
				+ " \"XPATH(/MyShipments/Shipment/Lines/Line/Order/@Order_Num)\"],\n"
				+ "		\"OrderTempInfo\": \"XPATH(/MyShipments/Shipment/Lines/Line/Order)\"\n" + "	}";
		JSONObject json = new XMLToJSONConverter().convertToJson(doc, schema);
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingSchemaAsJSONObj() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_3.xml");
		JSONObject schema = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_schema_3.json"));
		JSONObject json = new XMLToJSONConverter().convertToJson(doc, schema);
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingSchemaInsideXMLFile() throws Exception {
		JSONObject json = new XMLToJSONConverter()
				.convertToJson(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_4.xml"));
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingSchemaAsStringAndInputXMLAsFile() throws Exception {
		String schema = "{\n\"ShipmentIdentifier\": \"XPATH(/MyShipments/Shipment/@Shipment_Key)\",\n"
				+ "		\"Type\": \"XPATH(/MyShipments/Shipment/Lines/Line/Order/@Type)\",\n"
				+ "		\"OrderNo\": \"VALUE(/MyShipments/Shipment/Lines/Line/Order/@Order_Num)\",\n"
				+ "		\"Carrier\": \"XYZ-ABCD Logistics\",\n" + "		\"LineItems\" : [{\n"
				+ "			\"recurrent_path\": \"/MyShipments/Shipment/Lines/Line\",\n"
				+ "			\"Identifier\": \"RECUR_ELEM(/Item/@Id)\",\n"
				+ "			\"Quantity\": \"RECUR_ELEM(/Item/@Qty):INT\",\n"
				+ "			\"Description\": \"RECUR_ELEM(/Item/@Desc)\",\n"
				+ "			\"Status\": \"RECUR_ELEM(/Order/@Status)\",\n"
				+ "			\"Price\": \"RECUR_ELEM(/Item/@Cost):DBL\",\n" + "			\"Field1\": \"CONSTANT1\",\n"
				+ "			\"Field2\": 1000,\n" + "			\"Field3\": \"XPATH(/MyShipments/Shipment/@Number)\",\n"
				+ "			\"Field4\": \"VALUE(/MyShipments/Shipment/@Number):INT\"\n" + "		}],\n"
				+ "		\"Temp\": [\"VALUE(/MyShipments/Shipment/Organization)\", 1234, \"ABCD\","
				+ " \"XPATH(/MyShipments/Shipment/Lines/Line/Order/@Order_Num)\"],\n"
				+ "		\"OrderTempInfo\": \"XPATH(/MyShipments/Shipment/Lines/Line/Order)\"\n" + "	}";
		JSONObject json = new XMLToJSONConverter().convertToJson(
				new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_5.xml"), schema);
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingSchemaAsJSONObjAndInputXMLAsFile() throws Exception {
		JSONObject schema = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_schema_6.json"));
		JSONObject json = new XMLToJSONConverter().convertToJson(
				new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_6.xml"), schema);
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingSchemaAndInputXMLAsSeparateFiles() throws Exception {
		JSONObject json = new XMLToJSONConverter().convertToJson(
				new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_7.xml"),
				new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_schema_7.json"));
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingInputXMLAsString() throws Exception {
		Document d = X2JUtils
				.getXmlFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_8.xml"));
		JSONObject json = new XMLToJSONConverter().convertToJson(X2JUtils.getString(d));
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingInputXMLAsStringAndJSONSchemaAsString() throws Exception {
		Document d = X2JUtils
				.getXmlFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_9.xml"));
		JSONObject schema = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_schema_9.json"));
		JSONObject json = new XMLToJSONConverter().convertToJson(X2JUtils.getString(d), schema.toString());
		assertResults(json);
	}

	@Test
	public void testX2JConversionUsingInputXMLAsStringAndJSONSchemaAsJSONObject() throws Exception {
		Document d = X2JUtils
				.getXmlFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_input_10.xml"));
		JSONObject schema = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_conv_test_schema_10.json"));
		JSONObject json = new XMLToJSONConverter().convertToJson(X2JUtils.getString(d), schema);
		assertResults(json);
	}

	private void assertResults(JSONObject json) {
		assertFalse(X2JUtils.isVoid(json));
		assertEquals(json.get("ShipmentIdentifier"), "1202103301135561781564861");
		assertEquals(json.get("Type"), "COD_SHP");
		assertEquals(json.get("OrderNo"), "22268086");
		assertEquals(json.get("Carrier"), "XYZ-ABCD Logistics");
		assertEquals(((JSONArray) json.get("LineItems")).length(), 3);
		assertEquals(((JSONObject) ((JSONArray) json.get("LineItems")).get(0)).get("Identifier"), "I1");
		assertThat((JSONArray) json.get("Temp")).contains("Some Org Name", "ABCD", "22268086");
	}

}
