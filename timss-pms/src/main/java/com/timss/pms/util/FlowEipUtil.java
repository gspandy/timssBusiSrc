package com.timss.pms.util;





import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.timss.inventory.vo.InvEipOpinionsEip;
import com.timss.pms.exception.PmsBasicException;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetFlowsBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetTask;
import com.yudean.itc.dto.support.Attachment;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.configs.MvcWebConfig;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.service.WorkflowService;

/**
 * 将工作流信息转换为移动eip所需信息的帮助类
 * @ClassName:     FlowEipUtil
 * @company: gdyd

 * @author:    黄晓岚
 * @date:   2014-10-22 上午10:26:06
 */
public class FlowEipUtil {
	private static final Logger LOGGER=Logger.getLogger(FlowEipUtil.class);
	private String processId;
	private WorkflowService workflowService;
	private UserInfo userInfo;
	private  ItcMvcService itcMvcService;
	private HistoryInfoService historyInfoService;
	
	private static String rollBackTaskId="rollback";
	private static String rollBackTaskName="退回";
	private static String nextTaskId="next";
	private static String nextTaskName="下一环节";
	
	private static String endStatus="end";
	
	private static HashMap<String, String> pmsFlowProperties= null;
	public FlowEipUtil(UserInfo userInfo,String processId,WorkflowService workflowService,ItcMvcService itcMvcService,HistoryInfoService historyInfoService){
		this.userInfo=userInfo;
		this.processId=processId;
		this.workflowService=workflowService;
		this.itcMvcService=itcMvcService;
		this.historyInfoService=historyInfoService;
	}
	public  List<RetFlowsBean> assembleFlows(){
		LOGGER.info("获取流程基本信息,processInstId:"+processId);
		List<RetFlowsBean> retFlowsBeans=new ArrayList<RetFlowsBean>();
		addNextTasks(retFlowsBeans);
		addRollBackTasks(retFlowsBeans);
		return retFlowsBeans;
	}
	
	private  void addRollBackTasks(List<RetFlowsBean> retFlowsBeans) {
		//初始化回退task的基本信息
		LOGGER.info("获取流程的历史信息，processInst"+processId);
		RetFlowsBean retFlowsBean=new RetFlowsBean();
		retFlowsBean.setId(rollBackTaskId);
		retFlowsBean.setName(rollBackTaskName);
		
		List<RetTask> retTasks=getPreviousTasks();
		if(retTasks!=null&&!retTasks.isEmpty()){
			retFlowsBean.setTask(getPreviousTasks());
			retFlowsBeans.add(retFlowsBean);
		}
		
		
	}

	private  List<RetTask> getPreviousTasks() {
		List<RetTask> retTasks=new ArrayList<RetTask>();
		List<HistoricTask> htList = workflowService.getFakePreviousTasks(processId);
		if(htList!=null&& !htList.isEmpty()){
			for(int i=0;i<htList.size();i++){
				HistoricTask ht=htList.get(i);
				//设置节点名称
				RetTask retTask=new RetTask();
				retTask.setTask(new RetKeyValue(ht.getTaskDefinitionKey(), ht.getName()));
				//设置任务的执行人
				List<RetKeyValue> rkvList = new ArrayList<RetKeyValue>();
				UserInfo ui = itcMvcService.getUserInfoById(ht.getAssignee());
				rkvList.add(new RetKeyValue(ht.getAssignee(),ui.getUserName()));
				retTask.setUser(rkvList);
				
				retTasks.add(retTask);
			}

		}
		
		return retTasks;
	}
	private  void addNextTasks(List<RetFlowsBean> retFlowsBeans){
		LOGGER.info("获取流程下一个节点信息，processInst"+processId);
		RetFlowsBean retFlowsBean=new RetFlowsBean();
		retFlowsBean.setId(nextTaskId);
		retFlowsBean.setName(nextTaskName);
		retFlowsBean.setTask(getNextTasks());
		
		retFlowsBeans.add(retFlowsBean);
	}
	public List<RetTask> getNextTasks() {
		
		List<RetTask> retTasks=new ArrayList<RetTask>();
		
		List<Task> tasks=workflowService.getActiveTasks(processId);
		//获取当前执行人正在审批的节点
		Task task=getCurrentTask(tasks);
		EipBranchFlowProcessUtil eipBranchFlowProcessUtil=new EipBranchFlowProcessUtil(workflowService);
		try {
			retTasks=eipBranchFlowProcessUtil.getNextElementsByFlowProperty(processId, task);
		} catch (Exception e) {
			throw new RuntimeException("获取供eip使用的流程信息失败", e);
		}
		return retTasks;
	}
	private Task getCurrentTask(List<Task> tasks) {
		
		return tasks.get(0);
	}
	
