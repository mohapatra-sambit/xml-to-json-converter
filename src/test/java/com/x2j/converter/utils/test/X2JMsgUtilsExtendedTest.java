package com.x2j.converter.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.x2j.converter.utils.X2JErrorCodes;
import com.x2j.converter.utils.X2JMsgUtils;

public class X2JMsgUtilsExtendedTest {

	private static File file;

	private static X2JMsgUtils utils = null;

	@BeforeClass
	public static void init() throws IOException {
		String fileName = "x2j-msgs.properties";
//		fileName = System.getProperty("user.dir") + "/x2j-msgs.properties";
//		fileName = System.getProperty("user.home") + "/x2j-msgs.properties";
		file = new File(fileName);
		Properties p = new Properties();
		p.setProperty("X2J_ERR_003", "Extended error message 1");
		p.setProperty("X2J_ERR_005", "Extended error message 2");
		p.setProperty("X2J_ERR_007", "Extended error message 3");
		FileOutputStream outStream = new FileOutputStream(file);
		p.store(outStream, "");
		outStream.close();
		utils = X2JMsgUtils.getInstance();
	}

	@Test
	public void testErrorMessageCount() {
		Properties msgs = utils.getMsgs();
		assertEquals(msgs.size(), 12);
	}

	@Test
	public void testMessage000() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_000.name());
		assertTrue(message.equals("Fatal error. Please rebuild the jar or download the latest one from Github."));
	}

	@Test
	public void testMessage001() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_001.name());
		assertTrue(message.equals("Error while reading XML from file."));
	}

	@Test
	public void testMessage002() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_002.name());
		assertTrue(message.equals("Error while reading JSON from file/string."));
	}

	@Test
	public void testMessage003() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_003.name());
		assertTrue(message.equals("Extended error message 1"));
	}

	@Test
	public void testMessage004() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_004.name());
		assertTrue(message.equals("Error while reading XML data using XPaths."));
	}

	@Test
	public void testMessage005() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_005.name());
		assertTrue(message.equals("Extended error message 2")
				|| message.equals("Error while reading XML attribute value using XPaths."));
	}

	@Test
	public void testMessage006() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_006.name());
		assertTrue(message.equals("Error while reading XML element using XPaths."));
	}

	@Test
	public void testMessage007() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_007.name());
		assertTrue(message.equals("Extended error message 3")
				|| message.equals("Error while converting XML element/document to String."));
	}

	@Test
	public void testMessage008() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_008.name());
		assertTrue(message.equals("Error while writing JSON to file."));
	}

	@Test
	public void testMessage009() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_009.name());
		assertTrue(message.equals("Error while parsing XML from String."));
	}

	@Test
	public void testMessage010() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_010.name());
		assertTrue(message.equals("Input XML file not found."));
	}

	@Test
	public void testMessage011() {
		String message = utils.getMessage(X2JErrorCodes.X2J_ERR_011.name());
		assertTrue(message.equals("Error during string operation."));
	}

	@AfterClass
	public static void destroy() {
		File extnFileOne = new File("x2j-msgs.properties");
		File extnFileTwo = new File(System.getProperty("user.dir"), "x2j-msgs.properties");
		File extnFileThree = new File(System.getProperty("user.home"), "x2j-msgs.properties");
		if (extnFileOne.exists()) {
			extnFileOne.delete();
		}
		if (extnFileTwo.exists()) {
			extnFileTwo.delete();
		}
		if (extnFileThree.exists()) {
			extnFileThree.delete();
		}
	}

}
