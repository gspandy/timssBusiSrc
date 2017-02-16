package com.timss.pms.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.support.Attachment;
import com.yudean.itc.manager.support.impl.AttachmentManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @ClassName:     AttachUtil
 * @company: gdyd
 * @Description: 附件信息处理帮助类
 * @author:    黄晓岚
 * @date:   2014-6-30 下午2:56:36
 */
public class AttachUtil {
	private static final Logger LOGGER=Logger.getLogger(AttachUtil.class);
	@Autowired
	static ItcMvcService itcMvcService;
	@Autowired
	static AttachmentManager attachmentManager;
	/**
	 * 
	 * @Title: generatAttach
	 * @Description: 根据attachId列表字符串，获得前端显示的附件信息
	 * @param attachList
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> generatAttach(String attachList){
		ArrayList<HashMap<String,Object>> map=null;
		if(attachList!=null && !attachList.equals("")){
			LOGGER.info("获取附件列表的详细信息，idlist:"+attachList);
			String []attachs=attachList.split(",");
			ArrayList<String> aList=new ArrayList<String>( Arrays.asList(attachs));
			List<Map<String, Object>> tmpMaps=FileUploadUtil.getJsonFileList(Constant.basePath, aList);
	                //添加排序算法
			Map<String, Object>[] tmpArray = new HashMap[tmpMaps.size()];
			tmpMaps.toArray( tmpArray );
			bubbleSort(tmpArray,0);
                        tmpMaps = Arrays.asList( tmpArray );
			map=new ArrayList<HashMap<String,Object>>();
			
			for (Map<String, Object> map2 : tmpMaps) {
				HashMap<String,Object> hashMap=(HashMap<String, Object>) map2;
				map.add(hashMap);
			}
			
		}
		return map;
	}
	public static void bubbleSort(Map arr[],int begin) {
            for ( int i = begin; i < arr.length; i++ ) {
                if ( Long.valueOf( arr[begin].get("fileID").toString() ) >Long.valueOf( arr[i].get("fileID").toString() )) {
                    Map<String, Object> temp=arr[begin];
                    arr[begin] = arr[i];
                    arr[i] = temp;
                }
            }
            if ( begin<arr.length-1 ) {
                begin++;
                bubbleSort( arr, begin );
            }
         }
	/**
	 * 修改后台附件状态，
	 * @Title: changeAttachStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param attachmentMapper 
	 * @param attach 附件id列表
	 * @param status 0表示删除，1表示不删除
	 */
	private static void changeAttachStatus(AttachmentMapper attachmentMapper,String attach,int status){
		if(attach!=null && !"".equals(attach)){
			LOGGER.info("设置附件的绑定状态，attach："+attach+",status:"+status);
			String []attachList=attach.split(",");
			attachmentMapper.setAttachmentsBinded(attachList, status);
		}
	}
	
	/**
	 * 对附件进行操作，删除oldAttach附件，保存newAttach附件
	 * @Title: bindAttach
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param attachmentMapper
	 * @param oldAttach
	 * @param newAttach
	 */
	public static void bindAttach(AttachmentMapper attachmentMapper,String oldAttach,String newAttach){
		if(oldAttach!=null && !oldAttach.equals("")){
			changeAttachStatus(attachmentMapper, oldAttach, 0);
		}
		if(newAttach!=null && !newAttach.equals("")){
			changeAttachStatus(attachmentMapper, newAttach, 1);
		}
	}
	
	public static List<RetAttachmentBean> changeAttachStringToEipAttach(AttachmentMapper attachmentMapper,String attach){
		List<RetAttachmentBean> retAttachmentBeans=new ArrayList<RetAttachmentBean>();
		if(attach!=null && !attach.equals("")){
			//解析attach为多个attachmentId
			String []attachs=attach.split(",");
			for(int i=0;i<attachs.length;i++){
				//获取附件的详细信息
				Attachment attachment=attachmentMapper.selectById(attachs[i]);
				String url="/upload?method=downloadFile&id="+attachs[i];
				url=getCurrentUrlPath()+url;
				ContextLoader.getCurrentWebApplicationContext();
				//初始化返回的附件信息
				RetAttachmentBean retAttachmentBean=new RetAttachmentBean();
				retAttachmentBean.setFileName(attachment.getOriginalFileName());
				
				retAttachmentBeans.add(retAttachmentBean);
				
			}
		}
		return retAttachmentBeans;
	}
	
	private static String getCurrentUrlPath(){
//		ServletContext servletContext=(ServletContext) itcMvcService.getServletContexts();
//		String server=servletContext.getContextPath();
//		return server;
		return "";
	}
}
