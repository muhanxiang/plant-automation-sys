package com.example.demo.repo;

import com.example.demo.entity.BDZAllTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BDZAllTableRepo extends JpaRepository<BDZAllTable, String> {
    @Query(value = "select bdz.area from BDZAllTable bdz where bdz.station_name=:station")
    List<String> findAllByBDZData(@Param("station") String station);

    @Query(value = "select bdz.station_name from BDZAllTable bdz where bdz.area=:area")
    List<String> findAllByBuFenData(@Param("area") String area);
}
