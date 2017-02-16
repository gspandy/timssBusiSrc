package com.timss.purchase.service;

import java.util.List;
import java.util.Map;

import com.timss.purchase.bean.PurPay;
import com.timss.purchase.vo.PurPayDtlVO;
import com.timss.purchase.vo.PurPayStatVO;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: PurPayService
 * @description: 采购合同付款接口
 * @company: gdyd
 * @className: PurPayService.java
 * @author: 890162
 * @createDate: 2016-03-17
 * @updateUser: 890162
 * @version: 1.0
 */
public interface PurPayService {

    /**
     * @description:根据合同ID查询空白的付款记录
     * @author: 890162
     * @createDate: 2016-03-28
     * @param userinfo
     * @param sheetId
     * @return
     * @throws Exception
     */
    List<PurPayVO> queryBlankPurPayVoListBySheetId(UserInfoScope userInfo,String sheetId) throws Exception;
    
    /**
     * @description:根据合同ID查询付款记录
     * @author: 890162
     * @createDate: 2016-03-17
     * @param userinfo
     * @param sheetId
     * @return
     * @throws Exception
     */
    List<PurPayVO> queryPurPayVoListBySheetId(UserInfoScope userInfo,String sheetId) throws Exception;
    
    /**
     * @description:根据付款ID查询付款(预付款 到货款 质保金 结算款)记录
     * @author: 890162
     * @createDate: 2016-03-17
     * @param userInfo
     * @param payId
     * @return PurPayVO
     * @throws Exception
     */
    PurPayVO queryPurPayVoByPayId(UserInfoScope userInfo,String payId) throws Exception;
    
    /**
     * @Title:queryPurPayVoPlusInfo
     * @Description:补充purPayVO的总额 税额 和不含税额 对应物资类别，对应采购类型
     * @param userInfo
     * @param purPayVO
     * @param payType
     * @throws Exception
     * @return void
     */
    void queryPurPayVoAdditionalInfo(UserInfoScope userInfo,PurPayVO purPayVO,String payType) throws Exception;
    
    /**
     * @description:根据付款ID查询付款(到货款)详情记录
     * @author: 890162
     * @createDate: 2016-03-17
     * @param userInfo
     * @param payId
     * @param sheetId
     * @param payType
     * @return
     * @throws Exception
     */
    List<PurPayDtlVO> queryPurPayDtlVoListByCondition(UserInfoScope userInfo, String payId,String sheetId,String payType) throws Exception;

    /**
     * @description:生成采购付款页面
     * @author: 890162
     * @createDate: 2016-03-17
     * @param operType
     * @param payType
     * @param payId
     * @return
     * @throws Exception
     */
    Map<String, Object> initPurPayForm(String operType,String payType, String payId) throws Exception;

    /**
     * @description:更新采购付款基本信息状态
     * @author: 890162
     * @createDate: 2016-03-17
     * @param PurPay
     * @return
     * @throws Exception
     */
    int updatePurPayInfo(PurPay purPay) throws Exception;

    /**
     * @description:更新待办人
     * @author: 890162
     * @createDate: 2016-03-17
     * @param payId
     * @param transactor
     * @return
     * @throws Exception
     */
    int updatePurPayInfoTransactor(String payId, String transactor) throws Exception;

    /**
     * @description:更新采购付款信息的状态
     * @author: 890162
     * @createDate: 2016-03-17
     * @param payId
     * @param status
     * @return
     * @throws Exception
     */
    int updatePurPayStatus(String payId,String status) throws Exception;

    /**
     * @description:根据payId删除PurPayDtl信息
     * @author: 890162
     * @createDate: 2016-03-17
     * @param payId
     * @return
     * @throws Exception
     */
    int deletePurPayDtlByPayId(String payId) throws Exception;

    /**
     * @description:删除采购付款信息
     * @author: 890162
     * @createDate: 2016-03-17
     * @param payId
     * @return
     * @throws Exception
     */
    int deletePurPayInfo(String payId) throws Exception;

    /**
     * @description:更新采购付款信息的审批日期
     * @author: 890162
     * @createDate: 2016-03-30
     * @param payId
     * @return
     * @throws Exception
     */
    int updatePurPayAuditDate(String payId) throws Exception;
    
    /**
     * @Title:updateImtdPrice
     * @Description:更新流水表的价格
     * @param imtdid
     * @param price
     * @throws Exception
     * @return int
     */
    int updateImtdPrice(String imtdid,Double price) throws Exception;
    
    /**
     * @Title:updateImtdPrice
     * @Description:更新流水表的价格
     * @param imtdid
     * @param price
     * @param tax
     * @throws Exception
     * @return int
     */
    int updateImtrPrice(String imtdid,Double price,Double tax) throws Exception;
    
    /**
     * @description:采购付款统一保存方法
     * @author: 890162
     * @createDate: 2016-03-17
     * @param userInfo
     * @param purPayVo
     * @param purPayDtlVoList
     * @param paramMap
     * @return
     * @throws Exception
     */
    boolean saveOrUpdatePayInfo(UserInfoScope userInfo, PurPayVO purPayVo, List<PurPayDtlVO> purPayDtlVoList,
            Map<String, Object> paramMap) throws Exception;
    /**
     * @description:通过flowNo查询到sheetid
     * @author: 890166
     * @createDate: 2014-9-23
     * @param sheetNo
     * @param siteId
     * @return:
     */
    public PurPayVO queryPayVoByFlowNo(String payNo, String siteId) throws Exception;
    
    /**
     * @Title:queryPayPriceWithWID
     * @Description:按仓库 物资分类 审批年月查询报账总额和不含税报账总额
     * @param  siteid
     * @param  month
     * @param  warehouseid
     * @param  invcateid
     * @return List<PurPayStatVO>
     * @throws
     */
    public List<PurPayStatVO> queryPayPriceWithWID(String siteid,String month,String warehouseid,String invcateid);
    
    /**
     * @Title:queryCurMonthPayPriceWithWI
     * @Description:查询当月的某仓库 某物资分类的报账总额和不含税报账总额
     * @param  siteId
     * @param  warehouseid
     * @param  invcateid
     * @return PurPayStatVO
     * @throws
     */
    public PurPayStatVO queryCurMonthPayPriceWithWI(String siteId,String warehouseid,String invcateid);
}
