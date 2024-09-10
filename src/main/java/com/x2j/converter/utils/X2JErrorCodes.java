package com.x2j.converter.utils;

/**
 * Error codes to uniquely identify/associate an error scenario.
 */
public enum X2JErrorCodes {

	/**
	 * Fatal error. Please rebuild the jar or download the latest one from Github.
	 */
	X2J_ERR_000,

	/**
	 * Error while reading XML from file.
	 */
	X2J_ERR_001,

	/**
	 * Error while reading JSON from file/string.
	 */
	X2J_ERR_002,

	/**
	 * Error while parsing JSON from file/string.
	 */
	X2J_ERR_003,

	/**
	 * Error while reading XML data using XPaths.
	 */
	X2J_ERR_004,

	/**
	 * Error while reading XML attribute value using XPaths.
	 */
	X2J_ERR_005,

	/**
	 * Error while reading XML element using XPaths.
	 */
	X2J_ERR_006,

	/**
	 * Error while converting XML element/document to String.
	 */
	X2J_ERR_007,

	/**
	 * Error while writing JSON to file.
	 */
	X2J_ERR_008,

	/**
	 * Error while parsing XML from String.
	 */
	X2J_ERR_009,

	/**
	 * Input XML file not found.
	 */
	X2J_ERR_010,

	/**
	 * Error during string operation.
	 */
	X2J_ERR_011;

}
