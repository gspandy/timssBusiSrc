package com.timss.asset.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.asset.bean.AssetLayoutBean;
import com.timss.asset.bean.AstTemplateBean;
import com.timss.asset.service.AssetLayoutService;
import com.timss.asset.service.AstTemplateService;
import com.timss.asset.util.VOUtil;

/**
 * 设备配置的controller
 * @author 890147
 *
 */
@Controller
@RequestMapping(value = "asset/layout")
public class AssetLayoutController {
	@Autowired
	private AssetLayoutService layoutService;
	@Autowired
	private AstTemplateService templateService;
	
    static Logger logger = Logger.getLogger( AssetLayoutController.class );
    
    @RequestMapping("/queryAllByAssetId")
    public @ResponseBody Map<String, Object> queryAllByAssetId(String assetId) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        List<AssetLayoutBean> layoutList=layoutService.queryAllByAssetId(assetId);
        map.put("rows", layoutList);
        if( layoutList!=null ){
        	map.put( "total", layoutList.size() );
        }else{
        	map.put( "total", 0 );
        }
        return map;
    }
    
    @RequestMapping(value="/submitChange",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> submitChange( String assetId, String addRows, String delRows, String updateRows ) throws Exception{
    	String addNum,updateNum,delNum;//保存更新结果:执行结果/数据量
    	addNum=updateNum=delNum="0/0";
    	List<AssetLayoutBean>addList=VOUtil.fromJsonToListObject(addRows, AssetLayoutBean.class);
    	if(addList!=null&&addList.size()>0){
    		addNum=layoutService.batchInsert(addList, assetId)+"/"+addList.size();
    	}
    	List<AssetLayoutBean>updateList=VOUtil.fromJsonToListObject(updateRows, AssetLayoutBean.class);
    	if(updateList!=null&&updateList.size()>0){
    		updateNum=layoutService.batchUpdate(updateList)+"/"+updateList.size();
    	}
    	List<AssetLayoutBean>delList=VOUtil.fromJsonToListObject(delRows, AssetLayoutBean.class);
    	if(delList!=null&&delList.size()>0){
    		delNum=layoutService.batchDelete(delList)+"/"+delList.size();
    	}
    	logger.info("submitChange result->add:"+addNum+" update:"+updateNum+" del:"+delNum);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", "ok");
        return map;
    }
    
    @RequestMapping("/queryTemplateByAssetType")
    public @ResponseBody Map<String, Object> queryTemplateByAssetType(String assetType,String siteId) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        List<AstTemplateBean> list=templateService.getDataList(assetType, siteId);
        map.put("rows", list);
        if( list!=null ){
        	map.put( "total", list.size() );
        }else{
        	map.put( "total", 0 );
        }
        return map;
    }
}
