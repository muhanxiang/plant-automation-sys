package com.example.demo.repo;

import com.example.demo.entity.ydoyk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface YDoykTableRepo extends JpaRepository<ydoyk, String> {
    @Query(value = "select distinct ydoyk from ydoyk ydoyk where ydoyk.st_name=:station and ydoyk.ttime=:time")
    List<ydoyk> findAllByYDOYKPointData(@Param("station") String station, @Param("time") Date time);
}
