package com.example.demo.repo;

import com.example.demo.entity.D5000yc;
import com.example.demo.entity.VersionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionDataRepo extends JpaRepository<VersionData, String> {
    @Query(value = "select vd from VersionData vd where vd.id=:versionid")
    List<VersionData> findByVersionResult(@Param("versionid") int versionid);
}
