package com.timss.inventory.service.core;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.vo.InvMatAcceptDtlVo;
import com.timss.inventory.vo.InvMatAcceptVo;
import com.timss.inventory.vo.MTPurOrderVO;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 物资验收service接口
 * 
 * @company: gdyd
 * @className: InvAcceptionService.java
 * @author: 890145
 * @createDate: 2015-10-30
 * @updateUser: 890145
 * @version: 1.0
 */
public interface InvMatAcceptService {
    /**
     * 插入验收申请记录，第一次暂存或者提交调用，根据标志位决定是否生成流程，生成流程时，需要判断是否有多个验收申请人，
     * 如果有多个需要进行多个流程的生产工作。
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept 验收申请信息
     * @param invMatAcceptDetails 物资列表信息
     * @param startWorkflow 是否生成流程
     * @return:
     */
    public Map< String , Object > insertInvMatAccept ( InvMatAccept invMatAccept ,
	    List< InvMatAcceptDetail > invMatAcceptDetails , boolean startWorkflow );

    /**
     * 更新验收记录信息，暂存或者提交调用，根据标志位决定是否生成流程，生成流程时，需要判断是否有多个验收申请人，如果有多个需要进行多个流程的生产工作.
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept
     * @param invMatAcceptDetails
     * @param startWorkflow
     * @return:
     */
    public Map< String , Object > updateInvMatAccept ( InvMatAccept invMatAccept ,
	    List< InvMatAcceptDetail > invMatAcceptDetails , boolean startWorkflow );

    /**
     * 作废验收申请记录
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param inacId
     * @return:
     */
    public boolean voidFlow ( String inacId , String message );

    /**
     * 更新验收申请信息。
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept
     * @return:
     */
    public boolean updateInvMatAccept ( InvMatAccept invMatAccept );

    /**
     * 逻辑删除验收申请记录
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param inacId
     * @return:
     */
    public boolean deleteInvMatAccept ( String inacId );

    /**
     * 根据验收申请id，查询验收申请记录
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param inacId
     * @return:
     */
    public InvMatAcceptDtlVo queryInvMatAcceptById ( String inacId );

    /**
     * 根据验收申请id，查询验收申请基本信息
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param inacId
     * @return:
     */
    public InvMatAccept queryInvMatAcceptBasicInfoById ( String inacId );

    /**
     * 模糊搜索验收申请信息
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept
     * @return:
     */
    public Page< InvMatAcceptVo > queryInvMatAcceptList ( InvMatAcceptVo imVo , UserInfo userInfo );

    public Map< String , Object > queryWorkflowMap ( String inacId , String processId );

    /**
     * 物资入库
     */
    public boolean saveMatTran ( InvMatAccept invMatAccept , List< InvMatAcceptDetailVO > details , String checkUser ) throws Exception ;

    /**
     * 根据采购人拆分
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-6
     * @param invMatAccept
     * @param details
     * @return:
     */
    public boolean departInvMat ( InvMatAccept invMatAccept , List< InvMatAcceptDetail > details );

    public Page< MTPurOrderVO > queryPurOrderList ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "mtpo" ) MTPurOrderVO mtpo ) throws Exception;
    
    /**
     * @description: 通过sheetNo和站点id找到申请表id
     * @author: 890151
     * @createDate: 2016-8-8
     * @param sheetNo
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryInvMatAcceptIdByFlowNo ( String sheetNo , String siteId ) throws Exception;
    
    /**
     * @description: 判断物资是否属于自动发料类型，并获取相关联的第一条采购申请单名称
     * @author: 890151
     * @createDate: 2016-11-14
     * @param userInfo
     * @param imacId
     * @return
     * @throws Exception:
     */
    public Map<String, Object> getAutuDeleiveyInfo(UserInfoScope userInfo, String imacId) throws Exception;
}
