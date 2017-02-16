package com.timss.ptw.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.ptw.bean.PtwIsolationArea;
import com.timss.ptw.service.PtwIsolationAreaService;
import com.timss.ptw.vo.IsMethodPointVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
/**
 * 标准隔离证
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationAreaController.java
 * @author: 周保康
 * @createDate: 2014-10-31
 * @updateUser: 周保康
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/ptwIsolationArea")
public class PtwIsolationAreaController {
    private static final Logger log = Logger.getLogger( PtwIsolationAreaController.class );

    @Autowired
    private PtwIsolationAreaService ptwIsolationAreaService;

    @Autowired
    private ItcMvcService itcMvcService;

    @RequestMapping(value = "/openIsolationAreaListPage")
    public String openIsolationAreaListPage() throws Exception {
        return "/IsolationAreaList.jsp";
    }

    @RequestMapping(value = "/SelectKeyBox")
    @ReturnEnumsBind("PTW_KEYBOXSTATUS,PTW_KEYBOXTYPE")
    public String SelectKeyBox() throws Exception {
        return "/ptwBaseConf/SelectKeyBox.jsp";
    }

    @RequestMapping(value = "/isolationAreaListData")
    public Page<PtwIsolationArea> isolationAreaListData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<PtwIsolationArea> page = userInfoScope.getPage();

        String fuzzySearchParams = userInfoScope.getParam( "search" );

        if ( fuzzySearchParams != null ) {
            HashMap<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
            page.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( userInfoScope.getParamMap().containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "no" );
            page.setSortOrder( "desc" );
            page.setParameter( "siteId", siteId );
        }

        page = ptwIsolationAreaService.queryPtwIsolationAreaList( page );

        return page;
    }

    @RequestMapping(value = "/openPtwIsolationAreaPage")
    @ReturnEnumsBind("PTW_KEYBOXSTATUS,PTW_KEYBOXTYPE")
    public String openPtwIsolationAreaPage() throws Exception {
        return "/ptwBaseConf/IsolationArea.jsp";
    }

    @RequestMapping(value = "/queryPtwIsolationAreaById")
    public @ResponseBody
    HashMap<String, Object> queryPtwIsolationAreaById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int id = Integer.parseInt( userInfoScope.getParam( "id" ) );

        PtwIsolationArea data = ptwIsolationAreaService.queryPtwIsolationAreaById( id );

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        result.put( "data", JsonHelper.toJsonString( data ) );
        return result;
    }

    @RequestMapping(value = "/checkIsolationAreaNo")
    @Transactional(propagation = Propagation.REQUIRED)
    public @ResponseBody
    Boolean checkIsolationAreaNo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String islMethDefData = userInfoScope.getParam( "isolationAreaData" );
        JSONObject object = JSONObject.fromObject( islMethDefData );
        String idString = object.get( "id" ).toString();
        String noString = object.get( "isolationAreaNo" ).toString().trim();
        boolean flag = false;
        if ( "".equals( idString ) ) {
            flag = ptwIsolationAreaService.checkIsolationAreaNo( noString );
        } else {
            if ( !"".equals( noString ) ) {
                // 查询当前方法的no与传过来的no比较
                int id = Integer.valueOf( idString );
                PtwIsolationArea obj = ptwIsolationAreaService.queryPtwIsolationAreaById( id );

                if ( noString.equals( obj.getNo() ) ) { // 不改变no的编辑
                    return true;
                } else { // 改变了no 的编辑
                    flag = ptwIsolationAreaService.checkIsolationAreaNo( noString );
                }
            }
        }

        return flag;
    }

    /**
     * @description:更新或者插入隔离证
     * @author: fengzt
     * @createDate: 2014年10月28日
     * @param isolationAreaFormDate
     * @param rowDatas
     * @return HashMap<String, Object>
     * @throws Exception:
     */
    @RequestMapping(value = "/commitPtwIsolationArea", method = RequestMethod.POST)
    public @ResponseBody
    HashMap<String, Object> commitPtwIsolationArea(String isolationAreaFormDate, String rowDatas) throws Exception {
        log.info( "rowDatas = " + rowDatas );
        // 标准隔离证的隔离方法数据
        String isolationAreaMethodDate = rowDatas;

        HashMap<String, String> paramsDataMap = new HashMap<String, String>();
        paramsDataMap.put( "isolationAreaForm", isolationAreaFormDate );
        paramsDataMap.put( "isolationAreaMethodDate", isolationAreaMethodDate );

        PtwIsolationArea ptwIsolationArea = JsonHelper.fromJsonStringToBean( isolationAreaFormDate,
                PtwIsolationArea.class );
        int id = ptwIsolationArea.getId();
        if ( id == 0 ) { // 新建
            ptwIsolationAreaService.insertPtwIsolationArea( paramsDataMap );
        } else { // 修改
            ptwIsolationAreaService.updatePtwIsolationArea( paramsDataMap );
        }

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        return result;
    }

    /**
     * @description:删除或禁用某条隔离方法
     * @author: 王中华
     * @createDate: 2014-10-15
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteIsolationArea", method = RequestMethod.POST)
    public @ResponseBody
    HashMap<String, Object> unavailableIslMethDef(int id) throws Exception {
        int count = ptwIsolationAreaService.deletePtwIsolationAreaById( id );

        HashMap<String, Object> result = new HashMap<String, Object>();
        if ( count > 0 ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "fail" );
        }
        return result;
    }

    /**
     * @description:通过隔离证ID查找列表
     * @author: fengzt
     * @createDate: 2014年10月28日
     * @return: Map<String, Object>
     */
    @RequestMapping("/queryIsolationMethodList")
    public @ResponseBody
    Map<String, Object> queryIsolationMethodList(String areaId) {
        List<IsMethodPointVo> isMethodPointVos = ptwIsolationAreaService.queryIsolationMethodList( areaId );

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", isMethodPointVos );
        if ( !isMethodPointVos.isEmpty() ) {
            dataMap.put( "total", isMethodPointVos.size() );
        } else {
            dataMap.put( "total", 0 );
        }
        return dataMap;

    }

}
