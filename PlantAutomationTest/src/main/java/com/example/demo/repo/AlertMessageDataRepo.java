package com.example.demo.repo;


import com.example.demo.entity.AlertMessageData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AlertMessageDataRepo extends JpaRepository<AlertMessageData,String> {
    @Query(value = "select alert from AlertMessageData alert  order by alert.alert_time DESC")
    List<AlertMessageData> findAllByUpAlertMessage(Pageable pageable);


    @Query(value = "select alert from AlertMessageData alert")
    List<AlertMessageData> findAllByAllAlertMessage();

    @Query(value = "select alert from AlertMessageData alert where alert.station_name=:sta_name and alert.alert_name=:alert_name and alert.alert_time=:alert_time")
    AlertMessageData findByAlertMessage(String sta_name,String alert_name,Date alert_time);

    @Query(value = "select count(alert) from AlertMessageData alert where alert.handle_state=:state")
    int findAlertCount(String state);

    @Query(value = "select alert from AlertMessageData alert where alert.station_name=:station and alert.alert_time>=:time1 and alert.alert_time<=:time2 order by alert.alert_time DESC")
    List<AlertMessageData> findAllByUpAlertMessage(Pageable pageable,String station,@Param("time1") Date time1,@Param("time2") Date time2);

    @Query(value = "select alert from AlertMessageData alert where alert.station_name=:station and alert.alert_time>=:time1 and alert.alert_time<=:time2 order by alert.alert_time DESC")
    List<AlertMessageData> findAllByAllAlertMessage(String station,@Param("time1") Date time1,@Param("time2") Date time2);

    @Query(value = "select alert from AlertMessageData alert where alert.alert_time>=:time1 and alert.alert_time<=:time2")
    List<AlertMessageData> findAllByAllQuexian(@Param("time1") Date time1,@Param("time2") Date time2);

    @Query(value = "select alert.station_name from AlertMessageData alert")
    List<String> findAllBySAlertResult();
}
