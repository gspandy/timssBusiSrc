package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.ptw.bean.PtwIsolationPoint;
import com.timss.ptw.dao.PtwIsolationPointDao;
import com.timss.ptw.service.PtwIsolationPointService;
import com.timss.ptw.vo.IsMethodPointVo;
import com.timss.ptw.vo.PtwIsolationPointVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwIsolationPointServiceImpl implements PtwIsolationPointService {
    private static final Logger log = Logger.getLogger(PtwIsolationPointServiceImpl.class);

    @Autowired
    PtwIsolationPointDao  ptwIsolationPointDao;
    @Autowired
    ItcMvcService itcMvcService;
    
    @Autowired
    @Qualifier("assetInfoServiceForIsolatedPointImpl")
    private AssetInfoService assetInfoService;
    

	@Override
	public ArrayList<PtwIsolationPointVo> queryBeanByIsolationPointNo(String pointNo ,String siteId) {
		return ptwIsolationPointDao.queryBeanByIsolationPointNo(pointNo,siteId);
	}

	  /**
         * 
         * @description:插入或者更新隔离点
         * @author: fengzt
         * @createDate: 2015年1月5日
         * @param paramsDataMap
         * @param assetBean:
         * @return HashMap<String, Object>
	 * @throws Exception 
         */
        @Override
        @Transactional(propagation=Propagation.REQUIRED)
        public HashMap<String, Object> insertOrUpdatePtwIsolationPoint(HashMap<String, String> paramsDataMap, AssetBean assetBean) throws Exception {
            assetBean.setIsOnlyPoint( "Y" );
            
            int updateCount = 0;
            if( StringUtils.isNotBlank( assetBean.getAssetId() ) ){
                updateCount = assetInfoService.updateAssetInfo( assetBean );
                log.info( "更新隔离点： " + updateCount + " 条！" );
            }else{
                assetBean.setSite( getUserInfoScope().getSiteId() );
                String assetId = assetInfoService.insertAssetInfo( assetBean );
                log.info( "插入隔离点的Id ： " + assetId + " ！" );
                
                if( StringUtils.isNotBlank( assetId ) ){
                    updateCount = 1;
                }
            }
            
            paramsDataMap.put("isolationPointNo", assetBean.getAssetId() );
            assetBean = assetInfoService.queryAssetDetail( assetBean.getAssetId() );
            
            //更新datagrid
            insertPtwIsolationPoint( paramsDataMap );
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put( "assetBean", assetBean );
            if( updateCount > 0 ){
                map.put( "result", "success" );
            }else{
                map.put( "result", "fail" );
            }
            
            return map;
        }
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertPtwIsolationPoint( HashMap<String,String> paramsDataMap) throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String siteId = userInfoScope.getSiteId();
		String isolationPointNo = paramsDataMap.get("isolationPointNo"); //隔离点Id
		String isolationMethodDate = paramsDataMap.get("isolationMethodDate"); //隔离点与隔离方法的关联数据
	
		//TODO 先查询原先的隔离方法，然后比较现在的隔离方法，判断每条是插入还是删除
		 ArrayList<PtwIsolationPointVo>  oldDataList =ptwIsolationPointDao.queryBeanByIsolationPointNo(isolationPointNo,siteId);
		//页面里面的隔离方法
		JSONObject isolationMethodJsonObj = JSONObject.fromObject(isolationMethodDate);
		int isolationMethodNum =Integer.valueOf(isolationMethodJsonObj.get("total").toString());  //记录数
		JSONArray isolationMethodJsonArray = isolationMethodJsonObj.getJSONArray("rows"); //记录数组
		int result = 0;
		for(int i=0; i<isolationMethodNum; i++){
			String islMethodRecord = isolationMethodJsonArray.get(i).toString();  //某条记录的字符串表示
			PtwIsolationPointVo ptwIsolationPointVo = JsonHelper.toObject(islMethodRecord, PtwIsolationPointVo.class);
//			PtwIslMethodDefine ptwIslMethodDefine = JsonHelper.toObject(islMethodRecord, PtwIslMethodDefine.class);
			int id = ptwIsolationPointVo.getId();
			if(id == 0){  //前台新添加的隔离方法
				PtwIsolationPoint ptwIsolationPoint = new PtwIsolationPoint();
				ptwIsolationPoint.setPointNo(isolationPointNo); //隔离点编号
				ptwIsolationPoint.setMethodId(ptwIsolationPointVo.getMethodId()); //隔离方法ID
				ptwIsolationPoint.setSiteid(siteId);
				ptwIsolationPoint.setCreatedate(new Date());
				ptwIsolationPoint.setModifydate(new Date());
				ptwIsolationPoint.setCreateuser(userId);
				ptwIsolationPoint.setModifyuser(userId);
				ptwIsolationPoint.setYxbz(1);
				
				result = ptwIsolationPointDao.insertPtwIsolationPoint(ptwIsolationPoint);
			}else{
				for (int j = 0; j < oldDataList.size(); j++) {  //原来有的隔离方法
					PtwIsolationPointVo objIsolationPointVo = oldDataList.get(j);
					if(id == objIsolationPointVo.getId()){
						oldDataList.remove(j);
					}
				}
			}
		}
		
		//oldDataList中剩下的都是被前台删掉了的(此处将其从数据库中删掉)
		for (int i = 0; i < oldDataList.size(); i++) {
			ptwIsolationPointDao.deletePtwIsolationPointById(oldDataList.get(i).getId());
		}
		
		return result ;
	}

	@Override
	public Page<PtwIsolationPointVo> queryAllBeanBySiteId(Page<PtwIsolationPointVo> page) {
		List<PtwIsolationPointVo> ret = ptwIsolationPointDao.queryAllBeanVoBySiteId(page);
		page.setResults(ret);
		return page;
	}

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
         * @description:动态建立隔离点与隔离方法关联关系
         * @author: fengzt
         * @createDate: 2014年11月10日
         * @param methodIds
         * @param pointNo
         * @return:Map<String, Object> 
         */
        @Override
        @Transactional(propagation=Propagation.REQUIRED)
        public Map<String, Object> saveIslMethodByMethodIds(String methodIds, String pointNo) {
            Map<String, Object> map = new HashMap<String, Object>();
            if( StringUtils.isNotBlank( methodIds ) ){
                List<PtwIsolationPoint> ptList = new ArrayList<PtwIsolationPoint>();
                //构造bean
                ptList = constructPtwIsolationPoint( methodIds, pointNo );
                
                if( ptList.size() > 0 ){
                    boolean flag = false;
                    //检查是否已经存在关联关系
                    for( PtwIsolationPoint temp : ptList ){
                        PtwIsolationPoint tVo = ptwIsolationPointDao.queryIslMehtodByPoinIdAndMethodId( temp );
                        if( tVo != null ){
                            flag = true;
                            map.put( "reason", tVo.getPointNo() + "已存在所选数据！"  );
                            map.put("result", "fail" );
                            return map;
                        }
                    }
                    
                   //不存在关联关系时
                    List<IsMethodPointVo> isMethodPointVos = new ArrayList<IsMethodPointVo>();
                    if( !flag ){
                        for( PtwIsolationPoint tempVo : ptList ){
                            int count = ptwIsolationPointDao.insertPtwIsolationPoint( tempVo );
                            log.info( "插入隔离点与隔离方法关联 条数=" + count + " id = " + tempVo.getId() );
                            
                            List<IsMethodPointVo> isList = ptwIsolationPointDao.queryIsMethodByIsolationPoint( tempVo );
                            isMethodPointVos.addAll( isList );
                        }
                        
                        map.put( "result", "success" );
                        map.put( "rowData", isMethodPointVos );
                    }
                }
               
            }
            
            return map;
        }

        /**
         * 
         * @description:to PtwIsolationPoint bean
         * @author: fengzt
         * @createDate: 2014年11月10日
         * @param methodIdArr
         * @param pointNo
         * @return:List<PtwIsolationPoint>
         */
        private List<PtwIsolationPoint> constructPtwIsolationPoint(String methodIds, String pointNo) {
            String userId = getUserInfoScope().getUserId();
            String deptId = getUserInfoScope().getOrgId();
            String siteId = getUserInfoScope().getSiteId();
            
            String[] methodIdArr = methodIds.split( "," );
            
            List<PtwIsolationPoint> ptList = new ArrayList<PtwIsolationPoint>();
            
            //构造隔离点与隔离方法关联
            for( String methodId : methodIdArr ){
                int mId = Integer.parseInt( StringUtils.trim( methodId ) );
                PtwIsolationPoint vo = new PtwIsolationPoint();
                vo.setMethodId( mId );
                vo.setPointNo( pointNo );
                vo.setYxbz( 1 );
                
                vo.setCreatedate( new Date() );
                vo.setCreateuser( userId );
                vo.setDeptid( deptId );
                vo.setSiteid( siteId );
                
                ptList.add( vo );
            }
            return ptList;
        }

        /**
         * @description:删除隔离点
         * @author: fengzt
         * @createDate: 2015年1月6日
         * @param assetId
         * @return:int
         */
        @Override
        @Transactional(propagation=Propagation.REQUIRED)
        public int deleteIsolationPoint(String assetId) {
            int delAstCount = 0;
            try {
                int delPointCount = ptwIsolationPointDao.deleteIsolationPoint( assetId );
                delAstCount = assetInfoService.deleteAsset( assetId );
                log.info( "删掉的隔离点：" + delPointCount + " 条！ ---删除设备树点： " +  delAstCount );
            } catch (Exception e) {
            	log.error(e);
            }
            
            return delAstCount;
        }

}
