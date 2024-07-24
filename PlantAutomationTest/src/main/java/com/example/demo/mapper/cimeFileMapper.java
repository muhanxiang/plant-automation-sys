package com.example.demo.mapper;

import com.example.demo.entity.CimeFile;
import com.example.demo.entity.TypeError;
import com.example.demo.entity.Xyd_Alert_Records;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface cimeFileMapper {
    @Select("select * from cime_file where path='/upload'")
    public List<CimeFile> selectList();

    @Insert("insert into cime_file (fileName, createTime, path) VALUES (#{fileName},#{createTime},#{path})")
    public void insert(CimeFile cimeFile);

    public void insertBatch(List<TypeError> list);

    @Delete("delete from typeerror where tableName=#{tableName}")
    public void deleteByName(String tableName);

    @Select("select * from typeerror where tableName=#{tableName}")
    public List<TypeError> getByName(String tableName);

    @Select("select * from xyd_alert_records where station_name=#{stationName} and checktime=#{checkTime}")
    public List<Xyd_Alert_Records> getChangeDetail(String stationName, Date checkTime);
}
