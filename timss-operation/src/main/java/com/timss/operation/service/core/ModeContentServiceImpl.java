package com.timss.operation.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.ModeContentBean;
import com.timss.operation.dao.ModeContentDao;
import com.timss.operation.dao.ModeDao;
import com.timss.operation.service.ModeContentService;
import com.timss.operation.vo.ModeAssetVo;
import com.timss.operation.vo.ModeContentVo;
import com.timss.operation.vo.NoteBaseVo;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 运行方式
 * @description: {desc}
 * @company: gdyd
 * @className: ModeContentServiceImpl.java
 * @author: fengzt
 * @createDate: 2015年11月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("modeContentService")
public class ModeContentServiceImpl implements ModeContentService {
    
    private Log log = LogFactory.getLog( ModeContentServiceImpl.class );
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private ModeContentDao modeContentDao;

    @Autowired
    private ModeDao modeDao;
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertOrUpdateModeContentFromMode(int dutyId, int jobsId, int handoverId){
    	//查询系统的运行方式及值
    	List<ModeAssetVo> modeAssetVos = modeDao.queryModeAssetByJobId( jobsId, null );
        //构造出List ModeContentBean
        List<ModeContentBean> beans = constructModeContent( modeAssetVos, dutyId, jobsId, handoverId );
        //删除相关的运行方式
        int delCount = deleteModeContent( dutyId, jobsId, handoverId, null );
        log.info( "删除运行方式条数：" + delCount );
        //批量插入运行方式
        if( beans != null && beans.size() > 0  ){
            return insertBatchModeContent( beans );
        }
        return 0;
    }
    
    /**
     * 
     * @description:批量插入ModeContentBean
     * @author: fengzt
     * @createDate: 2015年11月4日
     * @param beans
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertBatchModeContent(List<ModeContentBean> beans) {
        return modeContentDao.insertBatchModeContent( beans );
    }

    /**
     * 
     * @description:删除modeContent
     * @author: fengzt
     * @createDate: 2015年11月4日
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @param team 
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int deleteModeContent(int dutyId, int jobsId, int handoverId, String team) {
        return modeContentDao.deleteModeContent( dutyId, jobsId, handoverId, team );
    }

    /**
     * 
     * @description:构造出List ModeContentBean
     * @author: fengzt
     * @createDate: 2015年11月3日
     * @param assetMap
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @return:List<ModeContentBean>
     */
    private List<ModeContentBean> constructModeContent(List<ModeAssetVo> modeAssetVos, int dutyId, int jobsId,
            int handoverId) {
        List<ModeContentBean> beans = new ArrayList<ModeContentBean>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        //构造bean
        for (ModeAssetVo vo : modeAssetVos) {
        	ModeContentBean bean = new ModeContentBean();
            bean.setContentId( UUIDGenerator.getUUID()  );
            bean.setAssetId( vo.getAssetId() );
            
            String modeVal=vo.getNowModeVal();
            if(modeVal==null||"".equals(modeVal)){//无值则从选项中赋值第一个
            	if(vo.getModeVal()==null||"".equals(vo.getModeVal())){//无可用选项则跳过
            		continue;
            	}else{
            		String[] vals=vo.getModeVal().split("/");
            		modeVal=vals[0];
            	}
            }
            bean.setContent( modeVal );
            
            bean.setDutyId( dutyId );
            bean.setJobsId( jobsId );
            bean.setHandoverId( handoverId );
            
            bean.setIsDelete( "N" );
            bean.setDeptid( userInfoScope.getOrgId() );
            bean.setSiteid( userInfoScope.getSiteId() );
            
            bean.setCreateuser( userInfoScope.getUserId() );
            bean.setCreateuser( userInfoScope.getUserName() );
            bean.setCreatedate( new Date() );
            
            beans.add( bean );
		}
        
        return beans;
    }

    /**
     * 
     * @description:通过 dutyId, jobsId handoverId 联合查询
     * @author: fengzt
     * @createDate: 2015年11月5日
     * @param dutyId
     * @param jobsId
     * @param handoverId
     * @param team 
     * @return:List<ModeContentVo>
     */
    @Override
    public List<ModeContentVo> queryModeContentByDutyJobsHandover(int dutyId, int jobsId,
            int handoverId, String team ) {
        return modeContentDao.queryModeContentByDutyJobsHandover( dutyId, jobsId, handoverId, team );
    }

    /**
     * 
     * @description:保存动态表单temp函数
     * @author: fengzt
     * @createDate: 2015年11月5日
     * @param baseVo
     * @return:int
     */
    @Override
    public int insertOrUpdateDynamicTemp(NoteBaseVo baseVo) {
        return modeContentDao.insertOrUpdateDynamicTemp( baseVo );
    }

}
