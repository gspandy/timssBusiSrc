package com.timss.asset.service;

import java.util.List;

import com.timss.asset.bean.AstBorrowRecordBean;
import com.timss.asset.vo.AstBorrowRecordVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;


public interface AstBorrowRecordService {
	public List<AstBorrowRecordBean> getAllDataList();
	public List<AstBorrowRecordVo> getDataList(String assetId);
	public int insertAstBorrowRecord(AstBorrowRecordBean abrb);
	public int deleteAstBorrowRecord(String id);
	public int updateAstBorrowRecord(AstBorrowRecordBean abrb);
	public List<AstBorrowRecordBean> getDetail(String assetId);
	public List<AstBorrowRecordVo> getInfo(String assetId);
	public Page<AstBorrowRecordVo> getAllBorrowRecordList(UserInfoScope userInfoScope,String assetId);
    Page< AstBorrowRecordVo > queryAstBorrowRecordOnCondition ( UserInfoScope userInfo , AstBorrowRecordVo abr );
}
