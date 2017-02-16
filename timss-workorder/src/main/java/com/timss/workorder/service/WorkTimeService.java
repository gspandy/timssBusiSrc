package com.timss.workorder.service;

import com.timss.workorder.vo.WorkTimeVo;

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
public interface WorkTimeService {

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
    public double calDay( WorkTimeVo workTimeVo ) throws Exception ;
}
