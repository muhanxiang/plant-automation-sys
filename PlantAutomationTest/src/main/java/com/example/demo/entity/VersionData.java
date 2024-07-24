package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "versiontree")

@Data
public class VersionData {
    @Id
    int id;

    String manufacturer;

    String version;
}
