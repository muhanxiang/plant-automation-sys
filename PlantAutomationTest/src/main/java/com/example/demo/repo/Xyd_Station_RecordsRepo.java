package com.example.demo.repo;

import com.example.demo.entity.Xyd_Station_Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface Xyd_Station_RecordsRepo extends JpaRepository<Xyd_Station_Records, String> {
    @Query(value = "select stA from Xyd_Station_Records stA where stA.ac_time=:time")
    List<Xyd_Station_Records> findByAcStationRecords(@Param("time") Date time);
}
