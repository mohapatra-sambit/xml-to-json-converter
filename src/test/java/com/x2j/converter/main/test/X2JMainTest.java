package com.x2j.converter.main.test;

import static com.x2j.converter.utils.X2JConstants.INPUT_JSON_SCHEMA_FILE;
import static com.x2j.converter.utils.X2JConstants.INPUT_XML_FILE;
import static com.x2j.converter.utils.X2JConstants.OUTPUT_JSON_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.main.X2JMain;
import com.x2j.converter.utils.X2JExecMode;
import com.x2j.converter.utils.X2JUtils;

public class X2JMainTest {

	private ByteArrayOutputStream out = new ByteArrayOutputStream();

	@BeforeClass
	public static void init() {
		X2JMain.setMode(X2JExecMode.TEST);
	}

	@Test
	public void testX2JMainMissingArgs() {
		X2JException expected = assertThrows(X2JException.class, () -> X2JMain.main(null));
		assertEquals(expected.getErrorMessage(), "Input XML file not found.");
	}

	@Test
	public void testX2JMainInvalidArgs() {
		System.setProperty(INPUT_XML_FILE, "ABCD");
		X2JException expected = assertThrows(X2JException.class, () -> X2JMain.main(null));
		System.clearProperty(INPUT_XML_FILE);
		assertEquals(expected.getErrorMessage(), "Error while reading XML from file.");
	}

	@Test
	public void testX2JMainValidInputXMLAndDefaultSchemaAndNoOutputFile() throws X2JException {
		System.setProperty(INPUT_XML_FILE, "src/test/java/com/x2j/converter/common/test/x2j_main_test_input_1.xml");
		setOutAndErr();
		X2JMain.main(null);
		System.clearProperty(INPUT_XML_FILE);
		JSONObject output = new JSONObject(out.toString());
		JSONObject expected = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_main_test_results_1.json"));
		assertTrue(output.similar(expected));
	}

	@Test
	public void testX2JMainValidInputXMLAndValidSchemaAndNoOutputFile() throws X2JException {
		System.setProperty(INPUT_XML_FILE, "src/test/java/com/x2j/converter/common/test/x2j_main_test_input_2.xml");
		System.setProperty(INPUT_JSON_SCHEMA_FILE,
				"src/test/java/com/x2j/converter/common/test/x2j_main_test_schema_2.json");
		setOutAndErr();
		X2JMain.main(null);
		System.clearProperty(INPUT_XML_FILE);
		System.clearProperty(INPUT_JSON_SCHEMA_FILE);
		JSONObject output = new JSONObject(out.toString());
		JSONObject expected = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_main_test_results_2.json"));
		assertTrue(output.similar(expected));
	}

	@Test
	public void testX2JMainValidInputXMLAndValidSchemaAndValidOutputFile() throws X2JException {
		File outFile = new File("x2j_main_test_output_1.json");
		System.setProperty(INPUT_XML_FILE, "src/test/java/com/x2j/converter/common/test/x2j_main_test_input_3.xml");
		System.setProperty(INPUT_JSON_SCHEMA_FILE,
				"src/test/java/com/x2j/converter/common/test/x2j_main_test_schema_3.json");
		System.setProperty(OUTPUT_JSON_FILE, outFile.getName());
		setOutAndErr();
		X2JMain.main(null);
		System.clearProperty(INPUT_XML_FILE);
		System.clearProperty(INPUT_JSON_SCHEMA_FILE);
		System.clearProperty(OUTPUT_JSON_FILE);
		JSONObject expected = X2JUtils
				.getJsonFromFile(new File("src/test/java/com/x2j/converter/common/test/x2j_main_test_results_3.json"));
		JSONObject actual = X2JUtils.getJsonFromFile(outFile);
		assertTrue(outFile.exists());
		outFile.delete();
		assertTrue(actual.similar(expected));
	}

	@Test
	public void testX2JMainConversionException() throws Exception {
		X2JMain.setMode(X2JExecMode.NORMAL);
		System.setProperty(INPUT_XML_FILE, "src/test/java/com/x2j/converter/common/test/x2j_main_test_input_4.xml");
		System.setProperty(INPUT_JSON_SCHEMA_FILE,
				"src/test/java/com/x2j/converter/common/test/x2j_main_test_schema_4.json");
		setOutAndErr();
		X2JMain.main(null);
		System.clearProperty(INPUT_XML_FILE);
		System.clearProperty(INPUT_JSON_SCHEMA_FILE);
		String output = out.toString();
		assertTrue(output.contains("Error while reading XML from file."));
	}

	private void setOutAndErr() {
		System.setOut(new PrintStream(out));
		System.setErr(new PrintStream(out));
	}

}
