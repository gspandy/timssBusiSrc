package com.timss.inventory.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.timss.asset.bean.AssetBean;
import com.timss.inventory.bean.InvMatAssetApply;
import com.timss.inventory.dao.InvMatReturnsDao;
import com.timss.inventory.exception.PriceNotExistException;
import com.timss.inventory.service.InvItemService;
import com.timss.inventory.service.InvMatApplyDetailService;
import com.timss.inventory.service.InvMatAssetApplyService;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatTranRecVO;
import com.yudean.homepage.facade.ITaskFacade;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.facade.sec.ISecurityFacade;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDetailController.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmatapplydetail")
public class InvMatApplyDetailController {
	private static final Logger log = Logger.getLogger(InvMatApplyDetailController.class);

    /**
     * service 注入
     */
    @Autowired
    private InvMatApplyDetailService invMatApplyDetailService;

    @Autowired
    private InvItemService invItemService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private InvMatAssetApplyService invMatAssetApplyService;
    
    @Autowired
    private ITaskFacade iTaskFacade;
    
    @Autowired
    private ISecurityFacade iSecurityFacade;

    @Autowired
    private InvMatReturnsDao invMatReturnsDao;
    /**
     * @description: 弹出详细信息框
     * @author: 890166
     * @createDate: 2014-7-28
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMatApplyDetailList", method = RequestMethod.GET)
    public String invMatApplyDetailList() {
        return "/invmatapplydetail/invMatApplyDetailList.jsp";
    }

    /**
     * @description: 查询列表信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param imaid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatApplyDetailList", method = RequestMethod.POST)
    public Page<InvMatApplyDetailVO> queryMatApplyDetailList(String imaid) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invMatApplyDetailService.queryMatApplyDetailList( userInfo, imaid );
    }

    /**
     * @description: 当环节是物资接收环节的时候
     * @author: 890166
     * @createDate: 2014-7-29
     * @param imaid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatApplyDetailCSList", method = RequestMethod.POST)
    public Page<InvMatApplyDetailVO> queryMatApplyDetailCSList(String imaid, String embed) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invMatApplyDetailService.queryMatApplyDetailCSList( userInfo, imaid, embed );
    }

    /**
     * @description: 弹出窗口中列表数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryConsumingList", method = RequestMethod.POST)
    public Page<InvMatApplyDetailVO> queryConsumingList(String search) throws Exception {
        InvMatApplyDetailVO imad = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            imad = JsonHelper.fromJsonStringToBean( search, InvMatApplyDetailVO.class );
        }

        return invMatApplyDetailService.queryConsumingList( userInfo, imad );
    }

    /**
     * @description:通过列表生成物资领料（停用）
     * @author: 890166
     * @createDate: 2014-8-25
     * @param codes
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatApplyDetailItems", method = RequestMethod.POST)
    public Map<String, Object> queryMatApplyDetailItems(String codes) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder jsonStr = new StringBuilder();
        List<InvMatApplyDetailVO> imadList = new ArrayList<InvMatApplyDetailVO>();

        if ( !"".equals( codes ) ) {
            String[] codesArray = codes.split( "," );
            for ( String codesStr : codesArray ) {
                //获取物资编码和仓库ID
                String itemCode = codesStr.substring(0,codesStr.indexOf("_"));
                String cateId = codesStr.substring(codesStr.indexOf("_")+1);
                //查询物资信息，和列表的查询方式保持一致
                InvItemVO iiv = new InvItemVO();
                iiv.setIshis(0);
                iiv.setItemcode(itemCode);
                iiv.setCateId(cateId);
                Page<InvItemVO> invItems = invItemService.queryItemsList( userInfo, iiv );
                //将查询到的物资加入待返回列表
                if(invItems!=null){
                    List<InvItemVO> invItemList = invItems.getResults();
                    for(InvItemVO invItemVO:invItemList){
                    	if(invItemVO.getPrice()==null || invItemVO.getNoTaxPrice()==null){
                    		throw new PriceNotExistException();
                    	}
                        InvMatApplyDetailVO imad = new InvMatApplyDetailVO();
                        imad.setItemid(invItemVO.getItemid());
                        imad.setItemcode(invItemVO.getItemcode());
                        imad.setItemname(invItemVO.getItemname());
                        imad.setCusmodel(invItemVO.getCusmodel());
                        imad.setQtyApply(new BigDecimal(1));
                        imad.setStockqty(invItemVO.getStockqty());
                        imad.setNowqty(invItemVO.getNowqty());
                        imad.setUnit1(invItemVO.getUnitname());
                        imad.setUnitCode1(invItemVO.getUnit1());
                        if("SWF".equals(siteId)){	//生物质领料价取不含税单价，其余站点取函数单价
                            imad.setPrice(invItemVO.getNoTaxPrice());
                        }
                        else if("SJW".equals(siteId)){	//沙C多经的价格是严格区分的
                            imad.setPrice(invItemVO.getPrice());
                            imad.setNoTaxPrice(invItemVO.getNoTaxPrice());
                        }
                        else{
                            imad.setPrice(invItemVO.getPrice());
                        }
                        imad.setTotalprice(new BigDecimal(imad.getPrice().toString()));
                        imad.setOutqty(new BigDecimal(0));
                        imad.setOutqtytemp("");
                        imad.setOutstockqty(new BigDecimal(0));
                        imad.setWarehouseid(invItemVO.getWarehouseid());
                        imad.setWarehouse(invItemVO.getWarehouse());
                        imad.setInvcateid(invItemVO.getCateId());
                        imad.setInvcate(invItemVO.getCateType());
                        imadList.add(imad);
                    }
                }
            }
        }

        if ( null != imadList && !imadList.isEmpty() ) {
            for ( InvMatApplyDetailVO imad : imadList ) {
                jsonStr.append( JsonHelper.fromBeanToJsonString( imad ) ).append( ";" );
            }
            String json = jsonStr.toString();
            if ( json.length() > 0 ) {
                json = json.substring( 0, json.length() - 1 );
            }
            map.put( "result", json );
        }
        return map;
    }

    /**
     * @description:查询领料单详情某个物资的发料信息、资产化信息等
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatApplyDetailInfo", method = RequestMethod.GET)
    public ModelAndView queryMatApplyDetailInfo ( @RequestParam String imadId ) throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String rowData = userInfoScope.getParam("rowData");
		InvMatApplyDetailVO invMatApplyDetailVO = JsonHelper.fromJsonStringToBean( rowData, InvMatApplyDetailVO.class );
		
    	String jumpPage = "/invmatapply/invMatApplyItemDetail.jsp";
    	ModelAndView mav = new ModelAndView( jumpPage );
    	
    	//发料情况
        List<InvMatRecipientsDetailVO> recipientsDetailList = invMatApplyDetailService.queryRelateRecipientsByImadId(imadId);

        mav.addObject("imadId",imadId);
        mav.addObject( "recipientsDetailListData", JSON.toJSONString(recipientsDetailList));
        mav.addObject("rowData", JSON.toJSONString( invMatApplyDetailVO ));
        return mav;
    }
    
    /**
     * @description:获取物资资产化信息
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatApplyDetailAssetInfo", method = RequestMethod.POST)
    public Map<String, Object> queryMatApplyDetailAssetInfo(String imadId) throws Exception {
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> map = new HashMap<String, Object>();
        List<AssetBean> assetList = invMatApplyDetailService.queryRelateAssetByImadId(imadId);
        int assetTimes = assetList.size();//已资产化次数，一次一个
        int sendTotal = invMatApplyDetailService.queryMatApplyDetailSendTotal(imadId);//已发料总数
        int returnsTotal = invMatReturnsDao.queryReturnsTotalByImadid(imadId);//已退库总数
        int availableTimes = sendTotal - returnsTotal;//提出资产化申请时，加上校验，发料m个，退库n个，那么只能资产化m-n个
        
        //可资产化次数如果为0，表明从未发料或者发料完后又全部退库，不可以进行资产化
        if(availableTimes <= 0){
            map.put( "result", "error" );
            map.put( "msg", "暂无可以资产化的物资，可能未发料或者已全部退库" );
            return map;
        }
        
        //可资产化次数大于0，表明已发料，或者发料后 退了部分，进而判断已资产化物资是否超过可资产化次数，超了则不能再提资产化申请
        if(assetTimes >= availableTimes){
            map.put( "result", "error" );
            map.put( "msg", "暂无可以资产化的物资，可能未发料或者已全部资产化" );
            return map;
        }
        
        //可资产化5次，提出资产化申请5次，实际资产化2次，以上判断都通过后，还要在此处判断提出资产化申请的次数是否大于可资产化次数
        List<InvMatAssetApply> applyList = invMatAssetApplyService.queryAssetApplyByImadId(imadId);
        int applyTotal = applyList.size();
        if(applyTotal+1 > availableTimes){
        	 map.put( "result", "error" );
             map.put( "msg", "无法提交资产化申请，申请次数超过可资产化数量" );
             return map;
        }
        
        //能拿到合同信息就拿，历史数据没有合同信息也可以进行资产化
        List<InvMatTranRecVO> tranList = invMatApplyDetailService.queryRelateTranRecByImadId(imadId);
        String poId = null;
        int sum = 0;
        for (InvMatTranRecVO invMatTranRecVO : tranList) {
        	sum += Integer.valueOf(invMatTranRecVO.getTranQty());
        	if( sum >= (assetTimes+1) ){
        		poId = invMatTranRecVO.getOutterId();
                map.put( "purOrderId", invMatTranRecVO.getOutterId() );
                break;
        	}
		}
        //获取合同采购日期以及供应商信息
        if(poId!=null && poId.length()>0){
            Map<String, Object> poInfo = invMatApplyDetailService.queryPoInfoByPoId(userInfo, poId);
            map.put( "companyName", poInfo.get("companyName") );
            map.put( "companyTel", poInfo.get("companyTel") );
            map.put( "companyContact", poInfo.get("companyContact") );
            map.put( "purchaseDate", poInfo.get("purchaseDate") );
        }

        map.put( "result", "success" );
        return map;
    }
    
    /**
     * @description:提交资产化备注信息
     * @author: 890199
     * @createDate: 2016-10-09
     * @param 
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/passMemo", method = RequestMethod.POST)
    public Map<String, Object> passMemo() throws Exception {
    	//
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	Map<String, Object> map = new HashMap<String,Object>();
    	String BeanStr = userInfoScope.getParam("BeanData");
		//将STR型装换成BEAN型
    	InvMatAssetApply invMatAssetApply = JsonHelper.fromJsonStringToBean(BeanStr, InvMatAssetApply.class);
    	invMatAssetApply.setAstApplyId( UUIDGenerator.getUUID() );
    	String siteid = userInfoScope.getSiteId();
    	String createuser = userInfoScope.getUserId();
    	invMatAssetApply.setSiteid(siteid);
    	invMatAssetApply.setCreatedate(new Date());
    	invMatAssetApply.setCreateuser(createuser);
    	invMatAssetApply.setStatus("N");
    	invMatAssetApplyService.insertAssetApply(invMatAssetApply);
    	String astApplyId = invMatAssetApply.getAstApplyId();
    	String flowNo = invMatAssetApply.getFlowNo();
    	String typeName = "固定资产";
    	String missionName = invMatAssetApply.getItemName()+"资产化";
    	String statusName = "资产管理员审批";
    	String jumpPath = "inventory/invmatapplydetail/getMemo.do?astApplyId="+astApplyId;
    	List<UserInfo> nextUserList = new ArrayList<UserInfo>();
    	//nextUserList.add(itcMvcService.getUserInfo("950059", userInfoScope.getSiteId()));
    	List<SecureUser> secureUserList = iSecurityFacade.retriveActiveUsersWithSpecificRole("ITC_ASSET_MANAGER", null, null);
    	//转型
    	for(SecureUser secureUser:secureUserList){
    		UserInfo userInfo = itcMvcService.getUserInfo(secureUser.getId(), userInfoScope.getSiteId());
    		nextUserList.add(userInfo);
    	}
    	//创建待办	
    	iTaskFacade.createTask(flowNo, flowNo, typeName, missionName, statusName, 
                jumpPath, nextUserList, userInfoScope, null); 
    	
    	map.put("result", "success");	
    	return map;
    }
    
    /**
     * @description:点击待办获取数据，并且跳转到jsp页面
     * @author: 890199
     * @createDate: 2016-10-10
     * @param 
     * @return
     * @throws Exception:
     */
    @RequestMapping(value="/getMemo")
    public ModelAndView getMemo() throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	Map<String,Object> dataMap = new HashMap<String,Object>();
    	//获取id：!!!!!
    	String astApplyId = userInfoScope.getParam("astApplyId");
    	List<InvMatAssetApply> list = invMatAssetApplyService.queryAssetApplyById(astApplyId);
    	InvMatAssetApply invMatAssetApply = list.get(0);
    	//String invMatAssetApplyStr = JSON.toJSONString(invMatAssetApply);
    	//dataMap.put("invMatAssetApplyStr", invMatAssetApplyStr);
    	dataMap.put("astApplyId", invMatAssetApply.getAstApplyId());
    	
    	String flowNo = invMatAssetApply.getFlowNo();
    	//删除待办
    	/*try{
			//删除待办
    		iTaskFacade.deleteTask(flowNo,userInfoScope);
		}catch(homePageServiceException e){
			log.error("资产化申请：" + flowNo + "获取任务失败");
		}*/
    	//跳转到中转页面
    	return new ModelAndView("/jumpPage.jsp", dataMap);
    }
    
    /**
     * @description:jumpPage页面发起异步请求，获取bean
     * @author: 890199
     * @createDate: 2016-10-20
     * @param 
     * @return
     * @throws Exception:
     */
    @RequestMapping(value="getAssetData")
    public Map<String, Object> getAssetData(String astApplyId)throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	List<InvMatAssetApply> list = invMatAssetApplyService.queryAssetApplyById(astApplyId);
    	InvMatAssetApply invMatAssetApply = list.get(0);
    	String assetDataStr = JSON.toJSONString(invMatAssetApply);
    	map.put("assetDataStr", assetDataStr);
    	return map;
    }
}
