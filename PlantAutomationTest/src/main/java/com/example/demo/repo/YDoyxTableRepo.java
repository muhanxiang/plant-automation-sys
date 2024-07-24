package com.example.demo.repo;

import com.example.demo.entity.ydoyx;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface YDoyxTableRepo extends JpaRepository<ydoyx, String> {
    @Query(value = "select distinct ydoyx.ttime from ydoyx ydoyx where ydoyx.st_name=:station order by ydoyx.ttime DESC")
    List<Date> findAllByYDOYXPointResult(@Param("station") String station,Pageable pageable);

    @Query(value = "select distinct ydoyx from ydoyx ydoyx where ydoyx.st_name=:station and ydoyx.ttime=:time")
//    List<ydoyx> findAllByYDOYXPointData(@Param("station") String station,@Param("time") Date time,Pageable pageable);
    List<ydoyx> findAllByYDOYXPointData(@Param("station") String station,@Param("time") Date time);

    @Query(value = "select  ydoyx from ydoyx ydoyx where ydoyx.st_name=:station")
    List<ydoyx> findAllByYDOYXData(@Param("station") String station);

    @Query(value = "select max(ydoyx.ttime)  from ydoyx ydoyx where ydoyx.st_name=:station")
    Date findBymaxTime(@Param("station") String station);

    @Query(value = "select DISTINCT ydoyx.ttime from ydoyx ydoyx where ydoyx.st_name=:station")
    List<Date> findByTimeList(@Param("station") String station);
}
