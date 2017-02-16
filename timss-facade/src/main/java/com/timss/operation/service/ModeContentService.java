package com.timss.operation.service;

import java.util.List;
import com.timss.operation.vo.ModeContentVo;
import com.timss.operation.vo.NoteBaseVo;

/**
 * 
 * @title: 运行方式
 * @description: {desc}
 * @company: gdyd
 * @className: ModeContentService.java
 * @author: fengzt
 * @createDate: 2015年11月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface ModeContentService {
    
    /**
     * 保存交接班时的运行方式的值
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @return
     */
    public int insertOrUpdateModeContentFromMode( int dutyId,int jobsId, int handoverId);
    
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
    public List<ModeContentVo> queryModeContentByDutyJobsHandover(int dutyId, int jobsId, int handoverId, String team);

    /**
     * 
     * @description:保存动态表单temp函数
     * @author: fengzt
     * @createDate: 2015年11月5日
     * @param baseVo
     * @return:int
     */
    public int insertOrUpdateDynamicTemp(NoteBaseVo baseVo);

}
