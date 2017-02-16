<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>两票审批人员配置列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var siteId = ItcMvcService.getUser().siteId;
var headSearchParams = {};
var stepMap = ${stepMap};
var stepEnumMap = ${stepEnumMap};

//var sptwtypeList = FW.parseEnumData("ptw_standard_type",_enum);
var ptwtypeList = FW.parseEnumData("PTW_MAJOR",_enum);
var ptotypeList = FW.parseEnumData("PTW_SPTO_TYPE",_enum);
/*for(var i=0; i<sptwtypeList.length ; i++){
	sptwtypeList[i][1]=sptwtypeList[i][1]+"(标准工作票)";
}
*/
for(var i=0; i<ptwtypeList.length ; i++){
	ptwtypeList[i][1]=ptwtypeList[i][1]+"(工作票)";
}
for(var i=0; i<ptotypeList.length ; i++){
	ptotypeList[i][1]=ptotypeList[i][1]+"(操作票)";
}
var typeMap = ptotypeList.concat(ptwtypeList);//concat(sptwtypeList);
var typeEnumMap = FW.getEnumMap("PTW_SPTO_TYPE");
/*
var typeEnumMapList = FW.split(FW.joinKey(FW.getEnumMap("ptw_standard_type")),",");
for(var i=0; i<typeEnumMapList.length ;i++){
	typeEnumMap[typeEnumMapList[i]]=FW.getEnumMap("ptw_standard_type")[typeEnumMapList[i]];
}
*/
var typeEnumMapList2 = FW.split(FW.joinKey(FW.getEnumMap("PTW_MAJOR")),",");
for(var i=0; i<typeEnumMapList2.length ;i++){
	typeEnumMap[typeEnumMapList2[i]]=FW.getEnumMap("PTW_MAJOR")[typeEnumMapList2[i]];
}
var isSearchMode = false;
function initDataGrid(){
	dataGrid = $("#user_table").iDatagrid("init",{
		pageSize:pageSize,//pageSize为全局变量
	    singleSelect:true,
	    url: basePath + "ptw/userInfo/userListData.do",	//basePath为全局变量，自动获取的       
	    columns:[[ 
			{field:"category",title:"类别",width:120,fixed:true,sortable:true,
				formatter: function(value,row,index){
					return FW.getEnumMap("PTW_TYPE")[value];
				},
				"editor" : {
					"type":"combobox",
					"options" : {
						"data" : FW.parseEnumData("PTW_TYPE",_enum)
					}
				}	
			}, 
			{field:"type",title:"类型",width:180,fixed:true,sortable:true,
				formatter: function(value,row,index){
					return typeEnumMap[value];
				},
				"editor" : {
					"type":"combobox",
					"options" : {
						"data" : typeMap
					}
				}	
			}, 
			{field:"stepName",title:"环节",width:1,sortable:true,
				"editor" : {
					"type":"combobox",
					"options" : {
						"data" : stepMap
					}
				}	
			}, 
			{field:"beginDate",title:"时效开始日期",width:120,fixed:true,sortable:true,
				formatter: function(value,row,index){
					return FW.long2date(value);
				}	
			},
			{field:"endDate",title:"时效结束日期",width:120,fixed:true,sortable:true,
				formatter: function(value,row,index){
					return FW.long2date(value);
				}	
			}
		]],
	    onLoadSuccess: function(data){
		    //因为查询框使用了combo，发送了多次查请，会导致isSearchMode在后几次查请被值为false
	    	if("block"==$("#itc_gs_user_table").css("display")||isSearchMode){
				if(data && data.total==0){
			    	$("#noSearchResult").show();
			    }else{
			        $("#noSearchResult").hide();
			 	}
			}else{
			    $("#noSearchResult").hide();
		        if(data && data.total==0){
		            $("#mainContent").hide();
		            $("#grid1_empty").show();
		        }else{
		            $("#mainContent").show();
		            $("#grid1_empty").hide();
		        }
			}
			isSearchMode = false;
	    },
	    onDblClickRow : function(rowIndex, rowData) {
	    	toUserBaseInfoPage(rowData);
		}
	});
}
	
