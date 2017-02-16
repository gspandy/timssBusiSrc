package com.timss.attendance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.AbnormityItemBean;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 考勤异常DAO
 * @description: {desc}
 * @company: gdyd
 * @className: AbnormityDao.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface AbnormityDao {

    /**
     * 
     * @description:考勤异常
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param abnormityBean
     * @return:int
     */
    public int insertAbnormity(AbnormityBean abnormityBean);
    /**
     * 
     * @description:删除 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    public int deleteAbnormity(int id);    
    /**
     * 
     * @description:通过Id查询考勤异常信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:AbnormityBean
     */
    public AbnormityBean queryAbnormityById(int id);
    /**
     * 查询考勤异常单，包括详情项
     * @param id
     * @return
     */
    AbnormityBean queryAbnormityByIdWithItems(@Param("id")int id);   
    /**
     * 
     * @description:更新考勤异常信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    public int updateAbnormity(AbnormityBean vo);
    /**
     * 更新考勤异常单的申请时间
     * @param bean
     * @return
     */
    Integer updateAbnormityCreateDay(AbnormityBean bean);    
    /**
     * 
     * @description:流程更新状态和当前处理人
     * @author: fengzt
     * @createDate: 2014年9月1日
     * @param parmas:
     */
    public int updateOperUserById(HashMap<String, Object> parmas);
    /**
     * 
     * @description:通过序列号查找bean
     * @author: fengzt
     * @createDate: 2014年10月15日
     * @param map
     * @return:AbnormityBean
     */
    public AbnormityBean queryAbnormityByNum(Map<String, Object> map); 
    
    /**
     * 
     * @description:通过站点查询考勤信息
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return: List<AbnormityBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
    public List<AbnormityBean> queryAbnormityBySiteId(Page<HashMap<?, ?>> pageVo);
    /**
     * 
     * @description:高级查询
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return:List<AbnormityBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
    public List<AbnormityBean> queryAbnormityBySearch(Page<HashMap<?, ?>> pageVo);
    /**
     * 
     * @description:查询开始时间、结束时间内的已归档考勤信息
     * @author: fengzt
     * @createDate: 2015年6月15日
     * @param params
     * @return:List<LeaveContainItemVo>
     * @update:yyn 20160119 sql优化且支持时间段为空
     */
    public List<LeaveContainItemVo> queryAbnormityByDiffDay(Map<String, Object> params);


    /**
     * 批量插入考勤异常详情
     * @param abnormityId
     * @param itemList
     * @return
     */
    int insertBatchAbnormityItem(@Param("abnormityId")Integer abnormityId,@Param("itemList")List<AbnormityItemBean>itemList);    
    /**
     * 批量更新考勤异常详情
     * @param itemList
     * @return 无返回值
     */
    int updateBatchAbnormityItem(@Param("itemList")List<AbnormityItemBean>itemList);  
    /**
     * 批量删除考勤异常详情
     * @param itemList
     * @return
     */
    int deleteBatchAbnormityItem(@Param("itemList")List<AbnormityItemBean>itemList);
    
    /**
     * 通过请假申请查询附件明细
     */
    List<String> queryFileByAbnormityId(@Param("abnormityId")Integer abnormityId);
    /**
     * 删除附件
     */
    int deleteFileByAbnormityId(@Param("abnormityId")Integer abnormityId);
    /**
     * 批量插入附件
     * @param list
     * @return
     */
    int insertBatchAbnormityFile(@Param("abnormityId")Integer abnormityId,@Param("fileIds")String[] fileIds);
    
}
