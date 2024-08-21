package com.x2j.converter;

import static com.x2j.converter.utils.X2JConstants.JSON_SCHEMA_ELEMENT;

import java.io.File;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.X2JConversionManager;
import com.x2j.converter.utils.X2JUtils;

/**
 * The class XMLToJSONConverter is the starting point.<br>
 * This exposes various methods for the XML to JSON conversion.<br>
 * It accepts the input XML and the schema JSON as String or as a File
 * object.<br>
 * Before initiating the conversion logic, this class tries to figure out the
 * JSON schema, if not explicitly passed as argument.<br>
 * If the JSON schema is not found, the default conversion logic will be
 * applied.
 */
public class XMLToJSONConverter {

	/**
	 * Convert XML to JSON. The schema should be part of the XML element.<br>
	 * If the schema definition is present as a child of the root element, under the
	 * tag <b>JSONSchema</b>, then that schema definition will be considered for
	 * conversion. <br>
	 * Otherwise, the default conversion logic will be applied.
	 *
	 * @param inDoc the input XML document
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(Document inDoc) throws X2JException {
		Element root = inDoc.getDocumentElement();
		String jsonSchema = determineJsonSchema(root);
		JSONObject json = convertToJson(inDoc, jsonSchema);
		return json;
	}

	/**
	 * Convert XML to JSON. The schema is explicitly passed as a String
	 * argument.<br>
	 * The default conversion logic will only get applied when the schema string is
	 * null or empty.
	 *
	 * @param inDoc      the input XML document
	 * @param jsonSchema the JSON schema object
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(Document inDoc, String jsonSchema) throws X2JException {
		JSONObject jsonSchemaObj = X2JUtils.getJsonFromString(jsonSchema);
		JSONObject json = convertToJson(inDoc, jsonSchemaObj);
		return json;
	}

	/**
	 * Convert XML to JSON. The schema is explicitly passed as a JSONObject
	 * argument.<br>
	 * The default conversion logic will only get applied when the schema is null.
	 *
	 * @param inDoc         the input XML document
	 * @param jsonSchemaObj the JSON schema object
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(Document inDoc, JSONObject jsonSchemaObj) throws X2JException {
		Element root = inDoc.getDocumentElement();
		JSONObject returnJSON = null;
		if (X2JUtils.isVoid(jsonSchemaObj)) {
			returnJSON = X2JConversionManager.getInstance().processJSONObject(root);
		} else {
			returnJSON = X2JConversionManager.getInstance().processJSONObject(jsonSchemaObj, root);
		}
		return returnJSON;
	}

	/**
	 * Convert XML to JSON. The schema should be part of the XML element.<br>
	 * The input XML is stored in a file and the corresponding File object is passed
	 * as an argument to this method.<br>
	 * If the schema definition is present as a child of the root element, under the
	 * tag <b>JSONSchema</b>, then that schema definition will be considered for
	 * conversion. <br>
	 * Otherwise, the default conversion logic will be applied.
	 *
	 * @param inputXmlFile the input XML file
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(File inputXmlFile) throws X2JException {
		return convertToJson(X2JUtils.getXmlFromFile(inputXmlFile));
	}

	/**
	 * Convert XML to JSON. The schema is explicitly passed as a String
	 * argument.<br>
	 * The input XML is stored in a file and the corresponding File object is passed
	 * as an argument to this method.<br>
	 * The default conversion logic will only get applied when the schema string is
	 * null or empty.
	 *
	 * @param inputXmlFile the input XML file
	 * @param jsonSchema   the JSON schema
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(File inputXmlFile, String jsonSchema) throws X2JException {
		return convertToJson(X2JUtils.getXmlFromFile(inputXmlFile), jsonSchema);
	}

	/**
	 * Convert XML to JSON. The schema is explicitly passed as a JSONObject
	 * argument.<br>
	 * The input XML is stored in a file and the corresponding File object is passed
	 * as an argument to this method.<br>
	 * The default conversion logic will only get applied when the schema is null.
	 *
	 * @param inputXmlFile  the input XML file
	 * @param jsonSchemaObj the JSON schema
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(File inputXmlFile, JSONObject jsonSchemaObj) throws X2JException {
		return convertToJson(X2JUtils.getXmlFromFile(inputXmlFile), jsonSchemaObj);
	}

	/**
	 * Convert XML to JSON. The schema should be part of the XML element.<br>
	 * The input XML is a String and is passed as an argument to this method.<br>
	 * If the schema definition is present as a child of the root element, under the
	 * tag <b>JSONSchema</b>, then that schema definition will be considered for
	 * conversion. <br>
	 * Otherwise, the default conversion logic will be applied.
	 *
	 * @param inputXml the input XML String
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(String inputXml) throws X2JException {
		return convertToJson(X2JUtils.getXmlFromString(inputXml));
	}

	/**
	 * Convert XML to JSON. The schema is explicitly passed as a String
	 * argument.<br>
	 * The input XML is a String as well which is passed as an argument to this
	 * method.<br>
	 * The default conversion logic will only get applied when the schema string is
	 * null or empty.
	 *
	 * @param inputXml   the input XML String
	 * @param jsonSchema the JSON schema
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(String inputXml, String jsonSchema) throws X2JException {
		return convertToJson(X2JUtils.getXmlFromString(inputXml), jsonSchema);
	}

	/**
	 * Convert XML to JSON. The schema is explicitly passed as a JSONObject
	 * argument.<br>
	 * The input XML is a String and is passed as an argument to this method.<br>
	 * The default conversion logic will only get applied when the schema is null.
	 *
	 * @param inputXml      the input XML String
	 * @param jsonSchemaObj the JSON schema
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(String inputXml, JSONObject jsonSchemaObj) throws X2JException {
		return convertToJson(X2JUtils.getXmlFromString(inputXml), jsonSchemaObj);
	}

	/**
	 * Convert XML to JSON. The schema is explicitly passed as a JSONObject
	 * argument.<br>
	 * The input XML and the schema JSON are stored in 2 separate files and the
	 * corresponding File objects are passed as arguments to this method.<br>
	 * The default conversion logic will only get applied when the schema is null.
	 *
	 * @param inputXmlFile   the input XML file
	 * @param jsonSchemaFile the JSON schema file
	 * @return the converted JSON object
	 * @throws X2JException if there is any error during the conversion process.
	 */
	public JSONObject convertToJson(File inputXmlFile, File jsonSchemaFile) throws X2JException {
		return convertToJson(X2JUtils.getXmlFromFile(inputXmlFile), X2JUtils.getJsonFromFile(jsonSchemaFile));
	}

	private String determineJsonSchema(Element root) {
		NodeList nodeList = root.getElementsByTagName(JSON_SCHEMA_ELEMENT);
		if (!X2JUtils.isVoid(nodeList) && nodeList.getLength() > 0) {
			String jsonSchema = nodeList.item(0).getTextContent();
			if (X2JUtils.isVoid(jsonSchema)) {
				return "";
			}
			return jsonSchema;
		} else {
			return "";
		}
	}

}
