package com.example.demo.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class JsonResult {
    private int code;
    private long count;
    private String msg;
    private List<?> data;
    private Object item;
    public JsonResult() {
    }

    public JsonResult(int code, Long count, String msg, List<?> data, Object item) {
        this.code = code;
        this.count = count;
        this.msg = msg;
        this.data = data;
        this.item = item;
    }

    // 构造成功返回体
    public static JsonResult successResult(){
        return new JsonResult(0, 0L, "ok", new ArrayList<>(), null);
    }
    public static JsonResult successResult(Long count){
        return new JsonResult(0, count, "ok", new ArrayList<>(), null);
    }
    public static JsonResult successResult(int count){
        return new JsonResult(0, (long) count, "ok", new ArrayList<>(), null);
    }
    public static JsonResult successResult(List<?> data){
        return new JsonResult(0, (long) data.size(), "ok", data, null);
    }
    public static JsonResult successResult(String stringData){
        List<String> data = new ArrayList<>();
        data.add(stringData);
        return new JsonResult(0, (long) data.size(), "ok", data, null);
    }

    public static JsonResult successResult1(String stringData){
        List<String> data = new ArrayList<>();
        data.add(stringData);
        return new JsonResult(0, (long) data.size(), stringData, data, null);
    }

    public static JsonResult successResult(Map<String, ?> map){
        List<Map<String, ?>> data = new ArrayList<>();
        data.add(map);
        return new JsonResult(0, (long) data.size(), "ok", data, null);
    }

/*    public static JsonResult successResult(Set<String> set){
        List<Set<String>> data = new ArrayList<>();
        data.add(set);
        return new JsonResult(0, (long) data.size(), "ok", data, null);
    }*/

    public static JsonResult successResult(Set<?> set){
        List<Set<?>> data = new ArrayList<>();
        data.add(set);
        return new JsonResult(0, (long) data.size(), "ok", data, null);
    }

    public static JsonResult successResult(List<?> data, Map<String, ?> map){
        return new JsonResult(0, (long) data.size(), "ok", data, map);
    }

    // 构造失败的返回体
    public static JsonResult failureResult(String msg){
        return new JsonResult(-1, 0L, msg, new ArrayList<>(), null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
