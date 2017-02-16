
package com.timss.pms.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.PayDao;
import com.timss.pms.service.PayService;
import com.timss.pms.service.WFService;
import com.timss.pms.util.FlowEipUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.PayDtlVo;
import com.timss.pms.vo.PayplanVo;
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
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 项目立项与eip接口
 * @ClassName:     ProjectToEipMobile
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-10-21 上午10:10:56
 */
@Service

public class PayToEipMobile implements EipMobileInterface{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	PayService payService;
	@Autowired
	WFService wfService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	HistoryInfoService historyInfoService;
	@Autowired
	PayDao payDao;
	private static final Logger LOGGER=Logger.getLogger(PayToEipMobile.class);
	
	

	@Override
	public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
		RetContentBean efdb = new RetContentBean();
		 // 表单的sheetNo
        String flowNo = eipmobileparambean.getFlowNo();
        // 流程实例id
        String processId = eipmobileparambean.getProcessId();
        
        LOGGER.info("eip接口获取付款信息，processInstId"+processId+",flowNo:"+flowNo);
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 通过flowNo找到对应的id
        String id=wfService.queryBusinessIdByFlowId(processId);

        // 组装forms中数据
        PayDtlVo payDtlVoTmp=payDao.queryPayById(Integer.parseInt(id));
        PayDtlVo payDtlVo=payService.queryPayByPayplanId(payDtlVoTmp.getPayplanId().toString(), payDtlVoTmp.getContractId().toString());
        
        List<RetContentInLineBean> forms = assembleFroms( userInfo, payDtlVo ,processId);
        efdb.setContent( forms );

        // 组装附件中数据
        List<RetAttachmentBean> attachements = FlowEipUtil.assembleAttachements(payDtlVo.getAttach(),attachmentMapper);
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
			PayDtlVo payDtlVo,String processId) {
		List<RetContentInLineBean> contentInLineBeans=new ArrayList<RetContentInLineBean>();
		RetContentInLineBean retContentInLineBean=new RetContentInLineBean();
	    
		retContentInLineBean.setFoldable(true);
		retContentInLineBean.setIsShow(true);
		retContentInLineBean.setType(Type.KeyValue);
		retContentInLineBean.setName("付款信息");
		retContentInLineBean.setValue(getPayMessage(payDtlVo));
		
		contentInLineBeans.add(retContentInLineBean);
		
		FlowEipUtil flowEipUtil=new FlowEipUtil(null, processId, workflowService, itcMvcService,historyInfoService);
		contentInLineBeans.add(flowEipUtil.assembleOpinions(processId));
		return contentInLineBeans;
	}
	private List<Object> getPayMessage(PayDtlVo payDtlVo ) {
		PayDtlVo payDtlVoTmp=new PayDtlVo();
		try {
			PropertyUtils.copyProperties(payDtlVoTmp, payDtlVo);
		} catch (Exception e) {
			LOGGER.warn("复制信息时出错",e);
			
		}
		
		List ptypes=itcMvcService.getEnum("PMS_CONTRACT_TYPE");
		payDtlVoTmp.setType(InitVoEnumUtil.getEnumVal(payDtlVo.getType(), ptypes));
		
		ptypes=itcMvcService.getEnum("PMS_PAY_PAYWAY");
		payDtlVoTmp.setPayway(InitVoEnumUtil.getEnumVal(payDtlVo.getPayway(), ptypes));
		
		BigDecimal bepayPercent=new BigDecimal(payDtlVo.getBepay()/payDtlVo.getTotalSum() *100);
		payDtlVoTmp.setBepayPercent(bepayPercent.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		
		BigDecimal aPercent=new BigDecimal(payDtlVo.getActualpay()/payDtlVo.getTotalSum() *100);
		payDtlVoTmp.setActualpayPercent(aPercent.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		
		List payplanVos=payDtlVo.getPayplanVos();
		payDtlVoTmp.setPayStage(getPayStageById(payplanVos, payDtlVo.getPayplanId().toString()));
		
		List<Object> projectMaps=FlowEipUtil.getReturnForm(payDtlVoTmp, "itc-pay-form");
		return projectMaps;
	}
	
	private String getPayStageById(List<PayplanVo> payplanVos,String payplanId){
		String type="";
		if(payplanVos!=null){
			for(int i=0;i<payplanVos.size();i++){
				if(payplanId.equals(payplanVos.get(i).getId().toString())){
					type=payplanVos.get(i).getTypeName();
				}
			}
		}
		return type;
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
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
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
        	LOGGER.error(e);
            emrb.setRetcode( -1 );
        }
        return emrb;
	}

	
}
