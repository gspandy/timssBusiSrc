package com.timss.pms.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.timss.pms.service.ContractService;
import com.timss.pms.service.WFService;
import com.timss.pms.util.FlowEipUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.ContractDtlVo;
import com.yudean.interfaces.interfaces.EipMobileInterface;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetFlowsBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService;
import com.yudean.workflow.service.WorkflowService;
@Service
public class ContractAppToEipMobile implements EipMobileInterface {

	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	WFService wfService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	HistoryInfoService historyInfoService;
	
	private static final Logger LOGGER=Logger.getLogger(ContractToEipMobile.class);
	
	

	@Override
	public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
		RetContentBean efdb = new RetContentBean();
		 // 表单的sheetNo
        String flowNo = eipmobileparambean.getFlowNo();
        // 流程实例id
        String processId = eipmobileparambean.getProcessId();
        
        LOGGER.info("eip接口获取合同申请信息，processInstId"+processId+",flowNo:"+flowNo);
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 通过flowNo找到对应的id
        String id=wfService.queryBusinessIdByFlowId(processId);
    
        // 组装forms中数据
        ContractDtlVo contractDtlVo=contractService.queryContractById(id);
        List<RetContentInLineBean> forms = assembleFroms( userInfo, contractDtlVo ,processId);
        efdb.setContent( forms );

        // 组装附件中数据
        List<RetAttachmentBean> attachements = FlowEipUtil.assembleAttachements(contractDtlVo.getAttach(),attachmentMapper);
       
        efdb.setAttachments( attachements );

        // 组装flows中数据
        List<RetFlowsBean> flows = assembleFlows( userInfo, processId);
        efdb.setFlows( flows );
       
		return efdb;
	}
	private List<RetFlowsBean> assembleFlows(UserInfoScope userInfo,
			String processId) {
		FlowEipUtil flowEipUtil=new FlowEipUtil(userInfo, processId, workflowService, itcMvcService,historyInfoService);
		return flowEipUtil.assembleFlows();
	}

	private List<RetContentInLineBean> assembleFroms(UserInfoScope userInfo,
			ContractDtlVo contractDtlVo,String processId) {
		List<RetContentInLineBean> contentInLineBeans=new ArrayList<RetContentInLineBean>();
		RetContentInLineBean retContentInLineBean=new RetContentInLineBean();
	    
		retContentInLineBean.setFoldable(true);
		retContentInLineBean.setIsShow(true);
		retContentInLineBean.setType(Type.KeyValue);
		retContentInLineBean.setName("合同信息");
		retContentInLineBean.setValue(getContractMessage(contractDtlVo));
		
		contentInLineBeans.add(retContentInLineBean);
		

		
		FlowEipUtil flowEipUtil=new FlowEipUtil(null, processId, workflowService, itcMvcService,historyInfoService);
		contentInLineBeans.add(flowEipUtil.assembleOpinions(processId));
		return contentInLineBeans;
	}



	private List<Object> getContractMessage(ContractDtlVo contractDtlVo ) {
		ContractDtlVo contractDtlVoTmp=new ContractDtlVo();
		try {
			PropertyUtils.copyProperties(contractDtlVoTmp, contractDtlVo);
		} catch (Exception e) {
			LOGGER.warn("复制合同信息时出错",e);
			
		}
		
		List<AppEnum> ptypes=itcMvcService.getEnum("PMS_CONTRACT_TYPE");
		contractDtlVoTmp.setType(InitVoEnumUtil.getEnumVal(contractDtlVo.getType(), ptypes));
	
		List<Object> projectMaps=FlowEipUtil.getReturnForm(contractDtlVoTmp, "itc-contract-form");
		return projectMaps;
	}
	
	
	
	
	@Override
	public RetProcessBean processWorkflow(ParamProcessBean eipmobileparambean) {
		
		RetProcessBean emrb = new RetProcessBean();

        String flowId = eipmobileparambean.getFlowID();// 用户返回选择id
        String opinion = eipmobileparambean.getOpinion();// 用户填写意见
        String processId = eipmobileparambean.getProcessId();// 流程id
        
        String taskKey=eipmobileparambean.getTaskKey();
        List<String> nuser = eipmobileparambean.getNextUser();// 下一环节处理人员
       
        LOGGER.info("eip接口执行流程，processInstId"+processId+",taskKey:"+taskKey+",opinion:"+opinion);
        FlowEipUtil flowEipUtil=new FlowEipUtil(null, processId, workflowService, itcMvcService,historyInfoService);
        boolean flag = false;
        
        try {
           
            if ( "next".equals( flowId ) ) {// 提交下一环节
            	HashMap nextUserMap=new HashMap();
            	if(!FlowEipUtil.isEnd(taskKey)){
            		nextUserMap.put(taskKey, nuser);
            	}
            	
                flag = flowEipUtil.commit(opinion, nextUserMap, nuser,taskKey);
            } else if ( "rollback".equals( flowId ) ) {// 回退
                flag = flowEipUtil.rollBack(opinion, taskKey, (String)nuser.get(0));
            } 
            // 若流程提交操作成功，返回成功状态
            if ( flag ) {
                emrb.setRetcode( 1 );
                emrb.setRetmsg( "success" );
            } else {
                emrb.setRetcode( -1 );
            }
        } catch (Exception e) {
            LOGGER.error("eip 合同变更审批失败",e);
            emrb.setRetcode( -1 );
        }
        return emrb;
	}

}
