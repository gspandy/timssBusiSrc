package com.timss.asset.web; 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.asset.bean.AstBorrowRecordBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.asset.service.AstBorrowRecordService;
import com.timss.asset.vo.AstBorrowRecordVo;
import com.timss.inventory.service.InvMatRecipientsDetailService;
import com.timss.inventory.service.InvMatTranService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
/**
 * @description:
 * @author: 890199
 * @createDate: 2016年7月19日
 * @return
 * @throws Exception:
 */
@Controller
@RequestMapping(value="astBorrowRecord")
public class AstBorrowRecordController {
	private static final Logger log = Logger.getLogger(AstBorrowRecordController.class);
	@Autowired
	private AstBorrowRecordService astBorrowRecordService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private InvMatTranService invMatTranService;
	@Autowired
    public IAuthorizationManager authManager;
	
    /**
     * @description:添加领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@RequestMapping(value="/insertBorrowRecord",method= RequestMethod.POST)
	public Map<String, String> insertBorrowRecord() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String userId = userInfoScope.getUserId();
        Map<String, String> mav = new HashMap<String, String>();
		
		//获取参数
		String borrowData = userInfoScope.getParam("borrowData");
		AstBorrowRecordBean abrb = JsonHelper.fromJsonStringToBean( borrowData, AstBorrowRecordBean.class );
		String outQtyStr = userInfoScope.getParam( "outQty" );
		if( null == outQtyStr || "".equals(outQtyStr) ){
	        mav.put( "result", "error" );
	        mav.put( "msg", "发料数量异常");
	        return mav;
		}
		long outQty = Long.valueOf(outQtyStr);		
		
		//根据发料明细ID查询相关联的领用记录，超过了发料记录则不允许再领用
		AstBorrowRecordVo abrVo = new AstBorrowRecordVo();
		abrVo.setSiteid(siteId);
		abrVo.setImrdid( abrb.getImrdid() );
		Page<AstBorrowRecordVo> abrbPage = astBorrowRecordService.queryAstBorrowRecordOnCondition(userInfoScope, abrVo);
		List<AstBorrowRecordVo> abrbList = abrbPage.getResults();
		if(abrbList!=null && abrbList.size()>= outQty){
	        mav.put( "result", "error" );
	        mav.put( "msg", "该物资卡片的领用登记次数已超过发料数量，不能再进行领用登记");
	        return mav;
		}

		//根据资产ID找领用记录，在找到的记录中，如果有一条未归还的记录即代表已借出，不能借用
		Page<AstBorrowRecordVo> borrowRecordPage = astBorrowRecordService.getAllBorrowRecordList(userInfoScope, abrb.getAssetId());
		List<AstBorrowRecordVo> borrowRecordList = borrowRecordPage.getResults();
		for (AstBorrowRecordVo astBorrowRecordVo : borrowRecordList) {
			if( astBorrowRecordVo.getReturnUserId() == null ){
		        mav.put( "result", "error" );
		        mav.put( "msg", "该资产卡片处于领用状态，请先登记归还" );
		        return mav;
			}
		}
		
		//插入领用记录
		String borrowInfoStr = abrb.getBorrowUserId();
		String[] borrowInfo = borrowInfoStr.split("_");
		abrb.setBorrowUserId(borrowInfo[0]);
		abrb.setBorrowUserDeptId(borrowInfo[1]);
		abrb.setDeptid(userInfoScope.getOrgId());
		abrb.setSiteid(siteId);
		abrb.setCreatedate(new Date());
		abrb.setCreateuser(userId);
		astBorrowRecordService.insertAstBorrowRecord(abrb);
        mav.put( "result", "success" );
        return mav;
	}
	
    /**
     * @description:更新领用记录，并形成入库记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@RequestMapping(value="/updateAstBorrowRecord",method= RequestMethod.POST)
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
	public Map<String, String> updateAstBorrowRecord() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, String> mav = new HashMap<String, String>();
		// 将表单的数据转换成实体类
		String returnData = userInfoScope.getParam("borrowData");
		AstBorrowRecordBean abrb = JsonHelper.fromJsonStringToBean( returnData, AstBorrowRecordBean.class );
		String returnInfoStr = abrb.getReturnUserId();
		String[] returnInfo = returnInfoStr.split("_");
		abrb.setReturnUserId(returnInfo[0]);
		abrb.setReturnUserDeptId(returnInfo[1]);
		abrb.setModifydate(new Date());
		abrb.setModifyuser(userInfoScope.getUserId());
		//更新领用记录
		astBorrowRecordService.updateAstBorrowRecord(abrb);
		//插入归还入库流水
		invMatTranService.returnAsset(userInfoScope, abrb);
        mav.put( "result", "success" );
		return mav;
	}
	
    /**
     * @description:查询所有领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@RequestMapping(value="/getAllBorrowRecordList",method= RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAllBorrowRecordList(String assetId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		List<AstBorrowRecordVo> as = astBorrowRecordService.getDataList(assetId);
		map.put("rows", as);
		if( as!=null ){
        	map.put( "total", as.size() );
        }else{
        	map.put( "total", 0 );
        }

		//设置标志位，控制按钮的disabled属性
        List<AstBorrowRecordBean> borrowing=astBorrowRecordService.getDetail(assetId);
        if(borrowing.size()>0){
        	//有已领用，未归还的记录
        	map.put("state", true);
       }else{
    	   //无已领用，未归还的记录
    	   map.put("state", false);
        }
		return map;
	}
	
    /**
     * @description:查询未归还的物资
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@RequestMapping(value="/getInfo",method= RequestMethod.POST)
	public AstBorrowRecordVo getInfo() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String assetId=userInfoScope.getParam("assetId");
		List<AstBorrowRecordVo> as = astBorrowRecordService.getInfo(assetId);
		AstBorrowRecordVo result = null;
		//存在有领用未归还的记录
		if(0<as.size()){
			result = as.get(0);
		}
		return result;
	}
}
