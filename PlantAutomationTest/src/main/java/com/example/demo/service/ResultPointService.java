package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultPointService {

    @Autowired
    YXPointResultRepo yxPointResultRepo;
    @Autowired
    VeMaRepo veMaRepo;
    @Autowired
    TreetableRepo treetableRepo;
    @Autowired
    YXComparisonResultsRepo yxcomRepo;
    @Autowired
    YCComparisonResultsRepo yccomRepo;
    @Autowired
    YKComparisonResultsRepo ykcomRepo;
    @Autowired
    YTComparisonResultsRepo ytcomRepo;

    public JsonResult YXPointResult(String station, Date time){
        List<YXPointResult> YXResult  = yxPointResultRepo.findAllByYXPointResult(station, time);
        return JsonResult.successResult(YXResult);
    }

    public JsonResult VeMaResult(String station, Date time){
        List<VeMa> vema = veMaRepo.findAllByVeMaResult(station,time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(VeMa aa:vema){
            String time123 = simpleDateFormat.format(aa.getTime());
            aa.setTimeZhuan(time123);
        }
        return JsonResult.successResult(vema);
    }

    public JsonResult YxRecords(String station, Date time){
        List<YXComparisonResults> yxRecoeds = yxcomRepo.findAllByYXRecords(station,time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(YXComparisonResults aa:yxRecoeds){
            String time123 = simpleDateFormat.format(aa.getYx_comparisontime());
            aa.setTimestring(time123);
        }
        return JsonResult.successResult(yxRecoeds);
    }

    public JsonResult YcRecords(String station, Date time){
        List<YCComparisonResults> ycRecoeds = yccomRepo.findAllByYCRecords(station,time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(YCComparisonResults aa:ycRecoeds){
            String time123 = simpleDateFormat.format(aa.getYc_comparisontime());
            aa.setTimestring(time123);
        }
        return JsonResult.successResult(ycRecoeds);
    }

    public JsonResult YkRecords(String station, Date time){
        List<YKComparisonResults> ykRecoeds = ykcomRepo.findAllByYKRecords(station,time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(YKComparisonResults aa:ykRecoeds){
            String time123 = simpleDateFormat.format(aa.getYk_comparisontime());
            aa.setTimestring(time123);
        }
        return JsonResult.successResult(ykRecoeds);
    }

    public JsonResult YtRecords(String station, Date time){
        List<YTComparisonResults> ytRecoeds = ytcomRepo.findAllByYTRecords(station,time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(YTComparisonResults aa:ytRecoeds){
            String time123 = simpleDateFormat.format(aa.getYt_comparisontime());
            aa.setTimestring(time123);
        }
        return JsonResult.successResult(ytRecoeds);
    }

    public JsonResult GETtIME(String city,String station, Date time1,Date time2){
        List<Date> aa = new ArrayList<>();
        if(city.equals("null") && station.equals("null")){
            aa = veMaRepo.findAllTimeList(time1,time2);
        }
        if(!city.equals("null") && station.equals("null")){
            List<String> qq = treetableRepo.findSelectResult(city);
            for(String a:qq){
                List<Date>test = veMaRepo.findGetTimeList(a,time1,time2);
                aa.addAll(test);
            }
        }
        if(!city.equals("null") && !station.equals("null")){
            aa = veMaRepo.findGetTimeList(station,time1,time2);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> TimeList = new ArrayList<>();
        for(Date x:aa){
            String time = simpleDateFormat.format(x);
            TimeList.add(time);
        }
        Set<String> qwe = new HashSet<>(TimeList);
        List<String> www = new ArrayList<>(qwe);
        return JsonResult.successResult(www);
    }
}
