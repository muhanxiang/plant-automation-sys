package com.example.demo.repo;

import com.example.demo.entity.CZYCTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CZYCTableRepo extends JpaRepository<CZYCTable, String> {
    @Query(value = "select distinct ydoyx from CZYCTable ydoyx where ydoyx.st_name=:station and ydoyx.ttime=:time")
    List<CZYCTable> findAllByCZYCPointData(@Param("station") String station, @Param("time") Date time);

    @Query(value = "select max(ydoyx.ttime)  from CZYCTable ydoyx where ydoyx.st_name=:station")
    Date findBymaxTime(@Param("station") String station);
}
