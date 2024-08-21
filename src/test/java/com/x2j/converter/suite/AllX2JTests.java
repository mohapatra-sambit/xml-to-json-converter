package com.x2j.converter.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.x2j.converter.excp.test.X2JExceptionTest;
import com.x2j.converter.mgr.test.X2JConversionManagerTest;
import com.x2j.converter.test.XMLToJSONConverterTest;
import com.x2j.converter.utils.test.X2JMsgUtilsExtendedTest;
import com.x2j.converter.utils.test.X2JMsgUtilsTest;
import com.x2j.converter.utils.test.X2JUtilsTest;

@RunWith(Suite.class)

@SuiteClasses({ X2JMsgUtilsExtendedTest.class, X2JExceptionTest.class, X2JMsgUtilsTest.class, X2JUtilsTest.class,
		X2JConversionManagerTest.class, XMLToJSONConverterTest.class })

public class AllX2JTests {

}
