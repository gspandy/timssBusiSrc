package com.timss.ptw.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.ptw.bean.PtwIsolationBean;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwIsolationService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 隔离证列表
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationListController.java
 * @author: 周保康
 * @createDate: 2014-10-31
 * @updateUser: 周保康
 * @version: 1.0
 */
@Controller
@RequestMapping("ptw/ptwIsolation")
public class PtwIsolationListController {

    @Autowired
    private PtwIsolationService ptwIsolationService;
    @Autowired
    private PtwInfoService ptwInfoService;
    @Autowired
    private ItcMvcService itcMvcService;
   
    @RequestMapping("/preQueryPtwIslList")
    public String preQueryPtwIslList() {
        return "/islList.jsp";
    }

    
    @RequestMapping("/queryPtwIslList")
    public Page<PtwIsolationBean> queryPtwIslList() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<PtwIsolationBean> page = userInfoScope.getPage();
        //查询类型  wtKind, wtNo,advSearch,treeSearch
        String searchFrom = userInfoScope.getParam( "searchFrom" );
        int isStdWt = Integer.parseInt( userInfoScope.getParam( "isStdWt" ) );
        String searchParams = userInfoScope.getParam( "searchParams" );
        
        //工作票类型和工作票所属站点都要进行设置
        page.setParameter( "isStdWt", isStdWt );
        page.setParameter( "siteId", userInfoScope.getSiteId() );
        
        Map<String, Object> searchParamsMap =  MapHelper.jsonToHashMap( searchParams );
        if ( searchFrom.equals( "wtNo")) {
            page.setFuzzyParams( searchParamsMap );
        }else if(searchFrom.equals( "advSearch" )){
            //查询时间
            if ( ! searchParamsMap.get( "searchDateFrom" ).equals( "" ) || ! searchParamsMap.get( "searchDateEnd" ).equals( "" ) ) {
                page.setParameter( "searchDateType" , searchParamsMap.get( "searchDateType" ) );
                
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if ( ! searchParamsMap.get( "searchDateFrom" ).equals( "" ) ) {
                    page.setParameter( "searchDateFrom", df.parse( searchParamsMap.get( "searchDateFrom" ).toString()) );
                }
                if ( ! searchParamsMap.get( "searchDateEnd" ).equals( "" ) ) {
                    Date endDate = df.parse( searchParamsMap.get( "searchDateEnd" ).toString());
                    endDate = DateUtils.addDays( endDate, 1 );
                    page.setParameter( "searchDateEnd",endDate );
                }
            }
            //查询人
            if(searchParamsMap.get( "searchPersonId" ) != null && !searchParamsMap.get( "searchPersonId" ).equals( "0" )){
                page.setParameter( "searchPersonType",searchParamsMap.get( "searchPersonType" ));
                page.setParameter( "searchPersonId", searchParamsMap.get( "searchPersonId" ) );
            }
            
            //查询状态
            if(searchParamsMap.get( "wtStatus" ) != null && !searchParamsMap.get( "wtStatus" ).equals( "0" )){
                page.setParameter( "wtStatus", searchParamsMap.get( "wtStatus" ) );
            }
            
            //查询类型
            if(searchParamsMap.get( "wtType" ) != null && !searchParamsMap.get( "wtType" ).equals( "" ) && !searchParamsMap.get( "wtType" ).equals( "0" )){
                page.setParameter( "wtType", searchParamsMap.get( "wtType" ) );
            }
            
        }else if ( searchFrom.equals( "treeSearch" ) ) {
            //根据设备树查询
            String eqId = (String) searchParamsMap.get( "eqId" );
            if( eqId!= null && !eqId.equals( "0" ) && !eqId.toUpperCase().equals( userInfoScope.getSiteId() )){
                page.setParameter( "eqId", searchParamsMap.get( "eqId" ) );
            }
        }        
        
        //设置排序内容
        if ( userInfoScope.getParam( "sort" ) != null) {
            page.setSortKey( userInfoScope.getParam( "sort" ) );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }else {
            //设置默认的排序字段
            page.setSortKey( "modifyDate" );
            page.setSortOrder( "desc" );
        }
        
        page = ptwIsolationService.queryPtwIsolationList( page );
        return page;
    }

    

}
