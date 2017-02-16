package com.timss.asset.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.http.HttpRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.bean.AstBorrowRecordBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.asset.service.AstBorrowRecordService;
import com.timss.asset.service.AttachmentService;
import com.timss.asset.util.CommonUtil;
import com.timss.asset.util.UserPrivUtil;
import com.timss.asset.vo.AssetVo;
import com.timss.inventory.service.InvEquipItemMappingService;
import com.timss.inventory.service.InvMatAssetApplyService;
import com.timss.inventory.vo.SpareBean;
import com.timss.workorder.bean.WoQx;
import com.timss.workorder.service.MaintainPlanVOService;
import com.timss.workorder.service.WoQxService;
import com.timss.workorder.service.WorkOrderVOService;
import com.timss.workorder.vo.MaintainPlanVO;
import com.timss.workorder.vo.WorkOrderVO;
import com.yudean.homepage.facade.ITaskFacade;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;

/**
 * @title:
 * @description: {desc}
 * @company: gdyd
 * @className: AssetInfoController.java
 * @author: 890165
 * @createDate: 2014-6-17
 * @updateUser: 890147(将老的资产bean替换为新的资产bean，删除不使用的内容)
 * @version: 1.1
 */
@Controller
@RequestMapping(value = "asset/assetInfo")
public class AssetInfoController {
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    private AssetInfoService assetInfoService;
    @Autowired
    private UserPrivUtil userPrivUtil;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ItcMvcService timssService;
    @Autowired
    private InvEquipItemMappingService invService;
    @Autowired
    private WorkOrderVOService workOrderService;
    @Autowired
    private WoQxService woQxService;
    @Autowired
    private MaintainPlanVOService maintainPlanService;
    @Autowired
    private AstBorrowRecordService astBorrowRecordService;
    @Autowired
    private ITaskFacade iTaskFacade;
    @Autowired
    private InvMatAssetApplyService invMatAssetApplyService;

    static Logger logger = Logger.getLogger( AssetInfoController.class );

