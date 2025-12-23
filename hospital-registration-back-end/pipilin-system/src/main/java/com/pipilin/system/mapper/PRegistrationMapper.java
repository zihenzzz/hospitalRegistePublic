package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.PRegistration;

public interface PRegistrationMapper 
{
    public PRegistration selectPRegistrationByRegistrationId(Long registrationId);
    public List<PRegistration> selectPRegistrationList(PRegistration pRegistration);
    public int insertPRegistration(PRegistration pRegistration);
    public int updatePRegistration(PRegistration pRegistration);
    public int deletePRegistrationByRegistrationId(Long registrationId);
    public int deletePRegistrationByRegistrationIds(Long[] registrationIds);
}


