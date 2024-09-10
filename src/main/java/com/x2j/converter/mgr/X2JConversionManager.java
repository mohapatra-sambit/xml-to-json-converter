package com.x2j.converter.mgr;

import static com.x2j.converter.utils.X2JConstants.BOOLEAN;
import static com.x2j.converter.utils.X2JConstants.DOUBLE;
import static com.x2j.converter.utils.X2JConstants.INTEGER;
import static com.x2j.converter.utils.X2JConstants.RECURRENT_PATH;
import static com.x2j.converter.utils.X2JConstants.RECUR_ELEMENT;
import static com.x2j.converter.utils.X2JConstants.XPATH;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.x2j.converter.excp.X2JException;
import com.x2j.converter.mgr.handlers.X2JStringHandler;
import com.x2j.converter.mgr.handlers.X2JStringHandlerFactory;
import com.x2j.converter.utils.X2JErrorCodes;
import com.x2j.converter.utils.X2JUtils;

/**
 * This class is the place where the actual logic of XML to JSON conversion
 * resides. <br>
 * It first looks out for the JSON schema. If present, the code will parse the
 * schema and the incoming XML and then generates the output JSON as per the
 * schema definition.<br>
 * If not, the default conversion logic is applied wherein every XML node is
 * either a JSON object or JSON Array.<br>
 * For more information about the JSON schema, please visit: <a href=
 * "https://github.com/mohapatra-sambit/xml-to-json-converter">XML-To-JSON
 * Converter</a>.
 */
public class X2JConversionManager {

	private static class InstanceHolder {
		private static final X2JConversionManager INSTANCE = new X2JConversionManager();
	}

	/**
	 * Gets the single instance of X2JConversionManager.
	 *
	 * @return instance of X2JConversionManager
	 */
	public static X2JConversionManager getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private X2JConversionManager() {
	}

	/**
	 * This method generates the output JSON based on the default conversion logic.
	 * <br>
	 * For more information on the conversion logic, please check the
	 * XML.toJSONObject() class here:
	 * <a href="https://javadoc.io/doc/org.json/json/latest/index.html">org.json</a>
	 *
	 * @param root the root element of the XML
	 * @return the resultant JSON object
	 * @throws X2JException if any error is thrown during default conversion process
	 */
	public JSONObject processJSONObject(Element root) throws X2JException {
		try {
			if (X2JUtils.isVoid(root)) {
				throw new JSONException("");
			}
			return XML.toJSONObject(X2JUtils.getString(root));
		} catch (JSONException e) {
			throw new X2JException(X2JErrorCodes.X2J_ERR_002);
		}
	}

	/**
	 * This method parses the JSON schema, determines that value/object that needs
	 * to be set to a particular key in the final JSON and generates the final JSON
	 * as the schema.<br>
	 * It makes multiple other internal method calls to handle:
	 * <ol>
	 * <li>JSON Object (recursive call to itself)</li>
	 * <li>JSON Array</li>
	 * <li>Strings</li>
	 * </ol>
	 *
	 * @param jsonObj the JSON schema
	 * @param root    the root element of the XML
	 * @return the resultant JSON object
	 * @throws X2JException if any error is thrown during the conversion process
	 */
	public JSONObject processJSONObject(JSONObject jsonObj, Element root) throws X2JException {
		Set<String> keySet = jsonObj.keySet();
		for (String key : keySet) {
			Object jsonValueObj = jsonObj.get(key);
			if (jsonValueObj instanceof JSONObject) {
				processJSONObject((JSONObject) jsonValueObj, root);
			} else if (jsonValueObj instanceof JSONArray) {
				processJSONArray((JSONArray) jsonValueObj, root);
			} else if (jsonValueObj instanceof String) {
				processString(key, (String) jsonValueObj, jsonObj, root, -1);
			}
		}
		return jsonObj;
	}

	private void processString(String key, String jsonValStr, Object obj, Element root, int index) throws X2JException {
		if (!X2JUtils.isVoid(jsonValStr)) {
			X2JStringHandler handler = X2JStringHandlerFactory.getInstance().getHandler(jsonValStr);
			String xPathVal = handler.handleString(jsonValStr, root);
			String dataType = determineDataType(jsonValStr);
			setOrReplaceData(key, obj, index, xPathVal, dataType);
		}
	}

