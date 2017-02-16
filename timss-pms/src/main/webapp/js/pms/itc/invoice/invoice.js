
/**
* 年度计划基本的js引用页面
 */

var invoiceFormFields=[
	    {id:"id",type:"hidden"},
	    {id:"contractId",type:"hidden"},
	    {id:"payId",type:"hidden"},
	    {id:"payplanId",type:"hidden"},
	    {
	    	title : "合同名称", id : "contractName"
	    },
	    {
	    	title : "合同编号", id : "contractCode"
	    },
	    {
	    	title : "合同金额(元)", id : "contractSum"
	    },
	    {title : "发票号", id : "code", rules: {
                required: true
	        }
        },
	    {title : "发票代码", id : "invoiceCode", rules: {
            required: true
        }
	    },
	    {title : "发票金额(元)", id : "sum",
        	rules: {
                required: true
	        }
	    },
	    {
	        title : "税率(%)", 
	        id :"rate",
	        
	        rules: {
                required: true,
                number:true,
                range:[0,100]
	        }
	    },

	    {title : "不含税金额(元)", id : "withoutTax",
	        rules: {
                required: true,
                number:true
	        }
	    },
	    {title : "税额(元)", id : "tax",
	        rules: {
                required: true,
                number:true
	        }
	    },
	    {
			id : "invoiceDate",
			title:"开票时间",
			type : "date",
			dataType : "date",
	        rules: {
                
	        }
	    },
	    {
			id : "ischeck",
			title:"是否确认",
			type:"combobox",
			data:[["Y","是"],["N","否",true]],
	        rules: {
                required: true
	        }
	    },
	    {
			id : "checkDate",
			title:"到款时间",
			type : "date",
			dataType : "date",
	        rules: {
               
	        }
	    },
	    
	    {
	        title : "描述", 
	        id : "command",
	        type : "textarea",
	        linebreak:true,
	        wrapXsWidth:12,
	        wrapMdWidth:8,
	        height:48
	    }
];
var checkFormFields=[
               	    {id:"id",type:"hidden"},
               	    {id:"contractId",type:"hidden"},
               	    {id:"payId",type:"hidden"},
               	    {id:"payplanId",type:"hidden"},
               	    {
               	    	title : "合同名称", id : "contractName"
               	    },
               	    {
               	    	title : "合同编号", id : "contractCode"
               	    },
               	    {
               	    	title : "合同金额", id : "contractSum"
               	    },
               	    {title : "发票号", id : "code"
                       },
               	    {title : "发票代码", id : "invoiceCode"
               	    },
               	    {title : "发票金额(元)", id : "sum"
               	    },
               	    {
               	        title : "税率(%)", 
               	        id :"rate"
               	    },

               	    {title : "不含税金额(元)", id : "withoutTax"
               	    },
               	    {title : "税额(元)", id : "tax"
               	    },
               	    {
               			id : "invoiceDate",
               			title:"开票时间",
               			type : "date",
               			dataType : "date"
               	    },
               	    {
               			id : "ischeck",
               			title:"是否确认",
               			type:"combobox",
               			data:[["Y","是"],["N","否",true]],
               	        rules: {
                               required: true
               	        }
               	    },
               	    {
               			id : "checkDate",
               			title:"到款时间",
               			type : "date",
               			dataType : "date",
               	        rules: {
                               
               	        }
               	    },
               	    
               	    {
               	        title : "描述", 
               	        id : "command",
               	        type : "textarea",
               	        linebreak:true,
               	        wrapXsWidth:12,
               	        wrapMdWidth:8,
               	        height:48
               	    }
               ];



