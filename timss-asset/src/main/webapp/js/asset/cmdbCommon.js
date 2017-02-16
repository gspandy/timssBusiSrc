
var insertFields = "[" +
"				{title : 'id', id : 'id',type:'hidden'}," +
"				{title : 'CI名称', id : 'name',rules : {required:true,remote:{" +
"					url: basePath + 'asset/cmdbCi/queryCheckCmdbCiName.do'," +
"					type: 'post'," +
"					data: {" +
"						'paramsMap' : function(){" +
"							var params = {" +
"									'name': $('#f_name').val()," +
"									'id': $('#f_id').val()" +
"							};" +
"							 return FW.stringify(params);" +
"						}" +
"						" +
"					}" +
"				}}}," +
"	  			{title : 'CI类型', id : 'ciType',rules : {required:true}, type : 'combobox'," +
"					dataType : 'enum'," +
"					enumCat : 'CMDB_CI_TYPE'," +
"					options:{" +
"						allowEmpty:true," +
"						onChange : initFormCiType," +
"						initOnChange:false" +
"					}," +
"					render : function(id){" +
"						$('#' + id).iCombo('setTxt','');" +
"					}}," +
"	  			{title : 'CI子类型', id : 'subType',rules : {required:true},type : 'combobox',options:{firstItemEmpty:true,initOnChange:false}}," +
"	  			{title : 'CI状态', id : 'status',rules : {required:true},type : 'combobox',options:{firstItemEmpty:true,initOnChange:false}}," +
"	  			{title : 'CI责任人ID', id : 'responUserId',type:'hidden'}," +
"	  			{title : 'CI责任人', id : 'responUserName',rules : {required:true}," +
"	  				render: function(id) {" +
"	  					$('#' + id).attr('placeholder','请输入姓名或账号').initHintPersonList();" +
"	  					$('#' + id).initHintPersonList({" +
"	  						clickEvent: function(id, name) {" +
"	  							var uid = id.split('_')[0];" +
"	  							$('#f_responUserId').val(uid);" +
"	  							$('#f_responUserName').val(name);" +
"	  						}" +
"	  					});" +
"	  				}}," +
"	  			{title : '资产编号', id : 'eqId'}," +
"	  			{title : '所属单位', id : 'unitId',type : 'hidden'}," +
"	  			{title : '所属单位', id : 'unitName'}," +
"	  			{title : 'A端Id', id : 'aportId',type : 'hidden'}," +
"	  			{title : 'A端', id : 'aportName'}," +
"	  			{title : 'B端Id', id : 'bportId',type : 'hidden'}," +
"	  			{title : 'B端', id : 'bportName'}," +
"	  			{title : '所在机房ID', id : 'engineRoomId',type : 'hidden'}," +
"	  			{title : '所在机房', id : 'engineRoomName'}," +
"	  			{title : '所在机柜ID', id : 'cabinetId',type : 'hidden'}," +
"	  			{title : '所在机柜', id : 'cabinetName'}," +    
"	  			{title : '供应商ID', id : 'supplierId',type : 'hidden'}," +
"	  			{title : '供应商', id : 'supplier'}," +
"	  			{title : '供应商联系人', id : 'connectUser'}," +
"	  			{title : '供应商电话', id : 'connectPhone'}," +

"	  			{ title : '描述', id : 'description',type:'textarea',linebreak:true,wrapXsWidth:12,wrapMdWidth:8,height : 50 }" +
"	  		];" ;

var updateFields = "[" + 
              "{title : 'id', id : 'id',type:'hidden'}," + 
              "{title : 'CI名称', id : 'name',rules : {required:true,remote:{" + 
			"		url: basePath + 'asset/cmdbCi/queryCheckCmdbCiName.do'," + 
			"		type: 'post'," + 
			"		data: {" + 
			"			'paramsMap' : function(){" + 
			"				var params = {" + 
			"						'name': $('#f_name').val()," + 
			"						'id': $('#f_id').val()" + 
			"				};" + 
			"				 return FW.stringify(params);" + 
			"			}" + 
			"			" + 
			"		}" + 
			"	}}}," + 
              "{title : 'CI类型', id : 'ciType',rules : {required:true}, type : 'combobox'," + 
            "	  dataType : 'enum'," + 
            "	  enumCat : 'CMDB_CI_TYPE'," + 
            "	  options:{" + 
            "		  allowEmpty:true" + 
