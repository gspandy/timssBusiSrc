package com.timss.attendance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 考勤异常
 * @description: {desc}
 * @company: gdyd
 * @className: AbnormityService.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface AbnormityService {
    /**
     * 
     * @description:通过Id查询考勤异常信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:Map<String, Object>
     */
    Map<String, Object> queryAbnormityById(int id);
    
    AbnormityBean queryAbnormityBeanById(int id);

    /**
     * 
     * @description:更新考勤异常信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    int updateAbnormity(AbnormityBean vo);

    /**
     * @description:通过考勤异常申请查询附件明细
     * @return:ArrayList<HashMap<String, Object>>
     */
    List<Map<String, Object>> queryFileByAbnormityId(Integer abnormityId);
    
    /**
     * 
     * @description:通过站点拿到所有考勤异常信息
     * @author: fengzt
     * @param pageVo 
     * @createDate: 2014年9月2日
     * @return:List<AbnormityBean>
     */
    List<AbnormityBean> queryAbnormityBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map
     * @param pageVo
     * @return:List<AbnormityBean>
     */
    List<AbnormityBean> queryAbnormityBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:终止流程
     * @author: fengzt
     * @param businessId 
     * @param message 
     * @param owner 
     * @param assignee 
     * @param taskId 
     * @createDate: 2014年9月3日
     * @return:int
     */
    int deleteFlowAbnormity(String taskId, String assignee, String owner, String message, String businessId);

    /**
     * 
     * @description:通过flowNo 
     * @author: fengzt
     * @createDate: 2014年10月15日
     * @param flowNo
     * @param siteId
     * @return:int
     */
    int queryIdByFlowNo(String flowNo, String siteId);

    /**
     * 
     * @description:删除 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    int deleteAbnormity(int id);

    /**
     * 
     * @description:作废 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    int invalidAbnormity(int id);

    /**
     * 
     * @description:查询开始时间、结束时间内的已归档考勤信息
     * @author: fengzt
     * @createDate: 2015年6月15日
     * @param siteId
     * @param startDate
     * @param endDate
     * @return:List<LeaveContainItemVo>
     */
    List<LeaveContainItemVo> queryAbnormityByDiffDay(String siteId,String userId, String startDate, String endDate);

    /**
     * 审批时更新状态
     * @return
     */
    Boolean updateAuditStatus(Integer abnormityId,String instanceId,String status);
    
    /**
     * 将收到的数据转成bean
     * @param formData
     * @param fileIds
     * @param addRows
     * @param delRows
     * @param updateRows
     * @return
     */
    AbnormityBean convertBean(String formData,String fileIds,String addRows,String delRows,String updateRows);
    
    /**
     * 新建或更新考勤异常单
     * @param bean
     * @return
     */
    Integer insertOrUpdateAbnormity(AbnormityBean bean);
    
    /**
     * 提交考勤异常单，进入流程
     * @param bean
     * @return
     */
    Map<String, Object> submitAbnormity(AbnormityBean bean);
    
    /**
     * 保存考勤异常单，用于暂存
     * @param bean
     * @return
     */
    Map<String, Object> saveAbnormity(AbnormityBean bean);
    
    /**
     * 更新考勤异常单的申请时间
     * 需传入id
     * createday为空会取当前时间
     * @param bean
     * @return
     */
    Integer updateAbnormityCreateDay(AbnormityBean bean);
}
