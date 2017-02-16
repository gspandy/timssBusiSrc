package com.timss.inventory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.bean.InvItem;
import com.timss.inventory.vo.InvCategoryParam;
import com.timss.inventory.vo.InvCategoryVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvCategroyService.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvCategroyService {

    /**
     * 查询指定节点下的所有物资类型
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @param map
     * @return
     * @throws Exception :
     */
    List<HashMap<String, Object>> queryAllCategroyNodeById(Map<String, Object> map) throws Exception;

    /**
     * 根据name找CategroyId
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-12
     * @param userInfoScope
     * @param search
     * @return
     * @throws Exception :
     */
    List<String> queryCategroyIdByName(UserInfoScope userInfoScope, String name) throws Exception;
    
    /**
     * 查询物资分类下是否存在启用的主项目
     * 
     * @description:
     * @author: 890199
     * @createDate: 2016-8-22
     * @param invcateid
     * @param 
     * @return
     */
    List<InvItem> queryItemByInvcateid(String invcateid);
    
    /**
     * 查询物资分类表单
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param userInfoScope
     * @param cateId
     * @return
     * @throws Exception :
     */
    List<InvCategoryVO> queryInvCategroyDetail(UserInfoScope userInfoScope, String cateId) throws Exception;

    /**
     * @description: 通过父节点找到分类详细信息
     * @author: 890166
     * @createDate: 2014-7-26
     * @param userInfoScope
     * @param parentId
     * @return
     * @throws Exception :
     */
    List<InvCategoryVO> queryInvCategroyDetailByParentId(UserInfoScope userInfoScope, String parentId) throws Exception;

    /**
     * 更新物资分类名称
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param categoryId
     * @param categoryName
     * @return
     * @throws Exception :
     */
    int updateCategroyName(String categoryId, String categoryName) throws Exception;

    /**
     * 通过id删除物资分类
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param categoryId
     * @return
     * @throws Exception :
     */
    boolean deleteCategroyById(String categoryId) throws Exception;

    /**
     * 保存物资分类数据
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param userInfo
     * @param ic
     * @param paramMap
     * @return
     * @throws Exception :
     */
    boolean saveInvCategroy(UserInfoScope userInfo, InvCategory ic, Map<String, Object> paramMap) throws Exception;

    /**
     * @description:查询第一节点下的所有物资分类
     * @author: 890166
     * @createDate: 2014-8-5
     * @param userInfo
     * @param categoryId
     * @return
     * @throws Exception :
     */
    Page<InvCategoryVO> queryCategroyLevelOne(UserInfoScope userInfo, String categoryId) throws Exception;

    /**
     * @description: 接口提供查询物资类型
     * @author: yuanzh
     * @createDate: 2016-1-8
     * @param icParam 传入参数
     * @return:
     */
    List<InvCategory> queryCategroy(InvCategoryParam icParam);

}
