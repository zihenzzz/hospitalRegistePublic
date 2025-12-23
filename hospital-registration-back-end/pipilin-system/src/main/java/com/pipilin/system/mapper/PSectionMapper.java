package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.PSection;

/**
 * 科室Mapper接口
 * 
 * @author pipilin
 * @date 2024-12-22
 */
public interface PSectionMapper 
{
    /**
     * 查询科室
     * 
     * @param sectionId 科室主键
     * @return 科室
     */
    public PSection selectPSectionBySectionId(Long sectionId);

    /**
     * 查询科室列表
     * 
     * @param pSection 科室
     * @return 科室集合
     */
    public List<PSection> selectPSectionList(PSection pSection);

    /**
     * 新增科室
     * 
     * @param pSection 科室
     * @return 结果
     */
    public int insertPSection(PSection pSection);

    /**
     * 修改科室
     * 
     * @param pSection 科室
     * @return 结果
     */
    public int updatePSection(PSection pSection);

    /**
     * 删除科室
     * 
     * @param sectionId 科室主键
     * @return 结果
     */
    public int deletePSectionBySectionId(Long sectionId);

    /**
     * 批量删除科室
     * 
     * @param sectionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePSectionBySectionIds(Long[] sectionIds);
}


