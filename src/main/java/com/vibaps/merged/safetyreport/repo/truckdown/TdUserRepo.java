package com.vibaps.merged.safetyreport.repo.truckdown;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;

@Repository
public interface TdUserRepo extends JpaRepository<TdUser, Long>{

	

}
