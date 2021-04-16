package com.vibaps.merged.safetyreport.repo.gl;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.entity.gl.GlSelectedvaluesEntity;

@Repository
public interface GlSelectedvaluesEntityRepository extends JpaRepository<GlSelectedvaluesEntity, Long>{

	@Query(value = "SELECT a.* FROM gl_selectedvalues a ,gl_rulelist b,gen_user c WHERE a.gen_user_id=c.id AND b.db=c.db AND b.db=:db GROUP BY a.gen_rulelist_id", nativeQuery = true)
	List<GlSelectedvaluesEntity> getRuleList(@Param("db") String db);
	
	@Transactional
	@Modifying
	@Query(value = "insert into gl_selectedvalues(gen_user_id,gen_rulelist_id,status,weight) values (:userid,:rule,:status,:weight)", nativeQuery = true)
	void insertUserRuleList(@Param("userid") Long userid, @Param("rule") Long rule,@Param("status") int status, @Param("weight") int weight);


}
