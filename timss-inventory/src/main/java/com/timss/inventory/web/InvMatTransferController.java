package com.timss.inventory.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.timss.inventory.bean.InvBin;
import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.bean.InvMatTransfer;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvBinService;
import com.timss.inventory.service.InvCategroyService;
import com.timss.inventory.service.InvItemService;
import com.timss.inventory.service.InvMatTransferService;
import com.timss.inventory.service.InvWarehouseService;
import com.timss.inventory.utils.InvMatTransferStatus;
import com.timss.inventory.vo.InvCategoryParam;
import com.timss.inventory.vo.InvItemVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.MvcJsonUtil;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTransferController.java
 * @author: 890151
 * @createDate: 2016-1-9
 * @updateUser: 890151
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmattransfer")
public class InvMatTransferController {

    @Autowired
    private InvMatTransferService invMatTransferService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private InvWarehouseService invWarehouseService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    InvItemService invItemService;

    @Autowired
    InvCategroyService invCategroyService;

    @Autowired
    InvBinService invBinService;

    /**
     * log4j输出
     */
    private static final Logger logger = Logger.getLogger( InvMatTransferController.class );

    /**
     * @description: 移库表单新建跳转
     * @author: 890151
     * @createDate: 2016-1-7
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMatTransferForm", method = RequestMethod.GET)
    @ReturnEnumsBind("INV_MAT_TRANSFER_STATUS")
    public ModelAndView invMatTransferForm() throws Exception {
        String jumpPage = "/invmattransfer/invMatTransferForm.jsp";
        ModelAndView mav = new ModelAndView( jumpPage );

        // 基本信息
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        mav.addObject( "userId", userInfoScope.getUserId() );
        mav.addObject( "siteId", siteId );

        // 获取流程key
        String defKey = "inventory_[@@@]_invmattransfer".replace( "[@@@]", siteId.toLowerCase() );
        mav.addObject( "defKey", defKey );

        // 列表带过来的移出仓库ID和NAME
        String wareHouseFromId = userInfoScope.getParam( "wareHouseFromId" );
        String wareHouseFromName = userInfoScope.getParam( "wareHouseFromName" );
        mav.addObject( "wareHouseFromId", wareHouseFromId );
        wareHouseFromName = StringEscapeUtils.escapeEcmaScript(wareHouseFromName);  
        mav.addObject( "wareHouseFromName", wareHouseFromName );

        // 查询可选有效仓库 过滤带过来的仓库
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        List<InvWarehouse> wareHouseList = invWarehouseService.queryAllWarehouseBySiteId( userInfoScope.getSiteId() );
        for ( InvWarehouse wareHouse : wareHouseList ) {
            if ( ("Y".equals( wareHouse.getActive() )) && (!wareHouseFromId.equals( wareHouse.getWarehouseid() )) ) {
                ArrayList<Object> row = new ArrayList<Object>();
                row.add( wareHouse.getWarehouseid() );
                row.add( wareHouse.getWarehousename() );
                result.add( row );
            }
        }
        mav.addObject( "wareHouseArray", JSON.toJSONString(result) );

        // 列表带过来的物资编码
        String itemIds = userInfoScope.getParam( "itemIds" );
        String itemCodes = userInfoScope.getParam( "itemCodes" );
        String cateTypeIds = userInfoScope.getParam( "cateTypeIds" );
        mav.addObject( "itemIds", itemIds );
        mav.addObject( "itemCodes", itemCodes );
        mav.addObject( "cateTypeIds", cateTypeIds );

        // 返回所有状态
        mav.addObject( "DRAFT", InvMatTransferStatus.DRAFT );// 草稿
        mav.addObject( "TRANSFER_APPLY_COMMIT", InvMatTransferStatus.TRANSFER_APPLY_COMMIT );// 提交移库申请
        mav.addObject( "STOREMAN_AUDIT", InvMatTransferStatus.STOREMAN_AUDIT );// 接收方仓管员审批
        mav.addObject( "OBSOLETE", InvMatTransferStatus.OBSOLETE );// 作废
        mav.addObject( "DONE", InvMatTransferStatus.DONE );// 完成
        return mav;
    }

    /**
     * @description: 移库表单详情跳转
     * @author: 890151
     * @createDate: 2016-1-7
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMatTransferInfo", method = RequestMethod.GET)
    @ReturnEnumsBind("INV_MAT_TRANSFER_STATUS")
    public ModelAndView invMatTransferInfo() throws Exception {
        String jumpPage = "/invmattransfer/invMatTransferForm.jsp";
        ModelAndView mav = new ModelAndView( jumpPage );
        InvMatTransfer invMatTransfer = null;

        // 基本信息
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        mav.addObject( "userId", userInfoScope.getUserId() );
        mav.addObject( "siteId", siteId );

        // 流程key
        String defKey = "inventory_[@@@]_invmattransfer".replace( "[@@@]", siteId.toLowerCase() );
        mav.addObject( "defKey", defKey );

        // 移库申请ID或CODE
        String imtId = userInfoScope.getParam( "imtId" );
        String imtCode = userInfoScope.getParam( "imtCode" );
        mav.addObject( "imtId", imtId );
        mav.addObject( "imtCode", imtCode );
        if ( null != imtId && !"".equals( imtId ) ) {
            invMatTransfer = invMatTransferService.queryInvMatTransferById( userInfoScope, imtId );
        } else if ( null != imtCode && !"".equals( imtCode ) ) {
            invMatTransfer = invMatTransferService.queryInvMatTransferByCode( userInfoScope, imtId );
        }

        // 获取移出仓库的ID和NAME
        String wareHouseFromId = invMatTransfer.getWareHouseFromId();
        String wareHouseFromName = invMatTransfer.getWareHouseFromName();
        mav.addObject( "wareHouseFromId", wareHouseFromId );
        wareHouseFromName = StringEscapeUtils.escapeEcmaScript(wareHouseFromName);  
        mav.addObject( "wareHouseFromName", wareHouseFromName );

        // 列表带过来的物资编码
        String itemIds = userInfoScope.getParam( "itemIds" );
        String cateTypeIds = userInfoScope.getParam( "cateTypeIds" );
        mav.addObject( "itemIds", itemIds );
        mav.addObject( "cateTypeIds", cateTypeIds );

        // 查询可选有效仓库
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        List<InvWarehouse> wareHouseList = invWarehouseService.queryAllWarehouseBySiteId( siteId );
        for ( InvWarehouse wareHouse : wareHouseList ) {
            if ( ("Y".equals( wareHouse.getActive() )) && (!wareHouseFromId.equals( wareHouse.getWarehouseid() )) ) {
                ArrayList<Object> row = new ArrayList<Object>();
                row.add( wareHouse.getWarehouseid() );
                row.add( wareHouse.getWarehousename() );
                result.add( row );
            }
        }
        mav.addObject( "wareHouseArray", JSON.toJSONString(result) );

        // 审批时可以选择货柜和物资类型
        if ( InvMatTransferStatus.STOREMAN_AUDIT.equals( invMatTransfer.getStatus() ) ) {
            // 查询可选物资类型
            List<ArrayList<Object>> result2 = new ArrayList<ArrayList<Object>>();
            InvCategoryParam invCategoryParam = new InvCategoryParam();
            invCategoryParam.setWarehouseId( invMatTransfer.getWareHouseToId() );
            invCategoryParam.setSiteId( invMatTransfer.getSiteid() );
            invCategoryParam.setLevel( "1" );
            List<InvCategory> categoryList = invCategroyService.queryCategroy( invCategoryParam );
            for ( InvCategory invCategory : categoryList ) {
                ArrayList<Object> row = new ArrayList<Object>();
                row.add( invCategory.getInvcateid() );
                row.add( invCategory.getInvcatename() );
                result2.add( row );
            }
            mav.addObject( "categoryArray", JSON.toJSONString(result2) );

            // 查询可选货柜
            List<ArrayList<Object>> result3 = new ArrayList<ArrayList<Object>>();
            Page<InvBin> page = userInfoScope.getPage();
            page.setPageSize( 10000 );
            page.setSortKey( "binid" );
            page.setSortOrder( "asc" );
            page.setParameter( "siteId", userInfoScope.getSiteId() );
            List<InvBin> binList = invBinService.queryBinListBySiteId( page );
            for ( InvBin bin : binList ) {
                ArrayList<Object> row = new ArrayList<Object>();
                if ( bin.getWarehouseid().equals( invMatTransfer.getWareHouseToId() ) ) {
                    row.add( bin.getBinid() );
                    row.add( bin.getBinname() );
                    result3.add( row );
                }
            }
            mav.addObject( "binArray", JSON.toJSONString(result3) );
        }

        // 返回所有状态
        mav.addObject( "DRAFT", InvMatTransferStatus.DRAFT );// 草稿
        mav.addObject( "TRANSFER_APPLY_COMMIT", InvMatTransferStatus.TRANSFER_APPLY_COMMIT );// 提交移库申请
        mav.addObject( "STOREMAN_AUDIT", InvMatTransferStatus.STOREMAN_AUDIT );// 接收方仓管员审批
        mav.addObject( "OBSOLETE", InvMatTransferStatus.OBSOLETE );// 作废
        mav.addObject( "DONE", InvMatTransferStatus.DONE );// 完成
        return mav;
    }

    /**
     * @description:申请提交
     * @author: 890151
     * @createDate: 2014-7-29
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveInvMatTransfer", method = RequestMethod.POST)
    public Map<String, String> saveInvMatTransfer() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String invMatTransferData = userInfoScope.getParam( "invMatTransferData" );
        String invMatTransferDetailData = userInfoScope.getParam( "invMatTransferDetailData" );

        Map<String, Object> saveResultMap = invMatTransferService.saveInvMatTransfer( userInfoScope,
                invMatTransferData, invMatTransferDetailData, null );
        Map<String, String> result = new HashMap<String, String>();
        result.put( "imtId", saveResultMap.get( "imtId" ).toString() );
        result.put( "instanceId", saveResultMap.get( "instanceId" ) != null ? saveResultMap.get( "instanceId" )
                .toString() : "" );
        result.put( "taskId", saveResultMap.get( "taskId" ) != null ? saveResultMap.get( "taskId" ).toString() : "" );
        result.put( "result", "success" );
        return result;
    }

    /**
     * @description:申请提交
     * @author: 890151
     * @createDate: 2014-7-29
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitInvMatTransfer", method = RequestMethod.POST)
    public Map<String, String> commitInvMatTransfer() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String invMatTransferData = userInfoScope.getParam( "invMatTransferData" );
        String invMatTransferDetailData = userInfoScope.getParam( "invMatTransferDetailData" );

        Map<String, Object> saveResultMap = invMatTransferService.commitInvMatTransfer( userInfoScope,
                invMatTransferData, invMatTransferDetailData, null );
        Map<String, String> result = new HashMap<String, String>();
        result.put( "imtId", saveResultMap.get( "imtId" ).toString() );
        result.put( "instanceId", saveResultMap.get( "instanceId" ).toString() );
        result.put( "taskId", saveResultMap.get( "taskId" ).toString() );
        result.put( "result", "success" );
        return result;
    }

    /**
     * @description:删除申请
     * @author: 890151
     * @createDate: 2016-1-10
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteInvMatTransfer", method = RequestMethod.POST)
    public Map<String, String> deleteInvMatTransfer() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String imtId = userInfoScope.getParam( "imtId" );// 获取前台传过来的form表单数据
        invMatTransferService.deleteInvMatTransfer( userInfoScope, imtId );
        Map<String, String> result = new HashMap<String, String>();
        result.put( "result", "success" );
        return result;
    }

    /**
     * @description:作废申请
     * @author: 890151
     * @createDate: 2016-1-10
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/obsoleteInvMatTransfer", method = RequestMethod.POST)
    public Map<String, String> obsoleteInvMatTransfer() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String imtId = userInfoScope.getParam( "imtId" );// 获取前台传过来的form表单数据
        invMatTransferService.obsoleteInvMatTransfer( userInfoScope, imtId );
        Map<String, String> result = new HashMap<String, String>();
        result.put( "result", "success" );
        return result;
    }

    /**
     * @description:查询表单数据
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imtId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvMatTransferInfo", method = RequestMethod.POST)
    public Map<String, Object> queryInvMatTransferInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String imtId = userInfoScope.getParam( "imtId" );
        String userId = userInfoScope.getUserId();

        // 查询申请和物资详情
        InvMatTransfer invMatTransfer = invMatTransferService.queryInvMatTransferById( userInfoScope, imtId );
        List<InvMatTransferDetail> invMatTransferDetailList = invMatTransferService.queryInvMatTransferDetailList(
                userInfoScope, imtId );
        List<InvMatTransferDetail> invMatTransferDetailList2 = new ArrayList<InvMatTransferDetail>();
        for ( InvMatTransferDetail invMatTransferDetail : invMatTransferDetailList ) {
            if ( InvMatTransferStatus.STOREMAN_AUDIT.equals( invMatTransfer.getStatus() ) ) { // 审批时才查询物资在目标仓库是否有，有则设置好货柜和物资类型信息
                InvItemVO itemParam = new InvItemVO();
                String itemcode = invMatTransferDetail.getItemCode();
                String warehouseid = invMatTransferDetail.getToWareHouseId();
                List<InvItemVO> iivList = invItemService.queryInvCategory( itemcode, warehouseid );
                InvItemVO iiv = null;
                if ( null != iivList && !iivList.isEmpty() ) {
                    iiv = iivList.get( 0 );
                    if ( iiv.getWarehouseid() != null && invMatTransferDetail.getToWareHouseId() != null
                            && iiv.getWarehouseid().equals( invMatTransferDetail.getToWareHouseId() ) ) {
                        invMatTransferDetail.setToBinId( iiv.getBinid() );
                        invMatTransferDetail.setToBinName( iiv.getBin() );
                        invMatTransferDetail.setToCateTypeId( iiv.getCateId() );
                        invMatTransferDetail.setToCateTypeName( iiv.getCateType() );
                    }
                }
            }
            invMatTransferDetailList2.add( invMatTransferDetail );
        }

        JSONArray invMatTransferDetailArray = MvcJsonUtil.JSONArrayFromList( invMatTransferDetailList2 );

        // 查询是审批人
        String taskId = "";
        List<String> candidateUsers = new ArrayList<String>();
        if ( invMatTransfer != null && invMatTransfer.getInstanceId() != null
                && !invMatTransfer.getInstanceId().isEmpty() ) {
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( invMatTransfer.getInstanceId() );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            if ( activities.size() != 0 ) {
                Task task = activities.get( 0 );
                taskId = task.getId();
                // 获取节点的候选人
                candidateUsers = workflowService.getCandidateUsers( taskId );
            }
        }
        String approveFlag = null;
        if ( candidateUsers.contains( userId ) ) {
            approveFlag = "approver";
        } else {
            approveFlag = "others";
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put( "invMatTransferStr", JsonHelper.toJsonString( invMatTransfer ) );
        result.put( "invMatTransferDetailListStr", invMatTransferDetailArray.toString() );
        result.put( "approveFlag", approveFlag );
        result.put( "taskId", taskId );
        return result;
    }

    /**
     * @description:通过列表生成物资领料
     * @author: 890151
     * @createDate: 2016-1-9
     * @param codes
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatTransferDetailItems", method = RequestMethod.POST)
    public Map<String, Object> queryMatApplyDetailItems() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String wareHouseId = userInfoScope.getParam( "wareHouseId" );// 列表带过来的物资数据
        String itemIds = userInfoScope.getParam( "itemIds" );
        String itemCodes = userInfoScope.getParam( "itemCodes" );
        String cateTypeIds = userInfoScope.getParam( "cateTypeIds" );

        Map<String, Object> param = new HashMap<String, Object>();
        param.put( "wareHouseId", wareHouseId );
        param.put( "itemIds", itemIds );
        param.put( "itemCodes", itemCodes );
        param.put( "cateTypeIds", cateTypeIds );

        List<InvItemVO> itemList = invMatTransferService.queryItemInfoToMatTransfer( userInfoScope, param );

        Map<String, Object> result = new HashMap<String, Object>();
        StringBuilder jsonStr = new StringBuilder();
        if ( null != itemList && !itemList.isEmpty() ) {
            for ( InvItemVO item : itemList ) {
                jsonStr.append( JsonHelper.fromBeanToJsonString( item ) ).append( ";" );
            }
            String json = jsonStr.toString();
            if ( json.length() > 0 ) {
                json = json.substring( 0, json.length() - 1 );
            }
            result.put( "result", json );
        }
        return result;
    }
}
