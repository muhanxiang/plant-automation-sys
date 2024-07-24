package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ytcomparisonresults")

@Data
public class YTComparisonResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String st_name;

    String ydo_yt_name;

    String ydo_yt_dot_no;

    String ydt_yt_name;

    String ydt_yt_dot_no;

    String d5000_yt_name;

    String d5000_yt_dot_no;

    String yt_comparisonresult;

    Date yt_comparisontime;

    String reason;

    @Transient
    String timestring;
}
