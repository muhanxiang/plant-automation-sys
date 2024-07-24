package com.example.demo.repo;

import com.example.demo.entity.FDCtable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FDCtableRepo extends JpaRepository<FDCtable, String> {

    @Query(value = "select fdc.station_name from FDCtable fdc where fdc.area=:area")
    List<String> findAllByFDCData(@Param("area") String area);

}
