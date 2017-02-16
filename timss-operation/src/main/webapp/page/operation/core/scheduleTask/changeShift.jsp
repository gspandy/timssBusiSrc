<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>转其他班次</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	var stationId = '<%=request.getParameter("stationId")%>'; 
	var calendarVoList = null;
	/* 表单字段定义  */
	var fields = [
			    {title : "日期", id : "shiftDate",type:"date", dataType:"date",
					rules : {required:true},
					options : {
						startDate : new Date()
					}
				},
				{title : "班次", id : "shiftName",type : "hidden"},
				{title : "班次", id : "shiftId",rules : {required:true},type : "combobox"},
				{title : "值别", id : "dutyId", type : "hidden"},
			    {title : "值别", id : "dutyName", rules : {required:true},type : "label"},
		        {
			        title : "原因及备注", 
			        id : "remarks",
            		type : "textarea",
       		        linebreak:true,
       		        rules : {maxChLength:400},
       		        wrapXsWidth:12,
       		        wrapMdWidth:8,
       		        height:88
			    }
			];
		
	$(document).ready(function() {
		
		/* form表单初始化 */
		$("#changeShiftForm").iForm("init",{"fields":fields,"options":
			{
				validate:true,
				xsWidth:12,
				mdWidth:8,
				labelFixWidth:100
			}});
		 $("#f_shiftDate").change( function(){
			changeShift();
		} );
	});
//校验
function valid(){
	return $("#changeShiftForm").valid();
}
	
function changeShift(){
	var data = $("#changeShiftForm").iForm("getVal");
	var shiftDate = data.shiftDate;
	var url = basePath + "operation/scheduleTask/queryShiftByStationIdAndDate.do?stationId="+stationId+"&shiftDate="+shiftDate ;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.total > 0 ){
				var shiftList = data.rows;
				calendarVoList = data.calendarVoList;
				var shiftArr = [];
				for( var index in shiftList ){
					shiftArr.push( [shiftList[index].id,shiftList[index].name]);
				}
				$("#f_shiftId").iCombo("init", {
					data : shiftArr,
					allowEmpty:true,
					onChange:function(val,obj){ 
		    	    	if(val==""){
		    	    		$("#changeShiftForm").iForm("setVal",{dutyId:"",dutyName:""})
		    	    	}else{
		    	    		for(var i = 0 ; i<calendarVoList.length; i++ ){
		    	    			var objTemp = calendarVoList[i];
		    	    			if(objTemp.shiftId == val){
			    	    			$("#changeShiftForm").iForm("setVal",
										{"shiftName":objTemp.shiftName,"dutyId":objTemp.dutyId,"dutyName":objTemp.dutyName});
										break;
		    	    			}
		    	    		}
		    	    	}
			    	}
				});
			}
		}
	});
}
	
	</script>
  </head>
  <body>
  	
    <div style="margin-top:8px">
    	 <form id="changeShiftForm" class="autoform"></form>
    </div>
  </body>
</html>
