package com.example.demo.repo;

import com.example.demo.entity.CZYXTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CZYXTableRepo extends JpaRepository<CZYXTable, String> {
    @Query(value = "select DISTINCT d.st_name, b.area FROM CZYXTable d, BDZAllTable b WHERE d.st_name = b.station_name")
    List<String> findbdzAllList();
    @Query(value = "select DISTINCT d.st_name, b.area FROM CZYXTable d, FDCAllTable b WHERE d.st_name = b.station_name")
    List<String> finddfcAllList();
    @Query(value = "select DISTINCT ydoyx.ttime from CZYXTable ydoyx where ydoyx.st_name=:station")
    List<Date> findByTimeList(@Param("station") String station);

    @Query(value = "select distinct ydoyx from CZYXTable ydoyx where ydoyx.st_name=:station and ydoyx.ttime=:time")
    List<CZYXTable> findAllByCZYXPointData(@Param("station") String station, @Param("time") Date time);

    @Query(value = "select max(ydoyx.ttime)  from CZYXTable ydoyx where ydoyx.st_name=:station")
    Date findBymaxTime(@Param("station") String station);
}
