package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PSection;

/**
 * 科室信息Service接口
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public interface IPSectionService
{
    /**
     * 查询科室信息列表
     * 
     * @param pSection 科室信息
     * @return 科室信息集合
     */
    public List<PSection> selectPSectionList(PSection pSection);

    /**
     * 新增科室信息
     * 
     * @param pSection 科室信息
     * @return 结果
     */
    public int insertPSection(PSection pSection);

    /**
     * 修改科室信息
     * 
     * @param pSection 科室信息
     * @return 结果
     */
    public int updatePSection(PSection pSection);

    /**
     * 批量删除科室信息
     * 
     * @param ids 需要删除的科室信息主键集合
     * @return 结果
     */
    public int deletePSectionByIds(Long[] ids);

    /**
     * 删除科室信息信息
     * 
     * @param id 科室信息主键
     * @return 结果
     */
    public int deletePSectionById(Long id);
}


