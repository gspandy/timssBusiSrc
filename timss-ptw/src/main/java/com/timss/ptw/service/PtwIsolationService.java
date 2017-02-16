package com.timss.ptw.service;

import java.util.List;
import java.util.Map;

import com.timss.ptw.bean.PtwIsolationBean;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 隔离证service
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationService.java
 * @author: fengzt
 * @createDate: 2014年10月30日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PtwIsolationService {

    /**
     * 
     * @description:插入、更新 隔离证
     * @author: fengzt
     * @createDate: 2014年10月30日
     * @param formData
     * @param safeDatas
     * @param elecDatas
     * @param compSafeDatas 
     * @return:Map<String, Object>
     */
    Map<String, Object> insertOrUpdatePtwIsolation(String formData, String safeDatas, String elecDatas,String jxDatas, String compSafeDatas);

    /**
     * 
     * @description:通过站点来查找隔离证
     * @author: fengzt
     * @param page 
     * @createDate: 2014年10月30日
     * @return:List<PtwIsolationBean>
     */
    Page<PtwIsolationBean> queryPtwIsolationList(Page<PtwIsolationBean> page);

    /**
     * 
     * @description:更新隔离证
     * @author: fengzt
     * @createDate: 2014年10月30日
     * @param vo
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
    PtwIsolationBean queryPtwIsolationById( int id );
    
    /**
     * 
     * @description:插入隔离证
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param ptwIsolationBean
     * @return:int
     */
    int insertPtwIsolation(PtwIsolationBean ptwIsolationBean) ;

    
    /**
     * 
     * @description:更新隔离证状态
     * @author: fengzt
     * @createDate: 2014年11月5日
     * @param id
     * @param status
     * @param issueSuper
     * @param issueSuperNo
     * @param finElecInfo 
     * @param keyBoxId 
     * @return:int
     */
    int updatePtwIsolationStatusById(int id, int status, String issueSuper, String issueSuperNo, String finElecInfo,Integer keyBoxId);

    /**
     * 
     * @description:更新隔离证备注
     * @author: fengzt
     * @createDate: 2014年11月7日
     * @param id
     * @param remark
     * @return:int
     */
    int updatePtwIsolationRemarkById(int id, String remark);
    
    /**
     * 更新关联的钥匙箱号
     * @param id
     * @param relateKeyBoxId
     * @return
     */
    int updateRelateKeyBox(int id,String relateKeyBoxId);
    
    /**
     * 根据钥匙箱Id来查找对应的可用的隔离证
     * @param keyBoxId
     * @return
     */
    List<PtwIsolationBean> queryByKeyBoxId(int keyBoxId);
    
    /**
     * 根据钥匙箱Id来查找对应的隔离证
     * @param keyBoxId
     * @param status
     * @return
     */
	List<PtwIsolationBean> queryByKeyBoxId(int keyBoxId,String status);
	
	/**
     * 查询指定状态下，含有关联钥匙箱的隔离证
     * @param keyBoxId
     * @param status
     * @return
     */
	List<PtwIsolationBean> queryByRelateKeyBoxId(int keyBoxId,String status);
}
