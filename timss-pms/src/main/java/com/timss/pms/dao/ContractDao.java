package com.timss.pms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.pms.bean.Contract;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.ContractVo;
import com.yudean.itc.annotation.MapperMethod;
import com.yudean.itc.annotation.MapperMethod.Type;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 合同dao接口
 * @ClassName:     ContractDao
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-7 下午4:15:30
 */
public interface ContractDao {
	
	/**
	 * 插入合同信息
	 * @Title: insertContract
	 * @param contract
	 * @return
	 */
	@MapperMethod(excuteType=Type.Interceptor_OFF)
	int insertContract(Contract contract);
	
	/**
	 * 根据项目id，查询所属的合同列表
	 * @Title: queryContractListByProjectId
	 * @param id
	 * @return
	 */
	List<ContractVo> queryContractListByProjectId(int id);
	
	/**
	 * 根据合同id，查询合同详细信息
	 * @Title: queryContractById
	 * @param id
	 * @return
	 */
	ContractDtlVo queryContractById(int id);
	
	int updateContract(Contract contract);
	int updateContractDelFlag(Contract contract);
	int updateContractAttach(Contract contract);
	
	/**
	 * 更新合同内容，按需更新
	 * @Title: updateByPrimaryKeySelective
	 * @param contract
	 * @return
	 */
	int updateByPrimaryKeySelective(Contract contract);
	
	
	int deleteContract(int id);
	
	 /**
     * 查询符合条件的项目数量
     * @Title: selectByCodeAndSiteid
     * @param projectCode
     * @param siteid
     * @return
     */
    int selectByCodeAndSiteid(@Param("contractCode")String contractCode,@Param("siteid") String siteid);
    
    /**
	 * 
	 * @Title: queryContractList
	 * @Description: 查询合同信息
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
        @RowFilter(flowIdColumn="tipNo",exclusiveRule="PMS_EXCLUDE",exclusiveDeptRule="PMS_DEPT_EXCLUDE",isRouteFilter=true)
	List<ContractVo> queryContractList(Page<ContractVo> page);
        
        /**
         * @Title: queryContractListWithoutRowFilter
         * @Description: 查询合同信息 无行权限过滤
         * @param page 查询的条件
         * @return: List<ContractVo>  查询结果
         * @throws
         */
        List<ContractVo> queryContractListWithoutRowFilter(Page<ContractVo> page);
        
        /**
         * @Title: queryContractListForSJW
         * @Description: 查询合同信息 行权限加分公司特例
         * @param page 查询的条件
         * @return: List<ContractVo>  查询结果
         * @throws
         */
        @RowFilter(flowIdColumn="tipNo",exclusiveRule="PMS_EXCLUDE",exclusiveDeptRule="PMS_DEPT_EXCLUDE",isRouteFilter=true)
        List<ContractVo> queryContractListForSJW(Page<ContractVo> page);
	/**
	 * 
	 * @Title: queryContractListAndFilter
	 * @Description: 查询合同信息
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	@RowFilter(flowIdColumn="tipNo",exclusiveRule="PMS_EXCLUDE",exclusiveDeptRule="PMS_DEPT_EXCLUDE",isRouteFilter=true)
	List<ContractVo> queryContractListAndFilter(Page<ContractVo> page);
	
	/**
	 * @Title:queryContractListWithCodePrefix
	 * @Description:根据合同编号前缀，查询合同列表
	 * @param prefix
	 * @return List<ContractVo>
	 * @throws
	 */
	List<ContractVo> queryContractListWithCodePrefix(@Param("prefix")String prefix);
	/**
         * 更新合同作废流程实例id
         * @Title: updateNullifyProcInstId
         * @param contractId
         * @param nullifyProcInstId
         * @return
         */
        int updateNullifyProcInstId(@Param("contractId")String contractId,@Param("nullifyProcInstId")String nullifyProcInstId);
}
