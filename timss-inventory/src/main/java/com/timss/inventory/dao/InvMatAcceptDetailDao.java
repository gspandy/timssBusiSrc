package com.timss.inventory.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.vo.InvMatAcceptDetailVO;

/**
 * @title: 物资验收详细信息dao
 * @description: 物资验收详细信息dao
 * @company: gdyd
 * @className: InvMatAcceptDetailDao.java
 * @author: yuanzh
 * @createDate: 2015-10-30
 * @updateUser: yuanzh
 * @version: 1.0
 */
public interface InvMatAcceptDetailDao {

    /**
     * @description:根据采购合同查询到验收记录明细
     * @author: yuanzh
     * @createDate: 2015-10-30
     * @param siteId
     * @param poId
     * @return:
     */
    List<InvMatAcceptDetailVO> queryItem2InvMatAcceptDetail(@Param("siteId") String siteId, @Param("poId") String poId);
    
    /**
     * 根据物资id，删除对应物资记录。
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param inacdId
     * @return:
     */
    int deleteByPrimaryKey(String inacdId);
    
    /**
     * 根据物资申请id，删除对应物资记录。
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param inacdId
     * @return:
     */
    int deleteByInacId(String inacId);

    /**
     * 插入物资记录
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int insert(InvMatAcceptDetail record);
    
    /**
     * 插入物资记录，空值不插入
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int insertSelective(InvMatAcceptDetail record);

    /**
     * 查询物资id对应记录
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param inacdId
     * @return:
     */
    InvMatAcceptDetail queryByPrimaryKey(String inacdId);

    /**
     * 更新物资记录信息，空值不更新
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int updateByPrimaryKeySelective(InvMatAcceptDetail record);

    /**
     * 更新物资记录，空值更新
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int updateByPrimaryKey(InvMatAcceptDetail record);
    
    /**
     * 根据物资详情，模糊搜索记录
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param record
     * @return:
     */
    List<InvMatAcceptDetailVO> queryInvMatAcceptDetailList(InvMatAcceptDetail record);
}
