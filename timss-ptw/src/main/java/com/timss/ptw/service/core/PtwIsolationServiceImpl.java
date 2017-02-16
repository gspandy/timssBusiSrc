package com.timss.ptw.service.core;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwIsolationBean;
import com.timss.ptw.bean.PtwKeyBox;
import com.timss.ptw.bean.PtwType;
import com.timss.ptw.dao.PtwIsolationDao;
import com.timss.ptw.service.PtwIslMethodService;
import com.timss.ptw.service.PtwIsolationService;
import com.timss.ptw.service.PtwKeyBoxService;
import com.timss.ptw.service.PtwTypeService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 隔离证
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("ptwIsolationService")
@Transactional(propagation=Propagation.SUPPORTS)
public class PtwIsolationServiceImpl implements PtwIsolationService {

    private Logger log = Logger.getLogger( PtwIsolationServiceImpl.class );
    
    @Autowired
    private PtwIsolationDao ptwIsolationDao;
    
    @Autowired
    private PtwTypeService ptwTypeService;
    
    @Autowired
    private PtwKeyBoxService ptwKeyBoxService;
    
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PtwIslMethodService ptwIslMethodService;
    
    /**
     * 
     * @description:拿到用户信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private UserInfoScope getUserInfoScope(){
      //用户登录的站点
        UserInfoScope userInfoScope = null;
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
            return userInfoScope;
        } catch (Exception e) {
        	log.error(e);
        }
        return null;
    }
    
    
    /**
     * 
     * @description:插入隔离证
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param formData
     * @param safeDatas
     * @param elecDatas
     * @param compSafeDatas
     * @return:Map<String, Object>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdatePtwIsolation( String formData, String safeDatas, String elecDatas,String jxDatas, String compSafeDatas ) {
        PtwIsolationBean ptwIsolationBean = JsonHelper.fromJsonStringToBean( formData, PtwIsolationBean.class );
        String relateKeyBoxId = ptwIsolationBean.getRelateKeyBoxId();
        int count = 0;
        
        if( ptwIsolationBean.getId() > 0 ){
            //update PtwIsolation
            count = updatePtwIsolation( ptwIsolationBean );
            //删除datagrid
            int delCount = ptwIslMethodService.deletePtwIsolationItem( ptwIsolationBean.getId(),0 );
            log.info( "updatePtwIsolation  count = " + count + " ; delItemCount= " + delCount );
        }else{
            count = insertPtwIsolation( ptwIsolationBean );
        }
        ptwIsolationBean = queryPtwIsolationById( ptwIsolationBean.getId() );
        
        //更新关联钥匙箱号
        this.updateRelateKeyBox(ptwIsolationBean.getId(), relateKeyBoxId);
        
        //加入钥匙箱号用于展示
        if( ptwIsolationBean.getKeyBoxId() != null ){
            PtwKeyBox keyBox =  ptwKeyBoxService.queryPtwKeyBoxById( ptwIsolationBean.getKeyBoxId() );
            String keyBoxNo = keyBox.getKeyBoxNo();
            ptwIsolationBean.setKeyBoxNo( keyBoxNo );
        }
        
        int itemCount = ptwIslMethodService.insertOrUpdatePtwIsolationItem( ptwIsolationBean.getId(),0, safeDatas, elecDatas,jxDatas, compSafeDatas );
        log.info( "insertOrUpdatePtwIsolationItem itemCount = " + itemCount );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "count", count );
        map.put( "ptwIsolationBean", ptwIsolationBean );
        
        return map;
        
    }


    /**
     * 
     * @description:插入隔离证
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param ptwIsolationBean
     * @return:
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertPtwIsolation(PtwIsolationBean ptwIsolationBean) {
        String siteId = getUserInfoScope().getSiteId();
        String deptId = getUserInfoScope().getOrgId();
        String userId = getUserInfoScope().getUserId();
        String userName = getUserInfoScope().getUserName();
        Date currentDate = new Date();
        
        ptwIsolationBean.setSiteid( siteId );
        ptwIsolationBean.setDeptid( deptId );
        ptwIsolationBean.setCreateuser( userId );
        ptwIsolationBean.setCreateUserName( userName );
        
        ptwIsolationBean.setCreatedate( currentDate );
        ptwIsolationBean.setModifyuser( userId );
        ptwIsolationBean.setModifydate( currentDate );
        
        String num = getNum( ptwIsolationBean );
        ptwIsolationBean.setNo( num );
        
        //1有效  0无效
        ptwIsolationBean.setYxbz( 1 );
        //默认300
        int islStatus = 300;
        ptwIsolationBean.setStatus( islStatus );
        
        int insertCount = ptwIsolationDao.insertPtwIsolation( ptwIsolationBean );
        ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwIsolationBean.getKeyBoxId(), islStatus);
        return insertCount;
    }


    /**
     * 
     * @description:构造序列号
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param ptwIsolationBean
     * @return:String
     */
    private String getNum(PtwIsolationBean ptwIsolationBean) {
        Calendar calendar = Calendar.getInstance();  
        int year = calendar.get( Calendar.YEAR );
        
        int wtTypeId =  ptwIsolationBean.getWtTypeId();
        PtwType ptwType = ptwTypeService.queryPtwTypeById( wtTypeId );
        String preNum = "ISL-" + ptwType.getTypeCode() + "-" + year + "-";
        
        String siteId = getUserInfoScope().getSiteId();
        String maxNum = ptwIsolationDao.queryMaxNumPtwIsolationItemBySiteId( siteId );
        String postNum = "00000";
        if( StringUtils.isNotBlank( maxNum ) ){
            String[] arr = maxNum.split( "-" );
            String lastStr = arr[ arr.length - 1 ];
            Integer last = Integer.parseInt( lastStr );
            ++last;
            postNum = postNum.substring( 0, 5 - last.toString().length() ) + last;
        }else{
            postNum = "00001";
        }
        
        return preNum + postNum;
    }


