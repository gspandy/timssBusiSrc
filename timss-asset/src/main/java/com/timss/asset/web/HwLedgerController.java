package com.timss.asset.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.bean.HwLedgerDeviceBean;
import com.timss.asset.bean.HwLedgerEqptBean;
import com.timss.asset.bean.HwLedgerNetworkBean;
import com.timss.asset.bean.HwLedgerRoomEqptBean;
import com.timss.asset.bean.HwLedgerServerBean;
import com.timss.asset.bean.HwLedgerStorageBean;
import com.timss.asset.bean.HwLedgerVMBean;
import com.timss.asset.bean.SwLedgerAppBean;
import com.timss.asset.service.HwLedgerEqptService;
import com.timss.asset.service.HwLedgerService;
import com.timss.asset.service.HwLedgerTreeService;
import com.timss.asset.service.SwLedgerService;
import com.timss.asset.vo.BackupVo;
import com.timss.asset.vo.BaseInfoVo;
import com.timss.asset.vo.DriverVo;
import com.timss.asset.vo.MultiSearchVo;
import com.timss.asset.vo.NetVo;
import com.timss.asset.vo.ServiceInfoVo;
import com.timss.asset.vo.ServiceVo;
import com.timss.asset.vo.SysVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;

/**
 * @title: 硬件资产台账
 * @description: {desc}
 * @company: gdyd
 * @className: HwLedgerController.java
 * @author: fengzt
 * @createDate: 2014年11月24日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "asset/hwLedger")
public class HwLedgerController {
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private HwLedgerService hwLedgerService;
    
    @Autowired
    private SwLedgerService swLedgerService;
    
    @Autowired
    private HwLedgerTreeService hwLedgerTreeService;
    
    @Autowired
    private HwLedgerEqptService hwLedgerEqptService;

    static Logger logger = Logger.getLogger( HwLedgerController.class );

    /**
     * @description:查询硬件台账树
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/queryHwLedgerTree")
    public ModelAndViewAjax queryHwLedgerTree(String id) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        // 如果为空，则获得根节点的id
        if ( StringUtils.isBlank( id ) ) {
            id = hwLedgerTreeService.queryHwLedgerRootIdBySite();
            HwLedgerBean bean = hwLedgerService.queryHwLedgerDetail( id );
            result.put( "parent", bean );
        }
        List<HwLedgerBean> hwLedgerBeans = hwLedgerTreeService.queryHwLedgerChildren( id );
        
        //判断是否有孩子节点
        for( HwLedgerBean vo : hwLedgerBeans ){
            List<HwLedgerBean> listChilds = hwLedgerTreeService.queryHwLedgerChildren( vo.getHwId() );
            if( listChilds != null && listChilds.size() > 0 ){
                vo.setWithChild( true );
            }
        }
        
        result.put( "children", hwLedgerBeans );
        return itcMvcService.jsons( result );
    }
    
    
    /**
     * @description:查询硬件台账byId
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:Map
     */
    @RequestMapping(value = "/queryHwLedgerById")
    public @ResponseBody Map<String,Object> queryHwLedgerById(String id) {
        
        Map<String, Object> map = hwLedgerService.queryHwLedgerByHwTypeAndId( id );
        
        HwLedgerBean hwLedgerBean = (HwLedgerBean)map.get( "hwLedgerBean" );
        BaseInfoVo baseInfoVo = new BaseInfoVo();
        baseInfoVo.setHwId( hwLedgerBean.getHwId() );
        
        baseInfoVo.setHwName( hwLedgerBean.getHwName() );
        baseInfoVo.setHwType( hwLedgerBean.getHwType() );
        baseInfoVo.setParentId( hwLedgerBean.getParentId() );
        
        //父节点名称
        HwLedgerBean temp = hwLedgerService.queryHwLedgerDetail( hwLedgerBean.getParentId() );
        String parentName = temp.getHwName();
        baseInfoVo.setParentName( parentName );
        
        if( StringUtils.isNotBlank( hwLedgerBean.getHwId() ) ){
            map.put( "baseInfoVo", baseInfoVo );
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }

    /**
     * 
     * @description:IP或者名称查询--硬件台账树
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param kw
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/searchHwLedgerHint")
    public ModelAndViewAjax searchHwLedgerHint(String kw) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if ( StringUtils.isNotBlank( kw ) ) {
            // 可性能优化，前台只展示前十条，可考虑只查询10条
            result = hwLedgerTreeService.searchHwLedgerHint( kw );
        }
        return itcMvcService.jsons( result );
    }
    
    /**
     * 
     * @description:服务器信息IHint事件
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param kw
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/searchHwModelHint")
    public ModelAndViewAjax searchHwModelHint(String kw, String modelType ) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 可性能优化，前台只展示前十条，可考虑只查询10条
        result = hwLedgerService.searchHwModelHint( kw, modelType );
        return itcMvcService.jsons( result );
    }
    
    /**
     * 
     * @description:用于搜索框的点击事件
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:
     */
    @RequestMapping(value = "/searchHintHwLedgerParentIds")
    public ModelAndViewAjax searchHintHwLedgerParentIds( String id ) {
        List<String> result=new ArrayList<String>();
        if( StringUtils.isNotBlank(  id ) ){
                result = hwLedgerTreeService.searchHintHwLedgerParentIds( id );
        }
        return itcMvcService.jsons(result);       
    }
    
    /**
     * 
     * @description:查询除根节点之外各个类型的个数信息
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/queryHwLedgerByRoot")
    public Map<String, Object> queryHwLedgerByRoot(  ) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<Map<String, Object>> result = hwLedgerTreeService.queryHwLedgerByRoot( );
            if( result != null && result.size() > 0 ){
                map.put( "result", result );
            }
        return map;    
    }
    
    /**
     * 
     * @description:跳转到新建页面
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/insertHwLedgerPage")
    @ReturnEnumsBind("AST_HW_TYPE,AST_HW_SERVICE_STATUS,AST_HW_YES_NO,AST_HW_VMDEPLOC,AST_HW_NETAREA")
    public ModelAndView insertHwLedgerPage( String hwType, String parentId  ) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "hwType", hwType );
        map.put( "parentId", parentId );
        
        //父节点名称
        HwLedgerBean hwLedgerBean = hwLedgerService.queryHwLedgerDetail( parentId );
        String parentName = hwLedgerBean.getHwName();
        map.put( "parentName", parentName );
        
        return new ModelAndView( "hardware/HwLedger-insertHwLedger.jsp", map );    
    }
    
    /**
     * 
     * @description:更新或者插入硬件台账
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/insertOrUpdateHwLedger", method =RequestMethod.POST )
    public @ResponseBody Map<String, Object> insertOrUpdateHwLedger( String hwType, String formData  ) {
        Map<String, Object> map = hwLedgerService.insertOrUpdateHwLedger( hwType, formData );
        
        return map ;    
    }
    
    /**
     * 
     * @description:更新或者插入服务器
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/insertOrUpdateServiceHw", method =RequestMethod.POST)
    public @ResponseBody Map<String, Object> insertOrUpdateServiceHw( String baseInfoFormData,
            String deviceFormData, String serviceFormData, String sysFormData, 
            String netFormData, String backupFormData, String driverFormData ) {
        
        //json to bean 
        BaseInfoVo baseInfoVo = JsonHelper.fromJsonStringToBean( baseInfoFormData, BaseInfoVo.class );
        ServiceVo serviceVo = JsonHelper.fromJsonStringToBean( deviceFormData, ServiceVo.class );
        ServiceInfoVo serviceInfoVo = JsonHelper.fromJsonStringToBean( serviceFormData, ServiceInfoVo.class );
        
        SysVo sysVo = JsonHelper.fromJsonStringToBean( sysFormData, SysVo.class );
        NetVo netVo = JsonHelper.fromJsonStringToBean( netFormData, NetVo.class );
        BackupVo backupVo = JsonHelper.fromJsonStringToBean( backupFormData, BackupVo.class );
        DriverVo driverVo = JsonHelper.fromJsonStringToBean( driverFormData, DriverVo.class );
        
        //构造相应的bean
        HwLedgerBean hwLedgerBean = setHwLedgerBean( baseInfoVo );
        HwLedgerServerBean serverBean = setHwLedgerServerBean(baseInfoVo, serviceVo,serviceInfoVo );
        HwLedgerDeviceBean deviceBean = setHwLedgerDeviceBean( baseInfoVo, sysVo, netVo, backupVo, driverVo );
        HwLedgerEqptBean eqptBean = setHwLedgerEqptBean( baseInfoVo, serviceVo, serviceInfoVo );
        
        Map<String, Object> map = hwLedgerService.insertOrUpdateServiceHw( hwLedgerBean, serverBean, deviceBean, eqptBean );
        
        return map ;    
    }
    
    /**
     * 
     * @description:转换成设备bean
     * @author: fengzt
     * @createDate: 2014年12月22日
     * @param baseInfoVo
     * @param serviceVo
     * @param serviceInfoVo
     * @return:
     */
    private HwLedgerEqptBean setHwLedgerEqptBean(BaseInfoVo baseInfoVo, ServiceVo serviceVo, ServiceInfoVo serviceInfoVo) {
        HwLedgerEqptBean bean = new HwLedgerEqptBean();
        bean.setLocation( baseInfoVo.getLocation() );
        bean.setRelatedBusiness( baseInfoVo.getRelatedBusiness() );
        
        bean.setRemarks( baseInfoVo.getRemarks()  );
        bean.setOwnOrg( serviceInfoVo.getOwnOrg() );
        bean.setStatus( serviceInfoVo.getStatus() );
        
        bean.setAssetCode( serviceInfoVo.getAssetCode() );
        bean.setToUseTime( serviceInfoVo.getToUseTime() );
        bean.setElapsedTime( serviceInfoVo.getElapsedTime() );
        
        bean.setRepairRecard( serviceInfoVo.getRepairRecard() );
        bean.setSupplier( serviceInfoVo.getSupplier() );
        bean.setPrincipal( serviceInfoVo.getPrincipal() );
        bean.setEqptAttr01( serviceInfoVo.getPrincipalName() );
        return bean;
    }


    /**
     * 
     * @description:更新或者插入虚机
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/insertOrUpdateVMHw", method =RequestMethod.POST)
    public @ResponseBody Map<String, Object> insertOrUpdateVMHw( String baseInfoFormData,
            String vmFormData, String sysFormData, String netFormData,
            String backupFormData, String driverFormData, String serviceFormData ) {
        
        //json to bean 
        BaseInfoVo baseInfoVo = JsonHelper.fromJsonStringToBean( baseInfoFormData, BaseInfoVo.class );
        HwLedgerVMBean vmBean = JsonHelper.fromJsonStringToBean( vmFormData, HwLedgerVMBean.class );
        ServiceInfoVo serviceInfoVo = JsonHelper.fromJsonStringToBean( serviceFormData, ServiceInfoVo.class );

        SysVo sysVo = JsonHelper.fromJsonStringToBean( sysFormData, SysVo.class );
        NetVo netVo = JsonHelper.fromJsonStringToBean( netFormData, NetVo.class );
        BackupVo backupVo = JsonHelper.fromJsonStringToBean( backupFormData, BackupVo.class );
        DriverVo driverVo = JsonHelper.fromJsonStringToBean( driverFormData, DriverVo.class );
        
        //构造相应的bean
        HwLedgerBean hwLedgerBean = setHwLedgerBean( baseInfoVo );
        HwLedgerDeviceBean deviceBean = setHwLedgerDeviceBean( baseInfoVo, sysVo, netVo, backupVo, driverVo );
        ServiceVo serviceVo = new ServiceVo();
        HwLedgerEqptBean eqptBean = setHwLedgerEqptBean( baseInfoVo, serviceVo , serviceInfoVo  );
        
        Map<String, Object> map = hwLedgerService.insertOrUpdateVMHw( hwLedgerBean, vmBean, deviceBean, eqptBean );
        
        return map ;    
    }
    
    /**
     * 
     * @description:转换成设备bean
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param baseInfoVo
     * @param sysVo
     * @param netVo
     * @param backupVo
     * @param driverVo
     * @return:
     */
    private HwLedgerDeviceBean setHwLedgerDeviceBean(BaseInfoVo baseInfoVo, SysVo sysVo, NetVo netVo,
            BackupVo backupVo, DriverVo driverVo) {
        
        HwLedgerDeviceBean vo = new HwLedgerDeviceBean();
        
        vo.setRemarks( baseInfoVo.getRemarks() );
        
        vo.setComputerName( sysVo.getComputerName() );
        vo.setOs( sysVo.getOs() );
        vo.setOsPath( sysVo.getOsPath() );
        
        vo.setLogicCpu( sysVo.getLogicCpu() );
        vo.setLogicMem( sysVo.getLogicMem() );
        vo.setLogicHarddisk( sysVo.getLogicHarddisk() );
        
        vo.setIp( netVo.getIp() );
        vo.setIsClusterIp( netVo.getIsClusterIp() );
        vo.setMac( netVo.getMac() );
        
        vo.setVlan( netVo.getVlan() );
        vo.setNetArea( netVo.getNetArea() );
        vo.setIsPublicNet( netVo.getIsPublicNet() );
        
        vo.setIsRemoteBackup( backupVo.getIsRemoteBackup() );
        vo.setIsVtlBackup( backupVo.getIsVtlBackup() );
        vo.setIsCdpBackup( backupVo.getIsCdpBackup() );
        
        vo.setIsPtlBackup( backupVo.getIsPtlBackup() );
        vo.setIsManualBackup( backupVo.getIsManualBackup() );
        vo.setStorageModel( driverVo.getStorageModel() );
        
        vo.setStorageType( driverVo.getStorageType() );
        vo.setSanLun( driverVo.getSanLun() );
        vo.setRaidType( driverVo.getRaidType() );
        
        vo.setLunNum( driverVo.getLunNum() );
        vo.setDataChangeDegree( driverVo.getDataChangeDegree() );
        
        return vo;
    }


    /**
     * 
     * @description:转换成服务器bean
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param baseInfoVo
     * @param serviceVo
     * @param serviceInfoVo
     * @return:
     */
    private HwLedgerServerBean setHwLedgerServerBean(BaseInfoVo baseInfoVo, ServiceVo serviceVo,
            ServiceInfoVo serviceInfoVo) {
        HwLedgerServerBean bean = new HwLedgerServerBean();
        
        bean.setLocation( baseInfoVo.getLocation() );
        bean.setServerModel( serviceVo.getServerModel() );
        
        bean.setSnCode( serviceVo.getSnCode() );
        bean.setServerBrand( serviceVo.getServerBrand() );
        bean.setCpuModel( serviceVo.getCpuModel() );
        
        bean.setCpuNum( serviceVo.getCpuNum() );
        bean.setMemModel( serviceVo.getMemModel() );
        bean.setMemNum( serviceVo.getMemNum() );
        
        bean.setHarddiskModel( serviceVo.getHarddiskModel() );
        bean.setHarddiskNum( serviceVo.getHarddiskNum() );
        bean.setHbaModel( serviceVo.getHbaModel() );
        
        bean.setHbaNum( serviceVo.getHbaNum() );
        bean.setNetcardModel( serviceVo.getNetcardModel() );
        bean.setNetcardNum( serviceVo.getNetcardNum() );
        
        bean.setRaidModel( serviceVo.getRaidModel() );
        bean.setPower( serviceVo.getPower() );
        bean.setToUseTime( serviceInfoVo.getToUseTime() );
        
        bean.setElapsedTime( serviceInfoVo.getElapsedTime() );
       // bean.setMaintain( serviceInfoVo.getMaintain() );
        bean.setSupplier( serviceInfoVo.getSupplier() );
        
        bean.setRepairRecard( serviceInfoVo.getRepairRecard() );
        
        return bean;
    }


    /**
     * 
     * @description:to HwLedgerBean 
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param baseInfoVo
     * @return:
     */
    private HwLedgerBean setHwLedgerBean(BaseInfoVo baseInfoVo) {
        HwLedgerBean bean = new HwLedgerBean();
        bean.setHwId( baseInfoVo.getHwId() );
        bean.setHwName( baseInfoVo.getHwName() );
        bean.setHwType( baseInfoVo.getHwType() );
        bean.setParentId( baseInfoVo.getParentId() );
        
        return bean;
    }


    /**
     * 
     * @description:删除硬件台账
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/deleteHwLedger")
    public @ResponseBody Map<String, Object> deleteHwLedger( String hwType, String hwId ) {
        Map<String, Object> map = hwLedgerService.deleteHwLedger( hwType, hwId );
        
        return map ;    
    }
    
    /**
     * 
     * @description:通过HWID查询硬件台账服务器相关
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/queryHwLedgerServiceById")
    public @ResponseBody Map<String, Object> queryHwLedgerServiceById( String hwId ) {
        Map<String, Object> map = hwLedgerService.queryHwLedgerServiceById( hwId );
        
        return map ;    
    }
    
    
    /**
     * 
     * @description:通过HWID查询硬件台账虚机相关
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/queryHwLedgerVMById")
    public @ResponseBody Map<String, Object> queryHwLedgerVMById( String hwId ) {
        Map<String, Object> map = hwLedgerService.queryHwLedgerVMById( hwId );
        
        return map ;    
    }
    
    
    /**
     * 
     * @description:检查硬件台账名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/queryCheckHwLedgerName")
    public @ResponseBody Boolean queryCheckHwLedgerName( String paramsMap ) {
        JSONObject object = JSONObject.fromObject(paramsMap);
        String hwName = object.get("hwName").toString();
        String hwId = object.get("hwId").toString().trim();
        
        List<HwLedgerBean> result = hwLedgerService.queryCheckHwLedgerName( hwName, hwId );
        
        if( result != null && result.size() > 0  ){
            return false;
        }
        return true ;    
    }
    
    /**
     * 
     * @description:检查硬件台账名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     * @throws Exception 
     */
    @RequestMapping(value = "/querySwLedgerById")
    public @ResponseBody Map<String, Object> querySwLedgerById( String hwId ) throws Exception {
        /**
         * 根据硬件台账id查找部署在上面的所有的软件台账的应用
         */
        List<SwLedgerAppBean> list =  swLedgerService.querySwLedgerAppByHwId( hwId );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", list );
        if( list != null && list.size() > 0 ){
            dataMap.put( "total",  list.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
    }
    
    /**
     * 
     * @description:通过hwId查找HwLedgerDevice
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     * @throws Exception 
     */
    @RequestMapping(value = "/queryHwLedgerDeviceById")
    public @ResponseBody Map<String, Object> queryHwLedgerDeviceById( String hwId ) throws Exception {

        HwLedgerDeviceBean hwLedgerDeviceBean = new HwLedgerDeviceBean();
        if( StringUtils.isNotBlank( hwId ) ){
            hwLedgerDeviceBean =  hwLedgerService.queryHwLedgerDeviceById( hwId );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( hwLedgerDeviceBean.getHwId() ) ){
            dataMap.put( "result", "success" );
            dataMap.put( "hwLedgerDeviceBean", hwLedgerDeviceBean );
        }else{
            dataMap.put( "result", "fail" );
        }
        return dataMap;
    }
    
    /**
     * 
     * @description:树上DIV,当鼠标放在书上服务器或者虚机
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     * @throws Exception 
     */
    @RequestMapping(value = "/queryHwLedgerByTypeAndId")
    public @ResponseBody Map<String, Object> queryHwLedgerByTypeAndId( String hwId, String hwType ) throws Exception {
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( hwId ) && StringUtils.isNotBlank( hwType ) ){
            dataMap =  hwLedgerService.queryHwLedgerByTypeAndId( hwId, hwType );
        }
        
        return dataMap;
    }
    
    
    /**
     * 
     * @description:拖拽硬件台账树节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     * @throws Exception 
     */
    @RequestMapping(value = "/updateDropHwlTreeNode")
    public @ResponseBody Map<String, Object> updateDropHwlTreeNode( String id, String parentId ) throws Exception {
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( id ) && StringUtils.isNotBlank( parentId ) ){
            HwLedgerBean bean = hwLedgerService.queryHwLedgerDetail( id );
            /* HwLedgerBean sBean = hwLedgerService.queryHwLedgerDetail( bean.getParentId() );
            HwLedgerBean tBean = hwLedgerService.queryHwLedgerDetail( parentId );
            
            if( !StringUtils.equals( sBean.getHwType(), tBean.getHwType() )  ){
                dataMap.put( "result", "fail" );
                dataMap.put( "reason", "父节点类型不相同，不能移动！" );
            }*/
            
            if( StringUtils.equals( "Y", bean.getIsRoot() )){
                dataMap.put( "result", "forbidMoveRoot" );
            }else {
                int updateCount = hwLedgerTreeService.updateDropHwlTreeNode( id, parentId );
                if( updateCount > 0 ){
                    dataMap.put( "result", "ok" );
                }
            }
            
        }
        
        return dataMap;
    }
    
    /**
     * 
     * @description:硬件型号-放大镜 page
     * @author: fengzt
     * @createDate: 2014年12月18日
     * @param inputId
     * @param modeType
     * @return:ModelAndView
     */
    @RequestMapping("/showHwModelBox")
    @ReturnEnumsBind("AST_HW_MODEL_TYPE")
    public ModelAndView showHwModelBox( String inputId, String modelType ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "inputId", inputId );
        map.put( "modelType", modelType );
        
        return new ModelAndView( "hardware/HwLedger-showHwModelBox.jsp", map );
    }
    
    /**
     * 跳转到详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detailPage")
    @ReturnEnumsBind("AST_HW_TYPE,AST_HW_SERVICE_STATUS,AST_HW_VMDEPLOC,AST_HW_L_NETWORK_LOCATION,AST_HW_NETAREA")
    public ModelAndViewPage hwLedgerDetailPage() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String hwLedgerId = userInfo.getParam( "hwLedgerId" );
        String hwLedgerType = userInfo.getParam( "hwLedgerType" );
        
        HwLedgerBean hwLedgerBean=queryHwLedgerByType(hwLedgerId,hwLedgerType);       
        
        Map<String, String>result=new HashMap<String, String>();
        result.put("hwLedgerBean", JsonHelper.toJsonString(hwLedgerBean));
        return itcMvcService.Pages( "/hardware/detail.jsp", "result",result);
    }
    
    /**
     * 查询详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getHwLedgerDetail() throws Exception {
    	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String hwLedgerId = userInfo.getParam( "hwLedgerId" );
        String hwLedgerType = userInfo.getParam( "hwLedgerType" );
        
        HwLedgerBean hwLedgerBean=queryHwLedgerByType(hwLedgerId,hwLedgerType);
        
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put( "hwLedgerBean", hwLedgerBean );
        return itcMvcService.jsons(result);       
    }
    
    private HwLedgerBean queryHwLedgerByType(String hwLedgerId,String hwLedgerType)throws Exception{
    	HwLedgerBean hwLedgerBean=null;
        if ( hwLedgerId != null&&!"".equals(hwLedgerId) && hwLedgerType != null&&!"".equals(hwLedgerType) ) {
	        if("HW_L_NETWORK".equals(hwLedgerType)){//
	        	hwLedgerBean=hwLedgerEqptService.queryNetworkDetailById(hwLedgerId);
	        }else if("HW_L_ROOM_EQPT".equals(hwLedgerType)){//
	        	hwLedgerBean=hwLedgerEqptService.queryRoomEqptDetailById(hwLedgerId);
	        }else if("HW_L_STORAGE".equals(hwLedgerType)){//
	        	hwLedgerBean=hwLedgerEqptService.queryStorageDetailById(hwLedgerId);
	        }else{//不支持的类型
	        	
	        }
        }
        return hwLedgerBean;
    }
    
    private void fullInsertInfo(UserInfoScope userInfo,HwLedgerBean bean){//填充新建的其他信息
    	String siteId = userInfo.getSiteId();
    	bean.setSiteid(siteId);
        Date date=new Date();
        bean.setCreatedate(date);
        bean.setCreateuser(userInfo.getUserId());
        bean.setIsRoot("N");
    }
    
    @RequestMapping(value = "/insertHwLedger")
    @VaildParam(paramName = "hwLedgerBean")
    public ModelAndViewAjax insertHwLedger() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();        
        String type = userInfo.getParam( "hwLedgerType" );
        
        int rst=-1;
        Map<String, Object>result= new HashMap<String, Object>();
        if(type != null&&!"".equals(type)){
        	if("HW_L_NETWORK".equals(type)){//
        		HwLedgerNetworkBean bean=userInfo.getJavaBeanParam( "hwLedgerBean", HwLedgerNetworkBean.class );
        		fullInsertInfo(userInfo,bean);
        		rst=hwLedgerEqptService.insertNetwork(bean);
        		if(rst==1&&bean.getHwId()!=null){
        			bean=hwLedgerEqptService.queryNetworkDetailById( bean.getHwId() );
        			result.put("status", 1);
                	result.put("hwLedgerBean", bean);
        		}
	        }else if("HW_L_ROOM_EQPT".equals(type)){//
	        	HwLedgerRoomEqptBean bean=userInfo.getJavaBeanParam( "hwLedgerBean", HwLedgerRoomEqptBean.class );
	        	fullInsertInfo(userInfo,bean);
	        	rst=hwLedgerEqptService.insertRoomEqpt(bean);
	        	if(rst==1&&bean.getHwId()!=null){
        			bean=hwLedgerEqptService.queryRoomEqptDetailById( bean.getHwId() );
        			result.put("status", 1);
                	result.put("hwLedgerBean", bean);
        		}
	        }else if("HW_L_STORAGE".equals(type)){//
	        	HwLedgerStorageBean bean=userInfo.getJavaBeanParam( "hwLedgerBean", HwLedgerStorageBean.class );
	        	fullInsertInfo(userInfo,bean);
	        	rst=hwLedgerEqptService.insertStorage(bean);
	        	if(rst==1&&bean.getHwId()!=null){
        			bean=hwLedgerEqptService.queryStorageDetailById( bean.getHwId() );
        			result.put("status", 1);
                	result.put("hwLedgerBean", bean);
        		}
	        }
        }
        
        if(rst==-1){//类型错误			
        	result.put("status", -1);
		}else if(rst!=1){//新建失败
        	result.put("status", 0);
        }
        return itcMvcService.jsons(result);
    }
    
    /**
     * 删除
     * @return
     * @throws Exception
     */
    /*@RequestMapping(value="/deleteHwLedger")
    public ModelAndViewAjax deleteHwLedger() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String hwLedgerId = userInfoScope.getParam( "hwLedgerId" );
        
        return ViewUtil.Json(hwLedgerService.deleteHwLedger("", hwLedgerId));
    }*/
    
    private void fullUpdateInfo(UserInfoScope userInfo,HwLedgerBean bean){//填充更新的其他信息
    	String siteId = userInfo.getSiteId();
        bean.setSiteid(siteId);
        Date date=new Date();
        bean.setModifydate(date);
        bean.setModifyuser(userInfo.getUserId());
    }
    
    /**
     * 更新
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateHwLedger")
    @VaildParam(paramName = "hwLedgerBean")
    public ModelAndViewAjax updateHwLedger() throws Exception {
    	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();        
        String type = userInfo.getParam( "hwLedgerType" );
        
        int rst=-1;
        Map<String, Object>result= new HashMap<String, Object>();
        if(type != null&&!"".equals(type)){
        	if("HW_L_NETWORK".equals(type)){//
        		HwLedgerNetworkBean bean=userInfo.getJavaBeanParam( "hwLedgerBean", HwLedgerNetworkBean.class );
        		fullUpdateInfo(userInfo,bean);
        		rst=hwLedgerEqptService.updateNetwork(bean);
        		if(rst==1&&bean.getHwId()!=null){
        			bean=hwLedgerEqptService.queryNetworkDetailById( bean.getHwId() );
        			result.put("status", 1);
                	result.put("hwLedgerBean", bean);
        		}
	        }else if("HW_L_ROOM_EQPT".equals(type)){//
	        	HwLedgerRoomEqptBean bean=userInfo.getJavaBeanParam( "hwLedgerBean", HwLedgerRoomEqptBean.class );
	        	fullUpdateInfo(userInfo,bean);
	        	rst=hwLedgerEqptService.updateRoomEqpt(bean);
	        	if(rst==1&&bean.getHwId()!=null){
        			bean=hwLedgerEqptService.queryRoomEqptDetailById( bean.getHwId() );
        			result.put("status", 1);
                	result.put("hwLedgerBean", bean);
        		}
	        }else if("HW_L_STORAGE".equals(type)){//
	        	HwLedgerStorageBean bean=userInfo.getJavaBeanParam( "hwLedgerBean", HwLedgerStorageBean.class );
	        	fullUpdateInfo(userInfo,bean);
	        	rst=hwLedgerEqptService.updateStorage(bean);
	        	if(rst==1&&bean.getHwId()!=null){
        			bean=hwLedgerEqptService.queryStorageDetailById( bean.getHwId() );
        			result.put("status", 1);
                	result.put("hwLedgerBean", bean);
        		}
	        }
        }
        
        if(rst==-1){//类型错误			
        	result.put("status", -1);
		}else if(rst!=1){//更新失败
        	result.put("status", 0);
        }
        return itcMvcService.jsons(result);
    }
    
    /**
     * 判断站点下硬件台账的资产编号是否已存在
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/isAssetCodeExist")
    public ModelAndViewAjax isHwLedgerAssetCodeExist() throws Exception {
    	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String assetCode = userInfo.getParam( "assetCode" );
        String hwId = userInfo.getParam( "hwId" );
        return itcMvcService.jsons( checkHwLedgerAssetCodeExist(assetCode,hwId,userInfo.getSiteId()) );     
    }
    
    /**
     * 检查站点下硬件台账的资产编号是否已存在
     * @param type
     * @param name
     * @param excludeId：
     * @param siteId
     * @return
     * @throws Exception
     */
    private String checkHwLedgerAssetCodeExist(String code,String excludeId,String siteId) throws Exception {
    	HwLedgerBean infoBean = hwLedgerEqptService.queryHwLedgerByAssetCode(siteId,code);
        if(infoBean!=null&&!excludeId.equals(infoBean.getHwId()))
            return "硬件台账的资产编号已经存在";
        else
            return "true" ;
    }
    
    /**
     * 
     * @description:高级查询页面
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/searchHwLedgerPage")
    @ReturnEnumsBind("AST_HW_TYPE")
    public ModelAndView searchHwLedgerPage( String hwId, String hwName  ) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "hwId", hwId );
        map.put( "hwName", hwName );
        
        return new ModelAndView( "hardware/HwLedger-searchHwLedger.jsp", map );    
    }
    
    /**
     * 
     * @description:多条件查询跳转Page
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/queryHwLedgerMultiPage")
    @ReturnEnumsBind("AST_HW_TYPE")
    public ModelAndView queryHwLedgerMultiPage( String formData ) {
        Map<String, Object> map = new HashMap<String, Object>();
        MultiSearchVo multiSearchVo = JsonHelper.fromJsonStringToBean( formData, MultiSearchVo.class );
        map.put( "multiSearchVo", multiSearchVo );
        
        return new ModelAndView( "hardware/HwLedger-queryMultiHwLedger.jsp", map );    
    }
    
    /**
     * 
     * @description:多条件查询页面
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     */
    @RequestMapping(value = "/queryHwLedgerMulti")
    @ReturnEnumsBind("AST_HW_TYPE")
    public Map<String, Object> queryHwLedgerMulti( String formData, int rows, int page, String search, String sort, String order  ) {
        MultiSearchVo vo = JsonHelper.fromJsonStringToBean( formData, MultiSearchVo.class );
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<MultiSearchVo> beans = new ArrayList<MultiSearchVo>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "hwName" );
            pageVo.setSortOrder( "asc" );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            MultiSearchVo searchVo = JsonHelper.fromJsonStringToBean( search, MultiSearchVo.class );
            beans = hwLedgerService.queryHwLedgerMultiBySearch( vo, searchVo, pageVo );
            
        }else{
            //默认分页
            beans = hwLedgerService.queryHwLedgerMulti( vo, pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", beans );
        if( ! beans.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
}
