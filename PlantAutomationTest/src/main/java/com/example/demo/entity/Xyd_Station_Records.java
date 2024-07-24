package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "xyd_station_records")

@Data
public class Xyd_Station_Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String station_name;

    String state;

    Date ac_time;

    @Transient
    String St_time;
}
