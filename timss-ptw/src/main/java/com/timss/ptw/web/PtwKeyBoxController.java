package com.timss.ptw.web;

import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.ptw.bean.PtwKeyBox;
import com.timss.ptw.service.PtwKeyBoxService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "ptw/ptwKeyBox")
public class PtwKeyBoxController {
    private static final Logger log = Logger.getLogger(PtwKeyBoxController.class);
    
    @Autowired
    private PtwKeyBoxService ptwKeyBoxService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @RequestMapping(value="/openKeyBoxListPage")
    @ReturnEnumsBind("PTW_KEYBOXSTATUS,PTW_KEYBOXTYPE")
   	public String openIslMethDefListPage() throws Exception{
   		return "/KeyBoxList.jsp";
   	}
    
    @RequestMapping(value="/selectKeyBoxListPage")
    @ReturnEnumsBind("PTW_KEYBOXSTATUS,PTW_KEYBOXTYPE")
   	public String selectKeyBoxListPage() throws Exception{
   		return "/ptwBaseConf/SelectKeyBox.jsp";
   	}
    
    @RequestMapping(value="keyBoxListData")
	public Page<PtwKeyBox> keyBoxListData() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<PtwKeyBox> page = userInfoScope.getPage();
        
        String fuzzySearchParams = userInfoScope.getParam("search");
        
        if(fuzzySearchParams!=null){
        	HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap( fuzzySearchParams );
        	page.setFuzzyParams(fuzzyParams);
        } 
        
        // 设置排序内容
		if (userInfoScope.getParamMap().containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");
			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("keyBoxNo");
			page.setSortOrder("ASC");
			page.setParameter("siteId", siteId);
		}
        
        page = ptwKeyBoxService.queryPtwKeyBoxList(page);
        
        return page;
	}
    
    @RequestMapping(value="/openPtwKeyBoxPage")
    @ReturnEnumsBind("PTW_KEYBOXSTATUS,PTW_KEYBOXTYPE")
	public String openPtwKeyBoxPage() throws Exception{
		return "/ptwBaseConf/KeyBox.jsp";
	}
   
    @RequestMapping(value = "/queryPtwKeyBoxById")
    public @ResponseBody HashMap<String, Object> queryPtwKeyBoxById() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int id = Integer.parseInt(userInfoScope.getParam( "id" ));
        
        PtwKeyBox data = ptwKeyBoxService.queryPtwKeyBoxById(id);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        result.put( "data",  JsonHelper.toJsonString(data));
        return result;
    }
    
    
    
    /**
     * @description:检查no是否唯一
     * @author: 王中华
     * @createDate: 2014-10-14
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/checkKeyBoxNo")
    public @ResponseBody Boolean checkKeyBoxNo() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String islMethDefData = userInfoScope.getParam( "keyBoxData" );
        JSONObject object = JSONObject.fromObject(islMethDefData);
        String idString = object.get("id").toString();
        String noString = object.get("keyBoxNo").toString().trim();
        boolean flag = false;
        if("".equals(idString)){
        	flag =  ptwKeyBoxService.checkKeyBoxNo(noString);
        }else{
        	if(!"".equals(noString)){
        		//查询当前方法的no与传过来的no比较
        		int id = Integer.valueOf(idString);
        		PtwKeyBox obj = ptwKeyBoxService.queryPtwKeyBoxById(id);
        		
        		if(noString.equals(obj.getKeyBoxNo())){  //不改变no的编辑
        			return true;
        		}else{  //改变了no 的编辑
        			flag =  ptwKeyBoxService.checkKeyBoxNo(noString);
        		}
        	}
        }
       
        return flag;
    }
    
    @RequestMapping(value = "/commitPtwKeyBox")
    public @ResponseBody HashMap<String, Object> commitPtwKeyBox() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String keyBoxFormDate = userInfoScope.getParam( "keyBoxFormDate" );
        
        HashMap<String,String> paramsDataMap = new HashMap<String, String>();
        paramsDataMap.put("keyBoxForm", keyBoxFormDate);
		
        PtwKeyBox ptwKeyBox = JsonHelper.fromJsonStringToBean(keyBoxFormDate, PtwKeyBox.class);
        int id = ptwKeyBox.getId();
        if(id == 0){  //新建
        	ptwKeyBoxService.insertPtwKeyBox(paramsDataMap);
        }else{  //修改
        	ptwKeyBox.setModifydate(new Date());
        	ptwKeyBox.setModifyuser(userId);
        	ptwKeyBoxService.updatePtwKeyBox(ptwKeyBox);
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
    @RequestMapping(value = "/deleteKeyBox")
    public @ResponseBody HashMap<String, Object> unavailableIslMethDef() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );
      
        ptwKeyBoxService.deletePtwKeyBoxById(Integer.valueOf(id));
      
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        return result;
    }
    
    /**
     * 根据工作票或隔离证Id查询关联钥匙箱号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryRelateKeyBox")
    public @ResponseBody HashMap<String, Object> queryRelateKeyBox() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String wtId = userInfoScope.getParam( "wtId" );
        String islId = userInfoScope.getParam( "islId" );
        return ptwKeyBoxService.queryByWtIdorIslId(wtId, islId);
    }
    
}
