package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "xyd_yktable")
//@Table(name = "d5000yktable")

@Data
public class D5000yk {
    @Id
    String yk_name;

    String st_name;

    String dot_no;

    Date ttime;

    @Transient
    String tttime;

    @Transient
    int dot_int;
}
