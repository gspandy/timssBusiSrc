package com.timss.inventory.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvOrganizeData;
import com.timss.inventory.service.InvOrganizeDataService;
import com.timss.inventory.utils.ImportUtil;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOrganizeDataController.java
 * @author: 890166
 * @createDate: 2015-5-29
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invorganizedata")
public class InvOrganizeDataController {

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvOrganizeDataController.class );

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    public IAuthorizationManager authManager;

    @Autowired
    public InvOrganizeDataService invOrganizeDataService;

    /**
     * @description: 跳转页面
     * @author: 890166
     * @createDate: 2015-6-1
     * @return:
     */
    @RequestMapping(value = "/invOrganizeDataUpload", method = RequestMethod.GET)
    public ModelAndView invOrganizeDataUpload() {
        ModelAndView mav = new ModelAndView( "/invorganizedata/invOrganizeDataUpload.jsp" );
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
        LOG.info( "========================================" );
        LOG.info( "开始上传Excel文件" );
        for ( MultipartFile excelFile : excelFiles ) {
            if ( excelFile.isEmpty() ) {
                LOG.info( "文件未上传" );
            } else {
                LOG.info( "文件长度: " + excelFile.getSize() );
                LOG.info( "文件类型: " + excelFile.getContentType() );
                LOG.info( "文件名称: " + excelFile.getName() );
                LOG.info( "文件原名: " + excelFile.getOriginalFilename() );
                LOG.info( "========================================" );

                try {
                    InputStream is = excelFile.getInputStream();
                    // Excel转换成List
                    List<InvOrganizeData> iodList = ImportUtil.readAndChange2List( is );
                    // 若List是空的话，返回一个false
                    if ( !iodList.isEmpty() ) {
                        // 查询当前指定表中是否存在该站点数据
                        List<InvOrganizeData> oldList = invOrganizeDataService.queryInvOrganizeData( userInfo, null )
                                .getResults();
                        if ( !oldList.isEmpty() ) {
                            InvOrganizeData iod = new InvOrganizeData();
                            iod.setSiteId( userInfo.getSiteId() );
                            invOrganizeDataService.deleteInvOrganizeData( iod );
                        }
                        // 若List中存在数据，则遍历它并保存数据
                        for ( InvOrganizeData iod : iodList ) {
                            iod.setSiteId( userInfo.getSiteId() );
                            iod.setCreateUser( userInfo.getUserId() );
                            invOrganizeDataService.insertInvOrganizeData( iod );
                        }
                    }
                } catch (IOException e) {
                    throw new Exception( e );
                }
            }
        }
    }

    /**
     * @description: 查询列表数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvOrganizeDataList", method = RequestMethod.POST)
    public Page<InvOrganizeData> queryInvOrganizeDataList(String search) throws Exception {
        InvOrganizeData iod = new InvOrganizeData();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            iod = JsonHelper.fromJsonStringToBean( search, InvOrganizeData.class );
        }
        return invOrganizeDataService.queryInvOrganizeData( userInfo, iod );
    }

    /**
     * @description: 触发oracle的触发器自动生成业务数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/callOrganizeDataInit", method = RequestMethod.POST)
    public Map<String, Object> callOrganizeDataInit() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        paramMap.put( "siteid", userInfo.getSiteId() );
        boolean flag = invOrganizeDataService.callOrganizeDataInit( paramMap );
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
    @RequestMapping(value = "/queryOrganizeDataByStatus", method = RequestMethod.POST)
    public Map<String, Object> queryOrganizeDataByStatus() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvOrganizeData iod = new InvOrganizeData();
        iod.setStatus( "1" );
        int listSize = invOrganizeDataService.queryInvOrganizeData( userInfo, iod ).getTotalRecord();
        result.put( "listSize", listSize );
        return result;
    }

}
