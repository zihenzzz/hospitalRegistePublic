package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PSection;

/**
 * 科室信息Service接口
 */
public interface IPSectionService
{
    public PSection selectPSectionBySectionId(Long sectionId);

    public List<PSection> selectPSectionList(PSection pSection);

    public int insertPSection(PSection pSection);

    public int updatePSection(PSection pSection);

    public int deletePSectionBySectionIds(Long[] sectionIds);

    public int deletePSectionBySectionId(Long sectionId);
}
