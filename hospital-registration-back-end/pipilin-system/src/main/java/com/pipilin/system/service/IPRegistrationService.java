package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.PRegistration;

/**
 * 挂号信息Service接口
 */
public interface IPRegistrationService
{
    public PRegistration selectPRegistrationByRegistrationId(Long registrationId);

    public List<PRegistration> selectPRegistrationList(PRegistration pRegistration);

    public int insertPRegistration(PRegistration pRegistration);

    public int updatePRegistration(PRegistration pRegistration);

    public int deletePRegistrationByRegistrationIds(Long[] registrationIds);

    public int deletePRegistrationByRegistrationId(Long registrationId);
}
