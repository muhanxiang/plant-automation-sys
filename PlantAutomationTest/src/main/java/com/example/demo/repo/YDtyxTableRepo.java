package com.example.demo.repo;

import com.example.demo.entity.ydtyx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YDtyxTableRepo extends JpaRepository<ydtyx, String> {
    @Query(value = "select  ydtyx from ydtyx ydtyx where ydtyx.st_name=:station")
    List<ydtyx> findAllByYDTYXData(@Param("station") String station);
}
