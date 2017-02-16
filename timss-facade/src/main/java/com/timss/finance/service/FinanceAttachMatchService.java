package com.timss.finance.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinanceAttachMatch;
import com.timss.finance.bean.FinanceMain;

/**
 * 
 * @title: 值别Service
 * @description: 
 * @company: gdyd
 * @className: FinanceMainService.java
 * @author: wus
 * @createDate: 2014年7月1日
 * @version: 1.0
 */
public interface FinanceAttachMatchService {
	
	
    /**
     * 
     * @description:insert结果中返回主键方法
     * @author: wus
     * @createDate: 2014年7月1日
     * @param fid  其中id为自增，不需要设置
     * @return:FinanceAttachMatch
     */
	void insertFinanceAttachMatch(String fid,String fieldids);

	 /**
		 * 
		 * @description:查询结果中返回FinanceAttachMatch的方法
		 * @author: wus
		 * @createDate: 2014年6月23日
		 * @param 
		 * @return:ArrayList<HashMap<String,Object>>
		 * @ 
		 */
	List<Map<String,Object>> queryFinanceAttachMatchByFid(String fid) ;
	
	 /**
		 * 插入
		 * 
		 * @description:
		 * @author: wus
		 * @createDate: 2014-7-2
		 * @param page
		 * @param String
		 * @return:
		 */
	 
	 void insertAttachmentToServer(String[] ids) ;
	 
	 
	 
	 /**
		 * @description:insert探亲路费个人报销流程结果中返回主键方法
		 * @author: 吴圣
		 * @createDate: 2014年7月8日
		 * @param 
		 * @return:Map
		 * @
		 * */
		void getUploadFileids() ;
		
	
	/**
	 * 更新附件信息
	 */
    boolean updateFinanceAttachMatch(String fid,String attachIds);
    
    /**
     * 删除某个报销单的附件
     * @Title: deleteFinanceAttachMatch
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param fid 报销单id
     * @return
     */
    boolean deleteFinanceAttachMatch(String fid);
}
