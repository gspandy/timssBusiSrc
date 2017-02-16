package com.timss.ptw.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.ptw.bean.PtwIsolationBean;
import com.timss.ptw.service.PtwIslMethodService;
import com.timss.ptw.service.PtwIsolationService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;

/**
 * @title: 隔离证
 * @description: 
 * @company: gdyd
 * @className: PtwIsolationController.java
 * @author: fengzt
 * @createDate: 2014年10月30日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("ptw/ptwIsolation")
public class PtwIsolationController {

    @Autowired
    private PtwIsolationService ptwIsolationService;
    
    @Autowired
    private PtwIslMethodService ptwIslMethodService;
    
    @Autowired
    private IAuthorizationManager authManager;

    /**
     * 跳转到隔离证详情页面
     * @description:
     * @author: 周保康
     * @createDate: 2014-10-31
     * @return:
     */
    @RequestMapping(value = "/preQueryIslInfo")
    @ReturnEnumsBind("PTW_KEYBOXSTATUS,PTW_KEYBOXTYPE")
    public String preQueryIslInfo(  ){
        
        return "/islInfo.jsp" ;
    }
    
    /**
     * @description:插入隔离证
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param formData
     * @param safeDatas
     * @param elecDatas
     * @param compSafeDatas
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/insertPtwIsolation", method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> insertPtwIsolation(String formData, String safeDatas, String elecDatas,String jxDatas, String compSafeDatas) {
        Map<String, Object> dataMap = ptwIsolationService.insertOrUpdatePtwIsolation( formData, safeDatas, elecDatas,jxDatas, compSafeDatas );

        int count = (Integer)dataMap.get( "count" );
        if ( count > 0 ) {
            dataMap.put( "result", "success" );
        } else {
            dataMap.put( "result", "fail" );
        }
        return dataMap;
    }


    /**
     * @description:更新隔离证
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @return: Map<String, Object>
     */
    @RequestMapping("/updatePtwIsolation")
    public Map<String, Object> updatePtwIsolation(String formData) {

        PtwIsolationBean vo = JsonHelper.fromJsonStringToBean( formData, PtwIsolationBean.class );

        int count = ptwIsolationService.updatePtwIsolation( vo );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
   /**
    * 
    * @description:通过islId、safeType 查找隔离证和工作票的隔离方法
    * @author: fengzt
    * @createDate: 2014年11月4日
    * @param safeType
    * @param islId
    * @return:
    */
    @RequestMapping("/querySafeDatagridByWtOrIslId")
    public @ResponseBody Map<String, Object> querySafeDatagridByWtOrIslId( int safeType, int islId,int wtId ) {
        Map<String, Object> map = ptwIslMethodService.querySafeDatagridByWtOrIslId( safeType, islId,wtId );
        
        return map;
    }
    
    /**
     * 
     * @description:通过ID查找隔离证
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param id
     * @return:Map
     */
    @RequestMapping("/queryPtwIsolationById")
    public @ResponseBody Map<String, Object> queryPtwIsolationById( int id ) {
        PtwIsolationBean ptwIsolationBean = ptwIsolationService.queryPtwIsolationById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "ptwIsolationBean", ptwIsolationBean );
        if( ptwIsolationBean.getId() > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:通过ID更新隔离证的状态
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param id
     * @return:Map
     */
    @SuppressWarnings("deprecation")
	@RequestMapping("/updatePtwIsolationStatusById")
    public @ResponseBody Map<String, Object> updatePtwIsolationStatusById( int id, int status,String userId,String password, 
            String issueSuper, String issueSuperNo, String finElecInfo,String keyBoxId ) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        //校验密码
        if ( ! authManager.verifyPassword( userId.trim(), password.trim() ) ) {
            map.put( "result", "wrongPassword" );
            return map;
        }
        Integer keyBox = null;
        if (keyBoxId != null && !keyBoxId.trim().equals("")) {
			keyBox = Integer.parseInt(keyBoxId);
		}
        int count = ptwIsolationService.updatePtwIsolationStatusById( id , status, issueSuper, issueSuperNo, finElecInfo,keyBox);
        
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:通过ID更新隔离证的备注
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param id
     * @return:Map
     */
    @RequestMapping("/updatePtwIsolationRemarkById")
    public @ResponseBody Map<String, Object> updatePtwIsolationRemarkById( int id, String remark ) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        int count = ptwIsolationService.updatePtwIsolationRemarkById( id , remark );
        
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    @RequestMapping(value = "/findIslInfoByKeyBoxId")
    public @ResponseBody HashMap<String, Object> findIslInfoByKeyBoxId(int keyBoxId){
        HashMap<String, Object> result = new HashMap<String, Object>();
        List<PtwIsolationBean> list = ptwIsolationService.queryByKeyBoxId(keyBoxId);
        result.put("total", list == null ? 0: list.size());
        result.put("rows", list == null ? new String[0]: list);
        return result;
    }
}
