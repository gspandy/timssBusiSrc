/* 表单字段定义  */
var fields = [
				{title : "ID", id : "id",type : "hidden"},
				{title : "系统代号", id:"no",
					wrapXsWidth:4,wrapMdWidth:4,
					rules : {required:true,remote:{
							url: basePath + "ptw/ptwIsolationArea/checkIsolationAreaNo.do",
							type: "post",
							data: {
								"isolationAreaData": function() {
									var temp = { 
													id: $("#f_id").val(),
													isolationAreaNo:$("#f_no").val()
												};
							        return FW.stringify(temp);
							    }
							}
						}},
					messages:{remote:"编号已存在，请输入唯一编号"}
				},
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
				
				{
			        title : "系统名称", 
			        id : "name",
			        type : "textarea",
			        linebreak:true,
			        rules : {required:true,maxChLength:160},
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:55
			    }
			];
var isolationPointDelteId = 1;
var	isolationPointGridField = [[
        {field:"pointNo",title:"隔离点编号",width:110,fixed:true}, 
        {field:"pointName",title:"隔离点",width:200,fixed:true}, 
		{field:"stdMethodId",title:"隔离方法编号",width:110,fixed:true}, 
		{field:"methodName",title:"隔离方法名",width:300},
		{field : 'id',title : '',width:55, fixed:true,
		 formatter:function(value,row,index){
			     return '<img src="'+basePath+'img/ptw/btn_garbage.gif"  width="16" height="16" >';
		 	}
		}
	]];

//删除一行datagrid数据
function deleteThisRow(rowIndex, field, value) {
	if (field == 'id') {
		FW.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
			$("#isolationPointTable").datagrid("deleteRow",rowIndex);
		});
	}
}

