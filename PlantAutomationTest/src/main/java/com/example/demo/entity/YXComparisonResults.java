package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "yxcomparisonresults")

@Data
public class YXComparisonResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String st_name;

    String ydo_yx_name;

    String ydo_yx_dot_no;

    String ydt_yx_name;

    String ydt_yx_dot_no;

    String d5000_yx_name;

    String d5000_yx_dot_no;

    String yx_comparisonresult;

    Date yx_comparisontime;

    String reason;

    @Transient
    String timestring;
}

