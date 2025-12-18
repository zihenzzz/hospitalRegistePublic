package com.pipilin.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.pipilin.common.annotation.Excel;
import com.pipilin.common.core.domain.BaseEntity;

/**
 * 收藏信息对象 p_collect
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public class PCollect extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long collectId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

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

    public void setCollectId(Long collectId) 
    {
        this.collectId = collectId;
    }

    public Long getCollectId() 
    {
        return collectId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("collectId", getCollectId())
            .append("userId", getUserId())
            .append("doctorId", getDoctorId())
            .append("doctorName", getDoctorName())
            .append("sectionId", getSectionId())
            .append("sectionName", getSectionName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}

