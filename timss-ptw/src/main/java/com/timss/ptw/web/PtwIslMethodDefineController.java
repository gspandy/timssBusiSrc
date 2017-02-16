package com.timss.ptw.web;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.bean.PtwIslMethodDefine;
import com.timss.ptw.service.PtwIslMethodDefineService;
import com.timss.ptw.vo.IsMethodPointVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "ptw/ptwIslMethDef")
public class PtwIslMethodDefineController {
    private static final Logger log = Logger.getLogger(PtwIslMethodDefineController.class);
    
    @Autowired
    private PtwIslMethodDefineService ptwIslMethodDefineService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @RequestMapping(value="/openIslMethDefListPage")
   	public String openIslMethDefListPage() throws Exception{
   		return "/IslMethodDefineList.jsp";
   	}
    
    /**
     * 
     * @description:隔离证弹出页-隔离方法jsp
     * @author: fengzt
     * @createDate: 2014年10月24日
     * @return
     * @throws Exception:
     */
    @RequestMapping(value="/openIslMethDefItemPage")
    public ModelAndView openIslMethDefItemPage(String embbed){
        Map<String , Object> map = new HashMap<String, Object>();
        map.put( "embbed", embbed );
        log.info( "embbed=" + embbed  );
        return new ModelAndView("ptwBaseConf/IslMethodDefineItem.jsp",map);
    }
    
    @RequestMapping(value="islMethDefListData")
	public Page<PtwIslMethodDefine> islMethDefListData() throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<PtwIslMethodDefine> page = userInfoScope.getPage();
        
        String fuzzySearchParams = userInfoScope.getParam("search");
        //HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "faultTypeMap", "workorder","WoFaultTypeDao");
        
        if(fuzzySearchParams!=null){
        	HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap( fuzzySearchParams );
          //  fuzzyParams = MapHelper.fromPropertyToColumnMap(fuzzyParams, propertyColumnMap);
        	page.setFuzzyParams(fuzzyParams);
        } 
        
        // 设置排序内容
		if (userInfoScope.getParamMap().containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");
	
			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			//sortKey = propertyColumnMap.get(sortKey);
	
			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("NO");
			page.setSortOrder("asc");
			page.setParameter("siteId", siteId);
		}
        
        page = ptwIslMethodDefineService.queryPtwIsLMethDefList(page);
        
        return page;
	}
    
    @RequestMapping(value="/selectIslMethDefListPage")
   	public String selectIslMethDefListPage() throws Exception{
   		return "/ptwBaseConf/SelectIslMethDefList.jsp";
   	}
    
    
    @RequestMapping(value="/openPtwIslMethDefPage")
	public String openPtwIslMethDefPage() throws Exception{
		return "/ptwBaseConf/IslMethodDefine.jsp";
	}
   
    @RequestMapping(value = "/queryPtwIslMethDefById")
    public @ResponseBody HashMap<String, Object> queryPtwTypeInfo() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int id = Integer.parseInt(userInfoScope.getParam( "id" ));
        
        PtwIslMethodDefine data = ptwIslMethodDefineService.queryPtwIsLMethDefByPtwId(id);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        result.put( "data",  JsonHelper.toJsonString(data));
        return result;
    }
    
    
    
    /**
     * @description:检查no是否唯一
     * @author: 王中华
     * @createDate: 2014-10-14
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/checkIslMethNo")
    @Transactional(propagation = Propagation.REQUIRED)
    public @ResponseBody Boolean checkIslMethNo() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String islMethDefData = userInfoScope.getParam( "islMethDefData" );
        JSONObject object = JSONObject.fromObject(islMethDefData);
        String idString = object.get("id").toString();
        String noString = object.get("no").toString().trim();
        boolean flag = false;
        if("".equals(idString)){
        	flag =  ptwIslMethodDefineService.checkIslMethNo(noString);
        }else{
        	if(!"".equals(noString)){
        		//查询当前方法的no与传过来的no比较
        		int id = Integer.valueOf(idString);
        		PtwIslMethodDefine obj = ptwIslMethodDefineService.queryPtwIsLMethDefByPtwId(id);
        		
        		if(noString.equals(obj.getNo())){  //不改变no的编辑
        			return true;
        		}else{  //改变了no 的编辑
        			flag =  ptwIslMethodDefineService.checkIslMethNo(noString);
        		}
        	}
        }
       
        return flag;
    }
    
    @RequestMapping(value = "/commitPtwIslMethDef")
    @Transactional(propagation = Propagation.REQUIRED)
    public @ResponseBody HashMap<String, Object> commitPtwIslMethDef() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String islMethDefFormDate = userInfoScope.getParam( "islMethDefFormDate" );
        
        HashMap<String,String> paramsDataMap = new HashMap<String, String>();
        paramsDataMap.put("islMethDefForm", islMethDefFormDate);
		
        PtwIslMethodDefine ptwIslMethodDefine = JsonHelper.fromJsonStringToBean(islMethDefFormDate, PtwIslMethodDefine.class);
        int id = ptwIslMethodDefine.getId();
        if(id == 0){  //新建
        	ptwIslMethodDefineService.insertPtwIsLMethDef(paramsDataMap);
        }else{  //修改
        	ptwIslMethodDefine.setModifydate(new Date());
        	ptwIslMethodDefine.setModifyuser(userId);
        	ptwIslMethodDefineService.updatePtwIsLMethDef(ptwIslMethodDefine);
        }
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        return result;
    }
    
    
    /**
     * @description:删除或禁用某条隔离方法
     * @author: 王中华
     * @createDate: 2014-10-15
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/unavailableIslMethDef")
    @Transactional(propagation = Propagation.REQUIRED)
    public @ResponseBody HashMap<String, Object> unavailableIslMethDef() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );
      
        ptwIslMethodDefineService.deletePtwIsLMethDefById(Integer.valueOf(id));
      
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "result", "success" );
        return result;
    }
    
    /**
     * 
     * @description:隔离证添加弹出框
     * @author: fengzt
     * @createDate: 2014年10月27日
     * @param rows
     * @param page
     * @param search
     * @param sort
     * @param order
     * @return:
     */
    @RequestMapping("/queryIsMethodItemList")
    public @ResponseBody Map<String, Object> queryIsMethodItemList( int rows, int page, String search,String sort, String order ){
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        List<IsMethodPointVo> isMethodPointVos = new ArrayList<IsMethodPointVo>();
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setParameter( "sort", sort );
            pageVo.setParameter( "order", order );
        }
        
      //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				map = (HashMap<String, Object>) MapHelper.jsonToHashMap(search);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
            isMethodPointVos = ptwIslMethodDefineService.queryIsMethodBySearch( map, pageVo );
            
        }else{
            //默认分页
            isMethodPointVos = ptwIslMethodDefineService.queryIsMethodBySiteId( pageVo );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", isMethodPointVos );
        if( ! isMethodPointVos.isEmpty() ){
            dataMap.put( "total",  pageVo.getTotalRecord() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
        
    }
    
}
