package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.POutpatien;

public interface POutpatienMapper 
{
    public POutpatien selectPOutpatienByOutpatienId(Long outpatienId);
    public List<POutpatien> selectPOutpatienList(POutpatien pOutpatien);
    public int insertPOutpatien(POutpatien pOutpatien);
    public int updatePOutpatien(POutpatien pOutpatien);
    public int deletePOutpatienByOutpatienId(Long outpatienId);
    public int deletePOutpatienByOutpatienIds(Long[] outpatienIds);
}

