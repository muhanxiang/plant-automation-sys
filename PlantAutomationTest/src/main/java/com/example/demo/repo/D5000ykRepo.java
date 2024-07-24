package com.example.demo.repo;

import com.example.demo.entity.D5000yc;
import com.example.demo.entity.D5000yk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface D5000ykRepo extends JpaRepository<D5000yk, String> {
//    @Query(value = "select d5000yk from D5000yk d5000yk where d5000yk.st_name=:station and d5000yk.ttime=(select max(tt.ttime) from d5000yk tt where tt.st_name=:station)")
//    List<D5000yk> findAllByYKPointResult(@Param("station") String station);
    @Query(value = "select d5000yk from D5000yk d5000yk where d5000yk.st_name=:station")
    List<D5000yk> findAllByYKPointResult(@Param("station") String station);

    @Query(value = "select d5000yc from D5000yk d5000yc where d5000yc.st_name=:station and d5000yc.ttime=:time")
    List<D5000yk> findAllByStationAndTime(@Param("station") String station, @Param("time") Date time);
}
