package com.pipilin.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.pipilin.common.annotation.Excel;
import com.pipilin.common.core.domain.BaseEntity;

/**
 * 预约挂号信息对象 p_registration
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public class PRegistration extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 挂号ID */
    private Long registrationId;

    /** 挂号单号 */
    @Excel(name = "挂号单号")
    private String registrationNo;

    /** 患者ID */
    @Excel(name = "患者ID")
    private Long patientId;

    /** 患者姓名 */
    @Excel(name = "患者姓名")
    private String patientName;

    /** 患者电话 */
    @Excel(name = "患者电话")
    private String patientPhone;

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

    /** 门诊ID */
    @Excel(name = "门诊ID")
    private Long outpatienId;

    /** 排班ID */
    @Excel(name = "排班ID")
    private Long schedulingId;

    /** 挂号日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "挂号日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date registrationDate;

    /** 时间段 */
    @Excel(name = "时间段")
    private String timeSlot;

    /** 挂号费 */
    @Excel(name = "挂号费")
    private BigDecimal registrationFee;

    /** 状态（0待就诊 1已就诊 2已取消 3已过期） */
    @Excel(name = "状态", readConverterExp = "0=待就诊,1=已就诊,2=已取消,3=已过期")
    private String status;

    /** 支付状态（0未支付 1已支付 2已退款） */
    @Excel(name = "支付状态", readConverterExp = "0=未支付,1=已支付,2=已退款")
    private String payStatus;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 取消时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "取消时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    /** 取消原因 */
    @Excel(name = "取消原因")
    private String cancelReason;

    public void setRegistrationId(Long registrationId) 
    {
        this.registrationId = registrationId;
    }

    public Long getRegistrationId() 
    {
        return registrationId;
    }

    public void setRegistrationNo(String registrationNo) 
    {
        this.registrationNo = registrationNo;
    }

    public String getRegistrationNo() 
    {
        return registrationNo;
    }

    public void setPatientId(Long patientId) 
    {
        this.patientId = patientId;
    }

    public Long getPatientId() 
    {
        return patientId;
    }

    public void setPatientName(String patientName) 
    {
        this.patientName = patientName;
    }

    public String getPatientName() 
    {
        return patientName;
    }

    public void setPatientPhone(String patientPhone) 
    {
        this.patientPhone = patientPhone;
    }

    public String getPatientPhone() 
    {
        return patientPhone;
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

    public void setOutpatienId(Long outpatienId) 
    {
        this.outpatienId = outpatienId;
    }

    public Long getOutpatienId() 
    {
        return outpatienId;
    }

    public void setSchedulingId(Long schedulingId) 
    {
        this.schedulingId = schedulingId;
    }

    public Long getSchedulingId() 
    {
        return schedulingId;
    }

    public void setRegistrationDate(Date registrationDate) 
    {
        this.registrationDate = registrationDate;
    }

    public Date getRegistrationDate() 
    {
        return registrationDate;
    }

    public void setTimeSlot(String timeSlot) 
    {
        this.timeSlot = timeSlot;
    }

    public String getTimeSlot() 
    {
        return timeSlot;
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

    public void setPayStatus(String payStatus) 
    {
        this.payStatus = payStatus;
    }

    public String getPayStatus() 
    {
        return payStatus;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setCancelTime(Date cancelTime) 
    {
        this.cancelTime = cancelTime;
    }

    public Date getCancelTime() 
    {
        return cancelTime;
    }

    public void setCancelReason(String cancelReason) 
    {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() 
    {
        return cancelReason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("registrationId", getRegistrationId())
            .append("registrationNo", getRegistrationNo())
            .append("patientId", getPatientId())
            .append("patientName", getPatientName())
            .append("patientPhone", getPatientPhone())
            .append("doctorId", getDoctorId())
            .append("doctorName", getDoctorName())
            .append("sectionId", getSectionId())
            .append("sectionName", getSectionName())
            .append("outpatienId", getOutpatienId())
            .append("schedulingId", getSchedulingId())
            .append("registrationDate", getRegistrationDate())
            .append("timeSlot", getTimeSlot())
            .append("registrationFee", getRegistrationFee())
            .append("status", getStatus())
            .append("payStatus", getPayStatus())
            .append("payTime", getPayTime())
            .append("cancelTime", getCancelTime())
            .append("cancelReason", getCancelReason())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}


