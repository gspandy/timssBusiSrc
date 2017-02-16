package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.bean.InvItem;
import com.timss.inventory.vo.InvCategoryParam;
import com.timss.inventory.vo.InvCategoryVO;
import com.timss.inventory.vo.TreeBean;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvCategroy.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvCategroyDao {

    /**
     * @description:查询指定节点下的所有物资类型
     * @author: 890166
     * @createDate: 2014-7-11
     * @param map
     * @return
     * @throws Exception:
     */
    List<TreeBean> queryCategroyNodeById(Map<String, Object> map);

    /**
     * @description:根据name找CategroyId
     * @author: 890166
     * @createDate: 2014-7-12
     * @param userInfoScope
     * @param search
     * @return
     * @throws Exception:
     */
    List<String> queryCategroyIdByName(Map<String, Object> map);
    
    /**
     * @description:查询物资分类下是否存在启用的主项目
     * @author: 890199
     * @createDate: 2016-8-22
     * @return: 
     * @throws Exception:
     */
    List<InvItem> queryItemByInvcateid(String invcateid);
    
    /**
     * @description:查询物资分类表单
     * @author: 890166
     * @createDate: 2014-7-21
     * @param map
     * @return:
     */
    List<InvCategoryVO> queryInvCategroyDetail(Map<String, Object> map);

    /**
     * @description: 通过父节点找到分类详细信息
     * @author: 890166
     * @createDate: 2014-7-26
     * @param userInfoScope
     * @param parentId
     * @return
     * @throws Exception:
     */
    List<InvCategoryVO> queryInvCategroyDetailByParentId(Map<String, Object> map);

    /**
     * @description:查询物资分类的第一层节点
     * @author: 890166
     * @createDate: 2014-8-12
     * @param page
     * @return:
     */
    List<InvCategoryVO> queryCategroyLevelOne(Page<?> page);

    /**
     * @description:通过id删除物资分类
     * @author: 890166
     * @createDate: 2014-7-21
     * @param categoryId
     * @return:
     */
    int deleteCategroyById(String categoryId);

    /**
     * @description:保存物资分类数据
     * @author: 890166
     * @createDate: 2014-7-21
     * @param ic
     * @return:
     */
    int insertInvCategroy(InvCategory ic);

    /**
     * @description:更新物资分类数据
     * @author: 890166
     * @createDate: 2014-7-21
     * @param ic
     * @return:
     */
    int updateInvCategroy(InvCategory ic);

    /**
     * @description: 接口提供查询物资类型
     * @author: yuanzh
     * @createDate: 2016-1-8
     * @param icParam 传入参数
     * @return:
     */
    List<InvCategory> queryCategroy(InvCategoryParam icParam);
}
