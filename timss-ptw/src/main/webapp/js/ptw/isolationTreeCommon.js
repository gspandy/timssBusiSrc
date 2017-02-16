/**
 * 弹出隔离方法和隔离点的选择框
 * @param {} opts
 */
FW.showIsolationMethod = function(opts){
	if(!opts){
		return;
	}
	opts.basePath = opts.basePath || window.basePath || "";
	opts.width = opts.width || "75%";
	opts.height = 520;
	var treeSrc = basePath + "page/ptw/core/pointTree.jsp?embbed=1&opentype=&jumpMode=dialog";
	var listSrc = basePath + "ptw/ptwIslMethDef/openIslMethDefItemPage.do?embbed=1";

	if(_parent().$("#itcDlgIsolationTree").length==1){
		_parent().$("#itcDlgIsolation").dialog("open");
		_parent().$("#itcDlgIsolationTree").attr("src",treeSrc);
		_parent().$("#itcDlgIsolationPage").attr("src",listSrc);
		return;
	}
	else{		
		var dlgHtml = '<div id="itcDlgIsolation">' +
			'<div style="width:100%;height:100%;padding-left:240px;position:relative;overflow:hidden" class="bbox">' + 
			    '<iframe class="tree-iframe" frameborder="no" border="0" style="width:240px;height:100%;position:absolute;left:0px;top:0px;" id="itcDlgIsolationTree">' +
			    '</iframe>'+
			    '<iframe frameborder="no" border="0" style="width:100%;height:100%;" id="itcDlgIsolationPage">' +
			    '</iframe>' +
		    '</div>' + 
		'</div>' +
		'<div id="itcDlgIsolationBtn" style="display:none;" class="bbox itcdlg-btns">' +
	    	'<div id="itcDlgIsolationBtnWrap" style="width:100%;height:100%">' + 
	    	'</div>' +
		'</div>';
		_parent().$("body").append(dlgHtml);
	}
	var dlgOpts = {
		idSuffix : "Isolation",
		width : opts.width,
		height : opts.height,
		title : opts.title || "选择隔离方法"
	};
	var btnOpts = [{
            "name" : "关闭",
            "onclick" : function(){
                _parent().$("#itcDlgIsolation").dialog("destroy");
				_parent().$("#itcDlgIsolation").remove();
				_parent().$("#itcDlgIsolationBtn").remove();
            }
        }/*,{
        	"name" : "确定",
            //"name" : "添加",
            "style" : "btn-success",
            "onclick" : function(){
            	var pa = _parent();
                var p = pa.window.document.getElementById("itcDlgIsolationPage").contentWindow;
                var result = p.getSelected();
                if(!result){
                	FW.error("未选择隔离方法");
                	return;
                }
                var rowArr = {};
    			var chongfuMsg = "";
    			//判断是否有重复的pointNo
    			for( var k = 0; k < result.length; k++ ){
    				var pointNo = result[k].pointNo;
    				if( rowArr[pointNo] != 1){
    					rowArr[pointNo] = 1;
    				}else{
    					chongfuMsg = chongfuMsg + " " + pointNo;
    				}
    			}
    			if( chongfuMsg != "" ){
    				//FW.error( chongfuMsg + "隔离点选择重复！" );
    				FW.error( "隔离点编号相同的只能选择一条！" );
    				return;
    			}
                
				if(opts.onParseData && typeof(opts.onParseData)=="function"){
					opts.onParseData(p.getSelected());
				}
                _parent().$("#itcDlgIsolation").dialog("destroy");
				_parent().$("#itcDlgIsolation").remove();
				_parent().$("#itcDlgIsolationBtn").remove();
            }
        }*/
    ];
	FW.dialog("init",{btnOpts:btnOpts,dlgOpts:dlgOpts});
	_parent().$("#itcDlgIsolationTree").attr("src",treeSrc);
	_parent().$("#itcDlgIsolationPage").attr("src",listSrc);
};
