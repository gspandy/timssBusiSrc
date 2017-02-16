package com.timss.asset.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.bean.AstTemplateBean;



public interface AstTemplateDao {
	/**
	 * @author 890199
	 * @return List<AstTemplateBean>
	 */
	List<AstTemplateBean> getAllDataList();
	/**
	 * @author 890199
	 * @param typeName
	 * @param siteId
	 * @return List<AstTemplateBean>
	 */
	List<AstTemplateBean> getDataList(@Param("typeName")String typeName,@Param("siteId")String siteId);
	/**
	 * @author 890199
	 * @param ast
	 * @return 修改行数？
	 */
	int insertAstTemplate(AstTemplateBean ast);
	/**
	 * @author 890199
	 * @param id
	 * @return 修改行数？
	 */
	int deleteAstTemplate(int id);
	/**
	 * @author 890199
	 * @param as
	 * @return 修改行数？
	 *  
	 */
	int updateAstTemplate(AstTemplateBean as);
}
