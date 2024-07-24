package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "czpoint_yxtable")

@Data
public class CZYXTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String yxname;

    String st_name;

    String dot_no;

    Date ttime;

    int state;

    @Transient
    String tttime;

    @Transient
    int dot_int;
}
