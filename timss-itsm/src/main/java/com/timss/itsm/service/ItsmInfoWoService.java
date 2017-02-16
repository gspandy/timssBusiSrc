package com.timss.itsm.service;

import com.timss.itsm.bean.ItsmInfoWo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface ItsmInfoWoService extends ItsmBusinessPubService{

        /**
         * @description:查询详情
         * @author: 王中华
         * @createDate: 2016-11-2
         * @param id
         * @return:
         */
        ItsmInfoWo queryItsmInfoWoById(String id);
	
	/**
	 * @description:更新
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param ItsmInfoWo:
	 */
	void updateItsmInfoWo(ItsmInfoWo ItsmInfoWo)  throws Exception;
	
	/**
	 * @description:删除
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param infoWoId:
	 */
	void deleteItsmInfoWo(String infoWoId);
	
	/**
	 * @description:不启动工作流的插入
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param itsmInfoWo
	 * @return:
	 */
	int insertItsmInfoWo(ItsmInfoWo itsmInfoWo) throws Exception;
	
	/**
	 * @description:查询列表
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param page
	 * @return:
	 */
	Page<ItsmInfoWo> queryItsmInfoWoList(Page<ItsmInfoWo> page);
	
	/**
	 * @description:作废
	 * @author: 王中华
	 * @createDate: 2016-11-2
	 * @param infoWoId
	 * @return:
	 */
	int invalidWorkOrder(String infoWoId);

    /**
     * @description: 启动工作流的插入
     * @author: 王中华
     * @createDate: 2016-11-2
     * @param itsmInfoWo:
     */
    void insertItsmInfoWoWithFlow(ItsmInfoWo itsmInfoWo,UserInfoScope userInfoScope )  throws Exception ;

    void updateItsmInfoWoAndStartFlow(ItsmInfoWo itsmInfoWo,UserInfoScope userInfoScope) throws Exception ;

    /**
     * @description:暂存
     * @author: 王中华
     * @createDate: 2016-11-6
     * @param itsmInfoWo:
     */
    void saveItsmInfoWo(ItsmInfoWo itsmInfoWo,UserInfoScope userInfoScope) throws Exception;

    /**
     * @description:作废
     * @author: 王中华
     * @createDate: 2016-11-6
     * @param infoWoId:
     */
    void obsoleteItsmInfoWo(String infoWoId, UserInfoScope userInfoScope);

}
