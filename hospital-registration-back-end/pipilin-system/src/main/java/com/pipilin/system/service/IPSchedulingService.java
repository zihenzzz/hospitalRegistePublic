package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PScheduling;

/**
 * 排班信息Service接口
 */
public interface IPSchedulingService
{
    public PScheduling selectPSchedulingBySchedulingId(Long schedulingId);

    public List<PScheduling> selectPSchedulingList(PScheduling pScheduling);

    public int insertPScheduling(PScheduling pScheduling);

    public int updatePScheduling(PScheduling pScheduling);

    public int deletePSchedulingBySchedulingIds(Long[] schedulingIds);

    public int deletePSchedulingBySchedulingId(Long schedulingId);
}
