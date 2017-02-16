package com.timss.ptw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.ptw.bean.PtwStandardBean;
import com.timss.ptw.bean.PtwStdSafeBean;
import com.timss.ptw.vo.SptoInfoVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 标准工作票
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardService.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PtwStandardService {


    /**
     * 
     * @description:通过站点拿到所有标准工作票信息
     * @author: fengzt
     * @param pageVo 
     * @createDate: 2014年9月2日
     * @return:List<PtwStandardBean>
     */
    List<PtwStandardBean> queryPtwStandardBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    List<PtwStandardBean> queryPtwStandardBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:暂存 不包括流程
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param formData
     * @param safeItems
     * @return:Map
     */
    Map<String, Object> insertOrUpdatePtwStandard(String formData, String safeItems);

    /**
     * 
     * @description:更新标准工作票
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @param items
     * @return:int
     */
    int updatePtwStandard(PtwStandardBean bean, List<PtwStdSafeBean> items);

    /**
     * 
     * @description:插入标准工作票
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @param items
     * @return:int
     */
    int insertPtwStandard(PtwStandardBean bean, List<PtwStdSafeBean> items);

    /**
     * 
     * @description:保存 有流程 
     * @author: fengzt
     * @createDate: 2015年7月21日
     * @param formData
     * @param safeItems
     * @return:Map<String, Object>
     */
    Map<String, Object> insertPtwStandardAll(String formData, String safeItems);

    /**
     * 
     * @description:通过ID查询标准工作票基本信息
     * @author: fengzt
     * @createDate: 2015年7月21日
     * @param id
     * @return:PtwStandardBean
     */
    PtwStandardBean queryPtwStandardById(String id);

    /**
     * 
     * @description:通过标准工作票的ID查询安全措施
     * @author: fengzt
     * @createDate: 2015年7月21日
     * @param id
     * @return:List<PtwStdSafeBean>
     */
    List<PtwStdSafeBean> queryPtwStdSafeByWtId(String id);

    /**
     * 
     * @description:更新基础信息
     * @author: fengzt
     * @createDate: 2015年7月22日
     * @param bean
     * @return:int
     */
    int updatePtwStandardStatus(PtwStandardBean bean);

    /**
     * 
     * @description:更新标准工作票的信息 删除、过期、作废
     * @author: fengzt
     * @createDate: 2015年7月22日
     * @param id
     * @param flag
     * @return: int
     */
    int deletePtwStandardBaseInfo(String id, String flag);

    /**
     * 
     * @description:更具设备号查询树及其子节点
     * @author: fengzt
     * @createDate: 2015年7月24日
     * @param eqId
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    List<PtwStandardBean> queryPtwStandardByEqId(String eqId, Page<HashMap<?, ?>> pageVo)  throws Exception;

    /**
     * 
     * @description:根据树点击事件查询树及其子节点的标准工作票（已审批通过的）
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param eqId
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    List<PtwStandardBean> queryFinishPtwStandardByEqId(String eqId, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:标题查询（已审批通过的）
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param map
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    List<PtwStandardBean> queryFinishPtwStandardBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:按站点查询（已审批通过的）
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    List<PtwStandardBean> queryFinishPtwStandardBySiteId(Page<HashMap<?, ?>> pageVo);

    /**
     * @description:根据编号查询工作票基本信息
     * @author: 王中华
     * @createDate: 2016-4-20
     * @param page
     * @return:
     */
    Page<PtwStandardBean> querySptoInfoByMultiCode(Page<PtwStandardBean> page);

    /**
     * @description:获取当前站点下，同样编号的标准工作票的最大版本号，没有则返回0
     * @author: 王中华
     * @createDate: 2016-4-20
     * @param wtNo
     * @param siteId
     * @return:
     */
    int getMaxVersionByCode(String wtNo, String siteId);

    /**
     * @description: 查询同样编号的标准工作票记录
     * @author: 王中华
     * @createDate: 2016-4-20
     * @param page
     * @return:
     */
    Page<PtwStandardBean> querySameCodeSptwList(Page<PtwStandardBean> page);

    /**
     * @description:检查有效时间设置的合法性
     * @author: 王中华
     * @createDate: 2016-4-21
     * @param sptwId
     * @param wtNo
     * @param beginTime
     * @param endTime
     * @return:
     */
    boolean checkValidTimeData(String sptwId, String wtNo, Date beginTime, Date endTime);

    /**
     * @description: 更新有效时间
     * @author: 王中华
     * @createDate: 2016-4-21
     * @param id
     * @param beginTime
     * @param endTime
     * @return:
     */
    int updateValidTime(String id, Date beginTime, Date endTime);

    /**
     * @description: 使同编号的其他正在生效的标准票失效
     * @author: 王中华
     * @createDate: 2016-10-27
     * @param id 标准工作票id
     * @param wtNo 标准工作票编号
     * @param userId 用户id
     * @param siteId
     * @return:
     */
    int invalidateOtherSameCodeSptw(String id, String wtNo, String userId, String siteId);


}
