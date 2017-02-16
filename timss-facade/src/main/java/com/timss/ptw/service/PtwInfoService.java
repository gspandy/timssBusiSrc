package com.timss.ptw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.timss.ptw.bean.PtwFireInfo;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.vo.PtwInfoVoList;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 工作票相关接口
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoService.java
 * @author: 周保康
 * @createDate: 2014-6-27
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwInfoService {
    /**
     * 
     * @description:查询工作票列表的信息
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param page
     * @return:
     */
    Page<PtwInfoVoList> queryPtwInfoVoList(Page<PtwInfoVoList> page) throws Exception;
    
    /**
     * 
     * @description:查询历史票列表的信息
     * @author: zhuw
     * @createDate: 2016-6-21
     * @param page
     * @return:
     */
    Page<PtwInfoVoList> queryHisPtwList(Page<PtwInfoVoList> page) throws Exception;
    
    /**
     * @description:通过工单ID、站点ID查询所有相关的工作票
     * @author: 王中华
     * @createDate: 2015-12-17
     * @param woId
     * @param siteid
     * @return
     * @throws Exception:
     */
    List<PtwInfo> queryPtwInfoListByWoId(String woId, String siteid) throws Exception;
    
    /**
     * 查询工作票的基本信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param id
     * @return:PtwInfo
     */
    PtwInfo queryPtwInfoById(int id);
    
    /**
     * 
     * @description:增加一条工作票信息
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param ptwInfo
     * @param ptwTypeCode
     * @return:插入的行数
     */
    int insertPtwInfo(PtwInfo ptwInfo,String ptwTypeCode,String safeItems,PtwFireInfo ptwFireInfo);
    
    /**
     * 
     * @description:更新工作票状态信息
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param ptwInfo
     * @return:更新的行数
     */
    int updatePtwStatusInfo(PtwInfo ptwInfo,String password,Date modifyDate);
    
   /**
    * 
    * @description:更新工作票的基本信息
    * @author: 周保康
    * @createDate: 2014-8-7
    * @param ptwInfo
    * @param ptwTypeCode
    * @param ptwSafes
    * @param modifyDate
    * @param ptwFireInfo
    * @return:
    */
    int updatePtwBaseAndLicInfo(PtwInfo ptwInfo,String ptwTypeCode,String safeItems,Date modifyDate,PtwFireInfo ptwFireInfo);
    
    /**
     * 
     * @description:更新工作票签发信息
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param ptwInfo
     * @return:更新的行数
     */
    String updatePtwIssueInfo(PtwInfo ptwInfo,String password,Date modifyDate,boolean isEdit,String ptwTypeCode,String safeItems,PtwFireInfo ptwFireInfo)  throws Exception;
    
    /**
     * 更新工作票结束信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-30
     * @param ptwInfo
     * @param password
     * @param modifyDate
     * @return:
     */
    String updatePtwFinInfo(PtwInfo ptwInfo,String password,Date modifyDate) throws Exception;
    
    /**
     * 更新工作票许可信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-29
     * @param ptwInfo
     * @param password
     * @param modifyDate
     * @param ptwSafes
     * @return:
     */
    String updatePtwLicInfo(PtwInfo ptwInfo, String password, Date modifyDate,boolean isEdit,String ptwTypeCode,String safeItems,PtwFireInfo ptwFireInfo)  throws Exception;
    
    /**
     * 更新工作票终结信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-31
     * @param ptwInfo
     * @param password
     * @param modifyDate
     * @param ptwSafes
     * @return:
     */
    int updatePtwEndInfo(PtwInfo ptwInfo,String password,Date modifyDate,String safeItems);
    
    /**
     * 更新工作票的作废信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-1
     * @param ptwInfo
     * @param password
     * @param modifyDate
     * @return:
     */
    int updatePtwCancelInfo(PtwInfo ptwInfo,String password,Date modifyDate);
    
    
    /**
     * 根据工作票的编号和单位查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-8
     * @param ptwInfo
     * @return:
     */
    PtwInfo queryPtwInfoByNo(String wtNo,String siteId);
    
    /**
     * 更新备注
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param ptwId
     * @param remark
     * @return:
     */
    int updatePtwRemark(int ptwId,String remark);
    
    /**
     * 删除工作票
     * @param id
     * @return
     */
    int deletePtwInfo(int id);
    
    /**
     * 更新关联的钥匙箱号
     * @param id
     * @param relateKeyBoxId
     * @return
     */
    int updateRelateKeyBox(int id,String relateKeyBoxId);
    
    /**
     * 根据钥匙箱Id来查找对应的可用工作票
     * @param keyBoxId
     * @return
     */
    List<PtwInfo> queryByKeyBoxId(int keyBoxId);
    
    /**
     * 根据钥匙箱Id来查找对应的工作票
     * @param keyBoxId
     * @param status
     * @return
     */
	List<PtwInfo> queryByKeyBoxId(int keyBoxId,String status);
	
	/**
     * 查询指定状态下，含有关联钥匙箱的工作票
     * @param keyBoxId
     * @param status 用逗号分隔
     * @return
     */
     List<PtwInfo> queryByRelateKeyBoxId(int keyBoxId,String status);
	
	/**
	 * 校验工作票负责人是否能用
	 * 根据工作票规程 6.12 ： 一个工作负责人只能持有一张有效工作票，一个工作负责人不得在同一现场作业期间内担任两个及以上工作任务的工作负责人或工作组成员。
	 * 有效工作票，是指已履行许可手续，未办理结束手续、工作间断手续的工作票。
	 * @param userId 工作票负责人的Id
	 * @return {result,list}
	 */
	HashMap<String , Object> validWpicAvailable(String userId);
	
	/**
     * 更新动火票的审核信息
     * @description:
     * @author: 朱旺
     * @createDate: 2015-11-25
     * @param ptwInfo
     * @param password
     * @param modifyDate
     * @param ptwTypeCode
     * @return:
     */
    int updatePtwAuditInfo(PtwInfo ptwInfo,String password,Date modifyDate,String ptwTypeCode);
    
    /**
     * 更新工作票的审批信息
     * @description:
     * @author: 朱旺
     * @createDate: 2015-12-08
     * @param ptwInfo
     * @param password
     * @param modifyDate
     * @param ptwTypeCode
     * @return:
     */
    int updatePtwDepartAuditInfo(PtwInfo ptwInfo,String password,boolean isEdit,Date modifyDate,String ptwTypeCode,String safeItems);
}
