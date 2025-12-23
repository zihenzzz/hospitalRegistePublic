package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PCollect;

/**
 * 医生收藏Service接口
 */
public interface IPCollectService
{
    public PCollect selectPCollectByCollectId(Long collectId);

    public List<PCollect> selectPCollectList(PCollect pCollect);

    public int insertPCollect(PCollect pCollect);

    public int updatePCollect(PCollect pCollect);

    public int deletePCollectByCollectIds(Long[] collectIds);

    public int deletePCollectByCollectId(Long collectId);
}
