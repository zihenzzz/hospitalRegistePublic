package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.PEvaluate;

public interface PEvaluateMapper 
{
    public PEvaluate selectPEvaluateByEvaluateId(Long evaluateId);
    public List<PEvaluate> selectPEvaluateList(PEvaluate pEvaluate);
    public int insertPEvaluate(PEvaluate pEvaluate);
    public int updatePEvaluate(PEvaluate pEvaluate);
    public int deletePEvaluateByEvaluateId(Long evaluateId);
    public int deletePEvaluateByEvaluateIds(Long[] evaluateIds);
}

