package com.vibaps.merged.safetyreport.repo.gl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;

@Repository
public interface GlRulelistEntityRepository extends JpaRepository<GlRulelistEntity, Long>{

	@Query(value = "select * from (select a.id,CONCAT(a.rulecompany,'-',a.rulename) as rulename,rulevalue,a.rulecompany,b.status,b.weight,d.minmiles from gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and d.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.db=:db order by rulename) as value  order by value.status desc", nativeQuery = true)
	List<GlRulelistEntity> viewadd(@Param("userid") String userid, @Param("db") String db);

	
	@Query(value = "select * from (select a.id,CONCAT(a.rulecompany,'-',a.rulename) as rulename,rulevalue,a.rulecompany,b.status,b.weight,d.minmiles from gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and d.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.db=:db order by rulename) as value order by value.rulename", nativeQuery = true)
	List<GlRulelistEntity> viewBehaveList(@Param("userid") String userid, @Param("db") String db);

	//@Query(value = "select CONCAT(a.rulecompany,'-',a.rulename) as rulename,b.weight from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and a.db=:db order by rulename", nativeQuery = true)
	@Query(value = "select * from (select a.id,CONCAT(a.rulecompany,'-',a.rulename) as rulename,rulevalue,a.rulecompany,b.status,b.weight,d.minmiles from gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d where c.companyid=:userid and c.db=:db and b.status=1 and  b.gen_user_id=c.id and d.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.db=:db order by rulename) as value order by value.rulename", nativeQuery = true)
	List<GlRulelistEntity> getallbehaveui(@Param("userid") String userid, @Param("db") String db);

	@Query(value = "SELECT a.gen_rulelist_id,a.status,a.weight FROM gl_selectedvalues a ,gl_rulelist b,gen_user c WHERE a.gen_user_id=c.id AND b.db=c.db AND b.db=:db GROUP BY a.gen_rulelist_id", nativeQuery = true)
	List<GlRulelistEntity> getRuleList(@Param("db") String db);
	
	@Query(value = "insert into gl_selectedvalues(gen_user_id,gen_rulelist_id,status,weight) values (:userid,:rule,:status,:weight)", nativeQuery = true)
	void insertUserRuleList(@Param("userid") BigInteger userid, @Param("rule") Long rule,
	        @Param("status") Integer status, @Param("weight") int weight);

}
