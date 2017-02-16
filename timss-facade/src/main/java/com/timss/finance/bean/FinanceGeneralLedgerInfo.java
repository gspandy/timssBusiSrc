package com.timss.finance.bean;

import java.io.Serializable;
import java.util.Date;

public class FinanceGeneralLedgerInfo implements Serializable {
	private static final long serialVersionUID = -3087691489069073577L;
	
	private String seqno; //序号
	private String subject; //科目编号
	private String subjectremark; //科目描述
	private double debitamt; //借方金额
	private double creditamt; //贷方金额
	private String cashitem; //现金流项目
	private String intervalunit; //内部单位
	private String certrowdesc; //凭证行描述
	private String borrowdirection; //借贷方向
	private String subjectid; //科目编号
	private String subjectSeqNo; //
	
	public String getSubjectSeqNo() {
            return subjectSeqNo;
        }
        public void setSubjectSeqNo(String subjectSeqNo) {
            this.subjectSeqNo = subjectSeqNo;
        }
        public String getSeqno() {
		return seqno;
	}
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubjectremark() {
		return subjectremark;
	}
	public void setSubjectremark(String subjectremark) {
		this.subjectremark = subjectremark;
	}
	public double getDebitamt() {
		return debitamt;
	}
	public void setDebitamt(double debitamt) {
		this.debitamt = debitamt;
	}
	public double getCreditamt() {
		return creditamt;
	}
	public void setCreditamt(double creditamt) {
		this.creditamt = creditamt;
	}
	public String getCashitem() {
		return cashitem;
	}
	public void setCashitem(String cashitem) {
		this.cashitem = cashitem;
	}
	public String getIntervalunit() {
		return intervalunit;
	}
	public void setIntervalunit(String intervalunit) {
		this.intervalunit = intervalunit;
	}
	public String getCertrowdesc() {
		return certrowdesc;
	}
	public void setCertrowdesc(String certrowdesc) {
		this.certrowdesc = certrowdesc;
	}
	public String getBorrowdirection() {
		return borrowdirection;
	}
	public void setBorrowdirection(String borrowdirection) {
		this.borrowdirection = borrowdirection;
	}
	public String getSubjectid() {
		return subjectid;
	}
	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}
}
