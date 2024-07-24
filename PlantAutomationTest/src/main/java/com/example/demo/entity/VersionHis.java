package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "versionhistory")

@Data
public class VersionHis {
    @Id
    int id;

    String version;

    String station;

    Date ttime;

    String state;

    @Transient
    String tttime;
}
