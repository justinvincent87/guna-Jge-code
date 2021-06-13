package com.vibaps.merged.safetyreport.repo.gl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.vibaps.merged.safetyreport.entity.gl.GenDriverUsers;

@Repository
public interface GenDriverUsersRepo extends JpaRepository<GenDriverUsers, Long>{

    @Query("select d from GenDriverUsers d where d.driverId=:driverId and d.refComDatabaseId=:refComDatabaseId")
    @Cacheable(value = "driverCount")
    GenDriverUsers findByuserIdAndrefComDatabaseId(String driverId,Long refComDatabaseId);
	
	@Query("select count(d.id) from GenDriverUsers d where d.driverId=:driverId and d.refComDatabaseId=:refComDatabaseId")
	Long countdriverIdAndrefComDatabaseId(String driverId,Long refComDatabaseId);

}
