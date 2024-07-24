package com.example.demo.repo;

import com.example.demo.entity.YXComparisonResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface YXComparisonResultsRepo extends JpaRepository<YXComparisonResults,String> {
    @Query(value = "select yxc from YXComparisonResults yxc where yxc.st_name=:station and yxc.yx_comparisontime=:time")
    List<YXComparisonResults> findAllByYXRecords(@Param("station") String station, @Param("time") Date time);
}
