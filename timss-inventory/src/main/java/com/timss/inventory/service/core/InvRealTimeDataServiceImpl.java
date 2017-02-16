package com.timss.inventory.service.core;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvItemBaseField;
import com.timss.inventory.bean.InvMatMap;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvMatTranLog;
import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.bean.InvRealTimeData;
import com.timss.inventory.dao.InvMatMapDao;
import com.timss.inventory.dao.InvMatTranLogDao;
import com.timss.inventory.dao.InvMatTranRecDao;
import com.timss.inventory.dao.InvRealTimeDataDao;
import com.timss.inventory.service.InvFormulaDefService;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.utils.InvRealTimeType;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 实时计算库存数量接口实现类
 * @description: 实时计算库存数量接口实现类
 * @company: gdyd
 * @className: InvRealTimeDataServiceImpl.java
 * @author: yuanzh
 * @createDate: 2016-4-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
@Service ( "InvRealTimeDataServiceImpl" )
public class InvRealTimeDataServiceImpl implements InvRealTimeDataService {

    private static final Logger  LOG = Logger.getLogger( InvRealTimeDataServiceImpl.class );

    @Autowired
    private ItcMvcService	itcMvcService;

    @Autowired
    private InvFormulaDefService invFormulaDefService;

    @Autowired
    private InvRealTimeDataDao   invRealTimeDataDao;

    @Autowired
    private InvMatTranLogDao     invMatTranLogDao;
    
    @Autowired
    private InvMatTranRecDao  invMatTranRecDao;
    
    @Autowired
    private InvMatMapDao invMatMapDao;

    /**
     * @description:更新/插入库存实时数据接口
     * @author: yuanzh
     * @createDate: 2016-5-5
     * @param caluItemEntry: 物资条目集合
     * @param type: 执行类型
     * @return
     * @throws Exception :
     */

    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public void realTimeCaluByType ( List< InvItemBaseField > caluItemEntry , String type ) {
        LOG.info( ">>>>>>>>>>>>>>>>>>> 开始执行realTimeCaluByType... type:" + type );
        try {
            Method method = this.getClass().getDeclaredMethod( "saveInvRealTimeDataValueByType" + type ,
                    InvItemBaseField.class );
            method.setAccessible( true );
            if ( null != caluItemEntry && !caluItemEntry.isEmpty() ) {
                for ( InvItemBaseField iibf : caluItemEntry ) {
                    LOG.debug( ">>>>>>>>>>>>>>>>>>> 传入参数InvItemBaseField中的 itemid：" + iibf.getItemid()
                            + " | invcateid：" + iibf.getInvcateid() + " | siteid：" + iibf.getSiteid()
                            + " | warehouseid：" + iibf.getWarehouseid() );

                    method.invoke( this , iibf );
                }
            }
        } catch ( Exception e ) {
            LOG.info( ">>>>>>>>>>>>>>>>>>> 更新库存实时数据接口异常" + e );
        }
        LOG.info( ">>>>>>>>>>>>>>>>>>> 完成执行realTimeCaluByType... " );
    }

    /**
     * @description: 更新/插入业务相关的数据
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param caluItem: 物资条目
     */
    private void saveInvRealTimeDataValueByTypeBusi ( InvItemBaseField caluItem ) {
        LOG.info( ">>>>>>>>>>>>>>>>>>> 开始执行saveInvRealTimeDataValueByTypeBusi... " );
        saveInvRealTimeDataValue( caluItem , InvRealTimeType.REALTIME_BUSI );
    }

    /**
     * @description: 更新/插入物资主项目相关的数据
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param caluItem: 物资条目
     */
    private void saveInvRealTimeDataValueByTypeRunw ( InvItemBaseField caluItem ) {
        LOG.info( ">>>>>>>>>>>>>>>>>>> 开始执行saveInvRealTimeDataValueByTypeRunw... " );
        saveInvRealTimeDataValue( caluItem , InvRealTimeType.REALTIME_RUNW );
    }

    /**
     * @description:更新插入所有物资数据字段
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param caluItem: 物资条目
     */
    private void saveInvRealTimeDataValueByTypeAll ( InvItemBaseField caluItem ) {
        LOG.info( ">>>>>>>>>>>>>>>>>>> 开始执行saveInvRealTimeDataValueByTypeAll... " );
        saveInvRealTimeDataValue( caluItem , InvRealTimeType.REALTIME_ALL );
    }

