package com.timss.attendance.util;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @title: 考勤数据操作通用函数
 */
@Service("atdDataBaseUtil")
public class AtdDataBaseUtil {
	private Logger log = LoggerFactory.getLogger( AtdDataBaseUtil.class );
	
	/**
	 * 分批的批量插入，一次最多批量插入100条，防止暴内存
	 * @param insertData
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public Integer limitedBatchOperateList(List<?>operateData,Class<?>clazz,String methodName,Object instance) throws Exception{
    	Integer num=0;//计算实际插入条数
    	String operationStr=clazz.getName()+"."+methodName;
        if( operateData != null && operateData.size() > 0 ){
        	log.info("limitedBatchOperateList start->dataSize:"+operateData.size()+" method:"+operationStr);
        	//数据量过大，考虑分批插入
        	int count=0;//统计批量插入条数
        	int batchNum=100;//批量插入的条数
        	int insertIndex=0;//开始插入的序号
        	Method method = clazz.getMethod( methodName, new Class[]{List.class} );
        	while ((insertIndex+batchNum)<=operateData.size()) {
        		count = (Integer)method.invoke(instance, operateData.subList(insertIndex, insertIndex+batchNum) );
                log.info( "limitedBatchOperateList "+operationStr+" operated->" + count );
                num+=count;
        		insertIndex+=batchNum;
			}
        	if(insertIndex<operateData.size()){
        		count = (Integer)method.invoke(instance, operateData.subList(insertIndex, operateData.size()) );
                log.info( "limitedBatchOperateList "+operationStr+" operated->" + count );
                num+=count;
        	}
        }
        log.info( "limitedBatchOperateList "+operationStr+" finish->total operated: " + num+" / "+operateData.size() );
        return num;
    }
}
