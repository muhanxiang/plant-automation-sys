package com.example.demo.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "xyd_yctable")
//@Table(name = "d5000yctable")

@Data
public class D5000yc {

    @Id
    String yc_name;

    int id;

    String st_name;

    String dot_no;

//    Date time;
    Date ttime;

    @Transient
    String tttime;

    @Transient
    int dot_int;

}