    /**
     * @description: 保存实时数据（不分类型）
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param caluItem 计算条目
     * @param invRealTimeType: 计算类型
     */
    private void saveInvRealTimeDataValue ( InvItemBaseField caluItem , String invRealTimeType ) {
        LOG.info( ">>>>>>>>>>>>>>>>>>> 开始执行saveInvRealTimeDataValue... invRealTimeType：" + invRealTimeType );

        // 因为不能保证所有的siteid都用基类的siteid，有一些类是自己写的siteid，所以这里就要统一赋值
        if ( null == caluItem.getSiteid() ) {
            caluItem.setSiteid( itcMvcService.getUserInfoScopeDatas().getSiteId() );
        }

        try {
            String formulaSql = invFormulaDefService.queryAndReturnFormulaByType( invRealTimeType ,
                    caluItem.getSiteid() );
            LOG.debug( ">>>>>>>>>>>>>>>>>>> 查询返回的数据库脚本为：" + formulaSql );

            if ( null != formulaSql ) {
                // 将数据填入到查询出来的脚本中
                String canExecSql = invFormulaDefService.setRealData2TheScript( caluItem , formulaSql );
                LOG.debug( ">>>>>>>>>>>>>>>>>>> 整理后的脚本为：" + canExecSql );

                InvRealTimeData invRealTimeData = invRealTimeDataDao.queryInvRealTimeDataByScript( canExecSql );
                if ( null != invRealTimeData ) {
                    invRealTimeData = mergeInvItemBaseField2InvRealTimeData( caluItem , invRealTimeData );

                    LOG.debug( ">>>>>>>>>>>>>>>>>>> insertOrUpdateInvRealTimeData方法开始执行... itemid："
                            + invRealTimeData.getItemid() + " | invcateid：" + invRealTimeData.getInvcateid()
                            + " | warehouseid：" + invRealTimeData.getWarehouseid() + " | siteid："
                            + invRealTimeData.getSiteid() );

                    insertOrUpdateInvRealTimeData( invRealTimeData );
                    LOG.info( ">>>>>>>>>>>>>>>>>>> insertOrUpdateInvRealTimeData方法执行结束..." );
                }
            }

        } catch ( Exception e ) {
            LOG.info( ">>>>>>>>>>>>>>>>>>> 保存实时数据接口异常" + e );
        }
    }

    /**
     * @description: 将基类中的数据转换到查询后的实体中
     * @author: yuanzh
     * @createDate: 2016-5-10
     * @param caluItem 物资原数据
     * @param invRealTimeData 查询后记录
     * @return:
     */
    private InvRealTimeData mergeInvItemBaseField2InvRealTimeData ( InvItemBaseField caluItem ,InvRealTimeData invRealTimeData ) {
        if ( null == invRealTimeData ) {
            invRealTimeData = new InvRealTimeData();
        }
        invRealTimeData.setItemid( caluItem.getItemid() );
        invRealTimeData.setItemcode( caluItem.getItemcode() );
        invRealTimeData.setInvcateid( caluItem.getInvcateid() );
        invRealTimeData.setSiteid( caluItem.getSiteid() );
        return invRealTimeData;
    }

    /**
     * @description:插入或者保存实时数据
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param invRealTimeData: 实体数据
     */
    private void insertOrUpdateInvRealTimeData ( InvRealTimeData invRealTimeData ) {
        String itemId = invRealTimeData.getItemid();
        String invCateId = invRealTimeData.getInvcateid();
        String siteId = invRealTimeData.getSiteid();

        // 用itemId、invCateId、siteId来联合查询有就更新，没有就插入
        InvRealTimeData searchIRTData = invRealTimeDataDao.queryInvRealTimeDataByCompositeKey( itemId , invCateId , siteId );
        if ( null != searchIRTData ) {
            invRealTimeData.setIrtdId( searchIRTData.getIrtdId() );
            invRealTimeDataDao.updateInvRealTimeData( invRealTimeData );
        } else {
            invRealTimeDataDao.insertInvRealTimeData( invRealTimeData );
        }
    }

