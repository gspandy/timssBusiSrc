package com.timss.attendance.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.attendance.bean.MachineBean;
import com.timss.attendance.service.MachineService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;

/**
 * 考勤机的controller
 * @author 890147
 *
 */
@Controller
@RequestMapping(value = "attendance/machine")
public class MachineController {
	@Autowired
    private ItcMvcService timssService;
	@Autowired
	private MachineService machineService;
	@Autowired
    private AtdUserPrivUtil privUtil;
	
    static Logger logger = Logger.getLogger( MachineController.class );
    
    /**
     * 跳转到列表页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage")
    @ReturnEnumsBind("ATD_MACHINE_TYPE")
    public String machineListPage() throws Exception {
        return "/checkin/Machine-list.jsp";
    }
    
    /**
     * 跳转到详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detailPage")
    @ReturnEnumsBind("ATD_MACHINE_TYPE")
    public ModelAndViewPage machineDetailPage(String machineId,String mode) throws Exception {
        MachineBean machineBean=new MachineBean();
        if ( machineId != null&&!"".equals(machineId) ) {//初始查询 
        	machineBean=machineService.queryDetail(machineId);
        }
        Map<String, String>result=new HashMap<String, String>();
        result.put("machineBean", JsonHelper.toJsonString(machineBean));
        result.put("machineId", machineId);
        result.put("mode", mode);
        return timssService.Pages( "/checkin/Machine-detail.jsp", "params",result);
    }
    
    /**
     * 分页查询列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    public Page<MachineBean> getMachineList() throws Exception {
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        Page<MachineBean> page = userInfoScope.getPage();
        page.setParameter("siteId", userInfoScope.getSiteId());
        
        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        Map<String, String[]> params = userInfoScope.getParamMap();
        
        if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			if(fuzzyParams.containsKey("lastSync")){
				fuzzyParams.put("to_char(lastSync,'yyyy-mm-dd hh24:mi')", fuzzyParams.get("lastSync"));
				fuzzyParams.remove("lastSync");
			}
			if(fuzzyParams.containsKey("lastImport")){
				fuzzyParams.put("to_char(lastImport,'yyyy-mm-dd hh24:mi')", fuzzyParams.get("lastImport"));
				fuzzyParams.remove("lastImport");
			}
			page.setFuzzyParams(fuzzyParams);
		}
        
        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }
        
        page = machineService.queryList( page );
        return page;
    }

    /**
     * 查询详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getMachineDetail(String machineId) throws Exception {
        MachineBean bean = machineService.queryDetail( machineId );
        
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put( "machineBean", bean );
        return timssService.jsons(result);       
    }
    
    @RequestMapping(value = "/insertMachine")
    @VaildParam(paramName="machineBean")
    public ModelAndViewAjax insertMachine() throws Exception {
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        MachineBean bean = userInfo.getJavaBeanParam( "machineBean", MachineBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setCreatedate(date);
        bean.setCreateuser(userInfo.getUserId());
        int rst=-1;
        rst=machineService.insert(bean);
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getAmId()!=null){
        	result.put("status", 1);
        	result.put("machineBean", bean);
        }else{
        	result.put("status", 0);
        }
        return timssService.jsons(result);
    }
    
    /**
     * 删除
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/deleteMachine")
    public ModelAndViewAjax deleteMachine() throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        String machineId = userInfoScope.getParam( "machineId" );
        if(machineService.deleteById(machineId)>0){
            result.put( "result", "ok" );
        }else{
        	result.put( "result", "fail" );
        }
        return ViewUtil.Json(result);
    }
    
    /**
     * 更新
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateMachine")
    @VaildParam(paramName="machineBean")
    public ModelAndViewAjax updateMachine() throws Exception {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        MachineBean bean = userInfo.getJavaBeanParam( "machineBean", MachineBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setModifydate(date);
        bean.setModifyuser(userInfo.getUserId());
        int rst=-1;
        rst=machineService.update(bean);
        
        Map<String, Object>result= new HashMap<String, Object>();
        if(rst==1&&bean.getAmId()!=null){
        	result.put("status", 1);
        	result.put("machineBean", bean);
        }else{
        	result.put("status", 0);
        }
        return timssService.jsons(result);
    }
}
