package com.example.demo.repo;

import com.example.demo.entity.D5000yk;
import com.example.demo.entity.D5000yt;
import com.example.demo.entity.D5000yx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface D5000ytRepo extends JpaRepository<D5000yt, String> {
    @Query(value = "select d5000yk from D5000yt d5000yk where d5000yk.st_name=:station")
    List<D5000yt> findAllByYTPointResult(@Param("station") String station);
}
