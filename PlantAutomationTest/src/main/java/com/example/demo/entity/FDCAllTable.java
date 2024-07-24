package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fdctable_all")

@Data
public class FDCAllTable {
    @Id
    int no;

    String station_id;

    String station_name;

    String area;

    int checkstate;

    String type;

    String level;
}
