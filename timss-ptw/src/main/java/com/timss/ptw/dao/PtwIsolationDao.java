package com.timss.ptw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwIsolationBean;
import com.timss.ptw.bean.PtwIsolationMethodBean;
import com.timss.ptw.vo.IsolationVo;
import com.yudean.itc.dto.Page;


/**
 * 
 * @title: 隔离证DAO
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationDao.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PtwIsolationDao {

    /**
     * 
     * @description:插入
     * @author: fengzt
     * @createDate: 2014年10月30日
     * @param ptwIsolationBean
     * @return:int
     */
    int insertPtwIsolation(PtwIsolationBean ptwIsolationBean);

    /**
     * 
     * @description:通过站点信息来拿隔离证
     * @author: fengzt
     * @createDate: 2014年10月30日
     * @param pageVo
     * @return:List<PtwIsolationBean>
     */
    List<PtwIsolationBean> queryPtwIsolationList(Page<PtwIsolationBean> page);

    /**
     * 
     * @description:更新隔离证
     * @author: fengzt
     * @createDate: 2014年10月30日
     * @param PtwIsolationBean
     * @return:int
     */
    int updatePtwIsolation(PtwIsolationBean vo);

    /**
     * 
     * @description:通过ID查找隔离证
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param id
     * @return:PtwIsolationBean
     */
    PtwIsolationBean queryPtwIsolationById(int id);

    /**
     * 
     * @description:删除隔离证和工作票的隔离方法表 by islId
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param islId
     * @return:int
     */
    int deletePtwIsolationItem(@Param("islId")Integer islId, @Param("wtId")Integer wtId);

    /**
     * 
     * @description:插入insertPtwIsolationItem
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param vos
     * @return:
     */
    int insertBatchPtwIsolationItem(List<PtwIsolationMethodBean> list);

    /**
     * 
     * @description:查找隔离证最大的序号
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param siteId
     * @return:
     */
    String queryMaxNumPtwIsolationItemBySiteId(String siteId);

    /**
     * 
     * @description:加载隔离证子项 datagrid
     * @author: fengzt
     * @createDate: 2014年11月4日
     * @param map
     * @return:List<IsolationVo>
     */
    List<IsolationVo> querySafeDatagridByWtOrIslId(Map<String, Object> map);

    /**
     * 
     * @description:更新隔离证状态
     * @author: fengzt
     * @createDate: 2014年11月5日
     * @param map
     * @return:int
     */
    int updatePtwIsolationStatusById(Map<String, Object> map);

    /**
     * 
     * @description:更新隔离证备注
     * @author: fengzt
     * @createDate: 2014年11月7日
     * @param map
     * @return:int
     */
    int updatePtwIsolationRemarkById(Map<String, Object> map);
    
    /**
     * 更新关联钥匙箱号
     * @param id
     * @param relateKeyBoxId
     * @return
     */
    int updateRelateKeyBox(@Param("id")int id, @Param("relateKeyBoxId")String relateKeyBoxId);
    
    /**
     * 查询指定状态下的钥匙箱
     * @param keyBoxId
     * @param status 用逗号分隔
     * @return
     */
     List<PtwIsolationBean> queryByKeyBoxId(@Param("keyBoxId")int keyBoxId,@Param("status") String status);
     
     
     /**
      * 查询指定状态下的关联钥匙箱
      * @param keyBoxId
      * @param status 用逗号分隔
      * @return
      */
      List<PtwIsolationBean> queryByRelateKeyBoxId(@Param("keyBoxId")int keyBoxId,@Param("status") String status);

}
