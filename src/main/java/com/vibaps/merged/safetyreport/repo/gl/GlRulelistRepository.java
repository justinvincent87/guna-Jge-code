package com.vibaps.merged.safetyreport.repo.gl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntityI;

public interface GlRulelistRepository extends JpaRepository<GlRulelistEntityI, Long>{

	@Query(value="SELECT * FROM gl_rulelist where rulecompany='L' GROUP BY rulevalue",nativeQuery = true)
	List<GlRulelistEntityI> getLytxRuleForInsert();
	
	@Query(value="SELECT * FROM gl_rulelist where db=:db",nativeQuery = true)
	List<GlRulelistEntityI> getAllRuleForInsert(@Param("db") String db);
	
}
