package com.example.demo.repo;

import com.example.demo.entity.ydoyc;
import com.example.demo.entity.ydoyx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface YDoycTableRepo extends JpaRepository<ydoyc, String> {
    @Query(value = "select distinct ydoyc from ydoyc ydoyc where ydoyc.st_name=:station and ydoyc.ttime=:time")
//    List<ydoyc> findAllByYDOYCPointData(@Param("station") String station,@Param("time") Date time,Pageable pageable);
    List<ydoyc> findAllByYDOYCPointData(@Param("station") String station, @Param("time") Date time);

}
