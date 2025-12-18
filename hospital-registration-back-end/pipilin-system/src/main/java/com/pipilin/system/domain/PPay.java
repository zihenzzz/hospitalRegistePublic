package com.pipilin.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.pipilin.common.annotation.Excel;
import com.pipilin.common.core.domain.BaseEntity;

/**
 * 充值记录对象 p_pay
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public class PPay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 充值ID */
    private Long payId;

    /** 充值单号 */
    @Excel(name = "充值单号")
    private String payNo;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal payAmount;

    /** 充值类型（1微信 2支付宝 3现金） */
    @Excel(name = "充值类型", readConverterExp = "1=微信,2=支付宝,3=现金")
    private String payType;

    /** 充值状态（0待支付 1已支付 2已退款 3已取消） */
    @Excel(name = "充值状态", readConverterExp = "0=待支付,1=已支付,2=已退款,3=已取消")
    private String payStatus;

    /** 交易流水号 */
    @Excel(name = "交易流水号")
    private String transactionId;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 退款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "退款时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date refundTime;

    /** 退款原因 */
    @Excel(name = "退款原因")
    private String refundReason;

    /** 充值前余额 */
    @Excel(name = "充值前余额")
    private BigDecimal balanceBefore;

    /** 充值后余额 */
    @Excel(name = "充值后余额")
    private BigDecimal balanceAfter;

    public void setPayId(Long payId) 
    {
        this.payId = payId;
    }

    public Long getPayId() 
    {
        return payId;
    }

    public void setPayNo(String payNo) 
    {
        this.payNo = payNo;
    }

    public String getPayNo() 
    {
        return payNo;
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

    public void setPayAmount(BigDecimal payAmount) 
    {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() 
    {
        return payAmount;
    }

    public void setPayType(String payType) 
    {
        this.payType = payType;
    }

    public String getPayType() 
    {
        return payType;
    }

    public void setPayStatus(String payStatus) 
    {
        this.payStatus = payStatus;
    }

    public String getPayStatus() 
    {
        return payStatus;
    }

    public void setTransactionId(String transactionId) 
    {
        this.transactionId = transactionId;
    }

    public String getTransactionId() 
    {
        return transactionId;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setRefundTime(Date refundTime) 
    {
        this.refundTime = refundTime;
    }

    public Date getRefundTime() 
    {
        return refundTime;
    }

    public void setRefundReason(String refundReason) 
    {
        this.refundReason = refundReason;
    }

    public String getRefundReason() 
    {
        return refundReason;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) 
    {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceBefore() 
    {
        return balanceBefore;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) 
    {
        this.balanceAfter = balanceAfter;
    }

    public BigDecimal getBalanceAfter() 
    {
        return balanceAfter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("payId", getPayId())
            .append("payNo", getPayNo())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("payAmount", getPayAmount())
            .append("payType", getPayType())
            .append("payStatus", getPayStatus())
            .append("transactionId", getTransactionId())
            .append("payTime", getPayTime())
            .append("refundTime", getRefundTime())
            .append("refundReason", getRefundReason())
            .append("balanceBefore", getBalanceBefore())
            .append("balanceAfter", getBalanceAfter())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

