package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.ptw.service.PtwUtilService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "/ptw/ptwUtil")
public class PtwUtilController {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtwUtilService ptwUtilService;
    @Autowired
    private IAuthorizationManager authManager;
    
    @RequestMapping(value = "/queryEnum")
    @ResponseBody
    public ArrayList<JSONObject> initPageBaseData() throws Exception {
        ArrayList<JSONObject> resultMap = new ArrayList<JSONObject>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String ecatCode = userInfoScope.getParam( "ecatCode" );// 获取前台传过来的form表单数据
        String searchSiteid = userInfoScope.getParam( "siteid" );

        List<String> result = ptwUtilService.enumValueInSomeSites( ecatCode, searchSiteid );

        for ( int i = 0; i < result.size(); i++ ) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( "enumName", result.get( i ) );
            resultMap.add( jsonObject );
        }

        return resultMap;
    }

    /**
     * @description: 查询某角色的用户未combobox提供数据
     * @author: 王中华
     * @createDate: 2015-7-29
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getRoleUser")
    @ResponseBody
    public List<List<Object>> getRoleUserForCombobox() throws Exception {
        List<List<Object>> result = new ArrayList<List<Object>>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String roleId = userInfoScope.getParam("roleId");// 获取前台传过来的form表单数据

        List<SecureUser> roleUserList = authManager.retriveUsersWithSpecificRole( roleId, null, false, true );
        for ( SecureUser secureUser : roleUserList ) {
            List<Object> row = new ArrayList<Object>();
            row.add( secureUser.getId() );
            row.add( secureUser.getName() );
            result.add( row );
        }
        return result;
    }
    
}
