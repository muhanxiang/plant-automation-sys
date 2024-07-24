package com.example.demo.repo;

import com.example.demo.entity.YCComparisonResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface YCComparisonResultsRepo extends JpaRepository<YCComparisonResults,String> {
    @Query(value = "select yxc from YCComparisonResults yxc where yxc.st_name=:station and yxc.yc_comparisontime=:time")
    List<YCComparisonResults> findAllByYCRecords(@Param("station") String station, @Param("time") Date time);
}
