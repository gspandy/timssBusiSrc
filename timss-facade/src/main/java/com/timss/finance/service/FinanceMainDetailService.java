package com.timss.finance.service;

import java.util.List;

import com.timss.finance.bean.FinInsertParams;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.vo.FinanceMainDetailCostVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 
 * @title: Service
 * @description:
 * @company: gdyd
 * @className: FinanceMainDetailService.java
 * @author: 吴圣
 * @createDate: 2014年6月13日
 * @updateUser: 吴圣
 * @version: 1.0
 */
public interface FinanceMainDetailService {
	Page<FinanceMainDetailCostVo> queryFinanceMainDetailList(Page<FinanceMainDetailCostVo> page,
			UserInfoScope userInfoScope,  String fid);
//	List<FinanceMainDetailCostVo> queryFinanceMainDetailList(
//                UserInfoScope userInfoScope, String search, String fid);
	
	List<FinanceMainDetailCostVo> queryFinanceMainDetailCostListByFid(String fid);

	List<FinanceMainDetail> queryFinanceMainDetailByFid(String fid);

	FinanceMainDetail queryFinanceMainDetailById(String id) ;

	FinanceMainDetail deleteFinanceMainDetail(FinanceMain financeMain);
	
	FinanceMainDetail historyFinanceMainDetail(FinanceMain financeMain);

	/** 
	 * @description: 新增报销明细
	 * @author: 890170
	 * @createDate: 2014-12-25
	 */
//	List<FinanceMainDetail> insertFinanceMainDetail(FinanceMain getMain, String beneficiaryid,
//			String submitType, String formData, JSONArray jsonArr, 
//			String finNameEn, String finTypeEn);

    /**
     * @description:新增报销明细（重构）
     * @author: 王中华
     * @createDate: 2015-8-20
     * @param getMain
     * @param insertParamsMap
     * @return:
     */
    List<FinanceMainDetail> insertFinanceMainDetail(FinanceMain getMain, FinInsertParams insertParams);
    
    /**
     * @description: 更新报销明细中的报销标准（出差补贴标准）
     * @author: 王中华
     * @createDate: 2016-9-27
     * @param id 明细id
     * @param allwoanceType 补贴标准
     * @param amount  明细总额
     * @param allowancecost 补贴费
     * @return:
     */
    int updateFinDetailAllowanceType(String id,String allwoanceType, double amount,double allowancecost);
}
