package com.example.demo.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ydoyxtable")

@Data
public class ydoyx {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String yxname;

    String st_name;

    String dot_no;

    Date ttime;

    String version;

    @Transient
    String tttime;

    @Transient
    int dot_int;
}
