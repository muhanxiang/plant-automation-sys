package com.example.demo.repo;

import com.example.demo.entity.VeMa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VeMaRepo extends JpaRepository<VeMa,String> {
    @Query(value = "select ve from VeMa ve where ve.station_name=:station and ve.time=:time")
    List<VeMa> findAllByVeMaResult(@Param("station") String station, @Param("time") Date time);

    @Query(value = "select ve.time from VeMa ve where ve.station_name=:station and ve.time>=:time1 and ve.time<=:time2")
    List<Date> findGetTimeList(@Param("station") String station, @Param("time1") Date time1,@Param("time2") Date time2);

    @Query(value = "select ve.time from VeMa ve where ve.time>=:time1 and ve.time<=:time2")
    List<Date> findAllTimeList(@Param("time1") Date time1,@Param("time2") Date time2);
}
