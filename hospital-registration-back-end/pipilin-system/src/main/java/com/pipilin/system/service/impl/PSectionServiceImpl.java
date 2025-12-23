package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.PSectionMapper;
import com.pipilin.system.domain.PSection;
import com.pipilin.system.service.IPSectionService;

/**
 * 科室Service业务层处理
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@Service
public class PSectionServiceImpl implements IPSectionService 
{
    @Autowired
    private PSectionMapper pSectionMapper;

    /**
     * 查询科室
     * 
     * @param sectionId 科室主键
     * @return 科室
     */
    @Override
    public PSection selectPSectionBySectionId(Long sectionId)
    {
        return pSectionMapper.selectPSectionBySectionId(sectionId);
    }

    /**
     * 查询科室列表
     * 
     * @param pSection 科室
     * @return 科室
     */
    @Override
    public List<PSection> selectPSectionList(PSection pSection)
    {
        return pSectionMapper.selectPSectionList(pSection);
    }

    /**
     * 新增科室
     * 
     * @param pSection 科室
     * @return 结果
     */
    @Override
    public int insertPSection(PSection pSection)
    {
        pSection.setCreateTime(DateUtils.getNowDate());
        return pSectionMapper.insertPSection(pSection);
    }

    /**
     * 修改科室
     * 
     * @param pSection 科室
     * @return 结果
     */
    @Override
    public int updatePSection(PSection pSection)
    {
        pSection.setUpdateTime(DateUtils.getNowDate());
        return pSectionMapper.updatePSection(pSection);
    }

    /**
     * 批量删除科室
     * 
     * @param sectionIds 需要删除的科室主键
     * @return 结果
     */
    @Override
    public int deletePSectionBySectionIds(Long[] sectionIds)
    {
        return pSectionMapper.deletePSectionBySectionIds(sectionIds);
    }

    /**
     * 删除科室信息
     * 
     * @param sectionId 科室主键
     * @return 结果
     */
    @Override
    public int deletePSectionBySectionId(Long sectionId)
    {
        return pSectionMapper.deletePSectionBySectionId(sectionId);
    }
}


