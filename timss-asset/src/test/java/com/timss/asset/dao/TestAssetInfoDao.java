package com.timss.asset.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.asset.bean.AssetBean;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class TestAssetInfoDao extends TestUnit {
	static private Logger log = Logger.getLogger(TestAssetInfoDao.class);
	
	@Autowired
	private AssetInfoDao dao;
	    
    @Test
    public void testQueryChildrenNode() {
        List<AssetBean> list=dao.queryChildrenNode("AST-00000228",null);
        log.debug("TestAssetInfoDao.testQueryChildrenNode():id->AST-00000228 size->"+list.size());
    }
}