    /**
     * @description:根据旧的流水和映射关系插入新的流水和映射关系
     * @author: 890151
     * @createDate: 2016-6-2
     * @param imtd: 旧的流水
     * @param imm: 旧的映射关系
     * @param falg: 价格控制标识（flag=1批次价 flag=2实时价 flag=3自定义价格）
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public Map< String , Object > insertNewRecAndMap ( InvMatTranDetail imtd, InvMatMapping immOld, String flag ) {
    	//返回结果
        Map< String , Object > resultMap = new HashMap<String, Object>();
    	//转换为新的流水和映射对象
        InvMatTranRec imtr = CommonUtil.conventTranDetailToTranRec(imtd);
        InvMatMap immNew = CommonUtil.conventInvMatMappingToInvMatMap(immOld);
        // 出库数量
        BigDecimal outQty = imtr.getOutQty();
        // 入库数量
        BigDecimal inQty = imtr.getInQty();
        // 物资id
        String itemid = imtr.getItemid();
        // 物资分类id
        String invcateid = imtr.getInvcateid();
        // 站点id
        String siteid = imtr.getSiteId();
        // 新流水的不含税单价
        BigDecimal noTaxPrice = null;
        // 新流水的含税单价
        BigDecimal price = null;
        // 新流水的税额
        BigDecimal tax = null;
        //查询实时表数据
        InvRealTimeData invRealTimeData = invRealTimeDataDao.queryInvRealTimeDataByCompositeKey( itemid , invcateid , siteid );
        
        //出入库价格控制各不一样
        if ( outQty != null && outQty.compareTo( new BigDecimal( 0 ) ) == 1
                && (inQty==null || inQty.compareTo( new BigDecimal( 0 ) ) == 0) ) {
            //出库流水的价格根据flag进行控制
            if("1".equals(flag)){
            	//执行出库算法获取批次价
            	Map<String, Object> outInfo = realTimeCaluAndUpdateTran(imtr,false);
        	    // 更新出库批次的含税、不含税和税额字段
        	    price = new BigDecimal( String.valueOf( outInfo.get( "price" ) ) );
        	    noTaxPrice = new BigDecimal( String.valueOf( outInfo.get( "noTaxPrice" ) ) );
        	    tax = price.subtract( noTaxPrice );
        	    imtr.setPrice( price.divide( outQty , 6 , BigDecimal.ROUND_DOWN ) );
        	    imtr.setNoTaxPrice( noTaxPrice.divide( outQty , 6 , BigDecimal.ROUND_DOWN ) );
        	    imtr.setTax( price.subtract( noTaxPrice ) );
                LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时出库流水价格取批次价, flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
            }
            else if ("2".equals(flag)) {
                if(invRealTimeData!=null){
                	price = invRealTimeData.getWithTaxPrice();
                	noTaxPrice = invRealTimeData.getNoTaxPrice();
                	tax = invRealTimeData.getTax();
                	imtr.setPrice(price);
                	imtr.setNoTaxPrice(noTaxPrice);
                	imtr.setTax(tax);
                    LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时出库流水价格取实时价, flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
                }
                else{
                	imtr.setPrice(null);
                	imtr.setNoTaxPrice(null);
                	imtr.setTax(null);
                    LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时出库流水价格未取实时价, flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
                }
            }    
            else if ("3".equals(flag)) {
        		//imtr已包含了业务模块传入的含税、不含税和税额信息，此处获取是为了打印日志
            	price = imtr.getPrice();
            	noTaxPrice = imtr.getNoTaxPrice();
            	tax = imtr.getTax();
                LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时出库流水价格由业务模块设置, flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
            }
            else{
            	imtr.setPrice(null);
            	imtr.setNoTaxPrice(null);
            	imtr.setTax(null);
                LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时出库流水标志位异常,价格统一设置为0, flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
            }
            //设置价格标志位
        	imtr.setPriceFlag(flag);
            //出库流水的可出库数量设置为0
        	imtr.setCanOutQty(new BigDecimal(0));   
        	//插入新的流水和映射关系
            insertRecAndMap(imtr, immNew);
            //记录出入库对应关系
            resultMap = realTimeCaluAndUpdateTran(imtr,true);
        }
        else {
        	//对于入的操作，有的业务模块要用实时价（如盘盈），在此处进行查询和设置，其他一律根据imtr中的业务模块传入的含税、不含税和税额信息
            if ("2".equals(flag)) {
                if(invRealTimeData!=null){
                	price = invRealTimeData.getWithTaxPrice();
                	noTaxPrice = invRealTimeData.getNoTaxPrice();
                	tax = invRealTimeData.getTax();
                	imtr.setPrice(price);
                	imtr.setNoTaxPrice(noTaxPrice);
                	imtr.setTax(tax);
                    LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时入库流水价格取实时价, flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
                }
                else{
                	imtr.setPrice(null);
                	imtr.setNoTaxPrice(null);
                	imtr.setTax(null);
                    LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时入库流水价格未取实时价, flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
                }
            }  
            else{
            	price = imtr.getPrice();
            	noTaxPrice = imtr.getNoTaxPrice();
            	tax = imtr.getTax();
                LOG.info( ">>>>>>>>>>>>>>>>>>> insertNewRecAndMap 执行时入库流水价格由业务模块设置, 此处flag=" + flag + ";price=" + price + ";noTaxPrice=" + noTaxPrice + ";tax=" + tax );
            }
    		//imtr已包含了业务模块传入的含税、不含税和税额信息
        	imtr.setPriceFlag(flag);
        	//选候选批次时，物资入库类型流水不作为候选批次，其余类型（接收、移库入、退库、盘盈、调价）流水可出库数量和入库数量一致
        	imtr.setCanOutQty(imtd.getInQty());
        	//插入新的流水和映射关系
        	insertRecAndMap(imtr,immNew);
		}
        //返回新流水的三价
        resultMap.put("price", imtr.getPrice());
        resultMap.put("noTaxPrice", imtr.getNoTaxPrice());
        resultMap.put("tax", imtr.getTax());
        //TODO 候选批次不足 返回信息告诉业务模块
		return resultMap;
   }    
    
    
    /**
     * @description: 出库算法
     * @author: yuanzh
     * @param imtd 旧的流水（必须包含itemid,invcateid,siteId,outQty,inQty)
     * @param update: 是否执行还是只是预演一遍
     * @createDate: 2016-5-4
     * @return:返回模拟出库结果（包含此处出库物资的含税、不含税单价、税额和受影响的入库批次）
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public Map< String , Object > realTimeCaluAndUpdateTran ( InvMatTranRec imtr, Boolean update ) {
    	//返回结果
    	Map< String , Object > resultMap = new HashMap<String, Object>();
        // 出库数量
        BigDecimal outQty = imtr.getOutQty();
        // 入库数量
        BigDecimal inQty = imtr.getInQty();
        // 物资id
        String itemid = imtr.getItemid();
        // 物资分类id
        String invcateid = imtr.getInvcateid();
        // 站点id
        String siteid = imtr.getSiteId();
        
        // 出库数量大于0 而且 入库数量为0，这个是物资出库的标识
        if ( outQty != null && outQty.compareTo( new BigDecimal( 0 ) ) == 1
                && (inQty==null || inQty.compareTo( new BigDecimal( 0 ) ) == 0) ) {
            // 找到同样的物资在流水明细表中是否还存在库存 queryTranRecByBatch
            // 逻辑是按照itemid、invcateid和siteid三个字段来查询，然后按照outQty为0、inQty大于0、canOutQty大于0进行筛选，最后按照createdate来排升序
            List< InvMatTranRec > imtrList = invMatTranRecDao.queryTranRecByBatch( itemid , invcateid , siteid );

            // 调用出库算法并更新受影响的入库批次的can_out_qty字段
            resultMap = sortOutAndUpdateData( imtrList , imtr , outQty, update);
        }
		return resultMap;
   }

    /**
     * @description: 整理并更新数据
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @param imtrList 流水表物资
     * @param imtr 当前出库物资
     * @param outQty: 出库数量
     * @param update: 是否执行更新
     * @return:返回模拟出库结果（包含此处出库物资的含税、不含税单价、税额，受影响的入库批次列表，出入库对应关系列表）
     */
    private Map< String , Object > sortOutAndUpdateData ( List< InvMatTranRec > imtrList , InvMatTranRec imtr , 
    		BigDecimal outQty, Boolean update ) {
    	Map< String , Object > resultMap = new HashMap<String, Object>();
    	// 如果查出来的物资不为空的话证明还可以出库
    	if ( null != imtrList && !imtrList.isEmpty() ) {
    		resultMap = sortOutData( imtrList , imtr , outQty);

    	    if(update){
            	// 更新受影响的入库批次的可出库数量字段
        	    List< InvMatTranRec > updateImtrList = ( List< InvMatTranRec > ) resultMap.get( "updateImtrList" );
            	if ( null != updateImtrList && !updateImtrList.isEmpty() ) {
            	    for ( InvMatTranRec updateImtr : updateImtrList ) {
            	    	invMatTranRecDao.updateInvMatTranRec( updateImtr );
            	    }
            	}

        	    // 记录对应关系
        	    List< InvMatTranLog > updateImtlList = ( List< InvMatTranLog > ) resultMap.get( "updateImtlList" );
            	if ( null != updateImtlList && !updateImtlList.isEmpty() ) {
            	    for ( InvMatTranLog updateImtl : updateImtlList ) {
                        invMatTranLogDao.insertInvMatTranLog( updateImtl );
            	    }
            	}
    	    }
    	}
    	return resultMap;
    }

