package com.vibaps.merged.safetyreport.repo.gl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.UserReportFilterEntity;

@Repository
public interface UserReportFilterRepository extends JpaRepository<UserReportFilterEntity, Integer> {

	@Modifying
	@Query(value = "update gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d set b.status=0,d.minmiles=:minMiles  "
			+ "where  c.companyid=:companyId and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and d.gen_user_id=c.id",
			nativeQuery = true)
	void updateStatus(@Param("companyId") String companyId, @Param("minMiles") String minMiles);
	
	@Modifying
	@Query(value = "update gl_rulelist a,gl_selectedvalues b,gen_user c set b.status=1, b.weight=:weight "
			+ "where  c.companyid=:companyId and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.rulevalue=:exception",
			nativeQuery = true)
	void updateRule(@Param("companyId") String companyId, @Param("weight") Integer weight, @Param("exception") String exception);
}
