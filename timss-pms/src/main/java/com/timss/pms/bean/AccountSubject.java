package com.timss.pms.bean;


/**
 * 会计科目类型
 * @ClassName:     AccountSubject
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-8-25 上午9:25:21
 */
public class AccountSubject {
    private String content; //整个科目用字符串表示，如1.2.3.4.5.6.7.8.9.10
	
	private String segment1;//科目的组成部分，第1位，即例子的1
	private String segment2;//科目的组成部分，第2位，即例子的2
	private String segment3;//科目的组成部分，第3位，即例子的3
	private String segment4;//科目的组成部分，第4位，即例子的4
	private String segment5;//科目的组成部分，第5位，即例子的5
	private String segment6;//科目的组成部分，第6位，即例子的6
	private String segment7;//科目的组成部分，第7位，即例子的7
	private String segment8;//科目的组成部分，第8位，即例子的8
	private String segment9;//科目的组成部分，第9位，即例子的9
	private String segment10;//科目的组成部分，第10位，即例子的10
	
	public AccountSubject(){}
	public AccountSubject(String content){
		this.content=content;
	}
	
	/**
	 * 将会计科目拆分为多个子项
	 * @Title: dispart
	 */
	public void dispart(){
		String [] parts=content.split("\\.");
		segment1=parts[0];
		segment2=parts[1];
		segment3=parts[2];
		segment4=parts[3];
		segment5=parts[4];
		segment6=parts[5];
		segment7=parts[6];
		segment8=parts[7];
		segment9=parts[8];
		segment10=parts[9];
		
	}
	
	public void merge(){
		content=segment1+"."+segment2+"."+segment3+"."+segment4+"."+
					segment5+"."+segment6+"."+segment7+"."+segment8+"."+segment9+"."+segment10;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSegment1() {
		return segment1;
	}
	public void setSegment1(String segment1) {
		this.segment1 = segment1;
	}
	public String getSegment2() {
		return segment2;
	}
	public void setSegment2(String segment2) {
		this.segment2 = segment2;
	}
	public String getSegment3() {
		return segment3;
	}
	public void setSegment3(String segment3) {
		this.segment3 = segment3;
	}
	public String getSegment4() {
		return segment4;
	}
	public void setSegment4(String segment4) {
		this.segment4 = segment4;
	}
	public String getSegment5() {
		return segment5;
	}
	public void setSegment5(String segment5) {
		this.segment5 = segment5;
	}
	public String getSegment6() {
		return segment6;
	}
	public void setSegment6(String segment6) {
		this.segment6 = segment6;
	}
	public String getSegment7() {
		return segment7;
	}
	public void setSegment7(String segment7) {
		this.segment7 = segment7;
	}
	public String getSegment8() {
		return segment8;
	}
	public void setSegment8(String segment8) {
		this.segment8 = segment8;
	}
	public String getSegment9() {
		return segment9;
	}
	public void setSegment9(String segment9) {
		this.segment9 = segment9;
	}
	public String getSegment10() {
		return segment10;
	}
	public void setSegment10(String segment10) {
		this.segment10 = segment10;
	}
	

}
