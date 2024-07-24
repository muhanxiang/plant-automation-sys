package com.example.demo.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "yxcomparisonresults")

@Data
public class YXPointResult {

    @Id
    String st_name;

    String ydo_yx_name;

    String ydo_yx_dot_no;

    String ydt_yx_name;

    String ydt_yx_dot_no;

    String d5000_yx_name;

    String d5000_yx_dot_no;

    String yx_comparisonresult;

    Date yx_comparisontime;

}
