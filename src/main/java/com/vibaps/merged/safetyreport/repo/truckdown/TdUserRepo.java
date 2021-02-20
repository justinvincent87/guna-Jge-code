package com.vibaps.merged.safetyreport.repo.truckdown;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;

@Repository
public interface TdUserRepo extends JpaRepository<TdUser, Long>{

	@Modifying
	@Query(value="update td_user set token=:token,grant_type=:grantType,access_token=:accessToken where id=:Id",nativeQuery = true)
	public int update(String token,String grantType,String accessToken,Long Id);
	


}
