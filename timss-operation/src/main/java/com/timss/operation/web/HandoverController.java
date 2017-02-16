package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.operation.bean.Handover;
import com.timss.operation.service.HandoverService;
import com.timss.operation.util.VOUtil;
import com.timss.operation.util.ReturnCodeUtil;
import com.timss.operation.vo.HandoverVo;
import com.yudean.itc.dto.Page;

/**
 * @title: 交接班记录controller
 * @description:
 * @company: gdyd
 * @className: HandoverController.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/handover")
public class HandoverController {

    @Autowired
    private HandoverService handoverService;
    
    /**
     * @description:提交交接班数据
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     * @throws Exception 
     */
    @RequestMapping(value="/insertHandover", method=RequestMethod.POST)
    public Map<String, Object> insertHandover(String currentHandoverId,String formData) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        
        HandoverVo handoverVo = VOUtil.fromJsonToVoUtil( formData, HandoverVo.class );
        handoverVo.setId( Integer.parseInt( currentHandoverId ) );
        
        map = handoverService.commitHandover( handoverVo );
        if( map != null && map.get( "returnCode" ).equals( ReturnCodeUtil.SUCCESS )){
            map.put( "success", 1 );
        }else {
            map.put( "success", 0 );
        }
        return map;

    }

    /**
     * @description:交接班记录列表 分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllHandoverList")
    public Map<String, Object> queryAllHandoverList(int rows, int page, String search ) {
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        
        List<Handover> handoverList = new ArrayList<Handover>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            handoverList = handoverService.queryHandoverBySearch( map, pageVo );
            
        }else{
            //默认分页
            handoverList = handoverService.queryHandoverByPage( pageVo );
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", handoverList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新交接班记录
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateHandover") 
    public Map<String, Object> updateHandover(String formData) {

        HandoverVo handoverVo = VOUtil.fromJsonToVoUtil( formData, HandoverVo.class );

       int count = handoverService.updateHandover( handoverVo );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
    /**
     * @description:删除交接班记录
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteHandover")
    public Map<String, Object> deleteHandover(String formData) {
        
        Handover handover = VOUtil.fromJsonToVoUtil( formData, Handover.class );
        
        int count = 0 ;
        if( handover.getId() > 0 ){
            count = handoverService.deleteHandoverById( handover.getId() );
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
        
    }
    
    /**
     * @description:判断指定日期、岗位、值别（可选）的日历是否对应有交接班记录
     * @author: huanglw
     * @createDate: 2014年7月9日
     * @param dateYMD String
     * @param stationId String
     * @param dutyId String
     * @return:Map<String, Object>
     */
    @RequestMapping("/isExistHandoverByDSD")
    public Map<String, Object> isExistHandoverByDSD(String date,String stationId,String dutyId){
        Map<String, Object> resultMap = new HashMap<String, Object>();  
        resultMap.put( "isExist", handoverService.isExistHandoverByDSD(date,stationId,dutyId));
        return resultMap;
    }
    
    /**
     * 保存运行值班人员修改
     * @param formData
     * @return
     * @throws Exception 
     */
    @RequestMapping("/updateHandoverPerson")
    public Map<String, Object> updateHandoverPerson(Integer handoverId,String userIdStr) throws Exception{
        List<String> userIdList = VOUtil.fromJsonToListObject(userIdStr, String.class);
        int count = handoverService.updateHandoverPerson(handoverId, userIdList,null);
        
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count >= 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
}
