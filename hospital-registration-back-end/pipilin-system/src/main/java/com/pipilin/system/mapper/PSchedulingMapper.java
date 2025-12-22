package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.PScheduling;

public interface PSchedulingMapper 
{
    public PScheduling selectPSchedulingBySchedulingId(Long schedulingId);
    public List<PScheduling> selectPSchedulingList(PScheduling pScheduling);
    public int insertPScheduling(PScheduling pScheduling);
    public int updatePScheduling(PScheduling pScheduling);
    public int deletePSchedulingBySchedulingId(Long schedulingId);
    public int deletePSchedulingBySchedulingIds(Long[] schedulingIds);
}

