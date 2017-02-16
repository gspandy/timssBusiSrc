/**
 * 服务目录树
 * */
function initLeftTree(){
		FW.addSideFrame({
			src:basePath+"page/itsm/core/woParamsConf/serCatalogTree.jsp?embbed=1",
			id:"itsmSerCatalogTree",
			conditions :[{tab:"^itsm_root$",tree:"^itsm_root_itsm_infoWo$"},
			             {tab:"^ITSQ.+"},
						 {tab:"^infoWo.+"},
						 {tab:"^newInfoWo.+"}
						]
		}); 
	}

function print(){
	var printUrl = "http://timss.gdyd.com/";
	var src = fileExportPath + "preview?__report=report/TIMSS2_ITSM_INFOWO.rptdesign&__format=pdf"+
						"&infoWoId="+infoWoId+"&siteid="+siteId+"&workflow_id="+processInstId+
						"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
	var title ="信息工单详情信息";
	FW.dialog("init",{
		src: url,
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
	});
}

function showPrincipal(){
	var printUrl = "http://timss.gdyd.com/";
	var src = fileExportPath + "preview?__report=report/TIMSS2_ITSM_DPP_INFOWOPRINCIPAL.rptdesign&__format=pdf"+
						"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
	var title ="系统负责人信息";
	FW.dialog("init",{
		src: url,
		btnOpts:[{
	            "name" : "关闭",
	            "float" : "right",
	            "style" : "btn-default",
	            "onclick" : function(){
	                _parent().$("#itcDlg").dialog("close");
	             }
	        }],
		dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
	});
}