package com.example.demo.repo;


import com.example.demo.entity.D5000yc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface D5000ycRepo extends JpaRepository<D5000yc, String> {
//    @Query(value = "select d5000yc from D5000yc d5000yc where d5000yc.st_name=:station and d5000yc.ttime=(select max(tt.ttime) from d5000yc tt where tt.st_name=:station)")
//    List<D5000yc> findAllByYCPointResult(@Param("station") String station);

    @Query(value = "select d5000yc from D5000yc d5000yc where d5000yc.st_name=:station")
    List<D5000yc> findAllByYCPointResult(@Param("station") String station);

    @Query(value = "select d5000yc from D5000yc d5000yc where d5000yc.ttime=(select max(tt.ttime) from D5000yc tt)")
    List<D5000yc> findAllByNoStation();

    @Query(value = "select d5000yc from D5000yc d5000yc where d5000yc.st_name=:station and d5000yc.ttime=:time")
    List<D5000yc> findAllByStationAndTime(@Param("station") String station, @Param("time") Date time);
}
