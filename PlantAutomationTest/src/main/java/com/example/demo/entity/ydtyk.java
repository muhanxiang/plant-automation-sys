package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ydtyktable")

@Data
public class ydtyk {
    @Id
    String ykname;

    String st_name;

    String dot_no;

    Date ttime;

    String version;
}
