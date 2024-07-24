package com.example.demo.service;

import cn.hutool.core.date.DateTime;
import com.example.demo.entity.*;
import com.example.demo.mapper.cimeFileMapper;
import com.example.demo.repo.*;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xm.Similarity;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QueryService {
    @Autowired
    private cimeFileMapper FileMapper;
    @Autowired
    TreetableRepo treetableRepo;
    @Autowired
    YDoyxTableRepo ydoyxTableRepo;
    @Autowired
    YDoycTableRepo ydoycTableRepo;
    @Autowired
    YDoykTableRepo ydoykTableRepo;
    @Autowired
    HomeDataRepo homeDataRepo;
    @Autowired
    AlertMessageDataRepo alertMessageDataRepo;
    @Autowired
    VersionDataRepo versionDataRepo;
    @Autowired
    VersionHisRepo versionHisRepo;
    @Autowired
    BDZtableRepo bdztableRepo;
    @Autowired
    FDCtableRepo fdctableRepo;
    @Autowired
    ConrRepo conrRepo;
    @Autowired
    YDtyxTableRepo ydtyxTableRepo;
    @Autowired
    FileComRecordsRepo fileComRecordsRepo;
    @Autowired
    FileComRepo fileComRepo;
    @Autowired
    D5000yxRepo d5000yxRepo;
    @Autowired
    D5000ycRepo d5000ycRepo;
    @Autowired
    D5000ykRepo d5000ykRepo;
    @Autowired
    CZYXTableRepo czYXTableRepo;
    @Autowired
    CZYCTableRepo czYCTableRepo;
    @Autowired
    CZYKTableRepo czYKTableRepo;
    @Autowired
    CZYTTableRepo czYTTableRepo;
    @Autowired
    FDCAllTableRepo fdcAllTableRepo;
    @Autowired
    BDZAllTableRepo bdzAllTableRepo;
    @Autowired
    CheckedRepo checkedRepo;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    Xyd_Ac_RecordsRepo xyd_ac_recordsRepo;
    @Autowired
    Xyd_Station_RecordsRepo xyd_station_recordsRepo;

    public QueryService(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
        // 设置 JSON 序列化器
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
    }

    public JsonResult TreeData(){
        List<treetable> treelist = treetableRepo.findAll();
        for(treetable aa:treelist){
            aa.setTimelist(TreeTimeList(aa.getTransformersubstation()));
        }

        Map<String,List<treetable>> citylist =treelist.stream().collect(Collectors.groupingBy(treetable::getCity));

        Set<String> CityAll = citylist.keySet();
        List<Tree> TreeList = new ArrayList<>();

        for(String city:CityAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<treetable> treetable = citylist.get(city);
            for(treetable n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n.getTransformersubstation());
                List<Tree> tilist = new ArrayList<>();
                for(Date aa:n.getTimelist()){
                    Tree tree2 = new Tree();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(aa);
                    tree2.setPlabel(n.getTransformersubstation());
                    tree2.setLabel(time);
                    tilist.add(tree2);
                }
                tree1.setChildren(tilist);
                stationall.add(tree1);
            }
            tree.setLabel(city);
            tree.setChildren(stationall);
            TreeList.add(tree);
        }
        return JsonResult.successResult(TreeList);
    }

    public JsonResult StationIinfomationData(){

        List<String> bdz = d5000yxRepo.findbdzAllList();
        List<String> fdc = d5000yxRepo.finddfcAllList();
        bdz.addAll(fdc);
        int AllCZCount=bdz.size();
        Map<String,String> CZList = new HashMap<>();

        for(String st : bdz){
            String[] xx = st.split(",");
            CZList.put(xx[0],xx[1]);
        }

        // 根据值(value)进行分类
        Map<String, List<String>> classifiedMap = CZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        LinkedHashMap<String, List<String>> sortedMap = classifiedMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<String>>comparingByKey((key1, key2) -> {
                    boolean key1Contains = key1.contains("江苏");
                    boolean key2Contains = key2.contains("江苏");

                    if (key1Contains == key2Contains) {
                        return key1.compareTo(key2);
                    } else if (key1Contains) {
                        return -1;
                    } else {
                        return 1;
                    }
                }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Set<String> CityAll = sortedMap.keySet();

        List<String> yiCheck = checkedRepo.findAllByStationResult();
        List<String> QueXianList = alertMessageDataRepo.findAllBySAlertResult();

        List<DWdata> dianwDataList = new ArrayList<>();
        int AllyiCheck=0;
        int Allquexian=0;
        for(Map.Entry<String, List<String>> entry:sortedMap.entrySet()){
            int yicheckNum=0;
            int QueXianNum=0;
            DWdata dianwData = new DWdata();
            dianwData.setAreaName(entry.getKey());
            dianwData.setAllCount(entry.getValue().size());
            List<String> fenzhi = entry.getValue();
            for(String a:fenzhi){
                if(yiCheck.contains(a)){
                    yicheckNum++;
                    AllyiCheck++;
                }
                if(QueXianList.contains(a)){
                    QueXianNum++;
                    Allquexian++;
                }
            }
            dianwData.setYiCheck(yicheckNum);
            double yicheckNum1 = yicheckNum;
            double bibi = (yicheckNum1/entry.getValue().size())*100;
            String percentageString = String.format("%.2f%%", bibi);
            dianwData.setQuexina(QueXianNum);
            double yquexianNum1 = QueXianNum;
            double quexinabibi = (yquexianNum1/entry.getValue().size())*100;
            String percentageString1 = String.format("%.2f%%", quexinabibi);
            dianwData.setBili(percentageString);
            dianwData.setQuexianbili(percentageString1);
            dianwData.setWeiCheck(dianwData.getAllCount()-dianwData.getYiCheck());
            dianwDataList.add(dianwData);
        }
        int AllweiCheck = AllCZCount-AllyiCheck;
        double AllyiCheck1 = AllyiCheck;
        double AllyiCheckbili = (AllyiCheck1/AllCZCount)*100;
        String AllyiCheckbiliFin = String.format("%.2f%%", AllyiCheckbili);
        dianwDataList.get(0).setAllCount(AllCZCount);
        dianwDataList.get(0).setAllyiCheck(AllyiCheck);
        dianwDataList.get(0).setAllweiCheck(AllweiCheck);
        dianwDataList.get(0).setAllcheckbili(AllyiCheckbiliFin);
        dianwDataList.get(0).setAllquexina(Allquexian);

        return JsonResult.successResult(dianwDataList);
    }

    public JsonResult StationQueXianData(){

        List<String> bdz = d5000yxRepo.findbdzAllList();
        List<String> fdc = d5000yxRepo.finddfcAllList();
        bdz.addAll(fdc);
        Map<String,String> CZList = new HashMap<>();

        for(String st : bdz){
            String[] xx = st.split(",");
            CZList.put(xx[0],xx[1]);
        }

        // 根据值(value)进行分类
        Map<String, List<String>> classifiedMap = CZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        LinkedHashMap<String, List<String>> sortedMap = classifiedMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<String>>comparingByKey((key1, key2) -> {
                    boolean key1Contains = key1.contains("江苏");
                    boolean key2Contains = key2.contains("江苏");

                    if (key1Contains == key2Contains) {
                        return key1.compareTo(key2);
                    } else if (key1Contains) {
                        return -1;
                    } else {
                        return 1;
                    }
                }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Set<String> CityAll = sortedMap.keySet();

        List<String> yiCheck = alertMessageDataRepo.findAllBySAlertResult();
        List<DWdata> dianwDataList = new ArrayList<>();
        for(Map.Entry<String, List<String>> entry:sortedMap.entrySet()){
            int yicheckNum=0;
            DWdata dianwData = new DWdata();
            dianwData.setAreaName(entry.getKey());
            dianwData.setAllCount(entry.getValue().size());
            List<String> fenzhi = entry.getValue();
            for(String a:fenzhi){
                if(yiCheck.contains(a)){
                    yicheckNum++;
                }
            }
            dianwData.setYiCheck(yicheckNum);
            dianwDataList.add(dianwData);
        }
        return JsonResult.successResult(dianwDataList);
    }

    public JsonResult FiveDayData() throws ParseException{
        LocalDate currentDate = LocalDate.now();
        List<Integer> FiveCount = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = currentDate.minusDays(i);
            LocalTime maxTime = LocalTime.MAX;
            LocalTime minTime = LocalTime.MIN;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datemax = format.parse(formatTime(date, maxTime));
            Date datemin = format.parse(formatTime(date, minTime));
            List<AlertMessageData> TimeCount = alertMessageDataRepo.findAllByAllQuexian(datemin,datemax);
            int count = TimeCount.size();
            FiveCount.add(count);
        }
        Collections.reverse(FiveCount);
        return JsonResult.successResult(FiveCount);
    }
    private static String formatTime(LocalDate date, LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date.atTime(time));
    }

    public JsonResult StationListData(){
        List<BDZtable> stationlist = bdztableRepo.findAll();
        List<FDCtable> fdcstationlist = fdctableRepo.findAll();

        for(FDCtable fdc:fdcstationlist){
            BDZtable bdz = new BDZtable();
            bdz.setStation_id(fdc.getStation_id());
            bdz.setStation_name(fdc.getStation_name());
            bdz.setArea(fdc.getArea());
            bdz.setCheckstate(fdc.getCheckstate());
            stationlist.add(bdz);
        }

        Map<String,List<BDZtable>> city_stationlist =stationlist.stream().collect(Collectors.groupingBy(BDZtable::getArea));
        Set<String> CityAll = city_stationlist.keySet();

        List<Tree> TreeList = new ArrayList<>();

        for(String city:CityAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<BDZtable> treetable = city_stationlist.get(city);
            for(BDZtable n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n.getStation_name());
                List<Tree> tilist = new ArrayList<>();
//------------------------------------------------
//                List<Date> timeList = d5000yxRepo.findByTimeList(n.getStation_name());
//                n.setTimelist(timeList);
//                for(Date aa:n.getTimelist()){
//                    Tree tree2 = new Tree();
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String time = simpleDateFormat.format(aa);
//                    tree2.setLabel(time);
//                    tilist.add(tree2);
//                }
//------------------------------------------------
                tree1.setChildren(tilist);
                stationall.add(tree1);
            }
            tree.setLabel(city);
            tree.setChildren(stationall);
            TreeList.add(tree);
        }
        return JsonResult.successResult(TreeList);
    }

    public JsonResult VersionTreeData(){
        List<treetable> treelist = treetableRepo.findAll();

        Map<String,List<treetable>> citylist =treelist.stream().collect(Collectors.groupingBy(treetable::getCity));

        Set<String> CityAll = citylist.keySet();
        List<Tree> TreeList = new ArrayList<>();

        for(String city:CityAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<treetable> treetable = citylist.get(city);
            for(treetable n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n.getTransformersubstation());
                List<Tree> tilist = new ArrayList<>();

                Tree tree2 = new Tree();
                String finallversion="";
                List<VersionData> versionlist = versionDataRepo.findByVersionResult(n.getVersionid());
                for(VersionData a:versionlist){
                    finallversion=a.getVersion()+"（"+a.getManufacturer()+"）";
                }
                if(finallversion.equals("")) {
                }
                else {
                    tree2.setLabel(finallversion);
                    tilist.add(tree2);
                }
                tree1.setChildren(tilist);
                stationall.add(tree1);
            }
            tree.setLabel(city);
            tree.setChildren(stationall);
            TreeList.add(tree);
        }
        return JsonResult.successResult(TreeList);
    }

    public JsonResult YDOAllData(String type) {
        if(type.equals("null") || type.equals("YX")){
            List<ydoyx> aa = ydoyxTableRepo.findAll();
            return JsonResult.successResult(aa);
        }
        else if(type.equals("YC")){
            List<ydoyc> aa = ydoycTableRepo.findAll();
            return JsonResult.successResult(aa);
        }
        else if(type.equals("YK")){
            List<ydoyk> aa = ydoykTableRepo.findAll();
            return JsonResult.successResult(aa);
        }
        return JsonResult.successResult("");
    }

    public JsonResult AllVersionTreeData(){
        List<VersionData> versionlist = versionDataRepo.findAll();

        Map<String,List<VersionData>> stalist =versionlist.stream().collect(Collectors.groupingBy(VersionData::getManufacturer));

        Set<String> StationAll = stalist.keySet();
        List<Tree> TreeList = new ArrayList<>();

        for(String station:StationAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<VersionData> treetable = stalist.get(station);
            for(VersionData n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n.getVersion());
//-------------------------------
                List<Tree> tilist = new ArrayList<>();
                String finallversion="";
                List<treetable> versionlist1 = treetableRepo.findSationResult(n.getId());
                for(treetable a:versionlist1){
                    finallversion=a.getTransformersubstation()+"（"+a.getCity()+"）";
                    if(finallversion.equals("")) {
                    }
                    else {
                        Tree tree2 = new Tree();
                        tree2.setLabel(finallversion);
                        tilist.add(tree2);
                    }
                }
                tree1.setChildren(tilist);
//-------------------------------
                stationall.add(tree1);
            }
            tree.setLabel(station);
            tree.setChildren(stationall);
            TreeList.add(tree);
        }
        return JsonResult.successResult(TreeList);
    }

    public JsonResult AllVersionHisData(){
        List<VersionHis> aaa = versionHisRepo.findAll();
        for(VersionHis bb:aaa){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(bb.getTtime());
            bb.setTttime(time);
        }
        return JsonResult.successResult(aaa);
    }

    public List<Date> TreeTimeList(String station){
        //List<Date> aaa = ydoyxTableRepo.findAllByYDOYXPointResult(station).stream().sorted().collect(Collectors.toList());
        //---后续添加遥测、遥控时间节点--//
        PageRequest pageReques = PageRequest.of(0, 10);
        List<Date> aaa = ydoyxTableRepo.findAllByYDOYXPointResult(station,pageReques).stream().sorted().collect(Collectors.toList());
        return aaa;
    }

    public JsonResult dyoData(String station,Date time,String type){
        if("YX".equals(type)){
            List<ydoyx> ydoyxlist = ydoyxTableRepo.findAllByYDOYXPointData(station, time);
            return JsonResult.successResult(ydoyxlist);
        }
        else if("YC".equals(type)){
            List<ydoyc> ydoyclist = ydoycTableRepo.findAllByYDOYCPointData(station, time);
            return JsonResult.successResult(ydoyclist);
        }
        else{
            System.out.println("类型不对");
            return null;
        }
    }

    public JsonResult VersionInfoSer(String station,Date time,String type){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if("YX".equals(type)){
            List<CZYXTable> czyxlist = czYXTableRepo.findAllByCZYXPointData(station, time);
            for (CZYXTable a : czyxlist) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(czyxlist, Comparator.comparing(CZYXTable::getDot_int));
            return JsonResult.successResult(czyxlist);
        }
        else if("YC".equals(type)){
            List<CZYCTable> czyclist = czYCTableRepo.findAllByCZYCPointData(station, time);
            for (CZYCTable a : czyclist) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(czyclist, Comparator.comparing(CZYCTable::getDot_int));
            return JsonResult.successResult(czyclist);
        }
        else if("YK".equals(type)){
            List<CZYKTable> czyklist = czYKTableRepo.findAllByCZYKPointData(station, time);
            for (CZYKTable a : czyklist) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(czyklist, Comparator.comparing(CZYKTable::getDot_int));
            return JsonResult.successResult(czyklist);
        }
        else if("YT".equals(type)){
            List<CZYTTable> czytlist = czYTTableRepo.findAllByCZYTPointData(station, time);
            for (CZYTTable a : czytlist) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(czytlist, Comparator.comparing(CZYTTable::getDot_int));
            return JsonResult.successResult(czytlist);
        }
        else{
            System.out.println("类型不对");
            return JsonResult.failureResult("类型不对");
        }
    }

    public JsonResult homeData(){
        List<HomeData> homelist = homeDataRepo.findAll();
        for(HomeData home:homelist) {
            String a = getPercent(home.getYxgkonline(),home.getYxgktotal());
            home.setOnlinerate(a);
        }
        return JsonResult.successResult(homeDataRepo.findAll());
    }

    public String getPercent(int x, int y) {
        String result = "";
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        if (x == 0) {
            return "0";
        }
        result = numberFormat.format((float) x / (float) y * 100);
        return result;
    }

    public void SolveHomeData(Integer yxgktotal,Integer yxgkonline,Integer yxgkoffline,Integer yctjtotal,Integer yctjdefect){
        //System.out.println(yxgktotal+","+yxgkonline+","+yxgkoffline+","+yctjtotal+","+yctjdefect);
        List<HomeData> homelist = homeDataRepo.findAll();
        for(HomeData aa:homelist){
            aa.setYxgktotal(yxgktotal);
            aa.setYxgkonline(yxgkonline);
            aa.setYxgkoffline(yxgkoffline);
            aa.setYctjtotal(yctjtotal);
            aa.setYctjdefect(yctjdefect);
            homeDataRepo.save(aa);
        }
    }

    public JsonResult UAlertMessage(int page){
        PageRequest pageReques = PageRequest.of(page, 20);
        List<AlertMessageData> cc  = alertMessageDataRepo.findAllByUpAlertMessage(pageReques).stream().sorted(Comparator.comparing(AlertMessageData::getAlert_time)).collect(Collectors.toList());
        List<AlertMessageData> zz  = alertMessageDataRepo.findAllByAllAlertMessage();
        int sum = zz.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(AlertMessageData c:cc){
            String time = simpleDateFormat.format(c.getAlert_time());
            c.setAlert_time_string(time);
            c.setAllCount(sum);
            c.setHandle_logo("详情信息");
            if(c.getHandle_recommendation()!=null){
                c.setHandle_state("已处理");
                c.setHandle_logo("详情信息");
            }
            if(c.getHandle_recommendation()==null){
                c.setHandle_state("未处理");
                c.setHandle_logo("详情信息");
            }
        }
        return JsonResult.successResult(cc);
    }

    public JsonResult SelectAlertMessage(String station,Date time1,Date time2,int page){
        if(station.equals("null")){
            return null;
        }
        PageRequest pageReques = PageRequest.of(page, 20);
        List<AlertMessageData> all = alertMessageDataRepo.findAllByAllAlertMessage(station,time1,time2);
        int sum = all.size();
        List<AlertMessageData> cc  =alertMessageDataRepo.findAllByUpAlertMessage(pageReques,station,time1,time2).stream().sorted(Comparator.comparing(AlertMessageData::getAlert_time)).collect(Collectors.toList());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(AlertMessageData c:cc){
            String time = simpleDateFormat.format(c.getAlert_time());
            c.setAlert_time_string(time);
            c.setAllCount(sum);
            c.setHandle_logo("详情信息");
            if(c.getHandle_recommendation()!=null){
                c.setHandle_state("已处理");
                c.setHandle_logo("详情信息");
            }
            if(c.getHandle_recommendation()==null){
                c.setHandle_state("未处理");
                c.setHandle_logo("详情信息");
            }
        }
        return JsonResult.successResult(cc);
    }

    public void SolveAlertData(String station_name,String alert_name,Date alert_time,String alertMesssage,String handle_peo){
        AlertMessageData aa = alertMessageDataRepo.findByAlertMessage(station_name,alert_name,alert_time);
        aa.setHandle_recommendation(alertMesssage);
        if(handle_peo!=null){
            aa.setHandle_an(handle_peo);
        }
        aa.setHandle_state("已处理");
        alertMessageDataRepo.save(aa);
    }

    public JsonResult Selectt(String city){

        List<String> bdz = d5000yxRepo.findbdzAllList();
        List<String> fdc = d5000yxRepo.finddfcAllList();
        bdz.addAll(fdc);
        Map<String,String> CZList = new HashMap<>();

        for(String st : bdz){
            String[] xx = st.split(",");
            CZList.put(xx[0],xx[1]);
        }

        // 根据值(value)进行分类
        Map<String, List<String>> classifiedMap = CZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        List<String> stationAll = classifiedMap.get(city);

        List<Tree> bb = new ArrayList<>();
        for(String a:stationAll){
            Tree tree = new Tree();
            tree.setLabel(a);
            bb.add(tree);
        }
        return JsonResult.successResult(bb);
    }

    public JsonResult SelectCity(String station){
        List<String> aa = fdcAllTableRepo.findAllByFDCData(station);
        List<String> bb = bdzAllTableRepo.findAllByBDZData(station);
        aa.addAll(bb);
        return JsonResult.successResult(aa);
    }

    public JsonResult QueryAlert(){
        String aa = "未处理";
        int ss = alertMessageDataRepo.findAlertCount(aa);
        return JsonResult.successResult(ss);
    }

    public JsonResult QueryHomeData(){
        List<BDZtable> bdz = bdztableRepo.findAll();
        int bdzCount = bdz.size();
        List<BDZtable> Yycheck = bdz.stream().filter(item->item.getCheckstate()==1).collect(Collectors.toList());
        int Ycheck = Yycheck.size();
        int Wcheck = bdzCount-Ycheck;
        List<AlertMessageData> aa = alertMessageDataRepo.findAll();
        int qxian = aa.size();

        List<HomeData> hd = homeDataRepo.findAll();
        for(HomeData hh : hd){
            hh.setYxgktotal(bdzCount);
            hh.setYctjtotal(bdzCount);
            hh.setYxgkonline(Ycheck);
            hh.setYxgkoffline(Wcheck);
            hh.setYctjdefect(qxian);
            homeDataRepo.save(hh);
        }
        return JsonResult.successResult(bdzCount);
    }

    public JsonResult getCheckRecords(){
        List<Conr> cr = conrRepo.findCheckRecords().stream().limit(5).collect(Collectors.toList());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Conr c:cr){
            c.setStringtime(simpleDateFormat.format(c.getTtime()));
        }
        return JsonResult.successResult(cr);
    }

    public JsonResult getXydAllRecord(){
        List<Xyd_Ac_Records> xx=xyd_ac_recordsRepo.findAll();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Xyd_Ac_Records x:xx){
            x.setAAc_time(simpleDateFormat.format(x.getAc_time()));
        }

        return JsonResult.successResult(xx);
    }

    public JsonResult getydoYXData(String station,String time){
        if("".equals(time)){
            time = null;
        }
//        List<ydoyx> yxoyx = new ArrayList<>();
        List<CZYXTable> yxoyx = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            if (time != null) {
                date = simpleDateFormat.parse(time);
                yxoyx = czYXTableRepo.findAllByCZYXPointData(station, date);
            } else {
                Date newtime = czYXTableRepo.findBymaxTime(station);
                yxoyx = czYXTableRepo.findAllByCZYXPointData(station, newtime);
            }
            List<CZYXTable> yxoyx_new = yxoyx.stream().collect(Collectors.toMap(
                    person -> person.getDot_no() + "-" + person.getTtime(),
                    p -> p,
                    (p1, p2) -> p1))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            for (CZYXTable a : yxoyx_new) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(yxoyx_new, Comparator.comparing(CZYXTable::getDot_int));
            return JsonResult.successResult(yxoyx_new);
        }
        catch (ParseException e){
            e.printStackTrace();
            return JsonResult.failureResult(e.getMessage());
        }
    }

    public JsonResult getydoYCData(String station,String time){
        if("".equals(time)){
            time = null;
        }
        List<CZYCTable> ydoyc = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            if (time != null) {
                date = simpleDateFormat.parse(time);
                ydoyc = czYCTableRepo.findAllByCZYCPointData(station, date);
            } else {
                Date newtime = czYCTableRepo.findBymaxTime(station);
                ydoyc = czYCTableRepo.findAllByCZYCPointData(station, newtime);
            }
            List<CZYCTable> ydoyc_new = ydoyc.stream().collect(Collectors.toMap(
                    person -> person.getDot_no() + "-" + person.getTtime(),
                    p -> p,
                    (p1, p2) -> p1))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            for (CZYCTable a : ydoyc_new) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(ydoyc_new, Comparator.comparing(com.example.demo.entity.CZYCTable::getDot_int));
            return JsonResult.successResult(ydoyc_new);
        }
        catch (ParseException e){
            e.printStackTrace();
            return JsonResult.failureResult(e.getMessage());
        }
    }

    public JsonResult getydoYKData(String station,String time){
        if("".equals(time)){
            time = null;
        }
        List<CZYKTable> ydoykk = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            if (time != null) {
                date = simpleDateFormat.parse(time);
                ydoykk = czYKTableRepo.findAllByCZYKPointData(station, date);
            } else {
                Date newtime = czYKTableRepo.findBymaxTime(station);
                ydoykk = czYKTableRepo.findAllByCZYKPointData(station, newtime);
            }
            List<CZYKTable> ydoyk_new = ydoykk.stream().collect(Collectors.toMap(
                    person -> person.getDot_no() + "-" + person.getTtime(),
                    p -> p,
                    (p1, p2) -> p1))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            for (CZYKTable a : ydoyk_new) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(ydoyk_new, Comparator.comparing(CZYKTable::getDot_int));
            return JsonResult.successResult(ydoyk_new);
        }
        catch (ParseException e){
            e.printStackTrace();
            return JsonResult.failureResult(e.getMessage());
        }
    }

    public JsonResult getydoYTData(String station,String time){
        if("".equals(time)){
            time = null;
        }
        List<CZYTTable> ydoytt = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            if (time != null) {
                date = simpleDateFormat.parse(time);
                ydoytt = czYTTableRepo.findAllByCZYTPointData(station, date);
            } else {
                Date newtime = czYTTableRepo.findBymaxTime(station);
                ydoytt = czYTTableRepo.findAllByCZYTPointData(station, newtime);
            }
            List<CZYTTable> ydoyt_new = ydoytt.stream().collect(Collectors.toMap(
                    person -> person.getDot_no() + "-" + person.getTtime(),
                    p -> p,
                    (p1, p2) -> p1))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            for (CZYTTable a : ydoyt_new) {
                String ttime = simpleDateFormat.format(a.getTtime());
                a.setTttime(ttime);
                int count = Integer.valueOf(a.getDot_no());
                a.setDot_int(count);
            }
            Collections.sort(ydoyt_new, Comparator.comparing(CZYTTable::getDot_int));
            return JsonResult.successResult(ydoyt_new);
        }
        catch (ParseException e){
            e.printStackTrace();
            return JsonResult.failureResult(e.getMessage());
        }
    }

    public JsonResult getydtYXData(String station){
        List<ydtyx> yxtyx = ydtyxTableRepo.findAllByYDTYXData(station);

        return JsonResult.successResult(yxtyx);
    }

    public JsonResult getstationCheckRecords(String station){
        List<Conr> crList = conrRepo.findStationCheckRecords(station).stream().limit(1).collect(Collectors.toList());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Conr c : crList){
            c.setStringtime(simpleDateFormat.format(c.getTtime()));
        }
        return JsonResult.successResult(crList);
    }

    public JsonResult getFileCheckRecords(String station){
        List<FileComRecords> crList = fileComRecordsRepo.findFileCheckRecords(station).stream().limit(1).collect(Collectors.toList());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(FileComRecords c : crList){
            c.setTimestring(simpleDateFormat.format(c.getTtime()));
        }
        return JsonResult.successResult(crList);
    }

    public JsonResult getFileCheck(String station){
        List<FileCom> fileCom = fileComRepo.findFileCheckResult(station);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(FileCom c : fileCom){
            c.setTimestring(simpleDateFormat.format(c.getTtime()));
        }
        return JsonResult.successResult(fileCom);
    }

