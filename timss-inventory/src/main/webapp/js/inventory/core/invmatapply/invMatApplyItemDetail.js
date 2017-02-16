var form = [
	{title : "领料单", id : "sheetno"},
	{title : "物资编码", id : "itemcode"},
	{title : "物资名称", id : "itemname"},
	{title : "型号", id : "cusmodel"},
	{title : "领用人", id : "borrowUserName"}
];

var recipientsDetailListColumns = [[
                        {field:'imrdid',title:'发料物资详情ID',width:200,fixed:true,hidden:true},
                     	{field:'createdate',title:'发料日期',width:120,fixed:true,
                        	formatter: function(value,row,index){
								return FW.long2time(value);
							}
                        },
                     	{field:'sheetno',title:'发料单号',width:120,fixed:true},
                     	{field:'outstockqty',title:'发料数量',width:80,fixed:true,align:'right'},
                     	{field:'blank',title:'',width:200}
                     ]];

var assetListColumns = [[
                       	{field:'assetId', title:'资产ID', width:200, fixed:true, hidden:true},
                      	{field:'createdate',title:'资产化日期',width:120,fixed:true,
                       		formatter: function(value,row,index){
								return FW.long2time(value);
							}
                       	},
                     	{field:'assetCode',title:'资产编码',width:200,fixed:true,
                       		formatter:function(value,row){
                       			if(!!value){
                        			return "<a onclick='openAssetTab(\""+row.assetId+"\");'>"+row.assetCode+"</a>";
                       			}
            				}
                       	},
                     	{field:'createuser',title:'操作人',width:120}
                     ]];

//编辑表单加载数据（通用方法）
function initForm(){
	$("#autoform").iForm("init",{"fields":form,"options":{validate:true,labelFixWidth:140,xsWidth:6}});
	$("#recipientsDetailListTable").datagrid({
		singleSelect:true,
		fitColumns : true,
		columns:recipientsDetailListColumns 
	});
	$("#assetListTable").datagrid({
		singleSelect:true,
		fitColumns : true,
		columns:assetListColumns 
	});
	
	$("#autoform").iForm("setVal",rowData);
	$("#autoform").iForm("endEdit",['sheetno','itemcode','itemname','cusmodel','borrowUserName']);
	
	$("#recipientsDetailListDiv").iFold("init");
	if(recipientsDetailListData.length!=0){
		$("#recipientsDetailListTable").datagrid("loadData",recipientsDetailListData);
	}else{
		$("#recipientsDetailListDiv").iFold("hide");
	}
}

function openAssetTab(assetId){
	_parent().$("#itcDlgItemDetailBtn_0 button").click();
	//_parent().$("#itcDlg").dialog("close");
    FW.addTabWithTree({
        id : "equipment_" + assetId,
        url : basePath+ "asset/assetInfo/assetDetail.do?mode=view&assetId=" + assetId,
        name : "资产卡片",
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('$arg');FW.activeTabById('editMatApplyForm"+imaid+"');"
        }
    });
}