package com.pipilin.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.PCollectMapper;
import com.pipilin.system.domain.PCollect;
import com.pipilin.system.service.IPCollectService;

/**
 * 医生收藏Service业务层处理
 * 
 * @author pipilin
 * @date 2024-12-18
 */
@Service
public class PCollectServiceImpl implements IPCollectService 
{
    @Autowired
    private PCollectMapper pCollectMapper;

    /**
     * 查询医生收藏列表
     * 
     * @param pCollect 医生收藏
     * @return 医生收藏
     */
    @Override
    public List<PCollect> selectPCollectList(PCollect pCollect)
    {
        return pCollectMapper.selectPCollectList(pCollect);
    }

    /**
     * 新增医生收藏
     * 
     * @param pCollect 医生收藏
     * @return 结果
     */
    @Override
    public int insertPCollect(PCollect pCollect)
    {
        return pCollectMapper.insertPCollect(pCollect);
    }

    /**
     * 修改医生收藏
     * 
     * @param pCollect 医生收藏
     * @return 结果
     */
    @Override
    public int updatePCollect(PCollect pCollect)
    {
        return pCollectMapper.updatePCollect(pCollect);
    }

    /**
     * 批量删除医生收藏
     * 
     * @param ids 需要删除的医生收藏主键
     * @return 结果
     */
    @Override
    public int deletePCollectByIds(Long[] ids)
    {
        return pCollectMapper.deletePCollectByCollectIds(ids);
    }

    /**
     * 删除医生收藏信息
     * 
     * @param id 医生收藏主键
     * @return 结果
     */
    @Override
    public int deletePCollectById(Long id)
    {
        return pCollectMapper.deletePCollectByCollectId(id);
    }
}

