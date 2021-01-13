package com.vibaps.merged.safetyreport.repo.gl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.objenesis.instantiator.annotations.Instantiator;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.GenDevice;
import com.vibaps.merged.safetyreport.entity.gl.GenDriver;
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
	
	@Query(value="SELECT a.gen_rulelist_id,a.status,a.weight FROM gl_selectedvalues a ,gl_rulelist b,gen_user c WHERE a.gen_user_id=c.id AND b.db=c.db AND b.db=:db GROUP BY a.gen_rulelist_id",nativeQuery = true)
	List<GlRulelistEntity> getRuleList(@Param("db") String db);
	
	@Query(value="insert into gl_selectedvalues(gen_user_id,gen_rulelist_id,status,weight) values (:userid,:rule,:status,:weight)",nativeQuery = true)
	void insertUserRuleList(@Param("userid") BigInteger userid,@Param("rule") Integer rule,@Param("status") Integer status,@Param("weight") Integer weight);
	
	@Query(value="select * from (select a.id,CONCAT(a.rulecompany,'-',a.rulename) as value,rulevalue,a.rulecompany,b.status,b.weight,d.minmiles from gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and d.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.db=:db order by value) as value order by value.status desc",nativeQuery = true)
	List<GlRulelistEntity> viewadd(@Param("userid") String userid,@Param("db") String db);

	@Query(value = "select a.rulevalue from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and a.rulecompany='G' and a.db=:db",nativeQuery = true)
	List<GlRulelistEntity> getgeodropdown(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value="select a.rulevalue from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.db=:db and a.id=b.gen_rulelist_id and a.rulecompany='L'",nativeQuery = true)
	List<GlRulelistEntity> getLybehave(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value="select CONCAT(a.rulecompany,'-',a.rulename) as value from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and a.db=:db order by value",nativeQuery = true)
	List<String> getallbehave(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value = "select CONCAT(a.rulecompany,'-',a.rulename) as value,b.weight from gl_rulelist a,gl_selectedvalues b,gen_user c where c.companyid=:userid and c.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and a.db=:db order by value",nativeQuery = true)
	List getallbehaveui(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value = "select concat(b.rulecompany,'-',b.rulename) as val,a.weight from gl_selectedvalues a,gl_rulelist b,gen_user c where c.companyid=:userid and c.db=:db and b.db=:db and a.status= 1 and a.gen_user_id=c.id and a.gen_rulelist_id=b.id order by val",nativeQuery = true)
	List<Object[]> getallBehaveFromDB(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value = "select count(b.id) from gl_selectedvalues a,gl_rulelist b,gen_user c where c.companyid=:userid and c.db=:db and b.db=:db and a.status= 1 and a.gen_user_id=c.id and b.rulecompany='G' and a.gen_rulelist_id=b.id",nativeQuery = true)
	int geoCount(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value = "select count(b.id),a.weight from gl_selectedvalues a,gl_rulelist b,gen_user c where c.companyid=:userid and c.db=:db and a.status= 1 and a.gen_user_id=c.id and b.rulecompany='L' and a.gen_rulelist_id=b.id",nativeQuery = true)
	int lyCount(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value = "select a.weight from gl_selectedvalues a,gen_user b,gl_rulelist c where a.gen_user_id=b.id and b.companyid=:userid and b.db=:db and a.gen_rulelist_id=c.id and concat(c.rulecompany,'-',c.rulename)=:rule",nativeQuery = true)
	int getWeight(@Param("userid") String userid,@Param("db") String db,@Param("rule") String rule);
	
	@Query(value = "select a.minmiles from gl_minmiles a,gen_user b where b.companyid=:userid and b.db=:db and a.gen_user_id=b.id",nativeQuery = true)
	float getminmiles(@Param("userid") String userid,@Param("db") String db);
	
	@Modifying
	@Query(value = "update gl_responce a,gen_user b set a.responce_json=:responce where b.companyid=:userid and b.db=:db and b.id=a.gen_user_id",nativeQuery = true)
	void updateresponce(@Param("userid") String userid,@Param("db") String db,@Param("responce") String responseJson);
	
	@Query(value = "select a.responce_json from gl_responce a,gen_user b where b.companyid=:userid and b.db=:db and b.id=a.gen_user_id",nativeQuery = true)
	String selectresponce(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value = "update gl_rulelist a,gl_selectedvalues b,gen_user c,gl_minmiles d set b.status=0,d.minmiles=:minmiles  where  c.companyid=:companyid and c.db=:db  and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and b.status=1 and d.gen_user_id=c.id",nativeQuery = true)
	int getRuleListInsert(@Param("companyid") String companyid,@Param("db") String db,@Param("minmiles") Float minmiles);
	
	@Query(value = "update gl_rulelist a,gl_selectedvalues b,gen_user c set b.status=1,b.weight=:we where  c.companyid=:companyid and c.db=:db and a.db=:db and b.gen_user_id=c.id and a.id=b.gen_rulelist_id and a.id=:rval",nativeQuery = true)
	void updateRuleListValue(@Param("companyid") String companyid,@Param("db") String db,@Param("we") Integer we,@Param("rval") String rval);
	
	@Query(value = "SELECT a.device_id,a.device_name FROM gen_device a,gen_user b where a.ref_gen_user_id=b.id and b.companyid=:userid and b.db=:db",nativeQuery = true)
	List<GenDevice> deviceInfo(@Param("userid") String userid,@Param("db") String db);
	
	@Query(value="SELECT a.driver_id,a.driver_name FROM gen_driver a,gen_user b where a.ref_gen_user_id=b.id and b.companyid=:userid and b.db=:db",nativeQuery = true)
	List<GenDriver> getDriverInfo(@Param("userid") String userid,@Param("db") String db);

}
