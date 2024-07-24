package com.example.demo.repo;

import com.example.demo.entity.YKComparisonResults;
import com.example.demo.entity.YTComparisonResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface YTComparisonResultsRepo extends JpaRepository<YTComparisonResults,String> {
    @Query(value = "select yxc from YTComparisonResults yxc where yxc.st_name=:station and yxc.yt_comparisontime=:time")
    List<YTComparisonResults> findAllByYTRecords(@Param("station") String station, @Param("time") Date time);
}
