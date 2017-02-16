package com.timss.finance.dao;

import java.util.List;

import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementPay;
import com.timss.finance.vo.FinanceManagementPayDtlVo;


/**
 * 行政报销相关数据操作
 * @ClassName:     FinanceManagementPayDao
 * @company: gdyd
 * @author:    谷传伟
 * @date:   2015-4-22 下午2:00:10
 */
public interface FinanceManagementPayDao {
	/**
	 * @Title: insertFMP
	 * @Description: 插入行政审批信息
	 * @param fmp
	 * @return
	 */
	int insertFMP(FinanceManagementPay fmp);
	/**
	 * @Title: queryFMPIdByFMPMainId
	 * @Description: 根据mainid，查询行政审批信息的id
	 * @param mainid
	 * @return
	 */
	int queryFMPIdByFMPMainId(String mainid);
	/**
	 * @Title: queryFMPById
	 * @Description: 根据id，查询行政审批信息
	 * @param id
	 * @return
	 */
	FinanceManagementPayDtlVo queryFMPById(int id);
	/**
	 * @Title: queryFMDListByFMPId
	 * @Description: 根据id，查询行政审批信息的报销明细
	 * @param id
	 * @return
	 */
	List<FinanceMainDetail> queryFMDListByFMPId(int id);
	/**
	 * @Title: updateFMP
	 * @Description: 更新行政审批信息
	 * @param fmp
	 * @return
	 */
	int updateFMP(FinanceManagementPay fmp);
	/**
	 * @Title: deleteFMP
	 * @Description: 根据id，删除行政审批信息
	 * @param id
	 * @return
	 */
	int deleteFMP(String id);
}