//    public Workbook exportToExcel(String station,String time) throws ParseException{
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = simpleDateFormat.parse(time);
//
//        List<D5000yx> aa = d5000yxRepo.findAllByStationAndTime(station,date);
//
//        //这是表头
//        //String[] arr = {"名称","年龄"};
//        String[] arr = {"序号","名称","点号","厂站","日期"};
//        //这是具体数据
//        List<List> list = new ArrayList<>();
//        int count=0;
//        for(D5000yx yxd : aa){
//            List listD = new ArrayList<>();
//            listD.add(count);
//            count++;
//            listD.add(yxd.getYx_name());
//            listD.add(yxd.getDot_no());
//            listD.add(yxd.getSt_name());
//            listD.add(simpleDateFormat.format(yxd.getTtime()));
//            list.add(listD);
//        }
//        return writeToExcelByList(arr, list);
//    }
//    public static Workbook writeToExcelByList(String[] array, List<List> list) {
//        //创建工作薄
//        Workbook wb = new XSSFWorkbook();
//        //标题和页码
//        CellStyle titleStyle = wb.createCellStyle();
//        // 设置单元格对齐方式,水平居左
//        titleStyle.setAlignment(HorizontalAlignment.LEFT);
//        // 设置字体样式
//        Font titleFont = wb.createFont();
//        // 字体高度
//        titleFont.setFontHeightInPoints((short) 12);
//        // 字体样式
//        titleFont.setFontName("黑体");
//        titleStyle.setFont(titleFont);
//        //创建sheet
//        Sheet sheet = wb.createSheet("遥信(D5000)");
//        Sheet sheet1 = wb.createSheet("遥测(D5000)");
//        // 自动设置宽度
//        sheet.autoSizeColumn(0);
//        // 在sheet中添加标题行// 行数从0开始
//        Row row = sheet.createRow((int) 0);
//        for (int i = 0; i < array.length; i++) {
//            Cell cell = row.createCell(i);
//            cell.setCellValue(array[i]);
//            cell.setCellStyle(titleStyle);
//        }
//        // 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
//        CellStyle dataStyle = wb.createCellStyle();
//        // 设置居中样式，水平居中
//        dataStyle.setAlignment(HorizontalAlignment.CENTER);
//        //数据从序号1开始
//        try {
//            int index = 1;
//            for (List value : list) {
//                // 默认的行数从0开始，为了统一格式设置从1开始，就是从excel的第二行开始
//                row = sheet.createRow(index);
//                index++;
//                List data = value;
//                for (int j = 0; j < data.size(); j++) {
//                    Cell cell = row.createCell(j);
//                    // 为当前列赋值
//                    cell.setCellValue(data.get(j).toString());
//                    //设置数据的样式
//                    cell.setCellStyle(dataStyle);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return wb;
//    }
    public Workbook writeToExcelByList(String station,String time) throws ParseException{
        String[] array = {"序号","名称","点号","厂站","日期"};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);

