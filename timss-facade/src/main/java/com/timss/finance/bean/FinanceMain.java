package com.timss.finance.bean;

import java.util.Date;
import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

public class FinanceMain extends ItcMvcBean{
	private static final long serialVersionUID = 406692827664201480L;
	
	@AutoGen(value="FIN_FID_SEQ")
	private String fid; //业务id,使用 FIN+date+自增
	private String finance_type; //报销类型
        private String finance_typeid; //报销类型编号
        private String accType; //记账类型 department、company
	
        private String fname; //报销单名称
	private String createid; //创建人编号
        private String creatorname; //创建人名称
        private double total_amount; //总金额
        
	private String departmentid; //创建人所属部门编号
	private String department; //创建人所属部门名称
	private Date createdate; //创建日期
	
	private Date strdate; //开始日期
	private Date enddate; //结束日期
       
	private String description; //报销事由
	private String join_boss;  //参与领导
	private String join_bossid; //参加领导编号
	private String join_nbr; //参与人数
	
	private double price;//报销金额(元)
	

//	private String subject; //开支科目
//	private String payeeOnly;//收款方
//	private String payeeOther; //收款方
         
//	private String payeeCode; //报销人ID
//	private String payeeName; //报销人名
//	private String processInstId; //流程ID
        
	private String formDay;//填表日期
        
	private String status; //报销单状态
	private String statusName; //报销单状态名
	private String finance_flow; //报销流程
	private String finance_flowid; //报销流程编号
	
	private String is_show; //显示标志
	private String flag_item; //标志域
	private String fin_level; //报销层面
	private String updateid; //更新人编号
	
	private String siteid;
	private String deptid;
	private String applyId;  //费用申请的id
	private String applyName; //申请单名称
	private double budget;  //申请批复金额(元)
	private String isAdministractiveExpenses; //是否行政报销
	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getApplyName() {
        return applyName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}


	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public String getStatusName() {
            return statusName;
        }
    
        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getIsAdministractiveExpenses() {
            return isAdministractiveExpenses;
        }

        public void setIsAdministractiveExpenses(String isAdministractiveExpenses) {
            this.isAdministractiveExpenses = isAdministractiveExpenses;
        }

        public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	  public String getFinance_type() {
		return finance_type;
	}

	public void setFinance_type(String finance_type) {
		this.finance_type = finance_type;
	}

	public String getFinance_flow() {
		return finance_flow;
	}

	public void setFinance_flow(String finance_flow) {
		this.finance_flow = finance_flow;
	}
	public String getAccType() {
            return accType;
        }

        public void setAccType(String accType) {
            this.accType = accType;
        }

	public String getCreateid() {
		return createid;
	}

	public void setCreateid(String createid) {
		this.createid = createid;
	}

	
	public String getUpdateid() {
		return updateid;
	}

	public void setUpdateid(String updateid) {
		this.updateid = updateid;
	}

	public String getIs_show() {
		return is_show;
	}

	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}

	public String getFlag_item() {
		return flag_item;
	}

	public void setFlag_item(String flag_item) {
		this.flag_item = flag_item;
	}

	public String getFinance_typeid() {
		return finance_typeid;
	}

	public void setFinance_typeid(String finance_typeid) {
		this.finance_typeid = finance_typeid;
	}

	public String getFinance_flowid() {
		return finance_flowid;
	}

	public void setFinance_flowid(String finance_flowid) {
		this.finance_flowid = finance_flowid;
	}

	public String getFin_level() {
		return fin_level;
	}

	public void setFin_level(String fin_level) {
		this.fin_level = fin_level;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdate == null) ? 0 : createdate.hashCode());
		result = prime * result
				+ ((createid == null) ? 0 : createid.hashCode());
		result = prime * result
				+ ((creatorname == null) ? 0 : creatorname.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result
				+ ((departmentid == null) ? 0 : departmentid.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result
				+ ((finance_flow == null) ? 0 : finance_flow.hashCode());
		result = prime * result
				+ ((finance_type == null) ? 0 : finance_type.hashCode());
		result = prime * result + ((fname == null) ? 0 : fname.hashCode());
		result = prime * result + ((is_show == null) ? 0 : is_show.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		long temp;
		temp = Double.doubleToLongBits(total_amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((updateid == null) ? 0 : updateid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FinanceMain other = (FinanceMain) obj;
		if (createdate == null) {
			if (other.createdate != null)
				return false;
		} else if (!createdate.equals(other.createdate))
			return false;
		if (createid == null) {
			if (other.createid != null)
				return false;
		} else if (!createid.equals(other.createid))
			return false;
		if (creatorname == null) {
			if (other.creatorname != null)
				return false;
		} else if (!creatorname.equals(other.creatorname))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (departmentid == null) {
			if (other.departmentid != null)
				return false;
		} else if (!departmentid.equals(other.departmentid))
			return false;
		if (fid == null) {
			if (other.fid != null)
				return false;
		} else if (!fid.equals(other.fid))
			return false;
		if (finance_flow == null) {
			if (other.finance_flow != null)
				return false;
		} else if (!finance_flow.equals(other.finance_flow))
			return false;
		if (finance_type == null) {
			if (other.finance_type != null)
				return false;
		} else if (!finance_type.equals(other.finance_type))
			return false;
		if (fname == null) {
			if (other.fname != null)
				return false;
		} else if (!fname.equals(other.fname))
			return false;
		if (is_show == null) {
			if (other.is_show != null)
				return false;
		} else if (!is_show.equals(other.is_show))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (Double.doubleToLongBits(total_amount) != Double
				.doubleToLongBits(other.total_amount))
			return false;
		if (updateid == null) {
			if (other.updateid != null)
				return false;
		} else if (!updateid.equals(other.updateid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FinanceMain [fid=" + fid + ", fname=" + fname
			+ ", creatorname=" + creatorname + ", departmentid="
			+ departmentid + ", department=" + department + ", createdate="
			+ createdate + ", total_amount=" + total_amount + ", status="
			+ status + ", finance_type=" + finance_type + ", finance_flow="
			+ finance_flow + ", createid=" + createid + ", is_show="
			+ is_show + ", updateid=" + updateid + "]";
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

    public Date getStrdate() {
        return strdate;
    }

    public void setStrdate(Date strdate) {
        this.strdate = strdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJoin_boss() {
        return join_boss;
    }

    public void setJoin_boss(String join_boss) {
        this.join_boss = join_boss;
    }

    public String getJoin_bossid() {
        return join_bossid;
    }

    public void setJoin_bossid(String join_bossid) {
        this.join_bossid = join_bossid;
    }

    public String getJoin_nbr() {
        return join_nbr;
    }

    public void setJoin_nbr(String join_nbr) {
        this.join_nbr = join_nbr;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

 

    public String getFormDay() {
        return formDay;
    }

    public void setFormDay(String formDay) {
        this.formDay = formDay;
    }
	
	
}
