package com.vibaps.merged.safetyreport.repo.gl;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;

@Repository
public interface ComDatabaseRepository  extends JpaRepository<ComDatabase,Long>
{
	ComDatabase findBydatabaseName(String database);
	
	@Query("select count(p.id) from ComDatabase p where databaseName=:database")
	Long countdatabaseName(String database);
}