//初始化 form & datagrid
var editFlag = false;
function initIsolationAreaPage(id){
	$("#isolationAreaForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	
	$("#title_isolationPoint").iFold("init");
	$("#isolationPointTable").datagrid({
	    columns:isolationPointGridField,
	    idField:'isolationPointDelteId',
	    fitColumns:true,
	    scrollbarSize:0,
	    onClickCell : function(rowIndex, field, value) {
			if (field == 'id') {
				deleteThisRow(rowIndex, field, value);
			}
		},
        onLoadSuccess: function(data){
        	if( editFlag ){
        		$("#isolationPointTable").datagrid("showColumn","id");
        		$("#isolationPointBtnDiv").show(  );
        	}else{
        		$("#isolationPointTable").datagrid("hideColumn","id");
        		$("#isolationPointBtnDiv").hide(  );
        	}
        	var len = $("#isolationPointTable").datagrid("getRows").length;
        	var textVal =  len > 0 ? "继续添加" : "添加隔离点";
        	$("#btn_isolationPointTable").text( textVal );
        	FW.fixRoundButtons("#toolbar");
			
        }
	}); 
	
	if(id != 0){
		$.post(basePath + "ptw/ptwIsolationArea/queryPtwIsolationAreaById.do",{"id":id},
				function(isolationAreaData){
					if(isolationAreaData.result == "success"){
						$("#btn_isolationArea_save").hide(); 
						var isolationAreaDataFromData = eval("(" +isolationAreaData.data+ ")");
						$("#isolationAreaForm").iForm("setVal",isolationAreaDataFromData);
						$("#isolationAreaForm").iForm("endEdit");
						FW.fixRoundButtons("#toolbar");
					}
				},"json");
		
		var datagridUrl = basePath + "ptw/ptwIsolationArea/queryIsolationMethodList.do?areaId=" + id;
		$("#isolationPointTable").datagrid({
			url : datagridUrl
		});
	}
	
}



/**
 * 编辑隔离方法
 */
function editIsolationArea(){
	$("#isolationAreaForm").iForm("beginEdit"); 
	$("#btn_isolationArea_save").show(); 
	$("#btn_isolationArea_edit").hide(); 
	FW.fixRoundButtons("#toolbar");
	editFlag = true;
	$("#isolationPointTable").datagrid("reload");
}

/**
 * 禁用隔离方法
 */
function deleteIsolationArea(){
	Notice.confirm("确定删除|确定删除该隔离方法么？该操作无法删除。",function(){
		$.post(basePath + "ptw/ptwIsolationArea/deleteIsolationArea.do",
		 		{"id":id},
				function(data){
					if(data.result == "success"){
						FW.success("删除成功");
						closeCurPage();
					}else {
						FW.error("删除失败");
					}
		  },"json");
	},null,"info");	
	
}

var clickDatagridId = "";

/**
 * 添加隔离方法弹出框
 */
function appendIsolationPoint(){
	clickDatagridId = "isolationPointTable";
	FW.showIslPointAndMethod1({
		onParseData : function(data){
			var rowData = $("#isolationPointTable").datagrid( 'getRows' );
			var size = rowData.length;
			
			var flag = false;
			var msg = "";
			for(var i=0;i<size;i++){
				for( var j = 0; j < data.length; j ++ ){
					var dataId = data[j].pointNo;
					if( dataId == rowData[i].pointNo ){
						flag = true;
						msg += " " + dataId;
					}
				}
			}
			if( !flag ){
				for( var j = 0; j < data.length; j ++ ){
					$("#isolationPointTable").datagrid("appendRow",data[j] );
				}
			}else{
				FW.error( msg + "隔离点已经存在！" );
			}
			$("#btn_isolationPointTable").html("继续添加");
		}
	});
}  

/**
 * 弹出隔离方法和隔离点的选择框
 * @param {} opts
 */
FW.showIslPointAndMethod1 = function(opts){
	if(!opts){
		return;
	}
	opts.basePath = opts.basePath || window.basePath || "";
	opts.width = opts.width || "75%";
	opts.height = 520;
	var treeSrc = basePath + "page/ptw/core/tree.jsp?embbed=1&opentype=&jumpMode=dialog";
	var listSrc = basePath + "ptw/ptwIslMethDef/openIslMethDefItemPage.do?embbed=1";

	if(_parent().$("#itcDlgIsolationTree").length==1){
		_parent().$("#itcDlgIsolation").dialog("open");
		_parent().$("#itcDlgIsolationTree").attr("src",treeSrc);
		_parent().$("#itcDlgIsolationPage").attr("src",listSrc);
		return;
	}
	else{		
		var dlgHtml = '<div id="itcDlgIsolation">' +
			'<div style="width:100%;height:100%;padding-left:240px;position:relative;overflow:hidden" class="bbox">' + 
			    '<iframe class="tree-iframe" frameborder="no" border="0" style="width:240px;height:100%;position:absolute;left:0px;top:0px;" id="itcDlgIsolationTree">' +
			    '</iframe>'+
			    '<iframe frameborder="no" border="0" style="width:100%;height:100%;" id="itcDlgIsolationPage">' +
			    '</iframe>' +
		    '</div>' + 
		'</div>' +
		'<div id="itcDlgIsolationBtn" style="display:none;" class="bbox itcdlg-btns">' +
	    	'<div id="itcDlgIsolationBtnWrap" style="width:100%;height:100%">' + 
	    	'</div>' +
		'</div>';
		_parent().$("body").append(dlgHtml);
	}
	var dlgOpts = {
		idSuffix : "Isolation",
		width : opts.width,
		height : opts.height,
		title : opts.title || "选择隔离方法"
	};
	var btnOpts = [{
            "name" : "关闭",
            "onclick" : function(){
                _parent().$("#itcDlgIsolation").dialog("destroy");
				_parent().$("#itcDlgIsolation").remove();
				_parent().$("#itcDlgIsolationBtn").remove();
            }
        }/*,{
        	"name" : "确定",
            //"name" : "添加",
            "style" : "btn-success",
            "onclick" : function(){
            	var pa = _parent();
                var p = pa.window.document.getElementById("itcDlgIsolationPage").contentWindow;
                var result = p.getSelected();
                if(!result){
                	FW.error("未选择隔离方法");
                	return;
                }
                var rowArr = {};
    			var chongfuMsg = "";
    			//判断是否有重复的pointNo
    			for( var k = 0; k < result.length; k++ ){
    				var pointNo = result[k].pointNo;
    				if( rowArr[pointNo] != 1){
    					rowArr[pointNo] = 1;
    				}else{
    					chongfuMsg = chongfuMsg + " " + pointNo;
    				}
    			}
    			if( chongfuMsg != "" ){
    				//FW.error( chongfuMsg + "隔离点选择重复！" );
    				FW.error( "隔离点编号相同的只能选择一条！" );
    				return;
    			}
                
				if(opts.onParseData && typeof(opts.onParseData)=="function"){
					opts.onParseData(p.getSelected());
				}
                _parent().$("#itcDlgIsolation").dialog("destroy");
				_parent().$("#itcDlgIsolation").remove();
				_parent().$("#itcDlgIsolationBtn").remove();
            }
        }*/
    ];
	FW.dialog("init",{btnOpts:btnOpts,dlgOpts:dlgOpts});
	_parent().$("#itcDlgIsolationTree").attr("src",treeSrc);
	_parent().$("#itcDlgIsolationPage").attr("src",listSrc);
};





