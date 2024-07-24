package com.example.demo.service;
import cn.hutool.crypto.digest.DigestUtil;
import com.example.demo.entity.*;
import com.example.demo.repo.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class FileEqualService {
    @Autowired
    BatchService batchService;
    @Autowired
    D5000yxRepo d5000yxRepo;
    @Autowired
    D5000ycRepo d5000ycRepo;
    @Autowired
    D5000ykRepo d5000ykRepo;
    @Autowired
    D5000ytRepo d5000ytRepo;
    @Autowired
    ConrRepo conrRepo;
    @Autowired
    HomeDataRepo hometableRepo;
    @Autowired
    BDZtableRepo bdztableRepo;
    @Autowired
    VermRepo vermRepo;
    @Autowired
    AlertMessageDataRepo alertMessageDataRepo;

    static int num=0;
    //public List<String> FileEqual(String afileroad,String bfileroad){
    public JsonResult FileEqual(){
        String YDO = "17";
        String YDT = "19";
        String GenPath = "F:/home/File/";
        String YDOFilePath = GenPath+YDO;
        String YDTFilePath = GenPath+YDT;
        List<String> result=EqualResult(YDOFilePath,YDTFilePath,YDO,YDT);
        String arrAll[];
        String abc;
        List<String> resultF=new ArrayList<>();
        for(String a:result){
            arrAll=a.split(",");
            if(arrAll[0].contains("文件不同步")){
                //abc=arrAll[1]+" 和 "+arrAll[2]+" 文件不同步";
                resultF.add(arrAll[0]);
                //System.out.println("校验结果为：文件数量不同！");
            }
            else if(arrAll[0].equals("文件名称不同")){
                abc=arrAll[1]+" 和 "+arrAll[2]+" 文件名称不同";
                resultF.add(abc);
                //System.out.println("校验结果为：文件名称不同！");
            }
            else if(arrAll[0].contains("文件大小相同-内容不同")) {
                abc=arrAll[1]+" 和 "+arrAll[2]+" 文件大小相同，内容不同";
                resultF.add(abc);
                //System.out.println(arrAll[1]+" 和 "+arrAll[2]+" 文件内容不同");
            }
            else if(arrAll[0].contains("文件大小不同")){
                abc=arrAll[1]+" 和 "+arrAll[2]+" 文件大小不同";
                resultF.add(abc);
                //System.out.println(arrAll[1]+" 和 "+arrAll[2]+" 文件大小不同");
            }
            else{
                System.out.println("校验错误");
            }
        }
        return JsonResult.successResult(resultF);
    }

    public static List<String> EqualResult(String filePath1, String filePath2,String YDO,String YDT) {
        List<String>result= new ArrayList<>();
        List<String>listAname=getFileNameList(filePath1);
        List<String>listBname=getFileNameList(filePath2);
        String path1 = filePath1;
        String path2 = filePath2;
//        if(listAname.size()!=listBname.size()){
//            result.add(YDO+"、"+YDT+"  文件个数不同");
//            //return result;
//        }
//        else{
//            result.add(YDO+"、"+YDT+"  文件个数相同");
//        }
        try {
            //文件内容and名称一致性
            A: for(String afile : listAname) {
                int id=0;
                for (String bfile : listBname) {
                    if(!afile.equals(bfile)){
                        continue;
                    }
                    else if(afile.equals(bfile)){
                        id++;
                        num++;
                        String afileroad = path1 + "/" +afile;
                        String bfileroad = path2 + "/" +bfile;
                        Boolean flag = compareFiles(afileroad, bfileroad);
                        if(flag){
                            continue A;
                        }
                        else {
                            //System.out.println(flag+","+num+","+afile);
                            FileInputStream afis = new FileInputStream(afileroad);
                            FileInputStream bfis = new FileInputStream(bfileroad);
                            int len1 = afis.available();
                            int len2 = bfis.available();
                            //System.out.println(len1+","+len2);
                            if(len1==len2){
                                byte[] data1 = new byte[len1];
                                byte[] data2 = new byte[len2];
                                afis.read(data1);
                                bfis.read(data2);
                                for (int i = 0; i < len1; i++) {
                                    if (data1[i] != data2[i]) {
                                        result.add("文件大小相同-内容不同"+","+afile+","+bfile);
                                        afis.close();
                                        bfis.close();
                                        continue A;
                                    }
                                }
                            }
                            else {
                                result.add("文件大小不同"+","+afile+","+bfile);
                                afis.close();
                                bfis.close();
                                continue A;
                            }
                        }
                    }
                }
                if(id==0){
                    result.add(afile+" 文件不同步");
                    //return result;
                }
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result.size()==0){
            result.add("相同");
            return result;
        }
        return result;
    }

    public static Boolean compareFiles(String file1, String file2) {
        try {
            FileInputStream fileInputStream1 = new FileInputStream(new File(file1));
            FileInputStream fileInputStream2 = new FileInputStream(new File(file2));
            String s1 = DigestUtil.md5Hex(fileInputStream1);
            String s2 = DigestUtil.md5Hex(fileInputStream2);
            return s1.equals(s2);
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在,解析失败!");
        }
        return false;
    }

    public static List<String> getFileNameList(String filePath) {
        // 文件夹路径
        String path = filePath;
        File f = new File(path);
        if (!f.exists()) {
            //目录不存在就输出
            System.out.println(path + " not exists");
            return Collections.emptyList();
        }
        File[] fa = f.listFiles();
        ArrayList<String> fileList = new ArrayList<>();
        for (File fs : fa) {
            if (fs.isDirectory()) {
                // 目标文件为文件夹时
                System.out.println(path + fs.getName() + " [目录]");
            }
            else {
                //fileList.add(path + "/" + fs.getName());
                fileList.add(fs.getName());
            }
        }
        return fileList;
    }
    public void parseExcelFile(MultipartFile file,String sta_name,String tttime) throws IOException,ParseException{
//        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = dateFormat1.parse(tttime);
//        int jilu = 0;
//        List<List<String>> data = new ArrayList<>();
//        String fileName = file.getOriginalFilename();
//        String[] xx = fileName.split("信息表【江苏】");
//        //String stationName = xx[0];
//        String stationName = sta_name;
//        String[] timeList = xx[1].split("\\.");
//        String fileTime = timeList[0];
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//        Date fileDate = new Date();
//        try{
//            fileDate = dateFormat.parse(fileTime);
//        }
//        catch (ParseException e) {
//            e.printStackTrace();
//            System.out.println("日期解析失败：" + e.getMessage());
//        }
//        List<CZYXTable> yxList = new ArrayList<>();
//        List<CZYCTable> ycList = new ArrayList<>();
//        List<CZYKTable> ykList = new ArrayList<>();
//
//        try (InputStream inputStream = file.getInputStream()) {
//            Workbook workbook;
//            if (fileName.endsWith(".xls")) {
//                workbook = new HSSFWorkbook(inputStream);
//            }
//            else{
//                workbook = new XSSFWorkbook(inputStream);
//            }
//            Map<String,Integer> sheetList = new HashMap<>();
//            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//                String sheetName = workbook.getSheetName(i);
//                if(sheetName.equals("遥信")){
//                    sheetList.put("遥信",i);
//                }
//                else if(sheetName.equals("遥测")){
//                    sheetList.put("遥测",i);
//                }
//                else if(sheetName.equals("遥控")){
//                    sheetList.put("遥控",i);
//                }
//            }
//            for (Map.Entry<String, Integer> entry : sheetList.entrySet()) {
//                String key = entry.getKey();
//                Integer value = entry.getValue();
//                //System.out.println(key + ": " + value);
//
//                Sheet sheet = workbook.getSheetAt(value);
//                Iterator<Row> rowIterator = sheet.iterator();
//                int addstate=0;
//                A:while (rowIterator.hasNext()) {
//                    Row row = rowIterator.next();
//                    Iterator<Cell> cellIterator = row.cellIterator();
//                    List<String> rowData = new ArrayList<>();
//                    while (cellIterator.hasNext()) {
//                        Cell cell = cellIterator.next();
//                        switch (cell.getCellType()) {
//                            case STRING:
//                                rowData.add(cell.getStringCellValue());
//                                break;
//                            case NUMERIC:
//                                if (DateUtil.isCellDateFormatted(cell)) {
//                                    rowData.add(cell.getDateCellValue().toString());
//                                } else {
//                                    int intValue = (int) cell.getNumericCellValue();
//                                    rowData.add(String.valueOf(intValue));
//                                }
//                                break;
//                            case BOOLEAN:
//                                rowData.add(String.valueOf(cell.getBooleanCellValue()));
//                                break;
//                            case BLANK:
//                                rowData.add("");
//                                break;
//                            default:
//                                rowData.add("");
//                        }
//                    }
//                    if(rowData.size()<7){
//                        continue;
//                    }
//                    List<String> newList = new ArrayList<>();
//                    if(key.equals("遥控")){
//                        newList = IntStream.of(0,5)
//                                .mapToObj(rowData::get)
//                                .collect(Collectors.toList());
//                    }
//                    else {
//                        newList = IntStream.of(0,6)
//                                .mapToObj(rowData::get)
//                                .collect(Collectors.toList());
//                    }
//                    if(newList.get(0).equals("index")){
//                        addstate=1;
//                        continue A;
//                    }
//                    if(newList.get(1).equals("备用")){
//                        continue A;
//                    }
//                    if(addstate==1){
//                        data.add(newList);
//                        if(key.equals("遥信")){
//                            CZYXTable yx = new CZYXTable();
//                            yx.setSt_name(stationName);
//                            yx.setTtime(date);
//                            yx.setDot_no(newList.get(0));
//                            yx.setYxname(newList.get(1));
//                            yx.setState(1);
//                            yxList.add(yx);
//                        }
//                        else if(key.equals("遥测")){
//                            CZYCTable yc = new CZYCTable();
//                            yc.setSt_name(stationName);
//                            yc.setTtime(date);
//                            yc.setDot_no(newList.get(0));
//                            yc.setYcname(newList.get(1));
//                            yc.setState(1);
//                            ycList.add(yc);
//                        }
//                        else if(key.equals("遥控")){
//                            CZYKTable yk = new CZYKTable();
//                            yk.setSt_name(stationName);
//                            yk.setTtime(date);
//                            yk.setDot_no(newList.get(0));
//                            yk.setYkname(newList.get(1));
//                            yk.setState(1);
//                            ykList.add(yk);
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//-------------------------------------------------------------------------------------
        int jilu = 0;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat1.parse(tttime);
        int type = 0;
        List<List<String>> data = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        String stationName = sta_name;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Date currentDate = new Date();

        List<CZYXTable> yxList = new ArrayList<>();
        List<CZYCTable> ycList = new ArrayList<>();
        List<CZYKTable> ykList = new ArrayList<>();
        List<CZYTTable> ytList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook;
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook(inputStream);
            }
            Map<String, Integer> sheetList = new HashMap<>();
            if (workbook.getNumberOfSheets()==4) {
                type = 1;
            }
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String sheetName = workbook.getSheetName(i);
                if (sheetName.equals("遥信")) {
                    sheetList.put("遥信", i);
                } else if (sheetName.equals("遥测")) {
                    sheetList.put("遥测", i);
                } else if (sheetName.equals("遥控")) {
                    sheetList.put("遥控", i);
                } else if (sheetName.equals("遥调")) {
                    sheetList.put("遥调", i);
                }
            }
            for (Map.Entry<String, Integer> entry : sheetList.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                //System.out.println(key + ": " + value);

                Sheet sheet = workbook.getSheetAt(value);
                Iterator<Row> rowIterator = sheet.iterator();
                int addstate = 0;
                A:
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    List<String> rowData = new ArrayList<>();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case STRING:
                                rowData.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    rowData.add(cell.getDateCellValue().toString());
                                } else {
                                    int intValue = (int) cell.getNumericCellValue();
                                    rowData.add(String.valueOf(intValue));
                                }
                                break;
                            case BOOLEAN:
                                rowData.add(String.valueOf(cell.getBooleanCellValue()));
                                break;
                            case BLANK:
                                rowData.add("");
                                break;
                            default:
                                rowData.add("");
                        }
                    }
                    if (rowData.size() == 0) {
                        continue;
                    }
                    List<String> newList = new ArrayList<>();
                    if (key.equals("遥控")) {
                        if(type==1){
                            newList = IntStream.of(0, 1)
                                    .mapToObj(rowData::get)
                                    .collect(Collectors.toList());
                        }
                        else{
                            if(rowData.size()>=6){
                                newList = IntStream.of(0, 5)
                                        .mapToObj(rowData::get)
                                        .collect(Collectors.toList());
                            }
                        }
                    } else {
                        if(type==1){
                            newList = IntStream.of(0, 1)
                                    .mapToObj(rowData::get)
                                    .collect(Collectors.toList());
                        }
                        else{
                            if(rowData.size()>=7){
                                newList = IntStream.of(0, 6)
                                        .mapToObj(rowData::get)
                                        .collect(Collectors.toList());
                            }
                        }
                    }
                    if(newList.size()!=0){
                        if (newList.get(0).equals("index") ) {
                            addstate = 1;
                            continue A;
                        }
                        else if(newList.get(0).equals("遥信点号") || newList.get(0).equals("遥测点号")||newList.get(1).equals("遥信名称") || newList.get(1).equals("遥测名称") || newList.get(0).equals("遥控点号") || newList.get(1).equals("遥控名称")|| newList.get(1).equals("遥调名称")){
                            type =1;
                            addstate = 1;
                            continue A;
                        }
                        if(newList.size()>3){
                            if(newList.get(3).equals("名称")){
                                type =1;
                                addstate = 1;
                                continue A;
                            }
                        }
                        if (newList.get(1).equals("备用")) {
                            continue A;
                        }
                        if (addstate == 1) {
                            if("".equals(newList.get(0))||"".equals(newList.get(1))){
                                continue A;
                            }
                            data.add(newList);
                            if (key.equals("遥信")) {
                                CZYXTable yx = new CZYXTable();
                                yx.setSt_name(stationName);
                                yx.setTtime(currentDate);
                                yx.setDot_no(newList.get(0));
                                yx.setYxname(newList.get(1));
                                yx.setState(0);
                                yxList.add(yx);
                            } else if (key.equals("遥测")) {
                                CZYCTable yc = new CZYCTable();
                                yc.setSt_name(stationName);
                                yc.setTtime(currentDate);
                                yc.setDot_no(newList.get(0));
                                yc.setYcname(newList.get(1));
                                yc.setState(0);
                                ycList.add(yc);
                            } else if (key.equals("遥控")) {
                                CZYKTable yk = new CZYKTable();
                                yk.setSt_name(stationName);
                                yk.setTtime(currentDate);
                                yk.setDot_no(newList.get(0));
                                yk.setYkname(newList.get(1));
                                yk.setState(0);
                                ykList.add(yk);
                            } else if (key.equals("遥调")) {
                                CZYTTable yt = new CZYTTable();
                                yt.setSt_name(stationName);
                                yt.setTtime(currentDate);
                                yt.setDot_no(newList.get(0));
                                yt.setYtname(newList.get(1));
                                yt.setState(0);
                                ytList.add(yt);
                            }
                        }
                    }

                }
            }
