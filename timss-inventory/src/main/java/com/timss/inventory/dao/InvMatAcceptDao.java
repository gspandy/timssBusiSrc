package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.vo.InvMatAcceptVo;
import com.timss.inventory.vo.MTPurOrderVO;
import com.yudean.itc.dto.Page;


public interface InvMatAcceptDao {
	/**
	 * 逻辑删除物资接收记录
	 * @description:
	 * @author: 890145
	 * @createDate: 2015-10-30
	 * @param inacId
	 * @return:
	 */
    int deleteByPrimaryKey(String inacId);

    /**
     * 插入物资验收记录，如果存在空字段，则该字段也进行插入
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int insert(InvMatAccept record);
    /**
     * 插入物资验收记录，如果存在空字段，则该字段无插入
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int insertSelective(InvMatAccept record);

    /**
     * 根据物资验收编码，查询物资接收记录
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param inacId
     * @return:
     */
    InvMatAccept queryByPrimaryKey(String inacId);

    /**
     * 更新物资验收记录，不更新空值
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int updateByPrimaryKeySelective(InvMatAccept record);
    /**
     * 更新物资验收记录，更新空值
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    int updateByPrimaryKey(InvMatAccept record);
    
    /**
     * 根据mataccept值查询，模糊搜索对应验收单记录
     * @description:
     * @author: 890145
     * @createDate: 2015-10-30
     * @param record
     * @return:
     */
    List<InvMatAccept> queryListByMatAccept(InvMatAccept record);
    
    
    /**
     * @description:查询物资验收列表，包含一些特殊物资信息
     * @author: 890166
     * @createDate: 2014-7-23
     * @param page
     * @return:
     */
    List<InvMatAcceptVo> queryInvMatAcceptList(Page<?> page);
    
    /**
     * @description:获取采购单号列表方法
     * @author: 890166
     * @createDate: 2014-7-23
     * @param page
     * @return:
     */
    List<MTPurOrderVO> queryPurOrderList(Page<?> page);
    
    /**
     * @description:通过flowNo查询到sheetid
     * @author: 890151
     * @createDate: 2016-8-8
     * @param sheetNo
     * @param siteId
     * @return:
     */
    String queryInvMatAcceptIdByFlowNo(Map<String, Object> map);

    /**
     * 更新是否为即收即发
     * @description:
     * @author: 890151
     * @createDate: 2016-12-5
     * @param invMatAccept
     */
    int updateAutoDelivery(InvMatAccept invMatAccept);
    
}