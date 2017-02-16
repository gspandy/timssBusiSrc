var Machine={//basePath框架中已有
		objs:{
			form:[
				{title : "编码",id:"amCode",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "名称",id:"amName",rules:{
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "导入数据时间戳",id:"lastImport",type:"datetime"},
 				{title:"取数据方法",id:"type",type:"combobox",rules : {required:true},
					dataType : "enum",
					enumCat : "ATD_MACHINE_TYPE",
					options:{
						onChange:function(val){
							Machine.changeMachineType(val);
						}
					}
				},
 				{title : "IP",id:"amIp",rules:{
 					required:true,
 					maxChLength:parseInt(150*2/3)
 				}},
 				{title : "端口",id:"amPort",rules:{
 					required:true,
 					number : true
 				}},
 				{title:"文件路径",id:"path",rules:{maxChLength:parseInt(150*2/3)}},
 				{title:"登录名",id:"loginName",rules:{maxChLength:parseInt(50*2/3)}},
 				{title:"登录密码",id:"password",rules:{maxChLength:parseInt(50*2/3)}},
   				{title : "备注",id: "amDesc",linebreak:true,type:'textarea',
   					wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(4000*2/3)}
   				}
			]
		},

		init:function(){
			Machine.objs["isInit"]=false;
			$("#base_info").ITCUI_Foldable();
			$("#form_baseinfo").ITC_Form({fixLabelWidth:true,validate:true,labelFixWidth:120},Machine.objs.form); //主卡片信息	
			Machine.loadData();
			
			Machine.objs["isInit"]=true;
		},
		
		toEdit:function(){
			Machine.objs["mode"]="edit";
			Machine.changeShow();
		},

		toDelete:function(){
			Notice.confirm("确认删除|是否确定要删除考勤机“"+Machine.objs.machineBean.amIp+":"+Machine.objs.machineBean.amPort+
					"”？该操作无法撤销。",function(){Machine.delMachine();});
		},
		
		toBack:function(){//返回的操作
			if(Machine.objs.mode=="view"){
				Machine.toList();
			}else if(Machine.objs.mode=="create"){
				Machine.toList();
			}else if(Machine.objs.mode=="edit"){
				Machine.objs["mode"]="view";
				Machine.loadData();
			}
		},
		
		toList:function(){
			FW.navigate(basePath+"attendance/machine/listPage.do");
		},
		
		changeMachineType:function(type){//切换考勤机
			if("machine"==type){
				$("#form_baseinfo").iForm("hide",["path","loginName","password"]);
				$("#form_baseinfo").iForm("show",["amPort"]);
			}else if("smb"==type){
				$("#form_baseinfo").iForm("hide",["amPort"]);
				$("#form_baseinfo").iForm("show",["path","loginName","password"]);
			}
		},
		changeShow:function(){//切换模式
			if(Machine.objs.mode=="view"){
				$("#pageTitle").html("考勤机详情");
				$("#form_baseinfo").ITC_Form("beginedit");//临时使用，解决空字段在点击其他节点后不隐藏的问题
				$("#form_baseinfo").ITC_Form("readonly");
				
				$("#btnEdit,#btnBack").show();
				$("#btnCreate,#btnSave,#btnDel").hide();
			}else if(Machine.objs.mode=="create"){
				$("#pageTitle").html("新建考勤机");
				$("#form_baseinfo").ITC_Form("beginedit");
				
				$("#btnCreate,#btnBack").show();
				$("#btnEdit,#btnDel,#btnSave").hide();
			}else if(Machine.objs.mode=="edit"){
				$("#pageTitle").html("编辑考勤机");
				$("#form_baseinfo").ITC_Form("beginedit");

				$("#btnSave,#btnBack,#btnDel").show();
				$("#btnCreate,#btnEdit").hide();
			}
		},
		
		loadData:function(){//加载数据
			if(!Machine.objs.machineBean.type){
				Machine.objs.machineBean.type="machine";
			}
			Machine.changeMachineType(Machine.objs.machineBean.type);
			$("#form_baseinfo").ITC_Form("loaddata",Machine.objs.machineBean);
			Machine.changeShow();
		},

		createMachine:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			var obj = $("#form_baseinfo").iForm("getVal");

			$.ajax({
				type : "POST",
				url: basePath+"attendance/machine/insertMachine.do",
				data: {"machineBean":FW.stringify(obj)},
				dataType : "json",
				success : function(data) {				
					if(data.status==1){
						FW.success("新建考勤机成功");
						Machine.objs["machineBean"]=data.machineBean;						
						//设为浏览模式并重载数据
						Machine.objs["mode"]="view";
						Machine.loadData();
					}else{
						FW.error("新建考勤机失败");
					}
				}
			});
		},
		
		updateMachine:function(){
			if(!$("#form_baseinfo").valid()){
				//FW.error("提交的内容有错误的地方，请修改后重试");
				return;
			}
			
			var params = $("#form_baseinfo").iForm("getVal");
			//补充缺少的参数
			params.amId=Machine.objs.machineBean.amId;

			$.post(basePath + "attendance/machine/updateMachine.do",{
					"machineBean":FW.stringify(params)
				},function(data){
				if(data.status == "1"){
					FW.success("更新成功");
					Machine.objs["machineBean"]=data.machineBean;
					Machine.toBack();
				}else{
					FW.error("更新失败");
				}
			},"json");
		},
		
		delMachine:function(){
			$.post(basePath + "attendance/machine/deleteMachine.do",{"machineId":Machine.objs.machineBean.amId},function(data){
				if(data.result == "ok"){
					FW.success("删除成功");
					Machine.toList();
				}else{
					FW.error("删除失败"+","+"请稍后重试或联系管理员");
				}
			},"json");
		}
};