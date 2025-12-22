package com.pipilin.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.pipilin.common.annotation.Excel;
import com.pipilin.common.core.domain.BaseEntity;

/**
 * 科室信息对象 p_section
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public class PSection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 科室ID */
    private Long sectionId;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String sectionName;

    /** 科室编码 */
    @Excel(name = "科室编码")
    private String sectionCode;

    /** 科室描述 */
    @Excel(name = "科室描述")
    private String sectionDesc;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

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

    public void setSectionCode(String sectionCode) 
    {
        this.sectionCode = sectionCode;
    }

    public String getSectionCode() 
    {
        return sectionCode;
    }

    public void setSectionDesc(String sectionDesc) 
    {
        this.sectionDesc = sectionDesc;
    }

    public String getSectionDesc() 
    {
        return sectionDesc;
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
            .append("sectionId", getSectionId())
            .append("sectionName", getSectionName())
            .append("sectionCode", getSectionCode())
            .append("sectionDesc", getSectionDesc())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}


