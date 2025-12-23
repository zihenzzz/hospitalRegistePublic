package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.POutpatienMapper;
import com.pipilin.system.domain.POutpatien;
import com.pipilin.system.service.IPOutpatienService;

@Service
public class POutpatienServiceImpl implements IPOutpatienService 
{
    @Autowired
    private POutpatienMapper pOutpatienMapper;

    @Override
    public POutpatien selectPOutpatienByOutpatienId(Long outpatienId)
    {
        return pOutpatienMapper.selectPOutpatienByOutpatienId(outpatienId);
    }

    @Override
    public List<POutpatien> selectPOutpatienList(POutpatien pOutpatien)
    {
        return pOutpatienMapper.selectPOutpatienList(pOutpatien);
    }

    @Override
    public int insertPOutpatien(POutpatien pOutpatien)
    {
        pOutpatien.setCreateTime(DateUtils.getNowDate());
        return pOutpatienMapper.insertPOutpatien(pOutpatien);
    }

    @Override
    public int updatePOutpatien(POutpatien pOutpatien)
    {
        pOutpatien.setUpdateTime(DateUtils.getNowDate());
        return pOutpatienMapper.updatePOutpatien(pOutpatien);
    }

    @Override
    public int deletePOutpatienByOutpatienIds(Long[] outpatienIds)
    {
        return pOutpatienMapper.deletePOutpatienByOutpatienIds(outpatienIds);
    }

    @Override
    public int deletePOutpatienByOutpatienId(Long outpatienId)
    {
        return pOutpatienMapper.deletePOutpatienByOutpatienId(outpatienId);
    }
}


