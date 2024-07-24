package com.example.demo.repo;

import com.example.demo.entity.FileCom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileComRepo extends JpaRepository<FileCom, String> {
    @Query(value = "select filecom from FileCom filecom where filecom.st_name=:station and filecom.ttime=(select max(filecom.ttime) from FileCom filecom where filecom.st_name=:station)")
    List<FileCom> findFileCheckResult(@Param("station") String station);
}
