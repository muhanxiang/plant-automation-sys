package com.example.demo;

import cn.hutool.crypto.digest.DigestUtil;
import java.io.*;
import java.util.*;

public class FileEqual {
     static int num=0;
    public static void main(String[] args) {
        List<String> result=EqualResult("F:/home/File/17","F:/home/File/19");
        String arrAll[];
        String abc;
        for(String a:result){
            arrAll=a.split(",");
            //System.out.println(arrAll);
            if(arrAll[0].equals("数量不同")){
                System.out.println("校验结果为：文件数量不同！");
            }
            else if(arrAll[0].equals("文件名称不同")){
                abc=arrAll[1]+" 和 "+arrAll[2]+" 文件名称不同";
                System.out.println("校验结果为：文件名称不同！");
            }
            else if(arrAll[0].contains("文件内容不同")) {
                abc=arrAll[1]+" 和 "+arrAll[2]+" 文件内容不同";
                System.out.println(arrAll[1]+" 和 "+arrAll[2]+" 文件内容不同");
            }
            else if(arrAll[0].contains("文件大小不同")){
                abc=arrAll[1]+" 和 "+arrAll[2]+" 文件内容不同";
                System.out.println(arrAll[1]+" 和 "+arrAll[2]+" 文件大小不同");
            }
            else{
                System.out.println("校验错误");
            }
        }
    }

    public static List<String> EqualResult(String filePath1, String filePath2) {
        List<String>result= new ArrayList<>();
        List<String>listAname=getFileNameList(filePath1);
        List<String>listBname=getFileNameList(filePath2);
        String path1 = filePath1;
        String path2 = filePath2;

        if(listAname.size()!=listBname.size()){
            result.add("数量不同"+","+listAname+","+listBname);
            //return result;
        }
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
                                        result.add("文件内容不同"+","+afile+","+bfile);
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
                    result.add("文件名称不同");
                    return result;
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

}
