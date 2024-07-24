package com.example.demo.repo;

import com.example.demo.entity.YXPointResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface YXPointResultRepo extends JpaRepository<YXPointResult, String> {
    @Query(value = "select dsaScsPlate from YXPointResult dsaScsPlate where dsaScsPlate.st_name=:station and dsaScsPlate.yx_comparisontime=:time")
    List<YXPointResult> findAllByYXPointResult(@Param("station") String station, @Param("time")Date time);
}
