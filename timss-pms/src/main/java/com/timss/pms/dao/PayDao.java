package com.timss.pms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.pms.bean.Pay;
import com.timss.pms.vo.PayDtlVo;
import com.timss.pms.vo.PayVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 付款收款操作dao
 * @ClassName:     PayDao
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-25 下午4:04:09
 */
public interface PayDao {
	/**
	 * @Title: insertPay
	 * @Description: 插入付款或者收款信息
	 * @param pay
	 * @return
	 */
	int insertPay(Pay pay);
	
	/**
	 * @Title: queryHasPayByContractId
	 * @Description: 根据合同id，查询已付款
	 * @return
	 */
	Double queryHasPayByContractId(int contractId);
	
	List<PayVo> queryPayListByPayplanId(Integer id);
	
	int updatePay(Pay pay);
	
	int deletePay(int id);
	
	PayDtlVo queryPayById(int id);
	
	/**
	 * @Title: updateSendedToERP
	 * @Description:更新结算记录发送erp凭证的状态
	 * @param updateSendedToERP
	 * @param id
	 * @return
	 */
	int updateSendedToERP(@Param("sendedtoerp")String sendedtoerp,@Param("id")String id);
	
        /**
	 * @Title: queryPayList
	 * @Description: 查询结算信息
	 * @param page 查询的条件
	 * @return: List<PayVo>  查询结果
	 * @throws
	 */
	List<PayVo> queryPayList(Page<PayVo> page);
	
	/**
	 * @Title: queryPayListAndFilter
	 * @Description: 查询结算信息并过滤
	 * @param page 查询的条件
	 * @return: List<PayVo>  查询结果
	 * @throws
	 */
	@RowFilter(flowIdColumn="flowid",exclusiveRule="PMS_EXCLUDE",exclusiveDeptRule="PMS_DEPT_EXCLUDE",isRouteFilter=true)
	List<PayVo> queryPayListAndFilter(Page<PayVo> page);
	
	/**
	 * @Title: queryPayListByProjectId
	 * @Description: 查询项目所属的结算信息
	 * @param page 查询的条件
	 * @return: List<PayVo>  查询结果
	 * @throws
	 */
	List<PayVo> queryPayListByProjectId(int projectId);
	
	/**
	 * @Title:queryPayByCondition
	 * @Description:根据条件查询结算信息
	 * @param pay
	 * @return List<PayVo>
	 * @throws
	 */
	List<PayVo> queryPayByCondition(Pay pay);
	
	/**
	 * @Title: selectByPaySpNoAndSiteid
         * @Description:根据结算编号和站点查询结算记录 
	 * @param paySpNo
	 * @param siteid
	 * @return
	 */
	int selectByPaySpNoAndSiteid(@Param("paySpNo")String paySpNo,@Param("siteid") String siteid);
	
	/**
         * @Title: queryPayListWithPaySpNoPrefix
         * @Description:根据结算编号前缀，查询结算列表
         * @param prefix
         * @return List<PayVo>
         * @throws
         */
        List<PayVo> queryPayListWithPaySpNoPrefix(@Param("prefix")String prefix);
}
