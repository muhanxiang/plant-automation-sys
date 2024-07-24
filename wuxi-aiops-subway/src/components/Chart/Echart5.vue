<template>
  <v-chart
    class="sy-chart"
    ref="chart"
    :theme="theme"
    :option="chartOption"
    :loading="loading"
    :manual-update="manualUpdate"
    autoresize
    @click="onClick"
    @dblclick="onDblclick"
    @mouseup="onMouseUp"
    @mouseout="onMouseOut"
    @mousedown="onMouseDown"
    @mousemove="onMouseMove"
    @mouseover="onMouseOver"
    @globalout="onGlobalOut"
  ></v-chart>
</template>

<script>
import '@/plugins/echarts';
import { typeOf } from '@/utils';
import VChart from 'vue-echarts';

export default {
  name: 'SyChart',
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
    group: {
      type: String,
      default: 'default',
    },
    option: {
      type: Object,
      default() {
        return {};
      },
    },
    loading: {
      type: Boolean,
      default: false,
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
      pieOption: {
        legend: {
          show: true,
          top: 'middle',
          right: '5%',
          orient: 'vertical',
          itemGap: 10,
          itemWidth: 12,
          itemHeight: 6,
          textStyle: {
            fontSize: 14,
          },
        },
        tooltip: {
          show: true,
          trigger: 'item',
          backgroundColor: '#02182cd8',
          borderColor: '#035fa9',
          borderWidth: 1,
          textStyle: {
            color: '#fff',
            fontSize: 14,
          },
        },
        series: [],
      },
      barOption: {
        grid: {
          top: '15%',
          left: '3%',
          right: '3%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          axisLine: { show: true },
          axisTick: { show: false },
          axisLabel: { interval: 0, fontSize: 14 },
          splitLine: { show: false },
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          axisTick: {
            show: true,
            lineStyle: { color: '#01aff2' },
          },
          axisLabel: {
            show: true,
            margin: 10,
            color: '#004f95',
            fontSize: 18,
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
          textStyle: {
            fontSize: 14,
          },
        },
        tooltip: {
          show: true,
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            lineStyle: { color: '#01aff2' },
          },
          backgroundColor: '#02182cd8',
          borderColor: '#035fa9',
          borderWidth: 1,
          textStyle: {
            color: '#fff',
            fontSize: 14,
          },
        },
        series: [],
      },
      lineOption: {
        grid: {
          top: '15%',
          left: '3%',
          right: '3%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          axisLine: { show: true },
          axisTick: { show: false },
          axisLabel: { interval: 0, fontSize: 14 },
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed',
              color: '#b7cffc',
              opacity: 0.5,
            },
          },
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          axisTick: {
            show: true,
            lineStyle: { color: '#01aff2' },
          },
          axisLabel: {
            show: true,
            margin: 10,
            fontSize: 18,
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
          itemWidth: 20,
          itemHeight: 10,
          textStyle: {
            fontSize: 14,
          },
        },
        tooltip: {
          show: true,
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            lineStyle: { color: '#01aff2' },
          },
          backgroundColor: '#02182cd8',
          borderColor: '#035fa9',
          borderWidth: 1,
          textStyle: {
            color: '#fff',
            fontSize: 14,
          },
        },
        dataZoom: [
          {
            type: 'inside',
            show: true,
            xAxisIndex: [0],
            start: 0,
            end: 50,
          },
        ],
        series: [],
      },
      funnelOption: {
        legend: {
          show: true,
          top: 'top',
          left: 'center',
          icon: 'circle',
          itemGap: 10,
          itemWidth: 10,
          itemHeight: 10,
          textStyle: {
            fontSize: 14,
          },
        },
        tooltip: {
          show: true,
          trigger: 'item',
          backgroundColor: '#02182cd8',
          borderColor: '#035fa9',
          borderWidth: 1,
          textStyle: {
            color: '#fff',
            fontSize: 14,
          },
        },
        series: [],
      },
    };
  },
  computed: {
    echart() {
      return this.$refs.chart && this.$refs.chart.chart;
    },
    zrender() {
      return this.$refs.chart && this.$refs.chart.chart.getZr();
    },
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
      if (!chartOption) return this.option;
      let baseOption = this.option.baseOption || this.option;
      Object.keys(baseOption).forEach((key) => {
        if (chartOption[key] && typeOf(chartOption[key]) === 'Object' && typeOf(baseOption[key]) === 'Object') {
          this.mergeOption(chartOption[key], baseOption[key]);
          chartOption[key] = {
            ...chartOption[key],
            ...baseOption[key],
          };
        } else {
          chartOption[key] = baseOption[key];
        }
      });
      if (this.option.baseOption) {
        return { ...this.option, baseOption: chartOption };
      } else {
        return chartOption;
      }
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
    updateOptions() {
      return {
        silent: false,
        notMerge: false,
        lazyUpdate: false,
        replaceMerge: [],
        ...this.updateOption,
      };
    },
  },
  methods: {
    onClick(opt) {
      // console.log('onClick:', opt);
      this.$emit('click', opt);
    },
    onDblclick(opt) {
      this.$emit('dblclick', opt);
    },
    onMouseUp(opt) {
      // console.log('onMouseUp:', opt);
      this.$emit('mouseup', opt);
    },
    onMouseOut(opt) {
      // console.log('onMouseOut:', opt);
      this.$emit('mouseout', opt);
    },
    onMouseDown(opt) {
      // console.log('onMouseDown:', opt);
      this.$emit('mousedown', opt);
    },
    onMouseMove(opt) {
      // console.log('onMouseMove:', opt);
      this.$emit('mousemove', opt);
    },
    onMouseOver(opt) {
      // console.log('onMouseOver:', opt);
      this.$emit('mouseover', opt);
    },
    onGlobalOut(opt) {
      // console.log('onGlobalOut:', opt);
      this.$emit('globalout', opt);
    },
    bindZRenderEvent() {
      if (this.echart && this.zrender) {
        this.zrender.on('click', (opt) => {
          // console.log('zrclick: ', opt);
          const { offsetX, offsetY, target, topTarget } = opt;
          const pixelPoint = [offsetX, offsetY];
          const isContained = this.echart.containPixel('grid', pixelPoint);
          if (isContained) {
            const gridPoint = this.echart.convertFromPixel('grid', pixelPoint);
            // console.log('convertFromPixel: ', gridPoint);
            if (Array.isArray(gridPoint)) {
              const dataIndex = gridPoint[0];
              this.$emit('zrclick', { dataIndex, target, topTarget });
            }
          }
        });
        // this.zrender.on('mousemove', (opt) => {
        //   console.log('mousemove: ', opt);
        //   const { offsetX, offsetY } = opt;
        //   const pixelPoint = [offsetX, offsetY];
        //   const isContained = this.echart.containPixel('series', pixelPoint);
        //   if (isContained) {
        //     this.zrender.setCursorStyle('pointer');
        //   }
        // });
        this.$once('hook:beforeDestroy', () => {
          this.zrender.off('click');
        });
      }
    },
    mergeOption(source, target) {
      // console.log('before: ', source, target);
      if (source !== undefined) {
        if (typeOf(source) === 'Object' && typeOf(target) === 'Object') {
          Object.keys(target).forEach((key) => {
            this.mergeOption(source[key], target[key]);
          });
          source = { ...source, ...target };
        } else {
          source = target;
        }
      }
      // console.log('after: ', source, target);
    },
  },
  mounted() {
    this.bindZRenderEvent();
  },
};
</script>

<style lang="scss" scoped>
.sy-chart {
  width: 100%;
  height: 100%;
}
</style>
