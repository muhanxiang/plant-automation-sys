package com.example.demo.entity;

import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "treetable")

@Data
public class treetable {
    @Id
    String transformersubstation;

    String city;

    int versionid;

    @Transient
    List<Date> timelist;

}
