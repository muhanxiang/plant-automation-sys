package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ydoyktable")

@Data
public class ydoyk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String ykname;

    String st_name;

    String dot_no;

    Date ttime;

    String version;

    @Transient
    String tttime;

    @Transient
    int dot_int;
}
