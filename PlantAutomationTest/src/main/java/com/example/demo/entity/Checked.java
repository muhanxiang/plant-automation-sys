package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Checked")

@Data
public class Checked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int no;

    String station_name;

    int checkCount;
}
