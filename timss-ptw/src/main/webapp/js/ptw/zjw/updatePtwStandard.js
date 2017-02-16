
//设置form表单数据
function setForm( ){
	var data = {
			"eqNo" : eqNo,
			"eqName" : eqName
	};
	
	$("#autoform").iForm("setVal", data);
}


var updateFields = [
              {title : "id", id : "id" , type: "hidden"},
              {title : "sheetNo", id : "sheetNo" , type: "hidden"},
              {title : "编号",id:"wtNo",type:"label"},
              {title : "版本号",id : "version",type : "label"}, 
              {title : "生效时间", id : "beginTime", type:"datetime", dataType:"datetime"},
              {title : "失效时间", id : "endTime", type:"datetime", dataType:"datetime"},
              {title : "工作票类型", id : "wtTypeId" ,rules : {required:true},
            	  type : "combobox",
            	  dataType : "enum",
            	  enumCat : "ptw_standard_type",
            	  options:{
					allowEmpty:true
            	  },
            	  render : function(id){
					$("#" + id).iCombo("setTxt","");
            	  }  
				},
	  			{title : "历史操作票ID", id : "parentWtId", type: "hidden"},
	  			{title : "设备编码", id : "eqNo", type: "hidden"},
	  			{title : "设备名称",id:"eqName",type:"label",value:"请从左边设备树选择",
	  				formatter:function(val){
	  					if(val == "请从左边设备树选择"){
	  						return "<span style='color:red'>请从左边设备树选择</span>";
	  					}
	  					return val ? val : "";
	  				}},
	  			{title : "创建人",id:"createUserName",type:"label"},
	  			{title : "批准人",id:"approveUserName",type:"label"},
	  			{title : "工作内容", id : "workContent",wrapXsWidth:12,wrapMdWidth:8,rules : {required:true},linebreak:true},
	  			{title : "工作地点", id : "workPlace",wrapXsWidth:12,wrapMdWidth:8,rules : {required:true},linebreak:true}
              ];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	

	//初始化信息
	function initForm( ptwId ){
		var url = basePath + "ptw/ptwStandard/queryPtwStandardInfo.do?id=" + ptwId;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success"){
					var bean = data.bean;
					if(bean.version == 0){
						bean.version = null;
					}
					//实例ID
					instantId = bean.instantId;
					//审批状态
					flowStatus = bean.flowStatus;
					if(flowStatus.indexOf('_flow_std_status_4') >= 0){
						$("#autoform").iForm("show",["beginTime","endTime"])
					}else{
						$("#autoform").iForm("hide",["beginTime","endTime"])
					}
					//标准工作票类型
					stdType = bean.wtTypeId;
					//任务Id
					taskId = data.taskId;
					//审批状态
					applyFlag = data.applyFlag;
					//当前登录人
					currentUser = data.currentUser;
					//页面显示创建人信息
					if( bean.createUserName != null && bean.createUserName != "" && bean.createUserName != "null" ){
						bean.createUserName = bean.createUserName + " / " + FW.long2time(bean.createdate);
					}
					//页面显示批准人信息
					if( bean.approveUserName != null && bean.approveUserName != "" && bean.approveUserName != "null" ){
						bean.approveUserName = bean.approveUserName + " / " + FW.long2time(bean.approveDate);
					}
					//初始化form items
					setPtwStandard( bean, data.items );
					//初始化同编号的标准操作票记录
					initDataGrid(ptwId,bean.wtNo);
					//控制按钮
					setUpdateBtnPriv( bean );
				}else{
					FW.error( "初始化失败，请重新刷新！" );
				}
			}
		});
	}

	// 其他版本的标准操作票
	var sptwInfoDataGridColumn = [[ 
	           {field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'version',title:'版本号',width:50,fixed:true},
				/*{field:'eqName',title:'设备名称',width:150,fixed:true},*/
				{field:'workContent',title:'工作内容',width:300},
				{field:"beginTime",title:"生效时间",width:110,fixed:true,
		        	   formatter : function(value, row, index) {
							return FW.long2time(value);
						}
		           },
		           {field:"endTime",title:"失效时间",width:110,fixed:true,
		        	   formatter : function(value, row, index) {
							return FW.long2time(value);
						}
		           },
				{field:'createUserName',title:'编写人',width:80,fixed:true},
				{field:'flowStatus',title:'状态',width:80,fixed:true,formatter:function(val){
					var str = FW.getEnumMap("PTW_STD_STATUS")[val];
					if( str == null || str == "" ){
						str = val;
					}
					return str;
				},editor:{
					type : "combobox",
					options: {
						data : FW.parseEnumData("PTW_STD_STATUS",_enum)
					}
				}}
	       ]];
	   /**
	* 初始化数据表格
	*/
	function initDataGrid(sptwid,wtNo){
	   	var dataGrid = $("#sptwInfo_table").iDatagrid("init",{
	   			pageSize:pageSize,//pageSize为全局变量
	   			singleSelect:true,
			    url: basePath + "ptw/ptwStandard/sameCodeSptwListData.do?id="+sptwid+"&wtNo="+wtNo,	//basePath为全局变量，自动获取的       
			    columns:sptwInfoDataGridColumn,
			    onLoadSuccess: function(data){
			    	$("#title_sameCodeList").iFold("init");
					if(data && data.total==0){
						$("#title_sameCodeList").iFold("hide");
				    }else{
				    	$("#title_sameCodeList").iFold("show");
				 	}
			    },
			    onDblClickRow : function( rowIndex, rowData ){
			    	var url = basePath + "ptw/ptwStandard/updatePtwStandardMenu.do?id=" + rowData.id;
			    	addTabWithTree( "updatePtwStandard"+rowData.id, "标准工作票详细"+rowData.id, url,"ptw", "contentTb" );
			    }
		   	});
	}

	//根据标识flag 设置 过期-xpire、删除-delete、作废-cancel
	function deletePtwStandard( id, flag ){
		var url = basePath + "ptw/ptwStandard/deletePtwStandardBaseInfo.do?id=" + id + "&flag=" + flag;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success"){
					FW.success( "删除成功 ！");
					closeTab();
				}else{
					FW.error( "删除失败，请重试 ！");
				}
			}
		});
	}
	
	//设置update页面按钮权限
	function setUpdateBtnPriv( rowData ){
		var flowStatus = rowData.flowStatus;
		//当有审批权限 状态是草稿，说明还在申请阶段
		 if( currentUser == rowData.createuser  
				 && (  flowStatus.indexOf('_flow_std_status_0') >= 0 || flowStatus.indexOf('_flow_std_status_1') >= 0  ) ){
			$( "#approveBtn,#modifyButtonDiv,#printButtonDiv,#newPtwButton" ).hide();
			$( "#editDiv,#flowDiagramDiv" ).show();
			editFlag = true;
			//草稿状态时
			if(flowStatus.indexOf('_flow_std_status_0') >= 0 ){
				$("#deleteButtonDiv").show();
				$("#cancelButtonDiv").hide();
			}else if(flowStatus.indexOf('_flow_std_status_1') >= 0  ){
				//第一个环节时 流程可以作废不可以删除
				$("#deleteButtonDiv").hide();
				$("#cancelButtonDiv").show();
				$("#saveButton").hide();
				$("#commitButton").hide();
				$("#approveBtn").show();
				$("#closeBtn").show();
			}
			$("#autoform").iForm("hide",["createUserName","approveUserName"]);
			beginEditorPtwStd();
		}else if( applyFlag =="approver" && flowStatus.indexOf('_flow_std_status_0') < 0 ){ 
			auditInfoShowBtn = 1;
			//审批状态
			$( "#closeBtn,#approveBtn,#flowDiagramDiv" ).show();
			$( "#saveButton,#commitButton,#deleteButtonDiv,#cancelButtonDiv,#modifyButtonDiv,#printButtonDiv,#newPtwButton" ).hide();
			endEditorPtwStd();
		}else if( flowStatus.indexOf('_flow_std_status_4') >= 0 || flowStatus.indexOf('_flow_std_status_5') >= 0 
				|| applyFlag =="others"){
			if(flowStatus.indexOf('_flow_std_status_4') >= 0){ //如果是已通过状态
				$( "#modifyButtonDiv,#flowDiagramDiv" ).show();
				var newSptwflag = contains(privTypes,stdType); //登录用户是否有权限新建此类标准工作票，有才能修改、过期
				if(!newSptwflag){
					$( "#modifyButtonDiv" ).hide();
				}
			}else {//如果是其他状态
				$( "#modifyButtonDiv,#newPtwButton" ).hide();
			}
			$( "#closeBtn,#printButtonDiv,#flowDiagramDiv" ).show();
			$( "#editDiv,#approveBtn,#deleteButtonDiv,#cancelButtonDiv" ).hide();
			endEditorPtwStd();
		}else{
			$( "#closeBtn,#flowDiagramDiv" ).show();
			$( "#editDiv,#approveBtn,#deleteButtonDiv,#cancelButtonDiv,#modifyButtonDiv,#printButtonDiv,#newPtwButton" ).hide();
			endEditorPtwStd();
		}
		FW.fixRoundButtons("#toolbar");
	}
	
	function contains(privTypesList,stdTypeString){
		var result = false;
		for(var i=0;i<privTypesList.length;i++){
			if(stdTypeString == privTypesList[i]){
				return true;
			}
		}
		return result;
	}
	//开放编辑状态
	function beginEditorPtwStd(){
		$("#autoform").iForm("beginEdit");
		$("#autoform").iForm("hide","version");
		$(".safeItems .wrap-underline").css("paddingRight",0);
		$(".safeItems .safe-input-content,.exec-span,.remover-span").hide();
		$(".safeItems .safe-input").show();
	}
	
	//结束编辑状态
	function endEditorPtwStd(){
		$("#autoform").iForm("endEdit");
		var bean = $("#autoform").iForm('getVal');
		if(bean.version == null ){
			$("#autoform").iForm("hide","version");
		}
		$(".safe-input").attr("readOnly", true).attr("onkeyup","");
	}
	
	//点击修改按钮
	/**
	 * @description ：构造数据初始化新建标准工作票，会跳转到新建页面
	 * 参数格式要求如下：
	 * formData:{"id":"","wtTypeId":"sbs_ptw_std_type_1","parentWtId":"53lb032almzw9oep",
	 * "eqNo":"AST-00016796","eqName":"洋前变电站设备","workContent":"工作内容test","workPlace":"工作地点test"}
	 * safeItems:[{"safeType":"1","safeContent":"步骤1：test","safeOrder":1},{"safeType":"1","safeContent":"步骤二：test","safeOrder":2}]
	 */
	function copyToInintStdPtw( id ){
		/*var url = basePath + "ptw/ptwStandard/insertPtwStdPageForModify.do?formData=" + JSON.stringify( formData ) 
					+ "&safeItems=" + JSON.stringify( safeItems );*/
		var url = basePath + "ptw/ptwStandard/insertPtwStdPageForModify.do?id=" + id;
		addTabWithTree( "addPtwStandard", "新建标准工作票", url,"ptw", "contentTb");
	}
	
	//审批
	function audit1(){
		var formData = $("#autoform").iForm("getVal");
		var type = formData.wtTypeId;
	    //拿到安全措施内容
	  	var safeResult = getSafeInputs();
	  	
        var dataArr = {};
        dataArr.editFlag = "readonly";
        if(flowStatus.indexOf("_flow_std_status_1") >0  ){
        	dataArr.editFlag = "edit";
        }
      	
		dataArr.formData = JSON.stringify( formData );
		dataArr.safeItems = JSON.stringify( safeResult.safeItems );
		
		var workFlow = new WorkFlow();
		workFlow.showAudit(taskId, FW.stringify(dataArr), closeTab, closeTab, stop, "",0);
	}
	//空函数，不能删，否则IE报错
	function stop(){
		
	}