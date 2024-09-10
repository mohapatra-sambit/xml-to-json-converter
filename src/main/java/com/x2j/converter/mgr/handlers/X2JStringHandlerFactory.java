package com.x2j.converter.mgr.handlers;

import static com.x2j.converter.utils.X2JConstants.CONCAT;
import static com.x2j.converter.utils.X2JConstants.LEN;
import static com.x2j.converter.utils.X2JConstants.LOWER;
import static com.x2j.converter.utils.X2JConstants.SUBSTR;
import static com.x2j.converter.utils.X2JConstants.UPPER;
import static com.x2j.converter.utils.X2JConstants.VALUE;
import static com.x2j.converter.utils.X2JConstants.XPATH;

import com.x2j.converter.mgr.handlers.impl.ConcatHandler;
import com.x2j.converter.mgr.handlers.impl.DefaultHandler;
import com.x2j.converter.mgr.handlers.impl.LengthHandler;
import com.x2j.converter.mgr.handlers.impl.LowerCaseHandler;
import com.x2j.converter.mgr.handlers.impl.SubStringHandler;
import com.x2j.converter.mgr.handlers.impl.UpperCaseHandler;
import com.x2j.converter.mgr.handlers.impl.ValueHandler;
import com.x2j.converter.mgr.handlers.impl.XPathHandler;

/**
 * A factory for creating X2JStringHandler objects.
 */
public class X2JStringHandlerFactory {

	private static class InstanceHolder {
		private static final X2JStringHandlerFactory INSTANCE = new X2JStringHandlerFactory();
	}

	private X2JStringHandlerFactory() {
	}

	/**
	 * Gets the single instance of X2JStringHandlerFactory.
	 *
	 * @return instance of X2JStringHandlerFactory
	 */
	public static X2JStringHandlerFactory getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * Returns the appropriate handler for the JSON value string depending upon the
	 * keyword that is defined in the schema. <br>
	 * If the keyword is not as expected, then a default handler is returned.
	 *
	 * @param jsonValue the JSON value string
	 * @return the string handler
	 */
	public X2JStringHandler getHandler(String jsonValue) {
		if (jsonValue.startsWith(XPATH)) {
			return new XPathHandler();
		} else if (jsonValue.startsWith(VALUE)) {
			return new ValueHandler();
		} else if (jsonValue.startsWith(CONCAT)) {
			return new ConcatHandler();
		} else if (jsonValue.startsWith(LEN)) {
			return new LengthHandler();
		} else if (jsonValue.startsWith(UPPER)) {
			return new UpperCaseHandler();
		} else if (jsonValue.startsWith(LOWER)) {
			return new LowerCaseHandler();
		} else if (jsonValue.startsWith(SUBSTR)) {
			return new SubStringHandler();
		} else {
			return new DefaultHandler();
		}
	}

}
