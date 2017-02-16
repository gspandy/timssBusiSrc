package com.timss.finance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.vo.FinanceMainDetailCostVo;
import com.yudean.itc.dto.Page;

public interface FinanceMainDetailDao {

	/** 
	 * @description: 插入报销明细表
	 * @author: 890170
	 * @createDate: 2014-11-6
	 */
	void insertFinanceMainDetail(FinanceMainDetail financemainDetail);

	/** 
	 * @description: 查询报销明细
	 * @author: 890170
	 * @createDate: 2014-11-6
	 */
	List<FinanceMainDetailCostVo> queryFinanceMainDetailList(
			Page<FinanceMainDetailCostVo> page);

	
	List<FinanceMainDetailCostVo> queryFinanceMainDetailByFinId( @Param("finId") String finId);
	/** 
	 * @description: 通过报销编号查询报销明细
	 * @author: 890170
	 * @createDate: 2014-11-6
	 */
	List<FinanceMainDetail> queryFinanceMainDetailByFid(String fid);

	/**
	 * @description: 根据报销编号删除报销明细
	 * @author: 890170
	 * @createDate: 2014-11-6
	 */
	void deleteFinanceMainDetail(String fid) ;

	/**
         * @description: 根据报销编号将报销明细设置为历史记录
         * @author: 890170
         * @createDate: 2014-11-6
         */
        void historyFinanceMainDetail(String fid) ;
        
	/** 
	 * @description: 通过报销明细编号查询报销明细
	 * @author: 890170
	 * @createDate: 2014-11-6
	 */
	FinanceMainDetail queryFinanceMainDetailById(String id);

	/** 
	 * @description: 通过报销编号查询报销明细费用列表
	 * @author: 890170
	 * @createDate: 2014-11-20
	 */
	List<FinanceMainDetailCostVo> queryFinanceMainDetailCostListByFid(String fid);
	
	/**
	 * @description:更新出差标准
	 * @author: 王中华
	 * @createDate: 2016-9-26
	 * @param id
	 * @param allwoanceType 
	 * @return:
	 */
	int updateFinDetailAllowanceType(@Param("id")String id,@Param("allwoanceType")String allwoanceType,
	        @Param("amount")double amount);
}
