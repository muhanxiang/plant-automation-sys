<template>
  <div style="height: 48vh">
    <sy-chart ref="chart" type="line" :option="lineOption"></sy-chart>
  </div>
</template>

<script>
let color = {
  type: 'linear',
  x: 0,
  y: 0,
  x2: 0,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: 'rgba(104, 183, 250, 0.75)', // 0% 处的颜色
    },
    {
      offset: 1,
      color: 'rgba(5, 240, 255, 0.19)', // 100% 处的颜色
    },
  ],
  globalCoord: false, // 缺省为 false
};
export default {
  name: 'EnvChart',
  props: {
    type: {
      type: String,
      default: 'line',
    },
  },
  data() {
    return {
      lineOption: {
        grid: {
          left: '7%',
          right: '10%',
          top: '4%',
          bottom: 10,
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          axisLabel: {
            show: true,
            fontSize: 12,
            color: '#B7CFFC',
          },
        },
        yAxis: {
          type: 'value',
          boundaryGap: false,
          axisLabel: {
            show: true,
            fontSize: 12,
            color: '#B7CFFC',
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: 'rgba(62, 82, 99, .8)',
            },
          },
        },
        series: [
          {
            type: 'line',
            smooth: 0.6,
            symbol: 'none',
            lineStyle: {
              color: '#00CBFF',
              width: 2,
            },
            areaStyle: {
              color: color,
            },
            data: [
              ['0:00', 2],
              ['3:00', 5],
              ['6:00', 10],
              ['9:00', 58],
              ['12:00', 50],
              ['15:00', 67],
              ['18:00', 45],
              ['21:00', 3],
              ['24:00', 1],
            ],
          },
        ],
        // series: seriesOption,
      },
    };
  },
  mounted() {},
};
</script>

<style scoped></style>
