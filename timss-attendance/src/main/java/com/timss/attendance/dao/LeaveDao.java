package com.timss.attendance.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveFileBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.ShiftOprVo;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 请假申请
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveDao.java
 * @author: fengzt
 * @createDate: 2014年9月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface LeaveDao {

    /**
     * 
     * @description:请假申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param leaveBean
     * @return:int
     */
    public int insertLeave(LeaveBean leaveBean);

    /**
     * 
     * @description:通过Id查询请假申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:LeaveBean
     */
    public LeaveBean queryLeaveById(int id);

    /**
     * 
     * @description:更新请假申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    public int updateLeave(LeaveBean vo);

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
     * @description:通过站点查询考勤信息
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return: List<LeaveBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
    public List<LeaveBean> queryLeaveBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:高级查询
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
    public List<LeaveBean> queryLeaveBySearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:部门经理
     * @author: fengzt
     * @createDate: 2014年9月3日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    public List<LeaveBean> queryLeaveByDept(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:个人查询请假申请
     * @author: fengzt
     * @createDate: 2014年9月3日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    public List<LeaveBean> queryLeaveByUser(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:部门经理高级查询
     * @author: fengzt
     * @createDate: 2014年9月3日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    public List<LeaveBean> queryLeaveByDeptSearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:用户高级查询
     * @author: fengzt
     * @createDate: 2014年9月3日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    public List<LeaveBean> queryLeaveByUserSearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:批量插入请假附件
     * @author: fengzt
     * @createDate: 2014年9月5日
     * @param leaveFileBeans
     * @return:int
     */
    public int insertBatchLeaveFile(List<LeaveFileBean> list);

    /**
     * 
     * @description:批量插入请假明细
     * @author: fengzt
     * @createDate: 2014年9月5日
     * @param leaveItemBeans
     * @return:int
     */
    public int insertBatchLeaveItem(List<LeaveItemBean> list);

    /**
     * 
     * @description:通过请假申请查询明细
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param leaveId
     * @return:List<LeaveItemBean>
     */
    public List<LeaveItemBean> queryLeaveItemList(String leaveId);

    /**
     * 
     * @description:通过请假申请查询附件明细
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param leaveId
     * @return:List<LeaveItemBean>
     */
    public List<LeaveFileBean> queryFileByLeaveId(String leaveId);

    /**
     * 
     * @description:删除附件
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param leaveId
     * @return:int
     */
    public int deleteLeaveItemByLeaveId(int leaveId);

    /**
     * 
     * @description:通过请假主表删除附件
     * @author: fengzt
     * @createDate: 2014年9月12日
     * @param leaveId
     * @return:int
     */
    public int deleteFileByLeaveId(int leaveId);

    /**
     * 
     * @description:通过num查找请假申请主信息
     * @author: fengzt
     * @createDate: 2014年10月16日
     * @param map
     * @return:LeaveBean
     */
    public LeaveBean queryLeaveByNum(Map<String, Object> map);

    /**
     * 
     * @description:删除请假申请基本信息
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    public int deleteLeaveById(int id);

    /**
     * 
     * @description:通过月份查询请假申请（不含草稿和提交申请阶段）
     * @author: fengzt
     * @createDate: 2015年2月15日
     * @param params
     * @return:List<StatVo>
     */
    @RowFilter(flowIdColumn="remark1",exclusiveRule="ATD_EXCLUDE")
    public List<StatVo> queryLeaveByMonth(Map<String, Object> params);

    /**
     * 
     * @description:查询班组人员值班的班次
     * @author: fengzt
     * @createDate: 2015年3月30日
     * @param params
     * @return:List<ShiftOprVo>
     */
    public List<ShiftOprVo> queryShiftOprByUser(Map<String, Object> params);

    /**
     * 
     * @description:请假申请跨月审批列表
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE")
    public List<LeaveBean> queryExceptionLeaveList(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:请假申请跨月审批列表-查询
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    @RowFilter(flowIdColumn="NUM",exclusiveRule="ATD_EXCLUDE")
    public List<LeaveBean> queryExceptionLeaveListBySearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:查询时间区域内请假信息(已归档)
     * @author: fengzt
     * @createDate: 2015年6月12日
     * @param params
     * @return:
     * @update:yyn 20160119 sql优化且支持时间段为空
     */
    public List<LeaveContainItemVo> queryLeaveByDiffDay(Map<String, Object> params);

    /**
     * Name    根据申请人orgId,deptName返回该站点下所有部门的orgId和org_code
     * @author 890205 2016年12月5日
     * @param  Map包含申请人的OrgId和deptName
     * @return 
     */
    public List<Map<String,String>> queryAllDeparts(Map<String,String> map); 
}
