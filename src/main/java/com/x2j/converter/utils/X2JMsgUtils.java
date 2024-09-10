package com.x2j.converter.utils;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import com.x2j.converter.excp.X2JException;

/**
 * A single instance utility class that reads the error messages properties file
 * (both out-of-the-box and extended files) and returns the messages as and when
 * required.
 */
public class X2JMsgUtils {

	private static final String MSGS_FILE_NAME = "msgs.properties";

	private static final String EXTN_MSGS_FILE_NAME = "x2j-msgs.properties";

	private Properties msgs;

	private static class InstanceHolder {
		private static final X2JMsgUtils INSTANCE = new X2JMsgUtils();
	}

	/**
	 * Returns the single instance of X2JMsgUtils.
	 *
	 * @return instance of X2JMsgUtils
	 */
	public static X2JMsgUtils getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private X2JMsgUtils() {
		try {
			reset();
		} catch (X2JException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void loadMsgs() throws X2JException {
		loadDefaultMsgs();
		loadExtendedMsgs();
	}

	private void loadExtendedMsgs() {
		File extnMsgFile = new File(EXTN_MSGS_FILE_NAME);
		if (!extnMsgFile.exists() || !extnMsgFile.canRead()) {
			extnMsgFile = new File(System.getProperty("user.dir"), EXTN_MSGS_FILE_NAME);
			if (!extnMsgFile.exists() || !extnMsgFile.canRead()) {
				extnMsgFile = new File(System.getProperty("user.home"), EXTN_MSGS_FILE_NAME);
			}
		}
		if (extnMsgFile.exists() && extnMsgFile.canRead()) {
			try {
				msgs.load(new FileReader(extnMsgFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadDefaultMsgs() throws X2JException {
		try {
			msgs.load(X2JMsgUtils.class.getClassLoader().getResourceAsStream(MSGS_FILE_NAME));
		} catch (Exception e) {
			e.printStackTrace();
			throw new X2JException(X2JErrorCodes.X2J_ERR_000);
		}
	}

	/**
	 * Reloads the messages object from the properties file.
	 * 
	 * @throws X2JException X2JException
	 */
	public void reset() throws X2JException {
		msgs = new Properties();
		loadMsgs();
	}

	/**
	 * Returns the message for the given code.
	 *
	 * @param code the message code
	 * @return the message as defined/extended in the properties file
	 */
	public String getMessage(String code) {
		return msgs.getProperty(code);
	}

	/**
	 * Returns all the messages.
	 *
	 * @return the msgs
	 */
	public Properties getMsgs() {
		return msgs;
	}

}
