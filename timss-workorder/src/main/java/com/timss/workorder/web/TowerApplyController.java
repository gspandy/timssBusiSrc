package com.timss.workorder.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.workorder.bean.TowerApply;
import com.timss.workorder.service.WoTowerApplyService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: TowerApplyController.java
 * @author: 朱旺
 * @createDate: 2015-12-21
 * @updateUser: 
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/workorder/towerApply")
public class TowerApplyController {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WoTowerApplyService woTowerApplyService;
//	@Autowired
//	private IAuthorizationManager authManager;
	
	
	private static Logger logger = Logger.getLogger(TowerApplyController.class);

	/**
	 * @description: 登塔申请列表页面
	 * @author: 朱旺
	 * @createDate: 2015-12-21
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value="/towerApplyList",method=RequestMethod.GET)
	@ReturnEnumsBind("WO_TOWERAPPLY_STATUS")
	public ModelAndView workOrderList() throws Exception{
//		 UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	     Map<String, Object> map = new HashMap<String, Object>();
//	     boolean newBtnShowFlag = hasNewTowerApplyPriv(userInfoScope);
//	     map.put( "newBtnShowFlag",  newBtnShowFlag);
	    
	     ModelAndView modelAndView = new ModelAndView( "/towerApply/towerApplyList.jsp", map );
	     return modelAndView;
	}
	
 
    /**
	 * @description: 查询登塔列表数据
	 * @author: 朱旺
	 * @createDate: 2015-12-21
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/towerApplyListdata",method=RequestMethod.POST)
	public Page<TowerApply> towerApplyListData(String search) throws Exception {
		 TowerApply towerApply = null;
         if(StringUtils.isNotBlank(search)){
        	 towerApply = JsonHelper.fromJsonStringToBean(search, TowerApply.class);
         }
         return woTowerApplyService.queryAllTowerApply(towerApply);
		
	}
	
	 /**
     * @description: 跳转到新建登塔申请页面
     * @author: 朱旺
     * @createDate: 2015-12-21
     * @return:
     */
    @RequestMapping(value = "/openNewTowerApplyPage" )
    public String openNewWOPage(){
	return "/towerApply/towerApply.jsp";
    }
    
    /**
     * @description: 跳转到登塔申请详情页面
     * @author: 朱旺
     * @createDate: 2015-12-21
     * @return:
     */
    @RequestMapping(value = "/openTowerApplyInfoPage" )
    @ReturnEnumsBind("WO_TOWERAPPLY_STATUS")
    public String openTowerApplyInfoPage(){
    	return "/towerApply/towerApply.jsp";
    }
    
