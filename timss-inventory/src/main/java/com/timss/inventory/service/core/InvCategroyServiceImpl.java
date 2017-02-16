package com.timss.inventory.service.core;

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

import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.bean.InvItem;
import com.timss.inventory.dao.InvCategroyDao;
import com.timss.inventory.dao.InvWarehouseItemDao;
import com.timss.inventory.service.InvCategroyService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvCategoryParam;
import com.timss.inventory.vo.InvCategoryVO;
import com.timss.inventory.vo.TreeBean;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvCategroyServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvCategroyServiceImpl")
public class InvCategroyServiceImpl implements InvCategroyService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvCategroyDao invCategroyDao;

    @Autowired
    private InvWarehouseItemDao invWarehouseItemDao;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvCategroyServiceImpl.class );

    /**
     * @description:查询指定节点下的所有物资类型
     * @author: 890166
     * @createDate: 2014-7-11
     * @param map
     * @return
     * @throws Exception:
     */
    @Override
    public List<HashMap<String, Object>> queryAllCategroyNodeById(Map<String, Object> map) throws Exception {
        List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
        String categoryname = String.valueOf( map.get( "categoryname" ) );
        List<TreeBean> tbList = invCategroyDao.queryCategroyNodeById( map );
        boolean judgeFlag = false;

        if ( null != tbList && !tbList.isEmpty() ) {
            String siteId = String.valueOf( map.get( "siteId" ) );
            String matSwitch = CommonUtil.getProperties( "matSwitch" );
            if ( matSwitch.indexOf( siteId ) > -1 ) {
                judgeFlag = true;
            }
            for ( TreeBean tb : tbList ) {

                if ( !"".equals( categoryname ) && judgeFlag ) {
                    if ( tb.getText().indexOf( categoryname ) == -1 ) {
                        continue;
                    }
                }

                HashMap<String, Object> pNode = new HashMap<String, Object>();
                pNode.put( "id", tb.getId() );
                pNode.put( "text", tb.getText() );
                pNode.put( "state", "closed" );
                pNode.put( "type", tb.getType() );
                // 加入parentid条件进行查询
                map.put( "parentid", tb.getId() );
                map.remove( "categoryid" );
                List<TreeBean> tbMaterialList = invCategroyDao.queryCategroyNodeById( map );
                // 查找物资分类信息
                if ( null != tbMaterialList && !tbMaterialList.isEmpty() ) {
                    List<HashMap<String, Object>> retInside = new ArrayList<HashMap<String, Object>>();
                    for ( TreeBean tbMaterial : tbMaterialList ) {
                        HashMap<String, Object> mNode = new HashMap<String, Object>();
                        mNode.put( "id", tbMaterial.getId() );
                        mNode.put( "text", tbMaterial.getText() );
                        mNode.put( "state", "open" );
                        mNode.put( "type", tbMaterial.getType() );
                        mNode.put( "children", "" );
                        retInside.add( mNode );
                    }
                    pNode.put( "children", retInside );
                } else {
                    pNode.put( "state", "open" );
                    pNode.put( "children", "" );
                }
                ret.add( pNode );
            }
        }
        return ret;
    }

    /**
     * @description:根据name找CategroyId
     * @author: 890166
     * @createDate: 2014-7-12
     * @param userInfoScope
     * @param search
     * @return
     * @throws Exception:
     */
    @Override
    public List<String> queryCategroyIdByName(@Operator UserInfoScope userInfoScope, @LogParam("name") String name)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfoScope.getSiteId() );
        map.put( "invcatename", name );
        return invCategroyDao.queryCategroyIdByName( map );
    }
    
    /**
     * 
     * @description:查询物资分类下是否存在启用的主项目
     * @author: 890199
     * @createDate: 2016-8-22
     * @param invcateid
     * @return: 
     */
    @Override
    public List<InvItem> queryItemByInvcateid(String invcateid){
    	return invCategroyDao.queryItemByInvcateid(invcateid);
    }

    /**
     * @description:更新物资分类名称
     * @author: 890166
     * @createDate: 2014-7-21
     * @param categoryId
     * @param categoryName
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateCategroyName(String categoryId, String categoryName) throws Exception {
        InvCategory ic = new InvCategory();
        ic.setInvcateid( categoryId );
        ic.setInvcatename( categoryName );
        return invCategroyDao.updateInvCategroy( ic );
    }

    /**
     * @description:通过id删除物资分类
     * @author: 890166
     * @createDate: 2014-7-21
     * @param categoryId
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteCategroyById(String categoryId) throws Exception {
        int counter = invWarehouseItemDao.queryInvWarehouseItemCounter( categoryId );
        if ( counter > 0 ) {
            return false;
        } else {
            invCategroyDao.deleteCategroyById( categoryId );
            return true;
        }
    }

    /**
     * @description:查询物资分类表单
     * @author: 890166
     * @createDate: 2014-7-21
     * @param userInfoScope
     * @param cateId
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvCategoryVO> queryInvCategroyDetail(@Operator UserInfoScope userInfoScope,
            @LogParam("cateId") String cateId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfoScope.getSiteId() );
        map.put( "cateId", cateId );
        return invCategroyDao.queryInvCategroyDetail( map );
    }

    /**
     * @description:保存物资分类数据
     * @author: 890166
     * @createDate: 2014-7-21
     * @param userInfo
     * @param ic
     * @param paramMap
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveInvCategroy(@Operator UserInfoScope userInfo, @LogParam("ic") InvCategory ic,
            Map<String, Object> paramMap) throws Exception {
        boolean flag = true;
        int count = 0;
        try {
            if ( null == ic.getInvcateid() ) {
                ic.setCreatedate( new Date() );
                ic.setCreateuser( userInfo.getUserId() );
                ic.setSiteId( userInfo.getSiteId() );
                count = invCategroyDao.insertInvCategroy( ic );
            } else {
                ic.setModifydate( new Date() );
                ic.setModifyuser( userInfo.getUserId() );
                count = invCategroyDao.updateInvCategroy( ic );
            }
            if ( count <= 0 ) {
                flag = false;
            }
        } catch (Exception e) {
            LOG.info( "---------InvCategroyServiceImpl 中的 saveInvCategroy 方法抛出异常---------：" + e.getMessage() );
            flag = false;
        }
        return flag;
    }

    /**
     * @description: 通过父节点找到分类详细信息
     * @author: 890166
     * @createDate: 2014-7-26
     * @param userInfoScope
     * @param parentId
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvCategoryVO> queryInvCategroyDetailByParentId(@Operator UserInfoScope userInfoScope,
            @LogParam("parentId") String parentId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfoScope.getSiteId() );
        map.put( "parentid", parentId );
        return invCategroyDao.queryInvCategroyDetailByParentId( map );
    }

    /**
     * @description:查询第一节点下的所有物资分类
     * @author: 890166
     * @createDate: 2014-8-5
     * @param userInfo
     * @param categoryId
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvCategoryVO> queryCategroyLevelOne(@Operator UserInfoScope userInfo,
            @LogParam("categoryId") String categoryId) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvCategoryVO> page = scope.getPage();

        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "parentid", categoryId );
        List<InvCategoryVO> ret = invCategroyDao.queryCategroyLevelOne( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description: 接口提供查询物资类型
     * @author: yuanzh
     * @createDate: 2016-1-8
     * @param icParam 传入参数
     * @return:
     */
    @Override
    public List<InvCategory> queryCategroy(InvCategoryParam icParam) {
        return invCategroyDao.queryCategroy( icParam );
    }
}
