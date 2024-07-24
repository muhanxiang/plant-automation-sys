package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "czpoint_yctable")

@Data
public class CZYCTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String ycname;

    String st_name;

    String dot_no;

    Date ttime;

    int state;

    @Transient
    String tttime;

    @Transient
    int dot_int;
}
