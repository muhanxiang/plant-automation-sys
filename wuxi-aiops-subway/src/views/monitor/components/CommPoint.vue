<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium"
    :title="title"
    :visible.sync="isVisible"
    center
    width="500"
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-row class="sy-row">
      <el-col :span="16" class="sy-col">
        <div class="signal-left" v-loading="chartLoad">
          <sy-chart v-if="chartData && chartData.length > 0" ref="chart" type="line" :option="chartOption"></sy-chart>
          <sy-empty v-else-if="!chartLoad"></sy-empty>
        </div>
      </el-col>
      <el-col :span="8" class="sy-col">
        <div class="signal-right">
          <div class="signal-right-item signal-right-time">
            <div class="signal-right-item__title">时间范围</div>
            <div class="signal-right-item__content">
              <el-date-picker
                v-model="startTime"
                type="datetime"
                placeholder="选择开始时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                default-time="00:00:00"
                :picker-options="startTimeOptions"
                clearable
                @change="handleTimeChange"
              >
              </el-date-picker>
              <el-date-picker
                v-model="endTime"
                type="datetime"
                placeholder="选择结束时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                default-time="23:59:59"
                :picker-options="endTimeOptions"
                clearable
                @change="handleTimeChange"
              >
              </el-date-picker>
            </div>
          </div>
          <div class="signal-right-item">
            <div class="signal-right-item__title">历史值</div>
            <div class="signal-right-item__content">
              <div class="desc-item">
                <div class="desc-item__cell">平均值</div>
                <div class="desc-item__cell value">{{ avgValue }}</div>
              </div>
              <div class="desc-item">
                <div class="desc-item__cell">最大值</div>
                <div class="desc-item__cell value">{{ maxValue }}</div>
              </div>
              <div class="desc-item">
                <div class="desc-item__cell">最小值</div>
                <div class="desc-item__cell value">{{ minValue }}</div>
              </div>
            </div>
          </div>
          <div class="signal-right-item">
            <div class="signal-right-item__title">实时值</div>
            <div class="signal-right-item__content">
              <div class="desc-item">
                <div class="desc-item__cell">当前值</div>
                <div class="desc-item__cell value">{{ curtValue }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
export default {
  name: 'CommPoint',
  props: {
    guid: {
      type: String,
      default: '',
    },
    title: {
      type: String,
      default: '遥信点位名称',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    precision: {
      type: Number,
      default: 1,
    },
  },
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit('update:visible', val);
      },
    },
    valuePrec() {
      return Number.isInteger(this.precision) ? this.precision : 1;
    },
    startTimeOptions() {
      return {
        disabledDate: (date) => {
          if (!this.endTime && this.$dayjs().isAfter(date, 'minute')) return false;
          if (
            this.endTime &&
            this.$dayjs(date).isBefore(this.endTime, 'minute') &&
            this.$dayjs().isAfter(date, 'minute')
          )
            return false;
          return true;
        },
      };
    },
    endTimeOptions() {
      return {
        disabledDate: (date) => {
          if (!this.startTime && this.$dayjs().isAfter(date, 'minute')) return false;
          if (
            this.startTime &&
            this.$dayjs(date).isAfter(this.startTime, 'minute') &&
            this.$dayjs().isAfter(date, 'minute')
          )
            return false;
          return true;
        },
      };
    },
    chartOption() {
      return {
        grid: {
          top: '5%',
          bottom: '10%',
        },
        xAxis: {
          axisLabel: {
            interval: 'auto',
          },
        },
        tooltip: {
          axisPointer: { type: 'shadow' },
          formatter: (params) => {
            const item = params[0];
            return `
                <div>${this.$dayjs(item.data.time).format('YYYY-MM-DD HH:mm:ss')}</div>
                <div style="margin-top: 10px">
                  <span style="margin-right: 20px">${item.marker}${item.seriesName}</span>
                  <span>${item.data.value_disp || '-'}</span>
                </div>
            `;
          },
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 75,
            minSpan: 25,
            maxSpan: 100,
          },
          {
            type: 'slider',
            left: '5%',
            right: '5%',
            bottom: '3%',
            height: '5%',
            start: 0,
            end: 75,
            minSpan: 25,
            maxSpan: 100,
            textStyle: {
              overflow: 'break',
            },
          },
        ],
        dataset: {
          source: [...this.chartData].map((item) => ({
            name: this.$dayjs(item.time).format('YYYY-MM-DD\nHH:mm:ss'),
            time: item.time,
            value: item.value,
            value_disp: item.value_disp || '-',
          })),
        },
        series: [
          {
            type: 'line',
            name: '遥信点位值',
            symbol: 'circle',
            symbolSize: 5,
            smooth: true,
            areaStyle: {
              opacity: 0.5,
            },
            itemStyle: {
              normal: {
                lineStyle: {
                  width: 1, //设置线条粗细
                },
              },
            },
            encode: {
              x: 'name',
              y: 'value',
            },
          },
        ],
      };
    },
  },
  data() {
    return {
      chartLoad: false,
      chartData: [],
      startTime: '',
      endTime: '',
      avgValue: '',
      maxValue: '',
      minValue: '',
      curtValue: '',
    };
  },
  methods: {
    handleTimeChange() {
      this.getHistoryData();
    },
    onDialogOpen() {
      this.getHistoryData();
      this.getRealTimeData();
    },
    onDialogClose() {
      this.chartData = [];
      this.startTime = '';
      this.endTime = '';
      this.avgValue = '';
      this.maxValue = '';
      this.minValue = '';
      this.curtValue = '';
    },
    getHistoryData() {
      if (!this.guid) return;

      this.chartLoad = true;
      this.$api
        .getMeasureApi('query/commpoint', {
          id: this.guid || undefined,
          end: this.endTime ? this.$dayjs(this.endTime).format() : undefined,
          start: this.startTime ? this.$dayjs(this.startTime).format() : undefined,
          samples: 100,
        })
        .then((res) => {
          if (res?.results?.length > 0) {
            this.chartData = res.results.reverse();

            const commpoint = res.results[0];
            if (/\d+/.test(commpoint.value_disp)) {
              const unitVal = commpoint.value_disp.match(/[-\d.]+(.*)$/)[1];

              let maxVal,
                minVal,
                totalVal = 0;
              const length = res.results.length;
              for (let index = 0; index < length; index++) {
                const { value } = res.results[index];
                const numVal = +value || 0;
                // console.log('numVal: ', numVal);
                totalVal += numVal;
                if (maxVal === undefined || maxVal < numVal) maxVal = numVal;
                if (minVal === undefined || minVal > numVal) minVal = numVal;
              }
              this.maxValue = `${maxVal.toFixed(this.valuePrec)}${unitVal}`;
              this.minValue = `${minVal.toFixed(this.valuePrec)}${unitVal}`;
              this.avgValue = `${(totalVal / length).toFixed(this.valuePrec)}${unitVal}`;
            } else {
              this.avgValue = '-';
              this.maxValue = '-';
              this.minValue = '-';
            }
          } else {
            this.chartData = [];
            this.avgValue = '-';
            this.maxValue = '-';
            this.minValue = '-';
          }
        })
        .finally(() => {
          this.chartLoad = false;
        });
    },
    getRealTimeData() {
      if (!this.guid) return;
      this.$api
        .getMeasureApi('query/commpoint', {
          id: this.guid || undefined,
        })
        .then((res) => {
          // console.log('res: ', res);
          if (res?.results?.length > 0) {
            const commpoint = res.results[0];
            if (/\d+/.test(commpoint.value_disp)) {
              try {
                const unitVal = commpoint.value_disp.match(/[-\d.]+(.*)$/)[1];
                this.curtValue = `${(+commpoint.value).toFixed(this.valuePrec)}${unitVal}`;
              } catch (error) {
                this.curtValue = commpoint.value_disp;
              }
            } else {
              this.curtValue = commpoint.value_disp;
            }
          } else {
            this.curtValue = '-';
          }
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.signal {
  &-left {
    height: 100%;
    border: $border-default;
    padding-top: 10px;
  }
  &-right {
    display: flex;
    flex-direction: column;
    height: 100%;
    border: $border-default;

    &-time {
      flex: auto;
    }
    &-item {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;

      &__title {
        width: 100%;
        height: 5vh;
        font-size: 18px;
        font-weight: 600;
        line-height: 5vh;
        text-align: center;
        color: $color-primary;
        background-color: $bg-label;
      }
      &__content {
        display: flex;
        align-items: center;
        flex-direction: column;
        justify-content: center;
        flex: auto;
        width: 100%;

        > .el-date-editor {
          width: 250px;

          + .el-date-editor {
            margin-top: 2vh;
          }
        }

        .desc {
          &-item {
            display: flex;
            width: 100%;
            &__cell {
              color: $text-secondary;
              border-bottom: $border-default;
              width: 50%;
              height: 6vh;
              line-height: 6vh;
              text-align: center;
              &:first-of-type {
                border-right: $border-default;
              }
            }
            .value {
              color: $color-white;
            }
            .status {
              color: $color-primary;
            }
          }
        }
      }
    }
  }
}
</style>
