package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "xyd_ac_records")

@Data
public class Xyd_Ac_Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String state;

    Date ac_time;

    @Transient
    String AAc_time;
}
