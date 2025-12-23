package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PPay;

/**
 * 支付记录Service接口
 */
public interface IPPayService
{
    public PPay selectPPayByPayId(Long payId);

    public List<PPay> selectPPayList(PPay pPay);

    public int insertPPay(PPay pPay);

    public int updatePPay(PPay pPay);

    public int deletePPayByPayIds(Long[] payIds);

    public int deletePPayByPayId(Long payId);
}
