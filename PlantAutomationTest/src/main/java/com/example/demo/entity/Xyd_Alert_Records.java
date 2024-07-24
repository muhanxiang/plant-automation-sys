package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "xyd_alert_records")

@Data
public class Xyd_Alert_Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String station_name;

    Date checktime;

    String different_description;

    String type;

    String reason;

    @Transient
    String time;
}