    /**
     * @description:根据assetid获取资产设备信息
     * @author: 890165
     * @createDate: 2014-6-17
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/assetDetail", method = RequestMethod.GET)
    @ReturnEnumsBind("AST_ASSET_TYPE,AST_ASSET_SPEC")
    public ModelAndViewPage assetDetail(Model model,String assetId,String mode) throws Exception {
    	model.addAttribute("assetId",assetId);
    	model.addAttribute("mode",mode);
        return timssService.Pages( "/assetinfo/assetDetail.jsp",new HashMap<String, Object>() );
    }

    /**
     * 在根节点的指定资产下新建一个资产卡片
     * 需指定父资产id或者父资产名称（从根节点下查找，查找不到则新建）
     * @param parentId
     * @param parentName
     * @param paramsMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createAsset", method = RequestMethod.GET)
    @ReturnEnumsBind("AST_ASSET_TYPE,AST_ASSET_SPEC")
    public ModelAndViewPage createAsset(Model model,String siteId,String parentId,String parentName,String paramsMap,HttpServletRequest request) throws Exception {
    	model.addAttribute("mode", "create");
    	if(StringUtils.isBlank(siteId)){
    		UserInfoScope currUser=userPrivUtil.getUserInfoScope();
    		siteId=currUser.getSiteId();
    	}
    	
    	/*byte bb[];
        bb = parentName.getBytes("ISO-8859-1"); //以"ISO-8859-1"方式解析name字符串
        parentName= new String(bb, "UTF-8"); //再用"utf-8"格式表示name
        */
    	AssetBean rootAsset = assetInfoService.queryAssetTreeRootBySiteId(siteId);//站点根节点
    	if(StringUtils.isBlank(parentId)){//未指定父资产id
    		if(StringUtils.isBlank(parentName)){//也未指定父资产名称
    			parentId=rootAsset.getAssetId();//挂到根节点上
    		}else{
    			//在根节点下查找指定名称的父资产
    			List<AssetBean>beans=assetInfoService.queryByName(siteId, parentName, rootAsset.getAssetId());
    			if(beans!=null&&beans.size()>0){
    				parentId=beans.get(0).getAssetId();
    			}else{
    				//不存在则新建
    				AssetBean bean=new AssetBean();
    				bean.setSite(siteId);
    				bean.setParentId(rootAsset.getAssetId());
    				bean.setAssetName(parentName);
    				bean.setAssetType("production");
    				bean.setForbidDelete("Y");
    				bean.setForbidMove("Y");
    				bean.setForbidUpdate("Y");
    				String assetId = assetInfoService.insertAssetInfo( bean );
    				parentId=assetId;
    			}
    		}
    	}
    	model.addAttribute("assetId", parentId);
        return timssService.Pages( "/assetinfo/assetDetail.jsp",new HashMap<String, Object>());
    }
    
    /**
     * @description:由于要进行动态表单改造，所以表单数据采用异步获取的方式
     * @author: yuanzh
     * @createDate: 2015-10-29
     * @param assetId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryAssetForm", method = RequestMethod.POST)
    public AssetBean queryAssetForm(String assetId) throws Exception {
        return assetInfoService.queryAssetDetail( assetId );
    }

    /**
     * 根据id获取资产信息，直接返回bean，不返回页面
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/assetDetailById", method = RequestMethod.POST)
    public ModelAndViewAjax assetDetailById() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String assetId = userInfo.getParam( "assetId" );
        String site = userInfo.getSiteId();
        AssetBean assetInfoBean = assetInfoService.queryAssetDetail( assetId );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "assetInfoBean", assetInfoBean );
        String attachments = attachmentService.queryAttachmentInfo( assetId, site );
        result.put( "attachments", attachments );
        return timssService.jsons( result );
    }

    /**
     * 用于搜索框的查询
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/assetHint", method = RequestMethod.POST)
    public ModelAndViewAjax assetSearchHint() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String keyWord = userInfo.getParam( "kw" );
        String site = userInfo.getSiteId();
        if ( keyWord != null || !"".equals( keyWord ) ) {
            result = assetInfoService.queryAssetForHint( site, keyWord );// 可性能优化，前台只展示前十条，可考虑只查询10条，已优化
        }
        return timssService.jsons( result );
    }
    
    /**
     * @description:根据物资编码查询资产卡片
     * @author: 890151
     * @createDate: 2016-12-6
     * @throws Exception:
     */
    @RequestMapping(value = "/queryAssetForHintByCode", method = RequestMethod.POST)
    public ModelAndViewAjax queryAssetForHintByCode() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String keyWord = userInfo.getParam( "kw" );
        String site = userInfo.getSiteId();
        if ( StringUtils.isNotBlank(keyWord) ) {
            result = assetInfoService.queryAssetForHintByCode( site, keyWord );
        }
        return timssService.jsons( result );
    }

    /**
     * 用于搜索框的查询
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/assetParents", method = RequestMethod.POST)
    public ModelAndViewAjax assetParentIds() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        List<String> result = new ArrayList<String>();
        String assetId = userInfo.getParam( "id" );
        if ( assetId != null || !"".equals( assetId ) ) {
            result = assetInfoService.queryAssetParents( assetId );
        }
        return timssService.jsons( result );
    }

    /**
     * @description:插入资产数据
     * @author: 890165
     * @createDate: 2014-6-20
     * @throws Exception:
     */
    @RequestMapping(value = "/insertAssetData", method = RequestMethod.POST)
    public ModelAndViewAjax insertAssetData(String formData, String uploadIds) throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        /*
         * AssetBean bean = userInfo.getJavaBeanParam( "assetInfo",
         * AssetBean.class ); String uploadIds = userInfo.getParam( "uploadIds"
         * );
         */
        AssetBean bean = JsonHelper.fromJsonStringToBean( formData, AssetBean.class );
        String siteId = userInfo.getSiteId();
        bean.setSite( siteId );
        if ( bean.getAssetId() != null && bean.getAssetId().equals( bean.getParentId() ) ) {
            bean.setAssetId( null );
        }
        String assetId = null;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "资产创建失败");
        String assetCode = bean.getAssetCode();
      //校验是否存在assetCode
        List<AssetBean> list = assetInfoService.queryAssetByCodeAndSite( assetCode, siteId );
        if( list != null && list.size() > 0 ){
        	result.put("msg", "资产编码已存在");
         }else{
        	 assetId = assetInfoService.insertAssetInfo( bean );}

        if ( assetId != null ) {
            result.put( "status", 1 );
            result.put( "bean", bean );

            attachmentService.insertAttachment( assetId, uploadIds, siteId ); // 插入附件
                                                                              // 在两个service中插入数据，是否妥当？
            String attachments = attachmentService.queryAttachmentInfo( assetId, siteId );// 检索出来传回去
            result.put( "attachments", attachments );

            String addSpares = userInfo.getParam( "addSpares" );
            List<Map> addList = JsonHelper.toList( addSpares, Map.class );
            for ( Map tmp : addList ) {
                String spareId = (String) tmp.get( "itemid" );

                Map<String, Object> map = new HashMap<String, Object>();
                map.put( "equipId", assetId );
                map.put( "itemId", spareId );
                map.put( "siteId", siteId );
                invService.insertMappingInfoByItemInfo( map );

                
            }
        } else {
            result.put( "status", 0 );
        }
        return timssService.jsons( result );
    }

    // 查询资产的备件信息
    @RequestMapping(value = "/getAssetSpare")
    public Page<SpareBean> getAssetSpare() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Page<SpareBean> page = invService.getSpareByAssetId( userInfoScope );
        return page;
    }
    
    /**
     * @description:查询资产采购信息
     * @author: 890199
     * @createDate: 2016-12-06
     * @throws Exception:
     */
    @RequestMapping(value = "/getPurchaseList")
    public Page<AssetVo> getPurchaseList(String assetId) throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        Page<AssetVo> page = userInfo.getPage();
        List<AssetVo> purchaseList = assetInfoService.getPurchaseList(assetId);
        String pageSize = CommonUtil.getProperties("pageSize");
    	page.setResults(purchaseList);
    	page.setPageSize(Integer.parseInt(pageSize));
    	page.setTotalRecord(purchaseList.size());
    	page.setParameter("result","success");
        return page;
    }
    
    /**
     * @description:返回关联主项目列表页面
     * @author: 890199
     * @createDate: 2016-12-14
     * @param siteId
     */
    @RequestMapping(value = "/getInvItemListPage")
    public String getInvItemListPage(){
    	return "/assetinfo/assetInvItemList.jsp";
    }
    
    /**
     * @description:根据站点获取关联主项目列表
     * @author: 890199
     * @createDate: 2016-12-14
     * @param siteId
     */
    @RequestMapping(value = "/getInvItemList")
    public Page<AssetVo> getInvItemList() throws Exception {
    	UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
    	Page<AssetVo> page = userInfo.getPage();
    	Map<String, String[]> params = userInfo.getParamMap();
    	//搜索功能
    	if(params.containsKey("search")){
    		String fuzzySearchParams = userInfo.getParam("search");
    		Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
    		page.setFuzzyParams(fuzzyParams);
    	}
    	
    	//设置排序
    	if(params.containsKey("sort")){
    		String sortKey = userInfo.getParam("sort");
    		page.setSortKey( sortKey );
            page.setSortOrder( userInfo.getParam( "order" ) );
    	}else{
    		page.setSortKey("itemCode");
    		page.setSortOrder("asc");
    	}
    	//分好页才能查出invItemList
    	String rows = userInfo.getParam("rows");
    	page.setPageSize(Integer.parseInt(rows));
    	page.setParameter("siteId", userInfo.getSiteId());
    	List<AssetVo> invItemList = assetInfoService.getInvItemList(page);
    	page.setResults(invItemList);
    	page.setParameter("result", "success");
    	return page;
    }

    // 查询资产的工单
    @RequestMapping(value = "/getWorkOrder")
    public ModelAndViewAjax getWorkOrder() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String assetId = userInfo.getParam( "assetId" );
        String siteId = userInfo.getSiteId();
        List<WorkOrderVO> list = workOrderService.queryWOVOByAssetId( assetId, siteId );
        // List list=new ArrayList();
        // Page<WorkOrderVO>page=userInfo.getPage();
        // page.setResults=workOrderService.queryWOVOByAssetId(assetId,siteId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put( "rows", list );
        result.put( "total", list.size() );
        return timssService.jsons( result );
    }

    // 查询资产的维护计划
    @RequestMapping(value = "/getMaintainPlan")
    public ModelAndViewAjax getMaintainPlan() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String assetId = userInfo.getParam( "assetId" );
        String siteId = userInfo.getSiteId();
        List<MaintainPlanVO> list = maintainPlanService.queryMTPVOByAssetId( assetId, siteId );
        // List list=new ArrayList();
        // Page<WorkOrderVO>page=userInfo.getPage();
        // page.setResults=workOrderService.queryWOVOByAssetId(assetId,siteId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put( "rows", list );
        result.put( "total", list.size() );
        return timssService.jsons( result );
    }

    /**
     * @description:查询用户输入的assetCode是否存在
     * @author: 890165
     * @createDate: 2014-6-24
     * @param assetId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/isAssetCodeExist", method = RequestMethod.POST)
    public ModelAndViewAjax isAssetExist() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String assetCode = userInfo.getParam( "assetCode" );
        String mode = userInfo.getParam("mode");
        String assetId = userInfo.getParam( "assetId" );
        String siteId = userInfo.getSiteId();
        //编辑模式下进行多一层判断
        List<AssetBean> list = assetInfoService.queryAssetByCodeAndSite( assetCode, siteId );
        if ( list != null && list.size() > 0 ){
        	if("edit".equals(mode)){
        		for(int i = 0;i<list.size();i++){
            		if(!list.get(i).getAssetId().equals(assetId)){
            			//存在assetId相同
                		return timssService.jsons( "资产编码已经存在" );
            		}
            	}
            	return timssService.jsons( "true" );
        	}
        	else{
        		return timssService.jsons( "资产编码已经存在" );
        	}
        	
        }
        else{
        	return timssService.jsons( "true" );
        }
    }

    /**
     * @description:缺陷记录
     * @author: 890165
     * @createDate: 2014-6-24
     * @param assetId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getWoQx", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> getWoQx() throws Exception {
        UserInfoScope userInfo = timssService.getUserInfoScopeDatas();
        String assetId = userInfo.getParam( "assetId" );
        String siteId = userInfo.getSiteId();
        List<WoQx> woList = woQxService.queryQxByAssetId( assetId, siteId );
        Map<String, Object> dataMap = new HashMap<String, Object>();

        dataMap.put( "rows", woList );
        if ( woList != null && woList.size() > 0 ) {
            dataMap.put( "total", woList.size() );
        } else {
            dataMap.put( "total", 0 );
        }
        return dataMap;
    }

    /**
     * @description:
     * @author: 890165
     * @createDate: 2014-6-25
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/updateAssetInfo")
    public ModelAndViewAjax updateAssetInfo() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        HashMap<String, Object> result = new HashMap<String, Object>();
        String assetInfoString = userInfoScope.getParam( "formData" );
        AssetBean assetInfo = JsonHelper.toObject( assetInfoString, AssetBean.class );
        String assetId = assetInfo.getAssetId();
        String assetCode = assetInfo.getAssetCode();
        result.put( "msg", "更新失败" );
        // 处理资产类型和专业的空值
        if ( assetInfo.getSpec() == null ) {
            assetInfo.setSpec( "" );
        }
        String uploadIds = userInfoScope.getParam( "uploadIds" );
        String siteId = userInfoScope.getSiteId();
        int flag = 0; 
        List<AssetBean> list = assetInfoService.queryAssetByCodeAndSite( assetCode, siteId );
        if ( list != null && list.size() > 0 ){
        	for(int i = 0;i<list.size();i++){
            	if(!list.get(i).getAssetId().equals(assetId)){
                	result.put( "msg", "资产编码已经存在" );
                	flag = 2;
            	}
            }
            if(flag==0){
            	flag = assetInfoService.updateAssetInfo( assetInfo ); 
            }
        }
        else{
        	flag = assetInfoService.updateAssetInfo( assetInfo );
        }
        if ( flag == 1 ) {
            result.put( "result", "ok" );
            result.put( "bean", assetInfo );

            attachmentService.deleteAssetAttach( assetId, siteId ); // 先删除附件关联表的数据
            attachmentService.insertAttachment( assetId, uploadIds, siteId ); // 再插入附件关联表新的数据
            String attachments = attachmentService.queryAttachmentInfo( assetId, siteId );// 检索出来传回去
            result.put( "attachments", attachments );

            String addSpares = userInfoScope.getParam( "addSpares" );
            String delSpares = userInfoScope.getParam( "delSpares" );

            List<Map> addList = JsonHelper.toList( addSpares, Map.class );
            for ( Map tmp : addList ) {
                String spareId = (String) tmp.get( "itemid" );

                Map<String, Object> map = new HashMap<String, Object>();
                map.put( "equipId", assetId );
                map.put( "itemId", spareId );
                map.put( "siteId", siteId );
                invService.insertMappingInfoByItemInfo( map );
            }

            List<Map> delList = JsonHelper.toList( delSpares, Map.class );
            for ( Map tmp : delList ) {
                String spareId = (String) tmp.get( "itemid" );

                Map<String, Object> map = new HashMap<String, Object>();
                map.put( "equipid", assetId );
                map.put( "itemid", spareId );
                map.put( "siteid", siteId );
                invService.deleteMappingInfoByItemInfo( map );
            }
        }
        return ViewUtil.Json( result );
    }

    /**
     * @description:更新资产的位置（拖拉拽设备树的设备到别的地方之后执行）有用
     * @author: 890165
     * @createDate: 2014-7-2
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/updateAssetLocation")
    public HashMap<String, String> updateAssetLocation() throws Exception {
        HashMap<String, String> result = new HashMap<String, String>();
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        String assetId = userInfoScope.getParam( "id" );
        String parentId = userInfoScope.getParam( "parentId" );
        AssetBean bean = assetInfoService.queryAssetDetail( assetId );
        if ( "Y".equals( bean.getIsRoot() ) ) {
            result.put( "result", "forbidMoveRoot" );
        } else if ( "Y".equals( bean.getForbidMove() ) ) {
            result.put( "result", "forbidMove" );
        } else {
        	bean.setParentId( parentId );
            if ( assetInfoService.updateAssetLocation( assetId, parentId ) == 1 ) {
                result.put( "result", "ok" );
            }
        }

        return result;
    }

    /**
     * 删除资产及其子资产，返回父资产bean
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteAsset")
    public ModelAndViewAjax deleteAsset() throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        String assetId = userInfoScope.getParam( "assetId" );
        String parentId = userInfoScope.getParam( "parentId" );
        String siteId = userInfoScope.getSiteId();

        if ( parentId == null || "".equals( parentId ) ) {
            result.put( "result", "forbidDelRoot" );
            result.put( "msg", "不可删除根节点" );
        } else if ( assetInfoService.deleteAsset( assetId ) > 0 ) {
            result.put( "result", "ok" );
            result.put( "parent", assetInfoService.queryAssetDetail( parentId ) );
            String attachments = attachmentService.queryAttachmentInfo( parentId, siteId );// 检索出来传回去
            result.put( "attachments", attachments );
        }
        return ViewUtil.Json( result );
    }
    
    /**
     * @description:作废固定资产申请
     * @author: 890199
     * @createDate: 2016-10-17
     * @return
     * @throws Exception:
     */
    @RequestMapping("/invalidAssetApply")
    public Map<String,Object> invalidAssetApply() throws Exception{
    	//获取flowNo
    	UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
    	Map<String,Object> dataMap = new HashMap<String,Object>();
    	String astApplyId = userInfoScope.getParam("astApplyId");
    	String flowNo = userInfoScope.getParam("flowNo");
		//删除待办
    	iTaskFacade.deleteTask(flowNo,userInfoScope);
    	//判断是否需要删除资产申请(申请通过无需删除，申请不通过需删除)
    	String sign = userInfoScope.getParam("sign");
    	if(sign.equals("Y")){
			//删除数据库数据
			int n = 0;
			n = invMatAssetApplyService.removeAssetApply(astApplyId);
			if(n!=0){
		    	dataMap.put("result", "success");
			}else{
				dataMap.put("result", "fail");
			}
    	}else if(sign.equals("N")){
    		dataMap.put("result", "success");
    		assetInfoService.updateAssetApply(astApplyId);
    	}
		return dataMap;
    }
}
