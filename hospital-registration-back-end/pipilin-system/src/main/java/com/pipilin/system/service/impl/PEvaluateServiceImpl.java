package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.PEvaluateMapper;
import com.pipilin.system.domain.PEvaluate;
import com.pipilin.system.service.IPEvaluateService;

@Service
public class PEvaluateServiceImpl implements IPEvaluateService 
{
    @Autowired
    private PEvaluateMapper pEvaluateMapper;

    @Override
    public PEvaluate selectPEvaluateByEvaluateId(Long evaluateId)
    {
        return pEvaluateMapper.selectPEvaluateByEvaluateId(evaluateId);
    }

    @Override
    public List<PEvaluate> selectPEvaluateList(PEvaluate pEvaluate)
    {
        return pEvaluateMapper.selectPEvaluateList(pEvaluate);
    }

    @Override
    public int insertPEvaluate(PEvaluate pEvaluate)
    {
        pEvaluate.setCreateTime(DateUtils.getNowDate());
        return pEvaluateMapper.insertPEvaluate(pEvaluate);
    }

    @Override
    public int updatePEvaluate(PEvaluate pEvaluate)
    {
        pEvaluate.setUpdateTime(DateUtils.getNowDate());
        return pEvaluateMapper.updatePEvaluate(pEvaluate);
    }

    @Override
    public int deletePEvaluateByEvaluateIds(Long[] evaluateIds)
    {
        return pEvaluateMapper.deletePEvaluateByEvaluateIds(evaluateIds);
    }

    @Override
    public int deletePEvaluateByEvaluateId(Long evaluateId)
    {
        return pEvaluateMapper.deletePEvaluateByEvaluateId(evaluateId);
    }
}

