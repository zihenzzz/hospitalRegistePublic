package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.PPay;

public interface PPayMapper 
{
    public PPay selectPPayByPayId(Long payId);
    public List<PPay> selectPPayList(PPay pPay);
    public int insertPPay(PPay pPay);
    public int updatePPay(PPay pPay);
    public int deletePPayByPayId(Long payId);
    public int deletePPayByPayIds(Long[] payIds);
}


