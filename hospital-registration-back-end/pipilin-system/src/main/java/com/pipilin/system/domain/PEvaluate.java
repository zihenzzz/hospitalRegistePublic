package com.pipilin.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.pipilin.common.annotation.Excel;
import com.pipilin.common.core.domain.BaseEntity;

/**
 * 评价信息对象 p_evaluate
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public class PEvaluate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价ID */
    private Long evaluateId;

    /** 挂号ID */
    @Excel(name = "挂号ID")
    private Long registrationId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    /** 医生ID */
    @Excel(name = "医生ID")
    private Long doctorId;

    /** 医生姓名 */
    @Excel(name = "医生姓名")
    private String doctorName;

    /** 科室ID */
    @Excel(name = "科室ID")
    private Long sectionId;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String sectionName;

    /** 评分（1-5星） */
    @Excel(name = "评分")
    private Integer rating;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String evaluateContent;

    /** 状态（0正常 1隐藏） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=隐藏")
    private String status;

    public void setEvaluateId(Long evaluateId) 
    {
        this.evaluateId = evaluateId;
    }

    public Long getEvaluateId() 
    {
        return evaluateId;
    }

    public void setRegistrationId(Long registrationId) 
    {
        this.registrationId = registrationId;
    }

    public Long getRegistrationId() 
    {
        return registrationId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setDoctorId(Long doctorId) 
    {
        this.doctorId = doctorId;
    }

    public Long getDoctorId() 
    {
        return doctorId;
    }

    public void setDoctorName(String doctorName) 
    {
        this.doctorName = doctorName;
    }

    public String getDoctorName() 
    {
        return doctorName;
    }

    public void setSectionId(Long sectionId) 
    {
        this.sectionId = sectionId;
    }

    public Long getSectionId() 
    {
        return sectionId;
    }

    public void setSectionName(String sectionName) 
    {
        this.sectionName = sectionName;
    }

    public String getSectionName() 
    {
        return sectionName;
    }

    public void setRating(Integer rating) 
    {
        this.rating = rating;
    }

    public Integer getRating() 
    {
        return rating;
    }

    public void setEvaluateContent(String evaluateContent) 
    {
        this.evaluateContent = evaluateContent;
    }

    public String getEvaluateContent() 
    {
        return evaluateContent;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("evaluateId", getEvaluateId())
            .append("registrationId", getRegistrationId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("doctorId", getDoctorId())
            .append("doctorName", getDoctorName())
            .append("sectionId", getSectionId())
            .append("sectionName", getSectionName())
            .append("rating", getRating())
            .append("evaluateContent", getEvaluateContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

