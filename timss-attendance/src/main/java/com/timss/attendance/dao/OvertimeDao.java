package com.timss.attendance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.bean.OvertimeFileBean;
import com.timss.attendance.bean.OvertimeItemBean;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 加班申请DAO
 * @description: {desc}
 * @company: gdyd
 * @className: OvertimeDao.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface OvertimeDao {

    /**
     * 
     * @description:加班申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param overtimeBean
     * @return:int
     */
    public int insertOvertime(OvertimeBean overtimeBean);
    /**
     * 查询加班单，仅主表
     * @param id
     * @return
     */
    OvertimeBean queryOvertimeById(@Param("id")int id);
    /**
     * 查询加班单，包括详情项
     * @param id
     * @return
     */
    OvertimeBean queryOvertimeByIdWithItems(@Param("id")int id);   
    /**
     * 
     * @description:更新加班申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    public int updateOvertime(OvertimeBean vo);
    /**
     * 更新加班提交申请时间
     * @param vo
     * @return
     */
    int updateOvertimeCreateDay(OvertimeBean vo);   
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
     * @description:通过NUM查询OvertimeBean
     * @author: fengzt
     * @createDate: 2014年10月16日
     * @param map
     * @return:OvertimeBean
     */
    public OvertimeBean queryOvertimeByNum(Map<String, Object> map);
    /**
     * 
     * @description:删除基本信息
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    public int deleteOvertimeById(int id);
    
    /**
     * 
     * @description:通过站点查询考勤信息
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return: List<OvertimeBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
    public List<OvertimeBean> queryOvertimeBySiteId(Page<HashMap<?, ?>> pageVo);
    /**
     * 
     * @description:高级查询
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return:List<OvertimeBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
    public List<OvertimeBean> queryOvertimeBySearch(Page<HashMap<?, ?>> pageVo);
    
    /**
     * 
     * @description:插入加班明细
     * @author: fengzt
     * @createDate: 2014年9月10日
     * @param overtimeItemBean
     * @return:int
     */
    public int insertOvertimeItem(OvertimeItemBean overtimeItemBean);
    /**
     * 
     * @description:通过overtimeId 删除item明细
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param id
     * @return:int
     */
    public int deleteOvertimeItem(int id);
    /**
     * 用核定的加班时长更新核定转补休时长<br/>
     * 用于没有核定转补休时长的流程的站点正确统计补休时间
     * @param overtimeId
     * @return
     */
    int updateOvertimeItemTransferCompensateByRealOverHours(@Param("overtimeId")Integer overtimeId);    
    /**
     * 批量更新加班转补休时长
     * @param itemList
     * @return
     */
    int updateBatchOvertimeItemTransferCompensate(@Param("itemList")List<OvertimeItemBean>itemList);    
    /**
     * 用申请的加班时长更新核定加班时长<br/>
     * 用于没有核定加班时长的流程的站点正确统计补休时间
     * @param overtimeId
     * @return
     */
    int updateOvertimeItemRealOverHoursByPlanOverHours(@Param("overtimeId")Integer overtimeId);    
    /**
     * 批量更新实际加班时长
     * @param itemList
     * @return
     */
    int updateBatchOvertimeItemRealOverHours(@Param("itemList")List<OvertimeItemBean>itemList);    
    /**
     * 批量删除加班详情
     * @param itemList
     * @return
     */
    int deleteBatchOvertimeItem(@Param("itemList")List<OvertimeItemBean>itemList);
    /**
     * 批量插入加班详情
     * @param overtimeId
     * @param itemList
     * @return
     */
    int insertBatchOvertimeItem(@Param("overtimeId")Integer overtimeId,@Param("itemList")List<OvertimeItemBean>itemList);
    /**
     * 批量更新加班详情
     * @param itemList
     * @return
     */
    int updateBatchOvertimeItem(@Param("itemList")List<OvertimeItemBean>itemList);
    /**
     * 查询加班项转化成统一格式处理
     * @param params
     * @return
     */
    List<LeaveContainItemVo> queryOvertimeByDiffDay(Map<String, Object> params);
    
    /**
     * 根据文件id插入加班单附件
     * @param overtimeId
     * @param ids
     * @return
     */
    int insertBatchOvertimeFile(@Param("overtimeId")Integer overtimeId,@Param("ids")String[]ids);
    /**
     * 
     * @description:查找附件
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param leaveId
     * @return:List<OvertimeFileBean>
     */
    public List<OvertimeFileBean> queryFileByOvertimeId(String overtimeId);
    /**
     * 
     * @description:通过overtimeId 删除附件
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param id
     * @return:int
     */
    public int deleteOvertimeFile(int id);
    
    /**
     * 根据给定月份查询该月累计加班时长
     * @param userId 为空则统计整个站点
     * @param siteId
     * @param monthStr 格式如“2016-05”
     * @param statusList 申请单的状态列表
     * @return
     */
	Double queryOvertimeTotalHoursByMonth(@Param("userId")String userId, @Param("siteId")String siteId,
			@Param("monthStr")String monthStr,@Param("statusList")String[]statusList);
}
