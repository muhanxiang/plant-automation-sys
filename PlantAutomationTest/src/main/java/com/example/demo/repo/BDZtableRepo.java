package com.example.demo.repo;

import com.example.demo.entity.BDZtable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BDZtableRepo extends JpaRepository<BDZtable, String> {

    @Query(value = "select bdz.station_name from BDZtable bdz where bdz.area=:area")
    List<String> findAllByBDZData(@Param("area") String area);

}
