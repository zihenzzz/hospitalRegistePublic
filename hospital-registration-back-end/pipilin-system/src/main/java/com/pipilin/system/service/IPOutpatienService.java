package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.POutpatien;

/**
 * 就诊人信息Service接口
 */
public interface IPOutpatienService
{
    public POutpatien selectPOutpatienByOutpatienId(Long outpatienId);

    public List<POutpatien> selectPOutpatienList(POutpatien pOutpatien);

    public int insertPOutpatien(POutpatien pOutpatien);

    public int updatePOutpatien(POutpatien pOutpatien);

    public int deletePOutpatienByOutpatienIds(Long[] outpatienIds);

    public int deletePOutpatienByOutpatienId(Long outpatienId);
}
