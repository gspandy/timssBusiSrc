package com.timss.asset.service.itc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.HwLedgerAddressBean;
import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.bean.HwLedgerCabinetBean;
import com.timss.asset.bean.HwLedgerDeviceBean;
import com.timss.asset.bean.HwLedgerEqptBean;
import com.timss.asset.bean.HwLedgerRoomBean;
import com.timss.asset.bean.HwLedgerServerBean;
import com.timss.asset.bean.HwLedgerVMBean;
import com.timss.asset.bean.SwLedgerAppBean;
import com.timss.asset.dao.HwLedgerDao;
import com.timss.asset.dao.HwLedgerEqptDao;
import com.timss.asset.service.HwLedgerService;
import com.timss.asset.service.SwLedgerService;
import com.timss.asset.vo.BackupVo;
import com.timss.asset.vo.BaseInfoVo;
import com.timss.asset.vo.DriverVo;
import com.timss.asset.vo.MultiSearchVo;
import com.timss.asset.vo.NetVo;
import com.timss.asset.vo.ServiceInfoVo;
import com.timss.asset.vo.ServiceVo;
import com.timss.asset.vo.SysVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 硬件台账
 * @description: {desc}
 * @company: gdyd
 * @className: HwLedgerServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年11月21日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("hwLedgerService")
public class HwLedgerServiceImpl implements HwLedgerService {
    
    Logger log = LoggerFactory.getLogger( HwLedgerServiceImpl.class );
   
    @Autowired
    private HwLedgerDao hwLedgerDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private SwLedgerService swLedgerService;
    
    @Autowired
    private HwLedgerEqptDao hwLedgerEqptDao;
    

    /**
     * 
     * @description:拿到登录用户信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private UserInfoScope getUserInfoScope(){
        UserInfoScope userInfoScope = null;
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        return userInfoScope;
    }
    
    /**
     * @description:插入硬件台账
     * @author: fengzt
     * @createDate: 2014年11月21日
     * @param hwLedgerBean
     * @return:
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertHwLedger(HwLedgerBean hwLedgerBean) {
        String siteId = getUserInfoScope().getSiteId();
        String deptId = getUserInfoScope().getOrgId();
        String userId = getUserInfoScope().getUserId();
        
        hwLedgerBean.setCreatedate( new Date() );
        hwLedgerBean.setSiteid( siteId );
        hwLedgerBean.setDeptid( deptId );
        hwLedgerBean.setCreateuser( userId );
        if( StringUtils.isBlank( hwLedgerBean.getIsRoot() ) ){
            hwLedgerBean.setIsRoot( "N" );
        }
        
        return hwLedgerDao.insertHwLedger( hwLedgerBean );
    }

    /**
     * 
     * @description:通过站点来查找硬件台账
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return:HwLedgerBean
     */
    @Override
    public HwLedgerBean queryHwLedgerBySite() {
        return null;
    }

    /**
     * 
     * @description:更新硬件台账
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateHwLedger(HwLedgerBean hwLedgerBean) {
        String userId = getUserInfoScope().getUserId();
        hwLedgerBean.setModifyuser( userId );
        hwLedgerBean.setModifydate( new Date() );
        
        int count = hwLedgerDao.updateHwLedger( hwLedgerBean );
        return count;
    }

    /**
     * 
     * @description:通过ID查找硬件台账
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:HwLedgerBean
     */
    @Override
    public HwLedgerBean queryHwLedgerDetail(String id) {

        return hwLedgerDao.queryHwLedgerDetail( id );
    }

