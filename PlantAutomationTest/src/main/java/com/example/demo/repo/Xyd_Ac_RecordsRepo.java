package com.example.demo.repo;

import com.example.demo.entity.Xyd_Ac_Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface Xyd_Ac_RecordsRepo extends JpaRepository<Xyd_Ac_Records, String> {
}
