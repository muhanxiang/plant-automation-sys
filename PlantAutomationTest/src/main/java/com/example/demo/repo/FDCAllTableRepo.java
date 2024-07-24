package com.example.demo.repo;

import com.example.demo.entity.FDCAllTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FDCAllTableRepo extends JpaRepository<FDCAllTable, String> {
    @Query(value = "select fdc.area from FDCAllTable fdc where fdc.station_name=:station")
    List<String> findAllByFDCData(@Param("station") String station);

    @Query(value = "select fdc.station_name from FDCAllTable fdc where fdc.area=:area")
    List<String> findAllByFDCBuFenData(@Param("area") String area);
}
