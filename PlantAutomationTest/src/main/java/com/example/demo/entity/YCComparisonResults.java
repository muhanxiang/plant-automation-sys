package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "yccomparisonresults")

@Data
public class YCComparisonResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String st_name;

    String ydo_yc_name;

    String ydo_yc_dot_no;

    String ydt_yc_name;

    String ydt_yc_dot_no;

    String d5000_yc_name;

    String d5000_yc_dot_no;

    String yc_comparisonresult;

    Date yc_comparisontime;

    String reason;

    @Transient
    String timestring;
}
