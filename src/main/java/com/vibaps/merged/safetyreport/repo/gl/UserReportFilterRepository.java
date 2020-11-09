package com.vibaps.merged.safetyreport.repo.gl;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.objenesis.instantiator.annotations.Instantiator;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.UserReportFilterEntity;

@Repository
@Transactional
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

	@Query(value = "SELECT count(*) FROM gen_user where companyid=:companyid and db=:db",nativeQuery = true)
    Integer userCount(@Param("companyid") String companyid,@Param("db") String db);
	
	@Query(value = "call insertuserrecord(:userid,:db)",nativeQuery = true)
	BigInteger userCreation(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value="SELECT gen_rulelist_id,status,weight FROM gl_selectedvalues where gen_user_id=1 order by gen_rulelist_id",nativeQuery = true)
	List<GlRulelistEntity> getRuleList();
	
	@Query(value="insert into gl_selectedvalues(gen_user_id,gen_rulelist_id,status,weight) values (:userid,:rule,:status,:weight)",nativeQuery = true)
	void insertUserRuleList(@Param("userid") BigInteger userid,@Param("rule") Integer rule,@Param("status") Integer status,@Param("weight") Integer weight);
}
