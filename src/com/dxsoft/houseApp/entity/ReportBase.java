package com.dxsoft.houseApp.entity;


import java.math.BigDecimal;
import java.util.Date;

public class ReportBase implements java.io.Serializable {
    // 主键
    private BigDecimal sid;
    // 名称
    private String name;
    private String content;
    private Date createTime;
    private BigDecimal createUserSid;
    private String createUserName;
    private String shareUserName;
    private String isShare;

    public ReportBase() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSid() {
        return sid;
    }

    public void setSid(BigDecimal sid) {
        this.sid = sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getShareUserName() {
        return shareUserName;
    }

    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public void setCreateUserSid(BigDecimal createUserSid) {
        this.createUserSid = createUserSid;
    }

    public BigDecimal getCreateUserSid() {
        return createUserSid;
    }
}
