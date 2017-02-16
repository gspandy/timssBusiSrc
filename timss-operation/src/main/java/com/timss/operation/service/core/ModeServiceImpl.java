package com.timss.operation.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.ModeBean;
import com.timss.operation.dao.ModeDao;
import com.timss.operation.service.ModeService;
import com.timss.operation.vo.ModeAssetVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 运行方式设置service Implements
 * @description: 
 * @company: gdyd
 * @className: ModeServiceImpl.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Service("modeService")
@Transactional(propagation=Propagation.SUPPORTS)
public class ModeServiceImpl implements ModeService {

    @Autowired
    private ModeDao modeDao;
    
    @Autowired
    private ItcMvcService itcMvcService;


    /**
     * @description:更新运行方式设置表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param mode:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateMode(ModeBean mode) {
        
        return modeDao.updateMode( mode );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateNowModeVal(Integer jobId,String team,String assetId,String val) {
    	ModeBean bean=new ModeBean();
    	bean.setAssetId(assetId);
    	bean.setJobId(jobId);
    	bean.setTeam(team);
    	bean.setNowModeVal(val);
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	bean.setModifyuser( userInfoScope.getUserId() );
        bean.setModifyUserName( userInfoScope.getUserName() );
        bean.setModifydate( new Date() );
        return modeDao.updateNowModeVal( bean );
    }
    
    /**
     * @description:通过Id拿到运行方式设置表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id
     * @return:Mode
     */
    public ModeBean queryModeById(String id) {

        return modeDao.queryModeById( id );
    }

    /**
     * @description:通过ID 删除 mode
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteModeById(String id) {

        return modeDao.deleteModeById( id );
    }


    /**
     * @description:运行方式设置列表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Mode>
     */
    public Page<ModeBean> queryAllModeList(Page<ModeBean> paramsPage) {

        paramsPage.setParameter( "isDelete", "N" );
        List<ModeBean> modeList = modeDao.queryAllModeList( paramsPage );
        
        paramsPage.setResults( modeList );
        return paramsPage;
    }
    
    /**
     * 
     * @description:通过jobId查询ModeAsset
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param jobId
     * @param team
     * @return:List<ModeAssetVo>
     */
    @Override
    public List<ModeAssetVo> queryModeAssetByJobId(int jobId, String team) {
        return modeDao.queryModeAssetByJobId( jobId, team );
    }

    /**
     * 
     * @description:通过设备ID查询设备明细
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param assetId
     * @return:ModeAssetVo
     */
    @Override
    public ModeAssetVo queryModeAssetByAssetId(String assetId) {
        return modeDao.queryModeAssetByAssetId( assetId );
    }

    /**
     * 
     * @description:插入or更新
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param modeAssetVos
     * @return:int
     */
    @Override
    public int insertOrUpdateMode(List<ModeAssetVo> modeAssetVos,Integer jobId) {
        if(jobId==0){
        	if(modeAssetVos == null||modeAssetVos.size() <= 0){
        		return 0;
        	}else{
        		jobId = modeAssetVos.get( 0 ).getJobId();
        	}
        }
        //现在已经有的
        List<ModeAssetVo> exitResult = queryModeAssetByJobId(jobId,"");
        //比较拿出更新的
        //比较拿出新增的
        //比较拿出删除的
        int count = insertOrUpdateDiffMode( modeAssetVos, exitResult );
        return count;
    }

    /**
     * 
     * @description:比较差异增删改查Mode
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param modeAssetVos
     * @param exitResult
     * @return:
     */
    private int insertOrUpdateDiffMode(List<ModeAssetVo> modeAssetVos, List<ModeAssetVo> exitResult) {
        List<ModeAssetVo> insertResult = new ArrayList<ModeAssetVo>();
        List<ModeAssetVo> updateResult = new ArrayList<ModeAssetVo>();
        List<ModeAssetVo> deleteResult = new ArrayList<ModeAssetVo>();
        
        //找出新增、更新列表
        for( ModeAssetVo pageVo : modeAssetVos ){
            boolean flag = isExitModeAssetVoList( pageVo, exitResult );
            if( flag ){
                updateResult.add( pageVo );
            }else{
                insertResult.add( pageVo );
            }
        }
        
        //找出删除列表
        for( ModeAssetVo exitVo : exitResult ){
            boolean flag = isExitModeAssetVoList( exitVo, modeAssetVos );
            if( !flag ){
                deleteResult.add( exitVo );
            }
        }
        
        int insertCount = insertMode( insertResult );
        int updateCount = updateMode( updateResult );
        int deleteCount = deleteMode( deleteResult );
        
        return insertCount + updateCount + deleteCount;
    }
    