//            batchService.batchInsert(yxList);
//            batchService.batchInsert(ycList);
//            batchService.batchInsert(ykList);
        } catch (IOException e) {
            e.printStackTrace();
        }
//-------------------------------------------------------------------------------------
//excel文件入库校核
        if(yxList.size()!=0){
            batchService.batchInsert(yxList);
            String checkTime;
            List<YXComparisonResults> crList = new ArrayList<>();
            List<D5000yx> d5000yxList = d5000yxRepo.findAllByYXPointResult(stationName);

            if(d5000yxList.size()==0){
                System.out.println(sta_name+"没有找到对应的遥信数据！");
            }
            else{
                int checkState = 0;
//校核---------------------------------------------
                for(CZYXTable yxjl : yxList){
                    String point = yxjl.getDot_no();
                    List<D5000yx> pointList = d5000yxList.stream().filter(item->item.getDot_no().equals(point)).collect(Collectors.toList());
                    if(pointList.size()==0){
                        checkState=1;
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        YXComparisonResults cr = new YXComparisonResults();
                        cr.setSt_name(stationName);
                        cr.setYdo_yx_name(yxjl.getYxname());
                        cr.setYdo_yx_dot_no(yxjl.getDot_no());
                        cr.setReason("没有对应点号");
//                        cr.setYx_comparisontime(date);
                        cr.setYx_comparisontime(currentDate);
                        cr.setYx_comparisonresult("异常");
                        cr.setTimestring(dateFormat123.format(currentDate));
                        crList.add(cr);
                    }
                    else{
                        D5000yx xxxx = pointList.stream().max(Comparator.comparing(D5000yx::getTtime)).orElse(null);
                        String str1 = yxjl.getYxname();
                        String str2 = xxxx.getYx_name();
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (matchCharacters(str1, str2)) {
//                            System.out.println("字符串匹配成功");
                            YXComparisonResults cr = new YXComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yx_name(yxjl.getYxname());
                            cr.setYdo_yx_dot_no(yxjl.getDot_no());
                            cr.setD5000_yx_name(xxxx.getYx_name());
                            cr.setD5000_yx_dot_no(xxxx.getDot_no());
                            cr.setReason("校核正常");
//                            cr.setYx_comparisontime(date);currentDate
                            cr.setYx_comparisontime(currentDate);
                            cr.setYx_comparisonresult("正常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                        else {
                            checkState=1;
                            YXComparisonResults cr = new YXComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yx_name(yxjl.getYxname());
                            cr.setYdo_yx_dot_no(yxjl.getDot_no());
                            cr.setD5000_yx_name(xxxx.getYx_name());
                            cr.setD5000_yx_dot_no(xxxx.getDot_no());
                            cr.setReason("点号相同，遥信名称不同");
//                            cr.setYx_comparisontime(date);
                            cr.setYx_comparisontime(currentDate);
                            cr.setYx_comparisonresult("异常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                    }
                }
                batchService.batchInsert(crList);
                //校核记录
                Conr cc = new Conr();
                Verificationmanagement verm = new Verificationmanagement();
                AlertMessageData alert = new AlertMessageData();
                alert.setStation_name(stationName);
                alert.setAlert_name("点表比对(遥信)");
                alert.setAlert_time(currentDate);
                alert.setHandle_state("未处理");
                verm.setStation_name(stationName);
                verm.setType("点表比对(遥信)");
                verm.setTime(currentDate);
                verm.setReason("查看详情");
                cc.setTtime(currentDate);
                cc.setComparison(stationName);
                checkTime=dateFormat.format(currentDate);
                cc.setStringtime(checkTime);
                if(checkState==1){
                    cc.setCheckstate("异常");
                    verm.setResult("异常");
                    alertMessageDataRepo.save(alert);
                }
                else if(checkState==0){
                    cc.setCheckstate("正常");
                    verm.setResult("正常");
                }
                vermRepo.save(verm);
                conrRepo.save(cc);
                jilu++;

                List<HomeData> ss =hometableRepo.findAll();
                for(HomeData tt : ss){
                    int count = tt.getCheckcount();
                    count=count+1;
                    tt.setCheckcount(count);
                    hometableRepo.save(tt);
                }
            }
        }

        if(ycList.size()!=0){
            batchService.batchInsert(ycList);
            String checkTime;
            List<YCComparisonResults> crList = new ArrayList<>();
            List<D5000yc> d5000ycList = d5000ycRepo.findAllByYCPointResult(stationName);

            if(d5000ycList.size()==0){
                System.out.println(sta_name+"没有找到对应的遥测数据！");
            }
            else{
                int checkState = 0;
//校核---------------------------------------------
                for(CZYCTable yxjl : ycList){
                    String point = yxjl.getDot_no();
                    List<D5000yc> pointList = d5000ycList.stream().filter(item->item.getDot_no().equals(point)).collect(Collectors.toList());
                    if(pointList.size()==0){
                        checkState=1;
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        YCComparisonResults cr = new YCComparisonResults();
                        cr.setSt_name(stationName);
                        cr.setYdo_yc_name(yxjl.getYcname());
                        cr.setYdo_yc_dot_no(yxjl.getDot_no());
                        cr.setReason("没有对应点号");
                        cr.setYc_comparisontime(currentDate);
                        cr.setYc_comparisonresult("异常");
                        cr.setTimestring(dateFormat123.format(currentDate));
                        crList.add(cr);
                    }
                    else{
                        D5000yc xxxx = pointList.stream().max(Comparator.comparing(D5000yc::getTtime)).orElse(null);
                        String str1 = yxjl.getYcname();
                        String str2 = xxxx.getYc_name();
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (matchCharacters(str1, str2)) {
//                            System.out.println("字符串匹配成功");
                            YCComparisonResults cr = new YCComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yc_name(yxjl.getYcname());
                            cr.setYdo_yc_dot_no(yxjl.getDot_no());
                            cr.setD5000_yc_name(xxxx.getYc_name());
                            cr.setD5000_yc_dot_no(xxxx.getDot_no());
                            cr.setReason("校核正常");
                            cr.setYc_comparisontime(currentDate);
                            cr.setYc_comparisonresult("正常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                        else {
                            checkState=1;
                            YCComparisonResults cr = new YCComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yc_name(yxjl.getYcname());
                            cr.setYdo_yc_dot_no(yxjl.getDot_no());
                            cr.setD5000_yc_name(xxxx.getYc_name());
                            cr.setD5000_yc_dot_no(xxxx.getDot_no());
                            cr.setReason("点号相同，遥测名称不同");
                            cr.setYc_comparisontime(currentDate);
                            cr.setYc_comparisonresult("异常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                    }
                }
                batchService.batchInsert(crList);
                //校核记录
                Conr cc = new Conr();
                Verificationmanagement verm = new Verificationmanagement();
                AlertMessageData alert = new AlertMessageData();
                alert.setStation_name(stationName);
                alert.setAlert_name("点表比对(遥测)");
                alert.setAlert_time(currentDate);
                alert.setHandle_state("未处理");
                verm.setStation_name(stationName);
                verm.setType("点表比对(遥测)");
                verm.setTime(currentDate);
                verm.setReason("查看详情");
                cc.setTtime(currentDate);
                cc.setComparison(stationName);
                checkTime=dateFormat.format(currentDate);
                cc.setStringtime(checkTime);
                if(checkState==1){
                    cc.setCheckstate("异常");
                    verm.setResult("异常");
                    alertMessageDataRepo.save(alert);

                }
                else if(checkState==0){
                    cc.setCheckstate("正常");
                    verm.setResult("正常");
                }
                vermRepo.save(verm);
                if(jilu==0){
                    conrRepo.save(cc);
                }
                List<HomeData> ss =hometableRepo.findAll();
                for(HomeData tt : ss){
                    int count = tt.getCheckcount();
                    count=count+1;
                    tt.setCheckcount(count);
                    hometableRepo.save(tt);
                }
            }
        }

        if(ykList.size()!=0){
            batchService.batchInsert(ykList);
            String checkTime;
            List<YKComparisonResults> crList = new ArrayList<>();
            List<D5000yk> d5000ykList = d5000ykRepo.findAllByYKPointResult(stationName);

            if(d5000ykList.size()==0){
                System.out.println(sta_name+"没有找到对应的遥控数据！");
            }
            else{
                int checkState = 0;
//校核---------------------------------------------
                for(CZYKTable yxjl : ykList){
                    String point = yxjl.getDot_no();
                    List<D5000yk> pointList = d5000ykList.stream().filter(item->item.getDot_no().equals(point)).collect(Collectors.toList());
                    if(pointList.size()==0){
                        checkState=1;
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        YKComparisonResults cr = new YKComparisonResults();
                        cr.setSt_name(stationName);
                        cr.setYdo_yk_name(yxjl.getYkname());
                        cr.setYdo_yk_dot_no(yxjl.getDot_no());
                        cr.setReason("没有对应点号");
                        cr.setYk_comparisontime(currentDate);
                        cr.setYk_comparisonresult("异常");
                        cr.setTimestring(dateFormat123.format(currentDate));
                        crList.add(cr);
                    }
                    else{
                        D5000yk xxxx = pointList.stream().max(Comparator.comparing(D5000yk::getTtime)).orElse(null);
                        String str1 = yxjl.getYkname();
                        String str2 = xxxx.getYk_name();
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (matchCharacters(str1, str2)) {
//                            System.out.println("字符串匹配成功");
                            YKComparisonResults cr = new YKComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yk_name(yxjl.getYkname());
                            cr.setYdo_yk_dot_no(yxjl.getDot_no());
                            cr.setD5000_yk_name(xxxx.getYk_name());
                            cr.setD5000_yk_dot_no(xxxx.getDot_no());
                            cr.setReason("校核正常");
                            cr.setYk_comparisontime(currentDate);
                            cr.setYk_comparisonresult("正常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                        else {
                            checkState=1;
                            YKComparisonResults cr = new YKComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yk_name(yxjl.getYkname());
                            cr.setYdo_yk_dot_no(yxjl.getDot_no());
                            cr.setD5000_yk_name(xxxx.getYk_name());
                            cr.setD5000_yk_dot_no(xxxx.getDot_no());
                            cr.setReason("点号相同，遥控名称不同");
                            cr.setYk_comparisontime(currentDate);
                            cr.setYk_comparisonresult("异常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                    }
                }
                batchService.batchInsert(crList);
                //校核记录
                Conr cc = new Conr();
                Verificationmanagement verm = new Verificationmanagement();
                AlertMessageData alert = new AlertMessageData();
                alert.setStation_name(stationName);
                alert.setAlert_name("点表比对(遥控)");
                alert.setAlert_time(currentDate);
                alert.setHandle_state("未处理");
                verm.setStation_name(stationName);
                verm.setType("点表比对(遥控)");
                verm.setTime(currentDate);
                verm.setReason("查看详情");
                cc.setTtime(currentDate);
                cc.setComparison(stationName);
                checkTime=dateFormat.format(currentDate);
                cc.setStringtime(checkTime);
                if(checkState==1){
                    cc.setCheckstate("异常");
                    verm.setResult("异常");
                    alertMessageDataRepo.save(alert);
                }
                else if(checkState==0){
                    cc.setCheckstate("正常");
                    verm.setResult("正常");
                }
                vermRepo.save(verm);
                if(jilu==0){
                    conrRepo.save(cc);
                }

                List<HomeData> ss =hometableRepo.findAll();
                for(HomeData tt : ss){
                    int count = tt.getCheckcount();
                    count=count+1;
                    tt.setCheckcount(count);
                    hometableRepo.save(tt);
                }
            }
        }
        if(ytList.size()!=0){
            batchService.batchInsert(ytList);
            String checkTime;
            List<YTComparisonResults> crList = new ArrayList<>();
            List<D5000yt> d5000ytList = d5000ytRepo.findAllByYTPointResult(stationName);

            if(d5000ytList.size()==0){
                System.out.println(sta_name+"没有找到对应的遥调数据！");
            }
            else{
                int checkState = 0;
//校核---------------------------------------------
                for(CZYTTable yxjl : ytList){
                    String point = yxjl.getDot_no();
                    List<D5000yt> pointList = d5000ytList.stream().filter(item->item.getDot_no().equals(point)).collect(Collectors.toList());
                    if(pointList.size()==0){
                        checkState=1;
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        YTComparisonResults cr = new YTComparisonResults();
                        cr.setSt_name(stationName);
                        cr.setYdo_yt_name(yxjl.getYtname());
                        cr.setYdo_yt_dot_no(yxjl.getDot_no());
                        cr.setReason("没有对应点号");
                        cr.setYt_comparisontime(currentDate);
                        cr.setYt_comparisonresult("异常");
                        cr.setTimestring(dateFormat123.format(currentDate));
                        crList.add(cr);
                    }
                    else{
                        D5000yt xxxx = pointList.stream().max(Comparator.comparing(D5000yt::getTtime)).orElse(null);
                        String str1 = yxjl.getYtname();
                        String str2 = xxxx.getYt_name();
                        SimpleDateFormat dateFormat123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (matchCharacters(str1, str2)) {
//                            System.out.println("字符串匹配成功");
                            YTComparisonResults cr = new YTComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yt_name(yxjl.getYtname());
                            cr.setYdo_yt_dot_no(yxjl.getDot_no());
                            cr.setD5000_yt_name(xxxx.getYt_name());
                            cr.setD5000_yt_dot_no(xxxx.getDot_no());
                            cr.setReason("校核正常");
                            cr.setYt_comparisontime(currentDate);
                            cr.setYt_comparisonresult("正常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                        else {
                            checkState=1;
                            YTComparisonResults cr = new YTComparisonResults();
                            cr.setSt_name(stationName);
                            cr.setYdo_yt_name(yxjl.getYtname());
                            cr.setYdo_yt_dot_no(yxjl.getDot_no());
                            cr.setD5000_yt_name(xxxx.getYt_name());
                            cr.setD5000_yt_dot_no(xxxx.getDot_no());
                            cr.setReason("点号相同，遥调名称不同");
                            cr.setYt_comparisontime(currentDate);
                            cr.setYt_comparisonresult("异常");
                            cr.setTimestring(dateFormat123.format(currentDate));
                            crList.add(cr);
                        }
                    }
                }
                batchService.batchInsert(crList);
                //校核记录
                Conr cc = new Conr();
                Verificationmanagement verm = new Verificationmanagement();
                AlertMessageData alert = new AlertMessageData();
                alert.setStation_name(stationName);
                alert.setAlert_name("点表比对(遥调)");
                alert.setAlert_time(currentDate);
                alert.setHandle_state("未处理");
                verm.setStation_name(stationName);
                verm.setType("点表比对(遥调)");
                verm.setTime(currentDate);
                verm.setReason("查看详情");
                cc.setTtime(currentDate);
                cc.setComparison(stationName);
                checkTime=dateFormat.format(currentDate);
                cc.setStringtime(checkTime);
                if(checkState==1){
                    cc.setCheckstate("异常");
                    verm.setResult("异常");
                    alertMessageDataRepo.save(alert);
                }
                else if(checkState==0){
                    cc.setCheckstate("正常");
                    verm.setResult("正常");
                }
                vermRepo.save(verm);
                if(jilu==0){
                    conrRepo.save(cc);
                }

                List<HomeData> ss =hometableRepo.findAll();
                for(HomeData tt : ss){
                    int count = tt.getCheckcount();
                    count=count+1;
                    tt.setCheckcount(count);
                    hometableRepo.save(tt);
                }
            }
        }
    }

    public static boolean matchCharacters(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();

        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                int m = i;
                int n = j;

                while (m < len1 && n < len2 && str1.charAt(m) == str2.charAt(n)) {
                    m++;
                    n++;
                }

                if (m - i > 1) {
                    return true; // 如果存在相同连续字符长度大于1，则返回true
                }
            }
        }

        return false; // 如果没有找到相同的连续字符，则返回false
    }

    public void AddCZInFO(MultipartFile file,String sta_name,String tttime) throws IOException,ParseException {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat1.parse(tttime);
        int type = 0;
        List<List<String>> data = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        String stationName = sta_name;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Date currentDate = new Date();

        List<CZYXTable> yxList = new ArrayList<>();
        List<CZYCTable> ycList = new ArrayList<>();
        List<CZYKTable> ykList = new ArrayList<>();
        List<CZYTTable> ytList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook;
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook(inputStream);
            }
            Map<String, Integer> sheetList = new HashMap<>();
            if (workbook.getNumberOfSheets()==4) {
                type = 1;
            }
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String sheetName = workbook.getSheetName(i);
                if (sheetName.equals("遥信")) {
                    sheetList.put("遥信", i);
                } else if (sheetName.equals("遥测")) {
                    sheetList.put("遥测", i);
                } else if (sheetName.equals("遥控")) {
                    sheetList.put("遥控", i);
                } else if (sheetName.equals("遥调")) {
                    sheetList.put("遥调", i);
                }
            }
            for (Map.Entry<String, Integer> entry : sheetList.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                //System.out.println(key + ": " + value);

                Sheet sheet = workbook.getSheetAt(value);
                Iterator<Row> rowIterator = sheet.iterator();
                int addstate = 0;
                A:
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    List<String> rowData = new ArrayList<>();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case STRING:
                                rowData.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    rowData.add(cell.getDateCellValue().toString());
                                } else {
                                    int intValue = (int) cell.getNumericCellValue();
                                    rowData.add(String.valueOf(intValue));
                                }
                                break;
                            case BOOLEAN:
                                rowData.add(String.valueOf(cell.getBooleanCellValue()));
                                break;
                            case BLANK:
                                rowData.add("");
                                break;
                            default:
                                rowData.add("");
                        }
                    }
                    if (rowData.size() == 0) {
                        continue;
                    }
                    List<String> newList = new ArrayList<>();
                    if (key.equals("遥控") || key.equals("遥调")) {
                        if(type==1){
                            newList = IntStream.of(0, 1)
                                    .mapToObj(rowData::get)
                                    .collect(Collectors.toList());
                        }
                        else{
                            if(rowData.size()>=6){
                                newList = IntStream.of(0, 5)
                                        .mapToObj(rowData::get)
                                        .collect(Collectors.toList());
                            }
                        }
                    } else {
                        if(type==1){
                            newList = IntStream.of(0, 1)
                                    .mapToObj(rowData::get)
                                    .collect(Collectors.toList());
                        }
                        else{
                            if(rowData.size()>=7){
                                newList = IntStream.of(0, 6)
                                        .mapToObj(rowData::get)
                                        .collect(Collectors.toList());
                            }
                        }
                    }
                    if(newList.size()!=0){
                        if (newList.get(0).equals("index") ) {
                            addstate = 1;
                            continue A;
                        }
                        else if(newList.get(0).equals("遥信点号") || newList.get(0).equals("遥测点号")||newList.get(1).equals("遥信名称") || newList.get(1).equals("遥测名称") || newList.get(0).equals("遥控点号") || newList.get(1).equals("遥控名称")|| newList.get(1).equals("遥调名称")){
                            type =1;
                            addstate = 1;
                            continue A;
                        }
                        if(newList.size()>3){
                            if(newList.get(3).equals("名称")){
                                type =1;
                                addstate = 1;
                                continue A;
                            }
                        }
                        if (newList.get(1).equals("备用")) {
                            continue A;
                        }
                        if (addstate == 1) {
                            if("".equals(newList.get(0))||"".equals(newList.get(1))){
                                continue A;
                            }
                            data.add(newList);
                            if (key.equals("遥信")) {
                                CZYXTable yx = new CZYXTable();
                                yx.setSt_name(stationName);
                                yx.setTtime(currentDate);
                                yx.setDot_no(newList.get(0));
                                yx.setYxname(newList.get(1));
                                yx.setState(0);
                                yxList.add(yx);
                            } else if (key.equals("遥测")) {
                                CZYCTable yc = new CZYCTable();
                                yc.setSt_name(stationName);
                                yc.setTtime(currentDate);
                                yc.setDot_no(newList.get(0));
                                yc.setYcname(newList.get(1));
                                yc.setState(0);
                                ycList.add(yc);
                            } else if (key.equals("遥控")) {
                                CZYKTable yk = new CZYKTable();
                                yk.setSt_name(stationName);
                                yk.setTtime(currentDate);
                                yk.setDot_no(newList.get(0));
                                yk.setYkname(newList.get(1));
                                yk.setState(0);
                                ykList.add(yk);
                            } else if (key.equals("遥调")) {
                                CZYTTable yt = new CZYTTable();
                                yt.setSt_name(stationName);
                                yt.setTtime(currentDate);
                                yt.setDot_no(newList.get(0));
                                yt.setYtname(newList.get(1));
                                yt.setState(0);
                                ytList.add(yt);
                            }
                        }
                    }

                }
            }
            batchService.batchInsert(yxList);
            batchService.batchInsert(ycList);
            batchService.batchInsert(ykList);
            batchService.batchInsert(ytList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
