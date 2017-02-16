<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>终结验证信息</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/datagrid-groupview.js?ver=${iVersion}"></script>
<script>
	var hasJdInfo = <%=request.getParameter("hasJdInfo")%>;
	var _dialogEmmbed = true;
	var currFrame = FW.getFrame(FW.getCurrentTabId());
	var safeItems = getSafeItems();
	
	var userName = ItcMvcService.user.getUserName();
	var datagrid = undefined;
	var safeDatas = {rows:safeItems,total:safeItems.length};
	var datagridHeight = hasJdInfo ? 205 : 225;
	$(document).ready(function() {
		
		if(hasJdInfo){
			$("#jdInfo").show();
		}
		
		$("#execCombo").iCombo("init",{
			onChange:function(val){
				if(datagrid){
					$("#safeTable").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
					.find("select").each(function(){
						$(this).iCombo("setVal",val);
					});
				}
			},
			allowSearch:true
		});
		
		$("#safeExecCheck").iCheck({
	        checkboxClass: 'icheckbox_flat-blue',
	        radioClass: 'iradio_flat-blue'
	    });
		$("#safeExecCheck").on('ifChecked', function(event){
			$("#execComboWrap").show();
			$("#checkDiv").css("width",90);
		});
		$("#safeExecCheck").on('ifUnchecked', function(event){
			$("#execComboWrap").hide();
			$("#checkDiv").css("width",124);
		});
		
		$.post(basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do",{"role" : "PTW_safe_exec","hasOther":"select"},function(data){
			$("#execCombo").iCombo("loadData",data);
			initDatagrid(data);
		},"json");
		
		var fields = [
			{id:"endWl",title : "终结许可人",type:"label",value:userName},
			{id:"endTime",title : "终结时间",type:"datetime",dataType:"datetime",value:new Date(), rules : {required:true}, linebreak:true,wrapXsWidth:5,wrapMdWidth:5},
			{id:"password",title : "密码",dataType:"password", rules : {required:true},wrapXsWidth:5,wrapMdWidth:5}
		];	
		$("#bizForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	});
	
	function initDatagrid(data){
		var safeTypes = ["必须采取的安全措施","检修必须采取的安全措施","运行必须采取的安全措施","应装接地线","补充安全措施","保留带电部分"];
		var columns = [[
			{field:'safeType',title:'',hidden:true},
			{field:'safeOrder',title:'',width:20,fixed:true},
			{field:'safeContent',title:'安全措施内容',width:320},
			{field:'executer',title:'执行人',width:80,fixed:true},
			{field:'remarks',title:'备注',width:160,'editor':{
			    "type" : "text"
			}},
			{field:'removerNo',title:'解除人',width:80,fixed:true,'editor':{
			    "type" : "combobox",
			    "options" : {
			        "data" : data,
					allowSearch:true             
			    }
			}}
		]];
		
		datagrid = $("#safeTable").datagrid({
			width:775,
			height:datagridHeight,
			columns:columns,
			singleSelect:true,
			fitColumns:true,
            data:safeDatas,
            collapsible:true,
            view:groupview,
            groupField:'safeType',
            groupFormatter:function(value,rows){
                return safeTypes[parseInt(value)-1];
            }
		});
		
		for(var i = 0 ; i <safeDatas.total; i++){
			datagrid.datagrid('beginEdit',i);
		}
	}
	
	function getFormData(){
		var endJdxNum = null;
		var endJdxNo = null;
		if(hasJdInfo){
			//验证接地刀闸
			endJdxNum = $("#endJdxNum").val();
			endJdxNo = $("#endJdxNo").val();
			var r = /^\d+$/;
			if(!r.test(endJdxNum)){
				FW.error("请输入正确的接地线数目");
				return null;
			}
		}
		
		var index = 0;
		$("#safeTable").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
		.find("select").each(function(){
			if($(this).iCombo("getVal") == -1){
				//FW.error("请填写备注或选择解除人");
				//index = -1;
				index ++;
				//return false;
			}else{
				safeItems[index].removerNo = $(this).iCombo("getVal");
				safeItems[index].remover = $(this).iCombo("getTxt");
				//safeItems[index].remarks = $(this).
				index++;
			}
		});
		
		var index2 = 0; index3=0;
		$("#safeTable").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row")
		.find("input").each(function(){
			if(index3==0||index3%2==0){
				if(this.value != -1 && $.trim(this.value)!=""){
					safeItems[index2].remarks = this.value.toString();
				}
				index2++;
			}
			index3++;
		});
		for(var n=0; n<safeItems.length;n++){
			var remarksLen = safeItems[n].remarks==null?0:safeItems[n].remarks.length;
			var removerLen = safeItems[n].remover==null?0:$.trim(safeItems[n].remover).length;
			if(remarksLen==0 && removerLen==0){  //备注和解除人同时为空的时候，验证不通过
				FW.error("请填写备注或选择解除人");
				return false;
			}
		}
		
		if(! $("#bizForm").valid()){
			return null;
		}
		
		return {formData:$("#bizForm").iForm("getVal"),safeItems:safeItems,endJdxNum:endJdxNum,endJdxNo:endJdxNo};
	}
	
	function getSafeItems(){
		var currSafeItems = currFrame.getEndSafeData();
		var currPtwSafes = currFrame.ptwSafes;
		if(!currPtwSafes){
			return currSafeItems;
		}
		var currSafeDatas = currPtwSafes.safeDatas;
		for(var i=0;i<currSafeItems.length;i++){
			var safeType = currSafeItems[i].safeType;
			var safeOrder = currSafeItems[i].safeOrder;
			for(var j=0;j<currSafeDatas.length;j++){
				var safeType2 = currSafeDatas[j].safeType;
				var safeOrder2 = currSafeDatas[j].safeOrder;
				if(safeType==safeType2 && safeOrder==safeOrder2){
					currSafeItems[i].id = currSafeDatas[j].id;
					currSafeItems[i].executer = currSafeDatas[j].executer;
					currSafeItems[i].executerNo = currSafeDatas[j].executerNo;
					break;
				}
			}
		}
		return currSafeItems;
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="toolbar-with-pager">
		<div id="execComboDiv" style="float: right;">
	    	<div id="execComboWrap" style="float: right;display: none;">
	    		<select id="execCombo" style="width:120px;"></select>
	    	</div>
	    	<div id="checkDiv" style="float: right;width:124px;">
			    <input type="checkbox" id="safeExecCheck">
			    <label for="safeExecCheck" style="font-size: 12px;">同一解除人</label>
			</div>			
	    </div>
	</div>
	<table id="safeTable"></table>
	<div style="text-align: center;font-size: 14px;font-weight: bold;margin-top: 10px;margin-bottom: 10px;">
		<span>本工作票上安全措施及补充安全措施已全部解除，警示牌已收回。</span>
	</div>
	<div id="jdInfo" style="font-size: 12px;line-height: 26px;margin-left: 70px;display: none;">
		<span style="float: left;" class="form-required">*</span>
		<span style="float: left;">接地线（刀）共 </span>
		<div class="input-group-sm"  style="float: left;">
		    <input id="endJdxNum" type="text" class="form-control" style="width:40px">
		</div>
		<span  style="float: left;">组已拆除，编号 </span>
		<div class="input-group-sm"  style="float: left;">
		    <input id="endJdxNo" type="text" class="form-control" style="width:365px">
		</div>
	</div>
	<div style="clear: both;"></div>
	<form id="bizForm" style="margin-left: 20px;"></form>
</body>
</html>