/*            "		  onChange : initFormCiTypeForUpdate," + 
            "		  initOnChange:false" + */
            "	  }," + 
            "	  render : function(id){" + 
            "		  $('#' + id).iCombo('setTxt','');" + 
            "	  }}," + 
            "	  {title : 'CI子类型', id : 'subType',rules : {required:true},type : 'combobox'}," + 
            "	  {title : 'CI状态', id : 'status',rules : {required:true},type : 'combobox'}," + 
            "	  {title : 'CI责任人ID', id : 'responUserId',type:'hidden'}," + 
            "	  {title : 'CI责任人', id : 'responUserName',rules : {required:true}," + 
            "		  render: function(id) {" + 
            "			  $('#' + id).attr('placeholder','请输入姓名或账号').initHintPersonList();" + 
            "			  $('#' + id).initHintPersonList({" + 
            "				  clickEvent: function(id, name) {" + 
            "					  var uid = id.split('_')[0];" + 
            "					  $('#f_responUserId').val(uid);" + 
            "					  $('#f_responUserName').val(name);" + 
            "				  }" + 
            "			  });" + 
            "		  }}," + 
            "		  {title : '资产编号', id : 'eqId'}," + 
            "		  {title : '所属单位', id : 'unitId',type : 'hidden'}," + 
            "		  {title : '所属单位', id : 'unitName'}," +
            "	  			{title : 'A端Id', id : 'aportId',type : 'hidden'}," +
            "	  			{title : 'A端', id : 'aportName'}," +
            "	  			{title : 'B端Id', id : 'bportId',type : 'hidden'}," +
            "	  			{title : 'B端', id : 'bportName'}," +
            "	  			{title : '所在机房ID', id : 'engineRoomId',type : 'hidden'}," +
            "	  			{title : '所在机房', id : 'engineRoomName'}," +
            "	  			{title : '所在机柜ID', id : 'cabinetId',type : 'hidden'}," +
            "	  			{title : '所在机柜', id : 'cabinetName'}," +        
            "	  	  {title : '供应商ID', id : 'supplierId',type : 'hidden'}," +
            "		  {title : '供应商', id : 'supplier'}," + 
            "		  {title : '供应商联系人', id : 'connectUser'}," + 
            "		  {title : '供应商电话', id : 'connectPhone'}," + 
            "		  { title : '描述', id : 'description',type:'textarea',linebreak:true,wrapXsWidth:12,wrapMdWidth:8,height : 50 }" + 
            "		  ];" ; 
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
//从基础属性拿值 
function setType( id, ciType, paramType ){
	var url = basePath + "asset/cmdbCi/queryOderTypeByType.do?ciType=" + ciType + "&paramType=" + paramType;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data && data.result ){
				var  arr = [["", "请选择"]];
				var result = data.result;
				for( index in result ){
					arr.push( [ result[index].id, result[index].paramVal ] );
				}
				$("#f_" + id ).iCombo("init", {
					data : arr
				});
			}
		}
	});
}

//从基础数据 初始化form 下拉框
function initFormCiType(){
	if(!isFireChange){
		return;
	}else{
		isFireChange=false;
	}
	
	var formData = $( "#" + formId ).iForm("getVal");
	var ciType = formData.ciType;
	//改变formId
	formId = "autoform_" + ciType;
	$(".changFormId").attr("id", formId );
	//重新初始化插入form
	initInsertForm();
	
	//$("#f_ciType").find("option[value='"+ciType+"']").attr("selected",true);
	$( "#" + formId ).iForm("setVal",{"ciType":ciType});
	//$( "#f_ciType").iCombo("setTxt",FW.getEnumMap("CMDB_CI_TYPE")[ciType]);
	//$("table[fieldid='ciType'] ").find("input").attr('value', ciType);
	//$("table[fieldid='ciType'] ").find("span[ class='itcui_combo_text' ]").text(FW.getEnumMap("CMDB_CI_TYPE")[ciType]);
	if( "CMDB_TYPE_10" == ciType ){
		 $( "#" + formId ).iForm("hide",["aportId", "aportName", "bportId", "bportName"]);
		 $( "#" + formId ).iForm("show",["engineRoomId", "engineRoomName"]);
	}else if( "CMDB_TYPE_14" == ciType ){
		 $( "#" + formId ).iForm("show",["aportId", "aportName", "bportId", "bportName"]);
		 $( "#" + formId ).iForm("hide",["engineRoomId", "engineRoomName"]);
	}else if($.inArray(ciType,["CMDB_TYPE_6","CMDB_TYPE_7","CMDB_TYPE_8"])>=0){
		 $( "#" + formId ).iForm("show",["cabinetId", "cabinetName"]);
		 $( "#" + formId ).iForm("hide",["aportId", "aportName", "bportId", "bportName","engineRoomId", "engineRoomName"]);
	}else{
		$( "#" + formId ).iForm("hide",["aportId", "aportName", "bportId", "bportName","engineRoomId", "engineRoomName","cabinetId", "cabinetName"]);
	}
	
	//form字段 对应的参数项枚举
	var paramArr = { 
			"subType":"CMDB_PARAM_1",
			"status":"CMDB_PARAM_2"
	};
	for( id in paramArr ){
		setType( id, ciType, paramArr[id] );
	}
	
	isFireChange=true;
}


