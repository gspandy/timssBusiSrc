package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.ModeBean;
import com.timss.operation.vo.ModeAssetVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: ModeMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: ModeMapper.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface ModeDao {
	
    /**
     * 
     * @description:更新运行方式设置表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param Mode:
     * @return int 更新个数
     */
    public int updateMode(ModeBean Mode);
    
    /**
     * 保存运行方式的值
     * @param Mode
     * @return
     */
    int updateNowModeVal(ModeBean Mode);
    
    /**
     * 
     * @description:通过Id拿到运行方式设置表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id
     * @return:Mode
     */
    public ModeBean queryModeById(String id);
    
    /**
     * 
     * @description:通过ID 删除 mode
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id:
     * @return 
     */
    public int deleteModeById(String id);

    /**
     * 
     * @description:mode 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<ModeBean> queryModeByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:查询运行方式设置列表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return: List<Mode>
     */
    public List<ModeBean> queryAllModeList(Page<ModeBean> paramsPage);

    /**
     * 
     * @description:通过jobId查询ModeAsset
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param jobId
     * @param team 
     * @return:List<ModeAssetVo>
     */
    public List<ModeAssetVo> queryModeAssetByJobId(@Param("jobId")int jobId, @Param("team")String team);

    /**
     * 
     * @description:通过设备ID查询设备明细
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param assetId
     * @return:ModeAssetVo
     */
    public ModeAssetVo queryModeAssetByAssetId(String assetId);

    /**
     * 
     * @description:批量插入Mode
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param modeList
     * @return:int
     */
    public int insertBatchMode(@Param("list") List<ModeBean> list);

    /**
     * 
     * @description:删除（非物理删除）
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param bean
     * @return:int
     */
    public int deleteMode(ModeBean bean);

    /**
     * 
     * @description:插入
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param bean
     * @return:int
     */
    public int insertMode(ModeBean bean);

    /**
     * 
     * @description:通过工种查找Mode分组信息
     * @author: fengzt
     * @createDate: 2015年11月11日
     * @param jobsId
     * @return:List<String>
     */
    public List<String> queryModeTeamByJobsId(int jobsId);

    
}
