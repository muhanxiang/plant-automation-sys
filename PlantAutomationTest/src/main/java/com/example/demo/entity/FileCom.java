package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "filecomparisonresults")

@Data
public class FileCom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String st_name;

    String ydo_filename;

    String ydt_filename;

    String ch_filename;

    Date ttime;

    String result;

    String reason;

    @Transient
    String timestring;
}