    /**
     * 
     * @description:to HwLedgerBean 
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param baseInfoVo
     * @return:
     */
    private HwLedgerBean toHwLedgerBean(BaseInfoVo baseInfoVo) {
        HwLedgerBean bean = new HwLedgerBean();
        bean.setHwId( baseInfoVo.getHwId() );
        bean.setHwName( baseInfoVo.getHwName() );
        bean.setHwType( baseInfoVo.getHwType() );
        bean.setParentId( baseInfoVo.getParentId() );
        
        return bean;
    }

    
    /**
     * 
     * @description:插入或者更新 硬件台账
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param hwType
     * @param formData
     * @return:Map<String, Object>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdateHwLedger(String hwType, String formData) {
        log.info( "hwType ="  + hwType );
        Map<String, Object> map = new HashMap<String, Object>();
        
        int count = 0;
        BaseInfoVo vo = JsonHelper.fromJsonStringToBean( formData, BaseInfoVo.class );
        HwLedgerBean bean = toHwLedgerBean( vo );
        String hwId = bean.getHwId();
        
        count = insertOrUpdateBaseHw( bean );
      
        //插入其他类型
        map = insertHwTypeRoomAddressCabinet( hwType, hwId, bean );
        map.put( "hwLedgerBean", bean );
        vo.setHwId( bean.getHwId() );
        map.put( "baseInfoVo", vo );
        
        log.info( "更新或插入条数：" + count );
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }

    /**
     * 
     * @description:插入基础信息
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwType
     * @param hwId
     * @param bean
     * @return:Map<String, Object>
     */
    private Map<String, Object> insertHwTypeRoomAddressCabinet(String hwType, String hwId, HwLedgerBean bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        if("HW_L_ADDRESS".equalsIgnoreCase( hwType ) ){
            //hwId 不为空是删除再插入
            if( StringUtils.isNotBlank( hwId ) ){
                int addDelCount = hwLedgerDao.deleteAddress( hwId );
                log.info( "删除Address:" + addDelCount );
            }
            
            HwLedgerAddressBean addressBean = new HwLedgerAddressBean();
            addressBean.setHwId( bean.getHwId() );
            hwLedgerDao.insertAddress( addressBean );
            map.put( "addressBean", addressBean );
            
        }else if( "HW_L_ROOM".equalsIgnoreCase( hwType ) ){
            //hwId 不为空是删除再插入
            if( StringUtils.isNotBlank( hwId ) ){
                int addDelCount = hwLedgerDao.deleteRoom( hwId );
                log.info( "删除deleteRoom:" + addDelCount );
            }
            
            HwLedgerRoomBean roomBean = new HwLedgerRoomBean();
            roomBean.setHwId( bean.getHwId() );
            hwLedgerDao.insertRoom( roomBean );
            map.put( "roomBean", roomBean );
            
        }else if( "HW_L_CABINET".equalsIgnoreCase( hwType ) ){
            //hwId 不为空是删除再插入
            if( StringUtils.isNotBlank( hwId ) ){
                int addDelCount = hwLedgerDao.deleteCabinet( hwId );
                log.info( "删除deleteCabinet:" + addDelCount );
            }
            
            HwLedgerCabinetBean cabinetBean = new HwLedgerCabinetBean();
            cabinetBean.setHwId( bean.getHwId() );
            hwLedgerDao.insertCabinet( cabinetBean );
            map.put( "cabinetBean", cabinetBean );
        }
        return map;
    }

