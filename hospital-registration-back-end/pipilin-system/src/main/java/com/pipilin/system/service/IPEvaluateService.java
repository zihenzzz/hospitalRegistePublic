package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PEvaluate;

/**
 * 评价信息Service接口
 */
public interface IPEvaluateService
{
    public PEvaluate selectPEvaluateByEvaluateId(Long evaluateId);

    public List<PEvaluate> selectPEvaluateList(PEvaluate pEvaluate);

    public int insertPEvaluate(PEvaluate pEvaluate);

    public int updatePEvaluate(PEvaluate pEvaluate);

    public int deletePEvaluateByEvaluateIds(Long[] evaluateIds);

    public int deletePEvaluateByEvaluateId(Long evaluateId);
}