$(document).ready(function() {
	initDataGrid();
	//表头搜索相关的
	$("#btn_advSearch").click(function(){
		if($(this).hasClass("active")){
			$("#user_table").iDatagrid("endSearch");
		}else{
			isSearchMode = true;
		    $("#user_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				//强烈建议转为一个string传给后台，方便转Bean或hashMap
				return {"search":JSON.stringify(args)};
			}});
		    //initOnChangeFunc--begin
			var selectors = $(".itc_gridsearch").find("select");
			var s_category = null;
			var s_type = null;
			var s_step = null;
			for(var i=0;i<selectors.length;i++){
				var sl = $(selectors[i]);
				if(sl.attr("field")=="category"){
					s_category = $(selectors[i]);
				}else if(sl.attr("field")=="type"){
					s_type = $(selectors[i]);
				}else if(sl.attr("field")=="stepName"){
					s_step = $(selectors[i]);
				}
            }
			var catagoryArray = FW.parseEnumData("PTW_TYPE",_enum);
			catagoryArray.unshift(["null","全部",true]);
			s_category.iCombo("init",{
				data :catagoryArray,
            	onChange : function(val){
            		if("pto"==val||"spto"==val){
            			var array = FW.parseEnumData("PTW_SPTO_TYPE",_enum);
            			array.unshift(["null","全部",true]);
            			s_type.iCombo("init",{data:array,onChange:function(){$(".itcui_input_icon")[0].click();refStepComboBox(s_category,s_type,s_step);}});
            		}else if("ptw"==val||"sptw"==val){
            			var array = FW.parseEnumData("PTW_MAJOR",_enum);
            			array.unshift(["null","全部",true]);
            			s_type.iCombo("init",{data:array,onChange:function(){$(".itcui_input_icon")[0].click();refStepComboBox(s_category,s_type,s_step);}});
            		}/*else if("sptw"==val){
            			var array = FW.parseEnumData("ptw_standard_type",_enum);
            			array.unshift(["null","全部",true]);
            			s_type.iCombo("init",{data:array,onChange:function(){$(".itcui_input_icon")[0].click();refStepComboBox(s_category,s_type,s_step);}});
            		}*/
            		else{
            			var array = typeMap.slice();
            			array.unshift(["null","全部",true]);
            			s_type.iCombo("init",{data:array,onChange:function(){$(".itcui_input_icon")[0].click();refStepComboBox(s_category,s_type,s_step);}});	
            		}
            	}
            });
			//initOnChangeFunc--end
		}
	});
});

/**
 * 打开新建审批配置信息标签页
 */
function newUserConfig(){
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var tabId = "initNewUser" + rand;
    var urlPath = basePath+ "ptw/userInfo/newUserInfo.do";  
    var opts = {
        id : tabId,
        name : "新建两票审批人员配置信息",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refresh();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
};

/**
 * 打开编辑审批配置信息标签页
 */
function toUserBaseInfoPage(rowData){
	var id = rowData.id;
	var status = rowData.status;
	  //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var opts = {
        id : "ptwUser" + rand,
        name : "审批人员配置",
        url : basePath+ "ptw/userInfo/editUserInfo.do?configId="+id,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('ptw');FW.getFrame('ptw').refCurPage();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
//刷新当前页
function refCurPage(){
 	$("#user_table").datagrid("reload");
}
//refreshStepComboBox
function refStepComboBox(s_category,s_type,s_step){
	var category =  $(s_category).iCombo("getVal");
	var type = $(s_type).iCombo("getVal");
	$.ajax({
		url : basePath + "ptw/userInfo/getStepListWithCon.do",
		type : "POST",
		data : {category:category,type:type},
		dataType:"json",
		success : function(data){
			var array = data.result;
			array.unshift(["null","全部",true]);
			s_step.iCombo("init",{data:array,onChange:function(){$(".itcui_input_icon")[0].click();}});			
		}
	});
}
/**
 * 刷新数据方法
 */
function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
    delete(_itc_grids["user_table"]);
    var pager = $("#user_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
</script>
</head>
<body style="height: 100%;min-width:850px" class="bbox list-page">

	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-success" onclick="newUserConfig()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">     
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="user_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px">
		</div>
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有审批人员配置信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		        	<button type="button" class="btn btn-success" onclick="newUserConfig();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>