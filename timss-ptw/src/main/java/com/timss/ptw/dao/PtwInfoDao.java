package com.timss.ptw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.vo.PtwInfoVoList;
import com.yudean.itc.dto.Page;
/**
 * 
 * @title: 工作票信息数据库持久层
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoDao.java
 * @author: 周保康
 * @createDate: 2014-6-27
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwInfoDao {
    /**
     * 
     * @description:工作票列表详情页面的查询方法
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param page
     * @return:
     */
    List<PtwInfoVoList> queryPtwInfoVoList(Page<PtwInfoVoList> page);
    
    /**
     * 
     * @description:历史票列表查询
     * @author: zhuw
     * @createDate: 2016-6-21
     * @param page
     * @return:
     */
    List<PtwInfoVoList> queryHisPtwList(Page<PtwInfoVoList> page);
    
    List<PtwInfo> queryPtwInfoVoListByWoId(@Param("woId")String woId, @Param("siteId")String siteid);
    
    /**
     * 查询工作票的基本信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param id
     * @return:PtwInfo
     */
    PtwInfo queryPtwInfoById(int id);
    
    /**
     * 
     * @description:增加一条工作票信息
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param ptwInfo
     * @return:插入的行数
     */
    int insertPtwInfo(PtwInfo ptwInfo);
    
    /**
     * 
     * @description:更新工作票状态信息
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param ptwInfo
     * @return:更新的行数
     */
    int updatePtwStatusInfo(PtwInfo ptwInfo);
    
    /**
     * 
     * @description:更新工作票的基本信息
     * @author: 周保康
     * @createDate: 2014-7-29
     * @param ptwInfo
     * @return:
     */
    int updatePtwBaseInfo(PtwInfo ptwInfo);
    
    /**
     * 更新工作票的签发信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-28
     * @param ptwInfo
     * @return:
     */
    int updatePtwIssueInfo(PtwInfo ptwInfo);
    
    /**
     * 更新工作票的结束信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-30
     * @param ptwInfo
     * @return:
     */
    int updatePtwFinInfo(PtwInfo ptwInfo);
    
    /**
     * 更新工作票的终结信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-31
     * @param ptwInfo
     * @return:
     */
    int updatePtwEndInfo(PtwInfo ptwInfo);
    
    /**
     * 更新工作票的许可信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-28
     * @param ptwInfo
     * @return:
     */
    int updatePtwLicInfo(PtwInfo ptwInfo);
    
    /**
     * 更新工作票的作废信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-1
     * @return:
     */
    int updatePtwCancelInfo(PtwInfo ptwInfo );
    
    /**
     * 将工作票逻辑删除掉
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-11
     * @param ptwId
     * @return:
     */
    int updatePtwNotInUse(int ptwId );
    
    /**
     * 
     * @description:根据工作票ID删除
     * @author: 周保康
     * @createDate: 2014-6-27
     * @param id
     * @return:删除的行数
     */
    int deletePtwInfoById(int id);
    
    /**
     * 
     * @description:查询最大的编号
     * @author: 周保康
     * @createDate: 2014-8-8
     * @param ptwInfo
     * @return:
     */
    int queryMaxPtwNumOfYear(PtwInfo ptwInfo);
    
    /**
     * 
     * @description:根据工作票的编号和单位查询
     * @author: 周保康
     * @createDate: 2014-8-8
     * @param ptwInfo
     * @return:
     */
    PtwInfo queryPtwInfoByNo(PtwInfo ptwInfo);
    
    /**
     * 更新附加文件的信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param ptwInfo
     * @return:
     */
    int updatePtwAttachFiles(PtwInfo ptwInfo);
    
    /**
     * 更新备注
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-23
     * @param ptwInfo
     * @return:
     */
    int updatePtwRemark(PtwInfo ptwInfo);
    
    /**
     * 查找当前站点已签发的数目
     * @description:
     * @author: 周保康
     * @createDate: 2014-12-2
     * @param ptwInfo
     * @return:
     */
    int queryIssuedNumber(PtwInfo ptwInfo);
    
    /**
     * 更新工作票编号
     * @param ptwInfo
     * @return
     */
    int updatePtwNo(PtwInfo ptwInfo);
    
    /**
     * 更新关联钥匙箱号
     * @param id
     * @param relateKeyBoxId
     * @return
     */
    int updateRelateKeyBox(@Param("id")int id, @Param("relateKeyBoxId")String relateKeyBoxId);
    
    /**
    * 查询指定状态下的钥匙箱
    * @param keyBoxId
    * @param status 用逗号分隔
    * @return
    */
    List<PtwInfo> queryByKeyBoxId(@Param("keyBoxId")int keyBoxId,@Param("status") String status);
    
    /**
     * 查询指定状态下的工作票负责人的工作票
     * @param userId
     * @param status 用逗号分隔
     * @return
     */
     List<PtwInfo> queryPtwInfoByWpicAndStatus(@Param("userId")String userId,@Param("status") String status);
     
     /**
      * 查询指定状态下的关联钥匙箱
      * @param keyBoxId
      * @param status 用逗号分隔
      * @return
      */
      List<PtwInfo> queryByRelateKeyBoxId(@Param("keyBoxId")int keyBoxId,@Param("status") String status);
      
      /**
       * 更新工作票的外委签发信息
       * @description:
       * @author: 朱旺
       * @createDate: 2015-12-14 
       * @param ptwInfo
       * @return:
       */
      int updatePtwOutIssueInfo(PtwInfo ptwInfo);


    /**
     * @description:查找当前站点已创建的数目
     * @author: 王中华
     * @createDate: 2016-5-24
     * @param ptwInfo
     * @return:
     */
    int queryCreatedNumber(PtwInfo ptwInfo);
}
