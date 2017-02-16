package com.timss.finance.service.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.timss.finance.bean.FinSubjectShowConfig;
import com.timss.finance.bean.FinanceGeneralLedgerInfo;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.dao.FinSubjectShowConfigDao;
import com.timss.finance.dao.FinanceGeneralLedgerInfoDao;
import com.timss.finance.dao.FinanceMainDao;
import com.timss.finance.dao.FinanceMainDetailDao;
import com.timss.finance.service.FinanceGeneralLedgerInfoService;
import com.timss.finance.util.Arith;
import com.timss.finance.util.FinanceUtil;
import com.timss.finance.vo.ErpIdsControl;
import com.timss.finance.vo.ErpIdsGLInt;
import com.yudean.interfaces.service.IEsbInterfaceService;
import com.yudean.itc.dto.interfaces.esb.ErpPutParam;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class FinanceGeneralLedgerInfoServiceImpl implements FinanceGeneralLedgerInfoService {
    Logger logger = Logger.getLogger( FinanceGeneralLedgerInfoServiceImpl.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    IEsbInterfaceService iEsbInterfaceService;
    @Autowired
    private FinanceGeneralLedgerInfoDao financeGeneralLedgerInfoDao;
    @Autowired
    private FinanceMainDao financeMainDao;
    @Autowired
    private FinanceMainDetailDao financeMainDetailDao;
    @Autowired
    private FinSubjectShowConfigDao finSubjectShowConfigDao;
    @Autowired
    IAuthorizationManager im;
    /**
     * @description: 通过报销单编号查询总帐信息
     * @author: 890170
     * @createDate: 2014-12-2
     */
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
//    // 事务控制要加这个注解
//    public List<FinanceGeneralLedgerInfo> queryFinanceGeneralLedgerInfoListByFid(String fid) {
//        return financeGeneralLedgerInfoDao.queryFinanceGeneralLedgerInfoListByFid( fid );
//    }

    /**
     * @description: 推送ERP总帐信息
     * @author: 890170
     * @createDate: 2014-12-2
     */
    @Override
    public String putGeneralLedgerInfo(String erpForm, String erpDetail) throws Exception {
        ErpPutParam erpPutParam = new ErpPutParam();

        String fFid = FinanceUtil.getJsonFieldString( erpForm, "fFid" ); // 报销编号
        String fCertHeadDesc = FinanceUtil.getJsonFieldString( erpForm, "fCertHeadDesc" ); // 凭证头描述
        String fCertType = FinanceUtil.getJsonFieldString( erpForm, "fCertType" ); // 凭证类别
       // String fSubType = FinanceUtil.getJsonFieldString( erpForm, "fSubType" ); // 子类
        String fAccMonth = FinanceUtil.getJsonFieldString( erpForm, "fAccMonth" ); // 期间
        // String fAccDate = FinanceUtil.getJsonFieldString( erpForm, "fAccDate"
        // ); //记账日期
        String fCcy = FinanceUtil.getJsonFieldString( erpForm, "fCcy" ); // 币种
        String fDocNbr = FinanceUtil.getJsonFieldString( erpForm, "fDocNbr" ); // 单据张数

        logger.info( "fDocNbr: " + fDocNbr );

        // 1.保存总帐数据入库(暂时不做)
        // financeGeneralLedgerInfoDao.insertFinanceGeneralLedgerInfo();

        // 2.推送ERP总帐信息
        JSONArray GLArr = JSONArray.fromObject( erpDetail );
        int recordCount = GLArr.size();
        BigDecimal recordCountDec = new BigDecimal( recordCount ); // 本批数据笔数,即借+贷笔数

        String subject[] = new String[recordCount]; // 科目
        String subjectremark[] = new String[recordCount]; // 科目描述
        String debitamt[] = new String[recordCount]; // 借方金额
        String creditamt[] = new String[recordCount]; // 贷方金额
        String cashitem[] = new String[recordCount]; // 现金流项目
        String internalunit[] = new String[recordCount]; // 内部单位
        String certrowdesc[] = new String[recordCount]; // 凭证行描述

        String segmentArr[];
        String segmentDescArr[];

        // 数据集data中包括接口控制信息ErpIdsControl和相应的一类财务数据（ErpIdsGLInt总帐）
        // 1.1.接口控制信息
        ErpIdsControl erpIdsControl = new ErpIdsControl();
        String controlId = iEsbInterfaceService.getErpControlID();
        logger.info( "controlId:\n" + controlId );

        Date curDateTime = new Date();

        BigDecimal controlIdDec = new BigDecimal( controlId );
        erpIdsControl.setControlId( controlIdDec ); // controlid唯一标识
        erpIdsControl.setSourceCode( "TIMSS" ); // 来源系统
        erpIdsControl.setSourceCompCode( "ITC" ); // 来源系统的公司三字码
        erpIdsControl.setTargetCode( "EBS" ); // 目标系统
        erpIdsControl.setTargetCompCode( "ITC" ); // 目标系统的公司三字码
        erpIdsControl.setTargetModel( "GL" );   //目标模块
        erpIdsControl.setProcessStatus( "U" );  //处理状态
        erpIdsControl.setReceiveStatus( "" );  //接收状态
        erpIdsControl.setRecordCount( recordCountDec ); // 本批数据笔数
        erpIdsControl.setErrorMessage( "" );  //处理错误信息
        erpIdsControl.setCreationDate( curDateTime );
        erpIdsControl.setCreatedBy( BigDecimal.valueOf( -1 ) );
        erpIdsControl.setLastUpdateDate( curDateTime );
        erpIdsControl.setLastUpdatedBy( BigDecimal.valueOf( -1 ) );
        erpIdsControl.setAttribute2( "外围报销系统" );   //凭证子类别

        XStream xstream = new XStream( new DomDriver() );
        xstream.alias( "ErpIdsGLInt", ErpIdsGLInt.class );
        xstream.alias( "ErpIdsControl", ErpIdsControl.class );
        List<Object> erpList = new ArrayList<Object>();
        erpList.add( erpIdsControl );

        // (2).总帐接口
        ErpIdsGLInt erpIdsGLInt;
        Date curDate = new Date();
        SimpleDateFormat sdfD = new SimpleDateFormat( "yyyy-MM-dd" );
        String sCurDate = sdfD.format( curDate );
        Date dCurDate = sdfD.parse( sCurDate );

        for ( int i = 0; i < GLArr.size(); i++ ) {
            subject[i] = GLArr.getJSONObject( i ).getString( "subject" ); // 科目
            subjectremark[i] = GLArr.getJSONObject( i ).getString( "subjectremark" ); // 科目描述
            debitamt[i] = GLArr.getJSONObject( i ).getString( "debitamt" ); // 借方金额
            creditamt[i] = GLArr.getJSONObject( i ).getString( "creditamt" ); // 贷方金额
            cashitem[i] = GLArr.getJSONObject( i ).getString( "cashitem" ).equals( "null" ) ? "" : GLArr.getJSONObject(
                    i ).getString( "cashitem" ); // 现金流项目
            internalunit[i] = GLArr.getJSONObject( i ).getString( "intervalunit" ).equals( "null" ) ? "" : GLArr
                    .getJSONObject( i ).getString( "intervalunit" ); // 内部单位
            certrowdesc[i] = GLArr.getJSONObject( i ).getString( "certrowdesc" ); // 凭证行描述

            segmentArr = new String[10];
            segmentDescArr = new String[10];
            segmentArr = subject[i].split( "\\." );
            segmentDescArr = subjectremark[i].split( "\\." );

            erpIdsGLInt = new ErpIdsGLInt();
            erpIdsGLInt.setControlId( controlIdDec );   //批次ID
            erpIdsGLInt.setCompCode( "ITC" );     //公司代码
            erpIdsGLInt.setSourceDocNum( fFid );    //源系统单据号
            erpIdsGLInt.setSobName( "YD_SOB" );   //分类帐名
            erpIdsGLInt.setAccountingDate( dCurDate );   //记账日期
            erpIdsGLInt.setCurrencyCode( fCcy );  //币种
            erpIdsGLInt.setUserJeCategoryName( fCertType );  //凭证种类
            erpIdsGLInt.setUserJeSourceName( "外围报销系统" );  //凭证来源
            erpIdsGLInt.setPeriodName( fAccMonth );    //期间
            erpIdsGLInt.setCurrencyConversionRate( new BigDecimal( 1.00 ) );   //利率
            erpIdsGLInt.setJeDesc( fCertHeadDesc );   //凭证头描述
            erpIdsGLInt.setJeLineNum( new Long( i + 1 ) );  //行号
            erpIdsGLInt.setLineDesc( certrowdesc[i] );   //凭证行描述
            erpIdsGLInt.setEnteredDr( debitamt[i].equals( "0" ) ? null : new BigDecimal( debitamt[i] ) );  //原币种借方金额
            erpIdsGLInt.setEnteredCr( creditamt[i].equals( "0" ) ? null : new BigDecimal( creditamt[i] ) );  //原币种贷方金额
            erpIdsGLInt.setSegment1( segmentArr[0] );
            erpIdsGLInt.setSegment2( segmentArr[1] );
            erpIdsGLInt.setSegment3( segmentArr[2] );
            erpIdsGLInt.setSegment4( segmentArr[3] );
            erpIdsGLInt.setSegment5( segmentArr[4] );
            erpIdsGLInt.setSegment6( segmentArr[5] );
            erpIdsGLInt.setSegment7( segmentArr[6] );
            erpIdsGLInt.setSegment8( segmentArr[7] );
            erpIdsGLInt.setSegment9( segmentArr[8] );
            erpIdsGLInt.setSegment10( segmentArr[9] );
            erpIdsGLInt.setAttribute1( fDocNbr ); // 附件张数
            erpIdsGLInt.setAttribute21( cashitem[i] );    //现金流项目
            erpIdsGLInt.setAttribute22( internalunit[i] );  //外部单位

            erpIdsGLInt.setCreationDate( curDateTime );
            erpIdsGLInt.setCreatedBy( BigDecimal.valueOf( -1 ) );
            erpIdsGLInt.setLastUpdateDate( curDateTime );
            erpIdsGLInt.setLastUpdatedBy( BigDecimal.valueOf( -1 ) );

            erpList.add( erpIdsGLInt );
        }

        String xmlData = xstream.toXML( erpList );

        logger.info( "xmlData:\n" + xmlData );

        erpPutParam.setData( xmlData );

        String resultData = iEsbInterfaceService.purErpData( erpPutParam );

        logger.info( "resultData:\n" + resultData );

        String returnMsg = "";

        SAXReader saxReader = new SAXReader();
        InputStream in_nocode = new ByteArrayInputStream( resultData.getBytes() );
        InputStream in_withcode = new ByteArrayInputStream( resultData.getBytes( "UTF-8" ) );

        logger.info( "in_nocode: " + in_nocode );
        logger.info( "in_withcode: " + in_withcode );

        Document document = saxReader.read( in_nocode );
        Element root = document.getRootElement();
        Element elResult = (Element) root.selectSingleNode( "/service/result" );
        Element elMsg = (Element) root.selectSingleNode( "/service/msg" );

        String sResult = elResult.getText();
        String sMsg = elMsg.getText();

        if ( sResult.equals( "1" ) ) {
            returnMsg = "success";
            logger.info( "INPUT DATA SUCCESS MSG: [" + sMsg + "]" );
        } else {
            returnMsg = "fail";
            logger.info( "INPUT DATA ERROR MSG: [" + sMsg + "]" );
        }

        // 3.更新报销主表信息
        FinanceMain financeMain = financeMainDao.queryFinanceMainByFid( fFid );
        financeMain.setFlag_item( "1" );
        financeMainDao.updateFinanceMainByFid( financeMain );

        return returnMsg;
    }

    /**
     * @description:
     * @author: 890170
     * @createDate: 2015-3-30
     */
    @Override
    public List<FinanceGeneralLedgerInfo> queryFinanceGeneralLedgerInfoListByFid2(Map<String, Object> paramsMap) {
        return financeGeneralLedgerInfoDao.queryFinanceGeneralLedgerInfoListByFid2( paramsMap );
    }

    @Override
    public List<FinanceGeneralLedgerInfo> getFinanceGeneralLedgerInfoList(String fid,String getReimbursementMan) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        FinanceMain financeMain = financeMainDao.queryFinanceMainByFid(fid);
        String finNameEn = financeMain.getFinance_flowid();  // 报销名称
        String finTypeEn = financeMain.getFinance_typeid();  // 报销类型
        //amount 用于存放个分录的额度（领导分录、技术副总分录、行政部、开发部、维护部、经营部、总账金额）顺序与bussSubjectIds[]要配套,最后一个元素存总额
        double[] amount = new double[7];   //如果是非业务招待费，则只有最后一个元素有用到，用于存储金额
        String[] bussSubjectIds = new String[7];  //业务报销费的科目subject_id
        //业务招待费报销的科目
        List<FinanceGeneralLedgerInfo> bussFinGeneralLedgerInfoList = initArrayAndBussFinGeneralLedgerInfoList(amount,bussSubjectIds);
        //要在凭证中显示的分录
        Set<String> subjectNoSet = new LinkedHashSet<String>(); 
       //查询数据库中配置的，某种报销流程的所有情况下的分录配置信息
        List<FinSubjectShowConfig>  finSubjectShowConfList = getParamsOfQueryFinanceGeneralLedger(finNameEn,siteid);
        //查询报销单对应的所有报销明细条目
        List<FinanceMainDetail> financeMainDetailList = financeMainDetailDao.queryFinanceMainDetailByFid( fid );
        
        if ( finNameEn.equals( "businessentertainment" ) ) { // 如果是业务招待费(可能同时存在多个分录)
            if ( finTypeEn.equals( "only" ) || finTypeEn.equals( "other" ) ) {
                onlyOtherSetAmountArr(financeMain,finSubjectShowConfList,bussFinGeneralLedgerInfoList,bussSubjectIds,amount,subjectNoSet);
            } else if ( finTypeEn.equals( "more" ) ) {  // 如果是多人报销
                moreSetAmountArr(financeMain,finSubjectShowConfList,bussFinGeneralLedgerInfoList,bussSubjectIds,amount,subjectNoSet);
            }
            for ( int i = 0; i < amount.length-2; i++ ) {  //最后一个元素就是存放总和的，所以只将前面的值累加存入最后一个元素里面
                amount[6] = Arith.add(amount[6],amount[i] );  //总和
            }
        }else { // 如果不是业务招待费(不会存在多个分录)，则完全按照数据库表中的分录配置添加，而且此时只有 amount[6]是有效数字，0-5都不需要
            double dtlAmount = 0.0;  //某一条明细的金额
            for ( int i = 0; i < financeMainDetailList.size(); i++ ) {
                dtlAmount = financeMainDetailList.get( i ).getAmount();
                for ( int j = 0; j < finSubjectShowConfList.size(); j++ ) {
                    FinSubjectShowConfig finSubjectShowConf = finSubjectShowConfList.get( j );
                    String[] subjectNoArr =  finSubjectShowConf.getShowSubjectNo().split( "," );
                    for ( int k = 0; k < subjectNoArr.length; k++ ) {
                        subjectNoSet.add(subjectNoArr[k]);
                    }
                    amount[6] = Arith.add(amount[6],dtlAmount);
                }
            }
        }

        Map<String, Object> paramsMap = new HashMap<String, Object>( 3 );
        paramsMap.put( "finNameEn", finNameEn );
        paramsMap.put( "subjectNoArr", subjectNoSet );
        // 2.根据规则抓取科目关联记录,查询条件为报销类型和科目序号
        List<FinanceGeneralLedgerInfo> financeGeneralLedgerInfoList = financeGeneralLedgerInfoDao.queryFinanceGeneralLedgerInfoListByFid2( paramsMap );
        // 根据报销类型,序号和借贷方向,加入金额
        setFinanceGeneralLedgerInfoListValue(financeGeneralLedgerInfoList,finNameEn,bussSubjectIds,amount,getReimbursementMan);
        
        return financeGeneralLedgerInfoList;
    }
    /**
     * @description: 多人 业务招待费报销
     * @author: 王中华
     * @createDate: 2015-9-8
     * @param financeMain  报销单信息
     * @param finSubjectShowConfList 查询数据库中配置的，某种报销流程的所有情况下的分录配置信息(此处为业务招待费报销)
     * @param bussFinGeneralLedgerInfoList 业务招待费报销的科目
     * @param bussSubjectIds 用于存储业务招待费科目中的subjectId字段的信息
     * @param amount 对应于上面的bussSubjectIds里的科目金额
     * @param subjectNoSet:要在凭证中显示的分录
     */
    private void moreSetAmountArr(FinanceMain financeMain, List<FinSubjectShowConfig> finSubjectShowConfList,
            List<FinanceGeneralLedgerInfo> bussFinGeneralLedgerInfoList, String[] bussSubjectIds, double[] amount,
            Set<String> subjectNoSet) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        double dtlAmount = 0.0;  //某一条明细的金额
        String finTypeEn = financeMain.getFinance_typeid();  // userInfoScope.getParam( "finTypeEn" ); // 报销类型
        String finLevel = financeMain.getFin_level();  //获取报销层面
        //查询报销单对应的所有报销明细条目
        List<FinanceMainDetail> financeMainDetailList = financeMainDetailDao.queryFinanceMainDetailByFid( financeMain.getFid() );
        for ( int i = 0; i < financeMainDetailList.size(); i++ ) {
            String  beneficiaryid = financeMainDetailList.get( i ).getBeneficiaryid();
            dtlAmount = financeMainDetailList.get( i ).getAmount();
            SecureUser su = im.retriveUserById( beneficiaryid, siteid );
            List<Role> roleList = su.getRoles();
            List<String> roleIdList = new ArrayList<String>();
            for ( int j = 0; j < roleList.size(); j++ ) {
                roleIdList.add( roleList.get( j ).getId() );
            }
            List<Organization> orgList = su.getOrganizations();

            String subjectseqNo = null; //“借”的subject_seq_no
            for ( int j = 0; j < finSubjectShowConfList.size(); j++ ) {
                FinSubjectShowConfig finSubjectShowConf = finSubjectShowConfList.get( j );
                //通过报销类型过滤
                if(finSubjectShowConf.getReimburseType().equals( finTypeEn )){
                    List<String> confRoleList = new ArrayList<String>();
                    if(finSubjectShowConf.getRoleid() != null){
                        confRoleList = Arrays.asList(finSubjectShowConf.getRoleid().split( "," ));
                    }
                    boolean tempflag = hasIntersection(roleIdList,confRoleList);
                    if(finSubjectShowConf.getRoleid() != null && tempflag==false){  //如果数据库里面配置了角色，而此时又没有交集，则这条配置不符合，跳出
                        continue;
                    }
                    if(tempflag){  //有交集, 如果是技术副总,则走技术副总分录
                        String[] subjectNoArr =  finSubjectShowConf.getShowSubjectNo().split( "," );
                        subjectseqNo = subjectNoArr[0];
                        for ( int k = 0; k < subjectNoArr.length; k++ ) {
                            subjectNoSet.add(subjectNoArr[k]);
                        }
                        break;
                    }else{  // 否则,走公司领导分录
                        //报销层面为空，或者不为空且与报销单的报销层面一样
                        boolean tempflag2 = (finSubjectShowConf.getFinLevel()!=null && finLevel.equals( finSubjectShowConf.getFinLevel()))
                                || finSubjectShowConf.getFinLevel() == null;
                        if(tempflag2){  //报销层面
                            boolean tempflag3 = (finSubjectShowConf.getDeptid()!=null && orgList.get( 0 ).getCode().equals( finSubjectShowConf.getDeptid()))
                                    || finSubjectShowConf.getDeptid() == null;
                            if(tempflag3){  //所在部门
                                String[] subjectNoArr =  finSubjectShowConf.getShowSubjectNo().split( "," );
                                subjectseqNo = subjectNoArr[0];
                                for ( int k = 0; k < subjectNoArr.length; k++ ) {
                                    subjectNoSet.add(subjectNoArr[k]);
                                }
                              //通过对于的subject_seq_no找到对应的subject_id,然后再根据这个subject_id的值对应的给amount[]里面赋值
                                if(subjectseqNo!=null){  //如果有找到对应的分录配置项
                                    for ( int m = 0; m < bussFinGeneralLedgerInfoList.size(); m++ ) {
                                        FinanceGeneralLedgerInfo bussFinGeneralLedgerInfo = bussFinGeneralLedgerInfoList.get( m );
                                        if(subjectseqNo.equals( bussFinGeneralLedgerInfo.getSubjectSeqNo())){
                                            String subjectid = bussFinGeneralLedgerInfo.getSubjectid(); //获取序号suject_seq_id对应的subject_id
                                            for ( int k = 0; k < bussSubjectIds.length; k++ ) {
                                                if(bussSubjectIds[k].equals( subjectid ) ){
                                                    amount[k] = Arith.add( amount[k], dtlAmount );;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    
                }
            }
        }
        
        
        
//        for ( int i = 0; i < financeMainDetailList.size(); i++ ) {
//            beneficiaryid = financeMainDetailList.get( i ).getBeneficiaryid();
//            dtlAmount = financeMainDetailList.get( i ).getAmount();
//            su = im.retriveUserById( beneficiaryid, siteid );
//            roleList = su.getRoles();
//            orgList = su.getOrganizations();
//
//            if ( FinanceUtil.containsRole( roleList, siteid+"_FGJSFZJL" ) ) { // 如果是技术副总,则走技术副总分录
//                subjectNoSet.add( "6" );
//                subjectNoSet.add( "7" );
//                jsViceAmount = Arith.add( jsViceAmount, dtlAmount );
//            } else { // 否则,走公司领导分录
//                subjectNoSet.add( "1" );
//                subjectNoSet.add( "7" );
//                leaderAmount = Arith.add( leaderAmount, dtlAmount );
//            }
//        }
        
        
        
    }

    /**
     * @description: 个人或者他人 业务招待费报销
     * @author: 王中华
     * @createDate: 2015-9-8
     * @param financeMain  报销单信息
     * @param finSubjectShowConfList 查询数据库中配置的，某种报销流程的所有情况下的分录配置信息(此处为业务招待费报销)
     * @param bussFinGeneralLedgerInfoList 业务招待费报销的科目
     * @param bussSubjectIds 用于存储业务招待费科目中的subjectId字段的信息
     * @param amount 对应于上面的bussSubjectIds里的科目金额
     * @param subjectNoSet:要在凭证中显示的分录
     */
    private void onlyOtherSetAmountArr(FinanceMain financeMain,List<FinSubjectShowConfig>  finSubjectShowConfList,
            List<FinanceGeneralLedgerInfo> bussFinGeneralLedgerInfoList,String[] bussSubjectIds, double[] amount, Set<String> subjectNoSet) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        double dtlAmount = 0.0;  //某一条明细的金额
        String finTypeEn = financeMain.getFinance_typeid();  // userInfoScope.getParam( "finTypeEn" ); // 报销类型
        String finLevel = financeMain.getFin_level();  //获取报销层面
        //查询报销单对应的所有报销明细条目
        List<FinanceMainDetail> financeMainDetailList = financeMainDetailDao.queryFinanceMainDetailByFid( financeMain.getFid() );
        
        for ( int i = 0; i < financeMainDetailList.size(); i++ ) {  //对每一条报销明细进行依次处理
            String beneficiaryid = financeMainDetailList.get( i ).getBeneficiaryid();  //报销人ID
            dtlAmount = financeMainDetailList.get( i ).getAmount();  //报销明细金额
            SecureUser su = im.retriveUserById( beneficiaryid, siteid );  
            List<Role> roleList = su.getRoles();  //报销人拥有的角色集合
            List<String> roleIdList = new ArrayList<String>();
            for ( int j = 0; j < roleList.size(); j++ ) {
                roleIdList.add( roleList.get( j ).getId() );
            }
            List<Organization> orgList = su.getOrganizations();  //报销人所在部门集合
            
            //依次遍历某个报销类型的finSubjectShowConfList配置记录，
            for ( int j = 0; j < finSubjectShowConfList.size(); j++ ) {  
                String subjectseqNo = null; //“借”的subject_seq_no
                FinSubjectShowConfig finSubjectShowConf = finSubjectShowConfList.get( j );
                //通过报销类型过滤
                if(finSubjectShowConf.getReimburseType().equals( finTypeEn )){
                    List<String> confRoleList = new ArrayList<String>();
                    if(finSubjectShowConf.getRoleid() != null){
                        confRoleList = Arrays.asList(finSubjectShowConf.getRoleid().split( "," ));
                    }
                    //判断roleIdList中是否有confRoleList中的元素
                    boolean tempflag = hasIntersection(roleIdList,confRoleList);
                    if(tempflag){  //有交集,根据角色判断是否是总经理或经营副总,则走公司领导分录；如果是技术副总,则走技术副总分录
                        //填充subjectNoSet，并赋值subjectseqNo
                        subjectseqNo = fillSubjectNoSet(subjectNoSet,finSubjectShowConf);
                        break;
                    }else{  // 否则为普通员工
                        if(finLevel.equals( finSubjectShowConf.getFinLevel() )){  //报销层面
                            if(orgList.get( 0 ).getCode().equals( finSubjectShowConf.getDeptid() )){  //所在部门
                                subjectseqNo = fillSubjectNoSet(subjectNoSet,finSubjectShowConf);
                            }
                        }
                    }
                   //通过对于的subject_seq_no找到对应的subject_id,然后再根据这个subject_id的值对应的给amount[]里面赋值
                    if(subjectseqNo!=null){  //如果有找到对应的分录配置项
                        for ( int m = 0; m < bussFinGeneralLedgerInfoList.size(); m++ ) {
                            FinanceGeneralLedgerInfo bussFinGeneralLedgerInfo = bussFinGeneralLedgerInfoList.get( m );
                            if(subjectseqNo.equals( bussFinGeneralLedgerInfo.getSubjectSeqNo())){
                                String subjectid = bussFinGeneralLedgerInfo.getSubjectid(); //获取序号suject_seq_id对应的subject_id
                                for ( int k = 0; k < bussSubjectIds.length; k++ ) {
                                    if(bussSubjectIds[k].equals( subjectid ) ){
                                        amount[k] = Arith.add( amount[k], dtlAmount );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2015-9-7
     * @param financeGeneralLedgerInfoList  科目关联记录
     * @param finNameEn 报销流程编码
     * @param bussSubjectIds 业务招待费对应的SubjectId
     * @param amount 金额
     * @param getReimbursementMan: 报销人名字
     */
    private void setFinanceGeneralLedgerInfoListValue(List<FinanceGeneralLedgerInfo> financeGeneralLedgerInfoList,
            String finNameEn,String[] bussSubjectIds,double[] amount,String getReimbursementMan) {
        for ( int j = 0; j < financeGeneralLedgerInfoList.size(); j++ ) {
            FinanceGeneralLedgerInfo financeGeneralLedgerInfo = financeGeneralLedgerInfoList.get( j );
            if ( finNameEn.equals( "businessentertainment" ) ) {
                String subjectid = financeGeneralLedgerInfo.getSubjectid();
                for ( int j2 = 0; j2 < bussSubjectIds.length; j2++ ) {
                     String tempSubjectId = bussSubjectIds[j2];
                     if(tempSubjectId.equals( subjectid )){
                         if(j2!=bussSubjectIds.length-1){
                             financeGeneralLedgerInfo.setDebitamt(amount[j2]); //各个分目录的额度（借）
                         }else{
                             financeGeneralLedgerInfo.setCreditamt( amount[j2] ); //总额（贷）
                         }
                     }
                }
            } else { // 如果不是业务招待费
                if ( financeGeneralLedgerInfo.getBorrowdirection().equals( "D" ) ) {  //D代表借
                    financeGeneralLedgerInfo.setDebitamt( amount[6] );
                } else if ( financeGeneralLedgerInfo.getBorrowdirection().equals( "C" ) ) {  //C代表贷
                    financeGeneralLedgerInfo.setCreditamt( amount[6] );
                }
            }

            // 替代凭证行描述的名称"XXX"为实际取报销款人的名称
            financeGeneralLedgerInfo.setCertrowdesc( financeGeneralLedgerInfo.getCertrowdesc().replace( "XXX",
                    getReimbursementMan ) );
        }
        
    }

    /**
     * @description:将finSubjectShowConf的SubjectNo分解后填入到subjectNoSet，并将它的第一记录返回（1,7 则返回1）
     * @author: 王中华
     * @createDate: 2015-9-7
     * @param subjectNoSet
     * @param finSubjectShowConf
     * @return:
     */
    private String fillSubjectNoSet(Set<String> subjectNoSet, FinSubjectShowConfig finSubjectShowConf) {
        String[] subjectNoArr =  finSubjectShowConf.getShowSubjectNo().split( "," );
        for ( int k = 0; k < subjectNoArr.length; k++ ) {
            subjectNoSet.add(subjectNoArr[k]);
        }
        return subjectNoArr[0];
    }

    /**
     * @description: 初始化数组amount、bussSubjectIds、并查询所有业务招待费报销的fin_subject_match科目记录
     * @author: 王中华
     * @createDate: 2015-9-7
     * @param amount
     * @param bussSubjectIds
     * @return:
     */
    private List<FinanceGeneralLedgerInfo> initArrayAndBussFinGeneralLedgerInfoList(double[] amount,
            String[] bussSubjectIds) {
        //从表fin_subject_match中获取“业务招待费报销”的所有记录
        List<FinanceGeneralLedgerInfo> bussFinGeneralLedgerInfoList = financeGeneralLedgerInfoDao.queryFinanceGeneralLedgerInfoListByFinType( "businessentertainment" );
        for ( int i = 0; i < bussFinGeneralLedgerInfoList.size(); i++ ) {
            bussSubjectIds[i] = bussFinGeneralLedgerInfoList.get( i ).getSubjectid();
            amount[i] = 0.0;
        }
        return bussFinGeneralLedgerInfoList;
    }

    /**
     * @description: 判断roleIdList 中是否包含 confRoleList中的某个元素（是否有交集）
     * @author: 王中华
     * @createDate: 2015-9-7
     * @param roleIdList
     * @param confRoleList
     * @return:
     */
    private boolean hasIntersection(List<String> roleIdList, List<String> confRoleList) {
       boolean result = false; 
       for ( int i = 0; i < roleIdList.size(); i++ ) {
           String roleid1 = roleIdList.get( i ).trim();
           for ( int j = 0; j < confRoleList.size(); j++ ) {
               if(roleid1.equals( confRoleList.get(j).trim() )){
                   return true;
               }
           }
       }
        return result;
    }

    private List<FinSubjectShowConfig> getParamsOfQueryFinanceGeneralLedger(String finNameEn,String siteid) {
        return finSubjectShowConfigDao.getFinSubjectShowConfByFlowType( finNameEn, siteid );
    }
}
