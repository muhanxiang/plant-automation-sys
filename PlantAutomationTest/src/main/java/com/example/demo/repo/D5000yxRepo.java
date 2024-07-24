package com.example.demo.repo;

import com.example.demo.entity.BDZAllTable;
import com.example.demo.entity.D5000yx;
import com.example.demo.entity.FDCAllTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface D5000yxRepo extends JpaRepository<D5000yx, String> {
//    @Query(value = "select d5000yx from D5000yx d5000yx where d5000yx.st_name=:station and d5000yx.ttime=(select max(tt.ttime) from D5000yx tt where tt.st_name=:station)")
//    List<D5000yx> findAllByYXPointResult(@Param("station") String station);
    @Query(value = "select d5000yx from D5000yx d5000yx where d5000yx.st_name=:station")
    List<D5000yx> findAllByYXPointResult(@Param("station") String station);

    @Query(value = "select d5000yx from D5000yx d5000yx where d5000yx.ttime=(select max(tt.ttime) from D5000yx tt)")
    List<D5000yx> findAllByNoStation();

    @Query(value = "select DISTINCT d5000yx from D5000yx d5000yx where d5000yx.st_name=:station and d5000yx.ttime=:newtime")
    List<D5000yx> findAllByStation(@Param("station") String station,@Param("newtime") Date newtime);

    @Query(value = "select max(d5000yx.ttime)  from D5000yx d5000yx where d5000yx.st_name=:station")
    Date findBymaxTime(@Param("station") String station);

    @Query(value = "select DISTINCT d5000yx.ttime from D5000yx d5000yx where d5000yx.st_name=:station")
    List<Date> findByTimeList(@Param("station") String station);

    @Query(value = "select d5000yx from D5000yx d5000yx where d5000yx.st_name=:station and d5000yx.ttime=:time")
    List<D5000yx> findAllByStationAndTime(@Param("station") String station,@Param("time") Date time);

    @Query(value = "select DISTINCT d5000yx.st_name from D5000yx d5000yx")
    List<String> findStatonAllList();

    @Query(value = "select DISTINCT d.st_name, b.area ,b.type, b.level FROM D5000yx d, BDZAllTable b WHERE d.st_name = b.station_name")
    List<String> findbdzAllList();
    @Query(value = "select DISTINCT d.st_name, b.area ,b.type, b.level FROM D5000yx d, FDCAllTable b WHERE d.st_name = b.station_name")
    List<String> finddfcAllList();

    @Query(value = "select DISTINCT b FROM D5000yx d, BDZAllTable b WHERE d.st_name = b.station_name")
    List<BDZAllTable> findbdzAllList_New();
    @Query(value = "select DISTINCT b FROM D5000yx d, FDCAllTable b WHERE d.st_name = b.station_name")
    List<FDCAllTable> finddfcAllList_New();
}
