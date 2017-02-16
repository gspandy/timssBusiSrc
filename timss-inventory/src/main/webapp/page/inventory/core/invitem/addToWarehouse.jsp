<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String type = request.getAttribute("type") == null ? "":String.valueOf(request.getAttribute("type"));
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>添加到仓库表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" contect="no-cache" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var _dialogEmmbed = true;
	var warehouseData = "";
	var type = '<%=type%>';
	var warehouseInfo = "";
	var warehouseInfo2 = "";
	
	//处理仓库下拉缓存
	var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	var noform = [
	    	{title : "仓库", id : "warehouseid",type : "combobox",rules:{required:true},
	    		options:{
	    			firstItemEmpty:true,
	    			url:basePath+ "inventory/invitem/queryAllWarehouseBySiteId.do?prefix="+prefix,
	    			initOnChange:false,
	    			onChange: function(val){
	    				var prefix2 = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
	    				$("#f_defBinid").iCombo("init",{
	    					allowEmpty: true,
	    					allowSearch:true,
	    				    url : basePath+ "inventory/invitem/queryBinByWarehouseId.do?warehouseid="+val+"&prefix="+prefix2
	    				});
	    				$("#f_invcateid").iCombo("init",{
	    	    			firstItemEmpty:true,
	    				    url : basePath+ "inventory/invitem/queryCategoryByWarehouseId.do?warehouseid="+val+"&prefix="+prefix2,
	    				   	onChange: function(val){
	    				   		if(type === 'new'){
		    				   		var t = $("#f_invcateid").iCombo("getTxt");
		    				   		$("#f_invcateid").iCombo("setTxt",t.replace("----",""));
	    				   		}else{
	    				   			var t = $("#readonly_f_invcateid").html();
	    				   			$("#readonly_f_invcateid").html(FW.specialchars(t.replace("----","")));
	    				   		}
	    				   	}
	    				});
    				    if(warehouseInfo2 != ""){
    				    	$("#f_defBinid").iCombo("setVal",warehouseInfo.defBinid);
    						$("#f_invcateid").iCombo("setVal",warehouseInfo.invcateid);
    				    } 
	    			}
	    		}
	    	},
	    	{title : "可用库存", id : "canUseQty",type :"hidden"},
		    {title : "数量", id : "qty",type :"hidden"},
		    {title : "价格", id : "price",type :"hidden"},
		    {title : "货柜", id : "defBinid", type : "combobox",
	    		options:{
	    			allowEmpty: true,
	    			data:[]
	    			}},
	    	{title : "物资分类", id : "invcateid",type : "combobox",rules:{required:true},
	    		options:{
	    			firstItemEmpty:true,
	    			data:[]
	    		}
	    	},
	    	{title : "使用状态", id : "active",type : "radio",
		        data : [
		            ["Y","启用",true],
		            ["N","禁用"]
		        ]
			},
		    {title : "启用安全库存", id : "issafety",linebreak:true,type:"radio",data:[["1","是"],["0","否",true]],render:isSafetyRadioEvent},
		    {title : "最低库存量", id : "qtyLowInv", rules : {number:true,min:0,required:function(){return $("#autoform").iForm("getVal","issafety")==1;}}},
		   	{title : "经济订购量", id : "qtyEconomic", rules : {number:true,min:0}},
		   	{title : "是否开启编辑", id : "isEdit", type :"hidden"}
	];
	
	//编辑表单加载数据（通用方法）
	function initForm(inForm){
		$("#autoform").iForm("init",{"fields":inForm,"options":{validate:true}});
		if(type === 'new'){
			var loaddata = {"canUseQty" : 0, "qty" : 0, "price" : 0, "qtyLowInv":0,"qtyEconomic":0,"isEdit":"Y"};
			$("#autoform").iForm("setVal",loaddata);
			$("#autoform").iForm("endEdit","active");
		}else if(type === 'edit'){
			warehouseInfo = FW.get("warehouseInfo");
			if(warehouseInfo){
				$("#autoform").iForm("setVal",warehouseInfo);
				//使用warehouseInfo2 不再直接使用warehouseInfo 避免IE释放资源后调用出错
				warehouseInfo2 = $("#autoform").iForm("getVal");
			}
			if(!(warehouseInfo && warehouseInfo.isEdit === "Y")){
				if(!warehouseInfo.invcateid){
					$("#autoform").iForm("endEdit",["warehouseid"]);
				}
				else{
					$("#autoform").iForm("endEdit",["warehouseid","invcateid"]);
				}
			}
			
		}else if(type === "read"){
			warehouseInfo = FW.get("warehouseInfo");
			if(warehouseInfo){
				$("#autoform").iForm("setVal",warehouseInfo);
			}
			$("#autoform").iForm("endEdit");
		}
		
		checkIsSafety();
	}
	
	//保存到仓库
	function saveInfo(){
		var formData =$("#autoform").ITC_Form("getdata");
		
		if(formData.warehouseid && (!formData.invcateid)){
			FW.error( "物资类型为空或原绑定的物资类型已停用，请点取消并重新添加绑定");
		}
		
		if(!$("#autoform").valid()){
			return "validError";
		}
		
		if($("#autoform").iForm("getVal","issafety")!=1){//当不启用安全库存时，将最低库存和经济订购量置空
			$("#autoform").iForm("setVal",{"qtyLowInv":null,"qtyEconomic":null});
		}
		
		var warehouseData = $("#autoform").iForm("getVal");
		var iwiid = "";
		if(type === 'edit'){
			iwiid = warehouseInfo.iwiid;
		}
		warehouseData['iwiid'] = iwiid;
		warehouseData['warehousename'] = $("#f_warehouseid").iCombo("getTxt");
		warehouseData['binname'] = $("#f_defBinid").iCombo("getTxt");
		warehouseData['invcatename'] = $("#f_invcateid").iCombo("getTxt").replace("----","");
		return warehouseData;
	}
	
	function checkIsSafety(){
		if($("#autoform").iForm("getVal","issafety")=="1"){
			$("#autoform").iForm("show",["qtyLowInv","qtyEconomic"]);
		}else{
			$("#autoform").iForm("hide",["qtyLowInv","qtyEconomic"]);
		}	
	} 
	
	 function isSafetyRadioEvent(){
		$("#f_issafety_1,#f_issafety_0").next().click(checkIsSafety);
	} 
	
	$(document).ready(function() {
		initForm(noform);
	});
	
</script>
</head>
<body style="margin-top:10px;">
	<form id="autoform" class="autoform"></form>
</body>
</html>