    /**
     * 
     * @description:更新或者插入基础硬件台账（AST_HW_L）
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param bean
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
     int insertOrUpdateBaseHw(HwLedgerBean bean) {
        int count = 0;
        if( StringUtils.isNotBlank( bean.getHwId() ) ){
            count = updateHwLedger( bean );
        }else{
            bean.setHwId( null );
            count = insertHwLedger( bean );
        }
        
        return count;
    }

    /**
     * 
     * @description:删除硬件台账
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param hwType
     * @param hwId
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> deleteHwLedger(String hwType, String hwId) {
        Map<String, Object> map = new HashMap<String, Object>();
        int count = 0;
        
        //因为是级联删除 所以不分类型
        count = hwLedgerDao.deleteHwLedger( hwId );
        if("HW_L_ADDRESS,HW_L_ROOM,HW_L_CABINET".contains( hwType ) ){
            
        }else if( "HW_L_SERVER".equalsIgnoreCase( hwType ) ){
            
        }else if( "HW_L_VM".equalsIgnoreCase( hwType ) ){
            
        }
        
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:插入或者更新服务器
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwLedgerBean
     * @param serverBean
     * @param deviceBean
     * @param eqptBean
     * @return: Map<String, Object>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdateServiceHw(HwLedgerBean hwLedgerBean, HwLedgerServerBean serverBean,
            HwLedgerDeviceBean deviceBean, HwLedgerEqptBean eqptBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        String hwId = hwLedgerBean.getHwId();
        int baseCount = insertOrUpdateBaseHw( hwLedgerBean );
        
        eqptBean.setHwId( hwLedgerBean.getHwId() );
        if( StringUtils.isNotBlank( hwId ) ){
            int updateCount = hwLedgerEqptDao.updateEqpt( eqptBean );
            log.info( "更新EQPT条数：" + updateCount );
        }else{
            int deviceCount = hwLedgerEqptDao.insertEqpt( eqptBean );
            log.info(  "插入EQPT ： " + deviceCount + "条！" );
        }
        
        deviceBean.setHwId( hwLedgerBean.getHwId() );
        if( StringUtils.isNotBlank( hwId ) ){
            int updateCount = hwLedgerDao.updateHwLedgerDevice( deviceBean );
            log.info( "更新DEVICE条数：" + updateCount );
        }else{
            int deviceCount = insertHwLedgerDevice( deviceBean );
            log.info(  "插入DEVICE： " + deviceCount + "条！" );
        }
        
        serverBean.setHwId( hwLedgerBean.getHwId() );
        if( StringUtils.isNotBlank( hwId ) ){
            int updateSerCount = hwLedgerDao.updateHwLedgerServer( serverBean );
            log.info( "更新服务器条数：" + updateSerCount );
        }else{
            int serverCount = insertHwLedgerServer( serverBean );
            log.info( "插入服务器：" + serverCount + " 条!" );
        }
        
        map = toServiceFormBean( hwLedgerBean, deviceBean, serverBean, eqptBean );
        map.put( "hwLedgerBean", hwLedgerBean );
        
        
        if( baseCount > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }

    /**
     * 
     * @description:插入或者更新虚机
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwLedgerBean
     * @param vmBean
     * @param deviceBean
     * @param eqptBean
     * @return:Map<String, Object>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdateVMHw(HwLedgerBean hwLedgerBean, HwLedgerVMBean vmBean,
            HwLedgerDeviceBean deviceBean, HwLedgerEqptBean eqptBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        String hwId = hwLedgerBean.getHwId();
        int baseCount = insertOrUpdateBaseHw( hwLedgerBean );
        
        eqptBean.setHwId( hwLedgerBean.getHwId() );
        if( StringUtils.isNotBlank( hwId ) ){
            int updateCount = hwLedgerEqptDao.updateEqpt( eqptBean );
            log.info( "更新EQPT条数：" + updateCount );
        }else{
            int deviceCount = hwLedgerEqptDao.insertEqpt( eqptBean );
            log.info(  "插入EQPT ： " + deviceCount + "条！" );
        }
        
        deviceBean.setHwId( hwLedgerBean.getHwId() );
        if( StringUtils.isNotBlank( hwId ) ){
            //int delDevCount = hwLedgerDao.delelteHwLedgerDevice( hwId );
            int updateCount = hwLedgerDao.updateHwLedgerDevice( deviceBean );
            log.info( "更新设备条数：" + updateCount );
        }else{
            int deviceCount = insertHwLedgerDevice( deviceBean );
            log.info( "插入设备条数：" + deviceCount );
        }
        
        vmBean.setHwId( hwLedgerBean.getHwId() );
        if( StringUtils.isNotBlank( hwId ) ){
           // int delSerCount = hwLedgerDao.delelteHwLedgerVM( hwId );
            int updateSerCount = hwLedgerDao.updateHwLedgerVM( vmBean );
            log.info( "更新虚机条数：" + updateSerCount );
        }else{
            int serverCount = insertHwLedgerVM( vmBean );
            log.info( "插入服务器：" + serverCount + " 条！" );
        }
        
        map = toFormVMBean( hwLedgerBean, deviceBean,eqptBean );
        map.put( "vmBean", vmBean );
        map.put( "hwLedgerBean", hwLedgerBean );
        
        if( baseCount > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:插入虚机
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param vmBean
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertHwLedgerVM(HwLedgerVMBean vmBean) {
        int count = hwLedgerDao.insertHwLedgerVM( vmBean );
        return count;
    }

    /**
     * 
     * @description:插入设备
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param deviceBean
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertHwLedgerDevice(HwLedgerDeviceBean deviceBean) {
        int count = hwLedgerDao.insertHwLedgerDevice( deviceBean );
        
        return count;
    }

    /**
     * 
     * @description:插入服务器
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param serverBean
     * @return:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertHwLedgerServer(HwLedgerServerBean serverBean) {

        int count = hwLedgerDao.insertHwLedgerServer( serverBean );
        return count;
    }

    /**
     * 
     * @description:查询硬件类型iHint
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param kw
     * @param modelType
     * @return: List<Map<String, Object>> 
     */
    @Override
    public List<Map<String, Object>> searchHwModelHint(String kw, String modelType) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        if( StringUtils.isNotBlank( kw ) ){
            kw = kw.toUpperCase();
        }
        params.put( "keyWord", kw );
        params.put( "modelType", modelType );
        
