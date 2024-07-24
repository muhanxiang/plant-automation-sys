package com.example.demo.controller;

import com.example.demo.entity.ExcelData;
import com.example.demo.entity.JsonResult;
import com.example.demo.service.FileEqualService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("scms")
public class FileEqualController {

    @Autowired
    FileEqualService fileEqualService;

    @GetMapping("getFileEqual")
    public JsonResult getMesssage(){
        return fileEqualService.FileEqual();
    }

    @PostMapping("uploadExcel")
    public void uploadImg(@RequestParam MultipartFile file,@RequestParam(defaultValue = "123") String name) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            Date CheckDate = new Date(currentTimeMillis);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ttt = dateFormat1.format(CheckDate);

            String fileName = file.getOriginalFilename();
            String sta_name = name;
            // 处理 Excel 文件
            fileEqualService.parseExcelFile(file,sta_name,ttt);

            String targetPath = "F:/home/File/inExcel/" + fileName;
            File targetFile = new File(targetPath);

            file.transferTo(targetFile);

            //return "文件上传成功";
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            //return "文件上传失败";
        }
    }

}