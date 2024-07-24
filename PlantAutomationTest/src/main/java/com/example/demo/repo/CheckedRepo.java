package com.example.demo.repo;

import com.example.demo.entity.Checked;
import com.example.demo.entity.D5000yc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckedRepo extends JpaRepository<Checked, String> {
    @Query(value = "select che.station_name from Checked che")
    List<String> findAllByStationResult();
}
