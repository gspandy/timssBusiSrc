package com.timss.asset.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.asset.bean.AstTemplateBean;
import com.timss.asset.dao.AstTemplateDao;
import com.timss.asset.service.AstTemplateService;


@Service
public class AstTemplateServiceImpl implements AstTemplateService{
	@Autowired
	private AstTemplateDao astTemplateDao ;
	@Override
	public List<AstTemplateBean> getAllDataList(){
	List<AstTemplateBean> as=astTemplateDao.getAllDataList();
	return as;
	}
	@Override
	public List<AstTemplateBean> getDataList(String typeName,String siteId){
		List<AstTemplateBean> bs=astTemplateDao.getDataList(typeName,siteId);
		return bs;
	}
	
	@Override
	public int insertAstTemplate(AstTemplateBean ast){
		int a = astTemplateDao.insertAstTemplate(ast);
		return a;
	}
	@Override
	public int deleteAstTemplate(int id){
		int b = astTemplateDao.deleteAstTemplate(id);
		return b;
	}
	@Override
	public int updateAstTemplate(AstTemplateBean as){
		int c = astTemplateDao.updateAstTemplate(as);
		return c;
	}
}