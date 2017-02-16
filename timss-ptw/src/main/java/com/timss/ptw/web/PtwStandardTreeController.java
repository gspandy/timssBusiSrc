package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.service.PtwStandardTreeService;
import com.timss.ptw.vo.PtwStdTreeVo;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * 
 * @title: 标准树
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardTree.java
 * @author: fengzt
 * @createDate: 2014年12月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("ptw/ptwStandardTree")
public class PtwStandardTreeController {

    @Autowired
    private PtwStandardTreeService ptwStandardTreeService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    /**
     * 
     * @description:标准树目录
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @return String
     * @throws RuntimeException
     * @throws Exception:
     */
    @RequestMapping(value = "/queryStdTreeMenu")
    public String queryStdTreeMenu() throws RuntimeException, Exception {
        return "/updatePtwStandard.jsp";
    }
    
    /**
     * 
     * @description:标准树目录
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @return String
     * @throws RuntimeException
     * @throws Exception:
     */
    @RequestMapping(value = "/queryDeviceTreePage")
    public ModelAndView queryDeviceTreePage() {
        return new ModelAndView("/tree.jsp");
    }
    
    /**
     * 
     * @description:标准树目录插入
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @return String
     * @throws RuntimeException
     * @throws Exception:
     */
    @RequestMapping(value = "/insertStdTreePage")
    public ModelAndView queryStdTreeMenu( String parentId, String parentName )  {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "parentId", parentId );
        map.put( "parentName", parentName );
        
        return new ModelAndView( "/insertPtwStandard.jsp", map );
    }
    
    /**
     * 
     * @description:同一个站点检查名字是否有重复
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param name
     * @return:
     */
    @RequestMapping(value = "/queryCheckStdTreeName")
    public @ResponseBody Boolean  queryCheckStdTreeName( String name )  {
        return false;
    }
    
    
    /**
     * 
     * @description:插入或者更新标准树
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param name
     * @return:Map
     */
    @RequestMapping(value = "/insertOrUpdateStandard", method=RequestMethod.POST)
    public @ResponseBody Map<String, Object>  insertOrUpdateStandard( String formData )  {
        PtwStdTreeVo ptwStdTreeVo = JsonHelper.fromJsonStringToBean(formData, PtwStdTreeVo.class);
        
        Map<String, Object> map = ptwStandardTreeService.insertOrUpdateStandard( ptwStdTreeVo );
        
        int count = (Integer) map.get( "count" );
        
        if( count > 0 ){
            map.put( "result", "success" );
        }
        
        return map;
    }
    
    /**
     * @description:查询标准树
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/queryPtwStdTree")
    public ModelAndViewAjax queryPtwStdTree(String id) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        // 如果为空，则获得根节点的id
        int tId = 0;
        PtwStdTreeVo bean = new PtwStdTreeVo();
        if ( StringUtils.isBlank( id ) ) {
            PtwStdTreeVo rootBean = ptwStandardTreeService.queryPtwStdRootIdBySite();
            if( rootBean == null ){
                return null;
            }
            tId = rootBean.getId();
            bean = ptwStandardTreeService.queryStandardTreeById( tId );
        }else{
            tId = Integer.parseInt( id.trim() );
        }
        
        Map<String,Object> params = new HashMap<String, Object>();
        params.put( "parentId", tId );
        
        List<PtwStdTreeVo> ptwStdTreeVos = ptwStandardTreeService.queryPtwStdByParentId( params );
        if( bean.getId() > 0 ){
            if( ptwStdTreeVos != null && ptwStdTreeVos.size() > 0 ){
                bean.setIconCls( "tree-folder" );
            }else{
                bean.setIconCls( "tree-file" );
            }
            result.put( "parent", bean );
        }
        result.put( "children", ptwStdTreeVos );
       
        return itcMvcService.jsons( result );
    }
    
    /**
     * 
     * @description:名称/编码 查询--标准树
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param kw
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/searchPtwStdHint")
    public ModelAndViewAjax searchPtwStdHint(String kw) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if ( StringUtils.isNotBlank( kw ) ) {
            // 可性能优化，前台只展示前十条，可考虑只查询10条
            result = ptwStandardTreeService.queryPtwStdSearch( kw );
        }
        return itcMvcService.jsons( result );
    }
    
    /**
     * 
     * @description:用于搜索框的选中
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:
     */
    @RequestMapping(value = "/searchHintPtwStdParentIds")
    public ModelAndViewAjax searchHintPtwStdParentIds( int id ) {
        List<String> result=new ArrayList<String>();
        if( id > 0 ){
                result = ptwStandardTreeService.searchHintPtwStdParentIds( id );
        }
        return itcMvcService.jsons(result);       
    }
    
    /**
     * 
     * @description:拖拽标准树节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:
     * @throws Exception 
     */
    @RequestMapping(value = "/updateDropHwlTreeNode")
    public @ResponseBody Map<String, Object> updateDropStdTreeNode( int id, int parentId ) throws Exception {
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( id > 0 && parentId >= 0  ){
            PtwStdTreeVo bean = ptwStandardTreeService.queryStandardTreeById( id );
           
            if( bean.getParentId() == 0 ){
                dataMap.put( "result", "forbidMoveRoot" );
            }else {
                int updateCount = ptwStandardTreeService.updateDropStdTreeNode( id, parentId );
                if( updateCount > 0 ){
                    dataMap.put( "result", "ok" );
                }
            }
            
        }
        
        return dataMap;
    }
    
    /**
     * 
     * @description:通过ID查询bean
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @return:Map
     */
    @RequestMapping(value = "/queryStandardTreeById")
    public @ResponseBody Map<String, Object> queryStandardTreeById( int id ){
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( id > 0 ){
            PtwStdTreeVo ptwStdTreeVo = ptwStandardTreeService.queryStandardTreeById( id );
            if( ptwStdTreeVo != null ){
                map.put( "ptwStdTreeVo", ptwStdTreeVo );
                map.put( "result", "success" );
            }
        }else{
            map.put( "reuslt", "fail" );
        }
        
        return map;
    }
    
    /**
     * 
     * @description:通过查询根节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @return:Map
     */
    @RequestMapping(value = "/queryPtwStdRootIdBySite")
    public @ResponseBody Map<String, Object> queryPtwStdRootIdBySite( ){
        Map<String, Object> map = new HashMap<String, Object>();
        
        PtwStdTreeVo rootBean = ptwStandardTreeService.queryPtwStdRootIdBySite();
        map.put( "ptwStdTreeVo", rootBean );
        map.put( "result", "success" );
        
        return map;
    }
    
    /**
     * 
     * @description:通过查询根节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @return:Map
     */
    @RequestMapping(value = "/deleteStandardTree")
    public @ResponseBody Map<String, Object> deleteStandardTree( int id ){
        Map<String, Object> map = new HashMap<String, Object>();
        
        int count = ptwStandardTreeService.deleteStandardTree( id );
        
        String result = count > 0 ? "success" : "fail";
        map.put( "result", result );
        
        return map;
    }
    
}
