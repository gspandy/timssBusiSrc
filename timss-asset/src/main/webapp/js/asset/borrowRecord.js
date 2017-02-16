function getAssetId(){
	return Asset.objs["bean"].assetId;
}

//表格初始化
function initDataGrid(){
	$("#borrowRecordListDiv").iFold("init");
	dataGrid = $("#borrowRecordListTable").iDatagrid("init",{
		pageSize:pageSize,
		//singleSelect:true,
		url: basePath + "astBorrowRecord/getAllBorrowRecordList.do",
		queryParams :{'assetId': getAssetId},
		columns:[[
			{field:'borrowDate' , title: '领用日期', width: 100, fixed:true,
				formatter: function(value,row,index){
					return FW.long2date(value);
				}
			},
			{field:'borrowUserDeptName' , title: '领用部门', width: 100, fixed:true},
			{field:'borrowUserName' , title: '资产使用人', width: 100, fixed:true},
			{field:'returnDate', title: '归还日期', width: 100, fixed:true,
				formatter: function(value,row,index){
					return FW.long2date(value);
				}
			},
			{field:'returnUserName', title: '归还人', width: 100, fixed:true},	
			{field:'memo', title: '备注', width: 50, breakAll:true }
		]],
		onLoadSuccess: function(data){
			borrowNum=data.total;
			borrowState=data.state;
			changeBorrowShow(Asset.objs.mode!='create');
		}
	});
}
var borrowNum=0;//列表数据量
var borrowState;//是否已借出
function changeBorrowShow( toShow ){
	if(toShow && borrowNum > 0){
		$("#borrowRecordListDiv").iFold("show");
    	$("#borrowRecordListDiv").css({"height":"auto"});	
    	$("#borrowRecordListTable").datagrid("resize");
	}else{
		$("#borrowRecordListDiv").css({"height":0});
		$("#borrowRecordListDiv").iFold("hide");
	}
	if(toShow&&Asset.objs.bean.allowBorrow=="Y"){
		$("#borrowDiv").show();
		if(borrowState){
    		$("#borrowRecord").attr("disabled",true);
			$("#returnRecord").show();
    	}else{
    		$("#borrowRecord").attr("disabled",false);
			$("#returnRecord").hide();
    	}
	}else{
		$("#borrowDiv").hide();
		$("#returnRecord").attr("disabled",true);
		$("#borrowRecord").attr("disabled",true);
	}
}

function showBorrowWindow(src,title,submiturl){
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
		"onclick" : function(){
			//捕获表单里的信息,并转为String型
			//子窗口
			var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			var borrowData=conWin.$("#borrowForm").iForm('getVal');
			if(!borrowData.returnUserId){
				FW.error("归还人信息异常");
				return;
			}
			if(!conWin.$("#borrowForm").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return false;
			}
			//放入assetId
			borrowData.assetId=getAssetId();
			var borrowDataStr = JSON.stringify(borrowData);  //取表单值
			
			$.ajax({
				type : "POST",
				cache: false,
				data: {
					borrowData:borrowDataStr
				},
				url: submiturl,
				success: function(data){
					if(data.result=="success"){
						FW.success("提交成功");
						$("#borrowRecordListTable").datagrid("reload");
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					FW.error(XMLHttpRequest.responseJSON.msg);
				}
				
			});
			
			_parent().$("#itcDlg").dialog("close");
		}
	}];
	//新建系统配置对话框
	var winHeight;
	if(title=='领用登记'){
		winHeight = 330;
	}
	else{
		if(!!Asset.objs["bean"].imtdId){
			winHeight = 400;
		}
		else{
			winHeight = 360;
		}
	}
	var dlgOpts = {
		width : 600,
		height : winHeight,
		closed : false,
		title : title,
		modal : true
	};
	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : dlgOpts,
		"btnOpts" : btnOpts
	});
}
//点击领用或归还按钮，弹窗出现	
function openBorrowWin(flag) {
	var src = basePath+"page/asset/itc/assetinfo/borrowRecordWin.jsp?" +
			"assetId=" + getAssetId() + 
			"&itemcode=" + Asset.objs["bean"].itemCode +
			"&itemname=" + encodeURIComponent(Asset.objs["bean"].itemName) +
			"&imtdId=" + Asset.objs["bean"].imtdId;
	var submiturl;
	if(flag=="borrow"){
		submiturl = basePath + '/astBorrowRecord/insertBorrowRecord.do';
		showBorrowWindow(src,'领用登记',submiturl);
	}
	else{
		submiturl = basePath + '/astBorrowRecord/updateAstBorrowRecord.do';
		showBorrowWindow(src,'归还登记',submiturl);
	}
}
