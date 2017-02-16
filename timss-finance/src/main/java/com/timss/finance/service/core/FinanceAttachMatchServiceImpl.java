package com.timss.finance.service.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.finance.bean.FinanceAttachMatch;
import com.timss.finance.dao.FinanceAttachMatchDao;
import com.timss.finance.service.FinanceAttachMatchService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;

@Service
public class FinanceAttachMatchServiceImpl implements FinanceAttachMatchService {

	
	@Autowired
	FinanceAttachMatchDao financeAttachMatchDao;
	
	@Autowired
	AttachmentMapper attachmentMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)//事物控制要加这个注解
	public void insertAttachmentToServer(String[] ids)  {
		//存于attchement中，避免2小时自动删除
		attachmentMapper.setAttachmentsBinded(ids, 1);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)//事物控制要加这个注解
	public void insertFinanceAttachMatch(String fid, String fieldids)  {
		if(StringUtil.isNotEmpty(fieldids) && fieldids!=null && fieldids !="") {
			String[] ids=fieldids.split(",");
			for(int i=0;i<ids.length;i++) {
				FinanceAttachMatch fam=new FinanceAttachMatch();
				fam.setFid(fid);
				fam.setAttachid(ids[i]);
				
				financeAttachMatchDao.insertFinanceAttachMatch(fam);
			}
			
			insertAttachmentToServer(ids);
		}
	}
	
	@Override
	public List<Map<String,Object>> queryFinanceAttachMatchByFid(String fid)
			 {
		List<FinanceAttachMatch> fmaList= financeAttachMatchDao.queryFinanceAttachMatchByFid(fid);
		ArrayList<String> ids = new ArrayList<String>();
		for(int i=0;i<fmaList.size();i++){
			FinanceAttachMatch fma=fmaList.get(i);
			System.out.println("########文件id==="+fma.getAttachid());
			ids.add(fma.getAttachid());
		}
		//ArrayList<HashMap<String,Object>> fileMap = (FileUploadUtil.getJsonFileList(Constant.basePath, ids);
		List<Map<String,Object>> fileMap = FileUploadUtil.getJsonFileList(Constant.basePath, ids);
//		JSONArray jsonArray = JSONArray.fromObject(fileMap);
		
		return fileMap;
	}

	@Override
	public void getUploadFileids()  {
	}

	@Override
	@Transactional
	public boolean updateFinanceAttachMatch(String fid, String attachIds) {
		deleteFinanceAttachMatch(fid);
		insertFinanceAttachMatch(fid, attachIds);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteFinanceAttachMatch(String fid) {
		//清除附件组件管理的附件
	    clearAttachmentNotUsed(fid);
		//清理业务与附件信息的绑定
	    financeAttachMatchDao.deleteFinanceAttachMatch(fid);
	    return true;
	}

	private void clearAttachmentNotUsed(String fid) {
		List<FinanceAttachMatch> fmaList= financeAttachMatchDao.queryFinanceAttachMatchByFid(fid);
		 String ids ="";
		 for(int i=0;i<fmaList.size();i++){
			 if(i!=0){
				 ids+=",";
			 }
			 ids+=fmaList.get(i).getAttachid();
		 }
		attachmentMapper.setAttachmentsBinded(ids.split(","), 0);
	}
}