	private void processJSONArray(JSONArray jsonArr, Element root) throws X2JException {
		if (jsonArr.length() > 0) {
			for (int i = 0; i < jsonArr.length(); i++) {
				Object jsonArrValObj = jsonArr.get(i);
				if (jsonArrValObj instanceof JSONObject) {
					JSONObject jsonArrDefn = (JSONObject) jsonArrValObj;
					String loopPath = jsonArrDefn.has(RECURRENT_PATH) ? (String) jsonArrDefn.get(RECURRENT_PATH) : null;
					if (X2JUtils.isVoid(loopPath)) {
						processJSONObject(jsonArrDefn, root);
					} else {
						jsonArrDefn.remove(RECURRENT_PATH);
						jsonArr.remove(0);
						NodeList loopNodes = X2JUtils.getNodesFromXPath(root, loopPath);
						if (!X2JUtils.isVoid(loopNodes) && loopNodes.getLength() > 0) {
							for (int j = 0; j < loopNodes.getLength(); j++) {
								JSONObject clonedObj = new JSONObject(jsonArrDefn, JSONObject.getNames(jsonArrDefn));
								if (clonedObj instanceof JSONObject) {
									processOrAddClonedJsonObject((JSONObject) clonedObj, loopPath, j, root);
								}
								jsonArr.put(clonedObj);
							}
							processFinalJsonArray(jsonArr, root);
						}
					}
				} else if (jsonArrValObj instanceof JSONArray) {
					processJSONArray((JSONArray) jsonArrValObj, root);
				} else if (jsonArrValObj instanceof String) {
					processString(null, (String) jsonArrValObj, jsonArr, root, i);
				}
			}
		}
	}

	private void processFinalJsonArray(JSONArray jsonArr, Element root) throws X2JException {
		for (int i = 0; i < jsonArr.length(); i++) {
			Object jsonValueObj = jsonArr.get(i);
			if (jsonValueObj instanceof JSONObject) {
				processJSONObject((JSONObject) jsonValueObj, root);
			} else if (jsonValueObj instanceof JSONArray) {
				processJSONArray((JSONArray) jsonValueObj, root);
			}
		}
	}

	private void processOrAddClonedJsonObject(JSONObject clonedJsonObject, String loopPath, int index, Element root)
			throws X2JException {
		Set<String> keySet = clonedJsonObject.keySet();
		for (String key : keySet) {
			Object valObj = clonedJsonObject.get(key);
			if (valObj instanceof JSONObject) {
				processJSONObject((JSONObject) valObj, root);
			} else if (valObj instanceof JSONArray) {
				processJSONArray((JSONArray) valObj, root);
			} else if (valObj instanceof String) {
				String value = (String) valObj;
				if (value.startsWith(RECUR_ELEMENT)) {
					String dataType = determineDataType(value);
					value = value.substring(value.indexOf('(') + 1, value.lastIndexOf(')'));
					value = XPATH + loopPath + "[" + (index + 1) + "]" + value + ")" + dataType;
					clonedJsonObject.put(key, value);
				}
			}
		}
	}

	private String determineDataType(String val) {
		int index = val.lastIndexOf(')');
		if (index == val.length() - 1) {
			return "";
		}
		return val.substring(val.lastIndexOf(')') + 1);
	}

	private void setOrReplaceData(String key, Object obj, int index, String value, String dataType) {
		if (obj instanceof JSONObject) {
			if (StringUtils.containsIgnoreCase(dataType, INTEGER)) {
				((JSONObject) obj).put(key, Integer.valueOf(value));
			} else if (StringUtils.containsIgnoreCase(dataType, DOUBLE)) {
				((JSONObject) obj).put(key, Double.valueOf(value));
			} else if (StringUtils.containsIgnoreCase(dataType, BOOLEAN)) {
				((JSONObject) obj).put(key, Boolean.valueOf(value));
			} else {
				((JSONObject) obj).put(key, value);
			}
		} else if ((obj instanceof JSONArray) && index >= 0) {
			if (StringUtils.containsIgnoreCase(dataType, INTEGER)) {
				((JSONArray) obj).put(index, Integer.valueOf(value));
			} else if (StringUtils.containsIgnoreCase(dataType, DOUBLE)) {
				((JSONArray) obj).put(index, Double.valueOf(value));
			} else if (StringUtils.containsIgnoreCase(dataType, BOOLEAN)) {
				((JSONArray) obj).put(index, Boolean.valueOf(value));
			} else {
				((JSONArray) obj).put(index, value);
			}
		}
	}

}
