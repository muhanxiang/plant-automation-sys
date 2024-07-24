package com.example.demo.repo;

import com.example.demo.entity.Verificationmanagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VermRepo extends JpaRepository<Verificationmanagement, String> {
}
