package com.example.demo.repo;

import com.example.demo.entity.CZYKTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CZYKTableRepo extends JpaRepository<CZYKTable, String> {
    @Query(value = "select distinct ydoyx from CZYKTable ydoyx where ydoyx.st_name=:station and ydoyx.ttime=:time")
    List<CZYKTable> findAllByCZYKPointData(@Param("station") String station, @Param("time") Date time);

    @Query(value = "select max(ydoyx.ttime)  from CZYKTable ydoyx where ydoyx.st_name=:station")
    Date findBymaxTime(@Param("station") String station);
}