    /**
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param insertResult
     * @return:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int deleteMode(List<ModeAssetVo> insertResult) {
        int count = 0;
        for( ModeAssetVo vo : insertResult ){
            ModeBean bean = modeAssetVoToModeBean( vo );
            count += modeDao.deleteMode( bean );
        }
        
        return count;
    }

    /**
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param insertResult
     * @return:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int updateMode(List<ModeAssetVo> insertResult) {
        int count = 0;
        for( ModeAssetVo vo : insertResult ){
            ModeBean bean = modeAssetVoToModeBean( vo );
            count += modeDao.updateMode( bean );
        }
        
        return count;
    }

    /**
     * 
     * @description:新增Mode
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param insertResult
     * @return:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertMode(List<ModeAssetVo> insertResult) {
        /*List<ModeBean> modeList = new ArrayList<ModeBean>();
        for( ModeAssetVo vo : insertResult ){
            ModeBean bean = modeAssetVoToModeBean( vo );
            modeList.add( bean );
        }
        
        if( insertResult != null && insertResult.size() > 0 ){
            return modeDao.insertBatchMode( modeList );
        }*/
        
        int count = 0;
        for( ModeAssetVo vo : insertResult ){
            ModeBean bean = modeAssetVoToModeBean( vo );
            count += modeDao.insertMode( bean );
        }
        
        return count;
    }

    /**
     * 
     * @description:转换成ModeBean
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param vo
     * @return:ModeBean
     */
    private ModeBean modeAssetVoToModeBean(ModeAssetVo vo) {
        ModeBean bean = new ModeBean();
        bean.setModeId( UUIDGenerator.getUUID() );
        bean.setAssetId( vo.getAssetId() );
        bean.setAssetName( vo.getAssetName() );
        bean.setJobId( vo.getJobId() );
        
        bean.setModeVal( vo.getModeVal() );
        if(vo.getModeVal()!=null&&!"".equals(vo.getModeVal())&&(
        		vo.getNowModeVal()==null||"".equals(vo.getNowModeVal())||(vo.getModeVal()+"/").indexOf(vo.getNowModeVal()+"/")<0)){//为空或错误初值则重新赋初值
        	String[]values=vo.getModeVal().split("/");
        	bean.setNowModeVal(values[0]);
        }
        bean.setTeam( StringUtils.trimToEmpty( vo.getTeam() ) );
        bean.setSortNum(vo.getSortNum());
        bean.setIsDelete( "N" );
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        bean.setCreateuser( userInfoScope.getUserId() );
        bean.setCreateUserName( userInfoScope.getUserName() );
        bean.setCreatedate( new Date() );
        bean.setDeptid( userInfoScope.getOrgId() );
        bean.setSiteid( userInfoScope.getSiteId() );
        
        bean.setModifyuser( userInfoScope.getUserId() );
        bean.setModifyUserName( userInfoScope.getUserName() );
        bean.setModifydate( new Date() );
        
        return bean;
    }

    /**
     * 
     * @description:比较Vo是否存在于list
     * @author: fengzt
     * @createDate: 2015年11月2日
     * @param vo
     * @param exitResult
     * @return:boolean
     */
    private boolean isExitModeAssetVoList( ModeAssetVo vo, List<ModeAssetVo> list ){
        for( ModeAssetVo exitVo : list ){
            if( StringUtils.equals( vo.getAssetId(), exitVo.getAssetId() )
                    && vo.getJobId() == exitVo.getJobId()){
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @description:通过工种查找Mode分组信息
     * @author: fengzt
     * @createDate: 2015年11月11日
     * @param jobsId
     * @return:List<String>
     */
    @Override
    public List<String> queryModeTeamByJobsId( int jobsId ){
        return modeDao.queryModeTeamByJobsId( jobsId );
    }
    
}