    /**
     * @description: 整理数据
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @updateUser: 890151
     * @param imtrList 流水表物资
     * @param imtr 当前出库物资
     * @param outQty: 出库数量
     * @param update: 是否执行更新
     * @return:返回模拟出库结果（包含此处出库物资的含税、不含税单价、税额，受影响的入库批次列表，出入库对应关系列表）
     * @return:
     */
    private Map< String , Object > sortOutData ( List< InvMatTranRec > imtrList , InvMatTranRec imtr , BigDecimal outQty ) {
    	// 用户信息
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 待返回数据
        Map< String , Object > reMap = new HashMap< String , Object >();
        // 受影响的入库批次
        List< InvMatTranRec > updateImtrList = new ArrayList< InvMatTranRec >();
        // 需要记录的批次对应关系
        List< InvMatTranLog > updateImtlList = new ArrayList< InvMatTranLog >();
        // 出库不含税单价
        BigDecimal noTaxPrice = new BigDecimal( 0.0D );
        // 出库含税单价
        BigDecimal price = new BigDecimal( 0.0D );
        // 可出库数量
        BigDecimal canOutQty = new BigDecimal( 0.0D );
        // 减法临时数量
        BigDecimal subtrQty = outQty;
        // 出库是否成功
        Boolean outSuccess = false;

        for ( InvMatTranRec imtrIn : imtrList ) {
            // 记录出库来源批次对应关系
            InvMatTranLog invMatTranLog = new InvMatTranLog();
            String imtlId = UUIDGenerator.getUUID();
            invMatTranLog.setImtlId( imtlId );// 主键id
            invMatTranLog.setImtdId( imtr.getImtdid() );// 出库流水ID
            invMatTranLog.setFromImtdId( imtrIn.getImtdid() );// 来自的入库记录流水ID
            invMatTranLog.setTranCategory("OUT");
            invMatTranLog.setSiteid( userInfo.getSiteId() );
            invMatTranLog.setCreatedate( new Date() );
            invMatTranLog.setCreateuser( userInfo.getUserId() );

            // 遍历后获取可出库数量
            canOutQty = imtrIn.getCanOutQty();
            // 相减后得出是否能够整个批次出库
            subtrQty = canOutQty.subtract( subtrQty );
            
            if ( subtrQty.compareTo( new BigDecimal( 0 ) ) == -1 ) {
                //更新入库批次的可出库数量
                imtrIn.setCanOutQty( new BigDecimal( 0 ) );
                updateImtrList.add( imtrIn );
                //插入出库来源批次对应关系（新增的对应关系中，从来源批次出库的数量为来源批次所有）
                invMatTranLog.setTranQty( new BigDecimal(canOutQty.toString()) );
                updateImtlList.add( invMatTranLog );
                // 一个批次数量不够当前出库的情况将可出库数量为0，并获取当前批次的非税价格
                //noTaxPrice = noTaxPrice.add( canOutQty.multiply( imtrIn.getNoTaxPrice() ) );
                //price = price.add( canOutQty.multiply( imtrIn.getPrice() ) );

            } else if ( subtrQty.compareTo( new BigDecimal( 0 ) ) == 0 ) {
                //更新入库批次的可出库数量
                imtrIn.setCanOutQty( new BigDecimal( 0 ) );
                updateImtrList.add( imtrIn );
                //插入出库来源批次对应关系（新增的对应关系中，从来源批次出库的数量为来源批次所有）
                invMatTranLog.setTranQty( new BigDecimal(canOutQty.toString()) );
                updateImtlList.add( invMatTranLog );
                // 一个批次数量不够当前出库的情况将可出库数量为0，并获取当前批次的非税价格
                //noTaxPrice = noTaxPrice.add( canOutQty.multiply( imtrIn.getNoTaxPrice() ) );
                //price = price.add( canOutQty.multiply( imtrIn.getPrice() ) );
            	outSuccess = true;
                break;
            } else {
                //更新入库批次的可出库数量
                imtrIn.setCanOutQty( subtrQty );
                updateImtrList.add( imtrIn );
                //插入出库来源批次对应关系（新增的对应关系中，从来源批次出库的数量为来源批次所有）
                invMatTranLog.setTranQty( new BigDecimal(canOutQty.subtract( subtrQty ).toString()) );
                updateImtlList.add( invMatTranLog );
                // 一个批次数量不够当前出库的情况将可出库数量为0，并获取当前批次的非税价格
                //noTaxPrice = noTaxPrice.add( canOutQty.subtract( subtrQty ).multiply(imtrIn.getNoTaxPrice() ) );
                //price = price.add( canOutQty.subtract( subtrQty ).multiply( imtrIn.getPrice() ) );
            	outSuccess = true;
                break;
            }
            // 若循环能够继续，则需要将减法数量取绝对值 在遍历第二次时使用
            subtrQty = subtrQty.abs();
        }
        
        // 使用加权平均算法算出出库批次的不含税单价、含税单价
        //imtr.setPrice( price.divide( outQty , 6 , BigDecimal.ROUND_DOWN ) );
        //imtr.setNoTaxPrice( noTaxPrice.divide( outQty , 6 , BigDecimal.ROUND_DOWN ) );
        //imtr.setTax( imtr.getPrice().subtract( imtr.getNoTaxPrice() ) );
        
        //设置返回值
        //reMap.put( "price" , imtr.getPrice() );
        //reMap.put( "noTaxPrice" , imtr.getNoTaxPrice() );
        //reMap.put( "tax" , imtr.getTax() );
   
        //循环可出库批次后如果没有出完，则返回异常信息
        if( outSuccess ){
            reMap.put( "updateImtrList" , updateImtrList );
            reMap.put( "updateImtlList" , updateImtlList );     
            reMap.put("result", "success");
            reMap.put("msg", "Actual quantity is enough");
        }
        else{
            reMap.put("result", "error");
            reMap.put("msg", "Actual quantity is not enough");
        }
        return reMap;
    }

    
    
