function getAsset(){
	return Asset;
}
//手动添加转菊花
function showAssetMask(mskName){
	_parent().$("#"+mskName).remove();
	var a=_parent().$("#mainframe_content"),
	b="<div id= '"+mskName+"' style='width: 100%;height: 100%;left: 0;z-index: 2333; background-color: #FFF;display: table;position: absolute;top: 0;vertical-align: middle;'><span style='display: table-cell;vertical-align:middle'><span class='loading-juhua'></span></span></div>";
	a.append(b),adjustMask(mskName)
}
//手动去除转菊花
function removeAssetMask(mskName){
	$("body").trigger("loadingMaskRemoved"),_parent().$("#"+mskName).fadeOut("normal",function(){_parent().$("#"+mskName).remove()})
}
function adjustMask(mskName){var a=_parent().$("#mainframe_content"),b=a.css("padding-left");if(b){var c=_parent().$("#"+mskName);if(c.length>0){var d=c.parent().width();c.css({left:b,width:d})}}}
var Asset={//basePath框架中已有
		objs:{
			src:null,
			initParams:null,
			pageTitle:"资产卡片",
			opts:{},
			blankBean:{//用于新建时填充表单的
				assetName:"",	
				assetCode:"",
				locationName:"",
				assetType:"production",
				spec:"",
				description:"",
				produceDate:null,
				installDate:null,
				companyNo:"",
				manufacturer:"",
				purchasePrice:"",
				purchaseDate:null,
				poexcuteId:"",
				lifeTime:"",
				modelDesc:"",
				cuModel:"",
				poId:"",
				imadId:"",
				allowBorrow:"Y",
				forbidDelete:null,
				forbidMove:null,
				forbidUpdate:null
			},
			intForm:[//资产卡片主表信息
				{title : '资产名称',id : 'assetName',rules : {	required : true,maxChLength : parseInt(100*2/3)}},
				{title:'资产编码',id:'assetCode',
					rules:{
						maxChLength:parseInt(30*2/3),
						remote:{'url': basePath + 'asset/assetInfo/isAssetCodeExist.do',
							type:'post',
							data:{assetCode : function(){return $('#f_assetCode').val();},mode : function(){return Asset.objs.mode;},assetId : function(){return Asset.objs['bean'].assetId;}}
						}
					}
				},
				{title : '父资产',id : 'locationName',type:'label'},
				{title : "是否允许领用", id : "allowBorrow",rules : {required:true},type : "radio",
		             data : [
		                 ["Y","是",true],
		                 ["N","否"]
		             ]
		        },
		        {title : '关联主项目', id: 'itemCode', type:'text', 
		        	render: function(id){
		        		$("#" + id).attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
		    	        $("#" + id).next(".itcui_input_icon").on("click",function(){
		    				FW.dialog("init",{
		    					src: basePath+"asset/assetInfo/getInvItemListPage.do",
		    					btnOpts:[{
		    				            "name" : "取消",
		    				            "float" : "right",
		    				            "style" : "btn-default",
		    				            "onclick" : function(){
		    				                _parent().$("#itcDlg").dialog("close");
		    				             }
		    				        },
		    				        {
		    				            "name" : "确定",
		    				            "float" : "right",
		    				            "style" : "btn-success",
		    				            "onclick" : function(){
		    				                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		    				                var info = p.getItemMessage();
		    				                var datas = info.split("||"); 
		    			                	if(null != info && "" != info){
		    				                	$("#form_baseinfo").iForm("setVal",{"itemCode":datas[0],"itemName" :datas[1],"itemId" :datas[2]});
		    	                            }
		    	                            _parent().$("#itcDlg").dialog("close"); 
		    				            }
		    				        }],
		    					dlgOpts:{ width:800, height:457, closed:false, title:"查询关联主项目", modal:true }
		    					});
		    			 });
		        }},
		        {title : '主项目名称', id: 'itemName', type:'text'},
				{title : '备注',id : 'description',type:'textarea',linebreak:true,
					wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(200*2/3)}},
				{title : '资产ID',id: 'assetId',type:'hidden'},
				{title : '状态',id: 'status',type:'hidden'},
				{title : '站点ID',id: 'site',type:'hidden'},
				{title : '父级ID',id: 'parentId',type:'hidden'},
				{title : '供应商信息',id: 'companyNo',type:'hidden'},
				{title : '制造商',id: 'manufacturer',type:'hidden'},
				{title : '订购单信息',id: 'poexcuteId',type:'hidden'},
				{title : '采购价格',id: 'purchasePrice',type:'hidden'},
				{title : '使用年限',id: 'lifeTime',type:'hidden'},
				{title : '生产日期',id: 'produceDate',type:'hidden'},
				{title : '采购日期',id: 'purchaseDate',type:'hidden'},
				{title : '原值',id: 'originalValue',type:'hidden'},
				{title : '年累计成本',id: 'ytdCost',type:'hidden'},
				{title : '预算名称',id: 'budgets',type:'hidden'},
				{title : '安装日期',id: 'installDate',type:'hidden'},
				{title : '资产序列号',id: 'serialNum',type:'hidden'},
				{title : '根的标识',id: 'isRoot',type:'hidden'},
				{title : '隔离点',id: 'isOnlyPoint',type:'hidden'},
				{title : '参数详细说明',id: 'modelDesc',type:'hidden'},
				{title : '设备型号',id: 'cuModel',type:'hidden'},
				{title : '资产类型',id: 'assetType',type:'hidden'},
				{title : '专业',id: 'spec',type:'hidden'},
				{title : '技术规范',id: 'attachments',type:'hidden'},
				{title : '位置',id: 'position',type:'hidden'},
				{title : '采购单id',id: 'poId',type:'hidden'},
				{title : '出库项id',id: 'imadId',type:'hidden'},
				{title : '禁止删除',id: 'forbidDelete',type:'hidden'},
				{title : '禁止移动',id: 'forbidMove',type:'hidden'},
				{title : '禁止更新',id: 'forbidUpdate',type:'hidden'},
				{title : '字表ID',id: 'imtdId',type:'hidden'},
				{title : '货物ID',id: 'itemId',type:'hidden'},
				{title : '固定资产申请编码', id:'astApplyId' ,type:'hidden'}
			]
		},

		/**预留自定义接口**/
		site:{
			init:function(){//站点初始化时，在其他初始化操作完成之后，页面状态变更之前调用，进行站点自定义初始化
			},
			changeViewMode:function(){//切换为只读的操作
			},
			changeCreateMode:function(){//切换为新建的操作
			},
			changeEditMode:function(){//切换为编辑的操作
			},
			loadData:function(){//刷新数据的操作
			},
			createAsset:function(){//执行新建的操作
			},
			updateAsset:function(){//执行更新的操作
			},
			delAsset:function(){//执行删除的操作
			},
			valid:function(){//执行校验
				return true;
			}
		},
		
		/**初始化**/
		init:function(paramsMapName){
			//检查是否有树，没有的话新建树后再初始化
			var mskName = "asset_msk";
			if(!isTreeExist()){
				initAssetTree(function(){Asset.init(paramsMapName);});
				showAssetMask(mskName);
				return;
			}else{
				removeAssetMask(mskName);
			}
			if(paramsMapName){//是否从缓存中取参数进行初始化
				var paramsMap=FW.get(paramsMapName);
				Asset.objs.initParams=paramsMap?paramsMap:null;
			}else{
				Asset.objs.initParams=null;
			}

			Asset.initInfo();//初始化基本信息			
			Asset.initSpare();//初始化备品备件	
			Asset.initPurchase();//初始化采购信息
			
			Asset.initSpecs();//初始化技术规范
			Asset.initWorkOrder();//初始化工单
			Asset.initMaintainPlan();//初始化维护计划
			Asset.initWoQxGrid();//初始化缺陷记录
			
			Asset.site.init();
			
			Asset.changeMode();
			
			//当树没有选择节点或所选非当前节点时，选择当前bean
			if((!Asset.objs["assetTree"].tree("getSelected")||
					Asset.objs["assetTree"].tree("getSelected").id!=Asset.objs["bean"].assetId)
					/*&&(Asset.objs["mode"]!="create")*/){
				var node=Asset.objs["assetTree"].tree("find",Asset.objs["bean"].assetId);
				if(!node){//找不到节点时刷新后重找
					FW.set("AssetTreeLoadRollBackFunc",function(node,data,assetTreeRollbackFuncAttrs){
						node=Asset.objs["assetTree"].tree("find",Asset.objs["bean"].assetId);
						if(node){//如果还找不到就不管了
							node.isOnlySelect=true;
							Asset.objs["assetTree"].tree("select",node.target);
						}
					});
					Asset.objs["assetTree"].tree("reload");
				}else{
					node.isOnlySelect=true;
					Asset.objs["assetTree"].tree("select",node.target);
				}
			}
			
			AssetPriv.init();
		},
		
		showDetail:function(assetId,isOnlyRefresh){//展示详情
			if(assetId&&!isOnlyRefresh){
				Asset.objs["bean"].assetId=assetId;//展示指定的资产台账
			}
			Asset.objs["mode"]="view";
			Asset.loadData(isOnlyRefresh);
			Asset.changeMode();			
		},

		changeMode:function(mode,src){//切换模式,src为后来添加，为了区分新建还是复制（890199）
			if(mode){
				Asset.objs["mode"]=mode;
			}
			if(!Asset.objs["mode"]){
				Asset.objs["mode"]="view";//设置为默认浏览模式
			}
			if(src){
				Asset.objs["src"]=src;
			}else{
				Asset.objs["src"]=null;
			}
			
			if(Asset.objs["mode"]=="view"){
				$("#pageTitle").html(Asset.objs.pageTitle+"详情");
				$("#form_baseinfo").ITC_Form("beginedit");//临时使用，解决空字段在点击其他节点后不隐藏的问题
				$("#form_baseinfo").ITC_Form("readonly");
				
				$("#btnNew").show();
				$("#btnCopy").show();
				if(Asset.objs.bean["forbidUpdate"]=="Y"){
					$("#btnEdit").hide();
				}else{
					$("#btnEdit").show();
				}
				if(Asset.objs.bean["forbidDelete"]=="Y"){
					$("#btnDel").hide();
				}else{
					$("#btnDel").show();
				}
				$("#btnCreate,#btnSave,#btnBack").hide();

				Asset.changeSpareState("read");
				Asset.changeSpecsState("read");
				
				Asset.site.changeViewMode();
			}else if(Asset.objs["mode"]=="create"){
				$("#pageTitle").html("新建"+Asset.objs.pageTitle);
				$("#form_baseinfo").ITC_Form("beginedit");
				
				$("#btnCreate,#btnBack").show();
				$("#btnEdit,#btnNew,#btnDel,#btnSave,#btnCopy").hide();
				
				Asset.changeSpareState("edit");
				Asset.changeSpecsState("edit");
				$("#form_baseinfo").iForm('endEdit',["itemName"]);
				$("#form_baseinfo").iForm('hide',["allowBorrow"]);
				/**加载空数据**/
				if(Asset.objs.initParams&&Asset.objs.initParams["blankBean"]){//从缓存中赋值
					$("#form_baseinfo").iForm("setFullVal",$.extend({assetId:Asset.objs.bean.assetId},Asset.mergeDFData("blankBean")));
					//用完之后删除
					Asset.objs.initParams=null;
					Asset.refreshSpecs([]);
				}else if(Asset.objs["src"] && Asset.objs["src"]=='copy'){ 
					Asset.objs["bean"].assetCode=null;
					$("#form_baseinfo").iForm("setFullVal",$.extend({assetId:Asset.objs.bean.assetId},Asset.objs["bean"]));
					$("#purchase_list").iFold("hide");
				}else{
					Asset.objs["blankBean"].locationName=Asset.objs["bean"].assetName;
					$("#form_baseinfo").iForm("setFullVal",$.extend({assetId:Asset.objs.bean.assetId},Asset.objs["blankBean"]));
					Asset.refreshSpecs([]);
					$("#purchase_list").iFold("hide");
				}
				
				$("#assetSpare_table").datagrid("loadData",[]);
				$("#workOrder_info").iFold("hide");
				$("#maintainPlan_info").iFold("hide");
				
				Asset.site.changeCreateMode();
			}else if(Asset.objs["mode"]=="edit"){
				$("#pageTitle").html("编辑"+Asset.objs.pageTitle);
				$("#form_baseinfo").ITC_Form("beginedit");

				$("#btnSave,#btnBack,#btnDel").show();
				$("#btnEdit,#btnNew,#btnCopy").hide();
				
				Asset.changeSpareState("edit");
				Asset.changeSpecsState("edit");
				
				Asset.site.changeEditMode();
				if(Asset.objs.bean["site"]!="ITC"){
					$("#form_baseinfo").iForm('hide',["allowBorrow"]);
				}
				$("#form_baseinfo").iForm('endEdit',["itemName"]);
				
			}
		},
		
		mergeDFData:function(beanName){
			if(Asset.objs.initParams&&(Asset.objs.initParams[beanName]||Asset.objs.initParams["DFData"])){
				if(!Asset.objs.initParams[beanName]){
					Asset.objs.initParams[beanName]={};
				}
				//把obj的bean合并到配置中
				Asset.objs.initParams[beanName]=$.extend({},(Asset.objs[beanName]?Asset.objs[beanName]:{}),Asset.objs.initParams[beanName]);
				if(Asset.objs.initParams["DFData"]){
					$.each(Asset.objs.initParams["DFData"], function(name, value) {
						Asset.objs.initParams[beanName][Asset.getDyFormFieldByName(name)]=value;
					});
				}
				return Asset.objs.initParams[beanName];
			}
			return null;
		},
		
		getDataFromDF:function(){
			//合并动态表单获取数据
			var formOptions = {fixLabelWidth:true,validate:true,labelFixWidth:120};
			var formUrlParam = basePath + "asset/assetInfo/queryAssetForm.do";
			var formSearchParam = {"assetId":Asset.objs["bean"].assetId,"sheetId":Asset.objs["bean"].assetId,"formSwitch":"on"};
			FW.dynamicForm(FW.stringify(Asset.objs.intForm),formOptions,formSearchParam,formUrlParam);
			Asset.objs["bean"]=$("#form_baseinfo").iForm("getVal");
			Asset.objs["atch"]=Asset.objs["bean"].attachments;
		},
		
		getDyFormFieldByName:function(name){//根据字段名获取动态表单中的字段
			var objs=$("#"+formId+" div.ctrl-label");
			for(var i=0;i<objs.length;i++){
				var contents=$(objs[i]).contents();
				var str=(!contents||contents.length==0)?"":contents[contents.length-1].data;//找到字段名称
				if(str==(name+"：")&&!$(objs[i]).parent().hasClass("type-hidden")){
					return $(objs[i]).parent().attr("fieldid");
				}
			}
			return null;
		},
		
		loadData:function(isOnlyRefresh){//加载数据
			
			Asset.getDataFromDF();
			//加载采购信息
			$("#assetSpare_table").datagrid("reload");
			Asset.refreshSpecs();
			$("#workOrder_table").datagrid("reload");
			$("#maintainPlan_table").datagrid("reload");
			$("#woQx_table").datagrid("reload");
			$("#purchase_grid").datagrid("reload");
			
			Asset.site.loadData();
		},

		createAsset:function(){
			$("#btn-create").button('loading');
			if(!$("#form_baseinfo").valid()||!Asset.site.valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				$("#btn-create").button('reset');
				return;
			}
			var obj = $("#form_baseinfo").iForm("getVal");
			if(!obj["allowBorrow"]&&obj["site"]=="ITC"){
				FW.error("是否允许领用不能为空，请修改后重试");
				$("#btn-create").button('reset');
				return;
			}
			
			if("copy"==Asset.objs["src"]){
				obj.assetId=obj.parentId;
				obj.imtdId =null;
			}else{
				obj.parentId=Asset.objs["bean"].assetId;
				obj.locationName=Asset.objs["bean"].assetName;
			}
			var rows=$("#assetSpare_table").datagrid("getRows");
			var addSpares=new Array();
			for(var i=0;i<rows.length;i++){
				addSpares.push(rows[i]);
			}

			var ids=$("#form_specs").ITC_Form("getdata");
			uploadIds=ids.field3;
			$.ajax({
				type : "POST",
				url: basePath+"asset/assetInfo/insertAssetData.do",
				data: {"formData":FW.stringify(obj),"uploadIds":uploadIds,"addSpares":FW.stringify(addSpares)},
				dataType : "json",
				success : function(data) {				
					if(data.status==1){
						$("#btn-create").button('reset');
						FW.success("资产创建成功");
						Asset.objs["bean"]=data.bean;
						Asset.objs["atch"]=data.attachments;
						
						Asset.site.createAsset();
						
						Asset.showDetail(null,true);
						
						var node = Asset.objs["assetTree"].tree('getSelected');
						if(!node&&Asset.objs["bean"].parentId==Asset.objs["assetTreeRootNode"].id){
							node=Asset.objs["assetTreeRootNode"];
						}
						if (node){
							var parentNode = Asset.objs["assetTree"].tree('getParent',node.target);
							if(Asset.objs["bean"].parentId == parentNode.id){
								node = parentNode ;
							}
							if(node.state=="open"){
								Asset.objs["assetTree"].tree('append', {
									parent: node.target,
									data: {
										id: Asset.objs["bean"].assetId,
										text: Asset.objs["bean"].assetName,
										state: "open",
										type: "system",
										assetType: Asset.objs["bean"].assetType
									}
								});
								var newNode = Asset.objs["assetTree"].tree('find', Asset.objs["bean"].assetId);
								Asset.objs["assetTree"].tree('select', newNode.target);
							}else{
								Asset.objs["waitingExpandNode"]=node;
								Asset.objs["assetTree"].tree('expand',node.target);
								Asset.objs["assetTree"].tree('options')
									.onExpand=function(node){
									if(Asset.objs["waitingExpandNode"]==node){
										delete Asset.objs.waitingExpandNode;
										var newNode = Asset.objs["assetTree"].tree('find', Asset.objs["bean"].assetId);
										Asset.objs["assetTree"].tree('select', newNode.target);
									}
								};
							}
						}
					}else{
						$("#btn-create").button('reset');
						FW.error(data.msg);
					}
				}
			});
		},
		updateAsset:function(){
			var params = $("#form_baseinfo").iForm("getVal");
			if(params["forbidUpdate"]=="Y"){
				FW.error("该节点不允许修改");
				return;
			}
			if(!$("#form_baseinfo").valid()||!Asset.site.valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			if(!params["allowBorrow"]&&params["site"]=="ITC"){
				FW.error("是否允许领用不能为空，请修改后重试");
				return;
			}
			//补充缺少的参数
			params.assetId=Asset.objs["bean"].assetId;
			params.parentId=Asset.objs["bean"].parentId;
			
			var rows=$("#assetSpare_table").datagrid("getRows");
			//查找新增的备件
			var addArray=new Array();
			for(var i=0;i<rows.length;i++){
				var isExist=false;
				for(var j=0;j<Asset.objs["spare"].length;j++){
					if(rows[i].itemid==Asset.objs["spare"][j].itemid){
						isExist=true;
						break;
					}						
				}
				if(!isExist)
					addArray.push(rows[i]);
			}
			//查找删除了的备件
			var delArray=new Array();
			for(var i=0;i<Asset.objs["spare"].length;i++){
				var isExist=false;
				for(var j=0;j<rows.length;j++){
					if(rows[j].itemid==Asset.objs["spare"][i].itemid){
						isExist=true;
						break;
					}
				}
				if(!isExist)
					delArray.push(Asset.objs["spare"][i]);
			}

			var ids=$("#form_specs").ITC_Form("getdata");
			uploadIds=ids.field3;
			
			$.post(basePath + "asset/assetInfo/updateAssetInfo.do",{
					"formData":JSON.stringify(params),
					"uploadIds":uploadIds,
					"addSpares":FW.stringify(addArray),
					"delSpares":FW.stringify(delArray)
				},function(data){
				if(data.result == "ok"){
					FW.success("更新成功");
					Asset.objs["bean"]=data.bean;
					Asset.objs["atch"]=data.attachments;
					
					Asset.site.updateAsset();
					
					Asset.showDetail(null,true);

					var node = Asset.objs["assetTree"].tree('getSelected');
					if (node){
						Asset.objs["assetTree"].tree('update', {
							target: node.target,
							text: Asset.objs["bean"].assetName
						});
					}
				}else{
					FW.error(data.msg);
				}
			},"json");
		},
		
		delAsset:function(){
			var params = $("#form_baseinfo").iForm("getVal");
			if(params["forbidDelete"]=="Y"){
				FW.error("该节点不允许删除");
				return;
			}
			params.assetId=Asset.objs["bean"].assetId;
			if(params.assetId==Asset.objs["assetTreeRootNode"].id){
				//根节点不可删除
				FW.error("根节点不可删除");
				return;
			}
			params.parentId=Asset.objs["bean"].parentId;
			Notice.confirm("确认删除|是否确定要删除资产“"+FW.specialchars(params.assetName)+"”及其所有子资产？该操作无法撤销。",function(){
				$.post(basePath + "asset/assetInfo/deleteAsset.do",{"assetId":params.assetId,"parentId":params.parentId},function(data){
					if(data.result == "ok"){
						FW.success("删除成功");
						
						Asset.site.delAsset(data);
						
						Asset.objs["bean"]=data.parent;//展示其父资产
						Asset.objs["atch"]=data.attachments;
						
						Asset.showDetail(null,true);
						
						var node = Asset.objs["assetTree"].tree('getSelected');
						if (node){
							Asset.objs["assetTree"].tree('select',Asset.objs["assetTree"].tree('getParent',node.target).target);
							Asset.objs["assetTree"].tree('remove', node.target);
						}
					}else if(data.result=="forbidDelRoot"){
						FW.error("删除失败"+","+data.msg);
					}else{
						FW.error("删除失败"+","+"请稍后重试或联系管理员");
					}
				},"json");
			});	
		},

/**主卡片**/
		initInfo:function(){
			//Asset.objs["assetTree"]=window.parent.frames["assetTree"].contentWindow.$('#asset_tree');
			Asset.objs["assetTree"]=window.parent.document.getElementById("assetTree").contentWindow.$('#asset_tree');
			
			Asset.objs["assetTreeRootNode"]=Asset.objs["assetTree"].tree("getRoot");
		
			$("#base_info").ITCUI_Foldable();
			Asset.getDataFromDF();
			
			if(Asset.objs.initParams&&Asset.objs.initParams["bean"]){//从缓存中赋值
				$("#form_baseinfo").ITC_Form("loaddata",Asset.mergeDFData("bean"));
			}
		},

/**备件信息**/
		initSpare:function(){
			$("#assetSpare_info").iFold("init");
			Asset.objs["spareForm"]=[[
			    /**{field:'ck',width:10,checkbox:'true',fixed:true,hidden:true}, **/
			    {field:'itemcode',title:'系统编码',width:80,fixed:true},
			    {field:'itemname',title:'备件名称',width:80,sortable:true},
			    {field:'cusmodel',title:'型号',width:100,fixed:true},
			    {field:'stockqty',title:'库存数量',width:60,fixed:true},
			    {field:'warehouse',title:'仓库',width:120,fixed:true},
			    {field:'sparecode',title:'备件编码',width:100,sortable:true,fixed:true},
			    {field:'manufacturer',title:'生产厂家',width:100},
				{field:'delBtn',align:'center',title:' ',width:40,hidden:true,fixed:true,formatter: function(value,row,index){
						return "<img onclick='Asset.delSpare(\""+row.itemid+"\",\""+row.itemname+"\")' src='"+basePath+"img/asset/btn_garbage.gif' class='btn-garbage'/>";
					}
				}
			]];

			$("#assetSpare_table").datagrid({
				columns:Asset.objs["spareForm"],
				fitColumns : true,
				idField:"itemid",
				pageSize:9999,
				singleSelect:true,
				url:basePath+"asset/assetInfo/getAssetSpare.do",
				queryParams :{'assetId': function(){return Asset.objs["bean"].assetId;}},
				onLoadSuccess:function(data){
					//Asset.objs["spare"]=data.rows;
					Asset.objs["spare"]=[];
					for(var i=0;i<data.rows.length;i++){
						Asset.objs["spare"].push(data.rows[i]);
					}
					Asset.changeSpareState();
				},
				onRenderFinish : function(){
					//Asset.changeSpareState("read");
				},
				onDblClickRow : function(rowIndex, rowData) {
					
				},
				onClickRow:function (rowIndex, rowData){
					var listData =$("#assetSpare_table").datagrid("getSelections");
				}
			});
		},
		
/**采购信息**/
		initPurchase:function(){
			$("#purchase_list").iFold("init");
			Asset.objs["purchaseForm"]=[[
			    {field:'imtId',title:'交易表ID',width:80,fixed:true,hidden:true},
			    {field:'poId',title:'采购合同',width:80,fixed:true,hidden:true},
			    {field:'poNo',title:'合同编号',width:200,fixed:true,
			    	formatter:function(value,row){
			 			if(!!value){
			 				return "<a onclick='openPurOrderTab(\""+row.poId+"\");'>"+row.poNo+"</a>";
			 			}
			 		}
			    },
			    {field:'sheetName',title:'合同名称',width:400,fixed:true},
			    {field:'sheetNo',title:'物资接收单',width:600,fixed:true,
			 		formatter:function(value,row){
			 			if(!!value){
			 				return "<a onclick='openInvMatTranTab(\""+row.imtId+"\");'>"+row.sheetNo+"</a>";
			 			}
			 		}
			    },
			    {field:'empty',title:'',width:100}
			]];

			$("#purchase_grid").datagrid({
				columns:Asset.objs["purchaseForm"],
				fitColumns : true,
				idField:"itemid",
				singleSelect:true,
				url:basePath+"asset/assetInfo/getPurchaseList.do",
				queryParams :{'assetId': function(){return Asset.objs["bean"].assetId;}},
				onLoadSuccess:function(data){
					if(data&&data.total==0){
						$("#purchase_list").iFold("hide");
					}else{
						$("#purchase_list").iFold("show");
					}
					setTimeout(function(){
						$("#purchase_list").datagrid("resize");
					},500);
					FW.fixToolbar("#toolbar");
				} 
			});
		},

		changeSpareState:function(state){//Asset.objs["spareState"]=read/edit
			if(state){
				Asset.objs["spareState"]=state;
			}
			if(!Asset.objs["spareState"]){
				Asset.objs["spareState"]="read";
			}

			if(!(Asset.objs["spareState"]=="read"&&Asset.objs["spare"]&&Asset.objs["spare"].length==0)){//无数据时，控制datagrid是否显示
				$("#assetSpare_info").iFold("show");
            	$("#assetSpare_info").css({"height":"auto"});	
            	$("#assetSpare_table").datagrid("resize");
			}

			if(Asset.objs["spareState"]=="read"){//控制添加删除按钮
				$("#spareBtn").hide();
				//$("#assetSpare_table").datagrid("hideColumn","ck");
				$("#assetSpare_table").datagrid("hideColumn","delBtn");
			}else if(Asset.objs["spareState"]=="edit"){
				Asset.changeAddSpareBtn(Asset.objs["spare"]);
				$("#spareBtn").show();
				//$("#assetSpare_table").datagrid("showColumn","ck");
				$("#assetSpare_table").datagrid("showColumn","delBtn");
			}

			if(Asset.objs["spareState"]=="read"&&Asset.objs["spare"]&&Asset.objs["spare"].length==0){//无数据时，控制datagrid是否显示
				$("#assetSpare_info").css({"height":0});
				$("#assetSpare_info").iFold("hide");
			}
			
			//将备件信息隐藏起来
			$("#assetSpare_info").css({"height":0});
			$("#assetSpare_info").iFold("hide");
		},
		
		addSpare:function(){
			FW.showInventoryDialog({
				onParseData:function(dataSelected){
					var rows=$("#assetSpare_table").datagrid("getRows");
					//防止浅拷贝带来的比较的时候数据变化的问题，先把所有id拷出来
					var tmp=rows.concat();
					var isToResize=false;
					for(var i=0;i<dataSelected.length;i++){
						var data=dataSelected[i];
						var isExist=false;
						for(var j=0;j<tmp.length;j++){
							if(data.itemid==tmp[j].itemid){
								isExist=true;
								break;
							}
						}
						if(!isExist){
							$('#assetSpare_table').datagrid('appendRow',data);
							Asset.changeAddSpareBtn(1);
							isToResize=true;
						}
					}
					if(isToResize){
						$("#assetSpare_table").datagrid("resize");
					}
				}
			});
		},
		
		delSpare:function(id,name){
			//Notice.confirm("确认删除|是否确定要删除备件“"+name+"”？",function(){
			$('#assetSpare_table').datagrid('deleteRow',$('#assetSpare_table').datagrid('getRowIndex',id));
			var rows=$("#assetSpare_table").datagrid("getRows");
			if(rows.length==0){
				Asset.changeAddSpareBtn(0);
			}else{
				Asset.changeAddSpareBtn(1);
			}
			$("#assetSpare_table").datagrid("resize");
			//});
		},
		
		changeAddSpareBtn:function(num){
			if(num==0){
				$("#btnAddSpare").html("添加备件");
			}else{
				$("#btnAddSpare").html("继续添加备件");
			}
		},
		
/**技术规范**/
		initSpecs:function(){
			$("#specs_info").iFold("init");
			//技术规范提交附件
			var fields = [{id:"field3",title:" ",type:"fileupload",linebreak:true,wrapXsWidth:12,wrapMdWidth:12,options:{
				"uploader" : basePath+"upload?method=uploadFile&jsessionid="+Asset.objs["sessId"],
				"delFileUrl" : basePath+"upload?method=delFile&key="+Asset.objs["valKey"],
				"downloadFileUrl" : basePath+"upload?method=downloadFile",
				"swf" : basePath+"js/asset/uploadify.swf",
				"fileSizeLimit":10*1024,
				"initFiles" :  Asset.objs["atch"],
				"delFileAfterPost" : true
			}}];
			$("#form_specs").iForm('init',{"fields":fields,"options":{
				labelFixWidth:1,
				labelColon:false
			}}); //技术规范
		},

		refreshSpecs:function(data){
			if(!data)
				data=Asset.objs["atch"];
			if(!isArray(data)){
				data=FW.parse(data);
			}
			$("#form_specs").iForm("setVal",{"field3":data});
			return;
			
			//$("#form_specs").html("");
			if($("#form_specs")){//刷新展示，临时用
				var p=$("#form_specs").parent();
				p.empty();
				p.append("<form id='form_specs' class='margin-form-title margin-form-foldable'></form>");
			}
			
			fields = [{id:"field3",title:"附件",type:"fileupload",linebreak:true,wrapXsWidth:12,wrapMdWidth:12,options:{
				"uploader" : basePath+"upload?method=uploadFile&jsessionid="+Asset.objs["sessId"],
				"delFileUrl" : basePath+"upload?method=delFile&key="+Asset.objs["valKey"],
				"downloadFileUrl" : basePath+"upload?method=downloadFile",
				"swf" : basePath+"js/asset/uploadify.swf",
				"fileSizeLimit":10*1024,
				"initFiles" : data ,
				"delFileAfterPost" : true
			}}];
			$("#form_specs").iForm('init',{"fields":fields}); //技术规范
		},

		changeSpecsState:function(state){//Asset.objs["specsState"]=read/edit
			if(state){
				Asset.objs["specsState"]=state;
			}
			if(!Asset.objs["specsState"]){
				Asset.objs["specsState"]="read";
			}

			if(!(Asset.objs["specsState"]=="read"&&Asset.objs["atch"]&&(Asset.objs["atch"]=="[]"||Asset.objs["atch"].length==0))){//无数据时，控制datagrid是否显示
				$("#specs_info").iFold("show");	
			}

			if(Asset.objs["specsState"]=="read"){//控制添加删除按钮
				$("#form_specs").ITC_Form("readonly");
			}else if(Asset.objs["specsState"]=="edit"){
				$("#form_specs").ITC_Form("beginedit");
			}

			if(Asset.objs["specsState"]=="read"&&Asset.objs["atch"]&&(Asset.objs["atch"]=="[]"||Asset.objs["atch"].length==0)){//无数据时，控制datagrid是否显示
				$("#specs_info").iFold("hide");
			}
		},
		
/**工单**/		
		initWorkOrder:function(){
			$("#workOrder_info").iFold("init");
			
			Asset.objs["workOrderForm"]=[[
			    {field:'workOrderCode',title:'工单编号',width:100},
			    {field:'description',title:'工单描述',width:100},
			    {field:'createDate',title:'报告日期',width:100,formatter: function(value,row,index){
					return value==null||value==""?"":new Date(value).format("yyyy-MM-dd");
				}},
			    {field:'principal',title:'工作负责人',width:100},
			    {field:'woSpec',title:'专业',width:100},
			    {field:'endReport',title:'处理情况',width:100},
			    {field:'woStatus',title:'状态',width:100}
			]];   
			
			$("#workOrder_table").datagrid({
				columns:Asset.objs["workOrderForm"],
				fitColumns : true,
				pageSize:9999,
				singleSelect:true,
				url:basePath+"asset/assetInfo/getWorkOrder.do",
				queryParams :{'assetId': function(){return Asset.objs["bean"].assetId;}},
				onLoadSuccess:function(data){
					if(Asset.objs.mode=="create"){
						data={rows:[],total:0};//新建模式下不展示
					}
					Asset.objs["workOrder"]=data.rows;
					if(data.total==0){
						$("#workOrder_info").iFold("hide");
					}else{
						$("#workOrder_info").iFold("show");
						$("#workOrder_table").datagrid("resize");
					}
				}
			});
		},
		
/**维护计划**/
		initMaintainPlan:function(){
			$("#maintainPlan_info").iFold("init");
			
			Asset.objs["maintainPlanForm"]=[[
			    {field:'maintainPlanCode',title:'维护计划编号',width:100},
			    {field:'description',title:'计划描述',width:100},
			    {field:'workTeam',title:'负责班组',width:100},
			    {field:'principal',title:'工作负责人',width:100},
			    {field:'specialty',title:'专业',width:100},
			    {field:'nextDate',title:'下一生成日期',width:100,formatter: function(value,row,index){
					return value==null||value==""?"":new Date(value).format("yyyy-MM-dd");
				}},
			    {field:'status',title:'状态',width:100}
			]];   
			              			
			$("#maintainPlan_table").datagrid({
			    columns:Asset.objs["maintainPlanForm"],
			    fitColumns : true,
			    singleSelect:true,
			    url:basePath+"asset/assetInfo/getMaintainPlan.do",
			    queryParams :{'assetId': function(){return Asset.objs["bean"].assetId;}},
			    onLoadSuccess:function(data){
			    	if(Asset.objs.mode=="create"){
						data={rows:[],total:0};//新建模式下不展示
					}
			    	Asset.objs["maintainPlan"]=data.rows;
					if(data.total==0){
						$("#maintainPlan_info").iFold("hide");
					}else{
						$("#maintainPlan_info").iFold("show");
						$("#maintainPlan_table").datagrid("resize");
					}
			    }
			});
		},
		
		/**缺陷记录**/
		initWoQxGrid:function(){
			$("#woQx_info").iFold("init");
			
			Asset.objs["woQxForm"]=[[
										{field:"defectCode",title:"缺陷编号",width:110,fixed:true,sortable:true},
										{field:"monthCode",title:"月度编号",width:70,fixed:true,sortable:true},
										{field:"equipName",title:"设备名称",width:110}, 
										{field:"defectDes",title:"缺陷情况",width:110},
										{field:"defectTime",title:"缺陷时间",width:110,fixed:true,
											formatter: function(value,row,index){
												return FW.long2time(value);
											}
										},   
										{field:"onDutyUserName",title:"值班人员",width:70,fixed:true},
										{field:"runningUserName",title:"运行人员",width:70,fixed:true},
										{field:"instructionsUserName",title:"批示领导",width:70,fixed:true} 
                                 ]];   
			
			$("#woQx_table").datagrid({
				columns:Asset.objs["woQxForm"],
				fitColumns : true,
				singleSelect:true,
				url:basePath+"asset/assetInfo/getWoQx.do",
				queryParams :{'assetId': function(){return Asset.objs["bean"].assetId;}},
				onLoadSuccess:function(data){
					if(Asset.objs.mode=="create"){
						data={rows:[],total:0};//新建模式下不展示
					}
					Asset.objs["woQx"]=data.rows;
					if(data.total==0){
						$("#woQx_info").iFold("hide");
					}else{
						$("#woQx_info").iFold("show");
						$("#woQx_table").datagrid("resize");
					}
				},
				onDblClickRow : function(rowIndex, rowData) {
		        	woQxInfoPage(rowData);
		        }

			});
		}
};

