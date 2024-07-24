package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("TypeError")
@Builder
public class TypeError {
    Long id;
    Long tableId;
    String tableName;
    String type;
    String signal;
    Long index;
    String data;
    String result;
    String reason="";
    Long templateIndex;
    String template="";
}
