package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "verificationmanagement")

@Data
public class VeMa {
    @Id
    int id;

    String station_name;

    String type;

    String result;

    String reason;

    Date time;

    @Transient
    String TimeZhuan;

}
