package com.timss.inventory.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvMatReturns;
import com.timss.inventory.vo.InvMatReturnsVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsDao.java
 * @author: 890166
 * @createDate: 2015-3-12
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatReturnsDao {

    /**
     * @description:查询全部被退货的物资
     * @author: 890166
     * @createDate: 2015-3-12
     * @param page
     * @return
     * @throws Exception:
     */
    List<InvMatReturnsVO> queryAllReturnItem(Page<InvMatReturnsVO> page);

    /**
     * @description: 删除物资退换货主单信息
     * @author: 890166
     * @createDate: 2015-3-13
     * @param whereTip
     * @return
     * @throws Exception:
     */
    int deleteMatReturnsByImrsid(String imrsid);

    /**
     * @description:查询表单详细信息
     * @author: 890166
     * @createDate: 2015-3-13
     * @param map
     * @return:
     */
    List<InvMatReturnsVO> queryMatReturnsForm(Map<String, Object> map);

    /**
     * @description: 更新InvMatReturns信息
     * @author: 890166
     * @createDate: 2015-3-18
     * @return:
     */
    int updateMatReturns(InvMatReturns imr);

    /**
     * @description: 插入InvMatReturns数据
     * @author: 890166
     * @createDate: 2015-3-18
     * @param imr
     * @return:
     */
    int insertMatReturns(InvMatReturns imr);

    /**
     * @description:根据sheetno找到退库信息
     * @author: yuanzh
     * @createDate: 2015-9-28
     * @param paramMap
     * @return:
     */
    List<InvMatReturns> queryReturnsBySheetNo(Map<String, String> paramMap);

    /**
     * @description:  查询某张领料单的某种物资退库的数量
     * @author: 890152
     * @createDate: 2016-5-31
     * @param imaid 物资领料单id
     * @param itemid 物资id
     * @return:
     */
    BigDecimal queryReturnsNumByImaid(@Param("imaid")String imaid, @Param("itemid")String itemid,
            @Param("siteid")String siteid,@Param("wareHouseId")String wareHouseId);
    
    /**
     * @description:  查询某张领料单的某种物资退库总数
     * @author: 890151
     * @createDate: 2016-10-25
     * @param imadid 物资领料单详情id
     * @return:
     */
    int queryReturnsTotalByImadid(String imadId) throws Exception;
}