        List<Map<String, Object>> result = hwLedgerDao.searchHwModelHint( params );
        
        return result;
    }
    
    /**
     * 
     * @description:拿到所有站点下硬件类型
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @return:
     */
   /* private List<Map<String, Object>> queryHwModelNameBySiteId(){
        String siteId = getUserInfoScope().getSiteId();
        List<Map<String, Object>> modelResult = new ArrayList<Map<String,Object>>();
        modelResult = hwLedgerDao.queryHwModelNameBySiteId( siteId );
        return modelResult ;
    }*/
    
    /**
     * 
     * @description:通过Id拿到硬件类型名字
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @return:String
     */
    private String queryHwModelNameById( String id ){
        if(  StringUtils.isBlank( id ) ){
            return "";
        }
        List<Map<String, Object>> modelResult = new ArrayList<Map<String,Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "siteId", getUserInfoScope().getSiteId() );
        params.put( "id", id );
        modelResult = hwLedgerDao.queryHwModelNameById( params );
        for( Map<String, Object> map : modelResult ){
            String key = (String)map.get( "id" );
            if( StringUtils.equals( key, id ) ){
                return (String)map.get( "name" );
            }
        }
        return "";
    }
    
    

    /**
     * 
     * @description:通过HwId查询服务器相关
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwId
     * @return:Map
     */
    @Override
    public Map<String, Object> queryHwLedgerServiceById(String hwId) {
        HwLedgerBean hwLedgerBean =  queryHwLedgerDetail( hwId );
        
        HwLedgerEqptBean eqptBean = hwLedgerEqptDao.queryHwLedgerEqptById( hwId );
        HwLedgerDeviceBean deviceBean = hwLedgerDao.queryHwLedgerDeviceById( hwId );
        HwLedgerServerBean serverBean = hwLedgerDao.queryHwLedgerServerById( hwId );
        
        //构造页面VO
        Map<String, Object> map = toServiceFormBean( hwLedgerBean, deviceBean, serverBean ,eqptBean);
        
        if(map.size() > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }

    /**
     * 
     * @description:构造页面VO
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwLedgerBean
     * @param deviceBean
     * @param serverBean
     * @param eqptBean 
     * @return:
     */
    private Map<String, Object> toServiceFormBean(HwLedgerBean hwLedgerBean, HwLedgerDeviceBean deviceBean,
            HwLedgerServerBean serverBean, HwLedgerEqptBean eqptBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        //转化成页面VO
        BaseInfoVo baseInfoVo = toBaseInfoVo( hwLedgerBean, eqptBean);
        ServiceVo serviceVo = toServiceVo( hwLedgerBean, serverBean );
        ServiceInfoVo serviceInfoVo = toServiceInfoVo( eqptBean );
        
        SysVo sysVo = toSysVo( deviceBean );
        NetVo netVo = toNetVO( deviceBean );
        BackupVo backupVo = toBackupVo( deviceBean );
        
        DriverVo driverVo = toDriverVo( deviceBean );
        
        map.put( "baseInfoVo", baseInfoVo );
        map.put( "serviceVo", serviceVo );
        map.put( "serviceInfoVo", serviceInfoVo );
        
        map.put( "sysVo", sysVo );
        map.put( "netVo", netVo );
        map.put( "backupVo", backupVo );
        map.put( "driverVo", driverVo );
        
        return map;
    }

    /**
     * 
     * @description:转化成DriverVo
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param deviceBean
     * @return:DriverVo
     */
    private DriverVo toDriverVo(HwLedgerDeviceBean deviceBean) {
        DriverVo vo = new DriverVo();
        
        vo.setStorageModel( deviceBean.getStorageModel() );
        String storageModelName =  queryHwModelNameById( deviceBean.getStorageModel() );
        vo.setStorageModelName( storageModelName );
        
        vo.setStorageType( deviceBean.getStorageType() );
        String storageTypeName = queryHwModelNameById( deviceBean.getStorageType() );
        vo.setStorageTypeName( storageTypeName );
        
        vo.setSanLun( deviceBean.getSanLun() );
        vo.setRaidType( deviceBean.getRaidType() );
        String raidTypeName = queryHwModelNameById( deviceBean.getRaidType() );
        vo.setRaidTypeName( raidTypeName );
        
        vo.setLunNum( deviceBean.getLunNum() );
        vo.setDataChangeDegree( deviceBean.getDataChangeDegree() );
        return vo;
    }

    /**
     * 
     * @description:toBackupVo
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param deviceBean
     * @return:BackupVo
     */
    private BackupVo toBackupVo(HwLedgerDeviceBean deviceBean) {
        BackupVo vo = new BackupVo();
        
        vo.setIsRemoteBackup( deviceBean.getIsRemoteBackup() );
        vo.setIsVtlBackup( deviceBean.getIsVtlBackup() );
        vo.setIsCdpBackup( deviceBean.getIsCdpBackup() );
        
        vo.setIsPtlBackup( deviceBean.getIsPtlBackup() );
        vo.setIsManualBackup( deviceBean.getIsManualBackup() );
        return vo;
    }

    /**
     * 
     * @description:toNetVO
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param deviceBean
     * @return:
     */
    private NetVo toNetVO(HwLedgerDeviceBean deviceBean) {
        NetVo vo = new NetVo();
        
        vo.setIp( deviceBean.getIp() );
        vo.setIsClusterIp( deviceBean.getIsClusterIp() );
        vo.setMac( deviceBean.getMac() );
        
        vo.setVlan( deviceBean.getVlan() );
        vo.setNetArea( deviceBean.getNetArea() );
        vo.setIsPublicNet( deviceBean.getIsPublicNet() );
        return vo;
    }

    /**
     * 
     * @description:转化成SysVo
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param deviceBean
     * @return:SysVo
     */
    private SysVo toSysVo(HwLedgerDeviceBean deviceBean) {
        SysVo vo = new SysVo();
        
        vo.setComputerName( deviceBean.getComputerName() );
        vo.setOs( deviceBean.getOs() );
        vo.setOsPath( deviceBean.getOsPath() );
        
        vo.setLogicCpu( deviceBean.getLogicCpu() );
        vo.setLogicMem( deviceBean.getLogicMem() );
        vo.setLogicHarddisk( deviceBean.getLogicHarddisk() );
            
        return vo;
    }

    /**
     * 
     * @description:toServiceInfoVo
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwLedgerBean
     * @param serverBean
     * @param eqptBean 
     * @return:
     */
    private ServiceInfoVo toServiceInfoVo( HwLedgerEqptBean eqptBean ) {
        ServiceInfoVo bean = new ServiceInfoVo();
        bean.setAssetCode( eqptBean.getAssetCode() );
        bean.setOwnOrg( eqptBean.getOwnOrg() );
        bean.setStatus( eqptBean.getStatus() );
        
        bean.setToUseTime( eqptBean.getToUseTime() );
        bean.setElapsedTime( eqptBean.getElapsedTime() );
        bean.setPrincipal( eqptBean.getPrincipal() );
        bean.setPrincipalName( eqptBean.getEqptAttr01() );
        
        
        bean.setSupplier( eqptBean.getSupplier() );
        bean.setRepairRecard( eqptBean.getRepairRecard() );
        
        return bean;
    }

    /**
     * 
     * @description:toServiceVo
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwLedgerBean
     * @param serverBean
     * @return:ServiceVo
     */
    private ServiceVo toServiceVo(HwLedgerBean hwLedgerBean, HwLedgerServerBean serverBean) {
        ServiceVo vo = new ServiceVo();
        
        vo.setServerModel( serverBean.getServerModel() );
        //硬件名称
        String modelName = queryHwModelNameById( serverBean.getServerModel() );
        vo.setServerModelName( modelName );
        vo.setSnCode( serverBean.getSnCode() );
        
        vo.setServerBrand( serverBean.getServerBrand() );
        String serverBrandName = queryHwModelNameById( serverBean.getServerBrand() );
        vo.setServerBrandName( serverBrandName );
        
        vo.setCpuModel( serverBean.getCpuModel() );
        String cpuModelName = queryHwModelNameById( serverBean.getCpuModel() );
        vo.setCpuModelName( cpuModelName );
        vo.setCpuNum( serverBean.getCpuNum() );
        
        vo.setMemModel( serverBean.getMemModel() );
        String memModelName =  queryHwModelNameById( serverBean.getMemModel() );
        vo.setMemModelName( memModelName );
        
        vo.setMemNum( serverBean.getMemNum() );
        vo.setHarddiskModel( serverBean.getHarddiskModel() );
        String harddiskModelName = queryHwModelNameById( serverBean.getHarddiskModel() );
        vo.setHarddiskModelName( harddiskModelName );
        
        vo.setHarddiskNum( serverBean.getHarddiskNum() );
        vo.setHbaModel( serverBean.getHbaModel() );
        String hbaModelName = queryHwModelNameById( serverBean.getHbaModel() );
        vo.setHbaModelName( hbaModelName );
        
        vo.setHbaNum( serverBean.getHbaNum() );
        
        vo.setNetcardModel( serverBean.getNetcardModel() );
        String netcardModelName = queryHwModelNameById( serverBean.getNetcardModel() );
        vo.setNetcardModelName( netcardModelName );
        
        vo.setNetcardNum( serverBean.getNetcardNum() );
        vo.setRaidModel( serverBean.getRaidModel() );
        String raidModelName =  queryHwModelNameById( serverBean.getRaidModel() );
        vo.setRaidModelName( raidModelName );
        
        vo.setPower( serverBean.getPower() );
        
        return vo;
    }

    /**
     * 
     * @description:toBaseInfoVo
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwLedgerBean
     * @param serverBean
     * @param deviceBean
     * @param eqptBean 
     * @return:
     */
    private BaseInfoVo toBaseInfoVo(HwLedgerBean hwLedgerBean, HwLedgerEqptBean eqptBean) {
        BaseInfoVo vo = new BaseInfoVo();
        vo.setRelatedBusiness( eqptBean.getRelatedBusiness() );
        
        vo.setRemarks( eqptBean.getRemarks() );
        vo.setLocation( eqptBean.getLocation() );
        vo.setHwId( hwLedgerBean.getHwId() );
        
        vo.setHwName( hwLedgerBean.getHwName() );
        vo.setHwType( hwLedgerBean.getHwType() );
        vo.setParentId( hwLedgerBean.getParentId() );
        
        //父节点名称
        HwLedgerBean temp = queryHwLedgerDetail( hwLedgerBean.getParentId() );
        String parentName = temp.getHwName();
        vo.setParentName( parentName );
        
        return vo;
    }

    /**
     * 
     * @description:通过ID查询虚机相关
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwId
     * @return:Map<String, Object> 
     */
    @Override
    public Map<String, Object> queryHwLedgerVMById(String hwId) {
        HwLedgerBean hwLedgerBean =  queryHwLedgerDetail( hwId );
        
        HwLedgerEqptBean eqptBean = hwLedgerEqptDao.queryHwLedgerEqptById( hwId );
        HwLedgerDeviceBean deviceBean = hwLedgerDao.queryHwLedgerDeviceById( hwId );
        HwLedgerVMBean vmBean = hwLedgerDao.queryHwLedgerVMById( hwId );
        
        //构造页面VO
        Map<String, Object> map = toFormVMBean( hwLedgerBean, deviceBean , eqptBean);
        map.put( "vmBean", vmBean );
        
        if(map.size() > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }

    /**
     * 
     * @description:构造虚机VO 
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwLedgerBean
     * @param deviceBean
     * @param eqptBean 
     * @return:
     */
    private Map<String, Object> toFormVMBean(HwLedgerBean hwLedgerBean, HwLedgerDeviceBean deviceBean,
            HwLedgerEqptBean eqptBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        //转化成页面VO
        BaseInfoVo baseInfoVo = toBaseInfoVo( hwLedgerBean, eqptBean );
        SysVo sysVo = toSysVo( deviceBean );
        NetVo netVo = toNetVO( deviceBean );
        BackupVo backupVo = toBackupVo( deviceBean );
        
        DriverVo driverVo = toDriverVo( deviceBean );
        ServiceInfoVo serviceInfoVo = toServiceInfoVo( eqptBean );
        
        map.put( "baseInfoVo", baseInfoVo );
        map.put( "serviceInfoVo", serviceInfoVo );
        
        map.put( "sysVo", sysVo );
        map.put( "netVo", netVo );
        map.put( "backupVo", backupVo );
        map.put( "driverVo", driverVo );
        
        return map;
    }

    /**
     * 
     * @description:检查硬件台账名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 2014年11月28日
     * @param hwName
     * @param hwId 
     * @return:List<HwLedgerBean>
     */
    @Override
    public List<HwLedgerBean> queryCheckHwLedgerName(String hwName, String hwId ) {
        Map<String, Object> params = new HashMap<String, Object>();
        String  siteId = getUserInfoScope().getSiteId();
        
        params.put( "siteId", siteId );
        params.put( "hwName", hwName );
        params.put( "hwId", hwId );
        
        List<HwLedgerBean> hwLedgerBeans = hwLedgerDao.queryCheckHwLedgerName( params );
        
        return hwLedgerBeans;
    }

    /**
     * 
     * @description:查找机房、机柜、物理地点
     * @author: fengzt
     * @createDate: 2014年11月29日
     * @param id
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryHwLedgerByHwTypeAndId(String id) {
        HwLedgerBean hwLedgerBean = queryHwLedgerDetail( id );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "hwLedgerBean", hwLedgerBean );
        
        if("HW_L_ADDRESS".equalsIgnoreCase( hwLedgerBean.getHwType() ) ){
            HwLedgerAddressBean addressBean = hwLedgerDao.queryHwAddressByHwId( hwLedgerBean.getHwId() );
            map.put( "addressBean", addressBean );
            
        }else if( "HW_L_ROOM".equalsIgnoreCase( hwLedgerBean.getHwType() ) ){
            HwLedgerRoomBean roomBean = hwLedgerDao.queryHwRoomByHwId( hwLedgerBean.getHwId() );
            map.put( "roomBean", roomBean );
            
        }else if( "HW_L_CABINET".equalsIgnoreCase( hwLedgerBean.getHwType() ) ){
            HwLedgerCabinetBean cabinetBean = hwLedgerDao.queryHwCabinetByHwId( hwLedgerBean.getHwId() );
            map.put( "cabinetBean", cabinetBean );
        }
        
        return map;
    }

    /**
     * 
     * @description:通过hwId查找HwLedgerDevice
     * @author: fengzt
     * @createDate: 2014年12月1日
     * @param hwId
     * @return:HwLedgerDeviceBean
     */
    @Override
    public HwLedgerDeviceBean queryHwLedgerDeviceById(String hwId) {
        return hwLedgerDao.queryHwLedgerDeviceById( hwId );
    }

    /**
     * 
     * @description:树上DIV,当鼠标放在书上服务器或者虚机
     * @author: fengzt
     * @createDate: 2014年12月2日
     * @param hwId
     * @param hwType
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryHwLedgerByTypeAndId(String hwId, String hwType) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( StringUtils.equalsIgnoreCase( "HW_L_SERVER", hwType ) ){
            map = queryHwLedgerServiceById( hwId );
        }else if( StringUtils.equalsIgnoreCase( "HW_L_VM", hwType ) ){
            map = queryHwLedgerVMById( hwId );
        }
        
        ServiceInfoVo serviceInfoVo = (ServiceInfoVo)map.get( "serviceInfoVo" );
        if(StringUtils.isNotBlank( serviceInfoVo.getStatus() ) ){
            String status = serviceInfoVo.getStatus();
            String statusName = getCategoryName( status, "AST_HW_SERVICE_STATUS" );
            map.put( "status", statusName );
        }
        
        //根据硬件台账id查找部署在上面的所有的软件台账的应用
        try {
            List<SwLedgerAppBean> swLedgerAppBeans =  swLedgerService.querySwLedgerAppByHwId( hwId );
            for( SwLedgerAppBean vo : swLedgerAppBeans ){
                String appType = getCategoryName( vo.getAppType(), "AST_SW_APP_TYPE" );
                vo.setAppType( appType );
            }
            map.put( "swLedgerAppBeans", swLedgerAppBeans );
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        
        return map;
    }

    /**
     * 
     * @description:枚举code to name
     * @author: fengzt
     * @createDate: 2014年10月11日
     * @param code
     * @param parentCode
     * @return:String
     */
    private String getCategoryName(String code, String parentCode ) {
        String categoryName = code;
        if( StringUtils.isNotBlank( categoryName ) ){
            List<AppEnum> emList = itcMvcService.getEnum( parentCode );
            if( emList != null && emList.size() > 0 ){
                for( AppEnum appVo : emList ){
                    if( categoryName.equalsIgnoreCase( appVo.getCode() ) ){
                        categoryName =  appVo.getLabel();
                        break;
                    }
                }
            }
        }
        return categoryName;
    }

    /**
     * 
     * @description:硬件台账多条件查询
     * @author: fengzt
     * @createDate: 2015年1月4日
     * @param vo
     * @param pageVo
     * @return:List<MultiSearchVo>
     */
    @Override
    public List<MultiSearchVo> queryHwLedgerMulti(MultiSearchVo vo, Page<HashMap<?, ?>> pageVo) {
        pageVo = setParams( vo, pageVo );
        String siteId = getUserInfoScope().getSiteId();
        pageVo.setParameter( "siteId", siteId );
        
        List<MultiSearchVo> result = hwLedgerDao.queryHwLedgerMulti( pageVo );
        return result;
    }

    /**
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2015年1月4日
     * @param vo
     * @param pageVo
     * @return:
     */
    private Page<HashMap<?, ?>> setParams(MultiSearchVo vo, Page<HashMap<?, ?>> pageVo) {
        if( StringUtils.isNotBlank( vo.getHwId() )){
            pageVo.setParameter( "hwId", vo.getHwId() );
        }
        if( StringUtils.isNotBlank( vo.getHwName() )){
            pageVo.setParameter( "hwName", vo.getHwName() );
        }
        if( StringUtils.isNotBlank( vo.getHwType() )){
            pageVo.setParameter( "hwType", vo.getHwType() );
        }
        if( StringUtils.isNotBlank( vo.get_hwType() )){
            pageVo.setParameter( "hwType", vo.get_hwType() );
        }
        if( StringUtils.isNotBlank( vo.getIp() )){
            pageVo.setParameter( "ip", vo.getIp() );
        }
        if( StringUtils.isNotBlank( vo.getModel() )){
            pageVo.setParameter( "model", vo.getModel() );
        }
        if( StringUtils.isNotBlank( vo.getToUseTime() )){
            pageVo.setParameter( "toUseTime", vo.getToUseTime() );
        }
        
        return pageVo;
    }

    /**
     * 
     * @description:硬件台账多条件查询--结果页查询
     * @author: fengzt
     * @createDate: 2015年1月4日
     * @param vo
     * @param searchVo
     * @param pageVo
     * @return:List<MultiSearchVo>
     */
    @Override
    public List<MultiSearchVo> queryHwLedgerMultiBySearch(MultiSearchVo vo, MultiSearchVo searchVo,
            Page<HashMap<?, ?>> pageVo) {
        pageVo = setParams( vo, pageVo );
        String siteId = getUserInfoScope().getSiteId();
        pageVo.setParameter( "siteId", siteId );
        
        pageVo = setParams( searchVo, pageVo );
        
        List<MultiSearchVo> result = hwLedgerDao.queryHwLedgerMulti( pageVo );
        return result;
    }

}
