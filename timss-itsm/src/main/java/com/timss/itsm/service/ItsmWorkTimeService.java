package com.timss.itsm.service;

import com.timss.itsm.vo.ItsmWorkTimeVo;

/**
 * 
 * @title: 计算上班时间
 * @description: {desc}
 * @company: gdyd
 * @className: WorkTimeService.java
 * @author: fengzt
 * @createDate: 2014年12月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface ItsmWorkTimeService {

    /**
     * 
     * @description:计算工作日期差
     * @author: fengzt
     * @createDate: 2014年12月3日
     * @param workTimeVo
     * @return double 天数
     *                  --如果需要小时，自己乘上 工作小时
     * @throws Exception:
     */
    public double calDay( ItsmWorkTimeVo workTimeVo ) throws Exception ;
}
