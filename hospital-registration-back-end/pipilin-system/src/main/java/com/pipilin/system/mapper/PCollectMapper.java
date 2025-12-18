package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.PCollect;

/**
 * 医生收藏Mapper接口
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public interface PCollectMapper 
{
    /**
     * 查询医生收藏
     * 
     * @param collectId 医生收藏主键
     * @return 医生收藏
     */
    public PCollect selectPCollectByCollectId(Long collectId);

    /**
     * 查询医生收藏列表
     * 
     * @param pCollect 医生收藏
     * @return 医生收藏集合
     */
    public List<PCollect> selectPCollectList(PCollect pCollect);

    /**
     * 新增医生收藏
     * 
     * @param pCollect 医生收藏
     * @return 结果
     */
    public int insertPCollect(PCollect pCollect);

    /**
     * 修改医生收藏
     * 
     * @param pCollect 医生收藏
     * @return 结果
     */
    public int updatePCollect(PCollect pCollect);

    /**
     * 删除医生收藏
     * 
     * @param collectId 医生收藏主键
     * @return 结果
     */
    public int deletePCollectByCollectId(Long collectId);

    /**
     * 批量删除医生收藏
     * 
     * @param collectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePCollectByCollectIds(Long[] collectIds);
}

