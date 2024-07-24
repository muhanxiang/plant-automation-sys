package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.D5000QueryService;
import com.example.demo.service.FileEqualService;
import com.example.demo.service.QueryService;
import com.example.demo.service.ResultPointService;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("Query")
@CrossOrigin
public class QueryController {
    @Value("${file.CIME.path}")
    String CIME_path;
    @Autowired
    D5000QueryService d5000QueryService;
    @Autowired
    QueryService queryService;
    @Autowired
    FileEqualService fileEqualService;
    @Autowired
    ResultPointService resultPointService;

    @GetMapping("d5000")
    public JsonResult getYXPointResult(@RequestParam(defaultValue = "null") String station, @RequestParam(defaultValue = "null") String type)throws ParseException {
        return d5000QueryService.D5000YXData(type,station);
    }

    @GetMapping("ydoAll")
    public JsonResult getYDOAllResult(@RequestParam(defaultValue = "null") String type) throws ParseException {
        return queryService.YDOAllData(type);
    }

    @GetMapping("ydo")
    public JsonResult getydoYXPointR8esult(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time,@RequestParam(defaultValue = "null") String type)throws ParseException {
        try {
            if (station.equals("null") && time.equals("null")) {
                return null;
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = simpleDateFormat.parse(time);
                return queryService.dyoData(station, date, type);
            }
        }
        catch (ParseException e){
            e.printStackTrace();
            return JsonResult.failureResult(e.getMessage());
        }
    }

    @GetMapping("tree")
    public JsonResult getTreeTableResult() throws ParseException {
        return queryService.TreeData();
    }

    @GetMapping("stationList")
    public JsonResult getStationListTableResult() throws ParseException {
        return queryService.StationListData();
    }

    @GetMapping("stationInfomation")
    public JsonResult getStationInfomationResult() throws ParseException {
        return queryService.StationIinfomationData();
    }

    @GetMapping("stationQuexian")
    public JsonResult getstationQuexianResult() throws ParseException {
        return queryService.StationQueXianData();
    }

    @GetMapping("FiveDayTime")
    public JsonResult getFiveDayTimeResult() throws ParseException {
        return queryService.FiveDayData();
    }

    @GetMapping("versiontree")
    public JsonResult getVersionTreeTableResult() throws ParseException {
        return queryService.VersionTreeData();
    }

    @GetMapping("allversiontree")
    public JsonResult getAllVersionTreeTableResult() throws ParseException {
        return queryService.AllVersionTreeData();
    }

    @GetMapping("allversionhis")
    public JsonResult getAllVersionHisResult() throws ParseException {
        return queryService.AllVersionHisData();
    }

    @GetMapping("selectt")
    public JsonResult getselectt(String city) throws ParseException {
        return queryService.Selectt(city);
    }

    @GetMapping("d5000stationallquery")
    public JsonResult getd5000stationallquery(String station,String time) throws ParseException {
        return d5000QueryService.D5000StationAllQuery(station,time);
    }

    @GetMapping("home")
    public JsonResult getHomeDataResult() throws ParseException {
        return queryService.homeData();
    }

    @Getter
    static class PublicRequestBody{
        Integer yxgktotal;
        Integer yxgkonline;
        Integer yxgkoffline;
        Integer yctjtotal;
        Integer yctjdefect;
    }

    @PostMapping ("solveHome")
    public void getSolveHomeResult(@RequestBody PublicRequestBody home){
        queryService.SolveHomeData(home.yxgktotal,home.yxgkonline,home.yxgkoffline,home.yctjtotal,home.yctjdefect);
    }

    @Getter
    static class salertRequestBody{
        String station_name;
        String alert_name;
        String alert_time;
        String alertMesssage;
        String handle_peo;
    }
    @GetMapping("upalert")
    public JsonResult getUpAlert(@RequestParam(defaultValue = "0") int page) throws ParseException {
        page--;
        return queryService.UAlertMessage(page);
    }

    @GetMapping("selectalert")
    public JsonResult getselectalert(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time1,@RequestParam(defaultValue = "null") String time2,@RequestParam(defaultValue = "0") int page) throws ParseException {
        page--;
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
        return queryService.SelectAlertMessage(station,date1,date2,page);
    }

    @PostMapping ("soalert")
    public void getSoAlert(@RequestBody salertRequestBody alert) throws ParseException{
            Date date = new Date();
            if (alert.alert_time != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = simpleDateFormat.parse(alert.alert_time);
            }
            else {
                date=null;
            }
        queryService.SolveAlertData(alert.station_name,alert.alert_name,date, alert.alertMesssage, alert.handle_peo);
    }