    /**
     * @description: 提交登塔申请数据
     * @author: 朱旺
     * @createDate: 2015-12-21
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitTowerApplydata",method=RequestMethod.POST)
    @ValidFormToken
    public Map<String,String> commitWoapplydata() throws Exception {
            logger.info("---------------进入提交登塔申请-------------------------");
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String towerApplyFormData = userInfoScope.getParam("towerApplyForm");//获取前台传过来的form表单数据
            TowerApply towerApply = JsonHelper.toObject(towerApplyFormData, TowerApply.class);
            
            String towerApplyId= towerApply.getId();
            String workflowId = towerApply.getWorkflowId();
            String commitStyle = userInfoScope.getParam("commitStyle");//提交方式（用于确定是点击的“提交”还是“暂存”）
            
            Map<String,String> addWODataMap = new HashMap<String, String>();
            addWODataMap.put("towerApplyFormData", towerApplyFormData);
            addWODataMap.put("commitStyle", commitStyle);
            
            String taskId = "noFlow";
            if(workflowId == "" || workflowId == null){  //确定不是因为回退的节点
                    if(towerApplyId == null || "".equals( towerApplyId )){  //初次提交或暂存
                    	towerApply.setId( null );
                        if("commit".equals(commitStyle)){  //提交，启动流程
                                Map<String, Object> insertResultMap = woTowerApplyService.insertTowerApply( towerApply );
                                taskId = insertResultMap.get("taskId").toString();
                                towerApplyId = insertResultMap.get("towerApplyId").toString();
                                workflowId = insertResultMap.get("workflowId").toString();
                        }else if("save".equals(commitStyle)){  //暂存，不启动流程
                                Map<String, Object> saveResultMap = woTowerApplyService.saveTowerApply(towerApply);
                                towerApplyId = saveResultMap.get("towerApplyId").toString();
                        }
                    }else{  //对暂存的登塔申请单提交 或 再次暂存  或其他状态下的提交
                    	if("commit".equals(commitStyle)){  //提交，启动流程
                            addWODataMap.put("updateStyle", "commit");
	                    }else if("save".equals(commitStyle)){  //再次暂存，不启动流程
	                            addWODataMap.put("updateStyle", "save");
	                    }
	                    Map<String, String> flowIdAndTaskId = woTowerApplyService.updateTowerApply(addWODataMap);
	                    workflowId  = flowIdAndTaskId.get("workflowId");
	                    taskId = flowIdAndTaskId.get("taskId");
	                    logger.info("-------------web层：暂存的登塔申请再次提交或暂存完成,登塔申请流程："+workflowId+"-------------");
                    }
            }else{
            	logger.info("-------------web层：回退的登塔申请单再次暂存-------------");
            	addWODataMap.put("updateStyle", "save");
            	Map<String, String> flowIdAndTaskId = woTowerApplyService.updateTowerApply(addWODataMap);
                workflowId  = flowIdAndTaskId.get("workflowId");
                taskId = flowIdAndTaskId.get("taskId");
                logger.info("-------------web层：暂存的登塔申请再次暂存完成,登塔申请流程："+workflowId+"-------------");
            }
            
            Map<String,String> mav = new HashMap<String,String>();
            mav.put("result", "success");
            if(commitStyle == "save"){
                    taskId = "noFlow";
            }
            mav.put("taskId", taskId);
            mav.put("towerApplyId", towerApplyId);
            mav.put("workflowId", workflowId);
            return mav;
    }
    
    @RequestMapping(value = "/queryTowerApplyDataById" )
    public Map<String, Object> queryTowerApplyDataById() throws Exception{
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam("id");
        Map<String, Object> map = woTowerApplyService.queryTowerApplyById( id );
        TowerApply towerApply = (TowerApply) map.get("bean");
        String taskId = (String) map.get("taskId");
      
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("towerApplyForm", JsonHelper.toJsonString(towerApply));
        resultMap.put("taskId", taskId);
        return resultMap;
    } 
    
    /**
     * @description:删除草稿
     * @author: 朱旺
     * @createDate: 2015-12-22
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteTowerApplyDraft",method=RequestMethod.POST)
    public Map<String,String> deleteTowerApplyDraft() throws Exception {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String towerApplyId = userInfoScope.getParam("id");//获取前台传过来的form表单数据
            woTowerApplyService.deleteTowerApply( towerApplyId );
            
            Map<String,String> mav = new HashMap<String,String>();
            mav.put("result", "success");
            return mav;
    }
    
    /**
     * @description:判断登录用户是否有新建登塔申请的权限
     * @author: 朱旺
     * @createDate: 2015-12-23
     * @param userInfoScope
     * @return:
     */
//    private boolean hasNewTowerApplyPriv(UserInfoScope userInfoScope) {
//        String loginUserId = userInfoScope.getUserId();
//        String siteId = userInfoScope.getSiteId();
//        boolean result = false;
//        List<SecureUser> resultList = new ArrayList<SecureUser>();
//        resultList = authManager.retriveUsersWithSpecificGroup(siteId+"_WO_YXJXZ", null, false, true);
//        
//        for (SecureUser temp :resultList ) {
//            if(loginUserId.equals( temp.getId() )){
//                return true;
//            }
//        }
//        
//        return result;
//    }
    
    /**
     * @description: 作废登塔申请，仅登塔发起人可以作废
     * @author: 朱旺
     * @createDate: 2015-12-23
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/obsoleteTowerApply",method=RequestMethod.POST)
    public Map<String,String> obsoleteTowerApply() throws Exception {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String towerApplyId = userInfoScope.getParam("towerApplyId");//获取前台传过来的form表单数据
            
            woTowerApplyService.obsoleteTowerApply(towerApplyId);
            Map<String,String> mav = new HashMap<String,String>();
            mav.put("result", "success");
            return mav;
    }

	
   
}