function openInvMatTranTab(imtid){
	_parent().$("#itcDlgItemDetailBtn_0 button").click();
	//_parent().$("#itcDlg").dialog("close");
    FW.addTabWithTree({
        id : "editMatTranForm" + imtid,
		url : basePath+ "inventory/invmattran/invMatTranForm.do?imtid="+imtid+"&openType=read",
		name : "物资接收",
		tabOpt : {
            closeable : true,
			afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
        }
    });
}

function openPurOrderTab(poId){
	_parent().$("#itcDlgItemDetailBtn_0 button").click();
	//_parent().$("#itcDlg").dialog("close");
    FW.addTabWithTree({
        id :  "editOrderForm" + poId,
        url : basePath+ "purchase/purorder/purOrderForm.do?sheetId="+poId+"&type=edit",
		name : "采购合同",
		tabOpt : {
            closeable : true,
			afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
        }
    });
}

function  woQxInfoPage(rowData){
	var woQxId = rowData.id;
	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = rowData.defectCode;
   var opts = {
        id : "newQx" + rand,
        name : "缺陷详情",
        url : basePath+ "workorder/woQx/newQxPage.do?woQxId="+woQxId,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equipment');FW.getFrame('equipment').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

//点击关联主项目列表的条目，将相应的值设入文本框
function getItemMessage(){
	var itemMessage = "";
	var rowData = $("#item_grid").datagrid("getSelected");
	if( rowData == null || rowData == "" ){
		FW.error("请选择关联主项目 ");
		return;
	}else{
		info = rowData.itemCode+"||"+rowData.itemName+"||"+rowData.itemId;
	}
	return info;
}
