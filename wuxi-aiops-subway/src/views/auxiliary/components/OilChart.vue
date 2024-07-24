<template>
  <div style="height: 37vh">
    <sy-chart ref="chart" type="line" :option="lineOption"></sy-chart>
  </div>
</template>

<script>
var color = ['#F60001', '#FF6600', '#FFCC00', '#28F2E6', '#00CBFF', '#8496B5', '#7556F9'];

export default {
  name: 'OilChart',
  props: {
    type: {
      type: String,
      default: 'line',
    },
  },
  data() {
    return {
      lineOption: {
        color: color,
        title: {
          show: false,
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(0,0,0,0.6)',
        },
        legend: {
          orient: 'vertical',
          left: 'auto',
          right: 10,
          top: 30,
          itemGap: 8,
          itemWidth: 5,
          itemHeight: 10,
          icon: 'rect',
          data: ['氧气', '一氧化碳', '二氧化碳', '甲烷', '乙烯', '乙炔', '碳水'],
          textStyle: {
            color: '#B7CFFC',
            fontSize: 14,
          },
        },
        grid: {
          left: '3%',
          right: '7%',
          bottom: 0,
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          show: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            show: false,
            fontSize: 12,
            color: '#B7CFFC',
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: 'rgba(62, 82, 99, .8)',
            },
          },
          axisLine: {
            show: false,
          },
        },
        series: [
          {
            name: '氧气',
            type: 'line',
            stack: '总量',
            data: [1, 2, 10, 4, 9, 5, 2],
          },
          {
            name: '一氧化碳',
            type: 'line',
            stack: '总量',
            data: [5, 10, 3, 2, 9, 4, 7],
          },
          {
            name: '二氧化碳',
            type: 'line',
            stack: '总量',
            data: [2, 7, 10, 8, 3, 6, 4],
          },
          {
            name: '甲烷',
            type: 'line',
            stack: '总量',
            data: [3, 8, 2, 6, 9, 1, 7],
          },
          {
            name: '乙烯',
            type: 'line',
            stack: '总量',
            data: [6, 3, 9, 1, 3, 10, 4],
          },
          {
            name: '乙炔',
            type: 'line',
            stack: '总量',
            data: [10, 2, 1, 9, 4, 10, 6],
          },
          {
            name: '碳水',
            type: 'line',
            stack: '总量',
            data: [8, 4, 7, 4, 9, 3, 10],
          },
        ],
        // series: seriesOption,
      },
    };
  },
};
</script>

<style scoped></style>
