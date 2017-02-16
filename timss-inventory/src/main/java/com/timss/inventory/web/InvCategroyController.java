package com.timss.inventory.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.bean.InvItem;
import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvCategroyService;
import com.timss.inventory.service.InvWarehouseService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvCategoryVO;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvCategroyController.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invcategroy")
public class InvCategroyController {

    /**
     * service 注入
     */
    @Autowired
    private InvCategroyService invCategroyService;

    @Autowired
    private InvWarehouseService invWarehouseService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 页面跳转
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invcategroyForm", method = RequestMethod.GET)
    public String invcategroyForm() {
        return "/invcategroy/invcategroyForm.jsp";
    }

    /**
     * @description:物资类型树形菜单跳转
     * @author: 890166
     * @createDate: 2014-8-2
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invCategroyTreeList", method = RequestMethod.GET)
    public String invCategroyTreeList() {
        return "/invcategroy/invCategroyTreeList.jsp";
    }

    /**
     * 查询物资分类表单
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param cateId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvCategroyDetail", method = RequestMethod.POST)
    public InvCategoryVO queryInvCategroyDetail(String cateId, String parentId) throws Exception {
        InvCategoryVO ic = new InvCategoryVO();
        InvWarehouse iw = null;
        String warehouseId = null;
        List<InvWarehouse> iwList = null;
        boolean flag = false;
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        if ( !"".equals( cateId ) ) {
            List<InvCategoryVO> icList = invCategroyService.queryInvCategroyDetail( userInfoScope, cateId );
            if ( null != icList && !icList.isEmpty() ) {
                ic = icList.get( 0 );
            }

            iwList = invWarehouseService.queryWarehouseByCategoryId( userInfoScope, cateId );
            if ( null != iwList && !iwList.isEmpty() ) {
                iw = iwList.get( 0 );
                ic.setWarehouseid( iw.getWarehouseid() );
                ic.setWarehousename( iw.getWarehousename() );
            }
        } else {
            List<InvCategoryVO> icList = invCategroyService.queryInvCategroyDetailByParentId( userInfoScope, parentId );
            if ( null != icList && !icList.isEmpty() ) {
                ic = icList.get( 0 );
                flag = true;
            } else {
                icList = invCategroyService.queryInvCategroyDetail( userInfoScope, parentId );
                if ( null != icList && !icList.isEmpty() ) {
                    ic = icList.get( 0 );
                    flag = true;
                }
            }
            if ( flag ) {
                warehouseId = ic.getWarehouseid();
            } else {
                warehouseId = parentId;
            }

            iwList = invWarehouseService.queryWarehouseById( warehouseId );
            if ( null != iwList && !iwList.isEmpty() ) {
                iw = iwList.get( 0 );
                ic = new InvCategoryVO();
                ic.setParentid( parentId );
                ic.setStatus( "ACTIVE" );
                ic.setWarehouseid( iw.getWarehouseid() );
                ic.setWarehousename( iw.getWarehousename() );
            }

        }
        return ic;
    }

    /**
     * 更新物资分类名称
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param formData
     * @param listData
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteCategroy", method = RequestMethod.POST)
    public Map<String, Object> deleteCategroy(String categoryId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean flag = invCategroyService.deleteCategroyById( categoryId );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param categoryId
     * @param categoryName
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/updateCategroyName", method = RequestMethod.POST)
    public Map<String, Object> updateCategroyName(String categoryId, String categoryName) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int count = invCategroyService.updateCategroyName( categoryId, categoryName );
        if ( count > 0 ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * 保存物资分类数据
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-21
     * @param formData
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveInvCategroy", method = RequestMethod.POST)
    public Map<String, Object> saveInvCategroy(String formData) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvCategoryVO icv = JsonHelper.fromJsonStringToBean( formData, InvCategoryVO.class );

        if ( "".equals( icv.getWarehouseid() ) ) {
            icv.setWarehouseid( icv.getParentid() );
        }

        InvCategory ic = CommonUtil.conventInvCategoryVOToBean( icv );
        boolean flag = invCategroyService.saveInvCategroy( userInfo, ic, paramMap );
        if ( flag ) {
            result.put( "id", ic.getInvcateid() );
            result.put( "text", ic.getInvcatename() );
            result.put( "parentId", ic.getParentid() );
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description: 查询第一层物资类型
     * @author: 890166
     * @createDate: 2014-8-2
     * @param cateId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryCategroyLevelOne", method = RequestMethod.POST)
    public Page<InvCategoryVO> queryCategroyLevelOne(String cateId) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String categoryId = cateId == null ? "" : cateId;
        Page<InvCategoryVO> page = invCategroyService.queryCategroyLevelOne( userInfo, categoryId );
        return page;
    }
    
    /**
     * @description: 查询物资分类下是否存在启用的主项目
     * @author: 890199
     * @createDate: 2016-8-23
     * @param invcateid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value="/checkItemByInvcateid", method = RequestMethod.POST)
    public Map<String, String> queryItemByInvcateid() throws Exception{
    	UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
    	Map<String, String> mav = new HashMap<String, String>();
		String formData = userInfoScope.getParam("formData");
		InvCategory invc = JsonHelper.fromJsonStringToBean(formData, InvCategory.class);
		String invcateid = invc.getInvcateid();
		List<InvItem> itemList = invCategroyService.queryItemByInvcateid(invcateid);
		if(itemList.size() != 0){
			String itemCode = itemList.get(0).getItemcode();
			mav.put( "itemCode", itemCode );
			mav.put("result", "true");
		}else{
			mav.put("result", "false");
		}
    	return mav;
    }
}
