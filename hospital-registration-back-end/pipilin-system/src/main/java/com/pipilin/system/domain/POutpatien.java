package com.pipilin.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.pipilin.common.annotation.Excel;
import com.pipilin.common.core.domain.BaseEntity;

/**
 * 门诊信息对象 p_outpatien
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public class POutpatien extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 门诊ID */
    private Long outpatienId;

    /** 科室ID */
    @Excel(name = "科室ID")
    private Long sectionId;

    /** 门诊名称 */
    @Excel(name = "门诊名称")
    private String outpatienName;

    /** 诊室名称 */
    @Excel(name = "诊室名称")
    private String roomName;

    /** 诊室编号 */
    @Excel(name = "诊室编号")
    private String roomNumber;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String sectionName;

    public void setOutpatienId(Long outpatienId) 
    {
        this.outpatienId = outpatienId;
    }

    public Long getOutpatienId() 
    {
        return outpatienId;
    }

    public void setSectionId(Long sectionId) 
    {
        this.sectionId = sectionId;
    }

    public Long getSectionId() 
    {
        return sectionId;
    }

    public void setOutpatienName(String outpatienName) 
    {
        this.outpatienName = outpatienName;
    }

    public String getOutpatienName() 
    {
        return outpatienName;
    }

    public void setRoomName(String roomName) 
    {
        this.roomName = roomName;
    }

    public String getRoomName() 
    {
        return roomName;
    }

    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() 
    {
        return roomNumber;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public String getSectionName() 
    {
        return sectionName;
    }

    public void setSectionName(String sectionName) 
    {
        this.sectionName = sectionName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("outpatienId", getOutpatienId())
            .append("sectionId", getSectionId())
            .append("outpatienName", getOutpatienName())
            .append("roomName", getRoomName())
            .append("roomNumber", getRoomNumber())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}



