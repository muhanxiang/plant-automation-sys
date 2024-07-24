package com.example.demo.controller;

import com.example.demo.entity.JsonResult;
import com.example.demo.service.ResultPointService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("PointResult")
@CrossOrigin
public class PointResultController {

    @Autowired
    ResultPointService resultPointService;

//    @GetMapping("YX")
//    public JsonResult getYXPointResult(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time)throws ParseException{
////      String staion="阳城厂";
//        Date date = new Date();
//        if (time != null) {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            date = simpleDateFormat.parse(time);
//        } else {
//            date=null;
//        }
//
//        return resultPointService.YXPointResult(station,date);
//    }

    @Getter
    static class His{
        String station;
        String time;
    }

    @GetMapping("allResult")
    public JsonResult getHisTime(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time)throws ParseException {
        Date date = new Date();
        if (time.equals("null")) {
            date=null;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(time);
        }
        return resultPointService.VeMaResult(station,date);
    }

    @GetMapping("yxRecords")
    public JsonResult getXxiRecords(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time)throws ParseException {
        Date date = new Date();
        if (time.equals("null")) {
            date=null;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(time);
        }
        return resultPointService.YxRecords(station,date);
    }

    @Getter
    static class ggtime{
        String station;
        String time1;
        String time2;
    }
    @GetMapping("TimeResult")
    public JsonResult getTime(@RequestParam(defaultValue = "null") String city,@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time1,@RequestParam(defaultValue = "null") String time2)throws ParseException {
        Date date1 = new Date();
        Date date2 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time1.equals("null") && time2.equals("null")) {
            time1="1900-01-01 00:00:00";
            time2="2900-01-01 00:00:00";
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
        } else {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
        }

        return resultPointService.GETtIME(city,station,date1,date2);
    }

}
