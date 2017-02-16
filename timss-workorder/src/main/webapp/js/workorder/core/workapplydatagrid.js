var workerDelteId = 1;    				    
var	workerGridField =[[
		{field : 'delteId',title : '序号',align:'center', width:50,fixed:true, 
			formatter:function(value,row,index){
				row.delteId =workerDelteId;
				return workerDelteId++;
			}
		},
        {field : 'id',title : 'ID', hidden:true},     
  		{field : 'name',title : '姓名',  width:80,fixed:true},
  		{field : 'sex',title : '性别',align:'center', width:60,fixed:true,
			formatter:function(value){
				if(value=='F'){
					return "女";
				}else{
					return "男";
				}
			}
  		},
  		{field : 'certificate',title : '持有特种作业证', width:160},
  		{field : 'ppe',title : 'PPE情况', width:160},
  		{field : 'score',title : '安规考试成绩', width:90,fixed:true},
  		
  		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
  			 formatter:function(value,row,index){
  				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
  				     		"'workerTable',"+row.delteId+')" width="16" height="16" >';
  			}
  		}
  	]];	

var riskDelteId = 1;    				    
var	riskGridField =[[
		{field : 'delteId',title : '序号',align:'center', width:50,fixed:true,
			formatter:function(value,row,index){
				row.delteId =riskDelteId;
				var rowcolumData =riskDelteId;
				riskDelteId = riskDelteId+1;
				return rowcolumData ;
			}
		},
        {field : 'id',title : 'ID', hidden:true},     
  		{field : 'riskPoint',title : '风险点',width:100},
  		{field : 'riskSource',title : '主要风险源', width:100},
  		{field : 'safeItem',title : '所采取的控制措施', width:160},
  		{field : 'bearFlag',title : '可承受风险', align:'center',width:80,fixed:true,
  			formatter:function(value){
				if(value=='true'){
					return "&radic;";
				}else{
					return "&times;";
				}
			}
  		},
  		{field : 'remarks',title : '备注', width:100},
  		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
  			 formatter:function(value,row,index){
  				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
  				     		"'riskTable',"+row.delteId+')" width="16" height="16" >';
  			}
  		}
  	]];	


function initdatagrid(){
	var workerDatagrid =  $("#workerTable").datagrid({
	    columns:workerGridField,
	    idField:'delteId',
	    singleSelect:true,
	    fitColumns:true,
	    nowrap : false,
	    scrollbarSize:0
	}); 
	$('#workerTable').datagrid({
		onDblClickRow: function(index, row){
			openAddDialog(row,"worker");
		}
	});
	
	var riskDatagrid =  $("#riskTable").datagrid({
		    columns:riskGridField,
		    idField:'delteId',
		    singleSelect:true,
		    fitColumns:true,
		    nowrap : false,
		    scrollbarSize:0
		     
		});
	$('#riskTable').datagrid({
		onDblClickRow: function(index, row){
			openAddDialog(row,"risk");
		}
	});
	
	$("#title_safeInform").iFold("init");
	$("#title_worker").iFold("init");
	$("#title_risk").iFold("init");

}
/**
 * @returns {Boolean}判断datagrid双击的弹出框是否可编辑
 */
function isDialogEditable(){
	var dlgEditable = false;
	if(applyStatus=="" ||applyStatus=="draft"||(operPriv && (applyStatus=="txkgsq"))){
		  dlgEditable = true;
	  }
	return dlgEditable;
}
function openAddDialog(rowData,type){
	  var dlgEditable = isDialogEditable();
	  var src = basePath + "page/workorder/core/workapply/";
	  var rowDataVarFlag = null;
	  var dlgTitle = null;
	  var dlgHeight = 350;
	  switch(type){
	  case "risk": 
		  src = src+"addRisk.jsp?editFlag="+dlgEditable;
		  rowDataVarFlag = "rowRiskData";
		  dlgTitle = "风险信息";
		  dlgHeight = 390;
		  break;
	  case "worker":
		  src = src+"addWorker.jsp?editFlag="+dlgEditable;
		  rowDataVarFlag = "rowWorkerData";
		  dlgTitle ="人员信息" ;
		  dlgHeight = 300 ;
		  break;
	  }
	  if(rowData != null){
		  FW.set(rowDataVarFlag, rowData); //设置全局变量,使其在明细页面获取
	  }
	  var btnOpts1 = [{"name" : "关闭",
          "onclick" : function(){
              return true;
             }
      	}];
	  
	    var btnOpts2 = [{"name" : "关闭",
			            "onclick" : function(){
			                return true;
			               }
			        	},
			           {
			            "name" : "确定",
			            "style" : "btn-success",
			            "onclick" : function(){
			                //itcDlgContent是对话框默认iframe的id
			                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
			                if(!p.$("#"+type+"Form").valid()){
			            		return ;
			            	}
			                var formData = p.$("#"+type+"Form").iForm("getVal");
			                if(formData.delteId){
			                	deleteGridRow(type+"Table",formData.delteId);
			                }
			                $("#"+type+"Table").datagrid("appendRow",formData );
			                return true;
			            }
			           }];
	    var dlgOpts = {width : 600,height:dlgHeight, title:dlgTitle};
	    if(dlgEditable){
	    	 FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts2}); 
	    }else{
	    	 FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts1}); 
	    }
}



function fillDataGrid(woapplyData){
	//外来队伍施工人员赋值
	var workerDataObj = eval("(" + woapplyData.worker + ")");
	if(workerDataObj.length!=0){
		$("#workerTable").datagrid("loadData",workerDataObj);
		$("#workerTable").datagrid("hideColumn","oper"); //隐藏某一列
		$("#btn_workerTable").hide();
	}else{$("#title_worker").iFold("hide");}
	//风险评估赋值
	var riskDataObj = eval("(" + woapplyData.risk + ")");
	if(riskDataObj.length!=0){
		$("#riskTable").datagrid("loadData",riskDataObj);
		$("#riskTable").datagrid("hideColumn","oper"); //隐藏某一列
		$("#btn_riskTable").hide();
	}else{$("#title_risk").iFold("hide");}
}



/** 删除dataGrid中的某一行数据
 * @param dataGridId
 * @param index
 */
function deleteGridRow(dataGridId,deleteId){
	 $('#'+dataGridId).datagrid('deleteRow', $('#'+dataGridId).datagrid('getRowIndex',deleteId));
	 var rowsLength = $("#"+dataGridId).datagrid('getRows').length;
	 
	 endEdit(dataGridId)
	 var tempdata = $("#"+dataGridId).datagrid('getRows');
	 if(dataGridId == "workerTable"){
		 workerDelteId = 1;
	 }else{
		 riskDelteId = 1;
	 }
	 $("#"+dataGridId).datagrid('loadData',{total:rowsLength,rows:tempdata});
	 beginEdit(dataGridId);
 }

