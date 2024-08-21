package com.x2j.converter.utils;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.stream.Stream;

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
		reset();
	}

	private void loadMsgs() {
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

	private void loadDefaultMsgs() {
		try {
			msgs.load(X2JMsgUtils.class.getClassLoader().getResourceAsStream(MSGS_FILE_NAME));
		} catch (Exception e) {
			loadDefaultEnglishMsgs();
		}
	}

	private void loadDefaultEnglishMsgs() {
		Stream.of(X2JErrorCodes.values())
				.forEach(errCode -> msgs.setProperty(errCode.name(), errCode.getDefaultEnglishErrorMessage()));
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

	/**
	 * Reloads the messages object from the properties file.
	 */
	public void reset() {
		msgs = new Properties();
		loadMsgs();
	}

}