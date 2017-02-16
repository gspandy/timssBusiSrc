package com.timss.attendance.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.service.CardDataService;
import com.timss.attendance.util.VOUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 打卡记录
 * @description: {desc}
 * @company: gdyd
 * @className: CardDataController.java
 * @author: fengzt
 * @createDate: 2015年6月8日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/cardData")
public class CardDataController {
    
    @Autowired
    private CardDataService cardDataService;
    
    /**
     * 
     * @description:通过站点查询打卡记录列表
     * @author: fengzt
     * @createDate: 2015年6月7日
     * @return: Map<String, Object> 
     */
    @RequestMapping("/queryCardDataList")
    public @ResponseBody Map<String, Object> queryCardDataList( int rows, int page, String search,String sort, String order ){
    	Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<CardDataBean> result = new ArrayList<CardDataBean>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            result = cardDataService.queryCardDataListBySearch( map, pageVo );
        }else{
            //默认分页
            result = cardDataService.queryCardDataList( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", result );
        if( ! result.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }

}