	public  boolean commit(String opinion,HashMap map,List owners,String taskKey){
		LOGGER.info("流程commint，processInstId"+processId);
		//预先设置流程变量
		EipBranchFlowProcessUtil eipBranchFlowProcessUtil=new EipBranchFlowProcessUtil(workflowService);
		try {
			eipBranchFlowProcessUtil.setFlowVariablesBeforeCommit(processId, taskKey);
		} catch (Exception e) {
			throw new RuntimeException("无法设置eip出来的流程变量",e);
		}
		List<Task> tasks=workflowService.getActiveTasks(processId);
		
		Task task=getCurrentTask(tasks);
		LOGGER.info("流程commint，taskId"+task.getId());
		String curUser=itcMvcService.getUserInfoScopeDatas().getUserId();
		
		Boolean flag = workflowService.complete(task.getId(), curUser,curUser, map, opinion, false);
		return flag;
		
	}
	
	public boolean rollBack(String opinion,String destTaskKey,String userId){
		LOGGER.info("流程回退,processInstId"+processId+",taskKey:"+destTaskKey+",opinion:"+opinion);
		Boolean flag=true;
		String assignee=itcMvcService.getUserInfoScopeDatas().getUserId();
		try {
			workflowService.rollback(processId, destTaskKey, opinion, assignee, assignee, userId);
		} catch (Exception e) {
			
			org.apache.log4j.Logger.getLogger(this.getClass()).error("回退失败",e);
			flag=false;
		} 
		return flag;
	}
	
	public static String  getPropertyValue(String key){
		initFlowProperties();
		return pmsFlowProperties.get(key);
	}
	