//        List<D5000yx> aa = d5000yxRepo.findAllByStationAndTime(station,date);
//        List<D5000yc> bb = d5000ycRepo.findAllByStationAndTime(station,date);

        List<CZYXTable> czYx = czYXTableRepo.findAllByCZYXPointData(station,date);
        List<CZYCTable> czYc = czYCTableRepo.findAllByCZYCPointData(station,date);
        List<CZYKTable> czYk = czYKTableRepo.findAllByCZYKPointData(station,date);

//        List<ydoyx> cc = ydoyxTableRepo.findAllByYDOYXPointData(station,date);
//        List<ydoyc> dd = ydoycTableRepo.findAllByYDOYCPointData(station,date);
//        List<ydoyk> ee = ydoykTableRepo.findAllByYDOYKPointData(station,date);
        //这是表头
        //这是具体数据
        List<List> list = new ArrayList<>();
        List<List> yx_cz = new ArrayList<>();
        List<List> yc_cz = new ArrayList<>();
        List<List> yk_cz = new ArrayList<>();
//        List<List> yx_yd = new ArrayList<>();
//        List<List> yc_yd = new ArrayList<>();
//        List<List> yk_yd = new ArrayList<>();

        int count=0;
        for(CZYXTable yxd : czYx){
            List listD = new ArrayList<>();
            listD.add(count);
            count++;
            listD.add(yxd.getYxname());
//            listD.add(yxd.getDot_no());
            listD.add(Integer.valueOf(yxd.getDot_no()));
            listD.add(yxd.getSt_name());
            listD.add(simpleDateFormat.format(yxd.getTtime()));
            yx_cz.add(listD);
        }
        int count1=0;
        for(CZYCTable ycd : czYc){
            List listyc = new ArrayList<>();
            listyc.add(count1);
            count1++;
            listyc.add(ycd.getYcname());
            //listyc.add(ycd.getDot_no());
            listyc.add(Integer.valueOf(ycd.getDot_no()));
            listyc.add(ycd.getSt_name());
            listyc.add(simpleDateFormat.format(ycd.getTtime()));
            yc_cz.add(listyc);
        }

        int count2=0;
        for(CZYKTable ycd : czYk){
            List listyc = new ArrayList<>();
            listyc.add(count2);
            count2++;
            listyc.add(ycd.getYkname());
            //listyc.add(ycd.getDot_no());
            listyc.add(Integer.valueOf(ycd.getDot_no()));
            listyc.add(ycd.getSt_name());
            listyc.add(simpleDateFormat.format(ycd.getTtime()));
            yk_cz.add(listyc);
        }
