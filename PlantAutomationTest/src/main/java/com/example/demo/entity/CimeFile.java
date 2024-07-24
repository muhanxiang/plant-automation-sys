package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="CIME_File")
@Data
public class CimeFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String fileName;
    String createTime;
    String path;

}
