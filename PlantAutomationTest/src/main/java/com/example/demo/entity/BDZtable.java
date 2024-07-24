package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bdztable")

@Data
public class BDZtable {
    @Id
    int no;

    String station_id;

    String station_name;

    String area;

    int checkstate;

    @Transient
    List<Date> timelist;
}
