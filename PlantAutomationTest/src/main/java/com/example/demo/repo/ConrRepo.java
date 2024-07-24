package com.example.demo.repo;

import com.example.demo.entity.Conr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ConrRepo extends JpaRepository<Conr, String> {
    @Query(value = "select cr from Conr cr order by cr.ttime DESC")
    List<Conr> findCheckRecords();

    @Query(value = "select cr from Conr cr where cr.Comparison=:station order by cr.ttime DESC")
    List<Conr> findStationCheckRecords(@Param("station") String station);

    @Query(value = "select cr.ttime from Conr cr where cr.Comparison=:station")
    List<Date> findStationTimeRecords(@Param("station") String station);
}
