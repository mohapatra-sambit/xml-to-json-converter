package com.x2j.converter.utils;

/**
 * Error codes to uniquely identify/associate an error scenario.
 */
public enum X2JErrorCodes {

	/**
	 * Error while reading XML from file.
	 */
	X2J_ERR_001("Error while reading XML from file."),

	/**
	 * Error while reading JSON from file/string.
	 */
	X2J_ERR_002("Error while reading JSON from file/string."),

	/**
	 * Error while parsing JSON from file/string.
	 */
	X2J_ERR_003("Error while parsing JSON from file/string."),

	/**
	 * Error while reading XML data using XPaths.
	 */
	X2J_ERR_004("Error while reading XML data using XPaths."),

	/**
	 * Error while reading XML attribute value using XPaths.
	 */
	X2J_ERR_005("Error while reading XML attribute value using XPaths."),

	/**
	 * Error while reading XML element using XPaths.
	 */
	X2J_ERR_006("Error while reading XML element using XPaths."),

	/**
	 * Error while converting XML element/document to String.
	 */
	X2J_ERR_007("Error while converting XML element/document to String."),

	/**
	 * Error while writing JSON to file.
	 */
	X2J_ERR_008("Error while writing JSON to file."),

	/**
	 * Error while parsing XML from String.
	 */
	X2J_ERR_009("Error while parsing XML from String.");

	private final String defaultEnglishErrorMessage;

	private X2JErrorCodes(String errorMessage) {
		this.defaultEnglishErrorMessage = errorMessage;
	}

	/**
	 * Returns the default English error message, if not defined in the OOB or
	 * Extended properties file.
	 *
	 * @return the default English error message
	 */
	public String getDefaultEnglishErrorMessage() {
		return defaultEnglishErrorMessage;
	}

}
