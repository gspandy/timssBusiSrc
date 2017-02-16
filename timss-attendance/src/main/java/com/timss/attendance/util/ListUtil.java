package com.timss.attendance.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	public static String toString(List<String> list){
		if (list==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : list) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
	}
	
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<String>();
		list.add("test1");
		list.add("test2");
		list.add("test3");
		
		System.out.println(ListUtil.toString(list));
	}
}
