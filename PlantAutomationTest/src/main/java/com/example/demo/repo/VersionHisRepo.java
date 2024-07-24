package com.example.demo.repo;

import com.example.demo.entity.VersionHis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionHisRepo extends JpaRepository<VersionHis, String> {
}