	private static synchronized void initFlowProperties(){
		if(pmsFlowProperties==null){
			pmsFlowProperties=new HashMap<String, String>();
			URL propertyFileURL = Thread.currentThread().getContextClassLoader()
	                .getResource( "pmsFlowProperty.properties" );
	        InputStream in = null;
	        try {
	            in = propertyFileURL.openStream();
	            Properties properties = new Properties();
	            properties.load( in );
	            
	            Enumeration<Object> enums = properties.keys();
	            // 2. 将配置文件中的名值对放进表中
	            while (enums.hasMoreElements()) {
	                String key = (String) enums.nextElement();
	                
	                String value = properties.getProperty( key );
	               
	                pmsFlowProperties.put( key, value );
	            }// END WHILE
	        } catch (Exception e) {
	            LOGGER.error("pms模块读取对eipm配置文件出错",e);
	        } finally {
	            if ( null != in ) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                    LOGGER.error("pms关闭对eipm的配置文件io出错",e);
	                }
	            }// END IF
	        }
		}
	}
	
	public static List<Object> getReturnForm(Object origin,String propertyName){
		List<Object> projectMaps=new ArrayList<Object>();
		//获取需要返回给eip的表单数据 格式
		String projectEipForm=getPropertyValue(propertyName);
	    //解析表单数据为一个表单字段
		String projectProperties[]=projectEipForm.split(",");
		for(int i=0;i<projectProperties.length;i++){
			 //解析表单字段，获取表单字段名称，以及字段值
			 String formField=projectProperties[i];
			 String []formFieldList=formField.split("##");
			 String formFieldName=formFieldList[0];
			 String formFieldValue=formFieldList[1];
			 
			 RetKeyValue retKeyValue=new RetKeyValue(formFieldName, (String) parseFormFieldValue(origin,formFieldValue));
			 projectMaps.add(retKeyValue);
		}
		return projectMaps;
	}
	private static Object parseFormFieldValue(Object origin,
			String formFieldValue) {
		if(origin==null){
			return null;
		}
		Class<?> c=origin.getClass();
		Object result=null;
		String methodName="get"+formFieldValue.substring(0,1).toUpperCase()+formFieldValue.substring(1);
		
		Method method;
		try {
			method = c.getMethod(methodName, null);
			result=method.invoke(origin, null);
			Class<?> returnType=method.getReturnType();
			result=parseReturnResultToString(result,returnType);
		}  catch (NoSuchMethodException e) {
			LOGGER.warn("类型"+c+"中没有找到"+methodName+"方法");
		} catch (Exception e) {
			
			throw new PmsBasicException("反射失败", e);
		}
		return result;
	}
	private static Object parseReturnResultToString(Object result,
			Class<?> returnType) {
		if(result==null){
			return null;
		}
		String name=returnType.getName();
		if("java.util.Date".equals(name)){
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
			result=sdf.format(((Date)result));
		}else if("java.lang.Double".equals(name)){
			BigDecimal bigDecimal=new BigDecimal((Double)result);
			result=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
		}
		
		return result;
	}
	
	/**
	 * 附件信息赋值
	 * @Title: assembleAttachements
	 * @param attach
	 * @return
	 */
	public static List<RetAttachmentBean> assembleAttachements(String attach,AttachmentMapper attachmentMapper) {
		List<RetAttachmentBean> retAttachmentBeans=new ArrayList<RetAttachmentBean>();
		if(StringUtils.isNotBlank(attach)){
			LOGGER.info("获取附件信息，attachList:"+attach);
			//解析得到附件id列表
			String []attachIds=attach.split(",");
			for(int i=0;i<attachIds.length;i++){
				//获取附件的详细信息并转化为eip待办接口需要的数据
				Attachment attachment=attachmentMapper.selectById(attachIds[i]);
				RetAttachmentBean retAttachmentBean=convertToRetAttachmentBean(attachment);
				retAttachmentBeans.add(retAttachmentBean);
			}
		}
		return retAttachmentBeans;
	}
	//将附件转为eip需要的格式
	private static RetAttachmentBean convertToRetAttachmentBean(Attachment attachment) {
		RetAttachmentBean retAttachmentBean=new RetAttachmentBean();
		retAttachmentBean.setFileName(attachment.getOriginalFileName());
		retAttachmentBean.setFileSize(getRealFileSizeFromAttachment(attachment.getFilesize()));
		retAttachmentBean.setFileSufx(getRealFileTypeFromAttachment(attachment.getFileType()));
		// 附件编码信息获取 同时处理url的域名信息
		String url=MvcWebConfig.serverBasePath+"upload?method=downloadFile&id="+attachment.getId();
		retAttachmentBean.setFileUrl(url);
		return retAttachmentBean;
	}
	
	/**
	 * 将字节转换为用户可识别的信息
	 * @Title: getRealFileSizeFromAttachment

	 * @param size
	 * @return
	 */
	private static String getRealFileSizeFromAttachment(Integer size){
		String result="";
		int KUnit=1024;
		int MUnit=KUnit*1024;
		if(size<KUnit){
			result="不到1K";
		}else if(size<MUnit){
			result=size/KUnit+"K";
		}else{
			result=size/MUnit+"M";
		}
		return result;
	}
	//处理fileType,将类似“。txt”转换为“txt”
	private static String getRealFileTypeFromAttachment(String fileType){
		if(StringUtils.isNotBlank(fileType)){
			
			if(fileType.indexOf(".")==0){
				fileType=fileType.substring(1);
			}
		}else{
			fileType="";
		}
		return fileType;
	}
	
	public  static boolean isEnd(String taskkey){
		return endStatus.equals(taskkey);
	}
	
	public  RetContentInLineBean assembleOpinions(String processId)  {
		LOGGER.info("获取流程审批意见。processInstId"+processId);
		RetContentInLineBean rcilb = new RetContentInLineBean(); 
		rcilb.setFoldable(true);
		rcilb.setIsShow(true);
		rcilb.setName("审批意见");
		rcilb.setType(Type.Approval);
		
		List<Object> rkvList = new ArrayList<Object>();
		if (null != processId && !"".equals(processId)) {
			//获取流程历史信息
//			List<HistoricTask> hiTaskList = workflowService.getFakePreviousTasks(processId);
//			if(hiTaskList!=null){
//				for(int i=hiTaskList.size()-1;i>=0;i--){
//					HistoricTask hiTask=hiTaskList.get(i);
//					InvEipOpinionsEip eob = new InvEipOpinionsEip();
//					//历史信息提供的是员工编码，要转换成中文
//					UserInfo ui = itcMvcService.getUserInfoById(hiTask.getAssignee());
//					eob.setWho(ui.getUserName());
//					//时间格式转换
//					eob.setWhen(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(hiTask.getEndTime()));
//					//审批信息
//					String comment = workflowService.getComment(hiTask.getId());
//					eob.setWhat(comment);
//					rkvList.add(eob);
//				}
//			}
//			
//			rcilb.setValue(rkvList);
			List<Map<String, Object>> list = historyInfoService.getHistoryComment(processId);
			for(Map<String, Object> map :list){
				InvEipOpinionsEip eob = new InvEipOpinionsEip();
				//历史信息提供的是员工编码，要转换成中文
				eob.setWho(String.valueOf(map.get("ASSIGNEENAME")));
				//时间格式转换
				eob.setWhen(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)map.get("END")));
				//审批信息
				eob.setWhat(String.valueOf(map.get("COMMENT_")));
				rkvList.add(eob);
			}
			rcilb.setValue(rkvList);

		}
		return rcilb;
	}
}
