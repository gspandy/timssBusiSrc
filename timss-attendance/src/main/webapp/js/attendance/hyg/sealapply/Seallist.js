$(document).ready(function(){
	setButtonPriv();
	PageList.init({
		datagrid:{
			id:"sealApplyList",//required
			detailPage:{
				url : basePath+"attendance/sealApply/detailPage.do?mode=view&saId=",//required
				createUrl : basePath+"attendance/sealApply/detailPage.do?mode=create",//required
				idPrefix : "sealApplyDetail",//详情页面id前缀，后面加_详情项id
				namePrefix : "用章申请"//详情页面名称前缀，后面加详情项名称
			},
			params:{
				idField:"saId",//打开详情页面的id，放在detailPage.url的后面
				columns:[[
					{field : 'saId', title : 'saId', hidden : true},
					{field : 'saNo', title : '申请单号', width : 140, sortable : true, fixed : true},
					{field : 'createUserName', title : '申请人', width : 80, sortable : true, fixed : true},
					{field : 'deptName', title : '申请部门', width : 100, sortable : true, fixed : true},
					{field : 'title', title : '文件标题', width : 250, sortable : true},
					{field : 'sendCompany', title : '发往单位', width : 220, fixed : true},
					{field : 'createdate', title : '申请日期', width : 100, sortable : true, fixed : true, 
					 	formatter : function(val){
					 		return FW.long2date(val);
					 	}
					 },
					{field:'statusName',title:'状态',width:150,fixed:true,sortable : true
						 /*formatter:function(val){
							return FW.getEnumMap("ATD_SEALAPPLY_STATUS")[val];
						},
						editor : {
							"type":"combobox",
							"options" : {
								"data" : FW.parseEnumData("ATD_SEALAPPLY_STATUS",_enum)
							}
						}*/		//海运站点列表页面查询时不需要下拉框					
					}
				]],
				url:basePath+"attendance/sealApply/getList.do"//required
			},
			onRenderFinish:function(){
				datagrid.datagrid("resize");
	        },
	        onLoadSuccess:function(){
				datagrid.datagrid("resize");
	        }			
		}
	});
});


function setButtonPriv(){
	Priv.map("privMapping.atd_sa_new","atd_sa_new");
	Priv.apply();
	FW.fixRoundButtons("#toolbar");	
}