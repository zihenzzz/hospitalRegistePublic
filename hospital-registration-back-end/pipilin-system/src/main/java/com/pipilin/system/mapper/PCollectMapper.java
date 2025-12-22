package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.PCollect;

public interface PCollectMapper 
{
    public PCollect selectPCollectByCollectId(Long collectId);
    public List<PCollect> selectPCollectList(PCollect pCollect);
    public int insertPCollect(PCollect pCollect);
    public int updatePCollect(PCollect pCollect);
    public int deletePCollectByCollectId(Long collectId);
    public int deletePCollectByCollectIds(Long[] collectIds);
}
