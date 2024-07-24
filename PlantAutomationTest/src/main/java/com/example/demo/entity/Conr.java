package com.example.demo.entity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comparisonrecords")

@Data
public class Conr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String Comparison;

    Date ttime;

    String checkstate;

    @Transient
    String stringtime;
}
