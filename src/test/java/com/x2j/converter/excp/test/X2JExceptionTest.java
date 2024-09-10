package com.x2j.converter.excp.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.utils.X2JErrorCodes;
import com.x2j.converter.utils.X2JMsgUtils;

public class X2JExceptionTest {

	@BeforeClass
	public static void init() throws IOException, X2JException {
		X2JMsgUtils.getInstance().reset();
	}

	@Test
	public void testErrorCode001() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_001);
		assertTrue(exception.getErrorMessage().equals("Error while reading XML from file."));
	}

	@Test
	public void testErrorCode002() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_002);
		assertTrue(exception.getErrorMessage().equals("Error while reading JSON from file/string."));
	}

	@Test
	public void testErrorCode003() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_003);
		assertTrue(exception.getErrorMessage().equals("Error while parsing JSON from file/string."));
	}

	@Test
	public void testErrorCode004() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_004);
		assertTrue(exception.getErrorMessage().equals("Error while reading XML data using XPaths."));
	}

	@Test
	public void testErrorCode005() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_005);
		assertTrue(exception.getErrorMessage().equals("Error while reading XML attribute value using XPaths."));
	}

	@Test
	public void testErrorCode006() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_006);
		assertTrue(exception.getErrorMessage().equals("Error while reading XML element using XPaths."));
	}

	@Test
	public void testErrorCode007() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_007);
		assertTrue(exception.getErrorMessage().equals("Error while converting XML element/document to String."));
	}

	@Test
	public void testErrorCode008() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_008);
		assertTrue(exception.getErrorMessage().equals("Error while writing JSON to file."));
	}

	@Test
	public void testErrorCode009() {
		X2JException exception = new X2JException(X2JErrorCodes.X2J_ERR_009);
		assertTrue(exception.getErrorMessage().equals("Error while parsing XML from String."));
	}

}
