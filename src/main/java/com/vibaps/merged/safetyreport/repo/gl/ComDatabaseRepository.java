package com.vibaps.merged.safetyreport.repo.gl;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;

@Repository
public interface ComDatabaseRepository  extends JpaRepository<ComDatabase,Long>
{	
    @Cacheable(value = "comdatabase", unless = "#cd0=='comdatabase'")
	ComDatabase findBydatabaseName(String database);
	
    @Cacheable(value = "comdatabasecount", unless = "#cd1=='comdatabase'")
	@Query("select count(p.id) from ComDatabase p where databaseName=:database")
	Long countdatabaseName(String database);
}
