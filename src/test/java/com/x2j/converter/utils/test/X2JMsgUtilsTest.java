package com.x2j.converter.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

import com.x2j.converter.utils.X2JErrorCodes;
import com.x2j.converter.utils.X2JMsgUtils;

public class X2JMsgUtilsTest {

	@Test
	public void testErrorMessageCount() {
		Properties msgs = X2JMsgUtils.getInstance().getMsgs();
		assertEquals(msgs.size(), 9);
	}

	@Test
	public void testMessage001() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_001.name());
		assertTrue(message.equals("Error while reading XML from file."));
	}

	@Test
	public void testMessage002() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_002.name());
		assertTrue(message.equals("Error while reading JSON from file/string."));
	}

	@Test
	public void testMessage003() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_003.name());
		assertTrue(message.equals("Error while parsing JSON from file/string."));
	}

	@Test
	public void testMessage004() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_004.name());
		assertTrue(message.equals("Error while reading XML data using XPaths."));
	}

	@Test
	public void testMessage005() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_005.name());
		assertTrue(message.equals("Error while reading XML attribute value using XPaths."));
	}

	@Test
	public void testMessage006() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_006.name());
		assertTrue(message.equals("Error while reading XML element using XPaths."));
	}

	@Test
	public void testMessage007() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_007.name());
		assertTrue(message.equals("Error while converting XML element/document to String."));
	}

	@Test
	public void testMessage008() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_008.name());
		assertTrue(message.equals("Error while writing JSON to file."));
	}

	@Test
	public void testMessage009() {
		String message = X2JMsgUtils.getInstance().getMessage(X2JErrorCodes.X2J_ERR_009.name());
		assertTrue(message.equals("Error while parsing XML from String."));
	}

}
