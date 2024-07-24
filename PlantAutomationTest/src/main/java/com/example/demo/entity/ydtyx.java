package com.example.demo.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ydtyxtable")

@Data
public class ydtyx {
    @Id
    String yxname;

    String st_name;

    String dot_no;

    Date ttime;

    String version;
}
