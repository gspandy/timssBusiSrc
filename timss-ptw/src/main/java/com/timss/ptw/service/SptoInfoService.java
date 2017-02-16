package com.timss.ptw.service;

import java.util.Date;
import java.util.Map;

import com.timss.ptw.bean.SptoInfo;
import com.timss.ptw.vo.SptoInfoVo;
import com.yudean.itc.dto.Page;

/***
 * 标准操作票 Service
 * 
 * @author gucw 2015-7-11
 */
public interface SptoInfoService {
    /***
     * @description:查询所有标准操作票
     * @author: 谷传伟
     * @createDate: 2015-7-2
     * @throws Exception
     */
    Page<SptoInfo> querySptoInfo(Page<SptoInfo> page) throws Exception;

    /**
     * @description: 添加/更新标准操作票
     * @author: 谷传伟
     * @createDate: 2015-7-9
     * @param sptoInfoVo
     * @param startWorkFlow
     * @throws Exception
     */
    Map<String, Object> saveOrUpdateSptoInfo(SptoInfoVo sptoInfoVo, boolean startWorkFlow) throws Exception;
    
    /**
     * @Title:querySptoInfoById
     * @Description:查询某一条标准操作票
     * @param @param id
     * @param @return
     * @return Map<String,Object>
     * @throws
     */
    public Map<String, Object> querySptoInfoById(String id);
    /**
     * @Title:deleteSptoInfo
     * @Description:删除
     * @param @param id
     * @return void
     * @throws
     */
    public void deleteSptoInfo(String id);
    /**
     * @Title:obsoleteSptoInfo
     * @Description:作废
     * @param @param id
     * @return void
     * @throws
     */
    public void obsoleteSptoInfo(String id);

    /**
     * @description: 模糊查询，查询该站点下的编号一样的标准操作票
     * @author: 王中华
     * @createDate: 2016-4-14
     * @param page
     * @return:
     */
    Page<SptoInfoVo> querySptoInfoByCode(Page<SptoInfoVo> page);

    /**
     * @description:修改标准操作票的有效时间
     * @author: 王中华
     * @createDate: 2016-4-15
     * @param id
     * @param beginTime
     * @param endTime
     * @return:
     */
    int updateValidTime(String id, Date beginTime, Date endTime);

    /**
     * @description:检查设置的有效时间是否合法
     * @author: 王中华
     * @createDate: 2016-4-18
     * @param sptoCode
     * @param beginTime
     * @param endTime
     * @return:
     */
    boolean checkValidTimeData(String sptoId, String sptoCode, Date beginTime, Date endTime);

    /**
     * @description:查询同样编号的标准操作票信息
     * @author: 王中华
     * @createDate: 2016-4-18
     * @param sptoCode
     * @param siteid
     * @return:
     */
    Page<SptoInfoVo> querySameCodeSptoList(Page<SptoInfoVo> page);
}