    @GetMapping("upcity")
    public JsonResult getUpCity(@RequestParam(defaultValue = "null") String station) throws ParseException {
        return queryService.SelectCity(station);
    }

    @GetMapping("alertCount")
    public JsonResult getalertCount() throws ParseException {
        return queryService.QueryAlert();
    }

    @GetMapping("HomeData")
    public JsonResult getHomeData() throws ParseException {
        return queryService.QueryHomeData();
    }

    @GetMapping("checkRecords")
    public JsonResult getCheckRecords() throws ParseException {
        return queryService.getCheckRecords();
    }

    @GetMapping("ydoYXData")
    public JsonResult getydoYXData(@RequestParam(defaultValue = "null") String station,String time) throws ParseException {
        return queryService.getydoYXData(station,time);
    }

    @GetMapping("ydoYCData")
    public JsonResult getydoYCData(@RequestParam(defaultValue = "null") String station,String time) throws ParseException {
        return queryService.getydoYCData(station,time);
    }

    @GetMapping("ydoYKData")
    public JsonResult getydoYKData(@RequestParam(defaultValue = "null") String station,String time) throws ParseException {
        return queryService.getydoYKData(station,time);
    }

    @GetMapping("ydoYTData")
    public JsonResult getydoYTData(@RequestParam(defaultValue = "null") String station,String time) throws ParseException {
        return queryService.getydoYTData(station,time);
    }

    @GetMapping("excelTimeList")
    public JsonResult getExcelTimeList(@RequestParam(defaultValue = "null") String station) throws ParseException {
        return d5000QueryService.ExcelTimeList(station);
    }

    @GetMapping("YDTimeList")
    public JsonResult getYDTimeList(@RequestParam(defaultValue = "null") String station) throws ParseException {
        return d5000QueryService.YDTimeList(station);
    }

    @GetMapping("VersionTimeList")
    public JsonResult getVersionTimeList(@RequestParam(defaultValue = "null") String station) throws ParseException {
        return d5000QueryService.VersionTimeList(station);
    }

    @GetMapping("stationCheckRecords")
    public JsonResult getstationCheckRecords(@RequestParam(defaultValue = "null") String station) throws ParseException {
        return queryService.getstationCheckRecords(station);
    }

    @GetMapping("FileCheckRecords")
    public JsonResult getFileCheckRecords(@RequestParam(defaultValue = "null") String station) throws ParseException {
        return queryService.getFileCheckRecords(station);
    }

    @GetMapping("FileCheck")
    public JsonResult getFileCheck(@RequestParam(defaultValue = "null") String station) throws ParseException {
        return queryService.getFileCheck(station);
    }

    @PostMapping("uploadExcel")
    public JsonResult uploadImg(@RequestParam MultipartFile file, @RequestParam(defaultValue = "123") String name) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            Date CheckDate = new Date(currentTimeMillis);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ttt = dateFormat1.format(CheckDate);

            String fileName = file.getOriginalFilename();
            String sta_name = name;
            // 处理 Excel 文件
            fileEqualService.parseExcelFile(file,sta_name,ttt);

            return JsonResult.successResult(ttt);

//            String targetPath = "F:/home/File/inExcel/" + fileName;
//            File targetFile = new File(targetPath);
//            file.transferTo(targetFile);
            //return "文件上传成功";
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            //return "文件上传失败";
        }
        return JsonResult.failureResult("上传失败！");
    }

    /**
     * 导出Excel
     */
    @GetMapping("exportToExcel")
    public void exportToExcel(HttpServletResponse response,@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String exceltime) throws IOException,ParseException{
        Workbook wb = queryService.writeToExcelByList(station,exceltime);
        OutputStream output = response.getOutputStream();
        String fileName = station+exceltime+".xlsx";
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ";" + "filename*=utf-8''" + fileName);
        wb.write(output);
        output.close();
    }

    @GetMapping("exportToExcel123")
    public void exportToExcel123(HttpServletResponse response,@RequestParam(defaultValue = "null") String station) throws IOException,ParseException{

        Workbook wb = queryService.writeToExcelXYDByList(station);
        OutputStream output = response.getOutputStream();
        String fileName = station+".xlsx";
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ";" + "filename*=utf-8''" + fileName);
        wb.write(output);
        output.close();
    }

    @GetMapping("StationAllList")
    public JsonResult getStationAllList() throws ParseException {
        return queryService.getStationAllList();
    }

    @GetMapping("StationAllTree")
    public JsonResult getStationAllTree() throws ParseException {
//        return queryService.getStationAllTree_NNew();
        return queryService.getRedisAllTree();
    }

    @GetMapping("SelectStationAll")
    public JsonResult getSelectStationAll() throws ParseException {
        return queryService.getSelectStation();
    }

    @GetMapping("XydAllRecord")
    public JsonResult getXydAllRecord() throws ParseException {
        return queryService.getXydAllRecord();
    }

    @GetMapping("VersionAllTree")
    public JsonResult getVersionAllTree() throws ParseException {
        return queryService.getVersionAllTree();
    }

    @PostMapping("addCZInfo")
    public void AddCZInfo(@RequestParam MultipartFile file, @RequestParam(defaultValue = "123") String name) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            Date CheckDate = new Date(currentTimeMillis);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ttt = dateFormat1.format(CheckDate);

            String fileName = file.getOriginalFilename();
            String sta_name = name;
            // 处理 Excel 文件
            fileEqualService.AddCZInFO(file,sta_name,ttt);