    /**
     * 查询当前站点下的物资条目
     */
    @Override
    public List< InvItemBaseField > queryInvItemBaseField4AllItem ( String siteId, String itemCodes ) {
        return invRealTimeDataDao.queryInvItemBaseField4AllItem( siteId, itemCodes );
    }

    /**
     *
     */
    @Override
    public void caluSiteInvData ( String siteId, String itemCodes ) {
        LOG.info( ">>>>>>>>>>>>>>>>>>> 开始执行caluSiteInvData... " );
        long startTime = System.currentTimeMillis();// 执行开始时间

        //物资范围，若为空，则查询站点下所有物资
        if(itemCodes.length()!=0){
            String[] itemCodeArr = itemCodes.split(",");
            StringBuilder temp = new StringBuilder("");
            for (int i = 0; i < itemCodeArr.length; i++) {
            	temp.append("'").append(itemCodeArr[i]).append("',");
            }
            itemCodes = temp.toString().substring(0, temp.toString().length() - 1);
        }
 
        // 采用全部字段插入/更新方式
        String caluTypeVal = "All";
        // 查询指定站点下所有物资基础信息
        List< InvItemBaseField > caluItemEntry = queryInvItemBaseField4AllItem( siteId, itemCodes);
        // 执行计算
        realTimeCaluByType( caluItemEntry , caluTypeVal );

        long endTime = System.currentTimeMillis();// 执行结束时间
        LOG.info( ">>>>>>>>>>>>>>  完成执行caluSiteInvData方法，用时：" + String.valueOf( ( endTime - startTime ) / 1000 ) + " s" );
    }
    
    /**
    *
    */
   	public void insertRecAndMap ( InvMatTranRec imtr, InvMatMap imm) {
	   // 插入物资信息数据到交易明细新表中
       if(imtr!=null){
           invMatTranRecDao.insertInvMatTranRec(imtr);  
       }
	   // 添加一条记录到映射表中
       if(imm!=null){
    	   invMatMapDao.insertInvMatMap( imm );
       }
    }
}
