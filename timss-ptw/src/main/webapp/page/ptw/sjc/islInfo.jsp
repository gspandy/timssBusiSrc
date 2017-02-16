<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String sessionid = session.getId();
	String delKey = FileUploadUtil.getValidateStr((SecureUser)session.getAttribute(Constant.secUser), FileUploadUtil.DEL_ALL);
 %>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>隔离证详情</title>
<script>
	_useLoadingMask = true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/ptw/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/commonForm.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/ptw/PtwUtil.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/isolationTreeCommon.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/popVerifyPassword.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/islButton.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/islSafe.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/relateKeyBox.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/sjc/openKeyBoxTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script>
	//是否在编辑
	var editFlag = true;
	//是否有地线
	var elecFlag = true;
	//是否有检修
	var jxFlag = true;
	//备注
	var remark = "";
	//是否copy
	var isCopy = false;
	
	var params = <%=request.getParameter("params")%>;
	var id = params.id;
	var opType = params.opType;
	var islStatus = null;
	
	function loadSafeDataFromIslArea(){
		if(params.areaId){
			var datagridUrl = basePath + "ptw/ptwIsolationArea/queryIsolationMethodList.do?areaId=" + params.areaId;
			params.areaId = null;
			$.post(datagridUrl,{},function(data){
				if(data){
					for(var i = 0 ; i < data.total; i++){
						data.rows[i].safeOrder = i + 1;
						data.rows[i].safeType = 1;
					}
				}
				$("#IsolationSafeDatagrid").datagrid('loadData',data);
			},"json");
		}
		
	}
	
	var fields = [
					{title : "id", id : "id",type:"hidden" },
					{title : "eqId", id : "设备Id",type:"hidden" },
					{id:"wtTypeId",title : "隔离证类型",type : "combobox",rules : {required:true},options:{
						url : basePath+ "ptw/ptwType/queryPtwTypes.do?queryType=all",
						remoteLoadOn : "init",
						onChange:function(value){
								$.post(basePath+ "ptw/ptwType/queryPtwTypeInfo.do?ptwTypeId="+value,{},function(data){
									var ptwTypeDefine = data.ptwTypeDefine;
									changeWtTypeId ( ptwTypeDefine );
							});
						}
					}},
					{id:"eqName",title : "设备名称",type:"label",value:"请从左边设备树选择"},
					{id:"eqNo",title : "设备编码",type:"label",value:"请从左边设备树选择"},
					{title : "钥匙箱ID", id : "keyBoxId",type : "hidden"},
					{title : "钥匙箱号", id : "keyBoxNo",rules : {required:true},
						render:function(id){
			            	 var ipt = $("#" + id);
			            	 ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
			            	 ipt.ITCUI_Input();
			            	 ipt.next(".itcui_input_icon").on("click",function(){
			                     var src = basePath + "ptw/ptwIsolationArea/SelectKeyBox.do";
			                     var dlgOpts = {
			                         width : 800,
			                         height:500,
			                         closed : false,
			                         title:"双击选择钥匙箱",
			                         modal:true
			                     };
			                     Notice.dialog(src,dlgOpts,null);
			                 });
			             }
					},
					{id:"createUserName",title : "创建人/时间",type:"label"},
					{id:"issuer",title : "签发人/时间",type:"label"},
					{id:"issueSuper",title : "签发值长",type:"label"},
					{id:"executer",title : "许可人/时间",type:"label"},
					{id:"withDraw",title : "结束人/时间",type:"label"},
					{id:"remover",title : "终结人/时间",type:"label"},
					{id:"workContent",title : "工作内容",height:50,linebreak:true,wrapXsWidth:12,wrapMdWidth:8,
						rules : {required:true,maxlength:300},breakAll : true,type : "textarea"
					}
				];
	
	//根据状态控制form 字段
	function controlFormFieldByStatus( status ){
		if( "newIsolation" == opType ){
			$("#baseInfoForm").iForm("hide",["createUserName","issuer","issueSuper","executer","withDraw","remover"]);
		}else if( "islDetail" == opType ){
			switch( status ){
				case 300 :
					$("#baseInfoForm").iForm("show",["createUserName"]);
					$("#baseInfoForm").iForm("hide",["issuer","issueSuper","executer","withDraw","remover"]);
					break;
				case 400 :
					$("#baseInfoForm").iForm("show",["createUserName","issuer","issueSuper"]);
					$("#baseInfoForm").iForm("hide",["executer","withDraw","remover"]);
					break;
				case 500 :
					$("#baseInfoForm").iForm("show",["createUserName","issuer","issueSuper","executer"]);
					$("#baseInfoForm").iForm("hide",["withDraw","remover"]);
					break;
				case 600 :
					$("#baseInfoForm").iForm("show",["createUserName","issuer","issueSuper","executer","withDraw"]);
					$("#baseInfoForm").iForm("hide",["remover"]);
					break;
				case 700 :
					$("#baseInfoForm").iForm("show",["createUserName","issuer","issueSuper","executer","withDraw","remover"]);
					break;
				case 800 :
					$("#baseInfoForm").iForm("show",["createUserName","issuer","issueSuper","executer","withDraw","remover"]);
					break;
				default :
					break;
			}
		}
	}
		  		
	//隔离证类型
	function changeWtTypeId( ptwTypeDefine ){
		elecFlag = ptwTypeDefine.hasElec == 1 ? true : false;
		jxFlag = ptwTypeDefine.hasSafeRepair == 1 ? true : false;
		showDatagridByConfig(jxFlag,elecFlag);
		if(opType == "newIsolation"){
			$("#CompSafeDiv").hide();
		}
	}	
	
	//设备数带过来的参数
	function onTreeItemClick(node){
		$("#baseInfoForm").iForm('setVal',{eqId:node.id,eqName:node.text,eqNo:node.assetCode});
	}
	
	//填充Form基本信息
	function loadBaseInfoData(rowData){
		editFlag = false;
		islStatus = rowData.status;
		id = rowData.id;
		remark = rowData.remark;
		var createDate = rowData.createdate;
		var createUserName = rowData.createUserName + "/" + FW.long2time(createDate);
		var issuer = rowData.issuer + "/" + FW.long2time(rowData.issuedTime);
		var executer = rowData.executer + "/" + FW.long2time(rowData.executerTime);
		var withDraw = rowData.withDraw + "/" + FW.long2time(rowData.withDrawTime);
		var remover = rowData.remover + "/" + FW.long2time(rowData.removerTime);
		var issueSuper = rowData.issueSuper;
		var data = {
				"id" : rowData.id,
				"eqId" : rowData.eqId,
				"wtTypeId" : rowData.wtTypeId,
				"eqName" : rowData.eqName,
				"eqNo" : rowData.eqNo ,
				"workContent" : rowData.workContent,
				"createUserName" : createUserName,
				"issuer" : issuer,
				"issueSuper" : issueSuper,
				"executer" : executer,
				"withDraw" : withDraw,
				"remover" : remover,
				"keyBoxId" : rowData.keyBoxId,
				"keyBoxNo" : rowData.keyBoxNo
				
			};
		$("#baseInfoForm").iForm("setVal",data);
		$("#baseInfoForm").iForm("show",["createUserName"]);
		controlFormFieldByStatus( islStatus );
		$("#baseInfoForm").iForm("endEdit");
		
		//控制按钮
		islButtonControl(opType,islStatus);
		
		//加载备注
		if( remark != null && remark != ""){
			initRemarkDiv( remark );
		}else{
			$("#remarkDiv").hide();
		}
	}
	
	//保存页面所有数据 isSave -- true : 点击保存按钮; false : 其他按钮; failMsg : 失败提示语
	function saveFormAndDatagrid( isSave, failMsg ){
		if(!$("#baseInfoForm").valid()){
			return false;
		}
		var formData = $("#baseInfoForm").iForm("getVal");
		if("请从左边设备树选择" == formData.eqName || "请从左边设备树选择" == formData.eqNo ){
			FW.error( "请从左边设备树选择一项！");
			return false;
		}
		//增加关联钥匙箱的信息
		formData.relateKeyBoxId = getRelateKeyBoxForSave();
		 
		var validSafeResult = validSafeDatagrid(islStatus,isCopy);
		if(!validSafeResult.valid){
			return false;
		}
		
		var url = basePath + "ptw/ptwIsolation/insertPtwIsolation.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			data :{
				formData :  JSON.stringify( formData ),
				safeDatas : FW.stringify( validSafeResult.IsolationSafeDatagrid ),
				elecDatas : FW.stringify( validSafeResult.IsolationElecDatagrid ),
				jxDatas : FW.stringify( validSafeResult.IsolationJxDatagrid ),
				compSafeDatas : FW.stringify( validSafeResult.CompSafeDatagrid )
			},
			success : function(data) {
				if( data.result == "success" ){
					if( isSave ){
						FW.success( "保存成功 ！");
						endEditor("IsolationSafe");
						endEditor("IsolationElec");
						endEditor("IsolationJx");
						endEditor("CompSafe");
						endEditRelateKeyBox();
					}
					if( opType == "newIsolation"){
						opType = "islDetail";
					}
					loadBaseInfoData( data.ptwIsolationBean );
					return true;
				}else{
					if( isSave ){
						FW.error( "保存失败 ！");
					}else{
						FW.error( failMsg );
					}
					return false;
				}
			}
		});
	}
	
	//复制form
	function copyIsl(){
		var formData = $("#baseInfoForm").iForm("getVal");
		formData.id = 0;
		formData.status = 300;
		formData.remark = "";
		formData.createUserName = "";
		opType = 'newIsolation';
			
		islStatus = formData.status;
		var data = {
				"id" : formData.id,
				"eqId" : formData.eqId,
				"wtTypeId" : formData.wtTypeId,
				"eqName" : formData.eqName,
				"eqNo" : formData.eqNo ,
				"workContent" : formData.workContent,
				"createUserName" : ""
			};
		$("#baseInfoForm").iForm("setVal",data);
		$("#baseInfoForm").iForm("hide",["createUserName"]);
		$("#baseInfoForm").iForm("beginEdit", ["workContent","keyBoxNo"]);
		
		//控制按钮
		islButtonControl(opType,islStatus);
		//控制隐藏form field
		controlFormFieldByStatus( islStatus );
		
		editFlag = true;
		isCopy = true;
		controlSafeDivForCopy();
		$("#remarkDiv").hide(); 
		beginEditRelateKeyBox();
	}
	
	$(document).ready(function() {
		initAssetTree();
		$("#baseInfoForm").iForm("init",{"options":{validate:true,fixLabelWidth:true},"fields":fields});
		controlFormFieldByStatus( islStatus );
		
		//控制按钮
		islButtonControl(opType,islStatus);
		if(opType == "newIsolation"){
			initExecAndDatagrid(islStatus,0,0);
			initRelateKeyBoxDatagrid(0,0);
		}else if(opType == "islDetail"){
			var url = basePath + "ptw/ptwIsolation/queryPtwIsolationById.do?id=" + id;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					if( data.result == "success"){
						islStatus = data.ptwIsolationBean.status;
						initExecAndDatagrid(islStatus,id,0);
						initRelateKeyBoxDatagrid(id,0);
						loadBaseInfoData( data.ptwIsolationBean );
					}else{
						FW.error( "加载信息失败！" );
					}
				}
			});
		} 
		
		
		
		//点击编辑
		$("#editBtn").click(function(){
			editFlag = true;
			$("#baseInfoForm").iForm("beginEdit",["workContent","keyBoxNo"]);
			
       		beginEditRelateKeyBox();
       		beginEditSafeItems();
			$("#editBtn").hide();
			FW.fixToolbar("#toolbar");
		});
		
		//点击备注
		$("#remarkBtn").click(function(){
			 remarkIsl( id , remark );
		});		
		//打印
		$("#printBtn").click(function(){
			var template = "TIMSS2_PTW_GL";
			var showName = $("#f_wtTypeId").find("option:selected").text();
			showName += "隔离证书";
			
			var url = fileExportPath + "preview?__report=report/"+template+".rptdesign&__format=pdf&wt_id=" + id
					+ "&showName=" + encodeURI(showName);
			//window.open(url);

			var title ="隔离证书"
			FW.dialog("init",{
				src: url,
				btnOpts:[{
			            "name" : "关闭",
			            "float" : "right",
			            "style" : "btn-default",
			            "onclick" : function(){
			                _parent().$("#itcDlg").dialog("close");
			             }
			        }],
				dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
			});
		});
	});
	
	
