var Property={//basePath框架中已有
		objs:{},

		init:function(){
			if(Property.objs.houseType=="house"){//区域楼层的form
				Property.objs["form"]=[
	   				{title : "房产名称",id : "houseName",rules : {	required : true,maxChLength : parseInt(48*2/3)}},
	   				{title:"业主姓名",id:"ownerName",
	   					rules:{
	   						maxChLength:parseInt(12*2/3)
	   					}
	   				},
	   				{title : "面积",id : "area",rules : {number : true}},
	   				{title : "单位",id : "unit",rules : {maxChLength : parseInt(48*2/3)}},
	   				{title : "权属",id : "rightOwner",rules : {maxChLength : parseInt(48*2/3)}}
	   			];
			}else if(Property.objs.houseType=="room"){//房间的form
				Property.objs["form"]=[
       				{title : "房号",id : "houseName",rules : {required : true,maxChLength : parseInt(48*2/3)}},
       				{title : "业主姓名",id:"ownerName",rules:{maxChLength:parseInt(12*2/3)}},
       				{title : "资产类型",id : "assetType",type:'label',value:'房产'},
       				{title : "面积",id : "area",rules : {number : true}},
       				{title : "单位",id: "unit",rules : {maxChLength : parseInt(48*2/3)}},
       				{title : "权属",id : "rightOwner",rules : {maxChLength : parseInt(48*2/3)}},
       				{title : "管理费(元)",id:"managementCost",rules : {number : true}},
       				{title : "室内装修",id: "decoration",linebreak:true,type:'textarea',
       					wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(4000*2/3)}
       				},
       				{title : "办公用品",id:"officeSupplies",linebreak:true,type:'textarea',
       					wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(4000*2/3)}
       				},
       				{title : "电器",id:"electricalAppliances",linebreak:true,type:'textarea',
       					wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(4000*2/3)}
       				},
       				{title : "空调",id:"airConditioner",linebreak:true,type:'textarea',
       					wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(4000*2/3)}
       				},
       				{title : "消防",id:"fireControl",linebreak:true,type:'textarea',
       					wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(4000*2/3)}
       				}
       			];
			}
			$("#base_info").ITCUI_Foldable();
			$("#form_baseinfo").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},Property.objs.form); //主卡片信息	
			Property.loadData();
		},

		getTree:function(){
			try{
				Property.objs["tree"]=window.parent.document.getElementById("propertyTree").contentWindow.$('#property_tree');
			}catch(err){
				return null;
			}
			return Property.objs.tree;
		},

		toShow:function(obj){
			if(obj.houseType=="house"){
				FW.navigate(basePath+"asset/property/listPage.do?propertyId="+obj.houseId);
			}else if(obj.houseType=="room"){
				if(Property.objs.houseType=="room"){//已经是房间了，直接换数据
					$.ajax({
						url : basePath + "asset/property/getDetail.do?propertyId=" + obj.houseId,
						type:'POST',
						dataType:"json",
						success : function(data){
							Property.objs["propertyBean"]=data.propertyBean;
							Property.objs["propertyId"]=Property.objs.propertyBean.houseId;
							Property.objs["houseType"]=Property.objs.propertyBean.houseType;
							Property.objs["mode"]="view";
							Property.loadData();
						}
					});
				}else if(Property.objs.houseType=="house"){//要重新初始化表单
					FW.navigate(basePath+"asset/property/detailPage.do?mode=view&houseType="+obj.houseType+"&propertyId="+obj.houseId);
				}
			}
		},
		
		toCreate:function(type){
			
		},
		
		toEdit:function(){
			Property.objs["mode"]="edit";
			Property.changeShow();
		},

		toDelete:function(){
			if(Property.objs.propertyBean.isRoot=="Y"){
				//根节点不可删除
				FW.error("根节点不可删除");
				return;
			}
			Notice.confirm("确认删除|是否确定要删除房产“"+Property.objs.propertyBean.houseName+"”及其所有子房产？该操作无法撤销。",function(){Property.delProperty();});
		},
		
		toBack:function(){
			if(Property.objs.houseType=="house"){
				Property.toNode(Property.objs.propertyId);//只可能是新建和编辑状态下
			}else if(Property.objs.houseType=="room"){
				if(Property.objs.mode=="view"){
					Property.toParent();
				}else if(Property.objs.mode=="create"){
					Property.toNode(Property.objs.propertyId);
				}else if(Property.objs.mode=="edit"){
					Property.objs["mode"]="view";
					Property.loadData();
				}
			}
		},
		
		toParent:function(){
			var tree=Property.getTree();
			var parent=tree.tree('find', Property.objs.propertyBean.parentId);
			tree.tree('select',parent.target);
		},
		
		toNode:function(propertyId){
			var tree=Property.getTree();
			var node=tree.tree('find', propertyId);
			tree.tree('select',node.target);
		},
		
		changeShow:function(){//切换模式
			var titleName=FW.getEnumMap("AST_HOUSE_TYPE")[Property.objs.houseType];
			if(Property.objs.mode=="view"){
				$("#pageTitle").html("房产卡片——"+titleName);
				$("#form_baseinfo").ITC_Form("beginedit");//临时使用，解决空字段在点击其他节点后不隐藏的问题
				$("#form_baseinfo").ITC_Form("readonly");
				
				$("#btnEdit,#btnNew,#btnBack").show();
				$("#btnCreate,#btnSave,#btnDel").hide();
			}else if(Property.objs.mode=="create"){
				$("#pageTitle").html("新建"+titleName);
				$("#form_baseinfo").ITC_Form("beginedit");
				
				$("#btnCreate,#btnBack").show();
				$("#btnEdit,#btnNew,#btnDel,#btnSave").hide();
			}else if(Property.objs.mode=="edit"){
				$("#pageTitle").html("编辑"+titleName);
				$("#form_baseinfo").ITC_Form("beginedit");

				$("#btnSave,#btnBack,#btnDel").show();
				$("#btnCreate,#btnEdit,#btnNew").hide();
			}
		},
		
		loadData:function(){//加载数据
			if(Property.objs.mode=="view"||Property.objs.mode=="edit"){
				$("#form_baseinfo").ITC_Form("loaddata",Property.objs.propertyBean);
			}else if(Property.objs.mode=="create"){
				if(!Property.objs.blankBean){
					//用于新建时填充表单的
					if(Property.objs.houseType=="house"){//区域楼层的form
						Property.objs["blankBean"]={
								houseName:"",	
								ownerName:"",
								rightOwner:"",
								area:"",
								unit:""
						};
					}else if(Property.objs.houseType=="room"){//房间的form
						Property.objs["blankBean"]={
								houseName:"",	
								ownerName:"",
								rightOwner:"",
								area:"",
								unit:"",
								managementCost:"",
								decoration:"",
								officeSupplies:"",
								electricalAppliances:"",
								airConditioner:"",
								fireControl:""
						};
					}
				}
				/**加载空数据**/
				$("#form_baseinfo").ITC_Form("loaddata",Property.objs.blankBean);
			}
			Property.changeShow();
		},

		createProperty:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			var obj = $("#form_baseinfo").iForm("getVal");
			obj.parentId=Property.objs.propertyId;
			obj.houseType=Property.objs.houseType;
			
			/*var rows=$("#assetSpare_table").datagrid("getRows");
			var addSpares=new Array();
			for(var i=0;i<rows.length;i++){
				addSpares.push(rows[i]);
			}*/

			$.ajax({
				type : "POST",
				url: basePath+"asset/property/insertProperty.do",
				data: {"propertyBean":FW.stringify(obj)},
				dataType : "json",
				success : function(data) {				
					if(data.status==1){
						FW.success("房产创建成功");
						Property.objs["propertyBean"]=data.propertyBean;
						
						var tree=Property.getTree();
						var node = tree.tree('getSelected');
						if (node){
							if(node.state=="open"){
								tree.tree('append', {
									parent: node.target,
									data: window.parent.document.getElementById("propertyTree").contentWindow.PropertyTree.changeToTreeNode(Property.objs.propertyBean)
									//data:function(){return {children:[Property.objs.propertyBean]};}
								});
								var newNode = tree.tree('find', Property.objs.propertyBean.houseId);
								tree.tree('select', newNode.target);
							}else{
								Property.objs["waitingExpandNode"]=node;
								tree.tree('expand',node.target);
								tree.tree('options')
									.onExpand=function(node){
										if(Property.objs.waitingExpandNode==node){
											delete Property.objs.waitingExpandNode;
											var tree=Property.getTree();
											var newNode = tree.tree('find', Property.objs.propertyBean.houseId);
											tree.tree('select', newNode.target);
										}
									};
							}
						}
					}else{
						FW.error("房产创建失败");
					}
				}
			});
		},
		
		updateProperty:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			
			var params = $("#form_baseinfo").iForm("getVal");
			//补充缺少的参数
			params.houseId=Property.objs.propertyBean.houseId;
			params.parentId=Property.objs.propertyBean.parentId;
			
			/*var rows=$("#assetSpare_table").datagrid("getRows");
			//查找新增的备件
			var addArray=new Array();
			for(var i=0;i<rows.length;i++){
				var isExist=false;
				for(var j=0;j<Property.objs["spare"].length;j++){
					if(rows[i].itemid==Property.objs["spare"][j].itemid){
						isExist=true;
						break;
					}						
				}
				if(!isExist)
					addArray.push(rows[i]);
			}
			//查找删除了的备件
			var delArray=new Array();
			for(var i=0;i<Property.objs["spare"].length;i++){
				var isExist=false;
				for(var j=0;j<rows.length;j++){
					if(rows[j].itemid==Property.objs["spare"][i].itemid){
						isExist=true;
						break;
					}
				}
				if(!isExist)
					delArray.push(Property.objs["spare"][i]);
			}*/

			$.post(basePath + "asset/property/updateProperty.do",{
					"propertyBean":FW.stringify(params)
				},function(data){
				if(data.status == "1"){
					FW.success("更新成功");
					Property.objs["propertyBean"]=data.propertyBean;
					Property.toBack();
					
					var node = Property.getTree().tree('getSelected');
					if (node){
						Property.getTree().tree('update', {
							target: node.target,
							text: Property.objs.propertyBean.houseName
						});
					}
				}else{
					FW.error("更新失败");
				}
			},"json");
		},
		
		delProperty:function(){
			$.post(basePath + "asset/property/deleteProperty.do",{"propertyId":Property.objs.propertyBean.houseId},function(data){
				if(data.result == "ok"){
					FW.success("删除成功");
					var tree=Property.getTree();
					tree.tree('remove', tree.tree('getSelected').target);
					Property.toParent();
				}else if(data.result=="forbidDelRoot"){
					FW.error("删除失败"+","+data.msg);
				}else{
					FW.error("删除失败"+","+"请稍后重试或联系管理员");
				}
			},"json");
		},

