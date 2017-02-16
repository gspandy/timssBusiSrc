package com.timss.inventory.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.inventory.bean.InvWarehouseItem;
import com.timss.inventory.service.InvWarehouseItemService;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseItemController.java
 * @author: 890166
 * @createDate: 2014-7-17
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invwarehouseitem")
public class InvWarehouseItemController {

    /**
     * service 注入
     */
    @Autowired
    private InvWarehouseItemService invWarehouseItemService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 保存到仓库
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-17
     * @param formData
     * @param listData
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveInvWarehouseItem", method = RequestMethod.POST)
    public Map<String, Object> saveInvWarehouseItem(String formData, String itemId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean flag = true;

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        InvWarehouseItemVO iwiv = JsonHelper.fromJsonStringToBean( formData, InvWarehouseItemVO.class );

        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        paramMap.put( "item", itemId );

        flag = invWarehouseItemService.saveInvWarehouseItem( userInfo, iwiv, paramMap );

        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    @RequestMapping(value = "/queryInvWarehouseItem", method = RequestMethod.POST)
    public Page<InvWarehouseItemVO> queryInvWarehouseItem(String itemId) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        UserInfoScope scope = userInfo;
        Page<InvWarehouseItemVO> page = scope.getPage();

        if ( null != itemId && !"".equals( itemId ) ) {
            List<InvWarehouseItemVO> iwiList = invWarehouseItemService.queryInvWarehouseItem( userInfo, itemId );
            page.setResults( iwiList );
        }

        return page;
    }
    
    /**
     * 批量保存物资安全库存
     * @description:
     * @author: 890151
     * @createDate: 2016-9-22
     * @param listData 
     * @return
     * @throws Exception :
     */    
    @RequestMapping(value = "/batchUpdateSafeQty", method = RequestMethod.POST)
    public Map<String, Object> batchUpdateSafeQty( String listData ) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	List< InvWarehouseItem > invWarehouseItems = JsonHelper.toList( listData , InvWarehouseItem.class );
    	Map<String, Object> result = invWarehouseItemService.batchUpdateSafeQty(userInfoScope, invWarehouseItems);
        return result;
    }
}
