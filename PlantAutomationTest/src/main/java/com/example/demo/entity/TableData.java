package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableData {
    private String stationType;
    private String voltage;
    private String stationName;
    private Integer stationCount;
    private Integer checkCount;
    private String checkRate;
    private Integer faultCount;
    private String faultRate;
    private Boolean check;
    private Boolean fault;
}