/**备件信息**/
		initSpare:function(){
			$("#assetSpare_info").iFold("init");
			Property.objs["spareForm"]=[[
			    /**{field:'ck',width:10,checkbox:'true',fixed:true,hidden:true}, **/
			    {field:'itemcode',title:'系统编码',width:80,fixed:true},
			    {field:'itemname',title:'备件名称',width:80,sortable:true},
			    {field:'cusmodel',title:'型号',width:100,fixed:true},
			    {field:'stockqty',title:'库存数量',width:60,fixed:true},
			    {field:'warehouse',title:'仓库',width:120,fixed:true},
			    {field:'sparecode',title:'备件编码',width:100,sortable:true,fixed:true},
			    {field:'manufacturer',title:'生产厂家',width:100},
				{field:'delBtn',align:'center',title:' ',width:40,hidden:true,fixed:true,formatter: function(value,row,index){
						return "<img onclick='Property.delSpare(\""+row.itemid+"\",\""+row.itemname+"\")' src='"+basePath+"img/asset/btn_garbage.gif' class='btn-garbage'/>";
					}
				}
			]];

			$("#assetSpare_table").datagrid({
				columns:Property.objs["spareForm"],
				fitColumns : true,
				idField:"itemid",
				pageSize:9999,
				singleSelect:true,
				url:basePath+"asset/assetInfo/getPropertySpare.do",
				queryParams :{'assetId': function(){return Property.objs["bean"].assetId;}},
				onLoadSuccess:function(data){
					//Property.objs["spare"]=data.rows;
					Property.objs["spare"]=[];
					for(var i=0;i<data.rows.length;i++){
						Property.objs["spare"].push(data.rows[i]);
					}
					Property.changeSpareState();
				},
				onRenderFinish : function(){
					//Property.changeSpareState("read");
				},
				onDblClickRow : function(rowIndex, rowData) {
					
				},
				onClickRow:function (rowIndex, rowData){
					var listData =$("#assetSpare_table").datagrid("getSelections");
				}
			});
		},

		changeSpareState:function(state){//Property.objs["spareState"]=read/edit
			if(state){
				Property.objs["spareState"]=state;
			}
			if(!Property.objs["spareState"]){
				Property.objs["spareState"]="read";
			}

			if(!(Property.objs["spareState"]=="read"&&Property.objs["spare"]&&Property.objs["spare"].length==0)){//无数据时，控制datagrid是否显示
				$("#assetSpare_info").iFold("show");
            	$("#assetSpare_info").css({"height":"auto"});	
			}

			if(Property.objs["spareState"]=="read"){//控制添加删除按钮
				$("#spareBtn").hide();
				//$("#assetSpare_table").datagrid("hideColumn","ck");
				$("#assetSpare_table").datagrid("hideColumn","delBtn");
			}else if(Property.objs["spareState"]=="edit"){
				Property.changeAddSpareBtn(Property.objs["spare"]);
				$("#spareBtn").show();
				//$("#assetSpare_table").datagrid("showColumn","ck");
				$("#assetSpare_table").datagrid("showColumn","delBtn");
			}

			if(Property.objs["spareState"]=="read"&&Property.objs["spare"]&&Property.objs["spare"].length==0){//无数据时，控制datagrid是否显示
				$("#assetSpare_info").css({"height":0});
				$("#assetSpare_info").iFold("hide");
			}
		},
		
		addSpare:function(){
			FW.showInventoryDialog({
				onParseData:function(dataSelected){
					var rows=$("#assetSpare_table").datagrid("getRows");
					for(var i=0;i<dataSelected.length;i++){
						var data=dataSelected[i];
						var isExist=false;
						for(var i=0;i<rows.length;i++){
							if(data.itemid==rows[i].itemid){
								isExist=true;
								break;
							}
						}
						if(!isExist){
							$('#assetSpare_table').datagrid('appendRow',data);
							Property.changeAddSpareBtn(1);
						}
					}
				}
			});
		},
		
		delSpare:function(id,name){
			//Notice.confirm("确认删除|是否确定要删除备件“"+name+"”？",function(){
			$('#assetSpare_table').datagrid('deleteRow',$('#assetSpare_table').datagrid('getRowIndex',id));
			var rows=$("#assetSpare_table").datagrid("getRows");
			if(rows.length==0){
				Property.changeAddSpareBtn(0);
			}else{
				Property.changeAddSpareBtn(1);
			}
			//});
		},
		
		changeAddSpareBtn:function(num){
			if(num==0){
				$("#btnAddSpare").html("添加备件");
			}else{
				$("#btnAddSpare").html("继续添加备件");
			}
		}
};