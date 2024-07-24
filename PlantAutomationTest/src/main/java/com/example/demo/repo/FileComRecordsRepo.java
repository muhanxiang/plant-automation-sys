package com.example.demo.repo;

import com.example.demo.entity.FileComRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileComRecordsRepo extends JpaRepository<FileComRecords, String> {
    @Query(value = "select cr from FileComRecords cr where cr.Comparison=:station order by cr.ttime DESC")
    List<FileComRecords> findFileCheckRecords(@Param("station") String station);
}
