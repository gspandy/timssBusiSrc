package com.timss.asset.service;

import java.util.List;

import com.timss.asset.bean.AstTemplateBean;



public interface AstTemplateService {
	public List<AstTemplateBean> getAllDataList();
	public List<AstTemplateBean> getDataList(String typeName,String siteId);
	public int insertAstTemplate(AstTemplateBean as);
	public int deleteAstTemplate(int id);
	public int updateAstTemplate(AstTemplateBean ast);
}