//            String targetPath = "F:/home/File/inExcel/" + fileName;
//            File targetFile = new File(targetPath);
//            file.transferTo(targetFile);
            //return "文件上传成功";
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            //return "文件上传失败";
        }
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

    @GetMapping("ycRecords")
    public JsonResult getCciRecords(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time)throws ParseException {
        Date date = new Date();
        if (time.equals("null")) {
            date=null;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(time);
        }
        return resultPointService.YcRecords(station,date);
    }

    @GetMapping("ykRecords")
    public JsonResult getKkiRecords(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time)throws ParseException {
        Date date = new Date();
        if (time.equals("null")) {
            date=null;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(time);
        }
        return resultPointService.YkRecords(station,date);
    }

    @GetMapping("ytRecords")
    public JsonResult getTtiRecords(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time)throws ParseException {
        Date date = new Date();
        if (time.equals("null")) {
            date=null;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(time);
        }
        return resultPointService.YtRecords(station,date);
    }

    @GetMapping("versionInfo")
    public JsonResult getversionInfoResult(@RequestParam(defaultValue = "null") String station,@RequestParam(defaultValue = "null") String time,@RequestParam(defaultValue = "null") String type)throws ParseException {
        try {
            if (station.equals("null") && time.equals("null")) {
                return JsonResult.failureResult("param error");
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = simpleDateFormat.parse(time);
                return queryService.VersionInfoSer(station, date, type);
            }
        }
        catch (ParseException e){
            e.printStackTrace();
            return JsonResult.failureResult(e.getMessage());
        }
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

    @GetMapping("redisTest")
    public JsonResult getRedis(){
        return queryService.getRedis();
    }

    @GetMapping("AcStation")
    public JsonResult getAcStation(@RequestParam(defaultValue = "null") String time)throws ParseException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        date = simpleDateFormat.parse(time);

        return queryService.getAcStation(date);
    }

    @GetMapping("getFileList")
    public JsonResult getFileList() throws Exception{
        List<CimeFile> fileList=queryService.getRemoteFileList();
        queryService.getLocalFileList(CIME_path,fileList);
        return JsonResult.successResult(fileList);
    }

    @GetMapping("tableData")
    public JsonResult getTableData(@RequestParam(required = false) String stationType,@RequestParam(required = false) String voltage,@RequestParam(required = false) String stationName){
        if(stationType==null&&voltage==null&&stationName==null)
            return queryService.getTableData();
        else if(voltage == null && stationName == null)
            return queryService.getTableDataByVoltage(stationType, null);
        else if(stationName == null&&stationType!=null)
            return queryService.getTableDataByVoltage(stationType,voltage);
        else if(stationType != null && voltage != null)
            return queryService.getTableDataByName(stationType,voltage,stationName);
        else
            return null;
    }

    @GetMapping("voltList")
    public JsonResult getVoltList(String stationType){
        return queryService.getVoltList(stationType);
    }

    @GetMapping("nameData")
    public JsonResult getNameData(String stationType,String voltage){
        return queryService.getNameData(stationType,voltage);
    }

    @PostMapping("typeMatch")
    public JsonResult typeMatch(@RequestParam MultipartFile file,@RequestParam String name){
        //需要获取到表的id，这里随便设置了一个
        Long tableId= 10L;
        TypeErrorVO typeErrorVO = queryService.matchTemplate(file, name, tableId);
        boolean res=typeErrorVO.getRes();
        if(res)
            return JsonResult.successResult(typeErrorVO.getList());
        return JsonResult.failureResult("未知类型的点表");
    }

    @GetMapping("changeDetail")
    public JsonResult getChangeDetail(String stationName,String time) throws ParseException {
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = simpleDateFormat.parse(time);

        List<Xyd_Alert_Records> list=queryService.getChangeDetail(stationName,date);
        return JsonResult.successResult(list);
    }


}
