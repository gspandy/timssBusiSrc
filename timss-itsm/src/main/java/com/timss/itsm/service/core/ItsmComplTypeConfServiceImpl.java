package com.timss.itsm.service.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmComplainTypeConf;
import com.timss.itsm.dao.ItsmComplainTypeConfDao;
import com.timss.itsm.service.ItsmComplainTypeConfService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class ItsmComplTypeConfServiceImpl implements ItsmComplainTypeConfService{

	@Autowired
	private ItsmComplainTypeConfDao complainTypeConfDao;
	@Autowired
	private ItcMvcService itcMvcService;
	
	private static final Logger LOG=Logger.getLogger(ItsmComplTypeConfServiceImpl.class);
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void insertComplainTypeConf(Map<String, String> addComplainTypeConfDataMap) {
		 UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		 String userId = userInfoScope.getUserId();
		 String siteid = userInfoScope.getSiteId();
		 
		String complainTypeConfForm = addComplainTypeConfDataMap.get("complainTypeConfForm");
		ItsmComplainTypeConf complainTypeConf;
		try {
			complainTypeConf=JsonHelper.toObject(complainTypeConfForm, ItsmComplainTypeConf.class);
			complainTypeConf.setCreatedate(new Date());
			complainTypeConf.setCreateuser(userId);
			complainTypeConf.setModifydate(new Date());
			complainTypeConf.setModifyuser(userId);
			complainTypeConf.setActive("Y");
			complainTypeConf.setSiteid(siteid);
			String id=complainTypeConf.getId();
			//判断id是否为空川，为空就设null，然后自动生成
			if("".equals(id)){
				complainTypeConf.setId(null);
			}
		} catch (Exception e) {
			 LOG.error( e.getMessage() );
	         throw new RuntimeException( e );
		}
		complainTypeConfDao.insertComplTypeConf(complainTypeConf);  //插入
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateComplainTypeConf(Map<String, String> addComplainTypeConfDataMap) {
		String complainTypeConfForm=addComplainTypeConfDataMap.get("complainTypeConfForm");
		ItsmComplainTypeConf complainTypeConf;
		try {
			complainTypeConf=JsonHelper.toObject(complainTypeConfForm, ItsmComplainTypeConf.class);
		}catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
		complainTypeConfDao.updateComplTypeConf(complainTypeConf);
	}


	@Override
	public ItsmComplainTypeConf queryComplainTypeConfById(String id) {
		return complainTypeConfDao.queryComplTypeConfById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteComplainTypeConfById(String id) {
		complainTypeConfDao.deleteComplTypeConf(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public Page<ItsmComplainTypeConf> queryComplainTypeConfList(Page<ItsmComplainTypeConf> page) {
		List<ItsmComplainTypeConf> ret=complainTypeConfDao.queryComplTypeConfList(page);
		page.setResults(ret);
		 LOG.info( "查询投诉类别列表信息" );
		return page;
	}

}
