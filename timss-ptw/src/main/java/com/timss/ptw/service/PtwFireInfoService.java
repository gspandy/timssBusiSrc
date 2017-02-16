package com.timss.ptw.service;

import java.util.Date;
import java.util.List;

import com.timss.ptw.bean.PtwFireInfo;

/**
 * 
 * @title: 动火相关信息Dao
 * @description: {desc}
 * @company: gdyd
 * @className: PtwFireInfoService.java
 * @author: 周保康
 * @createDate: 2014-8-5
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwFireInfoService {
    
    /**
     * 根据主表的工作票Id查询动火信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param wtId
     * @return:
     */
    PtwFireInfo queryPtwFireInfoByWtId(int wtId);
    
    /**
     * 查找工作票所有附加的动火票Id
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param wtId
     * @return:
     */
    List<Integer> queryFireIdsByAttachWtId(int attachWtId);
    
    /**
     * 插入一条动火基本信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:插入成功的条数
     */
    int insertPtwFireBaseInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新一条动火基本信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:更新成功的条数
     */
    int updatePtwFireBaseInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新工作票允许会签的流程信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFireFlowApprInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新工作票安全措施确认的流程信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFireFlowConfirmInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新工作票确认结束的流程信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFireFlowFinInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新动火票的动火工作示意图
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFirePic(PtwFireInfo ptwFireInfo);
    
    /**
     * 签发-安监负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireIssueAj(int wtId,String userId,String userName,Date date);
    
    /**
     * 签发-保卫负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireIssueGuard(int wtId,String userId,String userName,Date date);
    
    /**
     * 签发-厂级领导
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireIssueCj(int wtId,String userId,String userName,Date date);
    
    /**
     * 签发-动火单位负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireIssueBm(int wtId,String userId,String userName,Date date);
    
    /**
     * 签发-动火单位负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireIssueXf(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-工作负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireLicWpic(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-工作执行人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireLicWpExec(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-工作许可人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireLicAppr(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-保卫负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireLicGuard(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-保卫负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireLicXf(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-安监负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireLicAj(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-厂级负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireLicCj(int wtId,String userId,String userName,Date date);
    
    
    /**
     * 许可-工作负责人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireFinWpic(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-工作执行人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireFinWpExec(int wtId,String userId,String userName,Date date);
    
    /**
     * 许可-工作许可人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireFinAppr(int wtId,String userId,String userName,Date date);
    /**
     * 结束-消防监护人
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-2
     * @param wtId
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireFinXf(int wtId,String userId,String userName,Date date);
    
}
