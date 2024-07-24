package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "hometable")

@Data
public class HomeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int yxgktotal;

    int yxgkonline;

    int yxgkoffline;

    int checkcount;

    int yctjtotal;

    int yctjdefect;

    @Transient
    String onlinerate;
}
