var SwLedger={//basePath框架中已有
		objs:{},

		init:function(){
			//SwLedger.objs.swLedgerBean.apps=[{appId:"app001",appType:"web",appName:"1",path:"2",hwlDevice:{hwId:"",hwName:"3",ip:"4"},port:"5"}];//测试数据
			
			SwLedger.objs["isInit"]=false;
			
			SwLedger.objs["form"]=[
 				{title : "系统名称",id:"swName",rules:{
 					required : true,maxChLength:parseInt(150*2/3),
					remote:{
						"url": basePath + "asset/swLedger/isNameExist.do",
						type:"post",
						data:{
							swName : function(){return $("#f_swName").val();},
							swId : function(){return SwLedger.objs.swLedgerBean?SwLedger.objs.swLedgerBean.swId:"";}
						},
						complete:function(data){
							
						}
					}
 				}},
 				{title : "所属单位",id:"attr01",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : " ",id:"principal",type:"hidden"},
 				{id:"attr02",title:"负责人",render : function(id){
 					//在实际使用时还要指定clickEvent才有意义
 					$("#" + id).initHintPersonList({
 						clickEvent : function(id,name){
 							var uid = id.split("_")[0];//[1]是用户在当前站点下对应的一个组织编号
 							$("#form_baseinfo").iForm("setVal",{principal:uid,attr02:name});
 						}
 					});
 				},rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "供应商",id:"attr03",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "投入时间",id:"toUseTime",type:"date"},
 				{title : "业务关联",id:"relatedBusiness",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
 					maxChLength:parseInt(3000*2/3)
 				}},
 				{title : "备注",id:"remarks",type:"textarea",linebreak:true,wrapXsWidth:8,wrapMdWidth:8,height:50,rules:{
 					maxChLength:parseInt(3000*2/3)
 				}}
 			];
			$("#base_info").ITCUI_Foldable();
			$("#form_baseinfo").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},SwLedger.objs.form); //主卡片信息	
			
			SwLedger.initAppsList();
			
			SwLedger.loadData();
			
			SwLedger.objs["isInit"]=true;
		},
		
		toEdit:function(){
			SwLedger.objs["mode"]="edit";
			SwLedger.changeShow();
		},

		toDelete:function(){
			Notice.confirm("确认删除|是否确定要删除软件台账“"+SwLedger.objs.swLedgerBean.swName+"”及其所有应用？该操作无法撤销。",function(){SwLedger.delSwLedger();});
		},
		toClose:function(){
			closeTab();
		},
		toBack:function(){//返回的操作
			if(SwLedger.objs.mode=="view"){
				SwLedger.toList();
			}else if(SwLedger.objs.mode=="create"){
				SwLedger.toList();
			}else if(SwLedger.objs.mode=="edit"){
				SwLedger.objs["mode"]="view";
				SwLedger.loadData();
			}
		},
		
		toList:function(){
			FW.navigate(basePath+"asset/swLedger/listPage.do");
		},
		
		changeShow:function(){//切换模式
			if(SwLedger.objs.mode=="view"){
				$("#pageTitle").html("软件台账");
				$("#form_baseinfo").ITC_Form("beginedit");//临时使用，解决空字段在点击其他节点后不隐藏的问题
				$("#form_baseinfo").ITC_Form("readonly");
				
				$("#btnEdit,#btnBack").show();
				$("#btnCreate,#btnSave,#btnDel,#btnCancel").hide();
				
				SwLedger.changeAppsListState("read");
			}else if(SwLedger.objs.mode=="create"){
				$("#pageTitle").html("新建软件台账");
				$("#form_baseinfo").ITC_Form("beginedit");
				
				$("#btnCreate,#btnBack").show();
				$("#btnEdit,#btnDel,#btnSave,#btnCancel").hide();
				
				SwLedger.changeAppsListState("edit");
			}else if(SwLedger.objs.mode=="edit"){
				$("#pageTitle").html("编辑软件台账");
				$("#form_baseinfo").ITC_Form("beginedit");

				$("#btnSave,#btnBack,#btnDel,#btnCancel").show();
				$("#btnCreate,#btnEdit").hide();
				
				SwLedger.changeAppsListState("edit");
			}
		},
		
		loadData:function(){//加载数据
			if(SwLedger.objs.mode=="view"||SwLedger.objs.mode=="edit"){
				$("#form_baseinfo").ITC_Form("loaddata",SwLedger.objs.swLedgerBean);
			}else if(SwLedger.objs.mode=="create"){
				if(!SwLedger.objs.blankBean){
					//用于新建时填充表单的
					SwLedger.objs["blankBean"]={
							swName:"",
							attr01:"",
							relatedBusiness:"",
							attr02:"",
							attr03:"",
							toUseTime:"",
							remarks:"",
							apps:[]
					};
				}
				/**加载空数据**/
				$("#form_baseinfo").ITC_Form("loaddata",SwLedger.objs.blankBean);
				SwLedger.objs.swLedgerBean=SwLedger.objs.blankBean;
			}

			SwLedger.loadApps();

			SwLedger.changeShow();
		},

		createSwLedger:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			var obj = $("#form_baseinfo").iForm("getVal");
			var apps=SwLedger.getAppsChanged();

			$.ajax({
				type : "POST",
				url: basePath+"asset/swLedger/insertSwLedger.do",
				data: {"swLedgerBean":FW.stringify(obj),
					"addApps":FW.stringify(apps.addApps),
					"updateApps":FW.stringify(apps.updateApps),
					"delApps":FW.stringify(apps.delApps)},
				dataType : "json",
				success : function(data) {				
					if(data.status==1){
						FW.success("软件台账创建成功");
						SwLedger.toList();
						return;
						SwLedger.objs["swLedgerBean"]=data.swLedgerBean;						
						//设为浏览模式并重载数据
						SwLedger.objs["mode"]="view";
						SwLedger.loadData();
					}else if(data.status == "-1"){
						FW.error("创建失败，软件台账重名");
					}else{
						FW.error("软件台账创建失败");
					}
				}
			});
		},
		
		updateSwLedger:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			
			var params = $("#form_baseinfo").iForm("getVal");
			//补充缺少的参数
			params.swId=SwLedger.objs.swLedgerBean.swId;
			var apps=SwLedger.getAppsChanged();//变更的应用

			$.post(basePath + "asset/swLedger/updateSwLedger.do",{
					"swLedgerBean":FW.stringify(params),
					"addApps":FW.stringify(apps.addApps),
					"updateApps":FW.stringify(apps.updateApps),
					"delApps":FW.stringify(apps.delApps)
				},function(data){
				if(data.status == "1"){
					FW.success("软件台账更新成功");
					SwLedger.toList();
					return;
					SwLedger.objs["swLedgerBean"]=data.swLedgerBean;
					SwLedger.toBack();
				}else if(data.status == "-1"){
					FW.error("更新失败，软件台账重名");
				}else{
					FW.error("软件台账更新失败");
				}
			},"json");
		},
		
		delSwLedger:function(){
			$.post(basePath + "asset/swLedger/deleteSwLedger.do",{"swLedgerId":SwLedger.objs.swLedgerBean.swId},function(data){
				if(data.result == "ok"){
					FW.success("删除成功");
					SwLedger.toList();
				}else{
					FW.error("删除失败"+","+"请稍后重试或联系管理员");
				}
			},"json");
		},

		initAppsList:function(){
			$("#appsList_info").iFold("init");
			SwLedger.objs["rowId"]=1;
			SwLedger.objs["appsList"]=[[
				{field:'rowId ',hidden:true,formatter:function(value,row,index){
					if(!row.rowId)
						row.rowId=SwLedger.objs.rowId++;
					return row.rowId;
				}},                        
				{field:'appType',title:'类型',width:60,fixed:true,formatter:function(value,row,index){
					return FW.getEnumMap("AST_SW_APP_TYPE")[value];
				}},
				{field:'appName',title:'名称',width:100},
			    {field:'path',title:'路径',width:150},
			    {field:'hwlDevice',title:'所在硬件台账',width:100,formatter:function(value,row,index){
					return row.hwlDevice?FW.specialchars(row.hwlDevice.hwName):"";
				}},
			    {field:'ip',title:'IP',width:120,fixed:true,formatter:function(value,row,index){
					return row.hwlDevice?row.hwlDevice.ip:"";
				}},
			    {field:'port',title:'端口号',width:50,fixed:true},
			    {field:'delBtn',align:'center',title:' ',width:40,hidden:true,fixed:true,formatter: function(value,row,index){
					return "<img onclick=\"SwLedger.delApp('" + row.rowId + "','"+FW.specialchars(row.appName)+"')\" src='"+basePath+"img/asset/btn_garbage.gif' class='btn-garbage'/>";
				}}
			]];   
			              			
			$("#appsList_table").datagrid({
			    columns:SwLedger.objs.appsList,
			    idField:"rowId",
			    fitColumns : true,
			    singleSelect:true,
			    onLoadSuccess:function(data){
					SwLedger.changeAppsListState();
			    },
			    onDblClickRow:function(index,row){
			    	if(SwLedger.objs.mode=="view"){
			    		SwLedger.appDialog("view", row.rowId);
			    	}else{
			    		SwLedger.appDialog("edit", row.rowId);
			    	}
			    }
			});
		},

		changeAppsListState:function(state){//SwLedger.objs["appsListState"]=read/edit
			if(state){
				SwLedger.objs["appsListState"]=state;
			}
			if(!SwLedger.objs["appsListState"]){
				SwLedger.objs["appsListState"]="read";
			}

			if(SwLedger.objs["appsListState"]=="read"&&SwLedger.objs.swLedgerBean.apps&&SwLedger.objs.swLedgerBean.apps.length==0){//无数据时，控制datagrid是否显示
				$("#appsList_info").css({"height":0});
				$("#appsList_info").iFold("hide");
				return;
			}else{
				$("#appsList_info").iFold("show");
            	$("#appsList_info").css({"height":"auto"});	
			}

			if(SwLedger.objs["appsListState"]=="read"){//控制添加删除按钮
				$("#appBtn").hide();
				$("#appsList_table").datagrid("hideColumn","delBtn");
			}else if(SwLedger.objs["appsListState"]=="edit"){
				SwLedger.changeAddAppBtn(SwLedger.objs.swLedgerBean.apps.length);
				$("#appBtn").show();
				$("#appsList_table").datagrid("showColumn","delBtn");
			}

			//if(SwLedger.objs.isInit)//解决datagrid隐藏列不正确的问题
				$("#appsList_table").datagrid("resize");
			
		},
		
		addApp:function(){
			SwLedger.appDialog("create");
		},
		
		appDialog:function(appMode,rowId){//view/edit/create
			var swName=$("#f_swName").val()?$("#f_swName").val():SwLedger.objs.swLedgerBean.swName;
			SwLedger.objs.swName=swName;
			
			if(!appMode){
				appMode="view";
			}
			var opts={
					title:"应用信息",
					width:"75%",
					height:"75%",
					src:basePath + "page/asset/itc/swLedger/appDetail.jsp?mode="+appMode+"&rowId="+rowId+"&swName="+swName
			};
			var dlgOpts = {
				idSuffix : "SwLedgerApp",
				width : opts.width,
				height : opts.height,
				title : opts.title
			};
			var btnOptsView=[{
				"name" : "关闭",
	            "onclick" : function(){
	                return true;
	            }
			}];
			var btnOpts = [{
		            "name" : "取消",
		            "onclick" : function(){
		                return true;
		            }
		        },{
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgSwLedgerAppContent").contentWindow;
		                //验证
		                if(!p.App.valid()){
		                	//FW.error("提交的内容有错误的地方，请修改后重试");
		                	return;
		                }
		                
		                var app=p.App.getApp();
		                //插入datagrid或更新
		                if(app.rowId){//有rowId，更新
		                	$("#appsList_table").datagrid("updateRow",{
		                		index: $("#appsList_table").datagrid("getRowIndex",app.rowId),
		                		row: app
		                	});
		                }else{//插入
		                	$("#appsList_table").datagrid("appendRow",app);
		                	SwLedger.changeAddAppBtn();
		                }
		                SwLedger.objs["appsMap"][app.rowId]=app;
		                
		                _parent().$("#itcDlgSwLedgerApp").dialog("close");
		            }
		        }
		    ];
			
			FW.dialog("init",{src:opts.src,btnOpts:appMode=="view"?btnOptsView:btnOpts,dlgOpts:dlgOpts});
		},
		
		delApp:function(id,name){
			Notice.confirm("确认删除|是否确定要删除应用“"+name+"”？",function(){
				$('#appsList_table').datagrid('deleteRow',$('#appsList_table').datagrid('getRowIndex',id));
				SwLedger.changeAddAppBtn();
				$("#appsList_table").datagrid("resize");
			});
		},
		
		changeAddAppBtn:function(num){
			if(!num){
				var rows=$("#appsList_table").datagrid("getRows");
				num=rows.length;
			}
			if(num==0){
				$("#addAppBtn").html("添加应用");
			}else{
				$("#addAppBtn").html("继续添加应用");
			}
		},
		
		getAppsChanged:function(){//获得应用变更的情况
			var rows=$("#appsList_table").datagrid("getRows");
			var addApps=[];
			var updateApps=[];
			var delApps=[];
			var map={};//用于标记在最终列表中且非新建的
			for(var i=0;i<rows.length;i++){
				if(!rows[i].appId){//新建的
					SwLedger.objs.appsMap[rows[i].rowId].appId=null;//把appId置空，不能是undefined和""，否则后台会报错
					addApps.push(SwLedger.objs.appsMap[rows[i].rowId]);
				}else{
					if(SwLedger.objs.appsMap[rows[i].rowId].isChanged)//修改的
						updateApps.push(SwLedger.objs.appsMap[rows[i].rowId]);
					map[rows[i].appId]=true;
				}
			}
			//查找删除的
			for(var i=0;i<SwLedger.objs.swLedgerBean.apps.length;i++){
				if(!map[SwLedger.objs.swLedgerBean.apps[i].appId]){
					delApps.push(SwLedger.objs.swLedgerBean.apps[i]);
				}
			}
			return {addApps:addApps,updateApps:updateApps,delApps:delApps};
		},

		loadApps:function(){
			SwLedger.objs["rowId"]=1;
			
			//防止浅拷贝
	    	SwLedger.objs["appsMap"]={};
	    	var apps=SwLedger.objs.swLedgerBean.apps;
	    	var tmp=[];
	    	for(var i=0;i<apps.length;i++){
	    		var obj=$.extend(true,{},apps[i]);
	    		obj.isChanged=false;
				SwLedger.objs["appsMap"][i+1]=obj;
	    		tmp.push(obj);
	    	}
	    	
			$("#appsList_table").datagrid("loadData",tmp);
		}
};