package com.timss.attendance.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.timss.attendance.bean.SealApplyBean;
import com.timss.attendance.dao.SealApplyDao;
import com.timss.attendance.service.AtdAttachService;
import com.timss.attendance.service.SealApplyService;
import com.timss.attendance.vo.SealApplyVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 用章申请
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016-08-30
 * @updateUser: 890199
 * @version:1.0
 */
@Service("sealApplyService")
@Transactional(propagation=Propagation.SUPPORTS)
public class SealApplyServiceImpl implements SealApplyService{
	private Logger log = Logger.getLogger( SealApplyServiceImpl.class );
	/**
	 * 注入DAO
	 */
	@Autowired 
	private SealApplyDao sealApplyDao;
	@Autowired
    private ItcMvcService timssService;
	@Autowired
	private HomepageService homepageService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private AtdAttachService attachService;
	
	/**
	 * @description: 查看用章申请列表
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: page
	 * @return:
	 * @throws Exception:
	 */
	@Override
	public List<SealApplyVo> queryAllSealApply(Page<SealApplyVo> page){
		return sealApplyDao.queryAllSealApply(page);
	}
	
	/**
	 * @description: 查看用章申请详情
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: saId
	 * @return:
	 * @throws Exception:
	 */
	@Override
	public List<SealApplyVo> querySealApplyById(String saId){
		return sealApplyDao.querySealApplyById(saId);
	}
	
	/**
	 * @description: 提交用章申请
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	@Override
	public int commitSealApply(SealApplyBean sealApplyBean) throws Exception{
		String[] fileIds = sealApplyBean.getFileIds();
        attachService.delete("sealApply", sealApplyBean.getSaId(), null);
        if(null !=fileIds &&fileIds.length != 0){
        	attachService.insert("sealApply", sealApplyBean.getSaId(), fileIds);
        }
		return sealApplyDao.insertSealApply(sealApplyBean);
	}
	
	/**
	 * @description: 暂存用章申请
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	@Override
	public int saveSealApply(SealApplyBean sealApplyBean) throws Exception{
		String[] fileIds = sealApplyBean.getFileIds();
        attachService.delete("sealApply", sealApplyBean.getSaId(), null);
        if(null !=fileIds &&fileIds.length != 0){
        	attachService.insert("sealApply", sealApplyBean.getSaId(), fileIds);
        }
		return sealApplyDao.insertSealApply(sealApplyBean);
	}
	
	/**
	 * @description: 更新申请单状态
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	public int updateSealApply(SealApplyBean sealApplyBean) throws Exception{
		String[] fileIds = sealApplyBean.getFileIds();
        attachService.delete("sealApply", sealApplyBean.getSaId(), null);
        if(null !=fileIds &&fileIds.length != 0){
        	attachService.insert("sealApply", sealApplyBean.getSaId(), fileIds);
        }
		return sealApplyDao.updateSealApply(sealApplyBean);
	}
	
	/**
	 * @description: 作废用章申请
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	public Map<String,Object> invalidSealApply(String saId){
		UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
    	//更新状态信息
    	SealApplyBean sab = new SealApplyBean();
    	sab.setSaId(saId);
    	sab.setStatus("obsolete");
    	sab.setModifydate(new Date());
    	sab.setModifyuser(userInfoScope.getUserId());
    	int res = -1;
    	//状态，删除标志位 未设置
    	res = sealApplyDao.updateSealApply(sab);
    	//获取流程实例id  终止流程
    	List<SealApplyVo> list = sealApplyDao.querySealApplyById(saId);
    	SealApplyVo sav = list.get(0);
    	String userId=userInfoScope.getUserId();
    	String instanceId = sav.getInstanceId();
        log.info( "流程instantId = " + instanceId + "---流水号为： " + sav.getSaNo() );
        List<Task> activities = workflowService.getActiveTasks( instanceId );
        Task task = activities.get(0);
        String taskId = task.getId();
        workflowService.stopProcess( taskId, userId, userId, "作废" );
        //删除待办
        homepageService.complete( instanceId, userInfoScope, "作废" );//流程状态为作废
    	Map<String,Object> result = new HashMap<String,Object>();
    	if(res==1){
    		result.put("result", "success");
    	}else{
    		result.put("result", "fail");
    	}
    	
    	return result;
	}
	
	/**
	 * @description: 删除用章申请(物理删除)
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: saId
	 * @return:
	 * @throws Exception:
	 */
	public int removeSealApply(String saId){
		return sealApplyDao.removeSealApply(saId);
	}
}
