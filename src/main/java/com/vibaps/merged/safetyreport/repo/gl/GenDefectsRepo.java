package com.vibaps.merged.safetyreport.repo.gl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vibaps.merged.safetyreport.entity.gl.GenDefects;
import com.vibaps.merged.safetyreport.entity.gl.GenDevice;

public interface GenDefectsRepo extends JpaRepository<GenDefects, Long>{

    @Query("select d from GenDefects d where d.defectId=:defectId and d.refComDatabaseId=:refComDatabaseId")
    @Cacheable(value = "defectCount")
    GenDefects findBydefectIdAndrefComDatabaseId(String defectId,Long refComDatabaseId);
	
	@Query("select count(d.id) from GenDefects d where d.defectId=:defectId and d.refComDatabaseId=:refComDatabaseId")
	Long countdefectIdAndrefComDatabaseId(String defectId,Long refComDatabaseId);

}