//        int count_yd_yx=0;
//        for(ydoyx ycd : cc){
//            List listydyx = new ArrayList<>();
//            listydyx.add(count_yd_yx);
//            count_yd_yx++;
//            listydyx.add(ycd.getYxname());
//            //listydyx.add(ycd.getDot_no());
//            listydyx.add(Integer.valueOf(ycd.getDot_no()));
//            listydyx.add(ycd.getSt_name());
//            listydyx.add(simpleDateFormat.format(ycd.getTtime()));
//            yx_yd.add(listydyx);
//        }
//        int count_yd_yc=0;
//        for(ydoyc ycd : dd){
//            List listydyc = new ArrayList<>();
//            listydyc.add(count_yd_yc);
//            count_yd_yx++;
//            listydyc.add(ycd.getYcname());
//            //listydyc.add(ycd.getDot_no());
//            listydyc.add(Integer.valueOf(ycd.getDot_no()));
//            listydyc.add(ycd.getSt_name());
//            listydyc.add(simpleDateFormat.format(ycd.getTtime()));
//            yc_yd.add(listydyc);
//        }
//        int count_yd_yk=0;
//        for(ydoyk ycd : ee){
//            List listydyk = new ArrayList<>();
//            listydyk.add(count_yd_yk);
//            count_yd_yx++;
//            listydyk.add(ycd.getYkname());
//           // listydyk.add(ycd.getDot_no());
//            listydyk.add(Integer.valueOf(ycd.getDot_no()));
//            listydyk.add(ycd.getSt_name());
//            listydyk.add(simpleDateFormat.format(ycd.getTtime()));
//            yk_yd.add(listydyk);
//        }
        //创建工作薄
        Workbook wb = new XSSFWorkbook();
        //标题和页码
        CellStyle titleStyle = wb.createCellStyle();
        // 设置单元格对齐方式,水平居左
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        // 设置字体样式
        Font titleFont = wb.createFont();
        // 字体高度
        titleFont.setFontHeightInPoints((short) 12);
        // 字体样式
        titleFont.setFontName("黑体");
        titleStyle.setFont(titleFont);
        Sheet sheet;
        for(int z=0;z<3;z++){
            //创建sheet
            if(z==0){
                sheet = wb.createSheet("遥信");
                list.clear();
                list.addAll(yx_cz);
            }
            else if(z==1){
                sheet = wb.createSheet("遥测");
                list.clear();
                list.addAll(yc_cz);
            }
            else if(z==2){
                sheet = wb.createSheet("遥控");
                list.clear();
                list.addAll(yk_cz);
            }
//            else if(z==3){
//                sheet = wb.createSheet("遥信(远动)");
//                list.clear();
//                list.addAll(yx_yd);
//            }
//            else if(z==4){
//                sheet = wb.createSheet("遥测(远动)");
//                list.clear();
//                list.addAll(yc_yd);
//            }
//            else if(z==5){
//                sheet = wb.createSheet("遥控(远动)");
//                list.clear();
//                list.addAll(yk_yd);
//            }
            else {
                sheet = wb.createSheet("出错");
            }
            // 自动设置宽度
//            sheet.autoSizeColumn(0);
            // 在sheet中添加标题行// 行数从0开始
            Row row = sheet.createRow((int) 0);
            for (int i = 0; i < array.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(array[i]);
                cell.setCellStyle(titleStyle);
            }
            // 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
            CellStyle dataStyle = wb.createCellStyle();
            // 设置居中样式，水平居中
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            //数据从序号1开始
            try {
                int index = 1;
                for (List value : list) {
                    // 默认的行数从0开始，为了统一格式设置从1开始，就是从excel的第二行开始
                    row = sheet.createRow(index);
                    index++;
                    List data = value;
                    for (int j = 0; j < data.size(); j++) {
                        Cell cell = row.createCell(j);
                        // 为当前列赋值
                        cell.setCellValue(data.get(j).toString());
                        //设置数据的样式
                        cell.setCellStyle(dataStyle);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wb;
    }

    public Workbook writeToExcelXYDByList(String station) throws ParseException{
        String[] array = {"序号","名称","点号","厂站","日期"};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newtime = czYXTableRepo.findBymaxTime(station);

//        List<D5000yx> aa = d5000yxRepo.findAllByYXPointResult(station);
//        List<D5000yc> bb = d5000ycRepo.findAllByYCPointResult(station);
//        List<D5000yk> cc = d5000ykRepo.findAllByYKPointResult(station);

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

        List<D5000yc> D5000yc_new = new ArrayList<>();
        List<D5000yc> bbb=d5000ycRepo.findAllByYCPointResult(station);
        D5000yc_new = bbb.stream().collect(Collectors.toMap(
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

        List<CZYXTable> czYx = czYXTableRepo.findAllByCZYXPointData(station,newtime);
        List<CZYCTable> czYc = czYCTableRepo.findAllByCZYCPointData(station,newtime);
        List<CZYKTable> czYk = czYKTableRepo.findAllByCZYKPointData(station,newtime);

        //这是表头
        //这是具体数据
        List<List> list = new ArrayList<>();
        List<List> yx_cz = new ArrayList<>();
        List<List> yc_cz = new ArrayList<>();
        List<List> yk_cz = new ArrayList<>();

        List<List> yx_xyd = new ArrayList<>();
        List<List> yc_xyd = new ArrayList<>();
        List<List> yk_xyd = new ArrayList<>();

        int count=1;
        for(CZYXTable yxd : czYx){
            List listD = new ArrayList<>();
            listD.add(count);
            count++;
            listD.add(yxd.getYxname());
//            listD.add(yxd.getDot_no());
            listD.add(Integer.valueOf(yxd.getDot_no()));
            listD.add(yxd.getSt_name());
            listD.add(simpleDateFormat.format(yxd.getTtime()));
            yx_cz.add(listD);
        }
        int count1=1;
        for(CZYCTable ycd : czYc){
            List listyc = new ArrayList<>();
            listyc.add(count1);
            count1++;
            listyc.add(ycd.getYcname());
            //listyc.add(ycd.getDot_no());
            listyc.add(Integer.valueOf(ycd.getDot_no()));
            listyc.add(ycd.getSt_name());
            listyc.add(simpleDateFormat.format(ycd.getTtime()));
            yc_cz.add(listyc);
        }

        int count2=1;
        for(CZYKTable ycd : czYk){
            List listyc = new ArrayList<>();
            listyc.add(count2);
            count2++;
            listyc.add(ycd.getYkname());
            //listyc.add(ycd.getDot_no());
            listyc.add(Integer.valueOf(ycd.getDot_no()));
            listyc.add(ycd.getSt_name());
            listyc.add(simpleDateFormat.format(ycd.getTtime()));
            yk_cz.add(listyc);
        }

        int count_xyd_yx=1;
        for(D5000yx ycd : D5000yx_new){
            List listydyx = new ArrayList<>();
            listydyx.add(count_xyd_yx);
            count_xyd_yx++;
            listydyx.add(ycd.getYx_name());
            listydyx.add(Integer.valueOf(ycd.getDot_no()));
            listydyx.add(ycd.getSt_name());
            listydyx.add(simpleDateFormat.format(ycd.getTtime()));
            yx_xyd.add(listydyx);
        }
        int count_xyd_yc=1;
        for(D5000yc ycd : D5000yc_new){
            List listydyx = new ArrayList<>();
            listydyx.add(count_xyd_yc);
            count_xyd_yc++;
            listydyx.add(ycd.getYc_name());
            listydyx.add(Integer.valueOf(ycd.getDot_no()));
            listydyx.add(ycd.getSt_name());
            listydyx.add(simpleDateFormat.format(ycd.getTtime()));
            yc_xyd.add(listydyx);
        }
        int count_xyd_yk=1;
        for(D5000yk ycd : D5000yk_new){
            List listydyx = new ArrayList<>();
            listydyx.add(count_xyd_yk);
            count_xyd_yk++;
            listydyx.add(ycd.getYk_name());
            listydyx.add(Integer.valueOf(ycd.getDot_no()));
            listydyx.add(ycd.getSt_name());
            listydyx.add(simpleDateFormat.format(ycd.getTtime()));
            yk_xyd.add(listydyx);
        }

        //创建工作薄
        Workbook wb = new XSSFWorkbook();
        //标题和页码
        CellStyle titleStyle = wb.createCellStyle();
        // 设置单元格对齐方式,水平居左
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        // 设置字体样式
        Font titleFont = wb.createFont();
        // 字体高度
        titleFont.setFontHeightInPoints((short) 12);
        // 字体样式
        titleFont.setFontName("黑体");
        titleStyle.setFont(titleFont);
        Sheet sheet;
        for(int z=0;z<6;z++){
            //创建sheet
            if(z==0){
                sheet = wb.createSheet("遥信(厂站)");
                list.clear();
                list.addAll(yx_cz);
            }
            else if(z==1){
                sheet = wb.createSheet("遥测(厂站)");
                list.clear();
                list.addAll(yc_cz);
            }
            else if(z==2){
                sheet = wb.createSheet("遥控(厂站)");
                list.clear();
                list.addAll(yk_cz);
            }
            else if(z==3){
                sheet = wb.createSheet("遥信(新一代)");
                list.clear();
                list.addAll(yx_xyd);
            }
            else if(z==4){
                sheet = wb.createSheet("遥测(新一代)");
                list.clear();
                list.addAll(yc_xyd);
            }
            else if(z==5){
                sheet = wb.createSheet("遥控(新一代)");
                list.clear();
                list.addAll(yk_xyd);
            }
            else {
                sheet = wb.createSheet("出错");
            }
            // 自动设置宽度
//            sheet.autoSizeColumn(0);
            // 在sheet中添加标题行// 行数从0开始
            Row row = sheet.createRow((int) 0);
            for (int i = 0; i < array.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(array[i]);
                cell.setCellStyle(titleStyle);
            }
            // 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
            CellStyle dataStyle = wb.createCellStyle();
            // 设置居中样式，水平居中
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            //数据从序号1开始
            try {
                int index = 1;
                for (List value : list) {
                    // 默认的行数从0开始，为了统一格式设置从1开始，就是从excel的第二行开始
                    row = sheet.createRow(index);
                    index++;
                    List data = value;
                    for (int j = 0; j < data.size(); j++) {
                        Cell cell = row.createCell(j);
                        // 为当前列赋值
                        cell.setCellValue(data.get(j).toString());
                        //设置数据的样式
                        cell.setCellStyle(dataStyle);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wb;
    }

    public JsonResult getStationAllTree_New(){

        List<FDCAllTable> fdcAll = d5000yxRepo.finddfcAllList_New();
        Map<String, List<FDCAllTable>> groups = fdcAll.stream()
                .collect(Collectors.groupingBy(FDCAllTable::getLevel));

        List<BDZAllTable> bdzAll = d5000yxRepo.findbdzAllList_New();

        List<Tree> TreeList = new ArrayList<>();
        Tree maintree = new Tree();
        maintree.setLabel("发电厂");

        List<FDCAllTable> fdc_6kV = groups.get("6kV");
        List<FDCAllTable> fdc_10kV = groups.get("10kV");
        List<FDCAllTable> fdc_35kV = groups.get("35kV");
        List<FDCAllTable> fdc_110kV = groups.get("110kV");
        List<FDCAllTable> fdc_220kV = groups.get("220kV");
        List<FDCAllTable> fdc_500kV = groups.get("500kV");

//        Map<String, List<FDCAllTable>> group6kV = fdc_6kV.stream()
//                .collect(Collectors.groupingBy(FDCAllTable::getType));
//        Map<String, List<FDCAllTable>> group10kV = fdc_10kV.stream()
//                .collect(Collectors.groupingBy(FDCAllTable::getType));
//        Map<String, List<FDCAllTable>> group35kV = fdc_35kV.stream()
//                .collect(Collectors.groupingBy(FDCAllTable::getType));
//        Map<String, List<FDCAllTable>> group110kV = fdc_110kV.stream()
//                .collect(Collectors.groupingBy(FDCAllTable::getType));
//        Map<String, List<FDCAllTable>> group220kV = fdc_220kV.stream()
//                .collect(Collectors.groupingBy(FDCAllTable::getType));
//        Map<String, List<FDCAllTable>> group550kV = fdc_500kV.stream()
//                .collect(Collectors.groupingBy(FDCAllTable::getType));

        Tree fdc_tree6kV = new Tree();
        Tree fdc_tree10kV = new Tree();
        Tree fdc_tree35kV = new Tree();
        Tree fdc_tree110kV = new Tree();
        Tree fdc_tree220kV = new Tree();
        Tree fdc_tree500kV = new Tree();

        List<Tree> fdc_6AllTree = new ArrayList<>();
        for(FDCAllTable xx:fdc_6kV){
            Tree abc = new Tree();
            abc.setLabel(xx.getStation_name());
            fdc_6AllTree.add(abc);
        }
        fdc_tree6kV.setLabel("6kV");
        fdc_tree6kV.setChildren(fdc_6AllTree);

        List<Tree> fdc_10AllTree = new ArrayList<>();
        for(FDCAllTable xx:fdc_10kV){
            Tree abc = new Tree();
            abc.setLabel(xx.getStation_name());
            fdc_10AllTree.add(abc);
        }
        fdc_tree6kV.setLabel("10kV");
        fdc_tree6kV.setChildren(fdc_10AllTree);

        List<Tree> fdc_35AllTree = new ArrayList<>();
        for(FDCAllTable xx:fdc_35kV){
            Tree abc = new Tree();
            abc.setLabel(xx.getStation_name());
            fdc_35AllTree.add(abc);
        }
        fdc_tree6kV.setLabel("35kV");
        fdc_tree6kV.setChildren(fdc_35AllTree);

        List<Tree> fdc_110AllTree = new ArrayList<>();
        for(FDCAllTable xx:fdc_110kV){
            Tree abc = new Tree();
            abc.setLabel(xx.getStation_name());
            fdc_110AllTree.add(abc);
        }
        fdc_tree6kV.setLabel("110kV");
        fdc_tree6kV.setChildren(fdc_110AllTree);

        List<Tree> fdc_220AllTree = new ArrayList<>();
        for(FDCAllTable xx:fdc_220kV){
            Tree abc = new Tree();
            abc.setLabel(xx.getStation_name());
            fdc_220AllTree.add(abc);
        }
        fdc_tree6kV.setLabel("220kV");
        fdc_tree6kV.setChildren(fdc_220AllTree);

        List<Tree> fdc_500AllTree = new ArrayList<>();
        for(FDCAllTable xx:fdc_500kV){
            Tree abc = new Tree();
            abc.setLabel(xx.getStation_name());
            fdc_500AllTree.add(abc);
        }
        fdc_tree6kV.setLabel("500kV");
        fdc_tree6kV.setChildren(fdc_500AllTree);

        Tree bdz_tree10kV = new Tree();
        Tree bdz_tree35kV = new Tree();
        Tree bdz_tree110kV = new Tree();
        Tree bdz_tree220kV = new Tree();
        Tree bdz_tree500kV = new Tree();
        Tree bdz_tree1000kV = new Tree();

        List<Tree> fdcAllList = new ArrayList<>();


        for(FDCAllTable fdc: fdcAll){

        }


        return JsonResult.successResult(TreeList);
    }

    public JsonResult getStationAllList(){
        List<String>xxx = d5000yxRepo.findStatonAllList();
        List<Select> bb = new ArrayList<>();
        for(String a:xxx){
            Select tree = new Select();
            tree.setLabel(a);
            tree.setValue(a);
            bb.add(tree);
        }
        return JsonResult.successResult(bb);
    }

    public JsonResult getRedisAllTree(){
        List<Tree> value123 =  (List<Tree>) redisTemplate.opsForValue().get("stationAllTree");
        return JsonResult.successResult(value123);
    }

    public JsonResult getStationAllTree_NNew(){

        List<String> bdz = d5000yxRepo.findbdzAllList();
        List<String> fdc = d5000yxRepo.finddfcAllList();

        Map<String,String> CZList = new HashMap<>();
        Map<String,String> FDZList = new HashMap<>();


        for(String st : bdz){
            String[] xx = st.split(",");
            CZList.put(xx[0],xx[3]);
        }
        for(String st : fdc){
            String[] xx = st.split(",");
            FDZList.put(xx[0],xx[3]);
        }

        // 根据值(value)进行分类
        Map<String, List<String>> classifiedMap = CZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        Map<String, List<String>> fdcclassifiedMap = FDZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        Set<String> CityAll = classifiedMap.keySet();
        Set<String> fdcCityAll = fdcclassifiedMap.keySet();

        List<Tree> FinallTreeList = new ArrayList<>();
        List<Tree> TreeList = new ArrayList<>();
        List<Tree> FDCTreeList = new ArrayList<>();

        for(String city:CityAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<String> treetable = classifiedMap.get(city);
            for(String n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n);
                List<Tree> tilist = new ArrayList<>();
                tree1.setChildren(tilist);
                stationall.add(tree1);
            }
            tree.setLabel(city);
            tree.setChildren(stationall);
            TreeList.add(tree);
        }
        for(String city:fdcCityAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<String> treetable = fdcclassifiedMap.get(city);
            for(String n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n);
                List<Tree> tilist = new ArrayList<>();
                tree1.setChildren(tilist);
                stationall.add(tree1);
            }
            tree.setLabel(city);
            tree.setChildren(stationall);
            FDCTreeList.add(tree);
        }
        Tree fdctree = new Tree();
        fdctree.setLabel("发电厂");
        fdctree.setChildren(FDCTreeList);
        Tree bdztree = new Tree();
        bdztree.setLabel("变电站");
        bdztree.setChildren(TreeList);
        FinallTreeList.add(fdctree);
        FinallTreeList.add(bdztree);

        redisTemplate.opsForValue().set("stationAllTree", FinallTreeList);

        return JsonResult.successResult(FinallTreeList);
    }

    public JsonResult getStationAllTree(){

        List<String> bdz = d5000yxRepo.findbdzAllList();
        List<String> fdc = d5000yxRepo.finddfcAllList();
        bdz.addAll(fdc);
        Map<String,String> CZList = new HashMap<>();

        for(String st : bdz){
            String[] xx = st.split(",");
            CZList.put(xx[0],xx[1]);
        }

        // 根据值(value)进行分类
        Map<String, List<String>> classifiedMap = CZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        LinkedHashMap<String, List<String>> sortedMap = classifiedMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<String>>comparingByKey((key1, key2) -> {
                    boolean key1Contains = key1.contains("江苏");
                    boolean key2Contains = key2.contains("江苏");

                    if (key1Contains == key2Contains) {
                        return key1.compareTo(key2);
                    } else if (key1Contains) {
                        return -1;
                    } else {
                        return 1;
                    }
                }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Set<String> CityAll = sortedMap.keySet();
        List<Tree> TreeList = new ArrayList<>();

        for(String city:CityAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<String> treetable = sortedMap.get(city);
            for(String n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n);
                List<Tree> tilist = new ArrayList<>();
                tree1.setChildren(tilist);
                stationall.add(tree1);
            }
            tree.setLabel(city);
            tree.setChildren(stationall);
            TreeList.add(tree);
        }

        return JsonResult.successResult(TreeList);
    }

    public JsonResult getSelectStation(){

        List<String> bdz = d5000yxRepo.findbdzAllList();
        List<String> fdc = d5000yxRepo.finddfcAllList();
        bdz.addAll(fdc);
        Map<String,String> CZList = new HashMap<>();

        for(String st : bdz){
            String[] xx = st.split(",");
            CZList.put(xx[0],xx[1]);
        }

        // 根据值(value)进行分类
        Map<String, List<String>> classifiedMap = CZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        LinkedHashMap<String, List<String>> sortedMap = classifiedMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<String>>comparingByKey((key1, key2) -> {
                    boolean key1Contains = key1.contains("江苏");
                    boolean key2Contains = key2.contains("江苏");

                    if (key1Contains == key2Contains) {
                        return key1.compareTo(key2);
                    } else if (key1Contains) {
                        return -1;
                    } else {
                        return 1;
                    }
                }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Set<String> CityAll = sortedMap.keySet();
        List<Select> bb = new ArrayList<>();
        for(String aa : CityAll){
            Select xx = new Select();
            xx.setLabel(aa);
            xx.setValue(aa);
            bb.add(xx);
        }
        return JsonResult.successResult(bb);
    }

    public JsonResult getVersionAllTree(){

        List<String> bdz = czYXTableRepo.findbdzAllList();
        List<String> fdc = czYXTableRepo.finddfcAllList();
        bdz.addAll(fdc);
        Map<String,String> CZList = new HashMap<>();

        for(String st : bdz){
            String[] xx = st.split(",");
            CZList.put(xx[0],xx[1]);
        }

        // 根据值(value)进行分类
        Map<String, List<String>> classifiedMap = CZList.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        LinkedHashMap<String, List<String>> sortedMap = classifiedMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<String>>comparingByKey((key1, key2) -> {
                    boolean key1Contains = key1.contains("江苏");
                    boolean key2Contains = key2.contains("江苏");

                    if (key1Contains == key2Contains) {
                        return key1.compareTo(key2);
                    } else if (key1Contains) {
                        return -1;
                    } else {
                        return 1;
                    }
                }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Set<String> CityAll = sortedMap.keySet();
        List<Tree> TreeList = new ArrayList<>();

        for(String city:CityAll){
            List<Tree> stationall = new ArrayList<>();
            Tree tree = new Tree();
            List<String> treetable = sortedMap.get(city);
            for(String n:treetable){
                Tree tree1 = new Tree();
                tree1.setLabel(n);
                List<Tree> tilist = new ArrayList<>();
                tree1.setChildren(tilist);
                stationall.add(tree1);
            }
            tree.setLabel(city);
            tree.setChildren(stationall);
            TreeList.add(tree);
        }

        return JsonResult.successResult(TreeList);
    }

    public JsonResult getRedis(){
        List<Tree> value123 =  (List<Tree>) redisTemplate.opsForValue().get("stationAllTree");
        return JsonResult.successResult(value123);
    }

    public JsonResult getAcStation(Date date){

        List<Xyd_Station_Records> stA =  xyd_station_recordsRepo.findByAcStationRecords(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Xyd_Station_Records a:stA){
            a.setSt_time(dateFormat.format(a.getAc_time()));
        }

        return JsonResult.successResult(stA);
    }

    public List<CimeFile> getRemoteFileList() {
        return FileMapper.selectList();
    }

    public void uploadExcel(MultipartFile file, String name, String cimePath) throws IOException {
        String Path = cimePath.replace("\\", "\\\\");
        // 检查并创建目标目录
        File directory = new File(Path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + Path);
            }
        }

        // 获取文件后缀名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 拼接目标文件路径
        String targetFilename = name + fileExtension;
        File targetFile = new File(directory, targetFilename);

        // 保存文件到目标路径
        file.transferTo(targetFile);
        CimeFile cimeFile = new CimeFile();
        cimeFile.setFileName(targetFilename);
        cimeFile.setPath(cimePath);
        cimeFile.setCreateTime(DateTime.now().toString());
        FileMapper.insert(cimeFile);
    }

    public void getLocalFileList(String directoryPath, List<CimeFile> fileList) {
        Path path = Paths.get(directoryPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                    String fileName = entry.getFileName().toString();
                    Instant lastModifiedInstant = attrs.lastModifiedTime().toInstant();
                    LocalDateTime lastModifiedTime = LocalDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault());
                    String formattedLastModifiedTime = lastModifiedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    CimeFile cimeFile = new CimeFile();
                    cimeFile.setFileName(fileName);
                    cimeFile.setCreateTime(formattedLastModifiedTime);
                    cimeFile.setPath(directoryPath);
                    fileList.add(cimeFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonResult getTableDataByVoltage(String stationType, String voltage) {
        List<String> stationInfo = null;
        if (stationType.equals("发电厂"))
            stationInfo = d5000yxRepo.finddfcAllList();
        else if (stationType.equals("变电站")) {
            stationInfo = d5000yxRepo.findbdzAllList();
        }
        if (voltage != null) {
            if (stationInfo != null) {
                stationInfo.removeIf(info -> !info.split(",")[3].equals(voltage));
            }
        }
        HashSet<String> hashSet = new HashSet<>();
        if (stationInfo != null) {
            for (String info : stationInfo) {
                String[] infos = info.split(",");
                hashSet.add(infos[0]);
            }
        }
        Integer stationCount = stationInfo.size();
        List<String> checkInfo = checkedRepo.findAllByStationResult();
        Integer checkCount = 0;
        for (String info : checkInfo) {
            if (hashSet.contains(info))
                checkCount++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String checkRate = "0%";
        if (stationCount != 0)
            checkRate = df.format(((double) checkCount / (double) stationCount) * 100.000) + '%';
        List<String> alertInfo = alertMessageDataRepo.findAllBySAlertResult();
        Set<String> set = new HashSet<>(alertInfo);
        alertInfo = new ArrayList<>(set);
        Integer alertCount = 0;
        for (String info : alertInfo) {
            if (hashSet.contains(info))
                alertCount++;
        }
        String alertRate = "0%";
        if (stationCount != 0)
            alertRate = df.format(((double) alertCount / (double) stationCount) * 100.000) + '%';
        TableData tableData = TableData.builder()
                .stationType(stationType)
                .stationCount(stationCount)
                .checkCount(checkCount)
                .checkRate(checkRate)
                .faultCount(alertCount)
                .faultRate(alertRate)
                .build();
        if (voltage != null)
            tableData.setVoltage(voltage);
        List<TableData> list = new ArrayList<>();
        list.add(tableData);
        return JsonResult.successResult(list);
    }

    public JsonResult getTableDataByName(String stationType, String voltage, String stationName) {
        Boolean check = false;
        List<String> allByStationResult = checkedRepo.findAllByStationResult();
        for (String s : allByStationResult) {
            if (s.equals(stationName)) {
                check = true;
                break;
            }
        }
        Boolean fault = false;
        List<String> alertInfo = alertMessageDataRepo.findAllBySAlertResult();
        Set<String> set = new HashSet<>(alertInfo);
        alertInfo = new ArrayList<>(set);
        for (String s : alertInfo) {
            if (s.equals(stationName)) {
                fault = true;
                break;
            }
        }
        TableData tableData = TableData.builder()
                .stationType(stationType)
                .stationName(stationName)
                .voltage(voltage)
                .check(check)
                .fault(fault)
                .build();
        List<TableData> list = new ArrayList<>();
        list.add(tableData);
        return JsonResult.successResult(list);
    }

    public JsonResult getTableData() {
        List<String> stationInfo = d5000yxRepo.finddfcAllList();
        stationInfo.addAll(d5000yxRepo.findbdzAllList());
        HashSet<String> hashSet = new HashSet<>();
        for (String info : stationInfo) {
            String[] infos = info.split(",");
            hashSet.add(infos[0]);
        }
        Integer stationCount = stationInfo.size();
        List<String> checkInfo = checkedRepo.findAllByStationResult();
        Integer checkCount = 0;
        for (String info : checkInfo) {
            if (hashSet.contains(info))
                checkCount++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String checkRate = "0%";
        if (stationCount != 0)
            checkRate = df.format(((double) checkCount / (double) stationCount) * 100.000) + '%';
        List<String> alertInfo = alertMessageDataRepo.findAllBySAlertResult();
        Set<String> set = new HashSet<>(alertInfo);
        alertInfo = new ArrayList<>(set);
        Integer alertCount = 0;
        for (String info : alertInfo) {
            if (hashSet.contains(info))
                alertCount++;
        }
        String alertRate = "0%";
        if (stationCount != 0)
            alertRate = df.format(((double) alertCount / (double) stationCount) * 100.000) + '%';
        TableData tableData = TableData.builder()
                .stationCount(stationCount)
                .checkCount(checkCount)
                .checkRate(checkRate)
                .faultCount(alertCount)
                .faultRate(alertRate)
                .build();
        List<TableData> list = new ArrayList<>();
        list.add(tableData);
        return JsonResult.successResult(list);
    }

    public JsonResult getVoltList(String stationType) {
        HashSet<String> hashSet = new HashSet<>();
        List<String> infos = null;
        if (stationType.equals("发电厂"))
        {
            infos = d5000yxRepo.finddfcAllList();
        }
        else
            infos = d5000yxRepo.findbdzAllList();
        for (String info : infos) {
            hashSet.add(info.split(",")[3]);
        }
        return JsonResult.successResult(new ArrayList<>(hashSet));
    }

    public JsonResult getNameData(String stationType, String voltage) {
        List<String> stationInfo = null;
        if (stationType.equals("发电厂"))
            stationInfo = d5000yxRepo.finddfcAllList();
        else if (stationType.equals("变电站")) {
            stationInfo = d5000yxRepo.findbdzAllList();
        }
        if (stationInfo != null) {
            stationInfo.removeIf(info -> !info.split(",")[3].equals(voltage));
        }
        List<TableData> list=new ArrayList<>();
        for (String infos : stationInfo) {
            String stationName=infos.split(",")[0];
            Boolean check = false;
            List<String> allByStationResult = checkedRepo.findAllByStationResult();
            for (String s : allByStationResult) {
                if (s.equals(stationName)) {
                    check = true;
                    break;
                }
            }
            Boolean fault = false;
            List<String> alertInfo = alertMessageDataRepo.findAllBySAlertResult();
            Set<String> set = new HashSet<>(alertInfo);
            alertInfo = new ArrayList<>(set);
            for (String s : alertInfo) {
                if (s.equals(stationName)) {
                    fault = true;
                    break;
                }
            }
            TableData tableData = TableData.builder()
                    .stationType(stationType)
                    .stationName(stationName)
                    .voltage(voltage)
                    .check(check)
                    .fault(fault)
                    .build();
            list.add(tableData);
        }
        return JsonResult.successResult(list);
    }


    @Transactional
    public TypeErrorVO matchTemplate(MultipartFile file, String name, Long tableId) {
        FileMapper.deleteByName(name);
        String type="";
        String temPath="";
        if(name.contains("变电")){
            type="变电站";
            temPath="src/main/resources/500千伏变电站远动和PMU典型信息表【江苏省调】.xls";
            String signal="遥信";
            List<String> matchData = getMatchData(temPath, 3, 6, 10);
            List<String> pointData = getMatchData(file, 3, 6, 10);
            List<TypeError> typeErrors = TypeMatchFunction(pointData, matchData, tableId, name, type,signal);
            FileMapper.insertBatch(typeErrors);
            signal="遥测";
            matchData = getMatchData(temPath, 4, 6, 9);
            pointData = getMatchData(file, 4, 6, 9);
            typeErrors = TypeMatchFunction(pointData, matchData, tableId, name, type,signal);
            FileMapper.insertBatch(typeErrors);
        }
        else if(name.contains("电厂")||name.contains("风电")||(name.contains("光伏"))){
            if(name.contains("光伏")) {
                type = "光伏";
                temPath="src/main/resources/厂区光伏典型信息表【江苏】.xls";
            }
            else if(name.contains("风电"))
            {
                type="风电";
                temPath="src/main/resources/风电场典型信息表【江苏】.xls";
            }
            else
            {
                type="发电站";
                temPath="src/main/resources/电厂（带储能）典型信息表【江苏】.xls";
            }
            String signal="遥信";
            List<String> matchData = getMatchData(temPath, 3, 6, 10);
            List<String> pointData = getMatchData(file, 3, 6, 10);
            List<TypeError> typeErrors = TypeMatchFunction(pointData, matchData, tableId, name, type,signal);
            FileMapper.insertBatch(typeErrors);
            signal="遥测";
            matchData = getMatchData(temPath, 4, 6, 9);
            pointData = getMatchData(file, 4, 6, 9);
            typeErrors = TypeMatchFunction(pointData, matchData, tableId, name, type,signal);
            FileMapper.insertBatch(typeErrors);
            signal="遥控";
            matchData = getMatchData(temPath, 5, 5, 9);
            pointData = getMatchData(file, 5, 5, 9);
            typeErrors = TypeMatchFunction(pointData, matchData, tableId, name, type,signal);
            FileMapper.insertBatch(typeErrors);
            signal="遥调";
            matchData = getMatchData(temPath, 6, 3, 9);
            pointData = getMatchData(file, 6, 3, 9);
            typeErrors = TypeMatchFunction(pointData, matchData, tableId, name, type,signal);
            FileMapper.insertBatch(typeErrors);
        }
        else{
            //未匹配到类型
//            System.out.println("校验失败，未匹配到点表类型");
            return TypeErrorVO.builder().res(false).build();
        }
        return TypeErrorVO.builder().res(true).list(FileMapper.getByName(name)).build();
    }

    public List<TypeError> TypeMatchFunction(List<String> pointData1, List<String> matchData1, Long tableId, String name, String type,String signal){
        List<TypeError> result=new ArrayList<>();
        boolean[] matchData1Record=new boolean[matchData1.size()];
        for (int i = 0; i < pointData1.size(); i++) {
            String point=pointData1.get(i).replaceAll("[0-9]","X").replaceAll("X{2,}", "X");
            boolean success=false;
            double maxSim=0;
            String maxMatch="";
            String maxOrigin="";
            int maxIndex=0;
            for (int j = 0; j < matchData1.size(); j++) {
                String match=matchData1.get(j).replaceAll("X{2,}", "X");
                double similarity= Similarity.phraseSimilarity(match,point);
                if(similarity>maxSim){
                    maxSim=similarity;
                    maxMatch=match;
                    maxOrigin=matchData1.get(j);
                    maxIndex=j;
                }
            }
            if(fenciFunction3(maxMatch,point,maxSim)){
                success=true;
                TypeError typeError;
                if(equal(point,maxMatch,maxSim)){
                    typeError = TypeError.builder()
                            .tableId(tableId)
                            .tableName(name)
                            .type(type)
                            .signal(signal)
                            .index((long) i)
                            .data(pointData1.get(i))
                            .result("正常")
                            .template(maxOrigin)
                            .templateIndex((long)maxIndex)
                            .build();
                }
                else{
                    typeError = TypeError.builder()
                            .tableId(tableId)
                            .tableName(name)
                            .type(type)
                            .signal(signal)
                            .index((long) i)
                            .data(pointData1.get(i))
                            .result("异常")
                            .reason("不规范的名称")
                            .templateIndex((long) maxIndex)
                            .template(maxOrigin)
                            .build();
                }
                result.add(typeError);
                matchData1Record[maxIndex]=true;
            }
            if(!success){
                TypeError typeError = TypeError.builder()
                        .tableId(tableId)
                        .tableName(name)
                        .type(type)
                        .signal(signal)
                        .index((long) i)
                        .data(pointData1.get(i))
                        .result("异常")
                        .reason("未匹配到对应规范")
                        .templateIndex((long)-1)
                        .build();
                result.add(typeError);
            }
        }
        for (int i = 0; i < matchData1Record.length; i++) {
            if(!matchData1Record[i]){
                TypeError typeError = TypeError.builder()
                        .tableId(tableId)
                        .tableName(name)
                        .type(type)
                        .signal(signal)
                        .index((long) -1)
                        .result("异常")
                        .reason("规范未找到对应点")
                        .templateIndex((long)i)
                        .template(matchData1.get(i))
                        .build();
                result.add(typeError);
            }
        }
        return result;
    }

    public static boolean fenciFunction3(String s1,String  s2,double similarity){
        if(similarity>0.45){
            return true;
        }
        else{
            List<Term> segment1 = HanLP.segment(s1);
            List<Term> segment2 = HanLP.segment(s2);
            List<String> list1=new ArrayList<>(segment1.size());
            for (Term term : segment1) {
                String word = term.word;
                if(word.length()>1)
                    list1.add(word);
                if(word.length()>2&&word.charAt(word.length()-1)=='值')
                    list1.add(word.substring(0,word.length()-1));
            }
            int cnt=0;
            for (Term term : segment2) {
                String word = term.word;
                if(word.length()>1){
                    boolean b = false;
                    String t = null;
                    if(word.length()>2&&word.charAt(word.length()-1)=='值'){
                        b=true;
                        t=word.substring(0,word.length()-1);
                    }
                    for (String s : list1) {
                        if(s.contains(word)||word.contains(s)){
                            cnt++;
                            if(cnt==2)
                                return true;
                        }
                        else if(b&&(s.contains(t)||t.contains(s))){
                            cnt++;
                            if(cnt==2)
                                return true;
                        }
                    }
                }
            }
            HashSet<String> record1 = new HashSet<>(HanLP.extractKeyword(s2, s2.length()/2));
            list1= HanLP.extractKeyword(s1, s1.length() / 2);
            cnt=0;
            for (String s : list1) {
                if(s.length()>1&&record1.contains(s)){
                    cnt++;
                    if(cnt==2)
                        return true;
                }
            }
        }
        return false;
    }
    public static List<String> getMatchData(MultipartFile file, int sheetIndex,int nameColumnIndex,int startRow){
        try {
            InputStream fis = file.getInputStream();
            String excelFilePath=file.getOriginalFilename();
            Workbook workbook;
            if (excelFilePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (excelFilePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("The specified file is not Excel file");
            }
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            List<String> data=new ArrayList<>(sheet.getLastRowNum()-startRow+1);
            for (int i=startRow;i<=sheet.getLastRowNum();i++) {
                Row row=sheet.getRow(i);
                if(row!=null){
                    Cell nameCell = row.getCell(nameColumnIndex);
                    if(nameCell!=null){
                        String name = nameCell.getStringCellValue();
                        if(!name.equals("备用")&& !name.isEmpty())
                            data.add(name);
                    }
                }
            }
            workbook.close();
            fis.close();

//            // 打印或处理dataMap
//            for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
//                System.out.println("点号: " + entry.getKey() + ", 名称: " + entry.getValue());
//            }
            return data;
        } catch (IOException e) {
        }
        return null;
    }
    public static List<String> getMatchData(String excelFilePath, int sheetIndex,int nameColumnIndex,int startRow){
        try {
            FileInputStream fis = new FileInputStream(excelFilePath);
            Workbook workbook;
            if (excelFilePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (excelFilePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("The specified file is not Excel file");
            }
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            List<String> data=new ArrayList<>(sheet.getLastRowNum()-startRow+1);
            for (int i=startRow;i<=sheet.getLastRowNum();i++) {
                Row row=sheet.getRow(i);
                Cell nameCell = row.getCell(nameColumnIndex);
                if(nameCell!=null){
                    String name = nameCell.getStringCellValue();
//                    if(sheetIndex==3){
//                        Cell cell=row.getCell(nameColumnIndex+1);
//                        if(cell!=null)
//                            name+=cell.getStringCellValue();
//                        cell=row.getCell(nameColumnIndex+2);
//                        if(cell!=null)
//                        {
//                            String value = cell.getStringCellValue();
//                            if(!value.equals("信号"))
//                                name+=value;
//                        }
//                    }
                    if(!name.equals("备用"))
                        data.add(name);
                }
            }
            workbook.close();
            fis.close();

//            // 打印或处理dataMap
//            for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
//                System.out.println("点号: " + entry.getKey() + ", 名称: " + entry.getValue());
//            }
            return data;
        } catch (IOException e) {
        }
        return null;
    }

    public boolean equal(String point,String template,double sim){
        if(point.equals(template))
            return true;
        else if(point.contains(template)&&template.length()>=point.length()*0.6)
            return true;
        else if(template.contains(point)&&point.length()>=template.length()*0.6)
            return true;
        else return sim > 0.55;
    }


    public List<Xyd_Alert_Records> getChangeDetail(String stationName, Date checkTime) {
        List<Xyd_Alert_Records> changeDetail = FileMapper.getChangeDetail(stationName, checkTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Xyd_Alert_Records xydAlertRecords : changeDetail) {
            xydAlertRecords.setTime(dateFormat.format(xydAlertRecords.getChecktime()));
        }
        return changeDetail;
    }
}

