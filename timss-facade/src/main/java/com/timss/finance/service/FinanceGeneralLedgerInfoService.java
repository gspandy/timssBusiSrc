package com.timss.finance.service;

import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinanceGeneralLedgerInfo;

/**
 * @description: 科目表相关服务
 * @author: 890170
 * @createDate: 2014-12-2
 */
public interface FinanceGeneralLedgerInfoService {
    /**
     * @description: 根据报销单编号查询总帐信息
     * @author: 890170
     * @createDate: 2014-12-2
     */
//    List<FinanceGeneralLedgerInfo> queryFinanceGeneralLedgerInfoListByFid(String fid);

    /**
     * @description: 根据报销单编号查询总帐信息
     * @author: 890170
     * @createDate: 2014-12-2
     */
    List<FinanceGeneralLedgerInfo> queryFinanceGeneralLedgerInfoListByFid2(Map<String, Object> paramMap);

    /**
     * @description: 推送ERP总帐信息
     * @author: 890170
     * @createDate: 2014-12-2
     */
    String putGeneralLedgerInfo(String erpForm, String erpDetail) throws Exception;

    /**
     * @description:根据规则抓取科目关联记录,查询条件为报销类型和科目序号
     * @author: 王中华
     * @createDate: 2015-9-6
     * @return:
     */
    List<FinanceGeneralLedgerInfo> getFinanceGeneralLedgerInfoList(String fid,String getReimbursementMan);
}
