package com.timss.asset.service.core;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.asset.bean.AstBorrowRecordBean;
import com.timss.asset.dao.AstBorrowRecordDao;
import com.timss.asset.service.AstBorrowRecordService;
import com.timss.asset.vo.AstBorrowRecordVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 
 * @title: 固定资产的领用和归还
 * @company: gdyd
 * @className: AstBorrowRecordServiceImpl.java
 * @author: yucz
 * @createDate: 2016年7月19日
 * @updateUser: gdyd
 */
@Service
public class AstBorrowRecordServiceImpl implements AstBorrowRecordService{
	@Autowired
	private AstBorrowRecordDao astBorrowRecordDao;
	
	/**
     * @description:显示领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public List<AstBorrowRecordBean> getAllDataList(){
		List<AstBorrowRecordBean> as=astBorrowRecordDao.getAllDataList();
		return as;
	}
	
	/**
     * @description:通过assetId显示领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public List<AstBorrowRecordVo> getDataList(String assetId){
		List<AstBorrowRecordVo> as=astBorrowRecordDao.getDataList(assetId);
		return as;
	}
	
	/**
     * @description:显示领用记录返回Page
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public Page<AstBorrowRecordVo> getAllBorrowRecordList(UserInfoScope userInfoScope, String assetId){
        Page<AstBorrowRecordVo> page = userInfoScope.getPage();
		List<AstBorrowRecordVo> as = astBorrowRecordDao.getDataList(assetId);
		page.setResults(as);
		return page;
	}
	
	/**
     * @description:增加领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public int insertAstBorrowRecord(AstBorrowRecordBean abrb){
		int a=astBorrowRecordDao.insertAstBorrowRecord(abrb);
		return a;
	}
	
	/**
     * @description:删除领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public int deleteAstBorrowRecord(String id){
		int a=astBorrowRecordDao.deleteAstBorrowRecord(id);
		return a;
	}
	
	/**
     * @description:更新（归还）领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public int updateAstBorrowRecord(AstBorrowRecordBean abrb){
		int a=astBorrowRecordDao.updateAstBorrowRecord(abrb);
		return a;
	}
	
	/**
     * @description:获取领用记录来设置归还领用按钮的标志位
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public List<AstBorrowRecordBean> getDetail(String assetId){
		List<AstBorrowRecordBean> as=astBorrowRecordDao.getDetail(assetId);
		return as;
	}
	
	/**
     * @description:获取归还窗口内的初始值
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	@Override
	public List<AstBorrowRecordVo> getInfo(String assetId){
		List<AstBorrowRecordVo> as=astBorrowRecordDao.getInfo(assetId);
		return as;
	}

	/**
     * @description:条件查询领用归还记录
     * @author: 890151
     * @createDate: 2016-12-14
     * @return
     * @throws Exception:
     */
	@Override
	public Page<AstBorrowRecordVo> queryAstBorrowRecordOnCondition(
			UserInfoScope userInfo, AstBorrowRecordVo abr) {
		UserInfoScope scope = userInfo;
		Page< AstBorrowRecordVo > page = scope.getPage();
		page.setParameter( "siteId" , userInfo.getSiteId() );
		if( abr!=null ) {
		    page.setParameter( "assetId" , abr.getAssetId() );
		    page.setParameter( "imrdid" , abr.getImrdid() );
		}
		List< AstBorrowRecordVo > ret = astBorrowRecordDao.queryAstBorrowRecordList( page );
		page.setResults( ret );
		return page;
	}
	
}
