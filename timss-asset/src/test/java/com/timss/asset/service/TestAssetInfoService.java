package com.timss.asset.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.timss.asset.bean.AssetBean;
import com.timss.asset.dao.TestAssetInfoDao;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class TestAssetInfoService extends TestUnit {
	
	static private Logger log = Logger.getLogger(TestAssetInfoDao.class);
	
	@Autowired
    private AssetInfoService service;
	
	private AssetBean bean;
	
	@Before
	public void testInsertAsset() throws Exception{
		bean=new AssetBean();
		bean.setAssetName("用于测试的Asset");
		bean.setAssetType("location");
		bean.setParentId("AST-00009203");//挂到根节点
		bean.setSite("SBS");
		
		service.insertAssetInfo(bean);
		log.debug("TestAssetInfoService.testInsertAsset():name->"+bean.getAssetName()+" id->"+bean.getAssetId());
		
		//添加子节点
		AssetBean child=new AssetBean();
		child.setAssetName("用于测试的Asset的子节点");
		child.setAssetType("location");
		child.setParentId(bean.getAssetId());//挂到父节点
		child.setSite("SBS");
		
		service.insertAssetInfo(child);
		log.debug("TestAssetInfoService.testInsertAsset() child:name->"+child.getAssetName()+" id->"+child.getAssetId());
	}
	
    @After
    public void testDeleteAsset() throws Exception {
    	int size=service.deleteAsset(bean.getAssetId());
    	log.debug("TestAssetInfoService.testInsertAsset():name->"+bean.getAssetName()+" id->"+bean.getAssetId()+" 共"+size+"个");
    }
    
    @Test
    public void testQueryAssetForHint() throws Exception{
    	String site="SBS";
    	String kw="用于测试的Asset的子节点";
    	List<Map<String,Object>>result=service.queryAssetForHint(site, kw);
    	log.debug("TestAssetInfoService.testQueryAssetForHint():kw->"+kw+" resultSize->"+result.size());
    }
}
