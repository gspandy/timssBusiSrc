package com.timss.finance.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.vo.FinanceManagementApplyDtlVo;
import com.timss.finance.vo.FinanceManagementApplyVo;
import com.yudean.itc.dto.Page;

/**
 * 对管理费用申请进行增删改查service
 * @ClassName:     FinanceManagementApplyService
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-4-10 下午2:55:50
 */
public interface FinanceManagementApplyService {
	int insertFinanceManagementApply(FinanceManagementApply financeManagementApply);
	
	int updateFinanceManagementApply(FinanceManagementApply financeManagementApply);
	
	int updateFinanceManagementApplyBasic(FinanceManagementApply financeManagementApply);
	
	int deleteFinanceManagementApply(String id);
	
	FinanceManagementApplyDtlVo queryFinanceManagementApplyById(String id);
	
	FinanceManagementApplyDtlVo queryFinanceManagementApplyByProcessId(String processId);
	
	Page<FinanceManagementApplyVo> queryFinanceManagementApplyLsit(Page<FinanceManagementApplyVo> page);
	
	Page<FinanceManagementApplyVo> queryFuzzyFinanceManagementList(Page<FinanceManagementApplyVo> page);

    /**
     * @description:模糊查询，通过名字查询
     * @author: 王中华
     * @createDate: 2016-6-8
     * @param kw
     * @param string
     * @return:
     */
    List<Map<String, Object>> queryApplyInfoFuzzyByName(String kw, String string);
    /**
     * @description:查一段时间和某个人的出差申请单（提供给考勤统计，已经通过了的出差申请单）
     * @author: 王中华
     * @createDate: 2017-2-4
     * @param siteid
     * @param userId 可为空
     * @param beginDate
     * @param endDate
     * @return:
     */
    List<FinanceManagementApply> queryTravelApplyByDiffDay(String siteid,String userId, Date beginDate ,Date endDate);
    
    
    
}
