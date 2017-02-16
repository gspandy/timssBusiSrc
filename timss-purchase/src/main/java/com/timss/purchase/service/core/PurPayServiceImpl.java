package com.timss.purchase.service.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvItemBaseField;
import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.service.InvMatTranRecService;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.purchase.bean.PurAttachMapping;
import com.timss.purchase.bean.PurPay;
import com.timss.purchase.bean.PurPayDtl;
import com.timss.purchase.dao.PurPayDao;
import com.timss.purchase.dao.PurProcessMappingDao;
import com.timss.purchase.service.PurAttachMappingService;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.service.PurProcessMappingService;
import com.timss.purchase.vo.PurPayDtlVO;
import com.timss.purchase.vo.PurPayStatVO;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.bean.WorktaskBean;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
@Service("PurPayServiceImpl")
public class PurPayServiceImpl implements PurPayService {
    private static final Logger LOG = Logger.getLogger(PurPayServiceImpl.class);
    //注入Dao
    @Autowired
    private PurPayDao purPayDao;
    @Autowired
    private PurProcessMappingDao purProcessMappingDao;
    @Autowired
    private AttachmentMapper attachmentMapper;
    //注入Service
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private PurProcessMappingService purProcessMappingService;
    @Autowired
    private PurAttachMappingService purAttachMappingService;
    @Autowired
    private InvMatTranRecService invMatTranRecService;
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;
    
    /**
     * 根据采购合同的sheetId，生成空白的采购付款表
     */
    @Override
    public List<PurPayVO> queryBlankPurPayVoListBySheetId(UserInfoScope userInfo, String sheetId) throws Exception {
        List<PurPayVO> resultList = new ArrayList<PurPayVO>(0);
        resultList = purPayDao.queryBlankPurPayBySheetId( sheetId );   
        return resultList;
    }
    
    /**
     * 根据采购合同的sheetId，得到采购付款列表
     */
    @Override
    public List<PurPayVO> queryPurPayVoListBySheetId(UserInfoScope userInfo, String sheetId) throws Exception {
        List<PurPayVO> resultList = new ArrayList<PurPayVO>(0);
        resultList = purPayDao.queryPurPayByCondition( sheetId, null );   
        return resultList;
    }

    /**
     * 根据采购付款的payId，得到采购付款信息
     */
    @Override
    public PurPayVO queryPurPayVoByPayId(UserInfoScope userInfo, String payId) throws Exception {
        List<PurPayVO> purPayVOList = new ArrayList<PurPayVO>(0);
        purPayVOList = purPayDao.queryPurPayByCondition(null, payId );
        PurPayVO result = null;
        if ( 0<purPayVOList.size() ) {
            result = purPayVOList.get( 0 );
        }
        return result;
    }
    
    /**
     * 根据采购付款的payId和对应的采购合同sheetId以及采购付款的付款类型payType
     * 得到:采购付款的总含税金额 税额 和税前金额
     * 采购付款对应采购合同的采购类型，以及物资类型
     */
    @Override
    public void queryPurPayVoAdditionalInfo(UserInfoScope userInfo,PurPayVO purPayVO,String payType) throws Exception {
        String payId = purPayVO.getPayId();
        String sheetId = purPayVO.getSheetId();
        if ( "arrivepay".equals( payType )||"settlepay".equals( payType ) ) {
            //payType是为了得到正确的未报账数
            List<PurPayDtlVO> purPayDtlVOs = queryPurPayDtlVoListByCondition( userInfo, payId, sheetId,payType  );
            Double taxTotal = 0D;
            Double notaxTotal = 0D;
            for ( PurPayDtlVO purPayDtlVO : purPayDtlVOs ) {
                taxTotal += purPayDtlVO.getTaxTotal();
                notaxTotal += purPayDtlVO.getNotaxTotal();
            }
            purPayVO.setTaxTotal( taxTotal );
            purPayVO.setNoTaxTotal( notaxTotal );
            purPayVO.setTotal( taxTotal+notaxTotal );
        }
        List<String> itemClassIds = purPayDao.queryPurPayItemClassId( sheetId );
        List<String> sheetClassIds = purPayDao.queryPurPaySheetClassId( sheetId );
        if ( 0<itemClassIds.size() ){
            String itemClassId = itemClassIds.get( 0 );
            purPayVO.setItemClassid( itemClassId );
        }
        if ( 0<sheetClassIds.size() ) {
            String sheetClassId = sheetClassIds.get( 0 );
            purPayVO.setSheetClassId( sheetClassId );
        }
    }
    
