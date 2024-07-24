package com.example.demo.repo;

import com.example.demo.entity.YKComparisonResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface YKComparisonResultsRepo extends JpaRepository<YKComparisonResults,String> {
    @Query(value = "select yxc from YKComparisonResults yxc where yxc.st_name=:station and yxc.yk_comparisontime=:time")
    List<YKComparisonResults> findAllByYKRecords(@Param("station") String station, @Param("time") Date time);
}
