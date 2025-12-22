package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PCollect;

/**
 * 医生收藏Service接口
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public interface IPCollectService
{
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
     * 批量删除医生收藏
     * 
     * @param ids 需要删除的医生收藏主键集合
     * @return 结果
     */
    public int deletePCollectByIds(Long[] ids);

    /**
     * 删除医生收藏信息
     * 
     * @param id 医生收藏主键
     * @return 结果
     */
    public int deletePCollectById(Long id);
}