    /**
     * 根据采购付款PayId，对应采购合同的sheetId以及采购付款的类型payType，获取对应的采购详情
     */
    @Override
    public List<PurPayDtlVO> queryPurPayDtlVoListByCondition(UserInfoScope userInfo, String payId,String sheetId,String payType) throws Exception {
        List<PurPayDtlVO> resultList = new ArrayList<PurPayDtlVO>(0);
        resultList = purPayDao.queryPurPayDtlListByCondition( payId, sheetId,payType );
        return resultList;
    }
    /**
     * 初始化采购付款表单时获取的数据:
     * 会传到前端以下数据:
     * siteId  站点
     * defKey  流程key
     * orgName 用户所在组织的名称
     * payType 付款类型                         新建时，之间传递传入的值  非新建时，根据传入的PayId得到付款信息从中读取
     * isEdit                      新建时，为editable  非新建时，根据流程环节标识是否为可编辑的环节 且 当前用户为待办人时，设为editable，否则清空
     * isCandidate 办理人                           非新建时传递 缺省值 false
     * isEditableTask 是否可编辑的环节   非新建时传递 缺省值 false
     * taskId         任务ID          非新建时传递 且有流程Id
     * process        任务编码                 非新建时传递 且有流程Id  在前端 用了这个变量做判断表单的状态是草稿还是退回首环节，因此若修改草稿不启动流程的话要特别留意前端的判断逻辑
     * processName    任务名称                 非新建时传递 且有流程Id
     * status         状态                        非新建时传递
     * classType                      非新建时传递
     * 附件信息                                                非新建时传递
     */
    @Override
    public Map<String, Object> initPurPayForm(String operType,String payType, String payId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>(0);
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        String processDefKeySuffix = payType;
        String defKey = ("purchase_[@@@]_"+processDefKeySuffix).replace("[@@@]", siteId.toLowerCase());
        if ( "ZJW".equals( siteId ) ) {
            defKey = "purchase_zjw_pay";
        }
        result.put("siteId", siteId);
        result.put("defKey", defKey);
        result.put("orgName", userInfo.getOrgName());
        result.put("payType", payType );
        result.put("isEdit", "editable" );
        result.put("hasRelatePay", "false" );
        if ( !"new".equals( operType ) ) {
            PurPayVO purPayVO = queryPurPayVoByPayId(userInfo,payId);
            //如果是读取付款，付款类型直接从付款信息中取得
            payType = purPayVO.getPayType();
            //付款类型
            result.put( "payType", payType );
            //发送erp状态
            result.put("erpStatus", purPayVO.getErpStatus());
            result.put("erpDate", purPayVO.getErpDate());
            Map<String, Object> processInstInfoMap = getProcessInfo( payId );
            result.putAll( processInstInfoMap );
            if ( !Boolean.valueOf( result.get( "isCandidate" ).toString() )||
                    !Boolean.valueOf( result.get( "isEditableTask" ).toString() )) {
                result.put("isEdit", "" );
            }
            List<Map<String, Object>> fileMap = purAttachMappingService.queryPurAttach(purPayVO.getPayNo());
            JSONArray jsonArray = JSONArray.fromObject(fileMap);
            result.put("uploadFiles", jsonArray);
            if ( StringUtils.isNotEmpty( payId ) ) {
                List<String> hasRelatePay =  purPayDao.queryArrivePurPayHasQualityPurPay( payId );
                if ( 0 < hasRelatePay.size() ) {
                    Integer count = Integer.valueOf( hasRelatePay.get( 0 ).toString());
                    if ( 0 < count ) {
                        result.put("hasRelatePay", "true" );
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * @Title:getProcessInstanceId
     * @Description:获取流程相关信息
     * @param payId
     * @throws Exception
     * @return String
     * @throws
     */
    @SuppressWarnings("unchecked")
    private Map<String,Object> getProcessInfo(String payId) throws Exception{
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String,Object> processInfoMap = new HashMap<String,Object>(0);
        PurPay purPay = queryPurPayVoByPayId( userInfo, payId );
        String processInstId = purPay.getProcInstId();
        String status = purPay.getStatus();
        if ( StringUtils.isNotEmpty( status ) ) {
            processInfoMap.put( "status", status );
        }
        WorktaskBean wtBean = homepageService.getOneTaskByFlowNo(purPay.getPayNo(), userInfo);
        if (null != wtBean) {
            processInfoMap.put("classType", wtBean.getClasstype().toString());
        }
        //isCandidate、isEditableTask 默认值都为false
        processInfoMap.put( "isCandidate", false );
        processInfoMap.put( "isEditableTask", false );
        if (StringUtils.isNotEmpty( processInstId )) {
            processInfoMap.put( "procInstId", processInstId );
            List<Task> activities = workflowService.getActiveTasks(processInstId);
            if (null != activities && !activities.isEmpty()) {
              Task task = activities.get(0);
              List<String> userIdList = workflowService.getCandidateUsers(task.getId());
              String userId = userInfo.getUserId();
              if ( userIdList.contains( userId ) ) {
                  processInfoMap.put( "isCandidate", true );
              }       
              Map<String, String> processAttr = workflowService.getElementInfo(task.getId());
              //任务id
              processInfoMap.put("taskId", task.getId());
              //任务编码
              processInfoMap.put("process", task.getTaskDefinitionKey());
              //任务名称 
              processInfoMap.put("processName", task.getName());
              //任务是否可以编辑
              String modifiableAttr = processAttr.get( "modifiable" );
              if ( StringUtil.isNotEmpty( modifiableAttr )&&!"\"null\"".equals( modifiableAttr ) ) {
                  Map<String, String> modifiableMap = JsonHelper.fromJsonStringToBean( modifiableAttr, Map.class );
                  String modifiable = modifiableMap.get( "isEditableTask" );
                  if(StringUtils.isNotEmpty( modifiable )&&StringUtils.equals( "true",modifiable )){
                      processInfoMap.put("isEditableTask", true);
                  }  
              }     
            }
        }else if ( StringUtil.isEmpty( payId ) ) {
              //新建的时候
              processInfoMap.put( "isCandidate", true );
              processInfoMap.put( "isEditableTask", true);
        }
        return processInfoMap;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updatePurPayInfo(PurPay purPay) throws Exception {
        int result = purPayDao.updatePurPayInfo( purPay );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updatePurPayInfoTransactor(String payId, String transactor) throws Exception {
        int result = purPayDao.updatePurPayStatusInfo(payId, transactor, null );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updatePurPayStatus(String payId,String status) throws Exception {
        int result = purPayDao.updatePurPayStatusInfo(payId, null, status );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deletePurPayDtlByPayId(String payId) throws Exception {
        int result = purPayDao.deletePurPayDtlByPayId( payId );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deletePurPayInfo(String payId) throws Exception {
        int result = purPayDao.updatePurPayStatusInfo( payId, "", "deleted" );  
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveOrUpdatePayInfo(@Operator UserInfoScope userInfo, @LogParam("purPay") PurPayVO purPayVo, List<PurPayDtlVO> purPayDtlVoList,
            Map<String, Object> paramMap) throws Exception {
        Boolean flag = true;
        String saveType = String.valueOf( paramMap.get( "saveType" ));
        try {
            if (null != purPayVo) {
              //保存主表
              int saveBaseInfoResult = savePurPayBaseInfo( userInfo, purPayVo, saveType );
              if (saveBaseInfoResult > 0) {
                //保存子表
                  savePurPayDtlList(userInfo, purPayVo, purPayDtlVoList, saveType);
              }
              String uploadIds = paramMap.get("uploadIds") == null ? "" : String.valueOf(paramMap.get("uploadIds"));
              //保存附件
              savePurPayAttach(purPayVo.getPayNo(),uploadIds);
              //将新得到的值更新到VO
              paramMap.put("payId", purPayVo.getPayId());
              paramMap.put("payNo", purPayVo.getPayNo());
              paramMap.put("procInstId", purPayVo.getProcInstId());    
              paramMap.put("taskId", purPayVo.getTaskId());
            }
          } catch (Exception e) {
            LOG.info("--------------------------------------------");
            LOG.info("- PurPayServiceImpl 中的 saveOrUpdatePayInfo 方法抛出异常：", e);
            LOG.info("--------------------------------------------");
            flag = false;
          }
        return flag;
    }
    /**
     * @Title:savePurPayBaseInfo
     * @Description:保存付款主表
     * @param userInfo
     * @param purPayVo
     * @param saveType
     * @return int
     * @throws Exception 
     */
    private int savePurPayBaseInfo(UserInfoScope userInfo, PurPayVO purPayVo,String saveType) throws Exception{
        String payId = purPayVo.getPayId();
        String siteId = userInfo.getSiteId();
        String userId = userInfo.getUserId();
        String userName = userInfo.getUserName();
        //若payId为空，初始化部分字段
        if ( StringUtil.isEmpty( payId ) ) {
            purPayVo.setStatus( "draft" );
            purPayVo.setPayId( null );
            purPayVo.setCreatedate( new Date() );
            purPayVo.setCreateuser( userId );
            purPayVo.setTransactor( userName );
            purPayVo.setSiteid( siteId );
        }else {
            purPayVo.setModifydate( new Date() );
            purPayVo.setModifyuser( userId );
        }
        PurPay purPay = purPayVo;
        int result = 0;
        /**
         * 新增，insert业务数据，启动流程(启动流程不等于弹出审批框，那个控制在js的回调中根据saveType决定是否触发工作流的审批)
         * 编辑或审批，update业务数据，目前为了预留未可知的操作，故做了独立分支，后期可考虑合并
         */
        if ( ("save".equals( saveType )||"submit".equals( saveType )) && StringUtils.isEmpty( payId ) ) {
            //新增暂存 或 提交
            result = purPayDao.insertPurPayInfo( purPay );
            //暂时解决方法，由于不同的类型的付款有不同的编号生成规则
            purPayDao.updatePurpayPayNo( purPay.getPayId(), purPay.getPayType(), siteId );
            String payNo = queryPurPayNo(purPay.getPayId());
            payId = purPay.getPayId();
            purPayVo.setPayNo( payNo );
            purPayVo.setPayId( payId );
            //启动流程(这个方法会更新purPayVo 的taskId和ProcInstId)
            startupProcess(purPayVo, userInfo);
        }else if ( !StringUtils.isEmpty( payId ) ){
            //编辑暂存 提交 审批
            String taskId = "";
            String procInstId = purPayVo.getProcInstId();
            List<Task> taskList = workflowService.getActiveTasks(procInstId);
            if (null != taskList && !taskList.isEmpty()) {
                taskId = taskList.get(0).getId();
            }
            purPayVo.setTaskId( taskId );
            if ( "save".equals( saveType )||"submit".equals( saveType )||"audit".equals( saveType ) ) {
                result = purPayDao.updatePurPayInfo( purPay );
            }
        }
        return result;
    }
    
    private String queryPurPayNo(String payId){
        String payNo = "";
        List<PurPayVO> newPurPays = purPayDao.queryPurPayByCondition( null,payId  );
        if ( 0<newPurPays.size() ) {
            PurPayVO purPayVO = newPurPays.get( 0 );
            payNo = purPayVO.getPayNo();
        }
        return payNo;
    }
    /**
     * @Title:saveBaseInfoResult
     * @Description:保存付款子表
     * @param userInfo
     * @param purPayVo
     * @param purPayDtlVoList
     * @param saveType
     * @return int
     */
    private int savePurPayDtlList(UserInfoScope userInfo, PurPayVO purPayVo, List<PurPayDtlVO> purPayDtlVoList,String saveType){
        String payId = purPayVo.getPayId();
        String status = purPayVo.getStatus();
        String isEditableTask = "";
        List<PurPayDtl> purPayDtlTempList = new ArrayList<PurPayDtl>(purPayDtlVoList);
        List<PurPayDtl> purPayDtlList = new ArrayList<PurPayDtl>(0);
        PurPay purPay = purPayVo;
        int saveResult = 0;
        if ( StringUtils.isNotEmpty( payId ) ) {
            try {
                Map<String, Object> result = getProcessInfo(payId);
                isEditableTask = String.valueOf( result.get( "isEditableTask" ) );
            } catch (Exception e) {
                LOG.warn( "savePurPayDtlList--获取流程信息异常", e );
            }
        }
        // 保存付款明细信息
        if (null != purPayDtlTempList && !purPayDtlTempList.isEmpty()) {
          // 若是草稿或第一个环节状态
          if ("draft".equals(status)||"true".equals( isEditableTask )) {
            // 先根据payId将子单中的所有信息清空
            purPayDao.deletePurPayDtlByPayId( payId );
            // 通过遍历子表list做一些前期的工作
            for (PurPayDtl purPayDtlTemp : purPayDtlTempList) {
                //如果报账数为0，要把税额和不含税金额清零
                if ( 0==purPayDtlTemp.getSendAccount() ) {
                    purPayDtlTemp.setNotaxTotal( 0D );
                    purPayDtlTemp.setTaxTotal( 0D );
                }
                purPayDtlTemp.setPayDtlId( null );
                purPayDtlTemp.setPayId( payId );
                purPayDtlList.add( purPayDtlTemp );
            }
            // 重新插入子单列表信息
            saveResult = purPayDao.insertPurPayDtlInfo( purPayDtlList );
            /**
             * 可能会有重新更新主表的一些设置,暂时未写
             */
            purPayDao.updatePurPayInfo( purPay );
          } else {
            /**
             * 可能会有更新子表的状态的操作，暂时未写
             */
          }
        }
        return saveResult;
    }
    
    /**
     * @Title:savePurPayAttach
     * @Description:保存付款附件
     * @param payNo
     * @param uploadIds
     * @throws Exception
     * @return int
     * @throws Exception
     */
    private int savePurPayAttach(String payNo,String uploadIds) throws Exception {
        int result = 0;
        purAttachMappingService.deletePurAttachMappingBySheetno(payNo);
        if (!"".equals(uploadIds)) {
          String[] ids = uploadIds.split(",");
          // 先删除后插入
          for (int i = 0; i < ids.length; i++) {
            PurAttachMapping pam = new PurAttachMapping();
            pam.setSheetno(payNo);
            pam.setAttachid(ids[i]);
            result = purAttachMappingService.insertPurAttachMapping(pam);
          }
          attachmentMapper.setAttachmentsBinded(ids, 1);
        }
        return result;
    }
   
    /**
     * @Title:startupProcess
     * @Description:采购付款启动流程同时更新purPayVo task 和 procInstId 
     * @param purPayVo
     * @param userInfo
     * @throws Exception
     * @return 通过Map 返回 task 和 procInstId 
     * @throws
     */
    private Map<String, Object> startupProcess(PurPayVO purPayVo, UserInfoScope userInfo) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String processId = null;
        String userId = userInfo.getUserId();
        String siteId = userInfo.getSiteId();
        String processDefKeySuffix = purPayVo.getPayType();
        Task task = null;
        // 判断流程类型
        String processDefKey = ("purchase_[@@@]_" + processDefKeySuffix).replace("[@@@]", siteId.toLowerCase());
        if ( "ZJW".equals( siteId ) ) {
            processDefKey = "purchase_zjw_pay";
        }
        String processKey = workflowService.queryForLatestProcessDefKey(processDefKey);
        //存放流程变量
        Map<String, Object> flowMap = new HashMap<String, Object>();
        //目前决定流程条件分支的只有此三个流程变量：采购类型，物资类别，总价
        String sheetClassId = purPayVo.getSheetClassId();
        String itemClassId = purPayVo.getItemClassid();
        String itemType = "";//给GKHandler用的变量
        if(StringUtils.isNotEmpty( sheetClassId )){
            if(sheetClassId.contains( "IT" )){
                itemType="IT";
            }else if(sheetClassId.contains( "办公" )){
                itemType="OFFICE";
            }else if ( sheetClassId.contains( "消防" ) ) {
                itemType="FIRE";
            }else if ( sheetClassId.contains( "劳保" ) ) {
                itemType="LABOUR";
            }else if(StringUtils.isNotEmpty( itemClassId )){
                if ( itemClassId.contains( "易耗" ) ) {
                    itemType="EASYCONSUMED";
                }
            }
        }
        //关联的采购合同的采购类型--视图字段 读取的时候通过后台查询[原始信息]
        flowMap.put("sheetclassid", sheetClassId);
        //报账详情第一条明细的物资分类(不是从采购申请获取) 读取的时候通过后台查询[原始信息]
        flowMap.put("itemclassid", itemClassId);
        //付款金额[原始信息]
        Double pay = null==purPayVo.getPay()?0:purPayVo.getPay();
        flowMap.put("tatolcost", pay);  
        //转换为工作流中要用到的变量
        flowMap.put("needGKAudit", sheetClassId.contains( "计算机" )&&itemClassId.contains( "易耗" )||sheetClassId.contains( "劳保" )||sheetClassId.contains( "消防" )||sheetClassId.contains( "办公" )?"Y":"N");
        flowMap.put("totalPayment", pay>500000?"HIGH":"LOW");
        flowMap.put("itemType", itemType);
        flowMap.put("businessId", purPayVo.getPayId() );
        ProcessInstance p = workflowService.startLatestProcessInstanceByDefKey(processKey, userId,flowMap);
        processId = p.getProcessInstanceId();
        List<Task> taskList = workflowService.getActiveTasks(processId);
        if (null != taskList && !taskList.isEmpty()) {
            task = taskList.get(0);
        }
        //启动流程的时候就要生成待办
        String type = "edit";
        String taskId = task.getId();
        String payTypeName = "";
        String sheetNo = "";
        String sheetName = "";
        List<AppEnum> payTypeList = itcMvcService.getEnum("PUR_PAYTYPE");
        if (null != payTypeList && !payTypeList.isEmpty()) {
          for (AppEnum payTypeEnum : payTypeList) {
            if (payTypeEnum.getCode().equals(purPayVo.getPayType())) {
              payTypeName = payTypeEnum.getLabel();
              break;
            }
          }
        }
        sheetNo = purPayVo.getSheetNo();
        sheetName = purPayVo.getSheetName();
        homepageService.createProcess(
                String.valueOf(purPayVo.getPayNo()),
                task.getProcessInstanceId(),
                "付款申请",
                payTypeName+"-采购合同"+sheetNo+"-"+sheetName,
                task.getName(),
                "/purchase/purpay/purPayForm.do?procInstId="+ task.getProcessInstanceId() 
                +"&operType=" + type + "&payId=" + purPayVo.getPayId()
                +"&payType="+purPayVo.getPayType()+"&sheetId="+purPayVo.getSheetId()
                ,userInfo
                ,  new ProcessFucExtParam());
        resultMap.put("procInstId", processId);
        resultMap.put("taskId", taskId);
        purPayVo.setProcInstId( processId );
        purPayVo.setTaskId( taskId );
        //更新实体和procInstId的关系
        purPayDao.updatePurPayProcInst( purPayVo );
        return resultMap;
    }
    
    /**
     * 更新付款信息的审批日期
     */
    public int updatePurPayAuditDate(String payId) throws Exception{
        int result = 0 ;
        Map<String, Object> map = new HashMap<String, Object>(0);
        map.put( "auditDate",  new Date() );
        result = purPayDao.updatePurpaySpecProperties( payId, map);
        return result;
    }
    
    /**
     * 更新入库物资信息的价格
     */
    public int updateImtdPrice(String imtdid,Double price) throws Exception{
        int result = 0;
        result = purPayDao.updateInvMatTranDetailPrice( imtdid, price ) ;
        return result;
    }
    
    /**
     * 更新库存改造后入库物资信息的价格
     */
    public int updateImtrPrice(String imtdid,Double price,Double tax) throws Exception{
        int result = 0;
        //更新流水价格
        result = purPayDao.updateInvMatTranRecPrice( imtdid, price ,tax ) ;
        List<InvMatTranRec> imtrList = invMatTranRecService.queryInvMatTranRecByImtdId( imtdid );
        List<InvItemBaseField> iibfList = new ArrayList<InvItemBaseField>(0);
        for ( InvMatTranRec imtr : imtrList ) {
            InvItemBaseField iibf = (InvItemBaseField)imtr;
            iibfList.add( iibf );
        }
        //更新实时价格
        invRealTimeDataService.realTimeCaluByType( iibfList, "All" );
        return result;
    }
    
    /**
     * @description: 通过sheetNo和站点id找到报账表id
     * @author: 890162
     * @createDate: 2016-8-10
     * @param payNo
     * @param siteId
     * @return
     * @throws Exception
     *           :
     */
    @Override
    public PurPayVO queryPayVoByFlowNo(String payNo, String siteId) throws Exception {
      PurPayVO purPayVO = null;
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("payNo", payNo);
      map.put("siteId", siteId);
      List<PurPayVO> resultList = purPayDao.queryPayVoByFlowNo(map);
      if ( 0<resultList.size() ) {
          purPayVO = resultList.get( 0 );
      }
      return purPayVO;
    }

    @Override
    public List<PurPayStatVO> queryPayPriceWithWID(String siteid, String month, String warehouseid, String invcateid) {
        
        LOG.info( "--  queryPayPriceWithWID  Param{siteid:"+siteid+"month:"+month+"warehouseid:"+warehouseid+"invcateid:"+invcateid+"}--" );
        List<PurPayStatVO> result = new ArrayList<PurPayStatVO>(0);
        result = purPayDao.queryPayPriceWithWID( siteid, month, warehouseid, invcateid );
        LOG.info( "--  queryPayPriceWithWID  resultsize:"+result.size());
        return result;
    }

    @Override
    public PurPayStatVO queryCurMonthPayPriceWithWI(String siteId,String warehouseid, String invcateid) {
        PurPayStatVO result = null;
        String month = new SimpleDateFormat("yyyyMM").format( new Date() );
        List<PurPayStatVO> resultList = new ArrayList<PurPayStatVO>(0);
        resultList = this.queryPayPriceWithWID(siteId,month,warehouseid,invcateid);
        if ( 0 < resultList.size() ) {
            result = resultList.get( 0 );
        }
        return result;
    }
}
