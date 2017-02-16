package com.timss.asset.service.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.timss.asset.bean.AstOrganizeData;
import com.timss.asset.dao.AstOrganizeDataDao;
import com.timss.asset.service.AstOrganizeDataService;
import com.timss.asset.util.ImportUtil;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AstOrganizeDataServiceImpl.java
 * @author: 890166
 * @createDate: 2015-5-29
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("AstOrganizeDataServiceImpl")
public class AstOrganizeDataServiceImpl implements AstOrganizeDataService {

    private static final Logger LOG = Logger.getLogger( AstOrganizeDataServiceImpl.class );

    @Autowired
    private AstOrganizeDataDao astOrganizeDataDao;

    /**
     * @description: 插入AstOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    @Override
    public int insertAstOrganizeData(AstOrganizeData aod) {
        return astOrganizeDataDao.insertAstOrganizeData( aod );
    }

    /**
     * @description: 删除AstOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    @Override
    public int deleteAstOrganizeData(AstOrganizeData aod) {
        return astOrganizeDataDao.deleteAstOrganizeData( aod );
    }

    /**
     * @description: 查询AstOrganizeData信息
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    @Override
    public Page<AstOrganizeData> queryAstOrganizeData(UserInfoScope userInfo, AstOrganizeData aod) {
        UserInfoScope scope = userInfo;
        Page<AstOrganizeData> page = scope.getPage();
        page.setParameter( "siteid", userInfo.getSiteId() );

        if ( null != aod ) {
            page.setParameter( "parentid", aod.getParentid() );
            page.setParameter( "assetid", aod.getAssetid() );
            page.setParameter( "assetname", aod.getAssetname() );
            page.setParameter( "spec", aod.getSpec() );
            page.setParameter( "description", aod.getDescription() );
            page.setParameter( "modeldesc", aod.getModeldesc() );
            page.setParameter( "manufacturer", aod.getManufacturer() );
            page.setParameter( "producedate", aod.getProducedate() );
            page.setParameter( "isroot", aod.getIsroot() );
            page.setParameter( "cumodel", aod.getCumodel() );
            page.setParameter( "remark", aod.getRemark() );
            page.setParameter( "status", aod.getStatus() );
        }
        List<AstOrganizeData> ret = astOrganizeDataDao.queryAstOrganizeData( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description: 触发调用oracle的存储过程整理数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param paramMap
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callAstOrganizeDataInit(Map<String, Object> paramMap) {
        astOrganizeDataDao.callAstOrganizeDataInit( paramMap );
        return Boolean.valueOf( String.valueOf( paramMap.get( "flag" ) ) );
    }

    /**
     * @description: 上传excel方法
     * @author: 890166
     * @createDate: 2015-11-11
     * @param excelFiles:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void uploadExcel(MultipartFile[] excelFiles, UserInfoScope userInfo) throws Exception {
        LOG.info( "========================================" );
        LOG.info( "开始上传Excel文件" );
        for ( MultipartFile excelFile : excelFiles ) {
            if ( excelFile.isEmpty() ) {
                LOG.info( "文件未上传" );
            } else {
                LOG.info( "文件长度: " + excelFile.getSize() );
                LOG.info( "文件类型: " + excelFile.getContentType() );
                LOG.info( "文件名称: " + excelFile.getName() );
                LOG.info( "文件原名: " + excelFile.getOriginalFilename() );
                LOG.info( "========================================" );

                try {
                    InputStream is = excelFile.getInputStream();
                    // Excel转换成List
                    List<AstOrganizeData> aodList = ImportUtil.readAndChange2List( is );
                    // 若List是空的话，返回一个false
                    if ( !aodList.isEmpty() ) {
                        // 查询当前指定表中是否存在该站点数据
                        List<AstOrganizeData> oldList = queryAstOrganizeData( userInfo, null ).getResults();
                        if ( !oldList.isEmpty() ) {
                            AstOrganizeData aod = new AstOrganizeData();
                            aod.setSiteid( userInfo.getSiteId() );
                            deleteAstOrganizeData( aod );
                        }
                        // 若List中存在数据，则遍历它并保存数据
                        for ( AstOrganizeData aod : aodList ) {
                            aod.setSiteid( userInfo.getSiteId() );
                            insertAstOrganizeData( aod );
                        }
                    }
                } catch (IOException e) {
                    throw new Exception( e );
                }
            }
        }

    }
}
