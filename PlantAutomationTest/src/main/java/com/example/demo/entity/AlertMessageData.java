package com.example.demo.entity;


import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "alertmessagetable")

@Data
public class AlertMessageData {
    @Id
    int id;

    String station_name;

    String alert_name;

    Date alert_time;

    String handle_state;

    String handle_recommendation;

    String handle_an;

    @Transient
    String alert_time_string;
    @Transient
    String handle_logo;
    @Transient
    int AllCount;
}
