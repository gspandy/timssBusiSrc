<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>工单列表导出</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	var siteId = ItcMvcService.getUser().siteId;
	/* 表单字段定义  */
	var fields = [
			    {title : "开始时间", id : "fromTime",type:"date", dataType:"datetime",
					rules : {required:true},
					options : {endDate : new Date()}
				},
			    {title : "结束时间", id : "toTime",type:"date", dataType:"datetime",
					rules : {required:true},
					options : {endDate : new Date()}
				},
				 {title : "用户组", id : "rysx", type : "combobox",
				 	options : {
				 		allowEmpty:true,
					 	url : basePath + "workorder/woUtil/userGroupFilter.do?filterStr=itc_wo_wt",
			    		multiselect:true,
			    	    remoteLoadOn : "init",
			    	    onChange:function(val,obj){ 
			    	    	if(val==""){
			    	    		$("#f_engineers").iCombo("setVal",null);
			    	    	}else{
			    	    		$.post(basePath + "workorder/woUtil/userGroupGetUserIds.do",{"userGroupIds":val},
										function(data){
											var result = {}; 
											for(var i=0;i<data.length;i++){
												result[data[i]]=true;
											}
											$("#f_engineers").iCombo("setVal",result);
								},"json");
			    	    	}
			    	    	
				    		
				    	}
					}
				 },
			    {title : "工程师", id : "engineers", type : "combobox", 
			    	options : {
			    		allowSearch:true,
			    		allowEmpty:true,
			    		url : basePath + "workorder/woUtil/engineerRole.do",
			    		multiselect:true,
			    	    remoteLoadOn : "init"
			    	}	
			    },
			    {title : "服务目录ID", id : "serDirectorysId",type : "hidden"},
			    {title : "服务目录", id : "serDirectorys",
			    	render:function(id){
		            	 var ipt = $("#" + id);
		            	 ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
		            	 ipt.ITCUI_Input();
		            	 ipt.next(".itcui_input_icon").on("click",function(){
		                     var src = basePath+"page/workorder/itc/woParamsConf/sampleSerDirectoryTree.jsp?embbed=1";
		                     var dlgOpts = { width : 300,height:500, closed : false,title:"选择服务目录",
		                     				modal:true,"idSuffix":"Popup"};
		                     var btnOpts = [{"name" : "取消",
								            "onclick" : function(){
								                return true;}
								        	},
								           {
								            "name" : "确定",
								            "style" : "btn-success",
								            "onclick" : function(){
								                //itcDlgContent是对话框默认iframe的id
								                var p = _parent().window.document.getElementById("itcDlgPopupContent").contentWindow;
								                var nodes =p.$("#samplefaultType_tree").tree("getChecked");
								                var nodesId = "";
								                var nodesName="";
								                for(var i=0 ; i<nodes.length; i++){
								                	nodesId += nodes[i].id+",";
								                	nodesName += nodes[i].text+",";
								                }
								                nodesId = nodesId.substring(0,nodesId.length-1)
								                nodesName = nodesName.substring(0,nodesName.length-1);
								                
								              
								                $("#exportCondition").iForm("setVal",{"serDirectorysId":nodesId,
		    																"serDirectorys":nodesName});
								                return true;
								            }
								           }];
		                      FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
		                 });
		             }
		        },
		        {
			        title : "服务性质", 
			        id : "serCharacter",
			        type : "radio",
			        data : [
			            ["all",'全部',true],
			            ["event",'事件'],
			            ["request",'请求']
			        ]
			    }
			];
		
	$(document).ready(function() {
		
	/* form表单初始化 */
		$("#exportCondition").iForm("init",{"fields":fields,"options":
			{
				validate:true,
				xsWidth:12,
				mdWidth:8,
				labelFixWidth:80
			}});
		 
	});
	
	</script>
  </head>
  <body>
  	
    <div style="margin-top:8px">
    	 <form id="exportCondition" class="autoform"></form>
    </div>
  </body>
</html>
