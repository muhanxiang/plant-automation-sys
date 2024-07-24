package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;
import java.util.stream.Collectors;

@Service
public class D5000QueryService {

    @Autowired
    D5000yxRepo d5000yxRepo;
    @Autowired
    D5000ycRepo d5000ycRepo;
    @Autowired
    D5000ykRepo d5000ykRepo;
    @Autowired
    D5000ytRepo d5000ytRepo;
    @Autowired
    YDoyxTableRepo ydoyxTableRepo;
    @Autowired
    CZYXTableRepo czYXTableRepo;
    @Autowired
    ConrRepo conrRepo;

    public JsonResult D5000YXData(String type,String station){
        if("null".equals(station)){
//            if("YX".equals(type)){
//                List<D5000yx> aaa=d5000yxRepo.findAllByNoStation();
//                if(aaa.size()>20){
//                    aaa.subList(0,aaa.size()-20).clear();
//                }
//                return JsonResult.successResult(aaa);
//            }
//            else if("YC".equals(type)){
//                List<D5000yc> aaa=d5000ycRepo.findAllByNoStation();
//                if(aaa.size()>20){
//                    aaa.subList(0,aaa.size()-20).clear();
//                }
//                return JsonResult.successResult(aaa);
//            }
        }
        else{
            if("YX".equals(type)){
                List<D5000yx> D5000yx_new = new ArrayList<>();
                List<D5000yx> aaa=d5000yxRepo.findAllByYXPointResult(station);
                D5000yx_new = aaa.stream().collect(Collectors.toMap(
                        person -> person.getDot_no() + "-" + person.getTtime(),
                        p -> p,
                        (p1, p2) -> p1))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                for(D5000yx yxx : D5000yx_new){
                    int count = Integer.valueOf(yxx.getDot_no());
                    yxx.setDot_int(count);
                }
                Collections.sort(D5000yx_new, Comparator.comparing(D5000yx::getDot_int));
                return JsonResult.successResult(D5000yx_new);
            }
            else if("YC".equals(type)){
                List<D5000yc> D5000yc_new = new ArrayList<>();
                List<D5000yc> aaa=d5000ycRepo.findAllByYCPointResult(station);
                D5000yc_new = aaa.stream().collect(Collectors.toMap(
                        person -> person.getDot_no() + "-" + person.getTtime(),
                        p -> p,
                        (p1, p2) -> p1))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                for(D5000yc yxx : D5000yc_new){
                    int count = Integer.valueOf(yxx.getDot_no());
                    yxx.setDot_int(count);
                }
                Collections.sort(D5000yc_new, Comparator.comparing(D5000yc::getDot_int));
                return JsonResult.successResult(D5000yc_new);
            }
            else if("YK".equals(type)){
                List<D5000yk> D5000yk_new = new ArrayList<>();
                List<D5000yk> ccc = d5000ykRepo.findAllByYKPointResult(station);
                D5000yk_new = ccc.stream().collect(Collectors.toMap(
                        person -> person.getDot_no() + "-" + person.getTtime(),
                        p -> p,
                        (p1, p2) -> p1))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                for(D5000yk yxx : D5000yk_new){
                    int count = Integer.valueOf(yxx.getDot_no());
                    yxx.setDot_int(count);
                }
                Collections.sort(D5000yk_new, Comparator.comparing(D5000yk::getDot_int));
                return JsonResult.successResult(D5000yk_new);
            }
            else if("YT".equals(type)){
                List<D5000yt> D5000yt_new = new ArrayList<>();
                List<D5000yt> ccc = d5000ytRepo.findAllByYTPointResult(station);
                D5000yt_new = ccc.stream().collect(Collectors.toMap(
                        person -> person.getDot_no() + "-" + person.getTtime(),
                        p -> p,
                        (p1, p2) -> p1))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                for(D5000yt yxx : D5000yt_new){
                    int count = Integer.valueOf(yxx.getDot_no());
                    yxx.setDot_int(count);
                }
                Collections.sort(D5000yt_new, Comparator.comparing(D5000yt::getDot_int));
                return JsonResult.successResult(D5000yt_new);
            }
        }
        return null;
    }

    public JsonResult D5000StationAllQuery(String station,String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        List<D5000yx> yx = new ArrayList<>();
        List<D5000yx> D5000yx_new = new ArrayList<>();
        try {
            if("".equals(time)){
                time = null;
            }
            if (time != null) {
                date = simpleDateFormat.parse(time);
                yx = d5000yxRepo.findAllByStationAndTime(station,date);
                D5000yx_new = yx.stream().collect(Collectors.toMap(
                        person -> person.getDot_no() + "-" + person.getTtime(),
                        p -> p,
                        (p1, p2) -> p1))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                for(D5000yx yxx : D5000yx_new){
                    int count = Integer.valueOf(yxx.getDot_no());
                    yxx.setDot_int(count);
                }
                Collections.sort(D5000yx_new, Comparator.comparing(D5000yx::getDot_int));
            } else {
                Date newtime = d5000yxRepo.findBymaxTime(station);
                yx = d5000yxRepo.findAllByStation(station,newtime);
                D5000yx_new = yx.stream().collect(Collectors.toMap(
                        person -> person.getDot_no() + "-" + person.getTtime(),
                        p -> p,
                        (p1, p2) -> p1))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                for(D5000yx yxx : D5000yx_new){
                    int count = Integer.valueOf(yxx.getDot_no());
                    yxx.setDot_int(count);
                }
                Collections.sort(D5000yx_new, Comparator.comparing(D5000yx::getDot_int));
            }
            for (D5000yx a : D5000yx_new) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
            }
            return JsonResult.successResult(D5000yx_new);
        }
        catch (ParseException e){
            e.printStackTrace();
            return JsonResult.failureResult(e.getMessage());
        }
    }

    public JsonResult ExcelTimeList(String station){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> TimeList = new ArrayList<>();
        List<Date> timeList = d5000yxRepo.findByTimeList(station);
        Collections.sort(timeList, new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                return date1.compareTo(date2);
            }
        });
        for(Date ti : timeList){
            String time = simpleDateFormat.format(ti);
            TimeList.add(time);
        }
        return JsonResult.successResult(TimeList);
    }

    public JsonResult YDTimeList(String station){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> TimeList = new ArrayList<>();
        List<Date> timeList = ydoyxTableRepo.findByTimeList(station);
        Collections.sort(timeList, new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                return date1.compareTo(date2);
            }
        });
        for(Date ti : timeList){
            String time = simpleDateFormat.format(ti);
            TimeList.add(time);
        }
        return JsonResult.successResult(TimeList);
    }

    public JsonResult VersionTimeList(String station){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> TimeList = new ArrayList<>();
        List<Date> timeList = czYXTableRepo.findByTimeList(station);
        List<Date> yiCheckTime = conrRepo.findStationTimeRecords(station);
        List<String> yiCheckList = new ArrayList<>();
        for(Date ttt:yiCheckTime){
            String time = simpleDateFormat.format(ttt);
            yiCheckList.add(time);
        }

        Collections.sort(timeList, new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                return date1.compareTo(date2);
            }
        });
        for(Date ti : timeList){
            String time = simpleDateFormat.format(ti);
            if(yiCheckList.contains(time)){
                time=time+"*";
            }
            TimeList.add(time);
        }
        List<Select> TreeList = new ArrayList<>();
        for(String time : TimeList){
            Select tree1 = new Select();
            tree1.setLabel(time);
            tree1.setValue(time);
            TreeList.add(tree1);
        }

        return JsonResult.successResult(TreeList);
    }

}
