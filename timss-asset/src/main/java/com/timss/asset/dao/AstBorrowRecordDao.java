package com.timss.asset.dao;

import java.util.List;

import com.timss.asset.bean.AstBorrowRecordBean;
import com.timss.asset.vo.AstBorrowRecordVo;
import com.yudean.itc.dto.Page;
/**
 * 
 * @title: 固定资产的领用和归还
 * @company: gdyd
 * @className: AstBorrowRecordDao.java
 * @author: yucz
 * @createDate: 2016年7月19日
 * @updateUser: gdyd
 */

public interface AstBorrowRecordDao {
	
	/**
     * @description:显示领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	List<AstBorrowRecordBean> getAllDataList();
	
	/**
     * @description:通过assetId显示领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	List<AstBorrowRecordVo> getDataList(String assetId);
	
	/**
     * @description:增加领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	int insertAstBorrowRecord(AstBorrowRecordBean abrb);
	
	/**
     * @description:删除领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	int deleteAstBorrowRecord(String id);
	
	/**
     * @description:更新（归还）领用记录
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	int updateAstBorrowRecord(AstBorrowRecordBean abrb);
	
	/**
     * @description:获取领用记录来设置归还领用按钮的标志位
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	List<AstBorrowRecordBean> getDetail(String assetId);
	
	/**
     * @description:获取归还窗口内的初始值
     * @author: 890199
     * @createDate: 2016年7月19日
     * @return
     * @throws Exception:
     */
	List<AstBorrowRecordVo> getInfo(String assetId);
	
	/**
     * @description:条件查询领用归还记录
     * @author: 890151
     * @createDate: 2016-12-14
     * @return
     * @throws Exception:
     */
    List<AstBorrowRecordVo> queryAstBorrowRecordList(Page<?> page);

}
