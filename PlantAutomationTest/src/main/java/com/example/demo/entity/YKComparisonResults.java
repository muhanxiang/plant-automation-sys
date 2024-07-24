package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ykcomparisonresults")

@Data
public class YKComparisonResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String st_name;

    String ydo_yk_name;

    String ydo_yk_dot_no;

    String ydt_yk_name;

    String ydt_yk_dot_no;

    String d5000_yk_name;

    String d5000_yk_dot_no;

    String yk_comparisonresult;

    Date yk_comparisontime;

    String reason;

    @Transient
    String timestring;
}
