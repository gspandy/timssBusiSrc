package com.timss.ptw.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtoRelateUserInfo;
import com.timss.ptw.bean.SptoInfo;
import com.timss.ptw.bean.PtoOperItem;
import com.timss.ptw.vo.SptoInfoVo;
import com.yudean.itc.dto.Page;

/**
 * @title: 标准操作票Dao
 * @description: {desc}
 * @company: gdyd
 * @className: SptoInfoDao.java
 * @author: gucw
 * @createDate: 2015-7-2
 * @updateUser:
 * @version: 1.0
 */
public interface SptoInfoDao {
    /**
     * @description:查询所有标准操作票
     * @author: gucw
     * @createDate: 2015-7-2
     * @param page
     * @return:
     */
    List<SptoInfo> querySptoInfo(Page<SptoInfo> page);

    SptoInfoVo querySptoInfoById(@Param("id")String id,@Param("condition")Map<String,String> condition);

    List<PtoOperItem> querySptoItemsBySptoId(String id);

    int insertSptoInfo(SptoInfoVo sptoInfoVo);

    int updateSptoInfo(@Param("sptoInfoVo") SptoInfoVo sptoInfoVo, @Param("params") String[] params);

    int insertSptoItem(PtoOperItem item);

    int deleteSptoItem(String id);
    
    int insertSptoRelateUser(PtoRelateUserInfo ptoRelateUserInfo);
    
    int deleteSptoRelateUser(PtoRelateUserInfo ptoRelateUserInfo);

    List<SptoInfoVo> querySptoInfoByCode(Page<SptoInfoVo> page);
    
    List<SptoInfoVo> querySptoInfoListByCode(@Param("id") String sptoId,@Param("siteid") String siteid,@Param("code") String sptoCode);

    int updateValidTime(@Param("id")String id, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    List<SptoInfoVo> querySameCodeSptoListByCode(Page<SptoInfoVo> page);

    /**
     * @description: 查询同样编号的最大版本号
     * @author: 王中华
     * @createDate: 2016-4-19
     * @param code
     * @param siteId
     * @return:
     */
    int getMaxVersionByCode(@Param("code")String code, @Param("siteid")String siteid);

    SptoInfoVo querySptoMainInfoById(@Param("id")String id,@Param("siteid")String siteid);

    /**
     * @description:使同编号的其他标准操作票失效
     * @author: 王中华
     * @createDate: 2016-10-27
     * @param id
     * @param code
     * @param userId
     * @param siteId:
     */
    void invalidateOtherSameCodeSpto(@Param("id")String id, @Param("code")String code,
                    @Param("userId")String userId, @Param("siteid")String siteId, @Param("nowTime")Date nowTime);

   

}
