package com.timss.finance.util;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

public class PropertyReaderUtilTest {

	@Test
	public void test() {
		Properties properties=PropertyReaderUtil.readProperties("finance-FMA.properties");
		String value=properties.getProperty("role_baseRole");
		assertEquals("${processUserId.equals(\"currentUserId\")} && propertyValue.equals(\"Y\")", value);
	}

}
