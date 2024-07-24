package com.example.demo.repo;

import com.example.demo.entity.treetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreetableRepo extends JpaRepository<treetable, String> {
    @Query(value = "select tr.transformersubstation from treetable tr where tr.city=:city")
    List<String> findSelectResult(@Param("city") String city);

    @Query(value = "select tr.city from treetable tr where tr.transformersubstation=:station")
    List<String> findCityResult(@Param("station") String station);

    @Query(value = "select tr from treetable tr where tr.versionid=:versionid")
    List<treetable> findSationResult(@Param("versionid") int versionid);

}
