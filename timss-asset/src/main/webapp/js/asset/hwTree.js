var HardwareTree={
		objs:{},
		init:function(){
			HardwareTree.objs["isInitFinish"]=false;
			HardwareTree.initTree();
			HardwareTree.initHint();
		},
		
		//树的函数
		initTree:function(){
			var opts = {
				url : basePath + "asset/hwLedger/queryHwLedgerTree.do",
				dnd : true,
				onSelect : function(node){
					if(!HardwareTree.objs.isShowCard){//不显示卡片详情
						return;
					}
					if(HardwareTree.objs.isInitFinish){//树已经初始化完成
						window.parent.document.getElementById("navtab_equipment").contentWindow.toShow(node.obj);
					}else{//初始化后选择根节点时不再加载根节点列表
						HardwareTree.objs.isInitFinish=true;
					}
				},
				onLoadSuccess:function(node, data){
					if(!HardwareTree.objs.isInitFinish){//第一次初始化要选择根节点
						for(var i=0;i<data.length;i++){
							if(data[i].isRoot=="Y"){
								node=$('#property_tree').tree("find",data[i].id);
								HardwareTree.objs["rootNode"]=node;
								$('#property_tree').tree("select",HardwareTree.objs.rootNode.target);
								break;
							}
						}
					}
				},
				loadFilter:function(data,parent){//用于修改数据，使适合树
					var result=new Array();
					if(data.parent){//有根节点信息，则当前节点为根节点，底下加入子节点
						var obj={};
						obj.id=data.parent.hwId;
						obj.text=data.parent.hwName;
						obj.parentId=data.parent.parentId;
						obj.state="open";
						obj.type=data.parent.hwType;
						obj.isRoot=data.parent.isRoot;
						obj.children=HardwareTree.changeToTreeNodeList(data.children);
						obj.iconCls=HardwareTree.changeIcon(data.parent);
						obj.obj=data.parent;
						result.push(obj);
					}else if(data.children){
						result=HardwareTree.changeToTreeNodeList(data.children);
					}else{
						result=data;
					}
					return result;
				},
				onContextMenu: function(event,node,target){
					event.preventDefault();
					if( node != null && node != "" ){
						var hwId = node.obj.hwId;
						var hwType = node.obj.hwType;
						if( "HW_L_SERVER,HW_L_VM".indexOf(hwType) >= 0 ){
							HardwareTree.showNodeInfo(event, hwId, hwType );
						}
					}
				},
				onBeforeDrag : function(node){
					//不准根节点
					if(node.type=="root"){
						return false;
					}
					if(!Priv.hasPrivilege("AST_HWL_NEW")){//不可编辑时，不许拖动
						return false;
					}
					return true;
				},
				onStartDrag : function(node){
					//拖拽时关闭右键菜单
					$("#property_tree").removeClass("open");
					currDragNode = node;
				},
				onDragOver : function(target,source){
					var t = $('#property_tree').tree("getNode",target);
				},
				onBeforeDrop : function(target, source,point){
					var t=$('#property_tree').tree("getNode",target);
					var s=source;
					var tp=$('#property_tree').tree("getParent",target);
					var sp=$('#property_tree').tree("getParent",source.target);
					
					if(point=="append"){//移动到目标节点内时
						if(t==sp){//移动到原父节点内
							return true;
						}else{
							if( HardwareTree.isMove(s, t) ){
								HardwareTree.changeNodeParent(s,t,point);
							}
						}
					}else{//移动到目标节点同级时
						if(tp==sp){//目标节点是兄弟节点时
							return true;
						}else{
							if( HardwareTree.isMove(s, tp) ){
								HardwareTree.changeNodeParent(s,tp,point,t);
							}
						}
					}
					return false;
				}
			};
			$('#property_tree').tree(opts);
		},
		changeToTreeNodeList:function(list){//将数据列表转为树节点列表
			var result=[];
			for(var i=0;i<list.length;i++){
				result.push(HardwareTree.changeToTreeNode(list[i]));
			}
			return result;
		},
		changeToTreeNode:function(bean){
			var obj={};
			obj.id=bean.hwId;
			obj.text=bean.hwName;
			obj.type=bean.hwType;
			obj.isRoot=bean.isRoot;
			obj.parentId=bean.parentId;
			obj.iconCls=HardwareTree.changeIcon(bean);
			if( !bean.withChild ){
				obj.children=[];
				obj.state="open";
			}else{
				obj.state="closed";
			}
			obj.obj=bean;
			return obj;
		},
		changeIcon:function(data){
			var iconCls="";
			if(data.hwType=='HW_L_VM'){
				//data.iconCls='icon-folder';
				iconCls='icon-vm';
			}else if( data.hwType=='HW_L_SERVER' ){
				iconCls='icon-server';
			}else if( data.hwType=='HW_L_ROOM_EQPT' ){
				iconCls='icon-room';
			}else if( data.hwType=='HW_L_NETWORK' ){
				iconCls='icon-net';
			}else if( data.hwType=='HW_L_STORAGE' ){
				iconCls='icon-storage';
			}else if( data.hwType=='HW_L_CABINET' ){
				iconCls='icon-cabinet';
			}else if( data.hwType=='HW_L_ROOM' ){
				iconCls='icon-house';
			}	
			return iconCls;
		},	
		
		//搜索的函数
		initHint:function(){
			$("#search").iInput("init");
			$("#search").parent().children("ul").css({"left":"0","width":"180px"});			
			$("#search").ITCUI_HintList({
				"datasource":basePath + "asset/hwLedger/searchHwLedgerHint.do",
				"getDataOnKeyPress":true,
				"clickEvent":function(id,name){
					$("#search").val(name);
					$.ajax({
						url : basePath + "asset/hwLedger/searchHintHwLedgerParentIds.do?id=" + id,
						type:'POST',
						dataType:"json",
						success : function(data){
							HardwareTree.objs.expandList = data;
							HardwareTree.objs.currExpand = data.length-1;
							$('#property_tree').tree("options").onExpand=function(node){
								if(HardwareTree.objs.expandList){
									HardwareTree.expandForHint();
								}
							};
							HardwareTree.expandForHint();
						}
					});
				},
				"showOn":"input",
				"highlight":true
			});
		},
		expandForHint:function(){//搜索点击后展开的操作		
			if(HardwareTree.objs.currExpand>0){
				var node=$('#property_tree').tree("find",HardwareTree.objs.expandList[HardwareTree.objs.currExpand]);
				if(node.state=='open'){
					HardwareTree.objs.currExpand=HardwareTree.objs.currExpand-1;
					HardwareTree.expandForHint();
				}else{
					$('#property_tree').tree("expand",node.target);
					HardwareTree.objs.currExpand=HardwareTree.objs.currExpand-1;
				}
			}else{
				HardwareTree.onExpandHintNode(HardwareTree.objs.expandList[HardwareTree.objs.currExpand]);
				HardwareTree.objs.expandList=null;
			}				
		},
		onExpandHintNode:function(propertyId){//快速搜索后点击展开树时，当展开到最后一个节点，即选择的节点的操作
			var node=$('#property_tree').tree("find",propertyId);
			$('#property_tree').tree("select",node.target);
			$('#property_tree').tree("scrollTo",node.target);			
		}, 
		showNodeInfo : function( e, id, type ){
			var url =  basePath + "asset/hwLedger/queryHwLedgerByTypeAndId.do?hwId=" + id + "&hwType=" + type;
			$.ajax({
				url : url,
				type:'POST',
				dataType:"json",
				success : function(data){
					if( data.result == "success" ){
						var offset = _parent().$("#assetHwTree").offset(); 
						
						var hwName = data.baseInfoVo.hwName;
						var ip = data.netVo.ip;
						var status = data.status;
						var swNameArr = [];
						for(var index in data.swLedgerAppBeans){
							swNameArr[index] = data.swLedgerAppBeans[index].swl.swName + "( " 
							+ data.swLedgerAppBeans[index].appType + "/"
							+ data.swLedgerAppBeans[index].appName + " )";
						}
						
						var height = 85;
						var htm = "<ul style='list-style-type:none;font-size: 14px;padding: 0px;margin: 8px;'>";
						htm += "<li style='color: #33CCFF;'> " + hwName + "</li>";
						htm += "<li> " + ip + "<span id='statusSpan' style='float:right;margin-right: 5px;'>" + status + "</span></li>";
						if( swNameArr.length > 0 ){
							htm += "<li>部署的软件台账：</li>";
							for( var i in swNameArr ){
								htm += "<li> " + swNameArr[i] + "</li>";
								height += 30;
							}
						}
						 htm += "</ul>";
						
						_parent().$("#hwTreeContentDiv").html( htm );
						_parent().$("#hwTreeDiv").css({
							left:e.pageX + offset.left,
							top:e.pageY + offset.top,
							height : height
						});
						var color = '';
						if( status == "关机"){
							color = "#FF3333";
						}else if( status == "在线" ){
							color = "#33CC66";
						}else if( status == "下架" ){
							color = "#999999";
						}
						_parent().$("#statusSpan").css({
							color : color
						});
						_parent().$("#hwTreeDiv").show();
					}
				}
			});
		},
		//修改节点的父节点,传入sourceNode,parentNode,point,targetNode
		changeNodeParent : function(s,p,point,t){
			FW.confirm("确认移动|您确定将<span style='red'>"+s.text+"</span>移动到<span style='red'>"+p.text+"</span>中吗？",function(){
				$.post(basePath + "asset/hwLedger/updateDropHwlTreeNode.do",
					{"id":s.id,"parentId":p.id},
					function(data){
						if(data.result=="ok"){
							FW.success("移动成功");
				   			var node = $('#property_tree').tree("pop",s.target);
				   			if(point=="append"){//需判断目标是否已展开
				   				if(p.state=="open"){
				   					$('#property_tree').tree("append",{
										parent : p.target,
										data : node,
										state:"closed",
										type :"system"
									});
				   				}else{
				   					$('#property_tree').tree('expand',p.target);//可能有bug，如果展开速度比下面的展示卡片慢，可能下面选择不到该节点
				   				}
				   			}else if(point=="top"){
				   				$('#property_tree').tree("insert",{
				   					before : t.target,
									data : node,
									state:"closed",
									type :"system"
								});
				   			}else if(point=="bottom"){
				   				$('#property_tree').tree("insert",{
				   					after : t.target,
									data : node,
									state:"closed",
									type :"system"
								});
				   			}
				   			//更新展示的bean，展示卡片，确保了展示的bean的父节点的正确
				   			//FW.navigate(basePath+"asset/assetInfo/assetDetail.do?mode=view&assetId="+s.id);
				   		}else if(data.result=="forbidMoveRoot"){
				   			FW.error("移动失败,根节点不能移动");
				   		}else if( data.result == "fail"){
				   			FW.error("移动失败 : " + data.reason );
				   		}else{
				   			FW.error("移动失败 " );
				   		}		
					}
				);
			});
		},
		isMove : function ( sourceNode , targetNode ){
			var flag = false;
			if( "HW_L_CABINET,HW_L_SERVER,HW_L_NETWORK,HW_L_STORAGE,HW_L_ROOM_EQPT".indexOf( sourceNode.type ) >= 0 ){
				if( "HW_L_ROOM" == targetNode.type ){
					flag = true;
				}
			}
			if( "HW_L_SERVER,HW_L_NETWORK,HW_L_STORAGE,HW_L_ROOM_EQPT".indexOf( sourceNode.type ) >= 0 ){
				if( "HW_L_CABINET" == targetNode.type ){
					flag = true;
				}
			}
			if( "HW_L_VM" == sourceNode.type ){
				if( "HW_L_SERVER" == targetNode.type ){
					flag = true;
				}
			}
			
			if( !flag ){
				FW.error( sourceNode.text + " 不能移动到 " + targetNode.text + "！");
			}
			return flag;
		}
};

