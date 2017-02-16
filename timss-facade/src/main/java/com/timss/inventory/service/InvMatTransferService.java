package com.timss.inventory.service;

import java.util.List;
import java.util.Map;
import com.timss.inventory.bean.InvMatTransfer;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.vo.InvItemVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTransferService.java
 * @author: 890151
 * @createDate: 2016-1-7
 * @updateUser: 890151
 * @version: 1.0
 */
public interface InvMatTransferService {

    /**
     * @description:移库申请单列表数据
     * @author: 890151
     * @createDate: 2016-1-8
     * @return
     * @throws Exception :
     */
	Page<InvMatTransfer> queryInvMatTransferList(UserInfoScope userInfo, InvMatTransfer imt) throws Exception;

    /**
     * @description: 根据ID查询申请单
     * @author: 890151
     * @createDate: 2016-1-8
     * @return
     * @throws Exception :
     */
	InvMatTransfer queryInvMatTransferById(UserInfoScope userInfo, String imtId) throws Exception;

    /**
     * @description: 根据ID查询申请单
     * @author: 890151
     * @createDate: 2016-1-8
     * @return
     * @throws Exception :
     */
	List<InvMatTransferDetail> queryInvMatTransferDetailList(UserInfoScope userInfo, String imtId) throws Exception;

	/**
     * @description: 根据code查询申请单
     * @author: 890151
     * @createDate: 2016-1-8
     * @return
     * @throws Exception:
     */
	InvMatTransfer queryInvMatTransferByCode(UserInfoScope userInfo, String imtCode) throws Exception;

	
    /**
     * @description:暂存申请单和物资信息
     * @author: 890151
     * @createDate: 2016-1-8
     * @return
     * @throws Exception :
     */
    Map<String, Object> saveInvMatTransfer(UserInfoScope userInfoScope, String invMatTransferData, 
    		String invMatTransferDetailData, Map<String, Object> paramMap) throws Exception;

    /**
     * @description:提交申请单和物资信息
     * @author: 890151
     * @createDate: 2016-1-8
     * @return
     * @throws Exception :
     */
    Map<String, Object> commitInvMatTransfer(UserInfoScope userInfoScope, String invMatTransferData, 
    		String invMatTransferDetailData, Map<String, Object> paramMap) throws Exception;

    
	/**
	 * @description: 删除
	 * @author: 890151
	 * @createDate: 2016-1-8
	 * @param imtId
	 * @return
     * @throws Exception:
	 */
	int deleteInvMatTransfer(UserInfoScope userInfoScope, String imtId);
	
	/**
	 * @description: 作废
	 * @author: 890151
	 * @createDate: 2016-1-8
	 * @param woId:
	 */
	int obsoleteInvMatTransfer(UserInfoScope userInfoScope, String imtId);

	/**
	 * @description: 根据编号查询物资
	 * @author: 890151
	 * @createDate: 2016-1-8
	 */	
    List<InvItemVO> queryItemInfoToMatTransfer(UserInfoScope userInfo, Map<String, Object> paramMap) throws Exception;

}
