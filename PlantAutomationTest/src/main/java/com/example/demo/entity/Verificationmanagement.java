package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verificationmanagement")

@Data
public class Verificationmanagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String station_name;

    String type;

    String result;

    String reason;

    Date time;
}
