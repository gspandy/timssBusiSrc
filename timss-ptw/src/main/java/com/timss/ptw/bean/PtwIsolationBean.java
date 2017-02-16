package com.timss.ptw.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 隔离证
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationBean.java
 * @author: fengzt
 * @createDate: 2014年10月30日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PtwIsolationBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 7410308915064097191L;

    /**
     * id
     */
    private int id;
    
    /**
     * 工作票id
     */
    private int wtId;
    
    /**
     * 编号
     */
    private String no;
    
    /**
     * 工作内容
     */
    private String workContent;
    
    /**
     * 工作地点
     */
    private String workPlace;
    
    /**
     * 钥匙箱Id
     */
    private Integer keyBoxId;
    
    
    /**
     * 有效标识
     */
    private int yxbz;
    
    /**
     * 设备Id
     */
    private String eqId;
    
    /**
     * 设备编码
     */
    private String eqNo;
    
    /**
     * 设备名称
     */
    private String eqName;
    
    /**
     * 签发人编号
     */
    private String issuerNo;
    
    /**
     * 签发人
     */
    private String issuer;
    
    /**
     * 签发时间
     */
    private Date issuedTime;
    
    /**
     * 签发值长
     */
    private String issueSuper;
    
    /**
     * 签发值长编号
     */
    private String issueSuperNo;
    
    /**
     * 隔离人
     */
    private String executer;
    
    /**
     * 隔离人编号
     */
    private String executerNo;
    
    /**
     * 隔离时间
     */
    private Date executerTime;
    
    /**
     * 注销人
     */
    private String withDraw;
    
    /**
     * 注销人编号
     */
    private String withDrawNo;
    
    /**
     * 注销时间
     */
    private Date withDrawTime;
    
    /**
     * 解除人
     */
    private String remover;
    
    /**
     * 解除人编号
     */
    private String removerNo;
    
    /**
     * 解除时间
     */
    private Date removerTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 隔离证状态
     */
    private int status;
    
    /**
     * 是否为标准隔离证
     */
    private int isStdWt;
    
    /**
     * 钥匙箱编号
     */
    private String keyBoxNo;
    
    /**
     * 关联钥匙箱Id
     */
    private String relateKeyBoxId;
    
    /**
     * 钥匙箱类型定义Id
     */
    private int wtTypeId;
    
    /**
     * 用户名字
     */
    private String createUserName;

    /**
     * 作废人编号
     */
    private String cancelerNo;
    
    /**
     * 作废人
     */
    private String canceler;
    
    /**
     * 作废时间
     */
    private String cancelerTime;
    
    
    
    
    public String getCancelerNo() {
        return cancelerNo;
    }

    public void setCancelerNo(String cancelerNo) {
        this.cancelerNo = cancelerNo;
    }

    public String getCanceler() {
        return canceler;
    }

    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    public String getCancelerTime() {
        return cancelerTime;
    }

    public void setCancelerTime(String cancelerTime) {
        this.cancelerTime = cancelerTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public int getWtTypeId() {
        return wtTypeId;
    }

    public void setWtTypeId(int wtTypeId) {
        this.wtTypeId = wtTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWtId() {
        return wtId;
    }

    public void setWtId(int wtId) {
        this.wtId = wtId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public Integer getKeyBoxId() {
		return keyBoxId;
	}

	public void setKeyBoxId(Integer keyBoxId) {
		this.keyBoxId = keyBoxId;
	}

	public int getYxbz() {
        return yxbz;
    }

    public void setYxbz(int yxbz) {
        this.yxbz = yxbz;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public String getEqNo() {
        return eqNo;
    }

    public void setEqNo(String eqNo) {
        this.eqNo = eqNo;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getIssuerNo() {
        return issuerNo;
    }

    public void setIssuerNo(String issuerNo) {
        this.issuerNo = issuerNo;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Date getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(Date issuedTime) {
        this.issuedTime = issuedTime;
    }

    public String getIssueSuper() {
        return issueSuper;
    }

    public void setIssueSuper(String issueSuper) {
        this.issueSuper = issueSuper;
    }

    public String getIssueSuperNo() {
        return issueSuperNo;
    }

    public void setIssueSuperNo(String issueSuperNo) {
        this.issueSuperNo = issueSuperNo;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }

    public String getExecuterNo() {
        return executerNo;
    }

    public void setExecuterNo(String executerNo) {
        this.executerNo = executerNo;
    }

    public Date getExecuterTime() {
        return executerTime;
    }

    public void setExecuterTime(Date executerTime) {
        this.executerTime = executerTime;
    }

    public String getWithDraw() {
        return withDraw;
    }

    public void setWithDraw(String withDraw) {
        this.withDraw = withDraw;
    }

    public String getWithDrawNo() {
        return withDrawNo;
    }

    public void setWithDrawNo(String withDrawNo) {
        this.withDrawNo = withDrawNo;
    }

    public Date getWithDrawTime() {
        return withDrawTime;
    }

    public void setWithDrawTime(Date withDrawTime) {
        this.withDrawTime = withDrawTime;
    }

    public String getRemover() {
        return remover;
    }

    public void setRemover(String remover) {
        this.remover = remover;
    }

    public String getRemoverNo() {
        return removerNo;
    }

    public void setRemoverNo(String removerNo) {
        this.removerNo = removerNo;
    }

    public Date getRemoverTime() {
        return removerTime;
    }

    public void setRemoverTime(Date removerTime) {
        this.removerTime = removerTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getIsStdWt() {
        return isStdWt;
    }

    public void setIsStdWt(int isStdWt) {
        this.isStdWt = isStdWt;
    }

    public String getKeyBoxNo() {
        return keyBoxNo;
    }

    public void setKeyBoxNo(String keyBoxNo) {
        this.keyBoxNo = keyBoxNo;
    }
    
    

    public String getRelateKeyBoxId() {
		return relateKeyBoxId;
	}

	public void setRelateKeyBoxId(String relateKeyBoxId) {
		this.relateKeyBoxId = relateKeyBoxId;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((canceler == null) ? 0 : canceler.hashCode());
        result = prime * result + ((cancelerNo == null) ? 0 : cancelerNo.hashCode());
        result = prime * result + ((cancelerTime == null) ? 0 : cancelerTime.hashCode());
        result = prime * result + ((createUserName == null) ? 0 : createUserName.hashCode());
        result = prime * result + ((eqId == null) ? 0 : eqId.hashCode());
        result = prime * result + ((eqName == null) ? 0 : eqName.hashCode());
        result = prime * result + ((eqNo == null) ? 0 : eqNo.hashCode());
        result = prime * result + ((executer == null) ? 0 : executer.hashCode());
        result = prime * result + ((executerNo == null) ? 0 : executerNo.hashCode());
        result = prime * result + ((executerTime == null) ? 0 : executerTime.hashCode());
        result = prime * result + id;
        result = prime * result + isStdWt;
        result = prime * result + ((issueSuper == null) ? 0 : issueSuper.hashCode());
        result = prime * result + ((issueSuperNo == null) ? 0 : issueSuperNo.hashCode());
        result = prime * result + ((issuedTime == null) ? 0 : issuedTime.hashCode());
        result = prime * result + ((issuer == null) ? 0 : issuer.hashCode());
        result = prime * result + ((issuerNo == null) ? 0 : issuerNo.hashCode());
        result = prime * result + keyBoxId;
        result = prime * result + ((keyBoxNo == null) ? 0 : keyBoxNo.hashCode());
        result = prime * result + ((no == null) ? 0 : no.hashCode());
        result = prime * result + ((remark == null) ? 0 : remark.hashCode());
        result = prime * result + ((remover == null) ? 0 : remover.hashCode());
        result = prime * result + ((removerNo == null) ? 0 : removerNo.hashCode());
        result = prime * result + ((removerTime == null) ? 0 : removerTime.hashCode());
        result = prime * result + status;
        result = prime * result + ((withDraw == null) ? 0 : withDraw.hashCode());
        result = prime * result + ((withDrawNo == null) ? 0 : withDrawNo.hashCode());
        result = prime * result + ((withDrawTime == null) ? 0 : withDrawTime.hashCode());
        result = prime * result + ((workContent == null) ? 0 : workContent.hashCode());
        result = prime * result + ((workPlace == null) ? 0 : workPlace.hashCode());
        result = prime * result + wtId;
        result = prime * result + wtTypeId;
        result = prime * result + yxbz;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        PtwIsolationBean other = (PtwIsolationBean) obj;
        if ( canceler == null ) {
            if ( other.canceler != null )
                return false;
        } else if ( !canceler.equals( other.canceler ) )
            return false;
        if ( cancelerNo == null ) {
            if ( other.cancelerNo != null )
                return false;
        } else if ( !cancelerNo.equals( other.cancelerNo ) )
            return false;
        if ( cancelerTime == null ) {
            if ( other.cancelerTime != null )
                return false;
        } else if ( !cancelerTime.equals( other.cancelerTime ) )
            return false;
        if ( createUserName == null ) {
            if ( other.createUserName != null )
                return false;
        } else if ( !createUserName.equals( other.createUserName ) )
            return false;
        if ( eqId == null ) {
            if ( other.eqId != null )
                return false;
        } else if ( !eqId.equals( other.eqId ) )
            return false;
        if ( eqName == null ) {
            if ( other.eqName != null )
                return false;
        } else if ( !eqName.equals( other.eqName ) )
            return false;
        if ( eqNo == null ) {
            if ( other.eqNo != null )
                return false;
        } else if ( !eqNo.equals( other.eqNo ) )
            return false;
        if ( executer == null ) {
            if ( other.executer != null )
                return false;
        } else if ( !executer.equals( other.executer ) )
            return false;
        if ( executerNo == null ) {
            if ( other.executerNo != null )
                return false;
        } else if ( !executerNo.equals( other.executerNo ) )
            return false;
        if ( executerTime == null ) {
            if ( other.executerTime != null )
                return false;
        } else if ( !executerTime.equals( other.executerTime ) )
            return false;
        if ( id != other.id )
            return false;
        if ( isStdWt != other.isStdWt )
            return false;
        if ( issueSuper == null ) {
            if ( other.issueSuper != null )
                return false;
        } else if ( !issueSuper.equals( other.issueSuper ) )
            return false;
        if ( issueSuperNo == null ) {
            if ( other.issueSuperNo != null )
                return false;
        } else if ( !issueSuperNo.equals( other.issueSuperNo ) )
            return false;
        if ( issuedTime == null ) {
            if ( other.issuedTime != null )
                return false;
        } else if ( !issuedTime.equals( other.issuedTime ) )
            return false;
        if ( issuer == null ) {
            if ( other.issuer != null )
                return false;
        } else if ( !issuer.equals( other.issuer ) )
            return false;
        if ( issuerNo == null ) {
            if ( other.issuerNo != null )
                return false;
        } else if ( !issuerNo.equals( other.issuerNo ) )
            return false;
        if ( keyBoxId != other.keyBoxId )
            return false;
        if ( keyBoxNo == null ) {
            if ( other.keyBoxNo != null )
                return false;
        } else if ( !keyBoxNo.equals( other.keyBoxNo ) )
            return false;
        if ( no == null ) {
            if ( other.no != null )
                return false;
        } else if ( !no.equals( other.no ) )
            return false;
        if ( remark == null ) {
            if ( other.remark != null )
                return false;
        } else if ( !remark.equals( other.remark ) )
            return false;
        if ( remover == null ) {
            if ( other.remover != null )
                return false;
        } else if ( !remover.equals( other.remover ) )
            return false;
        if ( removerNo == null ) {
            if ( other.removerNo != null )
                return false;
        } else if ( !removerNo.equals( other.removerNo ) )
            return false;
        if ( removerTime == null ) {
            if ( other.removerTime != null )
                return false;
        } else if ( !removerTime.equals( other.removerTime ) )
            return false;
        if ( status != other.status )
            return false;
        if ( withDraw == null ) {
            if ( other.withDraw != null )
                return false;
        } else if ( !withDraw.equals( other.withDraw ) )
            return false;
        if ( withDrawNo == null ) {
            if ( other.withDrawNo != null )
                return false;
        } else if ( !withDrawNo.equals( other.withDrawNo ) )
            return false;
        if ( withDrawTime == null ) {
            if ( other.withDrawTime != null )
                return false;
        } else if ( !withDrawTime.equals( other.withDrawTime ) )
            return false;
        if ( workContent == null ) {
            if ( other.workContent != null )
                return false;
        } else if ( !workContent.equals( other.workContent ) )
            return false;
        if ( workPlace == null ) {
            if ( other.workPlace != null )
                return false;
        } else if ( !workPlace.equals( other.workPlace ) )
            return false;
        if ( wtId != other.wtId )
            return false;
        if ( wtTypeId != other.wtTypeId )
            return false;
        if ( yxbz != other.yxbz )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PtwIsolationBean [id=" + id + ", wtId=" + wtId + ", no=" + no + ", workContent=" + workContent
                + ", workPlace=" + workPlace + ", keyBoxId=" + keyBoxId + ", yxbz=" + yxbz + ", eqId=" + eqId
                + ", eqNo=" + eqNo + ", eqName=" + eqName + ", issuerNo=" + issuerNo + ", issuer=" + issuer
                + ", issuedTime=" + issuedTime + ", issueSuper=" + issueSuper + ", issueSuperNo=" + issueSuperNo
                + ", executer=" + executer + ", executerNo=" + executerNo + ", executerTime=" + executerTime
                + ", withDraw=" + withDraw + ", withDrawNo=" + withDrawNo + ", withDrawTime=" + withDrawTime
                + ", remover=" + remover + ", removerNo=" + removerNo + ", removerTime=" + removerTime + ", remark="
                + remark + ", status=" + status + ", isStdWt=" + isStdWt + ", keyBoxNo=" + keyBoxNo + ", wtTypeId="
                + wtTypeId + ", createUserName=" + createUserName + ", cancelerNo=" + cancelerNo + ", canceler="
                + canceler + ", cancelerTime=" + cancelerTime + "]";
    }

    
    
}
