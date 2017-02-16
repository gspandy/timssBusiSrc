package com.timss.operation.service;

import java.util.List;

import com.timss.operation.bean.ModeBean;
import com.timss.operation.vo.ModeAssetVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 运行方式设置service
 * @description: {desc}
 * @company: gdyd
 * @className: ModeService.java
 * @author: fengzt
 * @createDate: 2015年10月29日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface ModeService {


    /**
     * 
     * @description:运行方式设置分页
     * @author: fengzt
     * @createDate: 2015年10月29
     * @param pageVo
     * @return:
     */
    Page<ModeBean> queryAllModeList(Page<ModeBean> paramsPage);

    /**
     * 
     * @description:通过ID查询运行方式设置
     * @author: fengzt
     * @createDate: 2015年10月29
     * @param id
     * @return:
     */
    ModeBean queryModeById(String id);
    
    /**
     * 
     * @description:更新运行方式设置
     * @author: fengzt
     * @createDate: 2015年10月29
     * @param mode
     * @return:
     */
    int updateMode(ModeBean mode);
    
    /**
     * 保存运行方式的值
     * @param jobId
     * @param team
     * @param assetId
     * @param val
     * @return
     */
    int updateNowModeVal(Integer jobId,String team,String assetId,String val);
    
    /**
     * 
     * @description:删除运行方式设置
     * @author: fengzt
     * @createDate: 2015年10月29
     * @param id
     * @return:
     */
    int deleteModeById(String id);

    /**
     * 
     * @description:通过jobId查询ModeAsset
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param jobId
     * @param team 
     * @return:List<ModeAssetVo>
     */
    List<ModeAssetVo> queryModeAssetByJobId(int jobId, String team);

    /**
     * 
     * @description:通过设备ID查询设备明细
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param assetId
     * @return:ModeAssetVo
     */
    ModeAssetVo queryModeAssetByAssetId(String assetId);

    /**
     * 
     * @description:插入or更新
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param modeAssetVos
     * @return:int
     */
    int insertOrUpdateMode(List<ModeAssetVo> modeAssetVos,Integer jobId);

    /**
     * 
     * @description:通过工种查找Mode分组信息
     * @author: fengzt
     * @createDate: 2015年11月11日
     * @param jobsId
     * @return:List<String>
     */
    List<String> queryModeTeamByJobsId(int jobsId);

}
