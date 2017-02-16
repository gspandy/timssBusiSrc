package com.timss.inventory.service.core;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.vo.InvMatAcceptDetailVO;

/**
 * 物资验收详情service类
 * 
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptDetailService.java
 * @author: 890145
 * @createDate: 2015-11-2
 * @updateUser: 890145
 * @version: 1.0
 */
public interface InvMatAcceptDetailService {
    /**
     * 插入验收申请物资记录，
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept 验收申请信息
     * @param invMatAcceptDetails 物资列表信息
     * @param startWorkflow 是否生成流程
     * @return:
     */
    public Map< String , Object > insertInvMatAcceptDetail ( InvMatAccept invMatAccept ,
	    List< InvMatAcceptDetail > invMatAcceptDetails );

    /**
     * 更新验收申请物资信息，
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept
     * @param invMatAcceptDetails
     * @param startWorkflow
     * @return:
     */

    public Map< String , Object > updateInvMatAcceptDetail ( InvMatAccept invMatAccept ,
	    List< InvMatAcceptDetail > invMatAcceptDetails );

    /**
     * 根据验收申请id，删除对应物资记录
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param inacId
     * @return:
     */
    public int deleteInvMatAcceptDetailByInacId ( String inacId );

    /**
     * 根据验收申请id，查询对应的物资记录
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param inacId
     * @return:
     */
    public List< InvMatAcceptDetailVO > queryInvMatAcceptDetailListByInacId ( String inacId );
}
