package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fdctable")

@Data
public class FDCtable {
    @Id
    int no;

    String station_id;

    String station_name;

    String area;

    int checkstate;
}