</script>

</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div id="toolbar" class="btn-toolbar ">
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" id="closeDiv" onclick="closeTab()">关闭</button>
			</div>
			<div class="btn-group btn-group-sm" >
				<button type="button" class="btn btn-success" id="saveButtonDiv" onclick="saveFormAndDatagrid( true, '' );">保存</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-success" id="signBtnDiv" onclick="issueIsl();" >签发</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-success" id="permitBtnDiv" onclick="preLicIsl();" >许可</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-success" id="permitCfmBtnDiv" onclick="licIsl();" >确认许可</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-success" id="finBtnDiv" onclick="finishIsl();">结束</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-success" id="endBtnDiv" onclick="preEndIsl();">终结</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button type="button" class="btn btn-success" id="endCfmBtnDiv" onclick="endIsl();">确认终结</button>
			</div>				
			<div class="btn-group btn-group-sm" id="moreBtnDiv" style="display: none;">
				<button type="button" class="btn btn-default" id="editBtn" >编辑</button>
				<button type="button" class="btn btn-default" id="remarkBtn" >备注</button>
				<button type="button" class="btn btn-default" id="cancelBtn" onclick="cancelIsl();">作废</button>
				<button type="button" class="btn btn-default" id="addSafeBtn" onclick="editSafeDiv('CompSafe');" >补充安全措施</button>
				<button type="button" class="btn btn-default" id="printBtn">打印</button>
				<button type="button" class="btn btn-default" id="copyBtn" onclick="copyIsl();">复制</button>
			</div>
			
		</div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="inner-title" class="inner-title">
		隔离证基本信息
	</div>
	<div id="baseFormDiv">
		<form id="baseInfoForm"></form>
	</div>
	
    
    <div id="RelateKeyBoxDiv">
		<div id="titleRelateKeyBox" grouptitle="关联钥匙箱号">
			<div class="margin-title-table">
				<form id="RelateKeyBoxForm"><table id="RelateKeyBoxDatagrid" class="eu-datagrid"></table></form>
				<div id="RelateKeyBoxBtnDiv" class="row btn-group-xs">
					 <button id="btnRelateKeyBoxTable" onclick="popKeyBoxWindow();"
					 type="button" class="btn btn-success">添加</button>
				</div>
			</div>
		</div>
    </div>
    
    <div id="execComboDiv" style="float:right;">
    	<div id="execComboWrap" style="float: right;display: none;">
    		<select id="execCombo" style="width:80px;"></select>
    	</div>
    	<div id="checkDiv" style="float: right;width: 90px;">
		    <input type="checkbox" id="safeExecCheck">
		    <label for="safeExecCheck" style="font-size: 12px;" id="execCheckBoxLabel">同一执行人</label>
		</div>
    </div>
    
    <div id="IsolationJxDiv">
		<div id="titleIsolationJx" grouptitle="检修必须采取的安全措施">
			<div class="margin-title-table">
				<form id="IsolationJxForm"><table id="IsolationJxDatagrid" class="eu-datagrid"></table></form>
				<div id="IsolationJxBtnDiv" class="row btn-group-xs">
					 <button id="btnIsolationJxTable" onclick="appendIsolationSafe('IsolationJx');"
					 type="button" class="btn btn-success">添加</button>
				</div>
			</div>
		</div>
    </div>
    
	<div id="IsolationSafeDiv">
		<div id="titleIsolationSafe" grouptitle="必须采取的安全措施">
			<div class="margin-title-table">
				<form id="IsolationSafeForm"><table id="IsolationSafeDatagrid" class="eu-datagrid"></table></form>
				<div id="IsolationSafeBtnDiv" class="row btn-group-xs">
					 <button id="btnIsolationSafeTable" onclick="appendIsolationSafe('IsolationSafe');" 
					 type="button" class="btn btn-success">添加</button>
				</div>
			</div>
		</div>
    </div>
    
    
	<div id="IsolationElecDiv">
		<div id="titleIsolationElec" grouptitle="应接地线">
			<div class="margin-title-table">
				<form id="IsolationElecForm"><table id="IsolationElecDatagrid" class="eu-datagrid"></table></form>
				<div id="IsolationElecBtnDiv" class="row btn-group-xs">
					 <button id="btnIsolationElecTable" onclick="appendIsolationSafe('IsolationElec');"
					 type="button" class="btn btn-success">添加</button>
				</div>
			</div>
		</div>
    </div>
    
	<div id="CompSafeDiv">
		<div id="titleCompSafe" grouptitle="补充安全措施">
			<div class="margin-title-table">
				<form id="CompSafeForm"><table id="CompSafeDatagrid" class="eu-datagrid"></table></form>
				<div id="CompSafeBtnDiv" class="row btn-group-xs">
					 <button id="btnCompSafeTable" onclick="appendIsolationSafe('CompSafe');"
					 type="button" class="btn btn-success">添加</button>
				</div>
			</div>
		</div>
    </div>
    
    <div id="remarkDiv" style="display: none;" >
	    <div id="remarkFormDiv"  grouptitle="备注">
			<form id="remarkForm"></form>
		</div>
    </div>
    
</body>
</html>