package com.timss.asset.bean;

/**
 * @title: 附件的Bean。
 * @description: {desc}
 * @company: gdyd
 * @className: AttachmentBean.java
 * @author: 890165
 * @createDate: 2014-7-3
 * @updateUser: 890165
 * @version: 1.0
 */
public class AttachmentBean {
    private String fileID; //文件id
    private String fileName; //文件名
    private int fileSize; //文件大小
    /**
     * @return the fileID
     */
    public String getFileID() {
        return fileID;
    }
    /**
     * @param fileID the fileID to set
     */
    public void setFileID(String fileID) {
        this.fileID = fileID;
    }
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return the fileSize
     */
    public int getFileSize() {
        return fileSize;
    }
    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    
    
}
