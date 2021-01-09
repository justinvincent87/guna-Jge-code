package com.vibaps.merged.safetyreport.services.truckdown;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;
import com.vibaps.merged.safetyreport.repo.truckdown.TdUserRepo;


public interface ITdUserService {

    Optional<TdUser> findById(Long id);
	
}
