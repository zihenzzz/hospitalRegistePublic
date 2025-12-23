package com.pipilin.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.pipilin.common.annotation.Excel;
import com.pipilin.common.core.domain.BaseEntity;

/**
 * 医生排班信息对象 p_scheduling
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public class PScheduling extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 排班ID */
    private Long schedulingId;

    /** 医生ID */
    @Excel(name = "医生ID")
    private Long doctorId;

    /** 科室ID */
    @Excel(name = "科室ID")
    private Long sectionId;

    /** 门诊ID */
    @Excel(name = "门诊ID")
    private Long outpatienId;

    /** 排班日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "排班日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date schedulingDate;

    /** 时间段（上午/下午/晚上） */
    @Excel(name = "时间段")
    private String timeSlot;

    /** 开始时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "HH:mm:ss")
    private Date endTime;

    /** 总号源数 */
    @Excel(name = "总号源数")
    private Integer totalNumber;

    /** 剩余号源数 */
    @Excel(name = "剩余号源数")
    private Integer residualNumber;

    /** 挂号费 */
    @Excel(name = "挂号费")
    private BigDecimal registrationFee;

    /** 状态（0正常 1停用 2已满） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用,2=已满")
    private String status;

    /** 医生姓名 */
    @Excel(name = "医生姓名")
    private String doctorName;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String sectionName;

    /** 门诊名称 */
    @Excel(name = "门诊名称")
    private String outpatienName;

    public void setSchedulingId(Long schedulingId) 
    {
        this.schedulingId = schedulingId;
    }

    public Long getSchedulingId() 
    {
        return schedulingId;
    }

    public void setDoctorId(Long doctorId) 
    {
        this.doctorId = doctorId;
    }

    public Long getDoctorId() 
    {
        return doctorId;
    }

    public void setSectionId(Long sectionId) 
    {
        this.sectionId = sectionId;
    }

    public Long getSectionId() 
    {
        return sectionId;
    }

    public void setOutpatienId(Long outpatienId) 
    {
        this.outpatienId = outpatienId;
    }

    public Long getOutpatienId() 
    {
        return outpatienId;
    }

    public void setSchedulingDate(Date schedulingDate) 
    {
        this.schedulingDate = schedulingDate;
    }

    public Date getSchedulingDate() 
    {
        return schedulingDate;
    }

    public void setTimeSlot(String timeSlot) 
    {
        this.timeSlot = timeSlot;
    }

    public String getTimeSlot() 
    {
        return timeSlot;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setTotalNumber(Integer totalNumber) 
    {
        this.totalNumber = totalNumber;
    }

    public Integer getTotalNumber() 
    {
        return totalNumber;
    }

    public void setResidualNumber(Integer residualNumber) 
    {
        this.residualNumber = residualNumber;
    }

    public Integer getResidualNumber() 
    {
        return residualNumber;
    }

    public void setRegistrationFee(BigDecimal registrationFee) 
    {
        this.registrationFee = registrationFee;
    }

    public BigDecimal getRegistrationFee() 
    {
        return registrationFee;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public String getDoctorName() 
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName) 
    {
        this.doctorName = doctorName;
    }

    public String getSectionName() 
    {
        return sectionName;
    }

    public void setSectionName(String sectionName) 
    {
        this.sectionName = sectionName;
    }

    public String getOutpatienName() 
    {
        return outpatienName;
    }

    public void setOutpatienName(String outpatienName) 
    {
        this.outpatienName = outpatienName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("schedulingId", getSchedulingId())
            .append("doctorId", getDoctorId())
            .append("sectionId", getSectionId())
            .append("outpatienId", getOutpatienId())
            .append("schedulingDate", getSchedulingDate())
            .append("timeSlot", getTimeSlot())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("totalNumber", getTotalNumber())
            .append("residualNumber", getResidualNumber())
            .append("registrationFee", getRegistrationFee())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}



