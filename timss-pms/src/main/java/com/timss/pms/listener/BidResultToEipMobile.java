package com.timss.pms.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.service.BidResultService;
import com.timss.pms.service.WFService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.FlowEipUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.BidResultDtlVo;
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

public class BidResultToEipMobile implements EipMobileInterface{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	BidResultService bidResultService;
	@Autowired
	WFService wfService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	HistoryInfoService historyInfoService;
	private static final Logger LOGGER=Logger.getLogger(BidResultToEipMobile.class);
	
	

	@Override
	public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
		RetContentBean efdb = new RetContentBean();
		 // 表单的sheetNo
        String flowNo = eipmobileparambean.getFlowNo();
        // 流程实例id
        String processId = eipmobileparambean.getProcessId();
        
        LOGGER.info("eip接口获取招标结果流程信息，processInstId"+processId+",flowNo:"+flowNo);
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 通过flowNo找到对应的id
        String id=wfService.queryBusinessIdByFlowId(processId);

        // 组装forms中数据
        BidResultDtlVo bidResultDtlVo=bidResultService.queryBidResultById(Integer.parseInt(id));
        List<RetContentInLineBean> forms = assembleFroms( userInfo, bidResultDtlVo ,processId);
        efdb.setContent( forms );

        // 组装附件中数据
        List<RetAttachmentBean> attachements = FlowEipUtil.assembleAttachements(bidResultDtlVo.getAttach(),attachmentMapper);
        
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
	/**
	 * 附件信息赋值
	 * @Title: assembleAttachements
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param attach
	 * @return
	 */
	private List<RetAttachmentBean> assembleAttachements(String attach) {
		List<RetAttachmentBean> retAttachmentBeans=AttachUtil.changeAttachStringToEipAttach(attachmentMapper, attach);
		return retAttachmentBeans;
	}
	private List<RetContentInLineBean> assembleFroms(UserInfoScope userInfo,
			BidResultDtlVo bidResultDtlVo,String processId) {
		List<RetContentInLineBean> contentInLineBeans=new ArrayList<RetContentInLineBean>();
		RetContentInLineBean retContentInLineBean=new RetContentInLineBean();
	    
		retContentInLineBean.setFoldable(true);
		retContentInLineBean.setIsShow(true);
		retContentInLineBean.setType(Type.KeyValue);
		retContentInLineBean.setName("招标信息");
		retContentInLineBean.setValue(getBidResultMessage(bidResultDtlVo));
		
		contentInLineBeans.add(retContentInLineBean);
		
		FlowEipUtil flowEipUtil=new FlowEipUtil(null, processId, workflowService, itcMvcService,historyInfoService);
		contentInLineBeans.add(flowEipUtil.assembleOpinions(processId));
		return contentInLineBeans;
	}
	private List<Object> getBidResultMessage(BidResultDtlVo bidResultDtlVo ) {
		BidResultDtlVo bidResultDtlVoTmp=new BidResultDtlVo();
		try {
			PropertyUtils.copyProperties(bidResultDtlVoTmp, bidResultDtlVo);
		} catch (Exception e) {
			LOGGER.warn("复制项目信息时出错",e);
			
		}
		
		List ptypes=itcMvcService.getEnum("PMS_BID_TYPE");
		bidResultDtlVoTmp.setType(InitVoEnumUtil.getEnumVal(bidResultDtlVoTmp.getType(), ptypes));
		
		List<Object> projectMaps=FlowEipUtil.getReturnForm(bidResultDtlVoTmp, "itc-bidresult-form");
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
       
        FlowEipUtil flowEipUtil=new FlowEipUtil(null, processId, workflowService, itcMvcService,historyInfoService);
        boolean flag = false;
        LOGGER.info("eip接口执行流程，processInstId"+processId+",taskKey:"+taskKey+",opinion:"+opinion);
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
