package com.vibaps.merged.safetyreport.repo.gl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vibaps.merged.safetyreport.entity.gl.GenTrailer;

public interface GenTrailerRepository extends JpaRepository<GenTrailer,Long>{
	
	@Query("select d from GenTrailer d where d.trailerId=:trailerId and d.refComDatabaseId=:refComDatabaseId")
	@Cacheable(value = "trailerCount", unless = "#gt0=='trailerCount'")
	GenTrailer findBytrailerIdAndrefComDatabaseId(String trailerId,Long refComDatabaseId);
	
	@Query("select count(d.id) from GenTrailer d where d.trailerId=:trailerId and d.refComDatabaseId=:refComDatabaseId")
	@Cacheable(value = "trailerCountDB", unless = "#gt1=='trailerCountDB'")
	Long counttrailerIdAndrefComDatabaseId(String trailerId,Long refComDatabaseId);

}
