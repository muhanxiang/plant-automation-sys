package com.example.demo.repo;

import com.example.demo.entity.Xyd_Alert_Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface Xyd_Alert_RecordsRepo extends JpaRepository<Xyd_Alert_Records, String> {
}
