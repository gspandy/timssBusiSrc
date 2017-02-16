package com.timss.finance.util;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.yudean.mvc.testunit.TestUnit;

import static org.junit.Assert.*;

public class JunitTestJson{
	
    public void test(String jsonTestString) throws JsonParseException, JsonMappingException, IOException{
    	ObjectMapper objectMapper=new ObjectMapper();
    	
	    
    	
    	Map<String, Object> jsonMap=objectMapper.readValue(jsonTestString, Map.class);
		if(jsonMap==null){
			return ;
		}
    	Object niObject=jsonMap.get("ni");
    	assertEquals(true, niObject!=null);
    	System.out.println("-----------"+niObject.toString());
    	assertEquals(true, niObject instanceof Map);
		
    	Map<String,Object> map=(Map<String, Object>) niObject;
		Object string=map.get("hao");
		assertEquals(true, string instanceof Boolean);
		
		
    }
    @Test
    public void testAll() throws JsonParseException, JsonMappingException, IOException{
    	String jsonTestString="{\"ni\":{\"hao\":true},\"s\":[false]}";
    	test(jsonTestString);
    	
    	System.out.println("test empty value");
    	test("");
    	
    	System.out.println("test null value");
    	test(null);
    	
    	
    }
}
