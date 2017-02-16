package com.timss.operation.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.operation.service.HandoverService;
import com.timss.operation.service.ModeContentService;
import com.timss.operation.vo.ModeContentVo;
import com.timss.operation.vo.NoteBaseVo;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.handler.ThreadLocalVariable;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 运行方式设置controller
 * @description:
 * @company: gdyd
 * @className: ModeController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/modeContent")
public class ModeContentController {
    private Logger log = Logger.getLogger( ModeContentController.class );

    @Autowired
    private ModeContentService modeContentService;
    @Autowired
    private HandoverService handoverService;
    @Autowired
    private ItcMvcService itcMvcService;
        
    /**
     * 保存运行方式
     * @param formData
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @return
     */
    @RequestMapping("/insertOrUpdateModeContentFromMode")
    public Map<String, Object> insertOrUpdateModeContentFromMode( String formData,int dutyId, int jobsId, int handoverId){
        log.debug( "formData baseForm = " + formData + "--dutyId = " + dutyId + "--jobsId = " + jobsId + "-- handoverId = " + handoverId);
        
        NoteBaseVo baseVo = JsonHelper.fromJsonStringToBean( formData, NoteBaseVo.class );
        baseVo.setCurrentHandoverId(handoverId);
        //保存动态表单
        Integer num=handoverService.updateHandoverNote(baseVo);
        
        //插入or更新运行方式
        int count = modeContentService.insertOrUpdateModeContentFromMode( dutyId, jobsId, handoverId);
        log.debug("insertOrUpdateModeContentFromMode-->jobsId:"+jobsId+" dutyId:"+dutyId+" handoverId:"+handoverId+" num:"+count+" noteBase:"+num);
        Map<String, Object> map = new HashMap<String, Object>();
        
        ThreadLocalVariable ThreadlocData = ThreadLocalHandler.getInstance().getVariableIns();
    	if(ThreadlocData != null){
    		String dyFormMsg = (String)ThreadlocData.getThreadLocalAttribute("dyFormMsg");
    		map.put( "dyFormMsg", dyFormMsg );
    	}
        map.put( "result", "success" );
        
        return map;
    } 
    
    /**
     * 
     * @description:通过 dutyId, jobsId handoverId 联合查询
     * @author: fengzt
     * @createDate: 2015年11月5日
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @return:
     */
    @RequestMapping("/queryModeContentByDutyJobsHandover")
    public @ResponseBody Map<String, Object> queryModeContentByDutyJobsHandover( int dutyId, int jobsId,
            int handoverId, String team ){
        List<ModeContentVo> vos = modeContentService.queryModeContentByDutyJobsHandover(dutyId, jobsId, handoverId, team);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "vos", vos );
        if(vos==null){
        	map.put( "result", "fail" );
        }else if( !vos.isEmpty() ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "empty" );
        }
        return map;
    }
    
}
