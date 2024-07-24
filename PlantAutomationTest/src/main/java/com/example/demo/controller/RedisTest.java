package com.example.demo.controller;
import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args){

        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");

        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());
        // 获取存储的数据并输出

        //设置 redis 字符串数据
        jedis.set("site", "test.com");

        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("site"));
    }
}
