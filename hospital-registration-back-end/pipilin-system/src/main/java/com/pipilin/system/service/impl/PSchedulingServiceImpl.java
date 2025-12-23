package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.PSchedulingMapper;
import com.pipilin.system.domain.PScheduling;
import com.pipilin.system.service.IPSchedulingService;

@Service
public class PSchedulingServiceImpl implements IPSchedulingService 
{
    @Autowired
    private PSchedulingMapper pSchedulingMapper;

    @Override
    public PScheduling selectPSchedulingBySchedulingId(Long schedulingId)
    {
        return pSchedulingMapper.selectPSchedulingBySchedulingId(schedulingId);
    }

    @Override
    public List<PScheduling> selectPSchedulingList(PScheduling pScheduling)
    {
        return pSchedulingMapper.selectPSchedulingList(pScheduling);
    }

    @Override
    public int insertPScheduling(PScheduling pScheduling)
    {
        pScheduling.setCreateTime(DateUtils.getNowDate());
        return pSchedulingMapper.insertPScheduling(pScheduling);
    }

    @Override
    public int updatePScheduling(PScheduling pScheduling)
    {
        pScheduling.setUpdateTime(DateUtils.getNowDate());
        return pSchedulingMapper.updatePScheduling(pScheduling);
    }

    @Override
    public int deletePSchedulingBySchedulingIds(Long[] schedulingIds)
    {
        return pSchedulingMapper.deletePSchedulingBySchedulingIds(schedulingIds);
    }

    @Override
    public int deletePSchedulingBySchedulingId(Long schedulingId)
    {
        return pSchedulingMapper.deletePSchedulingBySchedulingId(schedulingId);
    }
}


