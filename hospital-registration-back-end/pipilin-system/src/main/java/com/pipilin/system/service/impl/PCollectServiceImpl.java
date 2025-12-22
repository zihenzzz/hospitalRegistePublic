package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.PCollectMapper;
import com.pipilin.system.domain.PCollect;
import com.pipilin.system.service.IPCollectService;

@Service
public class PCollectServiceImpl implements IPCollectService 
{
    @Autowired
    private PCollectMapper pCollectMapper;

    @Override
    public PCollect selectPCollectByCollectId(Long collectId)
    {
        return pCollectMapper.selectPCollectByCollectId(collectId);
    }

    @Override
    public List<PCollect> selectPCollectList(PCollect pCollect)
    {
        return pCollectMapper.selectPCollectList(pCollect);
    }

    @Override
    public int insertPCollect(PCollect pCollect)
    {
        pCollect.setCreateTime(DateUtils.getNowDate());
        return pCollectMapper.insertPCollect(pCollect);
    }

    @Override
    public int updatePCollect(PCollect pCollect)
    {
        pCollect.setUpdateTime(DateUtils.getNowDate());
        return pCollectMapper.updatePCollect(pCollect);
    }

    @Override
    public int deletePCollectByCollectIds(Long[] collectIds)
    {
        return pCollectMapper.deletePCollectByCollectIds(collectIds);
    }

    @Override
    public int deletePCollectByCollectId(Long collectId)
    {
        return pCollectMapper.deletePCollectByCollectId(collectId);
    }
}
