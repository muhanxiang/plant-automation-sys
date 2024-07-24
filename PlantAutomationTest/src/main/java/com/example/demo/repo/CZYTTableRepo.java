package com.example.demo.repo;


import com.example.demo.entity.CZYTTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CZYTTableRepo extends JpaRepository<CZYTTable, String> {
    @Query(value = "select distinct ydoyx from CZYTTable ydoyx where ydoyx.st_name=:station and ydoyx.ttime=:time")
    List<CZYTTable> findAllByCZYTPointData(@Param("station") String station, @Param("time") Date time);

    @Query(value = "select max(ydoyx.ttime)  from CZYTTable ydoyx where ydoyx.st_name=:station")
    Date findBymaxTime(@Param("station") String station);
}