    /**
     * 
     * @description:查找所有的隔离证
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param pageVo
     * @return:PtwIsolationBean
     */
    @Override
    public Page<PtwIsolationBean> queryPtwIsolationList(Page<PtwIsolationBean> page) {
        List<PtwIsolationBean> list = ptwIsolationDao.queryPtwIsolationList( page );
        page.setResults( list );
        return page;
    }

    /**
     * 
     * @description:更新隔离证
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updatePtwIsolation(PtwIsolationBean vo) {
        String userId = getUserInfoScope().getUserId();
        Date currentDate = new Date();
        
        vo.setModifydate( currentDate );
        vo.setModifyuser( userId );
        
        int count = ptwIsolationDao.updatePtwIsolation( vo );
        return count;
    }

    /**
     * 
     * @description:通过ID查找隔离证
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param id
     * @return:PtwIsolationBean
     */
    @Override
    public PtwIsolationBean queryPtwIsolationById(int id) {
        PtwIsolationBean ptwIsolationBean = ptwIsolationDao.queryPtwIsolationById( id );
        //加入钥匙箱号
        if( ptwIsolationBean.getKeyBoxId() != null){
            PtwKeyBox keyBox =  ptwKeyBoxService.queryPtwKeyBoxById( ptwIsolationBean.getKeyBoxId() );
            String keyBoxNo = keyBox.getKeyBoxNo();
            ptwIsolationBean.setKeyBoxNo( keyBoxNo );
        }
        return ptwIsolationBean;
    }


