package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.PRegistrationMapper;
import com.pipilin.system.domain.PRegistration;
import com.pipilin.system.service.IPRegistrationService;

@Service
public class PRegistrationServiceImpl implements IPRegistrationService 
{
    @Autowired
    private PRegistrationMapper pRegistrationMapper;

    @Override
    public PRegistration selectPRegistrationByRegistrationId(Long registrationId)
    {
        return pRegistrationMapper.selectPRegistrationByRegistrationId(registrationId);
    }

    @Override
    public List<PRegistration> selectPRegistrationList(PRegistration pRegistration)
    {
        return pRegistrationMapper.selectPRegistrationList(pRegistration);
    }

    @Override
    public int insertPRegistration(PRegistration pRegistration)
    {
        pRegistration.setCreateTime(DateUtils.getNowDate());
        return pRegistrationMapper.insertPRegistration(pRegistration);
    }

    @Override
    public int updatePRegistration(PRegistration pRegistration)
    {
        pRegistration.setUpdateTime(DateUtils.getNowDate());
        return pRegistrationMapper.updatePRegistration(pRegistration);
    }

    @Override
    public int deletePRegistrationByRegistrationIds(Long[] registrationIds)
    {
        return pRegistrationMapper.deletePRegistrationByRegistrationIds(registrationIds);
    }

    @Override
    public int deletePRegistrationByRegistrationId(Long registrationId)
    {
        return pRegistrationMapper.deletePRegistrationByRegistrationId(registrationId);
    }
}


