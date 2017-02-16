package com.timss.inventory.service.core;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.timss.inventory.bean.InvMatTranLog;
import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.dao.InvMatTranLogDao;
import com.timss.inventory.service.InvMatTranLogService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranLogServiceImpl.java
 * @author: 890151
 * @createDate: 2016-5-5
 * @updateUser: 890151
 * @version: 1.0
 */
@Service ( "InvMatTranLogServiceImpl" )
public class InvMatTranLogServiceImpl implements InvMatTranLogService {
    private static final Logger LOG = Logger.getLogger( InvMatTranLogServiceImpl.class );
	
    @Autowired
    private InvMatTranLogDao invMatTranLogDao;
    
    /**
     * @description:分页查询出库来源批次对应关系列表
     * @author: 890151
     * @createDate: 2016-5-5
     * @return
     * @throws Exception :
     */
	@Override
	public Page<InvMatTranLog> queryInvMatTranLogList(UserInfoScope userInfo, InvMatTranLog invMatTranLog) throws Exception {
        Page<InvMatTranLog> page = userInfo.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "userId", userInfo.getUserId() );
        page.setSortKey( "createdate" );
        page.setSortOrder( "desc" );
    	List<InvMatTranLog> invMatTranLogList = invMatTranLogDao.queryInvMatTranLogList(page);
        page.setResults( invMatTranLogList );
        return page;
	}

    /**
     * @description:保存一条出库来源批次对应关系
     * @author: 890151
     * @createDate: 2016-5-5
     * @return
     * @throws Exception :
     */
	@Override
	public int saveInvMatTranLog(UserInfoScope userInfoScope, InvMatTranLog invMatTranLog) throws Exception {
		return invMatTranLogDao.insertInvMatTranLog(invMatTranLog);
	}

    /**
     * @description:查询关联的批次信息
     * @author: 890151
     * @createDate: 2016-06-03
     * @return
     * @throws Exception :
     */
	@Override
	public Page<InvMatTranRec> queryRelateTranRecByLog(UserInfoScope userInfo, InvMatTranLog imtl) throws Exception {
        Page<InvMatTranRec> page = userInfo.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "imtdId", imtl.getImtdId() );
        page.setSortKey( "createdate" );
        page.setSortOrder( "asc" );
    	List<InvMatTranRec> invMatTranRecList = invMatTranLogDao.queryRelateTranRecByLog(page);
        page.setResults( invMatTranRecList );
        return page;		
	}

}