    /**
     * 
     * @description:更新隔离证状态
     * @author: fengzt
     * @createDate: 2014年11月5日
     * @param id
     * @param status
     * @param issueSuper
     * @param issueSuperNo
     * @param finElecInfo
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updatePtwIsolationStatusById(int id, int status, String issueSuper, String issueSuperNo, String finElecInfo,Integer keyBoxId ) {
        Map<String , Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        map.put( "status", status );
        
        if( StringUtils.isNotBlank( issueSuper ) ){
            map.put( "issueSuper", issueSuper );
        }
        
        if( StringUtils.isNotBlank( issueSuperNo ) ){
            map.put( "issueSuperNo", issueSuperNo );
        }
        
        if( StringUtils.isNotBlank( finElecInfo ) ){
            map.put( "finElecInfo", finElecInfo );
        }
        
        map = setIsolationInfo( status, map );
        
        int result = ptwIsolationDao.updatePtwIsolationStatusById( map );
        ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(keyBoxId,status);
        return result;
    }

    /**
     * 
     * @description:根据状态设置相关人员信息
     * @author: fengzt
     * @createDate: 2014年11月6日
     * @param status
     * @param map
     * @return:Map
     */
    private Map<String, Object> setIsolationInfo(int status, Map<String, Object> map) {
        String userId = getUserInfoScope().getUserId();
        String userName = getUserInfoScope().getUserName();
        Date currentDate = new Date();
        if( 400 == status ){
            //签发
            map.put( "issuerNo", userId );
            map.put( "issuer", userName );
            map.put( "issuedTime", currentDate );
        }else if( 500 == status ){
            //许可
            map.put( "executerNo", userId );
            map.put( "executer", userName );
            map.put( "executerTime", currentDate );
        }else if( 600 == status ){
            //结束
            map.put( "withDrawNo", userId );
            map.put( "withDraw", userName );
            map.put( "withDrawTime", currentDate );
        }else if( 700 == status ){
            //终结
            map.put( "removerNo", userId );
            map.put( "remover", userName );
            map.put( "removerTime", currentDate );
        }else if( 800 == status ){
            //作废
            map.put( "cancelerNo", userId );
            map.put( "canceler", userName );
            map.put( "cancelerTime", currentDate );
        }
        return map;
    }

    /**
     * 
     * @description:更新隔离证备注
     * @author: fengzt
     * @createDate: 2014年11月7日
     * @param id
     * @param remark
     * @return:int
     */
    @Override
    public int updatePtwIsolationRemarkById(int id, String remark) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        map.put( "remark", remark );
        
        return ptwIsolationDao.updatePtwIsolationRemarkById( map );
    }


	@Override
	public int updateRelateKeyBox(int id, String relateKeyBoxId) {
		return ptwIsolationDao.updateRelateKeyBox(id, relateKeyBoxId);
	}


	@Override
	public List<PtwIsolationBean> queryByKeyBoxId(int keyBoxId) {
		String status = "300,400,500,600";
		return queryByKeyBoxId(keyBoxId,status);
	}
	
	@Override
	public List<PtwIsolationBean> queryByKeyBoxId(int keyBoxId,String status) {
		List<PtwIsolationBean> list = ptwIsolationDao.queryByKeyBoxId(keyBoxId,status);
		for (PtwIsolationBean ptwIsolationBean : list) {
			if(ptwIsolationBean.getRelateKeyBoxId() != null && !ptwIsolationBean.getRelateKeyBoxId().equals("")){
				List<PtwKeyBox> keyBoxes = ptwKeyBoxService.queryByIds(ptwIsolationBean.getRelateKeyBoxId());
				StringBuffer keyBoxNos = new StringBuffer();
				for (PtwKeyBox ptwKeyBox : keyBoxes) {
					keyBoxNos.append(ptwKeyBox.getKeyBoxNo()).append(",");
				}
				ptwIsolationBean.setKeyBoxNo(keyBoxNos.substring(0, keyBoxNos.length() - 1).toString());
			}
		}
		return list;
	}


	@Override
	public List<PtwIsolationBean> queryByRelateKeyBoxId(int keyBoxId,String status) {
		List<PtwIsolationBean> list = ptwIsolationDao.queryByRelateKeyBoxId(keyBoxId, status);
		return list;
	}

}
