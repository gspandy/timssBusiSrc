package com.timss.operation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.ModeContentBean;
import com.timss.operation.vo.ModeContentVo;
import com.timss.operation.vo.NoteBaseVo;
import com.yudean.itc.annotation.DynamicFormBind;

/**
 * 
 * @title: 运行方式
 * @description: {desc}
 * @company: gdyd
 * @className: ModeContentDao.java
 * @author: fengzt
 * @createDate: 2015年11月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface ModeContentDao {

    /**
     * 
     * @description:删除modeContent
     * @author: fengzt
     * @createDate: 2015年11月4日
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @param team 
     * @return:int
     */
    int deleteModeContent(@Param("dutyId") int dutyId, @Param("jobsId") int jobsId,
            @Param("handoverId") int handoverId, @Param("team") String team);

    /**
     * 
     * @description:批量插入ModeContentBean
     * @author: fengzt
     * @createDate: 2015年11月4日
     * @param list
     * @return:int
     */
    int insertBatchModeContent(List<ModeContentBean> list);

    /**
     * 
     * @description:通过 dutyId, jobsId handoverId 联合查询
     * @author: fengzt
     * @createDate: 2015年11月5日
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @param team 
     * @return:List<ModeContentVo>
     */
    List<ModeContentVo> queryModeContentByDutyJobsHandover(@Param("dutyId") int dutyId,
            @Param("jobsId") int jobsId, @Param("handoverId") int handoverId, @Param("team")String team);

    /**
     * 
     * @description:保存动态表单temp函数
     * @author: fengzt
     * @createDate: 2015年11月5日
     * @param baseVo
     * @return:int
     */
    @DynamicFormBind(masterKey="keyword")
    int insertOrUpdateDynamicTemp(NoteBaseVo baseVo);
}
