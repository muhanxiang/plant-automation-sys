package com.example.demo.repo;


import com.example.demo.entity.HomeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeDataRepo extends JpaRepository<HomeData,String> {

}
