package com.timss.ptw.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwStandardBean;
import com.timss.ptw.bean.PtwStdSafeBean;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 标准工作票DAO
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardDao.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PtwStandardDao {

    
    /**
     * 
     * @description:通过站点查询标准工作票
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return: List<PtwStandardBean>
     */
    public List<PtwStandardBean> queryPtwStandardBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:高级查询
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    public List<PtwStandardBean> queryPtwStandardBySearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:更新标准工作票基本信息
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @return:int
     */
    public int updatePtwStandard(PtwStandardBean bean);
    
    /**
     * 
     * @description:通过标准票ID删除安全措施子项
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param id
     * @return:int
     */
    public int deletePtwStdSafeByWtId(String id);

    /**
     * 
     * @description:批量插入安全措施
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param list -- List<PtwStdSafeBean>
     * @return:int
     */
    public int insertBacthPtwStdSafe(List<PtwStdSafeBean> list);

    /**
     * 
     * @description:插入标准票的基础信息
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @return:int
     */
    public int insertPtwStdBaseInfo(PtwStandardBean bean);

    /**
     * 
     * @description:通过ID查询标准票的基本信息
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param id
     * @return:PtwStandardBean
     */
    public PtwStandardBean queryPtwStandardById(String id);

    /**
     * 
     * @description:通过标准票ID查询安全措施
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param id
     * @return:List<PtwStdSafeBean>
     */
    public List<PtwStdSafeBean> queryPtwStdSafeByWtId(String id);

    /**
     * 
     * @description:更新基础信息
     * @author: fengzt
     * @createDate: 2015年7月22日
     * @param bean
     * @return:int
     */
    public int updatePtwStandardStatus(PtwStandardBean bean);

    /**
     * 
     * @description:更新标准工作票的信息 删除、过期、作废
     * @author: fengzt
     * @createDate: 2015年7月22日
     * @param map
     * @return:int
     */
    public int deletePtwStandardBaseInfo(Map<String, Object> map);

    /**
     * 
     * @description:查询设备号查询树及其子节点
     * @author: fengzt
     * @createDate: 2015年7月24日
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    public List<PtwStandardBean> queryPtwStandardByEqId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:查询设备号查询树及其子节点(审批状态为：已通过)
     * @author: fengzt
     * @createDate: 2015年7月24日
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    public List<PtwStandardBean> queryFinishPtwStandardByEqId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:高级查询(审批状态为：已通过)
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    public List<PtwStandardBean> queryFinishPtwStandardBySearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:查询站点(审批状态为：已通过)
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    public List<PtwStandardBean> queryFinishPtwStandardBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * @description:根据编号模糊查询
     * @author: 王中华
     * @createDate: 2016-4-20
     * @param page
     * @return:
     */
    public List<PtwStandardBean> querySptoInfoByMultiCode(Page<PtwStandardBean> page);

    /**
     * @description: 获取同样编号的最大版本号
     * @author: 王中华
     * @createDate: 2016-4-20
     * @param wtNo
     * @param siteid
     * @return:
     */
    public int getMaxVersionByCode(@Param("wtNo")String wtNo, @Param("siteid")String siteid);

    /**
     * @description:查询同样编号的标准工作票记录
     * @author: 王中华
     * @createDate: 2016-4-20
     * @param page
     * @return:
     */
    public List<PtwStandardBean> querySameCodeSptwListByCode(Page<PtwStandardBean> page);

    /**
     * @description: 查询所有同编号的标准工作票记录
     * @author: 王中华
     * @createDate: 2016-4-21
     * @param sptwId
     * @param siteid
     * @param wtNo
     * @return:
     */
    public List<PtwStandardBean> querySptwInfoListByCode(@Param("id")String id, @Param("siteid")String siteid, @Param("wtNo")String wtNo);

    /**
     * @description: 更新生效时间
     * @author: 王中华
     * @createDate: 2016-4-21
     * @param id
     * @param beginTime
     * @param endTime
     * @return:
     */
    public int updateValidTime(@Param("id")String id, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * @description:使同编号的其他正在生效的标准票失效
     * @author: 王中华
     * @createDate: 2016-10-27
     * @param id
     * @param wtNo
     * @param userId
     * @param siteId
     * @return:
     */
    public int invalidateOtherSameCodeSptw(@Param("id")String id, @Param("wtNo")String wtNo, 
            @Param("userId")String userId, @Param("siteId")String siteId,@Param("nowTime")Date nowTime);



}
