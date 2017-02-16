package com.timss.pms.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.pms.service.ContractService;
import com.timss.pms.service.SequenceService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.timss.pms.vo.ContractDtlVo;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping("pms/sequence")
public class SequenceController {
	
	@Autowired
	SequenceService sequenceService;
	@Autowired
        ContractService contractService;
	@RequestMapping("/getContractSequence")
	public ModelAndViewAjax getContractSequence(String siteName,String type){
		String code=sequenceService.createContractSequenceService(siteName,type);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", code);
		return ViewUtil.Json(map);
	}
	@RequestMapping("/getContractSequenceEdit")
        public ModelAndViewAjax getContractSequenceEdit(String siteName,String type,String id){
                //通过id知道Contract之前的合同编号
	        ContractDtlVo contract = contractService.queryContractById(id);
	        String type_original = contract.getContractCategory();
	        String code=contract.getContractCode();
	        if ( !type.equals( type_original ) ) {
	            //如果合同类型发生了变化，就用createContractSequenceService的方法
	            code=sequenceService.createContractSequenceService(siteName,type);
                }
                Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", code);
                return ViewUtil.Json(map);
        }
	
	@RequestMapping("/getProjectSequence")
	public ModelAndViewAjax getProjectSequence(String type,String year){
		String code=sequenceService.createProjectSequenceService(type,year);
		Map map=CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "", code);
		return ViewUtil.Json(map);
	}
	
}
