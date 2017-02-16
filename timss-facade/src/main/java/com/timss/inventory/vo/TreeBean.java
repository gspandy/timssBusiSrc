package com.timss.inventory.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: TreeBean.java
 * @author: 890166
 * @createDate: 2014-7-11
 * @updateUser: 890166
 * @version: 1.0
 */
public class TreeBean {

    private String id;
    private String text;
    private String state;
    private String type;

    private List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();

    /**
     * @return the children
     */
    public List<HashMap<String, Object>> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<HashMap<String, Object>> children) {
        this.children = children;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
