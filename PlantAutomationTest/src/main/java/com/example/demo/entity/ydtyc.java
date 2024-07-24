package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ydtyctable")

@Data
public class ydtyc {
    @Id
    String ycname;

    String st_name;

    String dot_no;

    Date ttime;

    String version;
}
