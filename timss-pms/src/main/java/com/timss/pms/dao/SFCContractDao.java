package com.timss.pms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.ContractVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

public interface SFCContractDao {
	/**
	 * 根据合同id，查询合同详细信息
	 * @Title: queryContractById
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	ContractDtlVo queryContractById(int id);
	
	/**
	 * 
	 * @Title: queryContractList
	 * @Description: 查询合同信息
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	List<ContractVo> queryContractList(Page<ContractVo> page);
	
	/**
	 * 
	 * @Title: queryContractListAndFilter
	 * @Description: 查询合同信息
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	@RowFilter(flowIdColumn="tipNo",exclusiveRule="PMS_EXCLUDE",isRouteFilter=true)
	List<ContractVo> queryContractListAndFilter(Page<ContractVo> page);
	
	/**
     * 查询符合条件的项目数量
     * @Title: selectByCodeAndSiteid
     * @param projectCode
     * @param siteid
     * @return
     */
    int selectByCodeAndSiteid(@Param("contractCode")String contractCode,@Param("siteid") String siteid);
}
