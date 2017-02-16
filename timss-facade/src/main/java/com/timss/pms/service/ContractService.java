package com.timss.pms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.ContractVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 合同service层接口
 * @ClassName:     ContractService
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-7 下午4:46:45
 */
public interface ContractService {
	
	/**
	 * 插入合同信息
	 * @Title: insertContract
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param contract
	 */
	HashMap<String,Object> insertContract(Contract contract,List<Payplan> payplans);
	
	void tmpInsertContract(Contract contract,List<Payplan> payplans);
	
	void deleteContract(String contractId);
	
	/**
	 * 根据项目id,查询合同列表
	 * @Title: queryContractByProjectId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<ContractVo> queryContractListByProjectId(String id);
	
	/**
	 * 根据合同id，查询合同详细信息
	 * @Title: queryContractById
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	ContractDtlVo queryContractById(String id);
	
	/**
	 * 启动变更结算计划的审批流程
	 * @Title: changePayList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param contract
	 * @param payplans
	 * @return
	 */
	Map changePayList(Contract contract,List<PayplanTmp> payplans);
	
	int updateContract(Contract contract,List<Payplan> payplans);
	
	int tmpUpdateContract(Contract contract,List<Payplan> payplans);
	
	/**
	 * 启动变更结算计划流程时，修改合同的状态
	 * @Title: updateContractApproving
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param contract
	 * @return
	 */
	int updateContractApproving(Contract contract);
	
	/**
	 * 变更结算计划审批通过后，修改合同状态
	 * @Title: updateContractApproved
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param contract
	 * @return
	 */
	int updateContractApproved(Contract contract);
	
	/**
	 * 根据招标id，查询合同信息
	 * @Title: queryContractByBidId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidId
	 * @return
	 */
	ContractDtlVo queryContractByBidId(String bidId);
	
	/**
	 * 删除合同以及合同的变更流程
	 * @Title: delWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param contractId
	 * @return
	 */
	int delWorkflow(String contractId);
	
	/**
	 * 终止合同变更流程，并修改合同状态
	 * @Title: stopWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param project
	 * @param procInstId
	 * @return
	 */
	int stopWorkflow(Contract contract,String processInstId,String reason);
	
	/**
	 * 判断当前站点下编号是否存在,排除统一
	 * @Title: isProjectCodeExisted
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param projectCode
	 * @return
	 */
	boolean isContractCodeExisted(String contractCode,String contractId);
	
	/**
	 * 
	 * @Title: queryContractList
	 * @Description: 查询合同信息
	 * @param: @param page 查询条件，包括分页信息
	 * @param: @param userInfo 用户信息
	 * @return: Page<ContractVo>  包含的分页信息的合同信息
	 * @throws
	 */
	Page<ContractVo> queryContractList(Page<ContractVo> page,UserInfoScope userInfo);
	
	/**
	 * @Title: queryContractListByKeyWord
	 * @Description: 根据关键字，即合同名称模块查询最多10条记录
	 * @param kw
	 * @return
	 */
	public List<Map> queryContractListByKeyWord(String kw);
	/**
         * @Title: queryContractListByKeyWordWithoutRowFilter
         * @Description: 根据关键字，即合同名称模块查询最多10条记录，剥离行权限过滤
         * @param kw
         * @return
         */
	public List<Map> queryContractListByKeyWordWithoutRowFilter(String kw);
	/**
	 * 
	 * @Title: voidFlow
	 * @Description: 合同作废
	 * @param params
	 * @return
	 */
	public boolean voidFlow(FlowVoidParamBean params);
	/**
	 * @Title: saveOrUpdateContractWithWorkFlow
	 * @Description: 提交合同信息(由于可能有多次提交，新增或更新，以及是否启动流程需要依据条件区分处理)
	 * @param contract
	 * @param startWorkFlow
	 */
	Map<String,Object> saveOrUpdateContractWithWorkFlow(Contract contract,boolean startWorkFlow);
	/**
	 * @Title: saveOrUpdateContractAttach
	 * @Description: 更新合同附件
	 * @param contract
	 */
	Map<String,Object> saveOrUpdateContractAttach(Contract contract);
	/**
	 * @Title: updateContractWithWorkFlow
	 * @Description: 更新合同以及所关联的结算计划(带审批流程)
	 * @param contract
	 * @param payplans
	 * @return
	 */
	int updateContractWithWorkFlow(Contract contract,List<Payplan> payplans);
	/**
	 * @Title: updateContractApproving
	 * @Description: 启动变更结算计划流程时，修改合同的状态审批中
	 * @param contractId
	 * @return
	 */
	int updateContractApproving(int contractId);
	/**
	 * @Title: updateContractApproved
	 * @Description: 启动变更结算计划流程时，修改合同的状态审批通过
	 * @param contractId
	 * @return
	 */
	int updateContractApproved(int contractId);
	/**
	 * 
	 * @Title: updateContractApproving
	 * @Description: 修改合同的审批状态 为审批中 suffix=App
	 * @param contract suffix
	 * @return
	 */
	int updateContractApprovingWithSuffix(Contract contract,String suffix);
	/**
	 * @Title: updateContractApproving
	 * @Description: 修改合同的状态 为审批中 suffix=App
	 * @param contractId suffix
	 * @return
	 */
	int updateContractApprovingWithSuffix(int contractId,String suffix);
	/**
	 * 
	 * @Title: updateContractApproved
	 * @Description: 修改合同状态 为审批中通过suffix=App
	 * @param contract suffix
	 * @return
	 */
	int updateContractApprovedWithSuffix(Contract contract,String suffix);
	/**
	 * @Title: updateContractApproved
	 * @Description: 修改合同的状态 为审批中通过suffix=App
	 * @param contractId suffix
	 * @return
	 */
	int updateContractApprovedWithSuffix(int contractId,String suffix);
	/**
	 * @Title: checkContractCodeExisted
	 * @Description: 判断contractCode是否存在
	 * @param contractCode
	 * @return
	 */
	public boolean checkContractCodeExisted(String contractCode);
	/**
         * @Title:generateNewContractCode
         * @Description:生产成新的合同编号
         * @param prefix
         * @return String
         * @throws
         */
        String generateNewContractCode(String prefix);
        /**
         * @Title:startNullifyWorkflow
         * @Description:启动合同作废流程
         * @param contract
         * @param startWorkFlow
         * @return Map<String,Object>
         * @throws
         */
        Map<String,Object> startNullifyWorkflow( Contract contract ) throws Exception;
        /**
         * @Title:updateContractToVoided
         * @Description:作废合同
         * @param businessId
         * @param processInstId
         * @return boolean
         * @throws
         */
        boolean updateContractToVoided(String businessId,String processInstId);
        /**
         * @Title:updateNullifyProcInstId
         * @Description:更新作废流程实例id
         * @param contractId
         * @param nullifyProcInstId
         * @return int
         * @throws
         */
        int updateNullifyProcInstId(String contractId,String nullifyProcInstId); 
}
