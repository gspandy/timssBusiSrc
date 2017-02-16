package com.timss.finance.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.vo.FinanceManagementApplyDtlVo;
import com.timss.finance.vo.FinanceManagementApplyVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 管理费用申请相关数据操作
 * @ClassName:     FinanceManagementApplyDao
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-3-26 下午4:56:10
 */
public interface FinanceManagementApplyDao {
	
	/**
	 * 插入管理费用申请记录
	 * @Title: insertFinanceManagementApply
	 * @param financeManagementApply
	 * @return
	 */
	int insertFinanceManagementApply(FinanceManagementApply financeManagementApply);
	
	/**
	 * 
	 * @Title: updateFinanceManagementApply
	 * @Description: 更新管理费用申请记录
	 * @param financeManagementApply
	 * @return
	 */
	int updateFinanceManagementApply(FinanceManagementApply financeManagementApply);
	
	/**
	 * 
	 * @Title: queryFinanceManagementApplyById
	 * @Description: 根据id查询管理费用申请记录
	 * @param id
	 * @return
	 */
	FinanceManagementApplyDtlVo queryFinanceManagementApplyById(String id);
	
	/**
	 * 
	 * @Title: queryFinanceManagementList
	 * @Description: 根据分页参数查询多个管理费用申请记录
	 * @param page
	 * @return
	 */
	@RowFilter(flowIdColumn="id",exclusiveRule="TRAVEL_EXCLUSIVE")
	List<FinanceManagementApplyVo> queryFinanceManagementList(Page<FinanceManagementApplyVo> page);
	
	int deleteFinanceManagementApplyById(String id);
	
	int updateFinanceManagementApplyFlowStatus(@Param("id") String id,@Param("flowStatus")String flowStatus,@Param("flowStatusName")String flowStatusName);

    int updateCurrHandUserById(Map<String, String> parmas); 

    /**
     * @description:根据流程实例Id，查找申请单主表信息
     * @author: 王中华
     * @createDate: 2016-6-17
     * @param processId
     * @return:
     */
    FinanceManagementApplyDtlVo queryFinanceManagementApplyByProcessId(@Param("processId") String processId);

    /**
     * @description:更新附件信息
     * @author: 王中华
     * @createDate: 2016-7-27
     * @param financeManagementApply:
     */
    void updateFinManagementApplyAttach(@Param("id") String id,@Param("attach")String attach);

    /**
     * @description:新建报销中的模糊查询
     * @author: 王中华
     * @createDate: 2016-9-5
     * @param page
     * @return:
     */
    List<FinanceManagementApplyVo> queryFuzzyFinanceManagementList(Page<FinanceManagementApplyVo> page);

    /**
     * @description: 查询某类申请单某段时间的所有结束了的单
     * @author: 王中华
     * @createDate: 2017-1-19
     * @param type
     * @param beginDate
     * @param endDate
     * @param siteid 
     * @return:
     */
    List<FinanceManagementApply> queryApplyList(@Param("type")String type, @Param("beginDate")Date beginDate, 
            @Param("endDate")Date endDate, @Param("siteid")String siteid);

    /**
     * @description: 查询一段时间一些人的出差申请审批完成的记录
     * @author: 王中华
     * @createDate: 2017-2-4
     * @param siteid
     * @param userIds
     * @param beginDate
     * @param endDate
     * @return:
     */
    List<FinanceManagementApply> queryFinanceManagementListByDiffDay(@Param("siteid")String siteid, @Param("userId")String userId, 
            @Param("type")String applyType, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);
 
}
