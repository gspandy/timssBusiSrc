package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.AstBorrowRecordBean;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.vo.InvMatTranVO;
import com.timss.inventory.vo.MTPurOrderVO;
import com.timss.purchase.bean.PurApply;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranService.java
 * @author: 890166
 * @createDate: 2014-7-18
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatTranService {

	/**
	 * 查询物资接收列表
	 * 
	 * @description:
	 * @author: 890166
	 * @createDate: 2014-7-23
	 * @param userInfo
	 * @param imt
	 * @return
	 * @throws Exception
	 *             :
	 */
	Page<InvMatTranVO> queryMatTranList(UserInfoScope userInfo, InvMatTranVO imt)
			throws Exception;

	/**
	 * 查询物资接收表单
	 * 
	 * @description:
	 * @author: 890166
	 * @createDate: 2014-7-23
	 * @param userInfo
	 * @param imtid
	 * @return
	 * @throws Exception
	 *             :
	 */
	List<InvMatTranVO> queryMatTranForm(UserInfoScope userInfo, String imtid)
			throws Exception;

	/**
	 * 获取采购单号列表方法
	 * 
	 * @description:
	 * @author: 890166
	 * @createDate: 2014-7-23
	 * @param userInfo
	 * @param mtpo
	 * @return
	 * @throws Exception
	 *             :
	 */
	Page<MTPurOrderVO> queryPurOrderList(UserInfoScope userInfo,
			MTPurOrderVO mtpo) throws Exception;

	/**
	 * 保存方法
	 * 
	 * @description:
	 * @author: 890166
	 * @createDate: 2014-7-24
	 * @param userInfo
	 * @param imt
	 * @param mapData
	 * @param paramMap
	 * @return
	 * @throws Exception
	 *             :
	 */
	boolean saveMatTran(UserInfoScope userInfo, InvMatTranVO imtv,
			Map<String, Object> mapData, Map<String, Object> paramMap)
			throws Exception;

	/**
	 * @description:获取用户信息
	 * @author: 890166
	 * @createDate: 2014-8-5
	 * @param userInfo
	 * @param keyWord
	 * @return
	 * @throws Exception
	 *             :
	 */
	List<Map<String, Object>> getUserHint(UserInfoScope userInfo, String keyWord)
			throws Exception;

	/**
	 * @description:自动触发物资接收
	 * @author: 890166
	 * @createDate: 2014-8-8
	 * @param userInfo
	 * @param appSheetId
	 * @return
	 * @throws Exception
	 *             :
	 */
	Map<String, Object> queryGenerateMatTran(UserInfoScope userInfo,
			String appSheetId, String userId) throws Exception;

	/**
	 * @description:快速查询
	 * @author: 890166
	 * @createDate: 2014-9-1
	 * @param userInfo
	 * @param schfield
	 * @return
	 * @throws Exception
	 *             :
	 */
	Page<InvMatTranVO> quickSearch(UserInfoScope userInfo, InvMatTranVO imt,
			String schfield) throws Exception;

	/**
	 * @description:物资出库列表查询
	 * @author: 890166
	 * @createDate: 2014-9-5
	 * @param userInfo
	 * @param imtv
	 * @return
	 * @throws Exception
	 *             :
	 */
	Page<InvMatTranVO> queryOutMatTran(UserInfoScope userInfo, InvMatTranVO imtv)
			throws Exception;

	/**
	 * @description:物资入库列表查询
	 * @author: 890166
	 * @createDate: 2014-9-5
	 * @param userInfo
	 * @param imtv
	 * @return
	 * @throws Exception
	 *             :
	 */
	Page<InvMatTranVO> queryInMatTran(UserInfoScope userInfo, InvMatTranVO imtv)
			throws Exception;

	/**
	 * @description:更新采购申请表中状态位
	 * @author: 890166
	 * @createDate: 2014-10-17
	 * @param imtid
	 * @param status
	 * @return
	 * @throws Exception
	 *             :
	 */
	boolean updatePurApplyStatus(String imtid) throws Exception;

	/**
	 * @description:
	 * @author: 王中华
	 * @createDate: 2015-10-9
	 * @param appSheetId
	 * @return:
	 */
	Map<String, Object> autoGenerateMatTran(String appSheetId,
			Map<String, Object> msgMap) throws Exception;

	/**
	 * 
	 * @param PoSheetno
	 *            : 根据采购合同单号找到所有接收单
	 * @return
	 */
	List<InvMatTran> queryInvMatTranByPoNo(String poSheetno, String siteId)
			throws Exception;

	/**
	 * @description:自动创建领料发料单，生成出库流水，和入库流水相对应
	 * @author: 890151
	 * @createDate: 2016-11-10
	 * @param userInfo
	 * @param purApply 采购申请
	 * @param invMatTranDetails 流水子表明细
	 * @return
	 * @throws Exception
	 */
	void autoDelivery(UserInfoScope userInfo, PurApply purApply, InvMatTran invMatTran, List<InvMatTranDetail> invMatTranDetails) throws Exception;

	/**
	 * @description:归还资产
	 * @author: 890151
	 * @createDate: 2016-12-9
	 * @param userInfo
	 * @param abrb 归还记录
	 * @return
	 * @throws Exception
	 */
	void returnAsset(UserInfoScope userInfoScope, AstBorrowRecordBean abrb) throws Exception;

}
