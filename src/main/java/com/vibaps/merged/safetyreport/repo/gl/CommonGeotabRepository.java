package com.vibaps.merged.safetyreport.repo.gl;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vibaps.merged.safetyreport.entity.gl.LyUserEntity;
import com.vibaps.merged.safetyreport.entity.gl.UserReportFilterEntity;

@Repository
@Transactional
public interface CommonGeotabRepository extends JpaRepository<UserReportFilterEntity, Integer>{

@Query(value="insert into gen_device(ref_gen_user_id,device_id,device_name) values (:refid,:deviceid,:devicename)",nativeQuery = true)
void insertDevice(@Param("refid") int refid,@Param("deviceid") String deviceid, @Param("devicename") String devicename);

@Query(value="select id from gen_user where companyid=:userid and db=:db",nativeQuery = true)
int getCompanyId(@Param("userid") String userid,@Param("db") String db);

@Query(value = "insert into gen_driver(ref_gen_user_id,driver_id,driver_name) values (:refid,:driverid,:drivername)",nativeQuery = true)
void insertDriver(@Param("refid") int refid,@Param("driverid") String driverid, @Param("drivername") String drivername);

@Query(value = "SELECT ly_username,ly_password FROM ly_user where dbname=:db",nativeQuery = true)
LyUserEntity getLytxCredentials(@Param("db") String db);

}
