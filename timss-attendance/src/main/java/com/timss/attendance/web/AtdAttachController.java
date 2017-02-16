package com.timss.attendance.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.attendance.service.AtdAttachService;

/**
 * 考勤机的controller
 * @author 890147
 *
 */
@Controller
@RequestMapping(value = "attendance/atdAttach")
public class AtdAttachController {
	@Autowired
	private AtdAttachService atdAttachService;

    static Logger logger = Logger.getLogger( AtdAttachController.class );
    
    @RequestMapping("/query")
    public @ResponseBody Map<String, Object> queryAttach(String itemType,String itemId) throws Exception{
        List<Map<String,Object>> fileMaps = atdAttachService.queryAll(itemType, itemId);
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( fileMaps != null && !fileMaps.isEmpty() ){
            dataMap.put( "fileMap", fileMaps );
            dataMap.put( "result", "success" );
        }else{
            dataMap.put( "result", "fail" );
        }
        
        return dataMap;
    }
}
