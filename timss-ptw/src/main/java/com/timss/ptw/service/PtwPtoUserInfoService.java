package com.timss.ptw.service;

import java.util.List;
import java.util.Map;

import com.timss.ptw.bean.PtwPtoStepInfo;
import com.timss.ptw.bean.PtwPtoUserInfoConfig;
import com.timss.ptw.vo.PtwPtoUserInfoVo;
import com.yudean.itc.dto.Page;

/***
 * 标准操作票审批人员 Service
 * 
 * @author gucw 2015-7-21
 */
public interface PtwPtoUserInfoService {
    /***
     * @description:查询所有两票审批配置信息
     * @author: 谷传伟
     * @createDate: 2015-7-29
     * @throws Exception
     */
    public Page<PtwPtoUserInfoConfig> queryUserInfoConfig(Page<PtwPtoUserInfoConfig> page) throws Exception;
    /***
     * @description:查询两票审批配置是否冲突
     * @author: 谷传伟
     * @createDate: 2015-8-12
     * @throws Exception
     */
    public boolean isUserInfoConfigConflict(PtwPtoUserInfoConfig config) throws Exception;
    /**
     * @description: 添加/更新两票审批人员信息
     * @author: 谷传伟
     * @createDate: 2015-7-21
     * @param ptwPtoUserInfoVo
     * @throws Exception
     */
    public Map<String, Object> saveOrUpdateUserInfo(PtwPtoUserInfoVo ptwPtoUserInfoVo) throws Exception;
    
    /**
     * @Title:queryUserInfoByConfigId
     * @Description:查询某一两票审批人员信息
     * @param id
     * @return Map<String,Object>
     * @throws
     */
    public PtwPtoUserInfoVo queryUserInfoByConfigId(String id);
    /**
     * @Title:queryStepInfo
     * @Description:查询两票环节信息
     * @param id
     * @return Map<String,Object>
     * @throws
     */
    public List<PtwPtoStepInfo> queryStepInfo(PtwPtoStepInfo ptwPtoStepInfo);
    /**
     * @Title:deleteUserInfo
     * @Description:删除两票审批人员信息
     * @param id
     * @return void
     * @throws
     */
    public void deleteUserInfoByConfigId(String id);
    /**
     * @Title:queryRelatedOrgListByOrgCode
     * @Description:查询站点对应的组织机构
     * @param siteId
     * @return List
     * @throws
     */
    public List<String> queryRelatedOrgListBySiteId(String siteId);
}
