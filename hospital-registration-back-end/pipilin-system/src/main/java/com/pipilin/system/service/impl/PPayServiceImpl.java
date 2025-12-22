package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.PPayMapper;
import com.pipilin.system.domain.PPay;
import com.pipilin.system.service.IPPayService;

@Service
public class PPayServiceImpl implements IPPayService 
{
    @Autowired
    private PPayMapper pPayMapper;

    @Override
    public PPay selectPPayByPayId(Long payId)
    {
        return pPayMapper.selectPPayByPayId(payId);
    }

    @Override
    public List<PPay> selectPPayList(PPay pPay)
    {
        return pPayMapper.selectPPayList(pPay);
    }

    @Override
    public int insertPPay(PPay pPay)
    {
        pPay.setCreateTime(DateUtils.getNowDate());
        return pPayMapper.insertPPay(pPay);
    }

    @Override
    public int updatePPay(PPay pPay)
    {
        pPay.setUpdateTime(DateUtils.getNowDate());
        return pPayMapper.updatePPay(pPay);
    }

    @Override
    public int deletePPayByPayIds(Long[] payIds)
    {
        return pPayMapper.deletePPayByPayIds(payIds);
    }

    @Override
    public int deletePPayByPayId(Long payId)
    {
        return pPayMapper.deletePPayByPayId(payId);
    }
}