// url = basePath + "asset/cmdbCi/searchCmdbParamsHint.do?paramType=" + paramType
//iHint
function searchHint( inputId, inputName, url ){
	var $firstPartyInput=$('#f_' + inputName);
	
	var firstPartyInit = {
		datasource : url,
		clickEvent : function(id, name) {
			$("#f_" + inputId ).val( id );
			$firstPartyInput.val( name );
		},maxItemCount : 100
	};
	
	$firstPartyInput.change(function(){
		var val = $.trim( $firstPartyInput.val() );
		if( val == "" ){
			$("#f_" + inputId ).val( "" );
		}
	});
	$firstPartyInput.iHint('init', firstPartyInit);
}

//供应商 iHint
function searchSupplierHint( inputId, inputName, url ){
	var $firstPartyInput=$('#f_' + inputName);
	
	var firstPartyInit = {
			datasource : url,
			clickEvent : function(id, name) {
				$("#f_" + inputId ).val( id );
				var arr = name.split("/");
				var len = arr.length;
				if( len == 1 ){
					$firstPartyInput.val( name );
				}else if( len == 2 ){
					$("#f_connectUser").val(arr[1]);
					$firstPartyInput.val( arr[0] );
				}else if( len == 3 ){
					$("#f_connectUser").val(arr[1]);
					$("#f_connectPhone").val(arr[2]);
					$firstPartyInput.val( arr[0] );
				}
			},maxItemCount : 100
	};
	
	$firstPartyInput.change(function(){
		var val = $.trim( $firstPartyInput.val() );
		if( val == "" ){
			$("#f_" + inputId ).val( "" );
		}
	});
	$firstPartyInput.iHint('init', firstPartyInit);
}


//初始化页面
function loadFormData( cId ){
	var url = basePath + "asset/cmdbCi/queryCmdbPubCiById.do?id=" + cId;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success"){
				$( "#"+formId ).iForm("setVal", data.bean );
				if( "CMDB_TYPE_10" == data.bean.ciType ){
					 $( "#" + formId ).iForm("hide",["aportId", "aportName", "bportId", "bportName"]);
					 $( "#" + formId ).iForm("show",["engineRoomId", "engineRoomName"]);
				}else if( "CMDB_TYPE_14" == data.bean.ciType ){
					 $( "#" + formId ).iForm("show",["aportId", "aportName", "bportId", "bportName"]);
					 $( "#" + formId ).iForm("hide",["engineRoomId", "engineRoomName"]);
				}else{
					$( "#" + formId ).iForm("hide",["aportId", "aportName", "bportId", "bportName","engineRoomId", "engineRoomName"]);
				}
				$( "#" + formId ).iForm("endEdit");
				
				$("#backButtonDiv, #deleteButtonDiv, #saveButtonDiv").hide();
				$("#closeButtonDiv, #editButtonDiv, #logButtonDiv, #relationButtonDiv").show();
				$(".inner-title").html("配置项详情");
				FW.fixRoundButtons("#toolbar");
				//权限
				setCiPriv();
			}else{
				FW.error("初始化数据错误！");
			}
		}
	});
}


//变更对话框
function changeDtlIframe(){
	var src = basePath + "page/asset/itc/cmdb/Cmdb-updateCmdbCiRemark.jsp";
	var pri_dlgOpts = {
			width : 550,
			height : 150,
			closed : false,
			title : "CI变更原因",
			modal : true
		};
	
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	},{
		"name" : "确定",
		"float" : "right",
		"style" : "btn-success",
		"onclick" : function() {				
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			if (!conWin.valid()) {
				return false;
			}
			var formdata = conWin.getFormData("statItemFrom");
			var remark = JSON.parse(formdata).remark1;
			saveOrUpdateCi( remark );
			return true;
		}
	} ];

	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : pri_dlgOpts,
		"btnOpts" : btnOpts
	});
}

//增加或更新CI
function saveOrUpdateCi( remark ){
	if(!$( "#" + formId ).valid()){
		return;
	}
	var formData = $( "#" + formId ).iForm("getVal");
	formData.createuser = remark;
	var url = basePath + "asset/cmdbCi/insertCmdbPubCi.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		data :{
			formData :  JSON.stringify( formData )
		},
		success : function(data) {
			if( data.result == "success" ){
				FW.success( "保存成功 ！");
				//resetForm();
				//closeTab();
				loadFormData( cId );
			}else{
				FW.error( "保存失败 ！");
			}
		}
	});
}

