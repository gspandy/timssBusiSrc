package com.timss.purchase.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.dao.PurApplyDao;
import com.timss.purchase.service.abstr.PurApplyServiceDefImpl;
import com.timss.purchase.vo.PurApplyItemVO;
import com.yudean.homepage.service.HomepageService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyServiceImpl.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("PurApplyServiceImpl")
public class PurApplyServiceImpl extends PurApplyServiceDefImpl {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PurApplyDao purApplyDao;
    @Autowired
    private HomepageService homepageService;
    private static final Logger LOG = Logger.getLogger(PurApplyServiceImpl.class);
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> autoSendDataToBusiness(String businessId, String taskName, String itemids) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, String> result = new HashMap<String, String>(0);
        JSONArray array = JSONArray.fromObject(itemids);
        String[] itemidArr = (String[]) JSONArray.toArray(array, String.class);
        try {
          //原handler就要执行的方法
          updatePurApplyToPass(businessId, taskName);
          LOG.info( "--执行PurApplyService autoSendDataToBusiness->updatePurApplyToPass(businessId:"+businessId+" ,taskName:"+taskName );
          //对应原回调ajax请求purchase/purapply/sendToBusiness.do
          // 调用商务网接口
          boolean flag = sendDataToBussiness(userInfo, businessId, itemidArr);
          // 这里补从发送商务网方法
          if (flag) {
            // 发送商务网状态为1，物资状态为2
            int count = updatePurApplyItemBusinessStatus(businessId, itemidArr, 1, "2");
            if (count == itemidArr.length) {
              result.put("result", "success");
            } else {
              result.put("result", "false");
            }
            syncDataToEamItem4RealTime(itemidArr, userInfo.getSiteId());
          } else {
            result.put("result", "false");
          }
          LOG.info( "--执行PurApplyService autoSendDataToBusiness->purchase/purapply/sendToBusiness.do");
          //对应原回调ajax请求purchase/purapply/buiss2UpdatePAStatus.do
          PurApply purApply = new PurApply();
          purApply.setSheetId(businessId);
          purApply.setPurchstatus("2");
          updatePurApplyInfo(purApply);
          result.put("result", "success");
          LOG.info( "--执行PurApplyService autoSendDataToBusiness->purchase/purapply/buiss2UpdatePAStatus.do");
        } catch (Exception e) {
          LOG.info("--------------------------------------------");
          LOG.info("- PurApplyService 中的 autoSendDataToBusiness 方法抛出异常：", e);
          LOG.info("--------------------------------------------");
          result.put("result", "false");
          throw new Exception();
        }
        return result;
    }
    @Override
    public String checkStopable(String sheetId){
        StringBuffer result = new StringBuffer();
        List<PurApplyItemVO> purApplyItems = purApplyDao.queryPAItemsInPO(sheetId,"");
        List<PurApplyItemVO> purApplyItems1 = purApplyDao.queryPAItemsInPO(sheetId,"'0'");
        List<PurApplyItemVO> purApplyItems2 = purApplyDao.queryPAItemsInBusinessApproving(sheetId);
        List<PurApplyItemVO> purApplyItems3 = purApplyDao.queryPAItemsInPO(sheetId,"'1','2','3'");        
        if ( purApplyItems1.size()>0 ) {
            result.append( "物资"+purApplyItems.get( 0 ).getItemid()+"关联了审批中的采购合同，不能进行终止操作" );    
        }
        if ( 0==result.length()&&purApplyItems2.size()>0  ) {
            result.append( "物资"+purApplyItems2.get( 0 ).getItemid()+"正在发送商务网，不能进行终止操作");
        }
        if ( purApplyItems3.size()==purApplyItems.size() ) {
            result.append( "所有物资关联了审批通过的采购合同，不能进行终止操作" );
        }
        return result.toString();
    }
    @SuppressWarnings("unused")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean stopPurApply(String sheetId) throws Exception  {
        boolean result = false;
        PurApply purApply = purApplyDao.queryPurApplyBySheetId( sheetId );
        int count =  purApplyDao.updatePurApplyInfoStopProcInfo( sheetId ,purApply.getStopProcInstId(),"stopped");
        result = true;
        return result;
    }
    @SuppressWarnings("unused")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean startStopPurApply(String sheetId,String processInstId)  throws Exception  {
        boolean result = false;
        int count = purApplyDao.updatePurApplyInfoStopProcInfo( sheetId ,processInstId,"stopping");
        result = true;
        return result;
    }
    @SuppressWarnings("unused")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean nullifyStopPurApply(String sheetId)  throws Exception  {
        boolean result = false;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        PurApply purApply = purApplyDao.queryPurApplyBySheetId( sheetId );
        homepageService.Delete( purApply.getStopProcInstId(), userInfo );
        int count = purApplyDao.updatePurApplyInfoStopProcInfo( sheetId ,"","");
        result = true;
        return result;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removeStopPurApply(String sheetId,String procInstId)  throws Exception  {
        boolean result = false;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        homepageService.Delete( procInstId, userInfo );
        result = true;
        return result;
    }
}
