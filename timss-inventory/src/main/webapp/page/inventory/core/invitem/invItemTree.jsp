<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String embbed = request.getParameter("embbed");
	String multi = request.getParameter("multi");
	String isspare = request.getParameter("isspare");
	String invmatapply = request.getParameter("invmatapply");
	String categoryname = request.getParameter("categoryname")==null?"":String.valueOf(request.getParameter("categoryname"));
	//20160108 add by yuanzh 添加根据仓库id查找
	String warehouseid = request.getParameter("warehouseid")==null?"":String.valueOf(request.getParameter("warehouseid"));
	String opentype = request.getAttribute("opentype")==null?"":String.valueOf(request.getAttribute("opentype"));
	String active = request.getParameter("active")==null?"":String.valueOf(request.getParameter("active"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
  <head>
  <link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
	<title>物资树</title>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
	<script>
		var embbed = <%=embbed%>;//是否是嵌入对话框中显示
		var multi = <%=multi%>;//是否允许多选
		var isspare = <%=isspare%>;//是否备件
		var invmatapply = <%=invmatapply%>;//是否备件
		var categoryname = '<%=categoryname%>';
		var opentype = '<%=opentype%>';
		var warehouseid = '<%=warehouseid%>';
		var active = '<%=active%>';
		var childrenNode;
		
		//跳转判断
		function jumpTo(url){
			if(url.indexOf("?")<0){
				url += "?nousearg=1";
			}
			if(!embbed){
				url +="&embbed="; 
				FW.navigate(url);
			}
			else if(embbed==1){
				url +="&embbed=1"; 
				_parent().$("#itcDlgInventoryPage").attr("src",url);
			}
			else if(embbed=2){
				return;//页内嵌入模式没有跳转
			}
		}
		
		//初始化设备树
		function initTree(id,type){
			var url = "";
			$("#item_tree").tree({
				url : basePath + "inventory/invitem/getItemsTree.do?id="+id+"&type="+type+"&opentype="+opentype+"&categoryname="+encodeURIComponent(categoryname)+"&warehouseid="+warehouseid,
				onSelect : function(node){
					if(embbed!=2){
						var navTreeId = FW.getCurrentNavTreeId();
						if(node.type=="category" ||node.type=="material"){
							if(!embbed){
								if(navTreeId=="stock_stockquery"){
									url = basePath+"inventory/invitem/invTreeItemList.do?cateId=" + node.id+"&opentype="+opentype+"&active="+active;
									jumpTo(url);
								}
								else if(navTreeId=="stock_classList"){
									if(node.type!="root"){
										url = basePath+"inventory/invcategroy/invcategroyForm.do?cateId=" + node.id;
										jumpTo(url);
									}else{
										url = basePath+"inventory/invitem/invTreeNoSelectWarehouse.do";
										jumpTo(url);
									}
								}
							}else{
								if("categroy" == opentype){
									url = basePath+"inventory/invcategroy/invcategroyForm.do?cateId=" + node.id;
									jumpTo(url);
								}else{
									if(null!=embbed){
										url = basePath+"inventory/invitem/invTreeItemList.do?cateId=" + node.id+"&opentype="+opentype+"&active="+active;
										if(isspare==1){
											url += "&isspare=1";
										}
										if(invmatapply==1){
											url += "&invmatapply=1";
										}
										jumpTo(url);
									}
								}
							}
							
						} else if( node.type == "warehouse"){
							if(navTreeId!="stock_classList"){
								//if(embbed!=1 && ("warehouse" == opentype||"" == opentype)){
							 	//	url = basePath+"inventory/invwarehouse/invWarehouseForm.do?warehouseId=" + node.id+"&opentype="+opentype+"&id="+node.id+"&mode=view&forbidEdit=true";
							 	//	jumpTo(url);
								//} 
								
								if("warehouse" == opentype||"" == opentype){
							 		url = basePath+"inventory/invitem/invTreeItemList.do?warehouseid=" + node.id+"&opentype="+opentype+"&active="+active;
							 		jumpTo(url);
								} 	
							}else{
									url = basePath+"inventory/invcategroy/invCategroyTreeList.do?cateId=" + node.id;
							 		jumpTo(url);
							}
						}
					}
					
					if(!multi){
						updateFormVal();
					}
				},
				onContextMenu : function(e, node){	
					var navTreeId = FW.getCurrentNavTreeId();
					if(navTreeId=="stock_classList"){		
						e.preventDefault();
						if(!embbed){
							_parent().$("#renameNode").show();
							_parent().$("#deleteNode").show();
							if(node.type=="root"){
								return;//根节点（组织机构）不能弹出菜单
							}							
							else if(node.type=="warehouse"){
								_parent().$("#newNodeAtCurrLevel").hide();
								_parent().$("#newNodeAtNexLevel").show().html("在当前仓库下创建物资分类");
								_parent().$("#deleteNode").hide();
								_parent().$("#renameNode").hide();
							}
							else if(node.type=="material"){
								var pNode = $("#item_tree").tree("getParent",node.target);								
								if(pNode.type=="warehouse"){
									//三级节点
									_parent().$("#newNodeAtCurrLevel").show().html("在对应仓库下创建物资分类");
									_parent().$("#newNodeAtNexLevel").show().html("在当前分类下创建子分类");
								}
								else{
									//四级节点
									_parent().$("#newNodeAtCurrLevel").show().html("在当前分类下创建子分类");
									_parent().$("#newNodeAtNexLevel").hide();
								}
							}							
							var offset = _parent().$("#itemTree").offset(); 
							_parent().$("#iiTreeMenu").css({
								left:e.pageX + offset.left,
								top:e.pageY + offset.top
							});
							_parent().$("#iiTreeMenuToggle").dropdown("toggle");
							currClickNode = node; //保存当前点击的树节点的信息
						}
					}
				},
				onCheck : function(node, checked){
					if(multi){
						updateFormVal();
					}
				}
			});		
		}
		
		//初始化物资树输入查询
		function initTreeSearch(){
			$("#search").iInput("init",{
				onSearch:function(val){
					if(null!=val&&''!=val){
						$.ajax({
							type : "POST",
							url: basePath+"/inventory/invitem/queryTreeNode.do",
							data: {"search":val},
							dataType : "json",
							success : function(data) {
								initTree(data.ids,data.type);
							}
						});
					}else{
						initTree("","init");
					}
				},
				onPressEnter:function(val){
					if(null!=val&&''!=val){
						$.ajax({
							type : "POST",
							url: basePath+"/inventory/invitem/queryTreeNode.do",
							data: {"search":val},
							dataType : "json",
							success : function(data) {
								initTree(data.ids,data.type);
							}
						});
					}else{
						initTree("","init");
					}
				}
			});
		}
		
		//树形目录
		function initTreeMenu(){
			var p = _parent();
			if(p.$("body #iiTreeMenu").length>0){
				$("#iiTreeMenu").remove();
			}
			$("#iiTreeMenu").appendTo(p.$("body"));
			p.$("#newNodeAtCurrLevel").click(function(){
				if(currClickNode.type!="warehouse"&&currClickNode.type!="root"){
					//如果是添加同级节点，则添加的位置节点的父节点是当前点击节点的父节点
					parentNode = $("#item_tree").tree("getParent",currClickNode.target);
					FW.navigate(basePath+"inventory/invcategroy/invcategroyForm.do?parentId="+parentNode.id);
				}
			});
			p.$("#newNodeAtNexLevel").click(function(){
				if(currClickNode.type=="warehouse"){
					parentNode = currClickNode;
					FW.navigate(basePath+"inventory/invcategroy/invcategroyForm.do?parentId="+parentNode.id);
				}
				if(currClickNode.type=="material"){
					parentNode = currClickNode;
					FW.navigate(basePath+"inventory/invcategroy/invcategroyForm.do?parentId="+parentNode.id);
				}
				if(currClickNode.type=="category"){
					parentNode = currClickNode;
					FW.navigate(basePath+"inventory/invcategroy/invcategroyForm.do?parentId="+parentNode.id);
				}
			});
			p.$("#renameNode").click(function(){
				if(currClickNode.type!="warehouse"&&currClickNode.type!="root"){ //位置重命名
					Notice.input("请输入新的物资分类名称",function(v){
				        if(v.length==0){
				        	FW.error("物资分类名称不能为空");
				        	return;
				        }
				        			        
			        	$.post(basePath + "inventory/invcategroy/updateCategroyName.do",{"categoryId":currClickNode.id,"categoryName":v },function(data){
				        	if(data.result == "success"){
				        		FW.success("重命名成功");
				        		$('#item_tree').tree('update', {
									target: currClickNode.target,
									text: v
								});	
				        	}else{
				        		FW.error("重命名失败");
				        	}
				        },"json");
			    	});
				}
			});
			
			p.$("#deleteNode").click(function(){
				$.ajax({
		            type:"POST",
		            async: false,
		            url : basePath + "inventory/invcategroy/deleteCategroy.do",//向后台提交数据
		            dataType: "json",
		            data:{"categoryId":currClickNode.id},
		            success:function(result){
		                if(result.result=="success"){
		                    FW.success("物资分类删除成功");
		                    $("#item_tree").tree('remove',currClickNode.target);      
		                }else{
		                	FW.error("该分类中还存在物资不能删除");
		                }
		            }
		        });                 
			});
		}
		
		//更新选择数据
		function updateFormVal(){
			var result = getInvSelected();
			var p = _parent();
			if(p.onInventorySelect && typeof(p.onInventorySelect)=="function"){
				p.onInventorySelect(FW.joinKey(result),FW.joinVal(result));
			}
		}
		
		//获取选择内容
		function getInvSelected(){
			var t = $('#item_tree');
			var obj = {};
			if(!multi){
				var node = t.tree("getSelected");
				if(node.type!="root" && node.type!="warehouse"){
					if(node){
						obj[node.id] = node.text;
						return obj;
					}else{
						return null;
					}
				}
			}
			else{
				var nodes = t.tree("getChecked");
				if(node.type!="root" && node.type!="warehouse"){
					if(nodes && nodes.length>0){					
						for(var i=0;i<nodes.length;i++){
							var n = nodes[i];
							obj[n.id] = n.text;
						}
						return obj;
					}else{
						return null;
					}
				}
			}
		}
		
		$(document).ready(function(){
			initTree("","init");
			initTreeSearch();
			//initTreeMenu();		
		});
	</script>
  </head>
<body class="bbox" style="height:100%;width:100%;padding-right:0">
	<div style="padding-top:38px;width:100%;height:100%">
		<div class="input-group input-group-sm bbox" style="width:210px;position:absolute;top:0px;margin: 5px 0;margin-right:10px;">
			<input type="text" placeholder="按物资类型查询" id="search" icon="itcui_btn_mag"/>    
		</div>
		<div id='inventoryTreeArea' style="width:100%;height:100%;overflow:auto">
			<ul id="item_tree" style="width:100%">
				
			</ul>
		</div>
		<div class="dropdown hide-on-click" id="iiTreeMenu" style="position:absolute">
			<a data-toggle="dropdown" id="iiTreeMenuToggle"></a>
			<ul class="dropdown-menu" role="menu">
				<li><a class="menuitem" id="newNodeAtCurrLevel">新建同级节点</a></li>
				<li><a class="menuitem" id="newNodeAtNexLevel">新建下级节点</a></li>
				<li><a class="menuitem" id="deleteNode">删除</a></li>
				<li><a class="menuitem" id="renameNode">重命名</a></li>
			</ul>
		</div>
	</div>
</body>	
</html>
