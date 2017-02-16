package com.timss.pms.service.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.timss.pms.service.ContractService;
import com.timss.pms.service.PrifixSequenceService;
import com.timss.pms.service.SequenceService;
import com.timss.pms.util.InitVoEnumUtil;
import com.yudean.itc.manager.support.ISequenceManager;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class SequeceServiceImpl implements SequenceService {
	
	private static  String contractSequenceId="PMS_CONTRACT_CODE";
	
	private static String projectSequenceType="PMS_PROJECT_CODE";

	@Autowired
	ISequenceManager iSequenceManager;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	PrifixSequenceService prifixSequenceService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	Logger LOGGER=Logger.getLogger(SequeceServiceImpl.class);
	@Override
	public String createContractSequenceService(String siteName,String type) {
// 由于同一个contractSequenceId下会出现多个不同前缀的独立序列，需要借用ProjectCode的编码方式，使用prefixsequence
//		String code=iSequenceManager.getSequenceId(contractSequenceId);
//		return code;
		//-------------------getNextSequenceVal没有加对象锁-----------------------------------------------
		String code = "";
		if(null==siteName&&null==type){
			code=iSequenceManager.getSequenceId(contractSequenceId);
		}else{
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String year = sdf.format(curDate);
			String codePrefix=produceContractCodePrefix(siteName,type,year);
			code = prifixSequenceService.getNextSequenceVal(codePrefix,contractSequenceId);
			try{
				String sn = code.split("-")[3].replace("-", "");
				code = codePrefix +String.format("%03d", Integer.parseInt(sn));
				//用户有可能在满足prifix正则的情况下 自定义输入编码，这样就在序列值和录入值之间留下一个区间。当序列值增长到用户输入值的时候，就会出现自动生成的编码已存在的bug。
				while(contractService.checkContractCodeExisted(code)&&Integer.parseInt(sn)<1000){
					prifixSequenceService.increaseSequence(codePrefix, contractSequenceId);
					code = prifixSequenceService.getNextSequenceVal(codePrefix,contractSequenceId);
					sn = code.split("-")[3].replace("-", "");
					code = codePrefix +String.format("%03d", Integer.parseInt(sn));
				}
			}catch(Exception e){
				LOGGER.warn("格式化字符串异常",e);
				code = null;
			}
		}
		return code;
	}
	private String produceContractCodePrefix(String siteName,String type, String year) {
		return siteName+"-"+type+"-"+year+"-";
	}

	@Override
	public String createProjectSequenceService(String type,String year) {
		String codePrifix=produceProjectCodePrifx(type,year);
		String code=prifixSequenceService.getNextSequenceVal(codePrifix, projectSequenceType);
		return code;
	}

	private String produceProjectCodePrifx(String type, String year) {
		
		return year+"-"+type+"-";
	}

	@Override
	public void updateContractSequence(String contractCode) {
		//合同流水号增加,如何合同编号等于数据库的编号
//		if(iSequenceManager.getSequenceId(contractSequenceId).equals(contractCode)){
//			iSequenceManager.getGeneratedId(contractSequenceId);
//		}
		//---------------------当下改用与projectCode类似的prifixSequence------------------------------
		Pattern contractCodePatten=Pattern.compile("[A-Z]+\\-[A-Z]+\\-[0-9]{4}\\-[0-9]+");
		Matcher isMatcher=contractCodePatten.matcher(contractCode);
		if(isMatcher.matches()){
			String []splits=contractCode.split("-");
			String contractCategory=splits[1];
			if(InitVoEnumUtil.isCodeInEnum(contractCategory, "PMS_CONTRACT_CATEGORY", itcMvcService)){
				String prefixCode=produceContractCodePrefix(splits[0],contractCategory ,splits[2]);
				//如果生成的编码与规则生成的一致，直接使用生成的编码
				String code = prifixSequenceService.getNextSequenceVal(prefixCode,contractSequenceId);
				String sn = code.split("-")[3].replace("-", "");
				code = prefixCode +String.format("%03d", Integer.parseInt(sn));
				if(contractCode.equals(code)){
					prifixSequenceService.increaseSequence(prefixCode, contractSequenceId);
				}
			}
		}else{
			if(iSequenceManager.getSequenceId(contractSequenceId).equals(contractCode)){
				iSequenceManager.getGeneratedId(contractSequenceId);
			}
		}
	}

	@Override
	public void updateProjectSequece(String projectCode) {
		Pattern projectCodePatten=Pattern.compile("^[0-9]{4,4}\\-[A-Z]+\\-[0-9]+$");
		Matcher isMatcher=projectCodePatten.matcher(projectCode);
		//编号符合【四位数字+”-“+多个字母+”-“+多个数字】，同时枚举变量PMS_PCODE_TYPE中找到多个字母对应的枚举值，同时流水号对应值等于当前编码则更新编号
		if(isMatcher.matches()){
			String []splits=projectCode.split("-");
			String projectType=splits[1];
			if(InitVoEnumUtil.isCodeInEnum(projectType, "PMS_PCODE_TYPE", itcMvcService)){
				String prifixCode=produceProjectCodePrifx(projectType, splits[0]);
				if(projectCode.equals(prifixSequenceService.getNextSequenceVal(prifixCode, projectSequenceType))){
					prifixSequenceService.increaseSequence(prifixCode, projectSequenceType);
				}

			}
		}
		
	}

}
