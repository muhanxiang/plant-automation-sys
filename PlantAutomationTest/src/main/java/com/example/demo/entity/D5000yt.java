package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "xyd_yttable")

@Data

public class D5000yt {
    @Id
    String yt_name;

    String st_name;

    String dot_no;

    Date ttime;

    @Transient
    String tttime;

    @Transient
    int dot_int;
}
