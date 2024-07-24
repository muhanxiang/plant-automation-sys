<template>
  <v-chart
    class="sy-chart"
    ref="chart"
    :theme="theme"
    :options="chartOption"
    :manual-update="manualUpdate"
    autoresize
  ></v-chart>
</template>

<script>
import { typeOf } from '@/utils';
import VChart from 'vue-echarts';
import theme from '@/plugins/echarts/theme.json';
import 'echarts/lib/chart/bar';
import 'echarts/lib/chart/pie';
import 'echarts/lib/chart/line';
import 'echarts/lib/chart/funnel';
import 'echarts/lib/component/legend';
import 'echarts/lib/component/tooltip';

VChart.registerTheme('echarts', theme);

export default {
  name: 'SgChart',
  components: {
    VChart,
  },
  props: {
    type: {
      type: String,
      default: 'bar',
    },
    theme: {
      type: String,
      default: 'echarts',
    },
    option: {
      type: Object,
      default() {
        return {};
      },
    },
    initOption: {
      type: Object,
      default() {
        return {};
      },
    },
    updateOption: {
      type: Object,
      default() {
        return {};
      },
    },
    manualUpdate: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      barOption: {
        grid: {
          top: '15%',
          left: '3%',
          right: '3%',
          bottom: 0,
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          axisLine: { show: true },
          axisTick: { show: false },
          axisLabel: { interval: 0 },
          splitLine: { show: false },
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          axisTick: {
            show: true,
            lineStyle: { color: '#03CEFB' },
          },
          axisLabel: {
            show: true,
            margin: 10,
            color: '#004F95',
            fontSize: 16,
            fontStyle: 'italic',
            fontFamily: 'DS-DIGIB',
          },
          splitLine: { show: false },
        },
        legend: {
          show: true,
          top: 'top',
          left: 'center',
          itemGap: 10,
          itemWidth: 12,
          itemHeight: 6,
        },
        tooltip: {
          show: true,
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          borderColor: 'rgba(255,255,255,.3)',
          backgroundColor: 'rgba(13,5,30,.6)',
          borderWidth: 1,
          padding: 5,
        },
        series: [],
      },
      pieOption: {
        legend: {
          show: true,
          top: 'middle',
          right: '5%',
          orient: 'vertical',
          itemGap: 10,
          itemWidth: 12,
          itemHeight: 6,
        },
        tooltip: {
          show: true,
          trigger: 'item',
        },
        series: [],
      },
      lineOption: {
        grid: {
          top: '15%',
          left: '3%',
          right: '3%',
          bottom: 0,
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          axisLine: { show: true },
          axisTick: { show: false },
          axisLabel: { interval: 0 },
          splitLine: { show: false },
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: { show: true },
          splitLine: { show: false },
        },
        legend: {
          show: true,
          top: 'top',
          left: 'center',
          itemGap: 10,
          itemWidth: 12,
          itemHeight: 6,
        },
        tooltip: {
          show: true,
          trigger: 'axis',
        },
        series: [],
      },
      funnelOption: {
        legend: {
          show: true,
          top: 'top',
          left: 'center',
          itemGap: 20,
          itemWidth: 10,
          itemHeight: 10,
          icon: 'circle',
        },
        tooltip: { show: true, trigger: 'item' },
        series: [],
      },
    };
  },
  computed: {
    chartOption() {
      let chartOption = null;
      switch (this.type) {
        case 'bar':
          chartOption = this.barOption;
          break;
        case 'pie':
          chartOption = this.pieOption;
          break;
        case 'line':
          chartOption = this.lineOption;
          break;
        case 'funnel':
          chartOption = this.funnelOption;
          break;
        default:
          break;
      }
      Object.keys(this.option).forEach((key) => {
        if (chartOption[key] && typeOf(chartOption[key]) === 'Object' && typeOf(this.option[key]) === 'Object') {
          chartOption[key] = {
            ...chartOption[key],
            ...this.option[key],
          };
        } else {
          chartOption[key] = this.option[key];
        }
      });
      return chartOption;
    },
    initOptions() {
      return {
        devicePixelRatio: window.devicePixelRatio,
        renderer: 'canvas',
        useDirtyRect: false, // 从 `v5.0.0` 开始支持
        // width?: number|string,
        // height?: number|string,
        // locale?: string
        ...this.initOption,
      };
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-chart {
  width: 100%;
  height: 100%;
}
</style>
