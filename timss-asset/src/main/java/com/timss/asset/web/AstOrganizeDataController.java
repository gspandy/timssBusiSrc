package com.timss.asset.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.timss.asset.bean.AstOrganizeData;
import com.timss.asset.service.AstOrganizeDataService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 资产台账导入功能
 * @description: 资产台账导入功能
 * @company: gdyd
 * @className: AstOrganizeDataController.java
 * @author: 890166
 * @createDate: 2015-11-9
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "asset/astorganizedata")
public class AstOrganizeDataController {

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    public IAuthorizationManager authManager;

    @Autowired
    public AstOrganizeDataService astOrganizeDataService;

    /**
     * @description: 跳转页面
     * @author: 890166
     * @createDate: 2015-6-1
     * @return:
     */
    @RequestMapping(value = "/astOrganizeDataUpload", method = RequestMethod.GET)
    public ModelAndView astOrganizeDataUpload() {
        ModelAndView mav = new ModelAndView( "/astorganizedata/astOrganizeDataUpload.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<Organization> orgs = authManager.retriveOrgsByRelation( userInfo.getOrgId(), true, true );

        if ( !orgs.isEmpty() ) {
            mav.addObject( "orgName", orgs.get( 0 ).getName() );
        }

        return mav;
    }

    /**
     * 导入Excel文件入口
     * 
     * @param myfiles
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "uploadExcel")
    public void uploadExcel(@RequestParam MultipartFile[] excelFiles, HttpServletRequest request) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        astOrganizeDataService.uploadExcel( excelFiles, userInfo );
    }

    /**
     * @description: 查询列表数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryAstOrganizeDataList", method = RequestMethod.POST)
    public Page<AstOrganizeData> queryAstOrganizeDataList(String search) throws Exception {
        AstOrganizeData aod = new AstOrganizeData();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            aod = JsonHelper.fromJsonStringToBean( search, AstOrganizeData.class );
        }
        return astOrganizeDataService.queryAstOrganizeData( userInfo, aod );
    }

    /**
     * @description: 触发oracle的触发器自动生成业务数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/callAstOrganizeDataInit", method = RequestMethod.POST)
    public Map<String, Object> callAstOrganizeDataInit() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        paramMap.put( "siteid", userInfo.getSiteId() );
        boolean flag = astOrganizeDataService.callAstOrganizeDataInit( paramMap );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description: 查询还是有没有合格的数据存在
     * @author: 890166
     * @createDate: 2015-6-3
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryAstOrganizeDataByStatus", method = RequestMethod.POST)
    public Map<String, Object> queryAstOrganizeDataByStatus() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        AstOrganizeData aod = new AstOrganizeData();
        aod.setStatus( "1" );
        int listSize = astOrganizeDataService.queryAstOrganizeData( userInfo, aod ).getResults().size();
        result.put( "listSize", listSize );
        return result;
    }

}
