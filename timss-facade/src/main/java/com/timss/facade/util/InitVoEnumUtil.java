package com.timss.facade.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yudean.mvc.service.ItcMvcService;

import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.Project;
import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidResultVo;
import com.timss.pms.vo.BidVo;
import com.timss.pms.vo.CheckoutVo;
import com.timss.pms.vo.ContractVo;
import com.timss.pms.vo.PayVo;
import com.timss.pms.vo.PayplanVo;
import com.timss.pms.vo.PlanVo;
import com.timss.pms.vo.ProjectVo;
import com.yudean.itc.dto.support.AppEnum;


/**
 * 
 * @ClassName:     InitVoEnumUtil
 * @company: gdyd
 * @Description: 枚举类型进行初始化的帮助类
 * @author:    黄晓岚
 * @date:   2014-7-3 上午11:40:53
 */
public class InitVoEnumUtil {
    
    
    private static Logger logger=Logger.getLogger(InitVoEnumUtil.class);
    
    //将枚举变量code转换为对应的val
    public static String getEnumVal(String enumCode,List<AppEnum> list){
    	String val=null;
    	if(enumCode==null || enumCode.equals("")){
    		return null;
    	}
    	if(list!=null){
    		for(int i=0;i<list.size();i++){
    			AppEnum appEnum=list.get(i);
    			if(enumCode.equals(appEnum.getCode())){
    				val=appEnum.getLabel();
    				break;
    			}
    		}
    	}
    	return val;
    }
	
   
	
}
