package com.x2j.converter.main;

import static com.x2j.converter.utils.X2JConstants.INPUT_JSON_SCHEMA_FILE;
import static com.x2j.converter.utils.X2JConstants.INPUT_XML_FILE;
import static com.x2j.converter.utils.X2JConstants.OUTPUT_JSON_FILE;

import java.io.File;

import org.json.JSONObject;

import com.x2j.converter.XMLToJSONConverter;
import com.x2j.converter.excp.X2JException;
import com.x2j.converter.utils.X2JErrorCodes;
import com.x2j.converter.utils.X2JExecMode;
import com.x2j.converter.utils.X2JUtils;

/**
 * This is the main class that will run when the x2j jar file is executed from
 * command line.<br>
 * The only mandatory argument that is required is the input XML file path.<br>
 * If the JSON schema file path is passed as an argument, then that will be used
 * for conversion, otherwise the default conversion logic will be applied.<br>
 * For more information on JSON schema definition, please visit: <a href=
 * "https://github.com/mohapatra-sambit/xml-to-json-converter">XML-To-JSON
 * Converter</a><br>
 * Additionally, if the output file path is passed as an argument, then the
 * output converted JSON will be saved in the specified file, otherwise the
 * converted JSON will be printed in the console.
 */
public class X2JMain {

	private static String xmlFile;

	private static String jsonFile;

	private static String outFile;

	private static X2JExecMode mode = X2JExecMode.NORMAL;

	/**
	 * The main method. <br>
	 * <b>NORMAL Mode</b>:<br>
	 * Validates, if the input XML file is passed as an argument.<br>
	 * if yes, checks whether JSON schema is passes as an argument as well.<br>
	 * if yes, invokes the {@link XMLToJSONConverter#convertToJson(File, File)}
	 * method.<br>
	 * otherwise, invokes the {@link XMLToJSONConverter#convertToJson(File)}
	 * method.<br>
	 * Additionally, checks if output JSON file path is passed as an argument.<br>
	 * if yes, writes the converted JSON to the file.<br>
	 * otherwise, prints the converted JSON in the console. otherwise, prints the
	 * usage and exits.<br>
	 * <br>
	 * <b>Usage</b>: java -DInputXml=PATH_OF_INPUT_XML_FILE
	 * -DSchemaJson=PATH_OF_SCHEMA_JSON_FILE -DOutputJson=PATH_OF_OUTPUT_JSON_FILE
	 * -jar XML2JSON-0.0.1-SNAPSHOT-jar-with-dependencies.jar<br>
	 * <br>
	 * <b>TEST Mode</b>:<br>
	 * In case of validation failure, this will throw X2JException instead of
	 * stopping the application with an error message.
	 * 
	 * @param args the command-line arguments
	 * @throws X2JException X2JException
	 */
	public static void main(String[] args) throws X2JException {
		try {
			validateArguments();
			XMLToJSONConverter converter = new XMLToJSONConverter();
			JSONObject convertedJson = new JSONObject();
			if (X2JUtils.isVoid(jsonFile)) {
				convertedJson = converter.convertToJson(new File(xmlFile));
			} else {
				convertedJson = converter.convertToJson(new File(xmlFile), new File(jsonFile));
			}
			if (X2JUtils.isVoid(outFile)) {
				System.out.println(convertedJson.toString(4));
			} else {
				X2JUtils.writeJsonToFile(convertedJson, new File(outFile));
				System.out.println("Conversion Completed!");
			}
		} catch (X2JException e) {
			if (mode == X2JExecMode.NORMAL) {
				System.out.println(e.getErrorMessage());
				e.printStackTrace();
			} else {
				throw e;
			}
		}
	}

	/**
	 * Sets the execution mode for the X2J application (X2JExecMode).
	 *
	 * @param mode the new execution mode
	 */
	public static void setMode(X2JExecMode mode) {
		X2JMain.mode = mode;
	}

	private static void validateArguments() throws X2JException {
		xmlFile = System.getProperty(INPUT_XML_FILE);
		if (X2JUtils.isVoid(xmlFile)) {
			exitWithUsage();
		}
		jsonFile = System.getProperty(INPUT_JSON_SCHEMA_FILE);
		outFile = System.getProperty(OUTPUT_JSON_FILE);
	}

	private static void exitWithUsage() throws X2JException {
		System.out.println("Usage::");
		System.out.println("java -DInputXml=PATH_OF_INPUT_XML_FILE -DSchemaJson=PATH_OF_SCHEMA_JSON_FILE"
				+ " -DOutputJson=PATH_OF_OUTPUT_JSON_FILE -jar XML2JSON-0.0.1-SNAPSHOT-jar-with-dependencies.jar");
		System.out.println("Optional Arguments:");
		System.out.println(
				"1. SchemaJson: If the schema json file is not specified, then default conversion will happen.");
		System.out.println(
				"2. OutputJson: If the output json file is not specified, then the output is printed to the console.");
		System.out.println(
				"For more information on this, please visit: https://github.com/mohapatra-sambit/xml-to-json-converter");
		switch (mode) {
		case NORMAL:
			System.exit(0);
		case TEST:
			throw new X2JException(X2JErrorCodes.X2J_ERR_010);
		}
	}

}
